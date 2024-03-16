package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCollectionRequestDTO {
    @NotNull
    @Size(min = 1, max = 20, message = "제목은 20자 이내로 작성해주세요.")
    private String title;

    @NotNull
    private String thumbnail;

    @NotNull
    @Size(max = 250, message = "세부내용은 250자 이내로 작성해주세요.")
    private String details;

    @Size(max = 5, message = "태그는 5개 이내만 가능합니다.")
    private List<@Size(max = 20, message = "각 태그는 20자 이내로 작성해주세요.") String> tags;
}
