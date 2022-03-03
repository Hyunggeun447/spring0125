package com.store.chichi.service;

import com.store.chichi.domain.*;
import com.store.chichi.domain.item.Item;
import com.store.chichi.repository.OrderSearch;
import com.store.chichi.repository.itemRepository.ItemRepository;
import com.store.chichi.repository.memberRepository.MemberRepository;
import com.store.chichi.repository.orderRepository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    public Long order(Long memberId, Long itemId, int count, Address address) {
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(itemId).get();

        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();

    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findByLoginNameAndOrderStatus(orderSearch);

    }

}
