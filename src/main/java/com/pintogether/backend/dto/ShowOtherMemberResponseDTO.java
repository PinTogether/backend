package com.pintogether.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShowOtherMemberResponseDTO {
    private Long id;

    private String name;

    private String membername;

    private String avatar;

    private String bio;

    private int collectionCnt;

    private int scrappedCollectionCnt;

    private boolean isFollowed;

    private int followerCnt;

    private int followingCnt;
}
