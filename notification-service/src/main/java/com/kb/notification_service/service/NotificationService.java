package com.kb.notification_service.service;

import com.kb.notification_service.dto.NotificationRequest;
import com.kb.notification_service.dto.NotificationResponse;
import com.kb.notification_service.entity.NotificationStatus;
import com.kb.notification_service.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    
    NotificationResponse createNotification(NotificationRequest notificationRequest);
    
    NotificationResponse getNotificationById(Long id);
    
    List<NotificationResponse> getNotificationsByUserId(Long userId);
    
    List<NotificationResponse> getNotificationsByUserIdAndStatus(Long userId, NotificationStatus status);
    
    List<NotificationResponse> getNotificationsByStatus(NotificationStatus status);
    
    List<NotificationResponse> getNotificationsByType(NotificationType type);
    
    List<NotificationResponse> getNotificationsByEmail(String recipientEmail);
    
    List<NotificationResponse> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<NotificationResponse> getNotificationsByUserIdPaginated(Long userId, Pageable pageable);
    
    NotificationResponse updateNotificationStatus(Long id, NotificationStatus status);
    
    NotificationResponse markAsRead(Long id);
    
    NotificationResponse markAsSent(Long id);
    
    void deleteNotification(Long id);
    
    long getUnreadCountByUserId(Long userId);
    
    void sendNotification(NotificationRequest notificationRequest);
} 