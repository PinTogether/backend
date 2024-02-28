package com.pintogether.backend.repository;

import com.pintogether.backend.entity.BaseUpdatedAtEntity;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.entity.components.Address;
import com.pintogether.backend.entity.enums.PlaceSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @AfterEach
    public void cleanUp() {
        placeRepository.deleteAll();
    }


    @Test
    @DisplayName(" place 저장 테스트 ")
    void savePlace() throws Exception {

        //given
        Address address = Address.builder()
                .roadNameAddress("경기도 어쩌구")
                .numberAddress("경기도 저쩌구")
                .latitude(37.10214)
                .longitude(127.31342)
                .build();
        Place place = Place.builder()
                .address(address)
                .placeSource(PlaceSource.LOCAL_DATA)
                .placeSourceId("adfdafwe11234")
                .name("짜장면집")
                .category("중국식")
                .build();

        //when
        placeRepository.save(place);
        Place foundPlace = placeRepository.findById(1L).orElseThrow(
                ()-> new Exception("저장 실패")
        );

        //then
        Assertions.assertSame(place, foundPlace);


    }

}