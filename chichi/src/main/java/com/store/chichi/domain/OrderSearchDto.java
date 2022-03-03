package com.store.chichi.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchDto {

    private Long id;
    private String loginName;
    private String itemName;
    private int orderPrice;
    private int count;
    private OrderStatus status;
    private LocalDateTime orderDate;

    @QueryProjection
    public OrderSearchDto(Long id, String loginName, String itemName, int orderPrice, int count, OrderStatus status, LocalDateTime orderDate) {
        this.id = id;
        this.loginName = loginName;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
        this.status = status;
        this.orderDate = orderDate;
    }
}
