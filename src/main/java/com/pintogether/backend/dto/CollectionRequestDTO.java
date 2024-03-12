package com.pintogether.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRequestDTO {
    @NotNull
    @Size(min = 1, max = 20, message = "TThe title must be between 1 and 20 characters long.")
    private String title;

    @NotNull
    private String thumbnail;

    @NotNull
    @Size(max = 250, message = "The maximum number of details is 250.")
    private String details;

    @NotNull
    @Size(max = 5, message = "The maximum number of tags is 5.")
    // 태그 글자도 다 확인해야됌.
    private List<String> tags;
}