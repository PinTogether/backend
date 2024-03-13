package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Following;
import com.pintogether.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    int countByFollowerId(Long followerId);

    int countByFolloweeId(Long followeeId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Optional<Following> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

}
