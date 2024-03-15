package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowCollectionCommentResponseDTO {
    private Long id;

    private Long writerId;

    private String writer;

    private String contents;

    private LocalDateTime createdAt;
}
