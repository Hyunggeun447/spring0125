package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
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

import static java.util.stream.Collectors.*;


/**
 * 컬렉션 조회 최적화 하기.
 * 일단 엔티티를 기본으로 하고(이유 : 페치조인이든, 라이브러리 설정 변경이든 수정이 가장 쉽다.)
 * 그럼에도 성능이 안나올경우 DTO방식으로
 * 단건조회면 v4가 좋으나 기본적으로 v5가 제일 좋다.
 * v6는 비추천(쿼리문은 1번에 끝나나 데이터 이동이 제일 많기때문에 코드복잡도에 비해 성능이 별로)
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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


    /**
     * 새로 OrderQueryRepository를 만듬.
     * ToOne은 그냥 join으로 직접 조회해서 orderQueryDto를 만들고,
     * loop 돌려서 orderQueryDto의 orderid로 직접 조회해서 orderItemQueryDto에 넣는다.
     *
     * 장점 : 코드가 단순. 특정 주문 한건만 조회하는거면(단건조회) 성능 제일 좋음.
     * 단점 : N+1문제 발생
     *
     */
    @GetMapping("/api/v4/orders")
    public ResultApi ordersV4() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos();
        return new ResultApi(orderQueryDtos.size(), orderQueryDtos);
    }


    /**
     * 쿼리문 = -> in 변환
     * orderitem의 map화 O(1)로 만들기
     * loop로 컬렉션 데이터 채우기
     *
     * 특징 : 코드 복잡. 여러 주문을 조회하는 것이면 이것을 사용해야함.
     * 장점 : N+1 문제 해결. 제일 좋은듯.
     *
     */
    @GetMapping("/api/v5/orders")
    public ResultApi ordersV5() {
        List<OrderQueryDto> result = orderQueryRepository.findAllByDto_optimization();

        return new ResultApi(result.size(), result);
    }

    /**
     * 1줄 쿼리로 요청 보내기.
     * 장점 : 쿼리 1번
     * 단점 : 페이징 불가능, 스팩도 안맞아서 변환작업 추가로 필요함(orderFlatDto -> orderQueryDto). 매우 높은 코드 복잡도
     * 변환작업은 참고문서를 찾아봐야함.
     * 비추천
     */
    @GetMapping("/api/v6/orders")
    public ResultApi ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> collect = flats.stream().collect(
                        groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                                mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(),
                        e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());

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
