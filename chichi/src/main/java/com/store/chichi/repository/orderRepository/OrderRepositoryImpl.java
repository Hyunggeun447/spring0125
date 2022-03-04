package com.store.chichi.repository.orderRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.chichi.domain.order.*;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.store.chichi.domain.QMember.*;
import static com.store.chichi.domain.QOrderItem.*;
import static com.store.chichi.domain.item.QItem.*;
import static com.store.chichi.domain.order.QOrder.*;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findByLoginNameAndOrderStatus(OrderSearch condition) {

        return queryFactory
                .select(order)
                .from(order)
                .join(order.member,member)
                .where(loginNameEq(condition.getLoginName()),
                        orderStatusEq(condition.getOrderStatus()))
                .limit(1000)
                .fetch();
    }

    @Override
    public List<OrderSearchDto> findByLoginNameAndOrderStatusDto(OrderSearch condition) {
        return queryFactory
                .select(new QOrderSearchDto(
                        order.id,
                        member.loginName,
                        orderItem.item.itemName,
                        orderItem.orderPrice,
                        orderItem.count,
                        order.status,
                        order.orderDate))
                .from(order)
                .join(order.member, member)
                .join(order.orderItems, orderItem)
                .where(loginNameEq(condition.getLoginName()),
                        orderStatusEq(condition.getOrderStatus()))
                .fetch();
    }


    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        if (orderStatus != null) {
            return order.status.eq(orderStatus);
        } else {
            return null;
        }
    }

    private BooleanExpression loginNameEq(String loginName) {

        return StringUtils.hasText(loginName) ? member.loginName.eq(loginName) : null;
    }


}
