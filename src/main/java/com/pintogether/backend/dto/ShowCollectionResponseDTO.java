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
public class ShowCollectionResponseDTO {
    private Long id;

    private String title;

    private String details;

    private Long writerId;

    private String writerMembername;

    private String thumbnail;

    private int likeCnt;

    private int pinCnt;

    private int scrapCnt;

    private boolean isScrapped;

    private boolean isLiked;

    private List<String> tags;
}
