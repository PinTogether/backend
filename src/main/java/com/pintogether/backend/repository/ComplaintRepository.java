package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Page<Complaint> findByReporterId(Pageable pageable, Long reporterId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Complaint c SET c.reporter = NULL WHERE c.reporter.id = :id")
    void updateReporterToNull(@Param("id") Long reporterId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Complaint c SET c.targetMember = NULL WHERE c.targetMember.id = :id")
    void updateTargetMemberToNull(@Param("id") Long targetMemberId);
}
