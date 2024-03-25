package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowPinResponseDTO {

    private Long id;

    private Long placeId;

    private Long collectionId;

    private String collectionTitle;

    private String writer;

    private String avatarImage;

    private String review;

    private LocalDateTime createdAt;

    private String[] imagePaths;

    private String[] tags;

}
