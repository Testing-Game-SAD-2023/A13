package com.t10;

public class NotificationResponseDTO {

    private Long notificationId;
    private String status;
    private String message;

    public NotificationResponseDTO() {
    }

    public NotificationResponseDTO(Long notificationId, String status, String message) {
        this.notificationId = notificationId;
        this.status = status;
        this.message = message;
    }

    public Long getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
