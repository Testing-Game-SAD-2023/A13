package com.a13.notification.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

@Schema(description = "Data Transfer Object per la creazione di una notifica")
public class NotificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(
            description = "Identificativo univoco dell'utente destinatario",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long userId;

    @Schema(
            description = "Categoria della notifica",
            example = "ACHIEVEMENT_UNLOCKED"
    )
    private String type;

    @Schema(
            description = "Titolo breve della notifica",
            example = "Benvenuto!"
    )
    private String title;

    @Schema(
            description = "Contenuto testuale completo della notifica",
            example = "Grazie per esserti registrato alla nostra piattaforma."
    )
    private String body;

    @Schema(
            description = "Nome della coda RabbitMQ su cui inviare la risposta (opzionale)",
            example = "reply.queue.service.t4"
    )
    private String replyTo;

    public NotificationDTO() {
    }

    public NotificationDTO(Long userId, String type, String title, String body, String replyTo) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.replyTo = replyTo;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }

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
