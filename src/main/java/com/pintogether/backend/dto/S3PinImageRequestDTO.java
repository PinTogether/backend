package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class S3PinImageRequestDTO {

    @NotNull
    @Size(min=1, max=5)
    private List<@Pattern(regexp = "(?i)image/jpeg|image/png|image/jpg", message = "유효하지 않은 타입입니다. 제공하는 타입은 image/jpeg, image/png, image/jpg 입니다.")String> contentType;

}
