package com.store.chichi.repository.orderRepository;

import com.store.chichi.domain.*;
import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.domain.item.Size;
import com.store.chichi.domain.order.Order;
import com.store.chichi.domain.order.OrderSearch;
import com.store.chichi.domain.order.OrderSearchDto;
import com.store.chichi.domain.order.OrderStatus;
import com.store.chichi.service.ItemService;
import com.store.chichi.service.MemberService;
import com.store.chichi.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.store.chichi.domain.QMember.member;
import static com.store.chichi.domain.QOrderItem.orderItem;
import static com.store.chichi.domain.item.QItem.item;
import static com.store.chichi.domain.order.QOrder.order;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryImplTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;

    @Test
    public void findByMemberNameAndOrderStatus() throws Exception {

        Member member = new Member("Kim", "Kim", "1234", "1", "1");
        memberService.join(member);

        Item item = new Shirt();
        item.setItemName("옷123");
        item.setPrice(20000);
        item.setStockQuantity(20);
        item.setItemSize(Size.XL);
        item.setItemColor(Color.RED);
        itemService.saveItem(item);

        Address address = new Address("seoul","123");

        orderService.order(member.getId(), item.getId(), 1, address);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setLoginName("Kim");
        orderSearch.setOrderStatus(OrderStatus.ORDER);

        List<Order> result = orderService.findOrders(orderSearch);
        List<OrderSearchDto> resultDto = orderRepository.findByLoginNameAndOrderStatusDto(orderSearch);

        System.out.println("result = " + result);
        System.out.println("result = " + result.get(0));
        System.out.println("result = " + result.get(0).getId());
        System.out.println("result = " + result.get(0).getMember().getLoginName());
        System.out.println("result = " + result.get(0).getStatus());
        System.out.println("result = " + result.get(0).getOrderItems());
        System.out.println("result = " + result.get(0).getOrderDate());

        System.out.println("resultDto = " + resultDto);
        System.out.println("resultDto = " + resultDto.get(0));
        System.out.println("resultDto = " + resultDto.get(0).getId());
        System.out.println("resultDto = " + resultDto.get(0).getLoginName());
        System.out.println("resultDto = " + resultDto.get(0).getStatus());
        System.out.println("resultDto = " + resultDto.get(0).getItemName());
        System.out.println("resultDto = " + resultDto.get(0).getOrderDate());


        //given

        //when

        //then

    }

    @Test
    public void findMemberByLoginName() throws Exception {

        //given
        Member member1 = new Member("kim1", "abc1", "1111", "eewr", "1234");
        Member member2 = new Member("kim2", "abc2", "1111", "eewr", "1234");

        Long join1 = memberService.join(member1);
        Long join2 = memberService.join(member2);
        //when

        Member abc1 = memberService.findByLoginName("abc1");


        //then

        assertThat(abc1).isEqualTo(member1);

    }

}