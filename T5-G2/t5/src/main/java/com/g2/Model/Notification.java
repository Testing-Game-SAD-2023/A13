package com.g2.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("titolo")
    private String titolo;
    @JsonProperty("message")
    private String message;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("isRead")
    private Boolean isRead;

    public Notification(Long id, String titolo, String message, LocalDateTime timestamp, Boolean isRead) {
        this.id = id;
        this.titolo = titolo;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return titolo;
    }
    public void setTitle(String titolo) {
        this.titolo = titolo;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public Boolean getIsRead() {
        return isRead;
    }
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

}
