package com.store.chichi.repository.orderRepository;

import com.store.chichi.domain.*;
import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.domain.item.Size;
import com.store.chichi.repository.OrderSearch;
import com.store.chichi.service.ItemService;
import com.store.chichi.service.MemberService;
import com.store.chichi.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class OrderRepositoryImplTest {

    @Autowired
    OrderService orderService;

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

        System.out.println("result = " + result);
        System.out.println("result = " + result.get(0));
        System.out.println("result = " + result.get(0).getId());
        System.out.println("result = " + result.get(0).getMember());
        System.out.println("result = " + result.get(0).getStatus());
        System.out.println("result = " + result.get(0).getOrderItems());
        System.out.println("result = " + result.get(0).getOrderDate());
        System.out.println("result = " + result.get(0).getDelivery());
        System.out.println("result = " + result.get(0).getTotalPrice());
        //given

        //when

        //then

    }

}