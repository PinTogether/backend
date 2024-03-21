package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePinSelectedPlacesRequestDTO {

    @NotNull
    private Long collectionId;

    @NotNull
    private Long[] placeId;

}
