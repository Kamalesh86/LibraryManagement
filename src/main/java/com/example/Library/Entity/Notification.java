package com.example.Library.Entity; // Changed package name

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "member_id", nullable = false)
    private String memberId; // Changed to Long to match Member entity's ID type

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "date_sent", nullable = false)
    private LocalDateTime dateSent;

    @Column(name = "type", length = 50) // e.g., "OVERDUE", "FINE", "DUE_DATE_REMINDER", "GENERAL"
    private String type;

    @Column(name = "related_entity_id") // e.g., bookId for overdue, fineId for fine, transactionId for reminder
    private Long relatedEntityId; // Changed to Long

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    // Constructors
    public Notification() {
        this.dateSent = LocalDateTime.now();
        this.isRead = false; // Default to unread
    }

    public Notification(String memberId, String message, String type, Long relatedEntityId) {
        this.memberId = memberId;
        this.message = message;
        this.dateSent = LocalDateTime.now();
        this.type = type;
        this.relatedEntityId = relatedEntityId;
        this.isRead = false; // Default to unread
    }

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", memberId=" + memberId +
                ", message='" + message + '\'' +
                ", dateSent=" + dateSent +
                ", type='" + type + '\'' +
                ", relatedEntityId=" + relatedEntityId +
                ", isRead=" + isRead +
                '}';
    }
}
