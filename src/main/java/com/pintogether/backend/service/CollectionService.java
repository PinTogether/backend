package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateCollectionCommentRequestDTO;
import com.pintogether.backend.dto.CreateCollectionRequestDTO;
import com.pintogether.backend.dto.UpdateCollectionRequestDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionComment;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionCommentRepository;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final InterestingCollectionRepository interestingCollectionRepository;
    private final MemberService memberService;
    private final CollectionCommentRepository collectionCommentRepository;
    private final AmazonS3Service amazonS3Service;

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

    public void deleteCollection(Long memberId, Collection collection) {
        if (!memberId.equals(collection.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "컬렉션을 삭제할 권한이 없습니다.");
        }
        this.collectionRepository.delete(collection);
    }
    public void createCollection(Long memberId, CreateCollectionRequestDTO createCollectionRequestDTO) {
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
                .collect(Collectors.toList());
        collectionRepository.save(newCollection);
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
        List<CollectionTag> collectionTags = updateCollectionRequestDTO.getTags().stream()
                .map(tag -> CollectionTag.builder()
                        .collection(collection)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());
        collection.updateTitle(updateCollectionRequestDTO.getTitle());
        collection.updateDetails(updateCollectionRequestDTO.getDetails());
        collection.updateThumbnail(updateCollectionRequestDTO.getThumbnail());
        collection.clearCollectionTags();
        collectionRepository.save(collection);
    }

    public List<Collection> getTopLikeCollections(int cnt) {
        return collectionRepository.findTopCollectionsByInterestType(InterestType.LIKES.getString(), cnt);
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
        return collectionRepository.findByMemberId(memberId, pageable);
    }

    public Page<Collection> getScrapCollectionsByMemberIdWithPageable(Long memberId, int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created_at");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return collectionRepository.findByMemberIdAndInterestType(memberId, InterestType.SCRAP.getString(), pageable);
    }
}
