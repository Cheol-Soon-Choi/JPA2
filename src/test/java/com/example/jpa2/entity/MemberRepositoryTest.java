package com.example.jpa2.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("멤버 저장 테스트")
    public void createItemTest() {
        //given
        Member member = new Member();
        member.setName("하이");
        member.setAge(123);

        //when
        memberRepository.save(member);
        List<Member> memberList = memberRepository.findAll();

        //then
        Member members = memberList.get(0);
        assertThat(members.getAge()).isEqualTo(123);
        assertThat(members.getName()).isEqualTo("하이");
    }
}