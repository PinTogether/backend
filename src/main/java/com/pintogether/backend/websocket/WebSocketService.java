package com.pintogether.backend.websocket;

import com.pintogether.backend.dto.ShowNotificationResponseDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionComment;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class WebSocketService {
    private final WebSocketHandler webSocketHandler;
    private final MemberService memberService;

    public void scrapCollection(Member collector, Collection collection) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(collector.getMembername())
                .notificationType(NotificationType.SCRAP_COLLECTION)
                .object(collection.getTitle())
                .build();
        try {
            webSocketHandler.sendMessageToMember(collection.getMember().getId(), showNotificationResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void commentOnCollection(Member writer, Collection collection) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(writer.getMembername())
                .notificationType(NotificationType.COMMENT_ON_COLLECTION)
                .object(collection.getTitle())
                .build();
        try {
            webSocketHandler.sendMessageToMember(collection.getMember().getId(), showNotificationResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void likeCollection(Member liker, Collection collection) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(liker.getMembername())
                .notificationType(NotificationType.LIKE_COLLECTION)
                .object(collection.getTitle())
                .build();
        try {
            webSocketHandler.sendMessageToMember(collection.getMember().getId(), showNotificationResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void follow(Member follower, Member followee) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(follower.getMembername())
                .notificationType(NotificationType.FOLLOW)
                .object(null)
                .build();
        try {
            webSocketHandler.sendMessageToMember(followee.getId(), showNotificationResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void createCollection(Member creator, Collection collection) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(creator.getMembername())
                .notificationType(NotificationType.CREATE_COLLECTION)
                .object(collection.getTitle())
                .build();
        try {
            for (Member follower: memberService.getFollowers(creator.getId())) {
                webSocketHandler.sendMessageToMember(follower.getId(), showNotificationResponseDTO.toString());
            }
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void removeComment(CollectionComment collectionComment) {
        ShowNotificationResponseDTO showNotificationResponseDTO = ShowNotificationResponseDTO.builder()
                .subject(null)
                .notificationType(NotificationType.COMMENT_DELETED)
                .object(collectionComment.getCollection().getTitle())
                .build();
        try {
            webSocketHandler.sendMessageToMember(collectionComment.getMember().getId(), showNotificationResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }
}
