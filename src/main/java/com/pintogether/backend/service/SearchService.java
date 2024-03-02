package com.pintogether.backend.service;

import com.pintogether.backend.dto.SearchPlaceResponseDto;

import java.util.List;

public interface SearchService {

    List<SearchPlaceResponseDto> searchPlace(String query, int page, int size);

}
