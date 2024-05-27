package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findAllByMemberIdOrderByIdDesc(Long memberId);

    public List<Notification> findAllByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Notification n SET n.subject = NULL WHERE n.subject.id = :id")
    void updateSubjectToNull(@Param("id") Long subjectId);
}
