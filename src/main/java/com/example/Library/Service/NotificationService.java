package com.example.Library.Service;


import com.example.Library.Entity.Notification;
import com.example.Library.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<Notification> getNotificationsForMember(String memberId) {
        return notificationRepository.findByMemberIdOrderByDateSentDesc(memberId);
    }

    public void sendNotification(String memberId, String message, String type, Long relatedId) {
        Notification notification = new Notification(memberId, message, type, relatedId);
        notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }
}
