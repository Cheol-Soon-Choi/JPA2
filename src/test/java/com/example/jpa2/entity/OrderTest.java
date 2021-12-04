package com.example.jpa2.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        return Item.builder()
                .name("영속성")
                .price(30000)
                .detail("테스트")
                .build();
    }

    @Test
    @DisplayName("영속성 전이 테스트1")
    //Order 추가시 Order_item도 추가됨
    public void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .order_price(30000)
                    .count(10)
                    .item(item)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        System.out.println("--------------------flush--------------------");
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("영속성 전이 테스트2")
    //Order 삭제시 Order_item도 삭제됨
    public void cascadeTest2() {

        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .order_price(30000)
                    .count(10)
                    .item(item)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        System.out.println("--------------------flush--------------------");
        orderRepository.saveAndFlush(order);
        em.clear();

        System.out.println("--------------------flush--------------------2");
        orderRepository.deleteById(order.getId());
        em.flush();
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    //Order에서 Order_item 삭제
    public void orphanRemovalTest() {

        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .order_price(30000)
                    .count(10)
                    .item(item)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        System.out.println("----------------flush-------------------");
        order.getOrderItems().remove(0);
        em.flush();

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(2);
    }
}
