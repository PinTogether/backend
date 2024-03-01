package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.entity.components.Address;
import com.pintogether.backend.entity.enums.PlaceSource;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

class PinRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                        .nickname("닉네임이다")
                        .registrationId("1341241251")
                        .registrationSource(RegistrationSource.KAKAO)
                        .roleType(RoleType.ROLE_MEMBER)
                        .build();
        memberRepository.save(member);

        Collection collection = Collection.builder()
                        .details("이것은 맛집에 관한 콜렉션")
                        .title("이것은 콜렉션의 제목")
                        .member(member)
                        .thumbnail("thumbnailurl")
                        .build();
        collectionRepository.save(collection);

        Address address = Address.builder()
                .roadNameAddress("경기도 어쩌구")
                .numberAddress("경기도 저쩌구")
                .latitude(37.10214)
                .longitude(127.31342)
                .build();
        Place place = Place.builder()
                .address(address)
                .placeSource(PlaceSource.LOCAL_DATA)
                .placeSourceId("adfdafwe--11234")
                .name("짜장면집")
                .category("중국식")
                .build();
        placeRepository.save(place);

    }

    @AfterEach
    public void cleanUp() {
        placeRepository.deleteAll();
        pinRepository.deleteAll();
        collectionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("Pin 저장 후 Pin 삭제")
    public void savePin() {

        //given
        Pin pin = Pin.builder()
                .collection(collectionRepository.findAll().get(0))
                .place(placeRepository.findAll().get(0))
                .review("핀에 대한 한줄평")
                .build();

        //when
        pinRepository.save(pin);

        //then
        Assertions.assertSame(pinRepository.findAll().get(0), pin);

        pinRepository.findAll().get(0).deletePlace();
        placeRepository.deleteAll();

        assert(pinRepository.findAll().get(0).getPlace() == null);

    }


}