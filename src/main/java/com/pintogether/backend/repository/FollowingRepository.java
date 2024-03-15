package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Following;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    int countByFollowerId(Long followerId);

    int countByFolloweeId(Long followeeId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Optional<Following> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

}
