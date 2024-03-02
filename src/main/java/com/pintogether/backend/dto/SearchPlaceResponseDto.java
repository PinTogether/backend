package com.pintogether.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchPlaceResponseDto {

    private Long id;

    private String name;

    private String roadNameAddress;

    private int pinCnt;

    private double latitude;

    private double longitude;

}
