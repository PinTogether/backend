package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    int countByMemberId(Long memberId);
    @Query("SELECT c FROM Collection c WHERE c.member.id = :memberId AND (:lastCollectionId IS NULL OR c.id > :lastCollectionId) ORDER BY c.createdAt DESC ")
    List<Collection> findCollectionsAfterId(@Param("memberId") Long memberId, @Param("lastCollectionId") Long lastCollectionId, Pageable pageable);
}
