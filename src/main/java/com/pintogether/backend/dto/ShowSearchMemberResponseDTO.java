package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ShowSearchMemberResponseDTO {
    private Long id;

    private String name;

    private String membername;

    private String avatar;

    private int collectionCnt;

    private boolean isFollowed;
}
