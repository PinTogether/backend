package com.pintogether.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePinReqeustDTO {

    @Size(min = 0, max = 1000, message = "1000자 이내로 작성해주세요.")
    private String review;

    @Size(max = 5)
    private List<@Size(max = 200, message = "이미지 주소의 길이가 너무 깁니다.")String> imagePaths;

    @Size(max = 5)
    private List<@Size(max = 20, message = "각 태그는 20자 이내로 작성해주세요.")String> tags;

}
