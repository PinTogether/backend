package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.ShowPinResponseDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.service.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ApiResponse loadPlaceById(@ThisMember Member member, @PathVariable Long placeId) {
        logger.info("[GET] /places/{placeId}");
        Place place = placeService.getPlace(placeId);
        int pinCnt = placeService.getPlacePinCnt(placeId);
        boolean starred = placeService.getStarred(member, placeId);
        return ApiResponse.makeResponse(place.toShowPlaceReponseDto(starred, pinCnt));
    }

    @GetMapping("/{placeId}/pins")
    public ApiResponse loadPinByPlaceId(@ThisMember Member member,
                                        @PathVariable("placeId") Long placeId,
                                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "0") int size) {
        List<Pin> pinList = placeService.getPinsPageListByPlaceId(placeId, page, size);
        List<ShowPinResponseDTO> showPinResponseDTOList = new ArrayList<>();
        for (Pin x : pinList) {
            showPinResponseDTOList.add(x.toShowPinResponseDTO());
        };
        return ApiResponse.makeResponse(showPinResponseDTOList);
    }

}
