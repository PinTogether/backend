package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByRegistrationId(String registrationId);

    Optional<Member> findOneById(Long id);

    @Query(value = "SELECT m.* FROM member m INNER JOIN following f ON m.id = f.follower_id WHERE f.followee_id = ?1", nativeQuery = true)
    List<Member> findAllFollowers(Long memberId);

    @Query(value = "SELECT m.* FROM member m INNER JOIN following f ON m.id = f.followee_id WHERE f.follower_id = ?1", nativeQuery = true)
    List<Member> findAllFollowings(Long memberId);

    boolean existsOneByMembername(String membername);
}
