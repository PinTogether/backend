package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Getter
public class UpdatePinReqeustDTO {

    @Size(min = 0, max = 1000, message = "1000자 이내로 작성해주세요.")
    private String review;

    @Size(max = 5)
    private String[] imagePaths;

    @Size(max = 5)
    private String[] tags;

}
