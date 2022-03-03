package com.store.chichi.repository.orderRepository;

import com.store.chichi.domain.Order;
import com.store.chichi.repository.OrderSearch;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByLoginNameAndOrderStatus(OrderSearch condition);


}
