package com.example.t9profileservice.service;

import com.example.t9profileservice.client.T4GameStatsClient;
import com.example.t9profileservice.dto.GameStatsDTO;
import com.example.t9profileservice.dto.PlayerBioData;
import com.example.t9profileservice.model.UserProfile;
import com.example.t9profileservice.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerBioDataService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBioDataService.class);

    private final UserProfileRepository userProfileRepository;
    private final T4GameStatsClient t4GameStatsClient;

    public PlayerBioDataService(UserProfileRepository userProfileRepository, T4GameStatsClient t4GameStatsClient) {
        this.userProfileRepository = userProfileRepository;
        this.t4GameStatsClient = t4GameStatsClient;
    }

    public PlayerBioData buildPlayerBioData(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);

        GameStatsDTO stats = null;
        try {
            stats = t4GameStatsClient.getStatsForUser(userId);
        } catch (Exception e) {
            logger.warn("Failed to retrieve game stats for user {}: {}", userId, e.toString());
            stats = null;
        }

        Integer matchesPlayed = stats != null ? Integer.valueOf(stats.matchesPlayed()) : null;
        Integer matchesWon = stats != null ? Integer.valueOf(stats.matchesWon()) : null;
        LocalDateTime lastMatchAt = stats != null ? stats.lastMatchAt() : null;

        String name = profile != null ? profile.getName() : null;
        String surname = profile != null ? profile.getSurname() : null;
        String nickname = profile != null ? profile.getNickname() : null;
        String language = profile != null ? profile.getLanguage() : null;

        return new PlayerBioData(
                userId,
                name,
                surname,
                nickname,
                language,
                matchesPlayed,
                matchesWon,
                lastMatchAt
        );
    }
}
