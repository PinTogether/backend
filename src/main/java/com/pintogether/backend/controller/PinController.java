package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.CurrentCollection;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Pin;
import com.pintogether.backend.entity.PinImage;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.DomainType;
import com.pintogether.backend.service.PinService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pins")
@RestController
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @PostMapping
    public ApiResponse createPin(@ThisMember Member member, @RequestBody CreatePinRequestDTO createPinRequestDTO, HttpServletResponse response) {
        pinService.createPin(member, createPinRequestDTO);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @PostMapping("/selected")
    public ApiResponse createSelectedPins(@ThisMember Member member, @RequestBody CreateSelectedPinsRequestDTO dto,
                                          HttpServletResponse response) {
        pinService.createSelectedPins(member, dto);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
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
    public ApiResponse deletePin(@ThisMember Member member, @PathVariable("id") Long id, HttpServletResponse response) {
        pinService.deletePin(member, id);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }

    @DeleteMapping
    public ApiResponse deleteSelectedPins(@ThisMember Member member,
                                          @RequestBody DeleteSelectedPinsRequestDTO dto,
                                          HttpServletResponse response) {
        pinService.deleteSelectedPins(member, dto);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT.getCode(), StatusCode.NO_CONTENT.getMessage(), response);
    }


    @GetMapping("/{id}/images/presigned-url")
    public ApiResponse getPresignedUrlForPinImage(@ThisMember Member member,
                                                  @PathVariable("id") Long id,
                                                  @RequestBody @Valid S3PinImageRequestDTO dto) {
        return ApiResponse.makeResponse(pinService.getPresignedUrlForPinImage(member.getId(), dto.getContentType(), DomainType.Pin.REVIEW_IMAGE.getName(), id));
    }

}