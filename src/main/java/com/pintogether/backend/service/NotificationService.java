package com.pintogether.backend.service;

import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Notification;
import com.pintogether.backend.entity.enums.NotificationType;
import com.pintogether.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    // 내가 누군가의 컬렉션을 스크랩해서 그 컬렉션 주인에게 알림이 생성됌
    public void scrapCollection(Member member, Collection collection) {
        Notification notification = Notification.builder()
                .member(collection.getMember())
                .subject(member)
                .notificationType(NotificationType.SCRAP_COLLECTION)
                .objectId(collection.getId())
                .object(collection.getTitle())
                .build();
        notificationRepository.save(notification);
        memberService.increaeAlertCnt(collection.getMember());
    }

    // 내가 누군가의 컬렉션에 댓글을 달아서 그 컬렉션 주인에게 알림이 생성됌
    public void commentOnCollection(Member member, Collection collection) {
        Notification notification = Notification.builder()
                .member(collection.getMember())
                .subject(member)
                .notificationType(NotificationType.COMMENT_ON_COLLECTION)
                .objectId(collection.getId())
                .object(collection.getTitle())
                .build();
        notificationRepository.save(notification);
        memberService.increaeAlertCnt(collection.getMember());
    }

    // 내가 누군가의 컬렉션에 좋아요를 눌러서 좋아요 당한 사람에게 알림이 생성됌
    public void likeCollection(Member member, Collection collection) {
        Notification notification = Notification.builder()
                .member(collection.getMember())
                .subject(member)
                .notificationType(NotificationType.LIKE_COLLECTION)
                .objectId(collection.getId())
                .object(collection.getTitle())
                .build();
        notificationRepository.save(notification);
        memberService.increaeAlertCnt(collection.getMember());
    }

    // 내가 팔로우를 해서 팔로우 당한 사람에게 알림이 생성됌
    public void follow(Member member, Member followee) {
        Notification notification = Notification.builder()
                .member(followee)
                .subject(member)
                .notificationType(NotificationType.FOLLOW)
                .objectId(null)
                .object(null)
                .build();
        notificationRepository.save(notification);
        memberService.increaeAlertCnt(followee);
    }

    // 내가 새로운 컬렉션을 생성해서 나를 팔로우하던 사람들에게 알림이 생성됌
    public void createCollection(Member member, Collection collection) {
        for (Member follower: memberService.getFollowers(member.getId())) {
            Notification notification = Notification.builder()
                    .member(follower)
                    .subject(member)
                    .notificationType(NotificationType.CREATE_COLLECTION)
                    .objectId(collection.getId())
                    .object(collection.getTitle())
                    .build();
            notificationRepository.save(notification);
            memberService.increaeAlertCnt(follower);
        }
    }

    public List<Notification> getAllNotification(Member member) {
        memberService.clearAlertCnt(member);
        return notificationRepository.findAllByMemberIdOrderByIdDesc(member.getId());
    }
}
