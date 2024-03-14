package com.pintogether.backend.controller;

import com.pintogether.backend.dto.CreateCollectionRequestDTO;
import com.pintogether.backend.dto.ShowCollectionResponseDTO;
import com.pintogether.backend.dto.UpdateCollectionRequestDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.CollectionService;
import com.pintogether.backend.service.InterestingCollectionService;
import com.pintogether.backend.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private final MemberService memberService;
    private final InterestingCollectionService interestingCollectionService;

    @PostMapping
    public ApiResponse createCollection(@RequestBody @Valid CreateCollectionRequestDTO createCollectionRequestDTO, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        collectionService.CreateCollection(memberId, createCollectionRequestDTO);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @GetMapping("/{collectionId}")
    public ApiResponse getCollection(@PathVariable Long collectionId) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        ShowCollectionResponseDTO showCollectionResponseDTO = ShowCollectionResponseDTO.builder()
                .id(collectionId)
                .title(collection.getTitle())
                .writerId(collection.getMember().getId())
                .details(collection.getDetails())
                .writer(collection.getMember().getNickname())
                .thumbnail(collection.getThumbnail())
                .likeCnt(collectionService.getLikeCnt(collectionId))
                .pinCnt(collectionService.getPinCnt(collectionId))
                .scrapCnt(collectionService.getScrappedCnt(collectionId))
                .isScrapped(interestingCollectionService.isScrappedByMember(memberId, collectionId))
                .isLiked(interestingCollectionService.isLikedByMember(memberId, collectionId))
                .tags(collection.getCollectionTags().stream()
                        .map(CollectionTag::getTag)
                        .collect(Collectors.toList()))
                .build();
        return ApiResponse.makeResponse(showCollectionResponseDTO);
    }

    @DeleteMapping("/{collectionId}")
    public ApiResponse deleteCollection(@PathVariable Long collectionId, HttpServletResponse response) {
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        collectionService.deleteCollection(collection);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @PostMapping("/{collectionId}/likes")
    public ApiResponse likeOnCollection(@PathVariable Long collectionId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        interestingCollectionService.likeOnCollection(memberId, collectionId);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @PostMapping("/{collectionId}/scraps")
    public ApiResponse scrapTheCollection(@PathVariable Long collectionId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        interestingCollectionService.scrapTheCollection(memberId, collectionId);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @DeleteMapping("/{collectionId}/likes")
    public ApiResponse cancelLikeOnCollection(@PathVariable Long collectionId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        interestingCollectionService.cancelLikeOnCollection(memberId, collectionId);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @DeleteMapping("/{collectionId}/scraps")
    public ApiResponse cancelScrapOnCollection(@PathVariable Long collectionId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        interestingCollectionService.cancelScrapOnCollection(memberId, collectionId);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @PutMapping("/{collectionId}")
    public ApiResponse updateCollection(@PathVariable Long collectionId, @RequestBody @Valid UpdateCollectionRequestDTO updateCollectionRequestDTO,  HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Collection collection = collectionService.getCollection(collectionId);
        if (collection == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
        }
        collectionService.updateCollection(memberId, collectionId, updateCollectionRequestDTO);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/top")
    public ApiResponse getTopLikeCollections(@Param("cnt") int cnt) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        List<Collection> collections = collectionService.getTopLikeCollections(cnt);

        System.out.println("**********" + collections.get(0).getTitle());

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
//    public void getPins(@PathVariable Long collectionId) {
//        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//
//        Collection collection = collectionService.getCollection(collectionId);
//        if (collection == null) {
//            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage());
//        }
//        collection.getPins();
//    }


}
