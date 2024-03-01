//package com.pintogether.backend.entity;
//
//import com.pintogether.backend.entity.enums.InterestType;
//import com.pintogether.backend.entity.enums.RegistrationSource;
//import com.pintogether.backend.entity.enums.RoleType;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//class InterestingCollectionTest {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private Member member;
//
//    private Collection collection;
//
//    private InterestingCollection likesCollection;
//
//    private InterestingCollection scrapCollection;
//
//    @BeforeEach
//    void setUp() {
//        // 더미 member 생성
//        member = Member.builder()
//                .nickname("spew11")
//                .registrationSource(RegistrationSource.GOOGLE)
//                .registrationId("dhbdg11@gmail.com")
//                .roleType(RoleType.ROLE_MEMBER)
//                .build();
//        entityManager.persist(member);
//
//        // 더미 collection 생성
//        collection = Collection.builder()
//                .member(member)
//                .title("My favorite")
//                .thumbnail("thumbnailUrl")
//                .details("My favorite restaurant in Gangnam")
//                .build();
//        entityManager.persist(collection);
//
//
//        // 더미 likesCollection 생성
//        likesCollection = InterestingCollection.builder().member(member).collection(collection).interestType(InterestType.LIKES).build();
//        entityManager.persist(likesCollection);
//
//        // 더미 scrapCollection 생성
//        scrapCollection = InterestingCollection.builder().member(member).collection(collection).interestType(InterestType.SCRAP).build();
//        entityManager.persist(scrapCollection);
//
//        entityManager.flush();
//    }
//
//    @AfterEach
//    void tearDown() {
//        entityManager.createNativeQuery("DELETE FROM interesting_collection").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM collection").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
//    }
//
//    @Test
//    @Transactional
//    void testFindInterestingCollection() {
//        // 관심 컬렉션 조회
//        InterestingCollection retrievedLikesCollection = entityManager.find(InterestingCollection.class, likesCollection.getId());
//        InterestingCollection retrievedScrapCollection = entityManager.find(InterestingCollection.class, scrapCollection.getId());
//        assertThat(retrievedLikesCollection).isNotNull();
//        assertThat(retrievedScrapCollection).isNotNull();
//    }
//
//    @Test
//    @Transactional
//    void testCascadeMember() {
//        // 멤버 삭제
//        entityManager.remove(member);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 멤버 삭제후 참조하고 있던 관심 컬렉션 삭제되는지 확인
//        List<InterestingCollection> remainingCollections = entityManager.createQuery("SELECT ic FROM InterestingCollection ic", InterestingCollection.class)
//                .getResultList();
//        assertThat(remainingCollections).isEmpty();
//    }
//
//    @Test
//    @Transactional
//    void testCascadeCollection() {
//        // 컬렉션 삭제
//        entityManager.remove(collection);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 컬렉션 삭제후 참조하고 있던 관심 컬렉션 삭제되는지 확인
//        List<InterestingCollection> remainingCollections = entityManager.createQuery("SELECT ic FROM InterestingCollection ic", InterestingCollection.class)
//                .getResultList();
//        assertThat(remainingCollections).isEmpty();
//    }
//}