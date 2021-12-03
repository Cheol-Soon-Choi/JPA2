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
class OrderRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    EntityManager em;


    public Member createMember() {

        return Member.builder()
                .age(33)
                .name("아이언맨")
                .build();
    }

    @Test
    @DisplayName("Order확인 - 즉시로딩 Test")
    public void findOrderTest() {
        Member member = createMember();

        memberRepository.save(member);

        Order order = Order.builder()
                .member(member)
                .build();

        orderRepository.save(order);

        em.flush();
        em.clear();

        Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(saveOrder.getMember().getId()).isEqualTo(member.getId());

    }
}