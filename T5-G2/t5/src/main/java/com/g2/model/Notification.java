package com.g2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("playerID")
    private Long playerID;

    @JsonProperty("titolo")
    private String titolo;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("isRead")
    private Boolean isRead;

    public Notification(Long id, Long playerID, String titolo, String message, LocalDateTime timestamp, Boolean isRead) {
        this.id = id;
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }
}
