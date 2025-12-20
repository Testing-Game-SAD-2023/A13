package com.a13.notification.client.dto;

import java.io.Serializable;

public class NotificationDTO implements Serializable {

    private static final long serialVersionUID = 1L; // Buona pratica per Serializable

    private Long userId;
    private String type;
    private String title;
    private String body;
    private String replyTo;

    public NotificationDTO() {
    }

    public NotificationDTO(Long userId,
                           String type,
                           String title,
                           String body,
                           String replyTo) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.replyTo = replyTo;
    }

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

    public String getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    // toString per loggare facilmente cosa stai inviando
    @Override
    public String toString() {
        return "NotificationDTO{" +
                "userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", replyTo='" + replyTo + '\'' +
                '}';
    }
}
