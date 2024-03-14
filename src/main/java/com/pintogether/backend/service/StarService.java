package com.pintogether.backend.service;

import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.Place;
import com.pintogether.backend.entity.Star;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.MemberRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Transactional
@Service
public class StarService {

    private final StarRepository starRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public void createStar(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage())
        );
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage())
        );
        starRepository.findByPlaceIdAndMemberId(placeId, memberId).ifPresentOrElse(
                star -> {
                    throw new CustomException(StatusCode.BAD_REQUEST, "이미 찜한 장소입니다.");
                    }, () -> starRepository.save(
                            Star.builder()
                                    .member(member)
                                    .place(place)
                                    .build()
                )
        );

    }

    public List<Place> loadStars(Long memberId) {
        List<Star> stars = starRepository.findAllByMemberId(memberId);
        List<Place> places = new ArrayList<>();
        for (Star star : stars) {
            placeRepository.findById(star.getPlace().getId()).ifPresentOrElse(
                    places::add,
                    () -> {
                        throw new CustomException(StatusCode.BAD_REQUEST, "찜한 장소를 불러오는 중 에러가 발생했습니다.");
                    }
            );
        }
        return places;
    }

    public void deleteStar(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage())
        );
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage())
        );
        starRepository.findByPlaceIdAndMemberId(placeId, memberId).ifPresentOrElse(
                starRepository::delete
                , () -> {
                    throw new CustomException(StatusCode.BAD_REQUEST, "이미 찜 취소한 장소입니다.");
                }
        );
    }


    }
