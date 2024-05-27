package com.pintogether.backend.repository;

import com.pintogether.backend.entity.SearchHistory;
import com.pintogether.backend.entity.enums.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Page<SearchHistory> findByMemberIdOrderByIdDesc(Pageable pageable, Long memberId);

    Page<SearchHistory> findByMemberIdAndSearchTypeOrderByIdDesc(Pageable pageable, Long memberId, SearchType searchType);

    List<SearchHistory> findAllByMemberId(Long memberId);

}
