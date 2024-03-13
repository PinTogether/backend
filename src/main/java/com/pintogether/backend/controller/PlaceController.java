package com.pintogether.backend.controller;

import com.pintogether.backend.entity.Place;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/{placeId}")
    public ApiResponse loadPlaceById(@PathVariable Long placeId) {
        return makeResponse(placeService.loadPlaceById(placeId));
    }

    public <T> ApiResponse<T> makeResponse(List<T> result) {
        return new ApiResponse<>(result);
    }

    public <T> ApiResponse<T> makeResponse(T result) {
        return makeResponse(Collections.singletonList(result));
    }

//    public ApiResponse makeResponse(int code, String message) {
//        return new ApiResponse(code, message);
//    }
}
