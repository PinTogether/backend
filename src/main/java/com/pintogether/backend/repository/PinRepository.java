package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {

    Long countByPlaceId(Long placeId);

    Page<Pin> findByPlaceId(Long placeId, Pageable pageable);

    Page<Pin> findPinsByReviewContainingOrPinTagsTagContainingOrderByIdDesc(Pageable pageable, String query1, String query2);

}
