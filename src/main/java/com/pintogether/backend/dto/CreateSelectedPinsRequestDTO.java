package com.pintogether.backend.dto;

import lombok.Getter;

@Getter
public class CreateSelectedPinsRequestDTO {

    private Long collectionId;

    private Long[] placeId;

}
