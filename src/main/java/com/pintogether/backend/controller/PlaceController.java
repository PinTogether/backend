package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.PinDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.service.PinService;
import com.pintogether.backend.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;


    @GetMapping("/{placeId}")
    public ApiResponse loadPlaceById(@ThisMember Member member, @PathVariable Long placeId) {
        Place place = placeService.getPlace(placeId);
        Long pinCnt = placeService.getPlacePinCnt(placeId);
        boolean starred = placeService.getStarred(member, placeId);
        return ApiResponse.makeResponse(place.toPlaceReponseDto(starred, pinCnt));
    }

    @GetMapping("/{placeId}/pins")
    public ApiResponse loadPinByPlaceId(@ThisMember Member member,
                                        @PathVariable("placeId") Long placeId,
                                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "0") int size) {
        List<Pin> pinList = placeService.getPinsPageListByPlaceId(placeId, page, size);
        List<PinDTO> pinDTOList = new ArrayList<>();
        for (Pin x : pinList) {
            pinDTOList.add(x.toPinDTO());
        };
        return ApiResponse.makeResponse(pinDTOList);
    }
}
