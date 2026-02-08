package com.example.db_setup.model.dto;

public class NewMessageDTO {

    private Long senderId;
    private Long receiverId;
    private String content;
    private String senderEmail;

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }


    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String email) { this.senderEmail = senderEmail; }
}
