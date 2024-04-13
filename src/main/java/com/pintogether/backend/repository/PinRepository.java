package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    int countByPlaceId(Long placeId);

    Page<Pin> findByPlaceId(Long placeId, Pageable pageable);

    @Query(value = "SELECT p.* FROM pin p " +
            "LEFT JOIN pin_tag t " +
            "ON p.id = t.pin_id " +
            "LEFT JOIN place pl " +
            "ON p.place_id = pl.id " +
            "WHERE (p.review LIKE %:query1% OR t.tag LIKE %:query2%) " +
            "GROUP BY p.id"
            , nativeQuery = true,
            countQuery =
                        "SELECT COUNT(p.*) FROM pin p " +
                        "LEFT JOIN pin_tag t " +
                        "ON p.id = t.pin_id " +
                        "LEFT JOIN place pl " +
                        "ON p.place_id = pl.id " +
                        "WHERE (p.review LIKE %:query1% OR t.tag LIKE %:query2%) " +
                        "GROUP BY p.id")
    Page<Pin> findByQuery(Pageable pageable, String query1, String query2);

    @Query(value = "SELECT p.* FROM pin p " +
            "LEFT JOIN pin_tag t " +
            "ON p.id = t.pin_id " +
            "LEFT JOIN place pl " +
            "ON p.place_id = pl.id " +
            "WHERE (p.review LIKE %:query1% OR t.tag LIKE %:query2%) " +
            "AND pl.latitude > :edgeLlatitude AND pl.latitude < :edgeRlatitude " +
            "AND pl.longitude > :edgeLlongitude AND pl.longitude < :edgeRlongitude " +
            "GROUP BY p.id"
            , nativeQuery = true,
            countQuery = "SELECT COUNT(p.*) FROM pin p " +
                        "LEFT JOIN pin_tag t " +
                        "ON p.id = t.pin_id " +
                        "LEFT JOIN place pl " +
                        "ON p.place_id = pl.id " +
                        "WHERE (p.review LIKE %:query1% OR t.tag LIKE %:query2%) " +
                        "AND pl.latitude > :edgeLlatitude AND pl.latitude < :edgeRlatitude " +
                        "AND pl.longitude > :edgeLlongitude AND pl.longitude < :edgeRlongitude " +
                        "GROUP BY p.id")
    Page<Pin> findByQueryFilter(Pageable pageable, String query1, String query2,
            double edgeLlatitude, double edgeLlongitude, double edgeRlatitude, double edgeRlongitude);

    List<Pin> findByCollectionId(Long id);
}
