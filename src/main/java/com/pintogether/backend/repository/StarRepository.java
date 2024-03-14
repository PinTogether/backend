package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {

    Optional<Star> findByPlaceIdAndMemberId(Long placeId, Long memberId);

    List<Star> findAllByMemberId(Long memberId);

    Optional<Star> findByMemberId(Long MemberId);

}
