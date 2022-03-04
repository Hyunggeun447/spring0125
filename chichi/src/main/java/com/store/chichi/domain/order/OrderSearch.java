package com.store.chichi.domain.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String loginName;
    private OrderStatus orderStatus;
}
