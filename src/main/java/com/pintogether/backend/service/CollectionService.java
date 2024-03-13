package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateCollectionRequestDTO;
import com.pintogether.backend.dto.UpdateCollectionRequestDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final InterestingCollectionRepository interestingCollectionRepository;
    private final MemberService memberService;

    public Collection getCollection(Long collectionId) {
        return collectionRepository.findOneById(collectionId).orElse(null);
    }

    public int getLikeCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.LIKES);
    }

    public int getScrappedCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.SCRAP);
    }

    public int getPinCnt(Long collectionId) {
        return this.getCollection(collectionId).getPins().size();
    }

    public void deleteCollection(Collection collection) {
        this.collectionRepository.delete(collection);
    }
    public void CreateCollection(Long memberId, CreateCollectionRequestDTO createCollectionRequestDTO) {
        Collection newCollection = Collection.builder()
                .member(memberService.getMember(memberId))
                .title(createCollectionRequestDTO.getTitle())
                .thumbnail(createCollectionRequestDTO.getThumbnail())
                .details(createCollectionRequestDTO.getDetails())
                .build();

        List<CollectionTag> collectionTags = createCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(newCollection)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());
        collectionRepository.save(newCollection);
    }

    public void updateCollection(Long memberId, Long collectionId, UpdateCollectionRequestDTO updateCollectionRequestDTO) {
        Collection collection = this.getCollection(collectionId);
        if (!collection.getMember().getId().equals(memberId)) {
            throw new CustomException(StatusCode.FORBIDDEN, "수정할 권한이 없습니다.");
        }
        collection.updateTitle(updateCollectionRequestDTO.getTitle());
        collection.updateDetails(updateCollectionRequestDTO.getDetails());
        collection.updateThumbnail(updateCollectionRequestDTO.getThumbnail());
        collection.clearCollectionTags();

        List<CollectionTag> collectionTags = updateCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(collection)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());

        collectionRepository.save(collection);
    }

    public List<Collection> getTopLikeCollections(int cnt) {
        return collectionRepository.findTopCollectionsByInterestType(InterestType.LIKES.getString(), cnt);
    }
}
