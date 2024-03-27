package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.ShowPlaceResponseDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.PinService;
import com.pintogether.backend.service.PlaceService;
import com.pintogether.backend.service.StarService;
import com.pintogether.backend.util.CoordinateConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stars")
@RestController
@RequiredArgsConstructor
public class StarController {

    private final StarService starService;
    private final PlaceService placeService;

    @GetMapping
    public ApiResponse loadAllPlace(@ThisMember Member member) {
        List<Place> places = starService.loadStars(member.getId());
        return new ApiResponse(places.stream().map(
                place -> place.toShowPlaceReponseDto(
                        placeService.getStarred(member, place.getId()), placeService.getPlacePinCnt(place.getId())))
                .toList()
        );
    }

    @PostMapping("/{placeId}")
    public ApiResponse createStar(@PathVariable Long placeId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        starService.createStar(memberId, placeId);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @DeleteMapping("/{placeId}")
    public ApiResponse deleteStar(@PathVariable Long placeId, HttpServletResponse response) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        starService.deleteStar(memberId, placeId);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

}
