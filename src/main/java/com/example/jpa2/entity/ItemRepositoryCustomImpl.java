package com.example.jpa2.entity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression a1(String Search) {
        return QItem.item.name.like("%" + Search + "%");
    }

    private BooleanExpression a2() {
        return QItem.item.price.gt(10003);
    }

    @Override
    public List<Item> findTest(String Search) {
        return jpaQueryFactory.selectFrom(QItem.item)
                .where(a1(Search)
                        , a2())
                .orderBy(QItem.item.id.desc())
                .fetch();
    }
}
