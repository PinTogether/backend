//package com.pintogether.backend.entity;
//
//import com.pintogether.backend.entity.components.Address;
//import com.pintogether.backend.entity.enums.PlaceSource;
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
//class CollectionTest {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private Member member;
//
//    private Collection collection;
//    private Place place;
//
//    private CollectionTag tag1;
//    private CollectionTag tag2;
//
//    private CollectionComment comment1;
//
//    private CollectionComment comment2;
//
//    private Pin pin;
//
//    private PinTag pinTag1;
//    private PinTag pinTag2;
//    private PinImage pinImage1;
//    private PinImage pinImage2;
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
//        // 더미 Place 생성
//        Address address = Address.builder()
//                .numberAddress("123 Baker Street")
//                .latitude(37.5656F)
//                .longitude(126.9780F)
//                .build();
//        place = Place.builder()
//                .address(address)
//                .placeSource(PlaceSource.LOCAL_DATA)
//                .placeSourceId("123")
//                .name("GOOKBAB")
//                .category("restruant").build();
//
//        entityManager.persist(place);
//
//        // 더미 collection 생성
//        collection = Collection.builder()
//                .member(member)
//                .title("My favorite")
//                .thumbnail("thumbnailUrl")
//                .details("My favorite restaurant in Gangnam")
//                .build();
//
//        // 연관된 CollectionTag와 CollectionImage 추가
//        tag1 = CollectionTag.builder().collection(collection).tag("Adventure").build();
//        tag2 = CollectionTag.builder().collection(collection).tag("hit").build();
//
//        comment1 = CollectionComment.builder()
//                .collection(collection)
//                .member(member)
//                .contents("Great collection!")
//                .build();
//
//        comment2 = CollectionComment.builder()
//                .collection(collection)
//                .member(member)
//                .contents("Can't wait to see more!")
//                .build();
//        // collection 저장
//        entityManager.persist(collection);
//
//
//        // 더미 Pin 생성
//        pin = Pin.builder()
//                .place(place)
//                .collection(collection)
//                .review("Great place!")
//                .build();
//
//        pinTag1 = PinTag.builder().pin(pin).tag("glory").build();
//        pinTag2 = PinTag.builder().pin(pin).tag("monster").build();
//        pinImage1 = PinImage.builder().pin(pin).imagePath("path1").build();
//        pinImage2 = PinImage.builder().pin(pin).imagePath("path2").build();
//        entityManager.persist(pin);
//
//        entityManager.flush();
//    }
//
//    @AfterEach
//    void tearDown() {
//        entityManager.createNativeQuery("DELETE FROM collection_comment").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM collection_tag").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM collection").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
//    }
//
//    @Test
//    @Transactional
//    void testFindCollection() {
//        // 컬렉션 조회
//        Collection retrievedCollection = entityManager.find(Collection.class, collection.getId());
//        assertThat(retrievedCollection).isNotNull();
//        assertThat(retrievedCollection.getCollectionTags()).hasSize(2);
//        assertThat(retrievedCollection.getCollectionTags()).extracting("tag").containsExactlyInAnyOrder("Adventure", "hit");
//    }
//
//    @Test
//    @Transactional
//    void testCascadeCollectionTag() {
//        Collection retrievedCollection = entityManager.find(Collection.class, collection.getId());
//        // 컬렉션 삭제
//        entityManager.remove(retrievedCollection);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 컬렉션 삭제후 컬렉션 태그가 삭제되었는지 확인
//        List<CollectionTag> retrievedTags = entityManager.createQuery("SELECT t FROM CollectionTag t", CollectionTag.class).getResultList();
//        assertThat(retrievedTags).isEmpty();
//    }
//
//    @Test
//    @Transactional
//    void testCascadeCollectionComment() {
//        Collection retrievedCollection = entityManager.find(Collection.class, collection.getId());
//
//        // 컬렉션 삭제
//        entityManager.remove(retrievedCollection);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 컬렉션 삭제후 컬렉션 댓글이 삭제되었는지 확인
//        List<CollectionComment> retrievedComments = entityManager.createQuery("SELECT c FROM CollectionComment c", CollectionComment.class).getResultList();
//        assertThat(retrievedComments).isEmpty();
//    }
//
//    @Test
//    @Transactional
//    void testCascadePin() {
//        Collection retrievedCollection = entityManager.find(Collection.class, collection.getId());
//
//        // 컬렉션 삭제
//        entityManager.remove(retrievedCollection);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 컬렉션 삭제후 핀들 삭제되었는지 확인
//        List<Pin> pinsAfterDelete = entityManager.createQuery("SELECT p FROM Pin p WHERE p.collection.id = :collectionId", Pin.class)
//                .setParameter("collectionId", collection.getId())
//                .getResultList();
//        assertThat(pinsAfterDelete).isEmpty();
//    }
//
//}