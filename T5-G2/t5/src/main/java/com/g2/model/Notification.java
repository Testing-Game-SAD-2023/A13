package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
