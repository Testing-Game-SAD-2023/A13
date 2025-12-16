package com.example.t9profileservice.dto;

import java.time.LocalDateTime;

public record GameStatsDTO(
        int matchesPlayed,
        int matchesWon,
        LocalDateTime lastMatchAt
) {
}
