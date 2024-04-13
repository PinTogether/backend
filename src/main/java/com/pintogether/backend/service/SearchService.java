package com.pintogether.backend.service;

import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.SearchHistory;
import com.pintogether.backend.entity.enums.SearchType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

    List<ShowPlaceResponseDTO> searchPlaces(Member member, String query, int page, int size, String filter);

    List<ShowCollectionResponseDTO> searchCollections(Member member, String query, int page, int size);

    List<ShowPinResponseDTO> searchPins(Member member, String query, int page, int size, String filter);

    Page<SearchHistory> getSearchHistory(Member member, SearchType searchType);

    void deleteSearchHistory(Member member, Long id);

    List<ShowSearchMemberResponseDTO> searchMembers(Member member, String query, int page, int size);

}
