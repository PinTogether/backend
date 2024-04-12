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

    private String placeName;

    private Long collectionId;

    private String collectionTitle;

    private String writerMembername;

    private Long writerId;

    private String avatarImage;

    private String review;

    private String createdAt;

    private String[] imagePaths;

    private String[] tags;

}
