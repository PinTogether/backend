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

    private String writerName;

    private String writerMembername;

    private String writerAvatar;

    private String contents;

    private String createdAt;
}
