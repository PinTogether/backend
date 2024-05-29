package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateCollectionCommentRequestDTO;
import com.pintogether.backend.dto.CreateCollectionRequestDTO;
import com.pintogether.backend.dto.UpdateCollectionRequestDTO;
import com.pintogether.backend.entity.*;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final InterestingCollectionRepository interestingCollectionRepository;
    private final MemberService memberService;
    private final CollectionCommentRepository collectionCommentRepository;
    private final PinRepository pinRepository;
    private final AmazonS3Service amazonS3Service;
    private final CollectionRepositoryCustom collectionRepositoryCustom;

    public Collection getCollection(Long collectionId) {
        // return collectionRepository.findOneById(collectionId).orElse(null);
        return collectionRepositoryCustom.findOneById(collectionId).orElse(null);
    }

    public int getLikeCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.LIKES);
    }

    public int getScrappedCnt(Long collectionId) {
        return interestingCollectionRepository.countByCollectionIdAndInterestType(collectionId, InterestType.SCRAP);
    }

    public int getCollectionPinCnt(Long collectionId) {
        return this.getPins(collectionId).size();
    }

    public void deleteCollection(Long memberId, Collection collection) {
        if (!memberId.equals(collection.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "컬렉션을 삭제할 권한이 없습니다.");
        }
        interestingCollectionRepository.deleteAll(interestingCollectionRepository.findAllByCollectionId(collection.getId()));
        this.collectionRepository.delete(collection);
    }
    public Collection createCollection(Long memberId, CreateCollectionRequestDTO createCollectionRequestDTO) {
        Collection newCollection = Collection.builder()
                .member(memberService.getMember(memberId))
                .title(createCollectionRequestDTO.getTitle())
                .thumbnail("https://pintogether-img.s3.ap-northeast-2.amazonaws.com/default/collection1.png")
                .details(createCollectionRequestDTO.getDetails())
                .build();

        List<CollectionTag> collectionTags = createCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(newCollection)
                        .tag(tag)
                        .build())
                .toList();
        collectionRepository.save(newCollection);
        return newCollection;
    }

    public void updateCollection(Long memberId, Long collectionId, UpdateCollectionRequestDTO updateCollectionRequestDTO) {
        Collection collection = this.getCollection(collectionId);
        if (!collection.getMember().getId().equals(memberId)) {
            throw new CustomException(StatusCode.FORBIDDEN, "컬렉션을 수정할 권한이 없습니다.");
        }

        Set<String> defaultImage = new HashSet<>(Arrays.asList(
                "https://pintogether-img.s3.ap-northeast-2.amazonaws.com/default/collection1.png"
        ));
        if (!defaultImage.contains(collection.getThumbnail()) && !collection.getThumbnail().equals(updateCollectionRequestDTO.getThumbnail())) {
            amazonS3Service.deleteS3Image(collection.getThumbnail());
        }
        collection.clearCollectionTags();
        List<CollectionTag> collectionTags = updateCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(collection)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());
        collection.updateTitle(updateCollectionRequestDTO.getTitle());
        collection.updateDetails(updateCollectionRequestDTO.getDetails());
        collection.updateThumbnail(updateCollectionRequestDTO.getThumbnail());
        collectionRepository.save(collection);
    }

    public List<Collection> getTopLikeCollections(int cnt, List<Long> excludeIds) {
        if (excludeIds == null) {
//            return collectionRepository.findTopCollectionsByInterestType(InterestType.LIKES.getString(), cnt);
            return collectionRepositoryCustom.findTopCollectionsByInterestType(InterestType.LIKES, cnt);
        } else {
//            return collectionRepository.findTopCollectionsByInterestTypeExcludingSpecificIds(InterestType.LIKES.getString(), excludeIds, cnt);
            return collectionRepositoryCustom.findTopCollectionsByInterestTypeExcludingSpecificIds(InterestType.LIKES, excludeIds, cnt);
        }
    }

    public void leaveAComment(Long memberId, Collection collection, CreateCollectionCommentRequestDTO createCollectionCommentRequestDTO) {
        CollectionComment collectionComment = CollectionComment.builder()
                .collection(collection)
                .member(memberService.getMember(memberId))
                .contents(createCollectionCommentRequestDTO.getContents())
                .build();
        collectionCommentRepository.save(collectionComment);
    }

    public void deleteComment(Long memberId, CollectionComment collectionComment) {
        if (!memberId.equals(collectionComment.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");
        }
        collectionCommentRepository.delete(collectionComment);
    }

    public List<CollectionComment> getComments(Collection collection) {
        return collection.getCollectionComments();
    }

    public AmazonS3Service.AmazonS3Response getPresignedUrlForThumbnail(Long memberId, String contentType, String domainType, Collection collection) {
        if (!memberId.equals(collection.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "컬렉션 썸네일 presigned url을 요청할 권한이 없습니다.");
        }
        return amazonS3Service.getneratePresignedUrlAndImageUrl(contentType, domainType, collection.getId());
    }

    public Page<Collection> getCollectionsByMemberIdWithPageable(Long memberId, int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//        return collectionRepository.findByMemberId(memberId, pageable);
        return collectionRepositoryCustom.findByMemberId(memberId, pageable);
    }

    public Page<Collection> getScrapCollectionsByMemberIdWithPageable(Long memberId, int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created_at");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//        return collectionRepository.findByMemberIdAndInterestType(memberId, InterestType.SCRAP.getString(), pageable);
        return collectionRepositoryCustom.findByMemberIdAndInterestType(memberId, InterestType.SCRAP, pageable);
    }

    public List<Collection> getCollectionsByMemberId(Long memberId) {
//        return collectionRepository.findByMemberId(memberId);
        return collectionRepositoryCustom.findByMemberId(memberId);
    }
    public List<Pin> getPins(Long collectionId) {
        return pinRepository.findByCollectionId(collectionId);
    }

    public boolean hasPin(Collection collection, Long placeId) {
        List<Pin> pins = getPins(collection.getId());
        for (Pin pin : pins) {
            if (pin.getPlace().getId().equals(placeId)) {
                return true;
            }
        }
        return false;
    }
}
