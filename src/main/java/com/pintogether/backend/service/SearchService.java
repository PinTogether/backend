package com.pintogether.backend.service;

import com.pintogether.backend.dto.ShowCollectionResponseDTO;
import com.pintogether.backend.dto.ShowPinResponseDTO;
import com.pintogether.backend.dto.ShowPlaceResponseDTO;
import com.pintogether.backend.dto.ShowSearchHistoryResponseDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.SearchType;

import java.util.List;

public interface SearchService {

    List<ShowPlaceResponseDTO> searchPlaces(Member member, String query, int page, int size);

    List<ShowCollectionResponseDTO> searchCollections(Member member, String query, int page, int size);

    List<ShowPinResponseDTO> searchPins(Member member, String query, int page, int size);

    List<ShowSearchHistoryResponseDTO> getSearchHistory(Member member, SearchType searchType);

}
