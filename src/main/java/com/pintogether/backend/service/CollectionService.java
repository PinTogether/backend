package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateCollectionRequestDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public int getScrappedCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.SCRAP);
    }

    public int getLikeCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.LIKES);
    }

    public void CreateCollection(Long memberId, CreateCollectionRequestDTO createCollectionRequestDTO) {
        Collection collection = Collection.builder()
                .member(memberService.getMember(memberId))
                .title(createCollectionRequestDTO.getTitle())
                .thumbnail(createCollectionRequestDTO.getThumbnail())
                .details(createCollectionRequestDTO.getDetails())
                .build();
        List<CollectionTag> collectionTags = createCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(collection)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());
        collectionRepository.save(collection);
    }

}
