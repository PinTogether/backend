package com.pintogether.backend.service;

import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PinRepository pinRepository;
    private final StarRepository starRepository;

    public Place getPlace(Long placeId) {
        return placeRepository.findOneById(placeId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage()));
    }

    public List<Pin> getPinsPageListByPlaceId(Long placeId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Pin> pinPage = pinRepository.findByPlaceId(placeId, pageable);
        return pinPage.get().toList();
    }

    public boolean getStarred(Member member, Long placeId) {
        return member != null && starRepository.findByPlaceIdAndMemberId(placeId, member.getId()).isPresent();
    }

    public Long getPlacePinCnt(Long placeId) {
        return pinRepository.countByPlaceId(placeId);
    }

}
