package com.pintogether.backend.service;

import com.pintogether.backend.dto.ShowPlaceResponseDTO;
import com.pintogether.backend.entity.Member;

import java.util.List;

public interface SearchService {

    List<ShowPlaceResponseDTO> searchPlace(Member member, String query, int page, int size);

}
