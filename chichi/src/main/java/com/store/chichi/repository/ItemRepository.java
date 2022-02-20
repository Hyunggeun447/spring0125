package com.store.chichi.repository;

import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.domain.item.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        em.persist(item);
    }

    public Item findById(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {

        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public List<Item> findByName(String itemName) {
        return em.createQuery("select i from Item i where i.itemName=:itemName", Item.class)
                .setParameter("itemName", itemName)
                .getResultList();
    }

    public void deleteItemById(Long itemId) {
        Item item = em.find(Item.class, itemId);
        em.remove(item);
    }




}
