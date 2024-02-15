package com.pintogether.backend.repository;

import com.pintogether.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByRegistrationId(String registrationId);

}
