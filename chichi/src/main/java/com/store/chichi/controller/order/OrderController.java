package com.store.chichi.controller.order;

import com.store.chichi.domain.Address;
import com.store.chichi.domain.Member;
import com.store.chichi.domain.order.Order;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.order.OrderSearch;
import com.store.chichi.domain.order.OrderSearchDto;
import com.store.chichi.service.ItemService;
import com.store.chichi.service.MemberService;
import com.store.chichi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createOrderForm(Model model) {
        List<Member> members = memberService.findAllMembers();
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        model.addAttribute("members", members);
        return "order/orderForm";

    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count,
                        @RequestParam("address1") String address1,
                        @RequestParam("address2") String address2) {
        Address address = new Address(address1, address2);
        orderService.order(memberId, itemId, count, address);
        return "redirect:/";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }


}
