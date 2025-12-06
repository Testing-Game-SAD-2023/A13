package com.model.dto;

import java.time.Instant;

public class NotificationDTO {

    // Dati che servono per creare una Notification nel tuo servizio
    private Long userId;
    private String type;
    private String title;
    private String body;

    // Metadata base
    private Instant createdAt;

    // Coda su cui il servizio notifiche deve inviare la risposta
    private String replyTo;

    public NotificationDTO() {
        this.createdAt = Instant.now();
    }

    public NotificationDTO(Long userId,
                           String type,
                           String title,
                           String body,
                           String replyTo,
                           Instant createdAt) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.replyTo = replyTo;
        this.createdAt = (createdAt != null) ? createdAt : Instant.now();
    }

    // Getter e setter

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
