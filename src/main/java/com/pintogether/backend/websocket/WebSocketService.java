package com.pintogether.backend.websocket;

import com.pintogether.backend.dto.ShowAlertCntResponseDTO;
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

    public void sendNotificationCntToMember(Member member) {
        ShowAlertCntResponseDTO showAlertCntResponseDTO = ShowAlertCntResponseDTO.builder()
                .alertCnt(member.getAlertCnt())
                .build();
        try {
            webSocketHandler.sendMessageToMember(member.getId(), showAlertCntResponseDTO.toString());
        } catch (IOException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
        }
    }

    public void sendNotificationCntToFollower(Member member) {
        for (Member follower: memberService.getFollowers(member.getId())) {
            ShowAlertCntResponseDTO showAlertCntResponseDTO = ShowAlertCntResponseDTO.builder()
                    .alertCnt(follower.getAlertCnt())
                    .build();
            try {
                webSocketHandler.sendMessageToMember(follower.getId(), showAlertCntResponseDTO.toString());
            } catch (IOException e) {
                throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, CustomStatusMessage.SOCKET_IO_EXCEPTION.getMessage());
            }
        }
    }
}