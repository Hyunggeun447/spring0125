package com.store.chichi;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    private Delivery delivery;


    @Enumerated
    private OrderStatus status;



}
