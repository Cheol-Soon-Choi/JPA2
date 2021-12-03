package com.example.jpa2.entity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void createItem2() {
        for (int i = 0; i < 5; i++) {
            Item item = new Item();
            item.setName("이름" + i);
            item.setPrice(10000 + i);
            item.setDetail("가나다라" + i);
            itemRepository.save(item);
        }
        for (int i = 5; i < 10; i++) {
            Item item = new Item();
            item.setName("이름" + i);
            item.setPrice(10000 + i);
            item.setDetail("마바사" + i);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("QuerydslPredicateExecutor 확인")
    public void QuerydslPredicateExecutorTest() {

        createItem2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qitem = QItem.item;

        booleanBuilder.and(qitem.name.like("%" + "이름" + "%"));
        booleanBuilder.and(qitem.price.gt(10005));
        booleanBuilder.and(qitem.detail.like("%" + "마바사" + "%"));

        Pageable pageable = PageRequest.of(0, 2);
        Page<Item> itemPage = itemRepository.findAll(booleanBuilder, pageable);

        System.out.println("getContent= " + itemPage.getContent());

        List<Item> resultItemList = itemPage.getContent();
        for(Item item: resultItemList){
            System.out.println(item);
        }

        System.out.println("getTotalElements= " + itemPage.getTotalElements());

        System.out.println("getTotalPages= " + itemPage.getTotalPages());

    }
}