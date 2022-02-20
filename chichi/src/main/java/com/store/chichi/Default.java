package com.store.chichi;

import com.store.chichi.domain.Member;
import com.store.chichi.domain.MemberGrade;
import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.domain.item.Size;
import com.store.chichi.service.ItemService;
import com.store.chichi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Default {

    private final MemberService memberService;

    private final ItemService itemService;


    //Default
    @PostConstruct
    public void init() {
        Member member = new Member("Kim", "abcd1234", "1234", "abcd@aaa.com", "01021345678");
        member.setMemberGrade(MemberGrade.ADMIN);
        memberService.join(member);
        Item item = new Shirt();
        item.setItemName("옷");
        item.setPrice(20000);
        item.setStockQuantity(20);
        item.setItemSize(Size.XL);
        item.setItemColor(Color.RED);
        itemService.saveItem(item);
    }

}
