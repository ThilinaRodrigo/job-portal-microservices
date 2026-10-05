package com.jobportal.notification_service.service;

import com.jobportal.notification_service.entity.Notification;

import java.util.List;

public interface INotificationService {

    Notification saveNotification(Notification notification);

    List<Notification> getNotificationsForUser(Long recipientId, String email);

    Notification markAsRead(Long notificationId);

    void markAllAsRead(Long recipientId, String email);

    long getUnreadCount(Long recipientId, String email);
}
