package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowNotificationsResponseDTO {
    private List<ShowNotificationResponseDTO> today;
    private List<ShowNotificationResponseDTO> yesterday;
    private List<ShowNotificationResponseDTO> aWeekAgo;
    private List<ShowNotificationResponseDTO> withinAMonth;
}
