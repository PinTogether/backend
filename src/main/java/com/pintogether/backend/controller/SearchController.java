package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.ShowSearchHistoryResponseDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.SearchType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

//    @Autowired
    private final SearchService searchService;

    @GetMapping("/places")
    public ApiResponse searchPlaces(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            @RequestParam(value = "filter", required = false) String filter,
            HttpServletRequest request
    ) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 인자입니다.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }

        return new ApiResponse(searchService.searchPlaces(member, query, page, size, filter));
    }

    @GetMapping("/collections")
    public ApiResponse searchCollections(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            HttpServletRequest request
    ) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 페이징 인자입니다.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }

        return new ApiResponse(searchService.searchCollections(member, query, page, size));
    }


    @GetMapping("/pins")
    public ApiResponse searchPins(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            @RequestParam(value = "filter", required = false) String filter,
            HttpServletRequest request
    ) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 페이징 인자입니다.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }
        return new ApiResponse(searchService.searchPins(member, query, page, size, filter));
    }

    @GetMapping("/history")
    public ApiResponse searchHistory(@ThisMember Member member,
                                     @RequestParam(value = "type", defaultValue = "TOTAL") SearchType searchType,
                                     HttpServletRequest request) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        return ApiResponse.makeResponse(searchService.getSearchHistory(member, searchType).stream()
                .map(h -> ShowSearchHistoryResponseDTO.builder()
                        .id(h.getId())
                        .query(h.getQuery())
                        .build())
                .toList());
    }

    @DeleteMapping("/history/{id}")
    public ApiResponse deleteHistory(@ThisMember Member member,
                                     @PathVariable("id") Long id,
                                     HttpServletRequest request) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        searchService.deleteSearchHistory(member, id);
        return ApiResponse.makeResponse(StatusCode.NO_CONTENT);
    }

    @GetMapping("/members")
    public ApiResponse searchMembers(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            HttpServletRequest request
    ) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 페이징 인자입니다.");
        }
        if (query.length() > 28) {
            throw new CustomException(StatusCode.BAD_REQUEST, "28자 이내로 검색해주세요.");
        }
        return new ApiResponse(searchService.searchMembers(member, query, page, size));
    }
}
