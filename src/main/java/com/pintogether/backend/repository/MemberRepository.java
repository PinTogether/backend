package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Optional<Member> findOneByMembername(String membername);

    @Query("SELECT m FROM Member m WHERE m.name LIKE CONCAT('%',:query,'%') OR m.membername LIKE CONCAT('%',:query,'%')")
    Page<Member> findMembersByMembernameContainingOrNameContaining(Pageable pageable, @Param("query") String query);

}
