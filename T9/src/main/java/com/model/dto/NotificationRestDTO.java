package com.model.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "NotificationRestDTO",
        description = "DTO che rappresenta una notifica restituita dalle API REST"
)
public class NotificationRestDTO {

    @Schema(
            description = "Identificativo univoco della notifica",
            example = "1001",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Identificativo dell'utente destinatario della notifica",
            example = "42",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long userId;

    @Schema(
            description = "Tipo di notifica (es. INFO, WARNING, ERROR)",
            example = "INFO"
    )
    private String type;

    @Schema(
            description = "Titolo della notifica",
            example = "Nuovo messaggio"
    )
    private String title;

    @Schema(
            description = "Corpo testuale della notifica",
            example = "Hai ricevuto un nuovo messaggio"
    )
    private String body;

    @Schema(
            description = "Indica se la notifica è già stata letta dall'utente",
            example = "false"
    )
    private boolean read;

    @Schema(
            description = "Data e ora di creazione della notifica in formato UTC (ISO-8601)",
            example = "2024-05-01T10:15:30Z",
            type = "string",
            format = "date-time",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;

    public NotificationRestDTO() {
    }

    public NotificationRestDTO(Long id,
                               Long userId,
                               String type,
                               String title,
                               String body,
                               boolean read,
                               Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.read = read;
        this.createdAt = createdAt;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
