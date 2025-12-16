package com.g2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.interfaces.ServiceManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Endpoint "proxy" per le funzionalità social (follow/unfollow) di T23.
 *
 * Motivo: dalla UI vogliamo chiamare lo stesso origin (T5) ed evitare di dover conoscere
 * i dettagli di routing/gateway verso T23.
 */
@RestController
@RequestMapping("/social")
public class SocialGraphController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ServiceManager serviceManager;

    public SocialGraphController(ServiceManager serviceManager) {
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

    private ResponseEntity<?> requireAuth(String jwt) {
        Long uid = extractUserIdFromJwt(jwt);
        if (uid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }
        return null;
    }

    /**
     * Ritorna se l'utente autenticato segue targetUserId.
     */
    @GetMapping("/isFollowing")
    public ResponseEntity<?> isFollowing(
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestParam("targetUserId") Long targetUserId
    ) {
        ResponseEntity<?> authErr = requireAuth(jwt);
        if (authErr != null) return authErr;

        Long authUserId = extractUserIdFromJwt(jwt);
        Boolean res = (Boolean) serviceManager.handleRequest(
                "T23",
                "isFollowing",
                authUserId,
                targetUserId
        );
        return ResponseEntity.ok(Map.of("isFollowing", res != null && res));
    }

    /**
     * Toggle follow/unfollow (auth -> target).
     * Ritorna lo stato successivo: { following: true/false }
     */
    @PostMapping("/toggleFollow")
    public ResponseEntity<?> toggleFollow(
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestBody ToggleFollowRequest request
    ) {
        ResponseEntity<?> authErr = requireAuth(jwt);
        if (authErr != null) return authErr;

        Long authUserId = extractUserIdFromJwt(jwt);
        if (request == null || request.getTargetUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing targetUserId"));
        }

        // T23 toggle_follow ritorna true se ORA segue, false se ORA ha smesso
        Object res = serviceManager.handleRequest(
                "T23",
                "followUser",
                request.getTargetUserId(),
                authUserId
        );

        boolean followingNow;
        // a seconda di come viene deserializzato, potrebbe arrivare come String o Boolean
        if (res instanceof Boolean b) {
            followingNow = b;
        } else {
            followingNow = Boolean.parseBoolean(String.valueOf(res));
        }
        return ResponseEntity.ok(Map.of("following", followingNow));
    }

    /**
     * Conta followers e following per un certo utente.
     */
    @GetMapping("/counts")
    public ResponseEntity<?> counts(
            @RequestParam("userId") Long userId
    ) {
        @SuppressWarnings("unchecked")
        List<?> followers = (List<?>) serviceManager.handleRequest("T23", "getFollowers", String.valueOf(userId));
        @SuppressWarnings("unchecked")
        List<?> following = (List<?>) serviceManager.handleRequest("T23", "getFollowing", String.valueOf(userId));

        int followersCount = followers == null ? 0 : followers.size();
        int followingCount = following == null ? 0 : following.size();

        return ResponseEntity.ok(Map.of(
                "followers", followersCount,
                "following", followingCount
        ));
    }

    /**
     * Counts per l'utente autenticato (comodo per la pagina /profile).
     */
    @GetMapping("/me/counts")
    public ResponseEntity<?> myCounts(
            @CookieValue(name = "jwt", required = false) String jwt
    ) {
        ResponseEntity<?> authErr = requireAuth(jwt);
        if (authErr != null) return authErr;
        Long authUserId = extractUserIdFromJwt(jwt);
        return counts(authUserId);
    }

    @Data
    public static class ToggleFollowRequest {
        private Long targetUserId;
    }
}
