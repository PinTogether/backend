package com.pintogether.backend.service;

import com.pintogether.backend.dto.CoordinateDto;
import com.pintogether.backend.dto.SearchPlaceResponseDto;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.util.CoordinateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SqlPlaceSearchImpl implements SearchService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<SearchPlaceResponseDto> searchPlace(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);
//        List<SearchPlaceResponseDto> dtoList = new ArrayList<>();
//        for (Place x : foundPlace) {
//            CoordinateDto dto = CoordinateConverter.convert(x.getAddress().getLongitude(), x.getAddress().getLatitude());
//            dtoList.add(SearchPlaceResponseDto.builder()
//                    .id(x.getId())
//                    .name(x.getName())
//                    .roadNameAddress(x.getAddress().getRoadNameAddress())
//                    .pinCnt(0)
//                    .latitude(dto.getLatitude())
//                    .longitude(dto.getLongitude())
//                    .build());
//        }
        List<SearchPlaceResponseDto> dtoList = foundPlace.stream()
                .map(place -> {
                    CoordinateDto dto = CoordinateConverter.convert(place.getAddress().getLongitude(), place.getAddress().getLatitude());
                    return SearchPlaceResponseDto.builder()
                            .id(place.getId())
                            .name(place.getName())
                            .roadNameAddress(place.getAddress().getRoadNameAddress())
                            .pinCnt(0)
                            .latitude(dto.getLatitude())
                            .longitude(dto.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        if (dtoList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return dtoList;
    }

}
