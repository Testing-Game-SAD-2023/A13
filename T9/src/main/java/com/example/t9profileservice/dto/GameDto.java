package com.example.t9profileservice.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO che rappresenta una Game come restituita dall'API T4.
 * Usato solo internamente da T4GameStatsClient per il parsing.
 * 
 * ⚠️ IMPORTANTE: startedAt e closedAt sono OffsetDateTime per supportare
 *     timestamp ISO-8601 con timezone offset (es: "2025-12-11T09:33:37.123+00:00")
 */
public class GameDto {
    private long id;
    private List<Long> players;
    private String status;
    private String gameMode;
    private OffsetDateTime startedAt;
    private OffsetDateTime closedAt;

    // Costruttore
    public GameDto() {}

    public GameDto(long id, List<Long> players, String status, String gameMode, 
                   OffsetDateTime startedAt, OffsetDateTime closedAt) {
        this.id = id;
        this.players = players;
        this.status = status;
        this.gameMode = gameMode;
        this.startedAt = startedAt;
        this.closedAt = closedAt;
    }

    // Getter e Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public void setPlayers(List<Long> players) {
        this.players = players;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(OffsetDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
