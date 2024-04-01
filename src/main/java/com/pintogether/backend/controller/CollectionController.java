package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.CurrentCollection;
import com.pintogether.backend.customAnnotations.CurrentCollectionComment;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.*;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.*;
import com.pintogether.backend.util.CoordinateConverter;
import com.pintogether.backend.util.DateConverter;
import com.pintogether.backend.websocket.NotificationType;
import com.pintogether.backend.websocket.WebSocketHandler;
import com.pintogether.backend.websocket.WebSocketService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private final CollectionCommentService collectionCommentService;
    private final InterestingCollectionService interestingCollectionService;
    private final PlaceService placeService;
    private final WebSocketService webSocketService;
    @PostMapping("")
    public ApiResponse createCollection(@ThisMember Member member, @RequestBody @Valid CreateCollectionRequestDTO createCollectionRequestDTO) {
        Collection newCollection = collectionService.createCollection(member.getId(), createCollectionRequestDTO);
        webSocketService.createCollection(member, newCollection);
        if (createCollectionRequestDTO.getContentType() == null) {
            return ApiResponse.makeResponse(CreateCollectionResponseDTO.builder().id(newCollection.getId()).build());
        } else {
            return ApiResponse.makeResponse(collectionService.getPresignedUrlForThumbnail(member.getId(), createCollectionRequestDTO.getContentType(), DomainType.Collection.THUMBNAIL.getName(), newCollection));
        }
    }

    @GetMapping("/{collectionId}")
    public ApiResponse getCollection(@ThisMember Member member, @CurrentCollection Collection collection) {
        ShowCollectionResponseDTO showCollectionResponseDTO = ShowCollectionResponseDTO.builder()
                .id(collection.getId())
                .title(collection.getTitle())
                .writerId(collection.getMember().getId())
                .details(collection.getDetails())
                .writerMembername(collection.getMember().getMembername())
                .thumbnail(collection.getThumbnail())
                .likeCnt(collectionService.getLikeCnt(collection.getId()))
                .pinCnt(collectionService.getPinCnt(collection.getId()))
                .scrapCnt(collectionService.getScrappedCnt(collection.getId()))
                .isScrapped(member!=null ? interestingCollectionService.isScrappedByMember(member.getId(), collection.getId()) : false)
                .isLiked(member!=null? interestingCollectionService.isLikedByMember(member.getId(), collection.getId()) : false)
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
    public ApiResponse likeOnCollection(@ThisMember Member member, @CurrentCollection Collection collection, HttpServletResponse response) {
        interestingCollectionService.likeOnCollection(member.getId(), collection.getId());
        webSocketService.likeCollection(member, collection);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @PostMapping("/{collectionId}/scraps")
    public ApiResponse scrapTheCollection(@ThisMember Member member, @CurrentCollection Collection collection, HttpServletResponse response) {
        interestingCollectionService.scrapTheCollection(member.getId(), collection.getId());
        webSocketService.scrapCollection(member, collection);
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
    public ApiResponse updateCollection(@CurrentCollection Collection collection, @RequestBody @Valid UpdateCollectionRequestDTO updateCollectionRequestDTO, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.updateCollection(memberId, collection.getId(), updateCollectionRequestDTO);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/top")
    public ApiResponse getTopLikeCollections(@ThisMember Member member, @RequestParam(value = "cnt", defaultValue = "10") int cnt) {
        List<Collection> collections = collectionService.getTopLikeCollections(cnt);

        List<ShowCollectionResponseDTO> showCollectionResponseDTOs = collections.stream()
                .map(c -> ShowCollectionResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .details(c.getDetails())
                        .writerMembername(c.getMember().getMembername())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .isScrapped(member!=null ? interestingCollectionService.isScrappedByMember(member.getId(), c.getId()) : false)
                        .isLiked(member!=null ? interestingCollectionService.isLikedByMember(member.getId(), c.getId()) : false)
                        .tags(c.getCollectionTags().stream()
                                .map(CollectionTag::getTag)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showCollectionResponseDTOs);
    }

    @GetMapping("/{collectionId}/pins")
    public ApiResponse getPins(@ThisMember Member member, @CurrentCollection Collection collection) {
        List<Pin> pins = collectionService.getPins(collection.getId());

        List<ShowPinsResponseDTO> showPinsResponseDTOs = new ArrayList<>();
        for (Pin p : pins) {
            CoordinateDTO coordinate = CoordinateConverter.convert(p.getPlace().getAddress().getLongitude(), p.getPlace().getAddress().getLatitude());
            showPinsResponseDTOs.add(
                    ShowPinsResponseDTO.builder()
                        .id(p.getId())
                        .collectionId(collection.getId())
                        .collectionTitle(collection.getTitle())
                        .writerId(collection.getMember().getId())
                        .writerMembername(collection.getMember().getMembername())
                        .avatarImage(collection.getMember().getAvatar())
                        .review(p.getReview())
                        .imagePaths(p.getPinImages().stream()
                                .map(PinImage::getImagePath)
                                .collect(Collectors.toList()))
                        .tags(p.getPinTags().stream()
                                .map(PinTag::getTag)
                                .collect(Collectors.toList()))
                        .createdAt(DateConverter.convert(p.getCreatedAt()))
                        .placeId(p.getPlace().getId())
                        .placeName(p.getPlace().getName())
                        .category(p.getPlace().getCategory())
                        .address(p.getPlace().getAddress().getRoadNameAddress())
                        .latitude(coordinate.getLatitude())
                        .longitude(coordinate.getLongitude())
                        .saveCnt(placeService.getPlacePinCnt(p.getId()))
                        .starred(placeService.getStarred(member, p.getPlace().getId()))
                        .build()
            );
        }
        return ApiResponse.makeResponse(showPinsResponseDTOs);
    }

    @PostMapping("/{collectionId}/comments")
    public ApiResponse leaveCollectionComment(@ThisMember Member member, HttpServletResponse response, @CurrentCollection Collection collection, @RequestBody CreateCollectionCommentRequestDTO createCollectionCommentRequestDTO) {
        collectionService.leaveAComment(member.getId(), collection, createCollectionCommentRequestDTO);
        webSocketService.commentOnCollection(member, collection);
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
                        .writerName(cc.getMember().getName())
                        .writerMembername(cc.getMember().getMembername())
                        .writerAvatar(cc.getMember().getAvatar())
                        .contents(cc.getContents())
                        .createdAt(DateConverter.convert(cc.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showCollectionCommentResponseDTOs);
    }

    @PostMapping("/{collectionId}/thumbnail/presigned-url")
    public ApiResponse getPresignedUrlForThumbnail(@CurrentCollection Collection collection, @RequestBody @Valid S3CollectionThumbnailRequestDTO s3CollectionThumbnailRequestDTO) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ApiResponse.makeResponse(collectionService.getPresignedUrlForThumbnail(memberId, s3CollectionThumbnailRequestDTO.getContentType(), DomainType.Collection.THUMBNAIL.getName(), collection));
    }

}
