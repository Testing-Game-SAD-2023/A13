package com.example.t9profileservice.service;

import com.example.t9profileservice.llm.LlmClient;
import com.example.t9profileservice.dto.PlayerBioData;
import com.example.t9profileservice.model.UserProfile;
import com.example.t9profileservice.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileBioService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileBioService.class);

    private final PlayerBioDataService playerBioDataService;
    private final LlmClient llmClient;
    private final UserProfileRepository userProfileRepository;

    public UserProfileBioService(PlayerBioDataService playerBioDataService, LlmClient llmClient, UserProfileRepository userProfileRepository) {
        this.playerBioDataService = playerBioDataService;
        this.llmClient = llmClient;
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile generateAndSaveBio(Long userId) {
        PlayerBioData data = playerBioDataService.buildPlayerBioData(userId);

        String bio = llmClient.generateBio(data);

        // Retrieve profile or throw (consistent with existing behavior)
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .or(() -> userProfileRepository.findByPlayerId(userId))
                .orElseThrow(() -> new RuntimeException("Profile not found for userId/playerId: " + userId));

        profile.setBio(bio);
        UserProfile saved = userProfileRepository.save(profile);
        logger.info("Updated bio for user {} (length={})", userId, bio != null ? bio.length() : 0);
        return saved;
    }
}
