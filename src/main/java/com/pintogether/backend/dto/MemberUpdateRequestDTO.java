package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequestDTO {
    @NotNull @NonNull
    @Size(min = 1, max = 10, message = "The nickname must be between 1 and 10 characters long.")
    private String nickname;
    @NotNull @NotNull
    private String avatar;
}
