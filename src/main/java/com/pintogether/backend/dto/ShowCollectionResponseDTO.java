package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowCollectionResponseDTO {
    private Long id;

    private String title;

    private Long writerId;

    private String writer;

    private String thumbnail;

    private int likeCnt;

    private int pinCnt;

    private int scrapCnt;

    private boolean isScrapped;

    private boolean isLiked;
}
