package com.store.chichi.repository.itemRepository;

import static com.store.chichi.domain.item.QItem.item;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.store.chichi.domain.item.Item;
import java.util.List;
import javax.persistence.EntityManager;

public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Item> findItemsByBiggerThPrice(Integer price) {
        return queryFactory.selectFrom(item)
            .where(item.price.goe(price))
            .fetch();
    }
}
