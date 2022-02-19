package com.store.chichi.repository;

import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional

class ItemRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void delete() throws Exception {


        //given
        Shirt item = new Shirt();
        item.setItemName("item1");
        itemRepository.save(item);

        //when
        itemRepository.deleteItemById(item.getId());


        //then
        Item byId = itemRepository.findById(item.getId());

        assertThat(byId).isNull();


    }

}