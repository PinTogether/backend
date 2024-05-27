package com.pintogether.backend.service;

import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.InterestingCollection;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestingCollectionService {
    private final InterestingCollectionRepository interestingCollectionRepository;
    private final MemberService memberService;
    private final CollectionService collectionService;

    public boolean isLikedByMember(Long memberId, Long collectionId) {
        return interestingCollectionRepository.existsByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.LIKES);
    }

    public boolean isScrappedByMember(Long memberId, Long collectionId) {
        return interestingCollectionRepository.existsByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.SCRAP);
    }

    public void likeOnCollection(Long memberId, Long collectionId) {
        if (this.getLikedInterestingCollection(memberId, collectionId) != null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "이미 '좋아요'한 컬렉션입니다.");
        }
        InterestingCollection interestingCollection = InterestingCollection.builder()
                .member(memberService.getMember(memberId))
                .collection(collectionService.getCollection(collectionId))
                .interestType(InterestType.LIKES)
                .build();
        interestingCollectionRepository.save(interestingCollection);
    }

    public InterestingCollection getLikedInterestingCollection(Long memberId, Long collectionId) {
        return interestingCollectionRepository.findOneByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.LIKES).orElse(null);
    }

    public InterestingCollection getScrappedInterestingCollection(Long memberId, Long collectionId) {
        return interestingCollectionRepository.findOneByMemberIdAndCollectionIdAndInterestType(memberId, collectionId, InterestType.SCRAP).orElse(null);
    }

    public void scrapTheCollection(Long memberId, Long collectionId) {
        Collection collection = collectionService.getCollection(collectionId);
        if (collection.getMember().getId().equals(memberId)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "자신이 만든 컬렉션은 스크랩할 수 없습니다.");
        }
        if (this.getScrappedInterestingCollection(memberId, collectionId) != null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "이미 스크랩한 컬렉션입니다.");
        }
        InterestingCollection interestingCollection = InterestingCollection.builder()
                .member(memberService.getMember(memberId))
                .collection(collectionService.getCollection(collectionId))
                .interestType(InterestType.SCRAP)
                .build();
        interestingCollectionRepository.save(interestingCollection);
    }

    public void cancelLikeOnCollection(Long memberId, Long collectionId) {
        InterestingCollection interestingCollection = this.getLikedInterestingCollection(memberId, collectionId);
        if (interestingCollection == null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "'좋아요'하지 않은 컬렉션입니다.");
        }
        interestingCollectionRepository.delete(interestingCollection);
    }

    public void cancelScrapOnCollection(Long memberId, Long collectionId) {
        InterestingCollection interestingCollection = this.getScrappedInterestingCollection(memberId, collectionId);
        if (interestingCollection == null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "스크랩하지 않은 컬렉션입니다.");
        }
        interestingCollectionRepository.delete(interestingCollection);
    }

    public void deleteInterestingCollectionByCollectionId(Long collectionId) {
        interestingCollectionRepository.deleteAll(interestingCollectionRepository.findAllByCollectionId(collectionId));
    }
}
