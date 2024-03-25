package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.CurrentMember;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
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
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final FollowingService followingService;
    private final CollectionService collectionService;
    private final InterestingCollectionService interestingCollectionService;
    private final AmazonS3Service amazonS3Service;

    @GetMapping("/me")
    public ApiResponse getMemberInformation() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member foundMember = memberService.getMember(id);
        ShowMemberResponseDTO showMemberResponseDTO = ShowMemberResponseDTO.builder()
                .id(foundMember.getId())
                .nickname(foundMember.getNickname())
                .registrationSource(foundMember.getRegistrationSource())
                .role(foundMember.getRoleType())
                .avatar(foundMember.getAvatar())
                .collectionCnt(memberService.getCollectionCnt(id))
                .scrappedCollectionCnt(memberService.getScrappedCollectionCnt(id))
                .followerCnt(memberService.getFollowerCnt(id))
                .followingCnt(memberService.getFolloweeCnt(id))
                .build();
        return ApiResponse.makeResponse(showMemberResponseDTO);
    }

    @PutMapping("/me")
    public ApiResponse updateMemberInformation(@RequestBody @Valid UpdateMemberRequestDTO updateMemberRequestDTO, HttpServletResponse response) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        memberService.update(id, updateMemberRequestDTO);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @DeleteMapping("/me")
    public ApiResponse deleteMemberAccount(HttpServletResponse response) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        memberService.delete(id);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/{targetId}")
    public ApiResponse getOtherMemberInformation(@ThisMember Member member, @PathVariable Long targetId) {
        Member targetMember = memberService.getMember(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage());
        }
        ShowOtherMemberResponseDTO showOtherMemberResponseDTO = ShowOtherMemberResponseDTO.builder()
                .nickname(targetMember.getNickname())
                .avatar(targetMember.getAvatar())
                .collectionCnt(memberService.getCollectionCnt(targetId))
                .scrappedCollectionCnt(memberService.getScrappedCollectionCnt(targetId))
                .isFollowed(member != null ? followingService.checkIfFollow(member.getId(), targetId) : false)
                .followerCnt(memberService.getFollowerCnt(targetId))
                .followingCnt(memberService.getFolloweeCnt(targetId))
                .build();
        return ApiResponse.makeResponse(showOtherMemberResponseDTO);
    }

    @PostMapping("/{targetId}/follow")
    public ApiResponse followMember(@PathVariable Long targetId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member targetMember = memberService.getMember(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage());
        }
        followingService.follow(memberId, targetId);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @DeleteMapping("/{targetId}/follow")
    public ApiResponse unfollowMember(@PathVariable Long targetId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member targetMember = memberService.getMember(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage());
        }
        followingService.unfollow(memberId, targetId);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @GetMapping("/me/followers")
    public ApiResponse getFollowers() {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<Member> followers = memberService.getFollowers(memberId);
        List<ShowSimpleMemberResponseDTO> showSimpleMemberResponseDTOs = followers.stream()
                .map(f -> ShowSimpleMemberResponseDTO.builder()
                        .id(f.getId())
                        .nickname(f.getNickname())
                        .avatar(f.getAvatar())
                        .collectionCnt(memberService.getCollectionCnt(f.getId()))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showSimpleMemberResponseDTOs);
    }

    @GetMapping("/me/followings")
    public ApiResponse getFollowings() {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<Member> followings = memberService.getFollowings(memberId);
        List<ShowSimpleMemberResponseDTO> showSimpleMemberResponseDTOs = followings.stream()
                .map(f -> ShowSimpleMemberResponseDTO.builder()
                        .id(f.getId())
                        .nickname(f.getNickname())
                        .avatar(f.getAvatar())
                        .collectionCnt(memberService.getCollectionCnt(f.getId()))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showSimpleMemberResponseDTOs);
    }
    @GetMapping("/{targetId}/collections")
    public ApiResponse getCollectionsByMember(@ThisMember Member member, @CurrentMember Member targetMember, @RequestParam(value="page", defaultValue = "0") int pageNumber, @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        List<ShowCollectionsResponseDTO> collections = collectionService.getCollectionsByMemberIdWithPageable(targetMember.getId(), pageNumber, pageSize).get()
                .map(c -> ShowCollectionsResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .writer(c.getMember().getNickname())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .isScrapped(member != null ? interestingCollectionService.isScrappedByMember(member.getId(), c.getId()) : false)
                        .isLiked(member != null ? interestingCollectionService.isLikedByMember(member.getId(), c.getId()) : false)
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(collections);
    }

    @GetMapping("/{targetId}/scraps")
    public ApiResponse getScrapCollectionsByMember(@ThisMember Member member, @CurrentMember Member targetMember,  @RequestParam(value="page", defaultValue = "0") int pageNumber, @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        List<ShowCollectionsResponseDTO> collections = collectionService.getScrapCollectionsByMemberIdWithPageable(targetMember.getId(), pageNumber, pageSize).get()
                .map(c -> ShowCollectionsResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .writer(c.getMember().getNickname())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .isScrapped(member!=null ? interestingCollectionService.isScrappedByMember(member.getId(), c.getId()) : false)
                        .isLiked(member!=null ? interestingCollectionService.isLikedByMember(member.getId(), c.getId()) : false)
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(collections);
    }

    @PostMapping("/me/avatar/presigned-url")
    public ApiResponse getPresignedUrlForAvatar(@RequestBody @Valid S3MemberAvatarRequestDTO s3MemberAvatarRequestDTO) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String contentType = s3MemberAvatarRequestDTO.getContentType();
        return ApiResponse.makeResponse(memberService.getPresignedUrl(contentType, DomainType.Member.AVATAR.getName(), memberId));
    }

    @GetMapping("/collections")
    public ApiResponse getMyCollectionsForAddingPin(@ThisMember Member member, @RequestParam(value = "place-id", required = true) Long placeId) {
        List<Collection> collections = collectionService.getCollectionsByMemberId(member.getId());

        List<ShowCollectionsForAddingPinResponseDTO> showCollectionsForAddingPinResponseDTOs = collections.stream()
                .map(c -> ShowCollectionsForAddingPinResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .pinned(collectionService.hasPin(c, placeId))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.makeResponse(showCollectionsForAddingPinResponseDTOs);
    }
}
