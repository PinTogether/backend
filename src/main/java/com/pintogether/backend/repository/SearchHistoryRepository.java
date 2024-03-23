package com.pintogether.backend.repository;

import com.pintogether.backend.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Page<SearchHistory> findByMemberIdOrderByIdDesc(Pageable pageable, Long memberId);

}
