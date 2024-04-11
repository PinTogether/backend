package com.pintogether.backend.dto;

import com.pintogether.backend.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowNotificationResponseDTO {
    private Long subjectId;
    private String subject;
    private NotificationType notificationType;
    private Long objectId;
    private String object;
}