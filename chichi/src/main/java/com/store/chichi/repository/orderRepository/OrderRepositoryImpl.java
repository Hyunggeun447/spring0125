package com.store.chichi.repository.orderRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.chichi.domain.*;
import com.store.chichi.repository.OrderSearch;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.store.chichi.domain.QMember.*;
import static com.store.chichi.domain.QOrder.*;

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
                        orderStatusSafe(condition.getOrderStatus()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression orderStatusSafe(OrderStatus orderStatus) {
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
