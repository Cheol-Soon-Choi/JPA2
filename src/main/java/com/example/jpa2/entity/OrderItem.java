package com.example.jpa2.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitem_id")
    private Long id;

    private Integer count;

    private Integer order_price;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Item item;
}
