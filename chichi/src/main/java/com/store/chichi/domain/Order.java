package com.store.chichi.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //연관관계 메서드 order - member
    public void addMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    private LocalDateTime orderDate;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //연관관계 메서드 order - delivery
    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);

    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //연관관계 메서드 order - orderItem
    public void addOrderItems(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //    ORDER, CANCEL

    public Order(Member member, LocalDateTime orderDate,
                 Delivery delivery, List<OrderItem> orderItems, OrderStatus status) {
        this.member = member;
        this.orderDate = orderDate;
        this.delivery = delivery;
        this.orderItems = orderItems;
        this.status = status;
    }
}
