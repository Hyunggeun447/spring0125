package com.store.chichi.repository.orderRepository;

import com.store.chichi.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryCustom {


}
