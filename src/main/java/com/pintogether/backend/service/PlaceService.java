package com.pintogether.backend.service;

import com.pintogether.backend.entity.Place;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public Place loadPlaceById(Long placeId) {
        return placeRepository.findOneById(placeId).orElseThrow(
                ()-> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage())
        );
    }
}
