package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Optional<Collection> findOneById(Long id);
    int countByMemberId(Long memberId);

    @Query(value = "SELECT c.*, COUNT(ic.collection_id) AS interest_count FROM collection c INNER JOIN interesting_collection ic ON c.id = ic.collection_id WHERE ic.interest_type = :interestType GROUP BY c.id ORDER BY interest_count DESC LIMIT :cnt", nativeQuery = true)
    List<Collection> findTopCollectionsByInterestType(@Param("interestType") String interestType, @Param("cnt") int cnt);
}
