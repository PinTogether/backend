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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String avatar;

    @Column(name = "collection_cnt")
    private int collectionCnt;

    @Column(name = "scrapped_collection_cnt")
    private int scrappedCollectionCnt;

    @Column(name = "registration_source")
    @Enumerated(EnumType.STRING)
    private RegistrationSource registrationSource;

    @Column(name = "registration_id")
    private String registrationId;

    @Builder
    public User(String nickname, RegistrationSource registrationSource, String registrationId) {
        this.nickname = nickname;
        this.avatar = "";
        this.collectionCnt = 0;
        this.scrappedCollectionCnt = 0;
        this.registrationSource = registrationSource;
        this.registrationId = registrationId;
    }

}
