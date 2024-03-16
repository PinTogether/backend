package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequestDTO {
    @NotNull
    @Size(min = 1, max = 16, message = "닉네임은 16자 이내로 작성해주세요.")
    private String nickname;
    @NotNull
    private String avatar;
}
