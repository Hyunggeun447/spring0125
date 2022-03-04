package com.store.chichi.restController;

import com.store.chichi.domain.Member;
import com.store.chichi.domain.order.OrderSearch;
import com.store.chichi.domain.order.OrderSearchDto;
import com.store.chichi.domain.order.OrderStatus;
import com.store.chichi.repository.memberRepository.MemberRepository;
import com.store.chichi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ControllerApi {

    private final OrderService orderService;

    private final MemberRepository memberRepository;


    @GetMapping("/orders/list/api")
    public List<OrderSearchDto> orderDtoListApi(OrderSearch condition) {
        List<OrderSearchDto> ordersDto = orderService.findOrdersDto(condition);
        return ordersDto;
    }
    @GetMapping("/orders/list/api/{loginName}&{status}")
    public List<OrderSearchDto> orderDtoListLoginNameAndStatusApi(@PathVariable("loginName") String loginName,
                                                                  @PathVariable("status") OrderStatus status) {

        OrderSearch condition = new OrderSearch();
        condition.setLoginName(loginName);
        condition.setOrderStatus(status);
        List<OrderSearchDto> ordersDto = orderService.findOrdersDto(condition);
        return ordersDto;
    }

    @GetMapping("/members/{loginName}")
    public Member findMember(@PathVariable("loginName") String loginName) {
        Member member = memberRepository.findByLoginName(loginName).get(0);
        return member;
    }
}
