package com.example.jpa2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Integer age;

    private String name;

    @Builder
    public Member(int age, String name) {
        this.age = age;
        this.name = name;
    }

}
