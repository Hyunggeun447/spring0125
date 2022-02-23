package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne(OneToOne, ManyToOne) 성능 최적화
 * Order
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 *
 * 1. 양방향 연관관계때문에 무한루프
 * -> 한쪽에 @JsonIgnore
 *
 * 2. fetch = LAZY(지연로딩) 이므로 로딩시 프록시 가져옴(ByteBuddyInterceptor)
 * -> Hibernate5 module를 gradle에 라이브러리 추가하면 해결 가능
 * -> Main class에 @Bean Hibernate5Module를 추가해줘야한다.
 *
 * 3. 다시 보내보면 Null로 표시된 정보들이 있음
 * -> 이것 역시 지연로딩때문
 * -> hibernate5Module을 FORCE_LAZY_LOADING으로 하면 한번에 다 긁어오긴함
 * -> 1) 엔티티 직접 노출해서, 2) 성능상의 이유로 비추천
 *
 *
 * 4. 3번의 방법을 쓰고싶지 않을경우
 * -> order.get... .get... -> Lazy 강제 초기화 시키면 초기화시킨쪽을 DB에서 긁어옴
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }


    /**
     * SimpleOrderDto를 사용함으로써, 엔티티를 직접노출시키지 않아도 된다.
     * ResultApi를 사용해서 감싸서 반환하는게 좋음.
     */
    @GetMapping("/api/v2/simple-orders")
    public ResultApi ordersV2() {

         /*
         N+1 문제 발생
         첫번째 order 조회 쿼리 [1] -> N(=2) 개의 order가 조회됨
         N번의 회원조회 [2], N번의 주소조회를 함 [2]
         도합 [5]번의 쿼리가 조회됨 (1 + N + N) -> 성능이 안좋음 -> fetch join으로 바꿔야한다.
         */
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new ResultApi(collect.size(), collect);

    }

    /**
     * OrderMemberRepository에 findAllWithMemberDelivery 메서드를 만들었다.
     * join fetch를 통해 쿼리 한번에 order, member, delivery를 EAGER타입으로 한번에 긁어온다.
     * [N+1] -> [1] 로 효율성 up
     */
    @GetMapping("/api/v3/simple-orders")
    public ResultApi ordersV3() {

        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new ResultApi(collect.size(), collect);
    }

    /**
     * Select 절이 줄어들어 성능이 향상된다.
     * why? OrderRepository의 findOrderDtos로 jpql에서 new 명령어를 사용해서 바로 DTO로 값을 넣어서 가져왔기 때문.
     *
     * but v3보다 마냥 좋은가? NO
     * 성능은 향상되었으나, 재사용성이 적다.
     *
     * 재사용성을 생각하면 V3. 성능만을 생각하면 V4
     */

    @GetMapping("/api/v4/simple-orders")
    public ResultApi ordersV4() {
        List<OrderSimpleQueryDto> orderDtos = orderRepository.findOrderDtos();
        return new ResultApi(orderDtos.size(), orderDtos);

    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
