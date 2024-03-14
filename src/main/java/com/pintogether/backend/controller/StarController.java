package com.pintogether.backend.controller;

import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.StarService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/stars")
@RestController
@RequiredArgsConstructor
public class StarController {

    private final StarService starService;

    @GetMapping
    public ApiResponse loadAllPlace() {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return new ApiResponse(starService.loadStars(memberId));
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
