package com.store.chichi.repository.orderRepository;

import com.store.chichi.domain.order.Order;
import com.store.chichi.domain.order.OrderSearch;
import com.store.chichi.domain.order.OrderSearchDto;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByLoginNameAndOrderStatus(OrderSearch condition);

    List<OrderSearchDto> findByLoginNameAndOrderStatusDto(OrderSearch condition);


}
