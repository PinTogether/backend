package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreatePinRequestDTO;
import com.pintogether.backend.entity.*;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PinService {

    private final CollectionRepository collectionRepository;
    private final PinRepository pinRepository;
    private final PlaceRepository placeRepository;

    public void createPin(Long memberId, CreatePinRequestDTO createPinRequestDTO) {
        Place place = placeRepository.findById(createPinRequestDTO.getPlaceId()).orElseThrow(
                ()-> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage())
        );
        Collection collection = collectionRepository.findOneById(createPinRequestDTO.getCollectionId()).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage())
        );
        if (!memberId.equals(collection.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "해당 컬렉션에 핀을 생성할 권한이 없습니다.");
        }
        Pin pin = Pin.builder()
                .place(place)
                .collection(collection)
                .review(createPinRequestDTO.getReview())
                .build();
        for (String x : createPinRequestDTO.getTags()) {
            pin.updateTag(x);
        }
        for (String x : createPinRequestDTO.getImagePaths()) {
            pin.updateImage(x);
        }
        pinRepository.save(pin);
    }

}
