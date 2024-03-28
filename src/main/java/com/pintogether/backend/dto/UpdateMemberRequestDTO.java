package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequestDTO {
    @NotNull
    @Size(min = 1, max = 16, message = "이름은 16자 이내로 작성해주세요.")
    private String nickname;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?\\\\|]{3,30}$", message = "사용자 이름은 3에서 30자 이내의 길이로, 영문자, 숫자, 일반 기호로 작성해주세요.")
    private String uniqueName;

    @NotNull
    private String avatar;
}
