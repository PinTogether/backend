package com.pintogether.backend.repository;

import com.pintogether.backend.entity.InterestingCollection;
import com.pintogether.backend.entity.enums.InterestType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterestingCollectionRepository extends JpaRepository<InterestingCollection, Long> {
    int countByMemberIdAndInterestType(Long memberId, InterestType interestType);

    int countByCollectionIdAndInterestType(Long collectionId, InterestType interestType);

    boolean existsByMemberIdAndCollectionIdAndInterestType(Long memberId, Long collectionId, InterestType interestType);

    Optional<InterestingCollection> findOneByMemberIdAndCollectionIdAndInterestType(Long memberId, Long collectionId, InterestType interestType);
}
