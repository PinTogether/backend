package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class S3CollectionThumbnailRequestDTO {
    @NotNull
    @Pattern(regexp = "(?i)image/jpeg|image/png|image/jpg", message = "유효하지 않은 타입입니다. 제공하는 타입은 image/jpeg, image/png, image/jpg 입니다.")
    private String contentType;
}
