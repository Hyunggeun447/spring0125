package com.store.chichi.service;

import com.store.chichi.domain.*;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.order.Order;
import com.store.chichi.domain.order.OrderSearch;
import com.store.chichi.domain.order.OrderSearchDto;
import com.store.chichi.repository.itemRepository.ItemRepository;
import com.store.chichi.repository.memberRepository.MemberRepository;
import com.store.chichi.repository.orderRepository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository,
        MemberRepository memberRepository,
        ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public Long order(Long memberId, Long itemId, int count, Address address) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(RuntimeException::new);

        Delivery delivery = new Delivery(address);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findByLoginNameAndOrderStatus(orderSearch);
    }

    public List<OrderSearchDto> findOrdersDto(OrderSearch orderSearch) {
        return orderRepository.findByLoginNameAndOrderStatusDto(orderSearch);
    }
}
