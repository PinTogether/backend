package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.CurrentMember;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Notification;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.*;
import com.pintogether.backend.websocket.WebSocketHandler;
import com.pintogether.backend.websocket.WebSocketService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final FollowingService followingService;
    private final CollectionService collectionService;
    private final InterestingCollectionService interestingCollectionService;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;


    @GetMapping("/me")
    public ApiResponse getMemberInformation() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member foundMember = memberService.getMember(id);
        ShowMemberResponseDTO showMemberResponseDTO = ShowMemberResponseDTO.builder()
                .id(foundMember.getId())
                .name(foundMember.getName())
                .membername(foundMember.getMembername())
                .bio(foundMember.getBio())
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

    @GetMapping("/{membername}")
    public ApiResponse getOtherMemberInformation(@ThisMember Member member, @PathVariable String membername) {
        Member targetMember = memberService.getMemberByMembername(membername);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage());
        }
        ShowOtherMemberResponseDTO showOtherMemberResponseDTO = ShowOtherMemberResponseDTO.builder()
                .id(targetMember.getId())
                .name(targetMember.getName())
                .membername(targetMember.getMembername())
                .avatar(targetMember.getAvatar())
                .bio(targetMember.getBio())
                .collectionCnt(memberService.getCollectionCnt(targetMember.getId()))
                .scrappedCollectionCnt(memberService.getScrappedCollectionCnt(targetMember.getId()))
                .isFollowed(member != null ? followingService.checkIfFollow(member.getId(), targetMember.getId()) : false)
                .followerCnt(memberService.getFollowerCnt(targetMember.getId()))
                .followingCnt(memberService.getFolloweeCnt(targetMember.getId()))
                .build();
        return ApiResponse.makeResponse(showOtherMemberResponseDTO);
    }

    @GetMapping("id/{targetId}")
    public ApiResponse getOtherMemberInformation(@ThisMember Member member, @PathVariable Long targetId) {
        Member targetMember = memberService.getMember(targetId);
        if (targetMember == null) {
            throw new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage());
        }
        ShowOtherMemberResponseDTO showOtherMemberResponseDTO = ShowOtherMemberResponseDTO.builder()
                .id(targetId)
                .name(targetMember.getName())
                .membername(targetMember.getMembername())
                .avatar(targetMember.getAvatar())
                .bio(targetMember.getBio())
                .collectionCnt(memberService.getCollectionCnt(targetId))
                .scrappedCollectionCnt(memberService.getScrappedCollectionCnt(targetId))
                .isFollowed(member != null ? followingService.checkIfFollow(member.getId(), targetId) : false)
                .followerCnt(memberService.getFollowerCnt(targetId))
                .followingCnt(memberService.getFolloweeCnt(targetId))
                .build();
        return ApiResponse.makeResponse(showOtherMemberResponseDTO);
    }

    @PostMapping("/{targetId}/follow")
    public ApiResponse followMember(@ThisMember Member member, @CurrentMember Member target, HttpServletResponse response) {
        followingService.follow(member.getId(), target.getId());
        notificationService.follow(member, target);
        webSocketService.sendNotificationCntToMember(target);
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
                        .name(f.getName())
                        .membername(f.getMembername())
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
                        .name(f.getName())
                        .membername(f.getMembername())
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
                        .writerMembername(c.getMember().getMembername())
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
                        .writerMembername(c.getMember().getMembername())
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

    @GetMapping("/profile-setting/membername-valid")
    public ApiResponse checkIfDuplicatedMembername(@RequestParam(value = "membername", required = true) String membername) {
        return ApiResponse.makeResponse(ShowMembernameValidationResponseDTO.builder().valid(!memberService.checkIfDuplicatedMembername(membername)).build());
    }

    @PostMapping("/me/notifications")
    public ApiResponse getAllNotifications(@ThisMember Member member) {
        List<Notification> notifications = notificationService.getAllNotification(member);

        List<ShowNotificationResponseDTO> today = new ArrayList<>();
        List<ShowNotificationResponseDTO> yesterday = new ArrayList<>();
        List<ShowNotificationResponseDTO> aWeekAgo = new ArrayList<>();
        List<ShowNotificationResponseDTO> withinAMonth = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for (Notification notification : notifications) {
            ShowNotificationResponseDTO dto = ShowNotificationResponseDTO.builder()
                    .subjectId(notification.getSubject().getId())
                    .subject(notification.getSubject().getMembername())
                    .notificationType(notification.getNotificationType())
                    .object(notification.getObject())
                    .build();

            LocalDate notificationDate = notification.getCreatedAt().toLocalDate();
            if (notificationDate.equals(now)) {
                today.add(dto);
            } else if (notificationDate.equals(now.minusDays(1))) {
                yesterday.add(dto);
            } else if (notificationDate.isAfter(now.minusWeeks(1)) && !notificationDate.isEqual(now().minusDays(1))) {
                aWeekAgo.add(dto);
            } else if (notificationDate.isAfter(now.minusMonths(1)) && notificationDate.isBefore(now().minusWeeks(1))) {
                withinAMonth.add(dto);
            }
        }
        return ApiResponse.makeResponse(ShowNotificationsResponseDTO.builder()
                .today(today)
                .yesterday(yesterday)
                .aWeekAgo(aWeekAgo)
                .withinAMonth(withinAMonth)
                .build());
    }

    @GetMapping("/me/alerts")
    public ApiResponse getAlertCnt(@ThisMember Member member) {
        return ApiResponse.makeResponse(
                ShowAlertCntResponseDTO.builder()
                        .alertCnt(member.getAlertCnt())
                        .build());
    }
}
