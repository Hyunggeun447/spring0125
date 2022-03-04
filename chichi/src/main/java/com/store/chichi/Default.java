package com.store.chichi;

import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import com.store.chichi.domain.MemberGrade;
import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.domain.item.Size;
import com.store.chichi.service.ItemService;
import com.store.chichi.service.MemberService;
import com.store.chichi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Default {

    private final MemberService memberService;

    private final ItemService itemService;

    private final OrderService orderService;


    //Default
//    @PostConstruct
    public void init() {

        for (int i = 0; i<50; i++) {
            Member member = new Member("Kim" + i, "abcd" + i, "" + i, "abcd@aaa.com", "01021345678");
            member.setMemberGrade(MemberGrade.ADMIN);
            memberService.join(member);
            Item item = new Shirt();
            item.setItemName("옷" + i);
            item.setPrice(20000);
            item.setStockQuantity(20);
            item.setItemSize(Size.XL);
            item.setItemColor(Color.RED);
            itemService.saveItem(item);
            Address address = new Address("sss", "cxf");
            orderService.order(member.getId(), item.getId(), i, address);


        }

    }

}
