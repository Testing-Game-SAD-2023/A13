package com.example.t9profileservice.dto;

import java.time.LocalDateTime;

/**
 * DTO per le statistiche di gioco calcolate da T9.
 * Contiene i dati aggregati basati sulla risposta dell'API T4.
 */
public record PlayerStats(
        long played,
        long won,
        LocalDateTime lastMatch
) {
}
