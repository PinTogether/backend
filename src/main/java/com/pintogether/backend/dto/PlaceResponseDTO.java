package com.pintogether.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceResponseDTO {

    private Long id;

    private String name;

    private String roadNameAddress;

    private Long pinCnt;

    private double latitude;

    private double longitude;

    private String category;

    private boolean starred;

}
