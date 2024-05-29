package com.pintogether.backend.entity;

import com.pintogether.backend.dto.UpdateMemberRequestDTO;
import com.pintogether.backend.entity.enums.EntityStatus;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "entity_status='ACTIVE'")
@SQLDelete(sql = "UPDATE member SET entity_status='DELETE' WHERE id=?")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Setter
    @NotNull
    private String membername;

    @NotNull
    private String bio;

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

    private int alertCnt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Collection> collections = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    @Builder
    public Member(String name, String membername, String bio, String avatar, RegistrationSource registrationSource, String registrationId, RoleType roleType) {
        this.name = name;
        this.membername = membername;
        this.avatar = avatar;
        this.bio = bio;
        this.registrationSource = registrationSource;
        this.registrationId = registrationId;
        this.roleType = roleType;
        this.alertCnt = 0;
    }

    public void updateMember(final UpdateMemberRequestDTO updateMemberRequestDTO) {
        this.name = updateMemberRequestDTO.getName();
        this.membername = updateMemberRequestDTO.getMembername();
        this.avatar = updateMemberRequestDTO.getAvatar();
        this.bio = updateMemberRequestDTO.getBio();
    }

    public void increaseAlertCnt() {
        this.alertCnt += 1;
    }
    public void clearAlertCnt() {
        this.alertCnt = 0;
    }
}
