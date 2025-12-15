package com.example.t9profileservice.dto;

import java.time.LocalDateTime;

public record PlayerBioData(
        Long userId,
        String name,
        String surname,
        String nickname,
        String language,
        Integer matchesPlayed,
        Integer matchesWon,
        LocalDateTime lastMatchAt
) {
}
