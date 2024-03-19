package com.pintogether.backend.service;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.CoordinateDTO;
import com.pintogether.backend.dto.PlaceResponseDTO;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.entity.Star;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import com.pintogether.backend.util.CoordinateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class SqlPlaceSearchImpl implements SearchService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private StarRepository starRepository;

    public List<PlaceResponseDTO> searchPlace(@ThisMember Long memberId, String query, int page, int size) {
//        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);
        List<PlaceResponseDTO> dtoList = foundPlace.stream()
                .map(place -> {
                    CoordinateDTO dto = CoordinateConverter.convert(place.getAddress().getLongitude(), place.getAddress().getLatitude());
                    boolean starred;
                    Optional<Star> optionalStar = starRepository.findByPlaceIdAndMemberId(place.getId(), memberId);
                    starred = optionalStar.isPresent();
                    return PlaceResponseDTO.builder()
                            .id(place.getId())
                            .name(place.getName())
                            .roadNameAddress(place.getAddress().getRoadNameAddress())
                            .pinCnt(pinRepository.countByPlaceId(place.getId()))
                            .category(place.getCategory())
                            .starred(starred)
                            .latitude(dto.getLatitude())
                            .longitude(dto.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        if (dtoList.isEmpty()) {
            return new ArrayList<PlaceResponseDTO>();
        }
        return dtoList;
    }

}
