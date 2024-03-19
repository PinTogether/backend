package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.AmazonS3Service;
import com.pintogether.backend.service.DomainType;
import com.pintogether.backend.service.FollowingService;
import com.pintogether.backend.service.MemberService;
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

    @GetMapping("/me/avatar/presigned-url")
    public ApiResponse getPresignedUrlForAvatar(@RequestBody @Valid S3MemberAvatarRequestDTO s3MemberAvatarRequestDTO) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String contentType = s3MemberAvatarRequestDTO.getContentType();
        return ApiResponse.makeResponse(memberService.getPresignedUrl(contentType, DomainType.Member.AVATAR.getName(), memberId));
    }
}
