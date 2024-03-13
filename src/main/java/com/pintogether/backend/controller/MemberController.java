package com.pintogether.backend.controller;

import com.pintogether.backend.auth.JwtService;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.CollectionService;
import com.pintogether.backend.service.FollowingService;
import com.pintogether.backend.service.InterestingCollectionService;
import com.pintogether.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final CollectionService collectionService;
    private final InterestingCollectionService interestingCollectionService;
    private final FollowingService followingService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/me")
    public ApiResponse getMemberInformation() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member foundMember = memberService.findOneById(id);
        ShowMemberResponseDTO showMemberResponseDTO = ShowMemberResponseDTO.builder()
                .id(foundMember.getId())
                .nickname(foundMember.getNickname())
                .registrationSource(foundMember.getRegistrationSource())
                .role(foundMember.getRoleType())
                .avatar(foundMember.getAvatar())
                .collectionCnt(collectionService.getCollectionCnt(id))
                .scrappedCollectionCnt(interestingCollectionService.getScrappedCollectionCnt(id))
                .followerCnt(followingService.getFollowerCnt(id))
                .followingCnt(followingService.getFolloweeCnt(id))
                .build();
        return makeResponse(showMemberResponseDTO);
    }

    @PutMapping("/me")
    public ApiResponse updateMemberInformation(@RequestBody @Valid UpdateMemberRequestDTO updateMemberRequestDTO) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        memberService.update(id, updateMemberRequestDTO);
        return makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage());
    }

    // 맨 나중에 테스트하자
    @DeleteMapping("/me")
    public ApiResponse deleteMemberAccount() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        memberService.delete(id);
        return makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage());
    }

    @GetMapping("/{targetId}")
    public ApiResponse getOtherMemberInformation(@PathVariable Long targetId) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member targetMember = memberService.findOneById(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "존재하지 않는 멤버입니다.");
        }
        ShowOtherMemberResponseDTO showOtherMemberResponseDTO = ShowOtherMemberResponseDTO.builder()
                .nickname(targetMember.getNickname())
                .avatar(targetMember.getAvatar())
                .collectionCnt(collectionService.getCollectionCnt(targetId))
                .scrappedCollectionCnt(interestingCollectionService.getScrappedCollectionCnt(targetId))
                .isFollowed(followingService.checkIfFollow(memberId, targetId))
                .followerCnt(followingService.getFollowerCnt(targetId))
                .followingCnt(followingService.getFolloweeCnt(targetId))
                .build();
        return makeResponse(showOtherMemberResponseDTO);
    }

    @PostMapping("/{targetId}/follow")
    public ApiResponse followMember(@PathVariable Long targetId) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member targetMember = memberService.findOneById(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "존재하지 않는 멤버입니다.");
        }
        followingService.follow(memberId, targetId);
        return makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage());
    }

    @DeleteMapping("/{targetId}/follow")
    public ApiResponse unfollowMember(@PathVariable Long targetId) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member targetMember = memberService.findOneById(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "존재하지 않는 멤버입니다.");
        }
        followingService.unfollow(memberId, targetId);
        return makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage());
    }

    // 반환 DTO 만들고 반환하기
    @GetMapping("/me/followers")
    public ApiResponse getFollowers() {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<Member> followers = memberService.getFollowers(memberId);
        List<ShowSimpleMemberResponseDTO> showSimpleMemberResponseDTOs = followers.stream()
                .map(f -> ShowSimpleMemberResponseDTO.builder()
                        .id(f.getId())
                        .nickname(f.getNickname())
                        .avatar(f.getAvatar())
                        .collectionCnt(collectionService.getCollectionCnt(f.getId()))
                        .build())
                .collect(Collectors.toList());
        return makeResponse(showSimpleMemberResponseDTOs);
    }

    // 반환 DTO 만들고 반환하기
    @GetMapping("/me/followings")
    public ApiResponse getFollowings() {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<Member> followings = memberService.getFollowings(memberId);
        List<ShowSimpleMemberResponseDTO> showSimpleMemberResponseDTOs = followings.stream()
                .map(f -> ShowSimpleMemberResponseDTO.builder()
                        .id(f.getId())
                        .nickname(f.getNickname())
                        .avatar(f.getAvatar())
                        .collectionCnt(collectionService.getCollectionCnt(f.getId()))
                        .build())
                .collect(Collectors.toList());
        return makeResponse(showSimpleMemberResponseDTOs);
    }
    public <T> ApiResponse<T> makeResponse(List<T> result) {
        return new ApiResponse<>(result);
    }

    public <T> ApiResponse<T> makeResponse(T result) {
        return makeResponse(Collections.singletonList(result));
    }

    public ApiResponse makeResponse(int code, String message) {
        return new ApiResponse(code, message);
    }
}
