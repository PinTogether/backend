package com.pintogether.backend.dto;

import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowMemberResponseDTO {
    private Long id;

    private String name;

    private String membername;

    private String bio;

    private RegistrationSource registrationSource;

    private RoleType role;

    private String avatar;

    private int collectionCnt;

    private int scrappedCollectionCnt;

    private int followerCnt;

    private int followingCnt;
}
