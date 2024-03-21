package com.pintogether.backend.controller;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/place")
    public ApiResponse searchPlace(
            @ThisMember Member member,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        if (query.isEmpty()) {
            throw new CustomException(StatusCode.BAD_REQUEST, "검색어를 입력해주세요.");
        }
        if (query.length() > 20) {
            throw new CustomException(StatusCode.BAD_REQUEST, "20자 이내로 검색해주세요.");
        }

        return new ApiResponse(searchService.searchPlace(member, query, page, size));
    }

}
