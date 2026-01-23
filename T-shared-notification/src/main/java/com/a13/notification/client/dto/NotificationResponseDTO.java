package com.a13.notification.client.dto;

import java.io.Serializable;

public class NotificationResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ID della notifica creata nel DB
    private Long notificationId;

    // Esito dell'operazione (es. "OK", "ERROR")
    private String status;

    // Messaggio opzionale (es. motivo errore)
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

    @Override
    public String toString() {
        return "NotificationResponseDTO{" +
                "notificationId=" + notificationId +
                ", status='" + status + '\'' +
                '}';
    }
}
