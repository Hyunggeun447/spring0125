package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 컬렉션 조회 최적화 하기.
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public ResultApi ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
            }

        return new ResultApi(all.size(), all);
    }

    /**
     * order을 orderDto로 감싸주었는데 orderItem이 엔티티형식으로 나옴.
     * orderItem 역시 Dto로 깜싸서 나가야한다.
     * 그래서 orderItemDto를 만들어 보내주었음.
     *
     * 하지만 성능이 안나온다
     */

    @GetMapping("/api/v2/orders")
    public ResultApi ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<Object> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return new ResultApi(collect.size(), collect);
    }

    /**
     * fetch join 을 활용하여 쿼리 요청을 한번만 보낼 수 있게 했으나,
     * order값이 곱연산되어 조회된다. (컬렉션 조회)
     *
     * 해결 : 중복제거를 위해서 쿼리문에 distinct 사용
     * -> 원래 DB에서는 distinct는 column값까지 완벽하게 같아야 중복이 제거된다. (실제로 log에 명령어를 db에 써도 테이블결과가 크게 달라지진 않음.)
     * but jpql에서는 엔티티가 중복일 경우에도 제거를 해주기 때문에 성능이 향상된다.
     *
     * 단점 : 페이징 불가능.
     * 주의점 : collection fetch join 은 1개만 하자.
     */
    @GetMapping("/api/v3/orders")
    public ResultApi ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream().map(order -> new OrderDto(order))
                .collect(Collectors.toList());
        return new ResultApi(collect.size(), collect);
    }

    /**
     * 1) ...ToOne 관계는 fetch join 사용
     *
     * application.yml > spring.jpa.properties.hibernate.default_batch_fetch_size: 100 설정.(100 은 인 쿼리의 개수)
     * 사이즈 설정이 중요(맥시멈 1000개 정도로 잡으면 됌). 대략 100~1000
     * v3에 비해 쿼리 요청문은 많이나가나,데이터 전송량이 최적화되어 성능이 때론 좋을 수도 있다.
     *
     * 페이징이 가능해지는 장점이 있다.
     *
     * detail하게 적용하고 싶어지면 class나 객체에 @BatchSize(size=100) 을 입력해주면 된다. (비추)
     *
     */
    @GetMapping("/api/v3.1/orders")
    public ResultApi ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                   @RequestParam(value = "limit", defaultValue = "100") int limit){

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit); // ToOne의 fetch join 가져옴

        List<OrderDto> collect = orders.stream().map(order -> new OrderDto(order))
                .collect(Collectors.toList());
        return new ResultApi(collect.size(), collect);
    }



    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name=order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
