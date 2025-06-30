package com.kb.notification_service.repository;

import com.kb.notification_service.entity.Notification;
import com.kb.notification_service.entity.NotificationStatus;
import com.kb.notification_service.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserId(Long userId);
    
    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
    
    List<Notification> findByStatus(NotificationStatus status);
    
    List<Notification> findByType(NotificationType type);
    
    List<Notification> findByRecipientEmail(String recipientEmail);
    
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    
    long countByUserIdAndStatus(Long userId, NotificationStatus status);
} 