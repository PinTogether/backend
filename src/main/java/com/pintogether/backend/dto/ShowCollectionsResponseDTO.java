package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowCollectionsResponseDTO {
    private Long id;
    private String title;
    private Long writerId;
    private String writerMembername;
    private String thumbnail;
    private int likeCnt;
    private int pinCnt;
    private int scrapCnt;
    private boolean isScrapped;
    private boolean isLiked;
}
