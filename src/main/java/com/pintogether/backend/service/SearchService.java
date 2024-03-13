package com.pintogether.backend.service;

import com.pintogether.backend.dto.SearchPlaceResponseDTO;

import java.util.List;

public interface SearchService {

    List<SearchPlaceResponseDTO> searchPlace(String query, int page, int size);

}
