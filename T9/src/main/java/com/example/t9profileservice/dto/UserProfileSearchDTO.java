package com.example.t9profileservice.dto;

import com.example.t9profileservice.model.UserProfile;
import lombok.Data;

/**
 * DTO leggero per la ricerca utenti.
 * Serve alla UI (T5) per mostrare card e link al profilo di altri utenti.
 */
@Data
public class UserProfileSearchDTO {

    private Long userId;
    private String name;
    private String surname;
    private String email;
    private String nickname;
    private String profilePicturePath;

    public static UserProfileSearchDTO from(UserProfile profile) {
        UserProfileSearchDTO dto = new UserProfileSearchDTO();
        dto.setUserId(profile.getUserId() != null ? profile.getUserId() : profile.getPlayerId());
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setEmail(profile.getEmail());
        dto.setNickname(profile.getNickname());
        dto.setProfilePicturePath(profile.getProfilePicturePath());
        return dto;
    }
}
