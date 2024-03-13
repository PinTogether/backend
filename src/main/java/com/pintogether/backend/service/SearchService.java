package com.pintogether.backend.service;

import com.pintogether.backend.dto.PlaceResponseDTO;

import java.util.List;

public interface SearchService {

    List<PlaceResponseDTO> searchPlace(String query, int page, int size);

}
