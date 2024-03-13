package com.pintogether.backend.controller;

import com.pintogether.backend.dto.SearchPlaceResponseDTO;
import com.pintogether.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/place")
    public List<SearchPlaceResponseDTO> searchPlace(
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        System.out.println("SearchPlaceController");
        return searchService.searchPlace(query, page, size);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("조건에 맞는 결과를 찾을 수 없습니다.");
    }
}
