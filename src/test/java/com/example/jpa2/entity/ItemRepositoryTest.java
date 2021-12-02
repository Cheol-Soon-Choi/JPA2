package com.example.jpa2.entity;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    ItemRepository itemRepository;

    public void createItem() {
        for (int i = 0; i < 11; i++) {
            Item item = new Item();
            item.setName("이름" + i);
            item.setPrice(1000 + i);
            item.setDetail("상세설명" + i);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("JQPL 확인")
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

    @Test
    @DisplayName("querydsl 확인")
    public void querydslTest() {

        createItem();

        QItem qitem = QItem.item;
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(qitem)
                .where(qitem.name.like("%" + "이름" + "%"))
                .where(qitem.price.gt(1005))
                .orderBy(qitem.price.desc());

        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }
}