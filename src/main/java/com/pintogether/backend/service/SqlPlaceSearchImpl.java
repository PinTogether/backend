package com.pintogether.backend.service;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.*;
import com.pintogether.backend.entity.*;
import com.pintogether.backend.entity.enums.SearchType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlPlaceSearchImpl implements SearchService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private StarRepository starRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private InterestingCollectionService interestingCollectionService;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private PlaceService placeService;

    @Transactional
    public List<ShowPlaceResponseDTO> searchPlaces(Member member, String query, int page, int size) {
        if (member != null) {
            this.saveHistory(member, query, SearchType.PLACE);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);
        List<ShowPlaceResponseDTO> dtoList = foundPlace.stream()
                .map(p -> p.toShowPlaceReponseDto(placeService.getStarred(member, p.getId()), placeService.getPlacePinCnt(p.getId()))
                ).toList();
        if (dtoList.isEmpty()) {
            return new ArrayList<>();
        }
        return dtoList;
    }

    public List<ShowCollectionResponseDTO> searchCollections(@ThisMember Member member, String query, int page, int size) {
        if (member != null) {
            this.saveHistory(member, query, SearchType.COLLECTION);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Collection> foundCollections = collectionRepository.findCollectionsByTitleContainingOrCollectionTagsTagContainingOrderByIdDesc(pageable, query, query);
        return foundCollections.stream()
                .map(c -> ShowCollectionResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .details(c.getDetails())
                        .writerMembername(c.getMember().getMembername())
                        .thumbnail(c.getThumbnail())
                        .likeCnt(collectionService.getLikeCnt(c.getId()))
                        .pinCnt(collectionService.getPinCnt(c.getId()))
                        .scrapCnt(collectionService.getScrappedCnt(c.getId()))
                        .isScrapped(member != null && interestingCollectionService.isScrappedByMember(member.getId(), c.getId()))
                        .isLiked(member != null && interestingCollectionService.isLikedByMember(member.getId(), c.getId()))
                        .tags(c.getCollectionTags().stream()
                                .map(CollectionTag::getTag)
                                .collect(Collectors.toList()))
                        .build())
                .toList();
    }

    public List<ShowPinResponseDTO> searchPins(@ThisMember Member member, String query, int page, int size) {
        if (member != null) {
            this.saveHistory(member, query, SearchType.PIN);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Pin> foundPins = pinRepository.findPinsByReviewContainingOrPinTagsTagContainingOrderByIdDesc(pageable, query, query);
        return foundPins.stream()
                .map(Pin::toShowPinResponseDTO)
                .toList();
    }

    public Page<SearchHistory> getSearchHistory(@ThisMember Member member, SearchType searchType) {
        Pageable pageable = PageRequest.of(0, 20);
        if (searchType.equals(SearchType.TOTAL)) {
            return searchHistoryRepository.findByMemberIdOrderByIdDesc(pageable, member.getId());
        }
        return searchHistoryRepository.findByMemberIdAndSearchTypeOrderByIdDesc(pageable, member.getId(), searchType);
    }

    @Transactional
    public void saveHistory(Member member, String query, SearchType searchType) {
        Page<SearchHistory> histories = this.getSearchHistory(member, SearchType.TOTAL);
        for (SearchHistory x : histories) {
            if (x.getQuery().equals(query)) {
                return;
            }
        }
        searchHistoryRepository.save(SearchHistory.builder()
                .searchType(searchType)
                .query(query)
                .member(member).build());
    }

    @Transactional
    public void deleteSearchHistory(Member member, Long id) {
        SearchHistory history = searchHistoryRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "해당 검색 기록을 찾을 수 없습니다.")
        );
        if (history.getMember() == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "탈퇴한 회원의 기록입니다.");
        }
        if (history.getMember().getId().equals(member.getId())) {
            searchHistoryRepository.delete(history);
        } else {
            throw new CustomException(StatusCode.FORBIDDEN, "기록을 삭제할 권한이 없습니다.");
        }
    }

}
