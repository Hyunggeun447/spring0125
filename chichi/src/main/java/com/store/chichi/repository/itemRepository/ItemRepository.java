package com.store.chichi.repository.itemRepository;

import com.store.chichi.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    List<Item> findByItemName(String itemName);

}
