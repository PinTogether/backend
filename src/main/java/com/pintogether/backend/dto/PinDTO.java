package com.pintogether.backend.dto;

import com.pintogether.backend.entity.PinImage;
import com.pintogether.backend.entity.PinTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PinDTO {

    private Long id;

    private Long collectionId;

    private String collectionTitle;

    private String writer;

    private String avatarImage;

    private String review;

    private LocalDateTime createdAt;

    private String[] imagePaths;

    private String[] tags;

}
