package com.example.jpa2.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    public void createItem() {
        for (int i = 0; i < 11; i++) {
            Item item = new Item();
            item.setName("이름" + i);
            item.setPrice(1000 + i);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("custom querydsl 확인")
    public void createItemTest() {
        //given
        createItem();

        //when
        List<Item> items = itemRepository.findByName("1");

        //then
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println(item.toString());
        }
    }
}