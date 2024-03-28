package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.PinImage;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.DomainType;
import com.pintogether.backend.service.PinService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/pins")
@RestController
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @PostMapping
    public ApiResponse createPin(@ThisMember Member member,
                                 @RequestBody @Valid CreatePinRequestDTO createPinRequestDTO,
                                 HttpServletResponse response) {
        Long id = pinService.createPin(member, createPinRequestDTO);
        return ApiResponse.makeResponse(CreatePinResponseDTO.builder().id(id).build(), StatusCode.CREATED, response);
    }

    @PostMapping("/selected-places")
    public ApiResponse createSelectedPins(@ThisMember Member member,
                                          @RequestBody @Valid CreatePinSelectedPlacesRequestDTO dto,
                                          HttpServletResponse response) {
        List<Long> ids = pinService.createSelectedPlaces(member, dto);
        List<CreatePinResponseDTO> responseDTO = new ArrayList<>();
        for (Long id : ids) {
            responseDTO.add(CreatePinResponseDTO.builder().id(id).build());
        }
        return ApiResponse.makeResponse(responseDTO, StatusCode.CREATED, response);
    }

    @PostMapping("/selected-collections")
    public ApiResponse createPinsSelectedPlaces(@ThisMember Member member,
                                                @RequestBody @Valid CreatePinsSelectedCollectionsRequestDTO dto,
                                                HttpServletResponse response) {
        List<Long> ids = pinService.createSelectedCollections(member, dto);
        List<CreatePinResponseDTO> responseDTO = new ArrayList<>();
        for (Long id : ids) {
            responseDTO.add(CreatePinResponseDTO.builder().id(id).build());
        }
        return ApiResponse.makeResponse(responseDTO, StatusCode.CREATED, response);

    }

    @PutMapping("/{id}")
    public ApiResponse updatePin(@ThisMember Member member,
                                 @PathVariable("id") Long id,
                                 @RequestBody @Valid UpdatePinReqeustDTO updatePinReqeustDTO,
                                 HttpServletResponse response) {
        pinService.updatePin(member, id, updatePinReqeustDTO);

        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);

    }

    @GetMapping("/{id}/images")
    public ApiResponse getPinImages(@PathVariable("id") Long id) {
        List<PinImage> images = pinService.getPin(id).getPinImages();
        return ApiResponse.makeResponse(ShowPinImagesResponseDTO.builder()
                .pinImages(images)
                .build());
    }

    @DeleteMapping("/{id}")
    public ApiResponse deletePin(@ThisMember Member member,
                                 @PathVariable("id") Long id,
                                 HttpServletResponse response) {
        pinService.deletePin(member, id);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @DeleteMapping("/selected")
    public ApiResponse deleteSelectedPins(@ThisMember Member member,
                                          @RequestBody DeleteSelectedPinsRequestDTO dto,
                                          HttpServletResponse response) {
        pinService.deleteSelectedPins(member, dto);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }


    @PostMapping("/{id}/images/presigned-url")
    public ApiResponse getPresignedUrlForPinImage(@ThisMember Member member,
                                                  @PathVariable("id") Long id,
                                                  @RequestBody @Valid S3PinImageRequestDTO dtos) {
        return ApiResponse.makeResponse(pinService.getPresignedUrlForPinImage(member.getId(), dtos, DomainType.Pin.REVIEW_IMAGE.getName(), id));
    }

}