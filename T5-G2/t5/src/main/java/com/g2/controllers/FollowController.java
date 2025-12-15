package com.g2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.interfaces.ServiceManager;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Compat controller per il routing del UI Gateway.
 *
 * Il gateway inoltra le chiamate social verso T5 solo per le rotte:
 *   - /friend/{id} (pagina profilo)
 *   - /follow/{id} (azione segui/non seguire)
 *
 * Le API “native” erano state esposte su /social/**, ma quella rotta non viene
 * proxata dal gateway. Questo controller fornisce quindi un alias su /follow/{id}
 * senza modificare né il gateway né le API già esistenti su /social/**.
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ServiceManager serviceManager;

    public FollowController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    private Long extractUserIdFromJwt(String jwt) {
        try {
            if (jwt == null || jwt.split("\\.").length < 2) return null;
            byte[] decodedBytes = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            String decodedJson = new String(decodedBytes, StandardCharsets.UTF_8);
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = MAPPER.readValue(decodedJson, Map.class);
            Object userId = payload.get("userId");
            return userId == null ? null : Long.parseLong(userId.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * GET /follow/{targetUserId}
     * Ritorna sempre i counts e, se l'utente è loggato, anche isFollowing.
     */
    @GetMapping("/{targetUserId}")
    public ResponseEntity<?> statusAndCounts(
            @CookieValue(name = "jwt", required = false) String jwt,
            @PathVariable("targetUserId") Long targetUserId
    ) {
        if (targetUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing targetUserId"));
        }

        // counts (pubblico)
        @SuppressWarnings("unchecked")
        List<?> followers = (List<?>) serviceManager.handleRequest("T23", "getFollowers", String.valueOf(targetUserId));
        @SuppressWarnings("unchecked")
        List<?> following = (List<?>) serviceManager.handleRequest("T23", "getFollowing", String.valueOf(targetUserId));

        int followersCount = followers == null ? 0 : followers.size();
        int followingCount = following == null ? 0 : following.size();

        // isFollowing (solo se autenticato)
        Long authUserId = extractUserIdFromJwt(jwt);
        Boolean isFollowing = null;
        if (authUserId != null) {
            Boolean res = (Boolean) serviceManager.handleRequest(
                    "T23",
                    "isFollowing",
                    authUserId,
                    targetUserId
            );
            isFollowing = res != null && res;
        }

        return ResponseEntity.ok(Map.of(
                "followers", followersCount,
                "following", followingCount,
                "isFollowing", isFollowing
        ));
    }

    /**
     * POST /follow/{targetUserId}
     * Toggle follow/unfollow (auth -> target). Richiede jwt cookie.
     */
    @PostMapping("/{targetUserId}")
    public ResponseEntity<?> toggleFollow(
            @CookieValue(name = "jwt", required = false) String jwt,
            @PathVariable("targetUserId") Long targetUserId,
            @RequestBody(required = false) ToggleFollowRequest ignoredBody
    ) {
        Long authUserId = extractUserIdFromJwt(jwt);
        if (authUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }
        if (targetUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing targetUserId"));
        }

        // T23 followUser (toggle) ritorna true se ORA segue, false se ORA ha smesso
        Object res = serviceManager.handleRequest(
                "T23",
                "followUser",
                targetUserId,
                authUserId
        );

        boolean followingNow;
        if (res instanceof Boolean b) {
            followingNow = b;
        } else {
            followingNow = Boolean.parseBoolean(String.valueOf(res));
        }

        // Ritorniamo anche i counts aggiornati (comodo per la UI)
        @SuppressWarnings("unchecked")
        List<?> followers = (List<?>) serviceManager.handleRequest("T23", "getFollowers", String.valueOf(targetUserId));
        @SuppressWarnings("unchecked")
        List<?> following = (List<?>) serviceManager.handleRequest("T23", "getFollowing", String.valueOf(targetUserId));

        int followersCount = followers == null ? 0 : followers.size();
        int followingCount = following == null ? 0 : following.size();

        return ResponseEntity.ok(Map.of(
                "following", followingNow,
                "followers", followersCount,
                "followingCount", followingCount
        ));
    }

    @Data
    public static class ToggleFollowRequest {
        // Solo per compatibilità con eventuali client che mandano JSON.
        private Long targetUserId;
    }
}
