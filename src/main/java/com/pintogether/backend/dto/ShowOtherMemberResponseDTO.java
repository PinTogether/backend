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
    private String nickname;

    private String avatar;

    private int collectionCnt;

    private int scrappedCollectionCnt;

    private boolean isFollowed;

    private int followerCnt;

    private int followingCnt;
}
