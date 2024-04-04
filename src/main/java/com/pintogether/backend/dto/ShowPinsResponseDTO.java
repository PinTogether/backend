package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ShowPinsResponseDTO {
    private Long id;
    private Long collectionId;
    private String collectionTitle;
    private Long writerId;
    private String writerMembername;
    private String avatarImage;
    private String review;
    private List<String> tags;
    private List<String> imagePaths;
    private String createdAt;
    private Long placeId;
    private String placeName;
    private String category;
    private String roadNameAddress;
    private double latitude;
    private double longitude;
    private int saveCnt;
    private int pinCnt;
    private boolean starred;
}
