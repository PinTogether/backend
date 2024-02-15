package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.RegistrationSource;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Member {

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

<<<<<<< HEAD:src/main/java/com/pintogether/backend/domain/User.java
    @Column(name = "registration_key")
    private String registrationKey;
=======
    @Column(name = "registration_id")
    private String registrationId;
>>>>>>> ddc274eded8d2db9c3079834de119c732b70390d:src/main/java/com/pintogether/backend/entity/Member.java

    @Builder
    public Member(String nickname, RegistrationSource registrationSource, String registrationId) {
        this.nickname = nickname;
        this.avatar = "";
        this.collectionCnt = 0;
        this.scrappedCollectionCnt = 0;
        this.registrationSource = registrationSource;
<<<<<<< HEAD:src/main/java/com/pintogether/backend/domain/User.java
        this.registrationKey = registrationKey;
=======
        this.registrationId = registrationId;
>>>>>>> ddc274eded8d2db9c3079834de119c732b70390d:src/main/java/com/pintogether/backend/entity/Member.java
    }

}
