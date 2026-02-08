package com.g2.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
    private String senderEmail;
    private String receiverEmail;

    public Message() {
    }

    public Message(Long id, Long senderId, Long receiverId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
