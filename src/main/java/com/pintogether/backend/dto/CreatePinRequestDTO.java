package com.pintogether.backend.dto;

import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.PinImage;
import com.pintogether.backend.entity.PinTag;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreatePinRequestDTO {

    @NotNull
    private Long placeId;

    @NotNull
    private Long collectionId;

    @NotNull
    private String review;

    private String[] tags;

    private String[] imagePaths;

}
