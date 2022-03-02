package study.study.domain.entity.order;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.study.domain.entity.Delivery;
import study.study.domain.entity.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();



    /**
     * 연관관계 편의 메서드
     */
    public void linkMember(Member member) {
        if (this.member != null) {
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }

    public Order(Member member, Delivery delivery, OrderItem...orderItems) {
        this.member = member;
        this.delivery = delivery;
        for (OrderItem orderItem : orderItems) {
            this.getOrderItems().add(orderItem);
        }
    }
}
