package com.jobportal.notification_service.service.impl;

import com.jobportal.notification_service.entity.Notification;
import com.jobportal.notification_service.repository.NotificationRepository;
import com.jobportal.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Notification saveNotification(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long recipientId, String email) {
        if (recipientId != null) {
            List<Notification> byId = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
            if (!byId.isEmpty()) {
                return byId;
            }
        }
        if (email != null && !email.trim().isEmpty()) {
            return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email.trim());
        }
        return List.of();
    }

    @Override
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long recipientId, String email) {
        List<Notification> notifications = getNotificationsForUser(recipientId, email);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public long getUnreadCount(Long recipientId, String email) {
        if (recipientId != null) {
            long count = notificationRepository.countByRecipientIdAndIsReadFalse(recipientId);
            if (count > 0) return count;
        }
        if (email != null && !email.trim().isEmpty()) {
            return notificationRepository.countByRecipientEmailAndIsReadFalse(email.trim());
        }
        return 0;
    }
}
