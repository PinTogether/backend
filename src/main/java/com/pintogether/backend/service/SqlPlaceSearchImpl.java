package com.pintogether.backend.service;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.dto.CoordinateDTO;
import com.pintogether.backend.dto.ShowCollectionResponseDTO;
import com.pintogether.backend.dto.ShowPlaceResponseDTO;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.entity.CollectionTag;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import com.pintogether.backend.util.CoordinateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    public List<ShowPlaceResponseDTO> searchPlaces(@ThisMember Member member, String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Place> foundPlace = placeRepository.findByQuery(pageable, query);
        List<ShowPlaceResponseDTO> dtoList = foundPlace.stream()
                .map(place -> {
                    CoordinateDTO dto = CoordinateConverter.convert(place.getAddress().getLongitude(), place.getAddress().getLatitude());
                    boolean starred;
                    return ShowPlaceResponseDTO.builder()
                            .id(place.getId())
                            .name(place.getName())
                            .roadNameAddress(place.getAddress().getRoadNameAddress())
                            .pinCnt(pinRepository.countByPlaceId(place.getId()))
                            .category(place.getCategory())
                            .starred(member != null && starRepository.findByPlaceIdAndMemberId(place.getId(), member.getId()).isPresent())
                            .latitude(dto.getLatitude())
                            .longitude(dto.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        if (dtoList.isEmpty()) {
            return new ArrayList<ShowPlaceResponseDTO>();
        }
        return dtoList;
    }

    public List<ShowCollectionResponseDTO> searchCollections(@ThisMember Member member, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Collection> foundCollections = collectionRepository.findByTitleLike(pageable,"%" + query + "%");

        return foundCollections.stream()
                .map(c -> ShowCollectionResponseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .writerId(c.getMember().getId())
                        .details(c.getDetails())
                        .writer(c.getMember().getNickname())
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

}
