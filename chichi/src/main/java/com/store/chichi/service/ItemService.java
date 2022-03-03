package com.store.chichi.service;

import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Size;
import com.store.chichi.repository.itemRepository.ItemRepository;
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

        sameItemNameFilter(item);
        itemRepository.save(item);
        return item.getId();
    }

    private void sameItemNameFilter(Item item) {
        String itemName = item.getItemName();
        List<Item> byName = itemRepository.findByItemName(itemName);
        if (!byName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 제품명입니다.");
        }
    }

    @Transactional
    public void updateItem(Long itemId, String itemName, int price, int stockQuantity, Size size, Color color) {
        Item item = itemRepository.findById(itemId).get();
        item.setItemName(itemName);
        item.setItemSize(size);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setItemColor(color);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findByName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    public void deleteItemById(Long itemId) {
        itemRepository.deleteById(itemId);
    }


}
