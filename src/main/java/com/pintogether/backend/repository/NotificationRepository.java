package com.pintogether.backend.repository;

import com.pintogether.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findAllByMemberIdOrderByIdDesc(Long memberId);
}
