//package com.pintogether.backend.entity;
//
//import com.pintogether.backend.entity.enums.RegistrationSource;
//import com.pintogether.backend.entity.enums.RoleType;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//import static org.assertj.core.api.Assertions.assertThat;
//@DataJpaTest
//class FollowingTest {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private Member me;
//    private Member you;
//    private Following meToYou;
//
//    @BeforeEach
//    void setUp() {
//        // 더미 member 생성
//        me = Member.builder()
//                .nickname("spew11")
//                .registrationSource(RegistrationSource.GOOGLE)
//                .registrationId("dhbdg11@gmail.com")
//                .roleType(RoleType.ROLE_MEMBER)
//                .build();
//        entityManager.persist(me);
//
//        you = Member.builder()
//                .nickname("tehyoyee")
//                .registrationSource(RegistrationSource.GOOGLE)
//                .registrationId("1234")
//                .roleType(RoleType.ROLE_MEMBER)
//                .build();
//        entityManager.persist(you);
//
//        // 더미 following 생성
//        meToYou = Following.builder().follower(me).followee(you).build();
//        entityManager.persist(meToYou);
//
//        entityManager.flush();
//    }
//
//    @AfterEach
//    void tearDown() {
//        entityManager.createNativeQuery("DELETE FROM following").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
//    }
//
//    @Test
//    @Transactional
//    void testFindFollowing () {
//        // 팔로잉 조회
//        Following retrievedMeToYou = entityManager.find(Following.class, meToYou.getId());
//        assertThat(retrievedMeToYou).isNotNull();
//    }
//
//    @Test
//    @Transactional
//    void testCascadeFollower () {
//        Following retrievedMeToYou = entityManager.find(Following.class, meToYou.getId());
//
//        // 멤버 'me' 삭제
//        entityManager.remove(me);
//        entityManager.flush();
//        entityManager.clear();
//
//        // follower 'me' 삭제 후 'meToYou'가 삭제되었는지 확인
//        Following deletedMeToYou = entityManager.find(Following.class, meToYou.getId());
//
//        // 삭제된 엔티티는 조회되어서는 안된다.
//        assertThat(deletedMeToYou).isNull();
//    }
//
//    @Test
//    @Transactional
//    void testCascadeFollowee () {
//        Following retrievedMeToYou = entityManager.find(Following.class, meToYou.getId());
//
//        // 멤버 'me' 삭제
//        entityManager.remove(you);
//        entityManager.flush();
//        entityManager.clear();
//
//        // followee 'you' 삭제 후 'meToYou'가 삭제되었는지 확인
//        Following deletedMeToYou = entityManager.find(Following.class, meToYou.getId());
//
//        // 삭제된 엔티티는 조회되어서는 안된다.
//        assertThat(deletedMeToYou).isNull();
//    }
//}