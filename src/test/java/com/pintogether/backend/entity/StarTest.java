package com.pintogether.backend.entity;

import com.pintogether.backend.entity.components.Address;
import com.pintogether.backend.entity.enums.PlaceSource;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class StarTest {

    @PersistenceContext
    private EntityManager entityManager;
    private Member member;
    private Place place;
    private Star star;


    @BeforeEach
    void setUp() {
        // 더미 member1 생성
        member = Member.builder()
                .nickname("spew11")
                .registrationSource(RegistrationSource.GOOGLE)
                .registrationId("dhbdg11@gmail.com")
                .roleType(RoleType.ROLE_MEMBER).build();

        entityManager.persist(member);

        // 더미 Place 생성
        Address address = Address.builder()
                .numberAddress("123 Baker Street")
                .latitude(37.5656F)
                .longitude(126.9780F)
                .build();


        place = Place.builder()
                .address(address)
                .placeSource(PlaceSource.LOCAL_DATA)
                .placeSourceId("123")
                .name("GOOKBAB")
                .category("음식점").build();

        entityManager.persist(place);

        star = Star.builder().member(member).place(place).build();
        entityManager.persist(star);

        entityManager.flush();
    }

    @AfterEach
    void tearDown() {
        entityManager.createNativeQuery("DELETE FROM star").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM place").executeUpdate();

    }

    @Test
    @Transactional
    void testFindStar() {
        // 조회
        Star retrievedStar = entityManager.find(Star.class, member.getId());

        assertThat(retrievedStar).isNotNull();
    }

    @Test
    @Transactional
    void testCascadeMember() {
        // 멤버 'member' 삭제
        entityManager.remove(member);
        entityManager.flush();
        entityManager.clear();

        // 멤버를 참조하던 star가 삭제되었는지 확인
        Star deletedStar = entityManager.find(Star.class, star.getId());

        // 삭제된 star는 조회되어서는 안된다.
        assertThat(deletedStar).isNull();
    }

    @Test
    @Transactional
    void testCascadePlace() {
        entityManager.remove(place);
        entityManager.flush();
        entityManager.clear();

        // 장소를 참조하던 찜이 삭제되었는지 확인
        Star deletedStar = entityManager.find(Star.class, star.getId());

        // 삭제된 찜은 조회되어서는 안된다.
        assertThat(deletedStar).isNull();
    }
}