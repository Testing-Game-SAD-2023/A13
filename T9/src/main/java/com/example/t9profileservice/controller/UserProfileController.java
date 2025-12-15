package com.example.t9profileservice.controller;

import com.example.t9profileservice.dto.*;
import com.example.t9profileservice.client.T4GameStatsClient;
import com.example.t9profileservice.dto.GameStatsDTO;
import com.example.t9profileservice.model.UserProfile;
import com.example.t9profileservice.service.UserProfileBioService;
import com.example.t9profileservice.service.UserProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileBioService userProfileBioService;
    private final T4GameStatsClient t4GameStatsClient;

    public UserProfileController(UserProfileService service,
                                 UserProfileBioService userProfileBioService,
                                 T4GameStatsClient t4GameStatsClient) {
        this.service = service;
        this.userProfileBioService = userProfileBioService;
        this.t4GameStatsClient = t4GameStatsClient;
    }

    private UserProfileResponseDTO toResponseWithStats(UserProfile profile, Long userIdForStats) {
        // Le stats sono calcolate interrogando T4. Se T4 non è raggiungibile, torna 0/0/null.
        GameStatsDTO stats = t4GameStatsClient.getStatsForUser(userIdForStats);
        return UserProfileResponseDTO.from(
                profile,
                stats != null ? stats.matchesPlayed() : 0,
                stats != null ? stats.matchesWon() : 0,
                stats != null ? stats.lastMatchAt() : null
        );
    }

    private Long requireAuthenticatedUserId(String headerValue) {
        if (headerValue == null || headerValue.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing X-Authenticated-UserId");
        }
        try {
            return Long.parseLong(headerValue);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid X-Authenticated-UserId");
        }
    }

    // ==========================
    // ✅ ENDPOINT "ME" (frontend)
    // ==========================

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile profile = service.getOrCreateByUserId(userId);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    @PutMapping("/me/nickname")
    public ResponseEntity<UserProfileResponseDTO> updateMyNickname(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader,
            @Valid @RequestBody UpdateNicknameRequest request
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile updated = service.updateNickname(userId, request.getNickname());
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    @PutMapping("/me/bio")
    public ResponseEntity<UserProfileResponseDTO> updateMyBio(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader,
            @Valid @RequestBody UpdateBioRequest request
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile updated = service.updateBio(userId, request.getBio());
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    @PutMapping("/me/avatar")
    public ResponseEntity<UserProfileResponseDTO> updateMyAvatar(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader,
            @Valid @RequestBody UpdateAvatarRequest request
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile updated = service.updateAvatar(userId, request);
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    @PutMapping("/me/preferences")
    public ResponseEntity<UserProfileResponseDTO> updateMyPreferences(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader,
            @Valid @RequestBody UpdatePreferencesRequest request
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile updated = service.updatePreferences(userId, request);
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    @PostMapping("/me/generate-bio")
    public ResponseEntity<UserProfileResponseDTO> generateMyBio(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader
    ) {
        Long userId = requireAuthenticatedUserId(userIdHeader);
        UserProfile updated = userProfileBioService.generateAndSaveBio(userId);
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    // ==========================
    // ENDPOINT ESISTENTI (T23 / debug)
    // ==========================

    @PostMapping("/{userId}/generate-bio")
    public ResponseEntity<UserProfileResponseDTO> generateAndSaveBio(@PathVariable Long userId) {
        UserProfile updated = userProfileBioService.generateAndSaveBio(userId);
        return ResponseEntity.ok(toResponseWithStats(updated, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable Long userId) {
        UserProfile profile = service.getOrCreateByUserId(userId);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> upsertProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UserProfile profile = service.upsertBasicProfile(userId, request);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<UserProfileResponseDTO> updateAvatar(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateAvatarRequest request
    ) {
        UserProfile profile = service.updateAvatar(userId, request);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    @PutMapping("/{userId}/preferences")
    public ResponseEntity<UserProfileResponseDTO> updatePreferences(
            @PathVariable Long userId,
            @Valid @RequestBody UpdatePreferencesRequest request
    ) {
        UserProfile profile = service.updatePreferences(userId, request);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    @PostMapping("/{userId}/init")
    public ResponseEntity<UserProfileResponseDTO> initProfile(
            @PathVariable Long userId,
            @RequestBody(required = false) InitProfileRequest request
    ) {
        UserProfile profile = service.initProfile(userId, request);
        return ResponseEntity.ok(toResponseWithStats(profile, userId));
    }

    // ==========================
    // 🔎 RICERCA UTENTI (T5 -> T9)
    // ==========================

    @GetMapping("/search")
    public Page<UserProfileSearchDTO> searchUserProfiles(
            @RequestHeader(value = "X-Authenticated-UserId", required = false) String userIdHeader,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // richiediamo autenticazione per coerenza con il resto delle API
        Long authUserId = requireAuthenticatedUserId(userIdHeader);
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));

        // ✅ Non includere mai l'utente autenticato nei risultati (evita "cerca anche me stessa")
        return service.searchProfiles(searchTerm, pageable, authUserId)
                .map(UserProfileSearchDTO::from);
    }
}
