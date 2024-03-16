package com.pintogether.backend.entity;

import com.pintogether.backend.dto.UpdateMemberRequestDTO;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @NotNull
    private String avatar;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RegistrationSource registrationSource;

    @NotNull
    private String registrationId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Collection> collections = new ArrayList<>();

    @Builder
    public Member(String nickname, RegistrationSource registrationSource, String registrationId, RoleType roleType) {
        this.nickname = nickname; // 닉네임 생성기로 생성
        this.avatar = ""; // 추후에 defualt 이미지 주소로 대치
        this.registrationSource = registrationSource;
        this.registrationId = registrationId;
        this.roleType = roleType;
    }

    public void updateMember(final UpdateMemberRequestDTO updateMemberRequestDTO) {
        this.nickname = updateMemberRequestDTO.getNickname();
        this.avatar = updateMemberRequestDTO.getAvatar();
    }
}
