package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.QCollection;
import com.pintogether.backend.entity.QInterestingCollection;
import com.pintogether.backend.entity.enums.InterestType;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CollectionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final static QCollection collection = QCollection.collection;
    private final static QInterestingCollection interestingCollection = QInterestingCollection.interestingCollection;

    public Optional<Collection> findOneById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(collection)
                        .from(collection)
                        .leftJoin(collection.collectionTags).fetchJoin()
                        .leftJoin(collection.member).fetchJoin()
                        .where(collection.id.eq(id))
                        .fetchOne()
        );
    }
    @Transactional
    public Page<Collection> findByMemberId(Long id, Pageable pageable) {
        Long total = queryFactory
                .select(collection.count())
                .from(collection)
                .leftJoin(collection.member)
                .where(collection.member.id.eq(id))
                .fetchOne();

        List<Collection> collectionList = queryFactory.select(collection)
                .from(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .where(collection.member.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(collectionList, pageable, total);
    }

    public List<Collection> findByMemberId(Long id) {
        return queryFactory.select(collection)
                .from(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .where(collection.member.id.eq(id))
                .fetch();
    }

    public List<Collection> findTopCollectionsByInterestType(InterestType interestType, int cnt) {
        return queryFactory
                .select(collection)
                .from(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .leftJoin(interestingCollection).on(collection.id.eq(interestingCollection.collection.id))
                .where(interestingCollection.interestType.eq(interestType))
                .groupBy(collection.id)
                .orderBy(interestingCollection.collection.id.count().desc())
                .limit(cnt)
                .fetch();
    }


    public List<Collection> findTopCollectionsByInterestTypeExcludingSpecificIds(InterestType interestType, List<Long> excludedIds, int cnt) {
        return queryFactory
                .select(collection)
                .from(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .leftJoin(interestingCollection).on(collection.id.eq(interestingCollection.collection.id))
                .where(interestingCollection.interestType.eq(interestType).and(collection.id.notIn(excludedIds)))
                .groupBy(collection.id)
                .orderBy(interestingCollection.collection.id.count().desc())
                .limit(cnt)
                .fetch();
    }
    @Transactional
    public Page<Collection> findByMemberIdAndInterestType(Long memberId, InterestType interestType, Pageable pageable) {
        Long total = queryFactory
                .select(collection.count())
                .from(collection)
                .leftJoin(interestingCollection).on(collection.id.eq(interestingCollection.collection.id))
                .where(interestingCollection.interestType.eq(interestType).and(interestingCollection.member.id.eq(memberId)))
                .fetchOne();

        List<Collection> collectionList = queryFactory
                .select(collection)
                .from(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .leftJoin(interestingCollection).on(collection.id.eq(interestingCollection.collection.id))
                .where(interestingCollection.interestType.eq(interestType).and(interestingCollection.member.id.eq(memberId)))
                .orderBy(collection.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(collectionList, pageable, total);
    }
    @Transactional
    public Page<Collection> findCollectionsByQuery(Pageable pageable, String query) {
        System.out.println("query: "+query);
        BooleanExpression titleCondition = collection.title.contains(query);
        BooleanExpression tagCondition = collection.collectionTags.any().tag.contains(query);

        Long total = queryFactory
                .select(collection.count())
                .from(collection)
                .leftJoin(collection.collectionTags)
                .leftJoin(collection.member)
                .where(titleCondition.or(tagCondition))
                .fetchOne();

        List<Collection> results = queryFactory
                .selectFrom(collection)
                .leftJoin(collection.collectionTags).fetchJoin()
                .leftJoin(collection.member).fetchJoin()
                .where(titleCondition.or(tagCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(results, pageable, total);
    }
}
