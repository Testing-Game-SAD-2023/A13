package com.example.t9profileservice.dto;

import com.example.t9profileservice.model.UserProfile;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO di risposta esposto dal microservizio T9.
 */
@Data
public class UserProfileResponseDTO {

    private Integer id;

    // Identificativo logico (uguale a players.id in T23)
    private Long userId;

    // Dati base
    private String name;
    private String surname;
    private String email;
    private String nickname;
    private String bio;

    // Avatar / immagine profilo
    private String profilePicturePath;

    // Personalizzazione profilo
    private String themeColor;
    private String accentColor;
    private String language;
    private Boolean publicProfile;

    // Statistiche (calcolate via T4)
    private Integer matchesPlayed;
    private Integer matchesWon;
    private LocalDateTime lastMatchAt;

    /**
     * Converte l'entità JPA UserProfile in DTO.
     */
    public static UserProfileResponseDTO from(UserProfile profile) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId() != null ? profile.getUserId() : profile.getPlayerId());
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setEmail(profile.getEmail());
        dto.setNickname(profile.getNickname());
        dto.setBio(profile.getBio());
        dto.setProfilePicturePath(profile.getProfilePicturePath());
        dto.setThemeColor(profile.getThemeColor());
        dto.setAccentColor(profile.getAccentColor());
        dto.setLanguage(profile.getLanguage());
        dto.setPublicProfile(profile.getPublicProfile());
        return dto;
    }

    public static UserProfileResponseDTO from(UserProfile profile, Integer matchesPlayed, Integer matchesWon, LocalDateTime lastMatchAt) {
        UserProfileResponseDTO dto = from(profile);
        dto.setMatchesPlayed(matchesPlayed);
        dto.setMatchesWon(matchesWon);
        dto.setLastMatchAt(lastMatchAt);
        return dto;
    }
}
