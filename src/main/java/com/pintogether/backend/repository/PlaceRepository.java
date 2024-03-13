package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select x from Place x where x.name like %:query%")
    Page<Place> findByQuery(Pageable pageable, String query);

    Optional<Place> findOneById(Long placeId);

}
