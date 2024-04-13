package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = "SELECT p.*, COUNT(pin.id) as pinCount FROM place p LEFT JOIN pin pin ON p.id = pin.place_id " +
            "WHERE p.name LIKE %:query% GROUP BY p.id ORDER BY pinCount DESC", nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM place p LEFT JOIN pin pin ON p.id = pin.place_id " +
            "WHERE p.name LIKE %:query% GROUP BY p.id")
    Page<Place> findByQuery(Pageable pageable, String query);

    @Query(value = "SELECT p.*, COUNT(pin.id) as pinCount FROM place p LEFT JOIN pin pin ON p.id = pin.place_id " +
            "WHERE p.name LIKE %:query% " +
            "AND p.latitude > :edgeLlatitude AND p.latitude < :edgeRlatitude " +
            "AND p.longitude > :edgeLlongitude AND p.longitude < :edgeRlongitude " +
            "GROUP BY p.id ORDER BY pinCount DESC", nativeQuery = true,
            countQuery = "SELECT COUNT(p.*) FROM place p LEFT JOIN pin pin ON p.id = pin.place_id " +
                    "WHERE p.name LIKE %:query% " +
                    "AND p.latitude > :edgeLlatitude AND p.latitude < :edgeRlatitude " +
                    "AND p.longitude > :edgeLlongitude AND p.longitude < :edgeRlongitude " +
                    "GROUP BY p.id")
    Page<Place> findByQueryFilter(Pageable pageable, String query, double edgeLlatitude, double edgeLlongitude, double edgeRlatitude, double edgeRlongitude);

    Optional<Place> findOneById(Long placeId);

}
