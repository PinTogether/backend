package com.pintogether.backend.service;

import com.pintogether.backend.dto.PlaceResponseDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.entity.Star;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PinRepository pinRepository;
    private final StarRepository starRepository;

    public PlaceResponseDTO loadPlaceById(Member member, Long placeId) {
        Place place = placeRepository.findOneById(placeId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage()));
        return place.toPlaceReponseDto(member != null && starRepository.findByPlaceIdAndMemberId(place.getId(), member.getId()).isPresent(),
                                            pinRepository.countByPlaceId(placeId));
    }

}
