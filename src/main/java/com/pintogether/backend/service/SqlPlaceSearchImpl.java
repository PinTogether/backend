package com.pintogether.backend.service;

import com.pintogether.backend.dto.SearchPlaceResponseDto;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SqlPlaceSearchImpl implements SearchService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<SearchPlaceResponseDto> searchPlace(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);
        List<SearchPlaceResponseDto> dtoList = new ArrayList<>();

        for (Place x : foundPlace) {
            dtoList.add(SearchPlaceResponseDto.builder()
                    .id(x.getId())
                    .name(x.getName())
                    .roadNameAddress(x.getAddress().getRoadNameAddress())
                    .pinCnt(0)
                    .latitude(x.getAddress().getLatitude())
                    .longitude(x.getAddress().getLongitude())
                    .build());
        }
        if (dtoList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return dtoList;
    }

}
