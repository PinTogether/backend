package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowCollectionsForAddingPinResponseDTO {
    private Long id;
    private String title;
    private String thumbnail;
    private int likeCnt;
    private int pinCnt;
    private int scrapCnt;
    private boolean pinned;
}
