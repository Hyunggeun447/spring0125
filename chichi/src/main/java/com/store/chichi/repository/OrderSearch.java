package com.store.chichi.repository;

import com.store.chichi.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String loginName;
    private OrderStatus orderStatus;
}
