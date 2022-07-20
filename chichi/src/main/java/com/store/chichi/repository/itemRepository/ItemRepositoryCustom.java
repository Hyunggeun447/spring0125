package com.store.chichi.repository.itemRepository;

import com.store.chichi.domain.item.Item;
import java.util.List;

public interface ItemRepositoryCustom {

  List<Item> findItemsByBiggerThPrice(Integer price);
}
