package com.pintogether.backend.dto;

import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.PinImage;
import com.pintogether.backend.entity.PinTag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreatePinRequestDTO {

    private Long placeId;

    private Long collectionId;

    private String review;

    private String[] tags;

    private String[] imagePaths;

}
