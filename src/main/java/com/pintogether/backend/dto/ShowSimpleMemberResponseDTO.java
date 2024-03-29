package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowSimpleMemberResponseDTO {
    private Long id;

    private String name;

    private String membername;

    private String avatar;

    private int collectionCnt;
}
