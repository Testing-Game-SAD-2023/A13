package com.example.t9profileservice.client;

import com.example.t9profileservice.dto.GameStatsDTO;
import com.example.t9profileservice.dto.GameDto;
import com.example.t9profileservice.dto.PlayerStats;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class T4GameStatsClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${t4.base-url:http://localhost:8084}")
    private String t4BaseUrl;

    public T4GameStatsClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public GameStatsDTO getStatsForUser(Long playerId) {
        // Usa la base URL configurabile
        String url = t4BaseUrl.replaceAll("/$", "") + "/games/player/" + playerId;

        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");

        try {
            return httpClient.execute(request, response -> handleResponse((ClassicHttpResponse) response, playerId, url));
        } catch (IOException e) {
            log.warn("Failed to call T4 at {}: {}", url, e.toString());
            return new GameStatsDTO(0, 0, null);
        }
    }

    private GameStatsDTO handleResponse(ClassicHttpResponse response, Long playerId, String url) throws IOException {
        int status = response.getCode();
        if (status >= 200 && status < 300) {
            List<GameDto> games = objectMapper.readValue(
                    response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, GameDto.class)
            );

            PlayerStats stats = computeStats(games, playerId);
            return new GameStatsDTO((int) stats.played(), (int) stats.won(), stats.lastMatch());
        } else {
            log.warn("T4 returned non-success status {} for url {}", status, url);
            return new GameStatsDTO(0, 0, null);
        }
    }

    /**
     * Calcola le statistiche di gioco basate sui dati T4.
     *
     * Regole:
     * - matchesPlayed = numero totale di partite restituite
     * - matchesWon = numero di partite dove players.size() == 1 (single-player = vittoria)
     * - lastMatch = closedAt più recente (convertito a LocalDateTime, ignorare null)
     *
     * @param games lista di GameDto da T4
     * @param playerId ID del giocatore (non usato nel calcolo, per futura estensione)
     * @return PlayerStats con played, won e lastMatch (in UTC/LocalDateTime)
     */
    private PlayerStats computeStats(List<GameDto> games, Long playerId) {
        if (games == null || games.isEmpty()) {
            return new PlayerStats(0, 0, null);
        }

        long played = games.size();

        long won = games.stream()
                .filter(g -> g.getPlayers() != null && g.getPlayers().size() == 1)
                .count();

        LocalDateTime lastMatch = games.stream()
                .map(GameDto::getClosedAt)
                .filter(Objects::nonNull)
                .map(offsetDt -> offsetDt.toLocalDateTime())  // Convert OffsetDateTime → LocalDateTime (UTC)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new PlayerStats(played, won, lastMatch);
    }
}
