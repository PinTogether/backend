package com.pintogether.backend.controller;

import com.pintogether.backend.auth.OAuth2LoginSuccessHandler;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.CreateComplaintRequestDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ComplaintController {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintController.class);
    private final ComplaintService complaintService;
    private final MemberService memberService;
    private final CollectionService collectionService;
    private final PinService pinService;
    private final CollectionCommentService collectionCommentService;

    @PostMapping
    public ApiResponse createComplaint(@ThisMember Member member, @RequestBody @Valid CreateComplaintRequestDTO dto, HttpServletResponse response) {
        logger.info("[POST /reports]");
        Member targetMember;
        switch (dto.getPlatformType()) {
            case COLLECTION -> targetMember = collectionService.getCollection(dto.getTargetId()).getMember();
            case PIN -> targetMember = pinService.getPin(dto.getTargetId()).getCollection().getMember();
            case COLLECTION_COMMENT -> targetMember = collectionCommentService.getCollectionComment(dto.getTargetId()).getMember();
            default -> throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 요청입니다.");
        }
        complaintService.createComplaint(member, targetMember, dto);
        return ApiResponse.makeResponse(StatusCode.CREATED.getCode(), StatusCode.CREATED.getMessage(), response);
    }

    @GetMapping
    public ApiResponse getComplaints(@ThisMember Member member,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        logger.info("[GET reports]");
        return ApiResponse.makeResponse(complaintService.getComplaintList(member, page, size));


    }
}
