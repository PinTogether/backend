package com.pintogether.backend.service;

import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final InterestingCollectionService interestingCollectionService;
    public int getCollectionCnt(Long memberId) {
        return collectionRepository.countByMemberId(memberId);
    }

    public List<Collection> getCreateCollections(Long memberId, Long lastCollectionId, Pageable pageable) {
        return collectionRepository.findCollectionsAfterId(memberId, lastCollectionId, pageable);
    }

}
