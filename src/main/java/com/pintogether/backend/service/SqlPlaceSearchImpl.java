package com.pintogether.backend.service;

import com.pintogether.backend.dto.CoordinateDTO;
import com.pintogether.backend.dto.PlaceResponseDTO;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.util.CoordinateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SqlPlaceSearchImpl implements SearchService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PinRepository pinRepository;

    public List<PlaceResponseDTO> searchPlace(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);

        List<PlaceResponseDTO> dtoList = foundPlace.stream()
                .map(place -> {
                    CoordinateDTO dto = CoordinateConverter.convert(place.getAddress().getLongitude(), place.getAddress().getLatitude());
                    return PlaceResponseDTO.builder()
                            .id(place.getId())
                            .name(place.getName())
                            .roadNameAddress(place.getAddress().getRoadNameAddress())
                            .pinCnt(pinRepository.countByPlaceId(place.getId()))
                            .category(place.getCategory())
                            .starred(false)
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
