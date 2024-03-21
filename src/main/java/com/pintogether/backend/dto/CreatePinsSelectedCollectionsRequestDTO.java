package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreatePinsSelectedCollectionsRequestDTO {

    @NotNull
    private List<Long> collectionId;

    @NotNull
    private Long placeId;

}
