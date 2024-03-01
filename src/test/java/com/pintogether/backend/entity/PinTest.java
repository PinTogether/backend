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
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class PinTest {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private Member member;
//    private Place place;
//    private Collection collection;
//    private Pin pin;
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
//        // 더미 Collection 생성
//        collection = Collection.builder()
//                .member(member)
//                .title("My favorite")
//                .thumbnail("thumbnailUrl")
//                .details("My favorite restaurant in Gangnam")
//                .build();
//        entityManager.persist(collection);
//
//        // 더미 Pin 생성
//        pin = Pin.builder()
//                .place(place)
//                .collection(collection)
//                .review("Great place!")
//                .build();
//
//        // 연관된 PinTag와 PinImage 추가
//        pinTag1 = PinTag.builder().pin(pin).tag("glory").build();
//        pinTag2 = PinTag.builder().pin(pin).tag("monster").build();
//        pinImage1 = PinImage.builder().pin(pin).imagePath("path1").build();
//        pinImage2 = PinImage.builder().pin(pin).imagePath("path2").build();
//
//        // pin 저장
//        entityManager.persist(pin);
//
//        entityManager.flush();
//    }
//
//    @AfterEach
//    void tearDown() {
//        entityManager.createNativeQuery("DELETE FROM pin_tag").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM pin_image").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM pin").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM place").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM collection").executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
//    }
//
//    @Test
//    @Transactional
//    void testFindPin() {
//        // 핀 조회
//        Pin retrievedPin = entityManager.find(Pin.class, pin.getId());
//        assertNotNull(retrievedPin);
//        assertThat(retrievedPin.getReview()).isEqualTo("Great place!");
//        assertThat(retrievedPin.getPinTags()).hasSize(2);
//        assertThat(retrievedPin.getPinImages()).hasSize(2);
//
//        // 핀 태그 및 핀 이미지 확인
//        assertThat(retrievedPin.getPinTags()).extracting("tag").containsExactlyInAnyOrder("glory", "monster");
//        assertThat(retrievedPin.getPinImages()).extracting("imagePath").containsExactlyInAnyOrder("path1", "path2");
//    }
//
//    @Test
//    @Transactional
//    void testCascadePinTag() {
//        Pin retrievedPin = entityManager.find(Pin.class, pin.getId());
//        // 핀 삭제
//        entityManager.remove(retrievedPin);
//        entityManager.flush();
//        entityManager.clear();
//
//
//        Pin deletedPin = entityManager.find(Pin.class, pin.getId());
//        assertNull(deletedPin);
//
//        // 핀 태그 삭제되었는지 확인
//        PinTag deletedPinTag1 = entityManager.find(PinTag.class, pinTag1.getId());
//        PinTag deletedPinTag2 = entityManager.find(PinTag.class, pinTag2.getId());
//        assertNull(deletedPinTag1);
//        assertNull(deletedPinTag2);
//
//    }
//
//    @Test
//    @Transactional
//    void testCascadePinImage() {
//        Pin retrievedPin = entityManager.find(Pin.class, pin.getId());
//        // 핀 삭제
//        entityManager.remove(retrievedPin);
//        entityManager.flush();
//        entityManager.clear();
//
//        Pin deletedPin = entityManager.find(Pin.class, pin.getId());
//        assertNull(deletedPin);
//
//        // 핀 이미지 삭제되었는지 확인
//        PinImage deletedPinImage1 = entityManager.find(PinImage.class, pinImage1.getId());
//        PinImage deletedPinImage2 = entityManager.find(PinImage.class, pinImage2.getId());
//        assertNull(deletedPinImage1);
//        assertNull(deletedPinImage2);
//
//    }
//
//    @Test
//    @Transactional
//    void testDeletePlaceCascadesToPin() {
//
//        Place foundPlace = entityManager.find(Place.class, place.getId());
//
//        // 장소 삭제
//        entityManager.remove(foundPlace);
//        entityManager.flush();
//        entityManager.clear();
//
//        // 장소를 참조하고 있던 pin이 삭제되었는지 확인
//        Pin deletedPin = entityManager.find(Pin.class, pin.getId());
//        assertNull(deletedPin);
//    }
//}