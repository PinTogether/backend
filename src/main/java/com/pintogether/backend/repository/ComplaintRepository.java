package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Page<Complaint> findByReporterId(Pageable pageable, Long reporterId);

}
