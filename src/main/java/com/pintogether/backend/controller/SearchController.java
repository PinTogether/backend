package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.SearchType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/places")
    public ApiResponse searchPlaces(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 페이징 인자입니다.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }

        return new ApiResponse(searchService.searchPlaces(member, query, page, size));
    }

    @GetMapping("/collections")
    public ApiResponse searchCollections(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
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
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        if (!(0 <= page && page <= 10000) || !(0 <= size && size <= 10000)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 페이징 인자입니다.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }
        return new ApiResponse(searchService.searchPins(member, query, page, size));
    }

    @GetMapping("/history")
    public ApiResponse searchHistory(@ThisMember Member member,
                                     @RequestParam(value = "type", defaultValue = "TOTAL") SearchType searchType) {
        if (member != null) {
            return ApiResponse.makeResponse(searchService.getSearchHistory(member, searchType));
        }
        return ApiResponse.makeResponse(new ArrayList<>());
    }
}
