package com.store.chichi.service;

import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Size;
import com.store.chichi.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, String itemName, int price, int stockQuantity, Size size, Color color) {
        Item item = itemRepository.findById(itemId);
        item.setItemName(itemName);
        item.setItemSize(size);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setItemColor(color);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findByName(String itemName) {
        return itemRepository.findByName(itemName);
    }

    public void deleteItemById(Long itemId) {
        itemRepository.deleteItemById(itemId);
    }
}
