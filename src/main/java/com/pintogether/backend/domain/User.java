package com.pintogether.backend.domain;

import com.pintogether.backend.domain.enums.RegistrationSource;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String avatar;

    @Column(name = "collection_cnt")
    private int collectionCnt;

    @Column(name = "likes_collection_cnt")
    private int likesCollectionCnt;

    @Column(name = "registration_source")
    @Enumerated(EnumType.STRING)
    private RegistrationSource registrationSource;

    @Column(name = "registration_pk")
    private String registrationPk;

    @Builder
    public User(String nickname, RegistrationSource registrationSource, String registrationPk) {
        this.nickname = nickname;
        this.avatar = "";
        this.collectionCnt = 0;
        this.likesCollectionCnt = 0;
        this.registrationSource = registrationSource;
        this.registrationPk = registrationPk;
    }

}
