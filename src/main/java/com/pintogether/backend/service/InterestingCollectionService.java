package com.pintogether.backend.service;

import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestingCollectionService {
    private final InterestingCollectionRepository interestingCollectionRepository;

    public boolean isLikedByMember(Long memberId, Long collectionId) {
        return interestingCollectionRepository.existsByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.LIKES);
    }

    public boolean isScrappedByMember(Long memberId, Long collectionId) {
        return interestingCollectionRepository.existsByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.SCRAP);
    }
}
