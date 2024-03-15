package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.CurrentCollection;
import com.pintogether.backend.customAnnotations.CurrentCollectionComment;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionComment;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private final CollectionCommentService collectionCommentService;
    private final InterestingCollectionService interestingCollectionService;
    private final PinService pinService;

    @PostMapping("")
    public ApiResponse createCollection(@RequestBody @Valid CreateCollectionRequestDTO createCollectionRequestDTO, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.createCollection(memberId, createCollectionRequestDTO);


        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @GetMapping("/{collectionId}")
    public ApiResponse getCollection(@CurrentCollection Collection collection) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        ShowCollectionResponseDTO showCollectionResponseDTO = ShowCollectionResponseDTO.builder()
                .id(collection.getId())
                .title(collection.getTitle())
                .writerId(collection.getMember().getId())
                .details(collection.getDetails())
                .writer(collection.getMember().getNickname())
                .thumbnail(collection.getThumbnail())
                .likeCnt(collectionService.getLikeCnt(collection.getId()))
                .pinCnt(collectionService.getPinCnt(collection.getId()))
                .scrapCnt(collectionService.getScrappedCnt(collection.getId()))
                .isScrapped(interestingCollectionService.isScrappedByMember(memberId, collection.getId()))
                .isLiked(interestingCollectionService.isLikedByMember(memberId, collection.getId()))
                .tags(collection.getCollectionTags().stream()
                        .map(CollectionTag::getTag)
                        .collect(Collectors.toList()))
                .build();
        return ApiResponse.makeResponse(showCollectionResponseDTO);
    }

    @DeleteMapping("/{collectionId}")
    public ApiResponse deleteCollection(@CurrentCollection Collection collection, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.deleteCollection(memberId, collection);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @PostMapping("/{collectionId}/likes")
    public ApiResponse likeOnCollection(@CurrentCollection Collection collection, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        interestingCollectionService.likeOnCollection(memberId, collection.getId());
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @PostMapping("/{collectionId}/scraps")
    public ApiResponse scrapTheCollection(@CurrentCollection Collection collection, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        interestingCollectionService.scrapTheCollection(memberId, collection.getId());
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @DeleteMapping("/{collectionId}/likes")
    public ApiResponse cancelLikeOnCollection(@CurrentCollection Collection collection, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        interestingCollectionService.cancelLikeOnCollection(memberId, collection.getId());
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @DeleteMapping("/{collectionId}/scraps")
    public ApiResponse cancelScrapOnCollection(@CurrentCollection Collection collection, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        interestingCollectionService.cancelScrapOnCollection(memberId, collection.getId());
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @PutMapping("/{collectionId}")
    public ApiResponse updateCollection(@CurrentCollection Collection collection, @RequestBody @Valid UpdateCollectionRequestDTO updateCollectionRequestDTO,  HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.updateCollection(memberId, collection.getId(), updateCollectionRequestDTO);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/top")
    public ApiResponse getTopLikeCollections(@RequestParam(value = "cnt", required = true) int cnt) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        List<Collection> collections = collectionService.getTopLikeCollections(cnt);

        List<ShowCollectionResponseDTO> showCollectionResponseDTOs = collections.stream()
                .map(c -> ShowCollectionResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .details(c.getDetails())
                        .writer(c.getMember().getNickname())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .isScrapped(interestingCollectionService.isScrappedByMember(memberId, c.getId()))
                        .isLiked(interestingCollectionService.isLikedByMember(memberId, c.getId()))
                        .tags(c.getCollectionTags().stream()
                                .map(CollectionTag::getTag)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showCollectionResponseDTOs);
    }

//    @GetMapping("/{collectionId}/pins")
//    public void getPins(@CurrentCollection Collection collection) {
//        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//        pinService.
//    }

    @PostMapping("/{collectionId}/comments")
    public ApiResponse leaveCollectionComment(HttpServletResponse response, @CurrentCollection Collection collection, @RequestBody CreateCollectionCommentRequestDTO createCollectionCommentRequestDTO) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.leaveAComment(memberId, collection, createCollectionCommentRequestDTO);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse deleteCollectionComment(HttpServletResponse response, @CurrentCollectionComment CollectionComment collectionComment) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.deleteComment(memberId, collectionComment);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/{collectionId}/comments")
    public ApiResponse getCollectionComments(@CurrentCollection Collection collection) {
        List<CollectionComment> collectionComments = collectionCommentService.getCollectionComments(collection.getId());
        List<ShowCollectionCommentResponseDTO> showCollectionCommentResponseDTOs = collectionComments.stream()
                .map(cc -> ShowCollectionCommentResponseDTO.builder()
                        .id(cc.getId())
                        .writerId(cc.getMember().getId())
                        .writer(cc.getMember().getNickname())
                        .contents(cc.getContents())
                        .createdAt(cc.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showCollectionCommentResponseDTOs);
    }

    @GetMapping("/{collectionId}/thumbnail/presigned-url")
    public ApiResponse getPresignedUrlForThumbnail(@CurrentCollection Collection collection, @RequestBody @Valid S3CollectionThumbnailRequestDTO s3CollectionThumbnailRequestDTO) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ApiResponse.makeResponse(collectionService.getPresignedUrlForThumbnail(memberId, s3CollectionThumbnailRequestDTO.getContentType(), DomainType.Collection.THUMBNAIL.getName(), collection));
    }
}
