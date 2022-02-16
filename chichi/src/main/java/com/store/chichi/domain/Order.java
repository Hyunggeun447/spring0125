package com.store.chichi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //연관관계 메서드 order - member
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    private LocalDateTime orderDate;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //연관관계 메서드 order - delivery
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);

    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //연관관계 메서드 order - orderItem
    public void setOrderItems(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //    ORDER, CANCEL






}
