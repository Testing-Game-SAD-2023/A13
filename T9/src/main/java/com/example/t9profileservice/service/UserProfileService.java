package com.example.t9profileservice.service;

import com.example.t9profileservice.dto.InitProfileRequest;
import com.example.t9profileservice.dto.UpdateAvatarRequest;
import com.example.t9profileservice.dto.UpdatePreferencesRequest;
import com.example.t9profileservice.dto.UpdateUserProfileRequest;
import com.example.t9profileservice.exception.DuplicateNicknameException;
import com.example.t9profileservice.model.UserProfile;
import com.example.t9profileservice.repository.UserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfile getByUserIdOrThrow(Long userId) {
        return repository.findByUserId(userId)
                .or(() -> repository.findByPlayerId(userId))
                .orElseThrow(() ->
                        new RuntimeException("Profile not found for userId/playerId: " + userId)
                );
    }

    public UserProfile getOrCreateByUserId(Long userId) {
        return repository.findByUserId(userId)
                .or(() -> repository.findByPlayerId(userId))
                .orElseGet(() -> createDefaultProfile(userId));
    }

    public UserProfile initProfile(Long userId, InitProfileRequest request) {
        return repository.findByUserId(userId)
                .or(() -> repository.findByPlayerId(userId))
                .map(existing -> {
                    if (request != null) {
                        if (request.getName() != null) existing.setName(request.getName());
                        if (request.getSurname() != null) existing.setSurname(request.getSurname());
                        if (request.getEmail() != null) existing.setEmail(request.getEmail());

                        if (request.getNickname() != null && !"default_nickname".equals(request.getNickname())) {
                            existing.setNickname(request.getNickname());
                        }
                    }

                    if ("default_nickname".equals(existing.getNickname())) {
                        existing.setNickname("user" + userId + "_nick");
                    }

                    if (existing.getBio() == null) existing.setBio("Test addicted...");
                    if (existing.getProfilePicturePath() == null) existing.setProfilePicturePath("default.png");
                    if (existing.getPublicProfile() == null) existing.setPublicProfile(Boolean.TRUE);

                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    if (request == null) {
                        return createDefaultProfile(userId);
                    }

                    UserProfile p = new UserProfile();
                    p.setPlayerId(userId);
                    p.setUserId(userId);

                    p.setName(request.getName() != null ? request.getName() : "default_name");
                    p.setSurname(request.getSurname() != null ? request.getSurname() : "default_surname");

                    String nickname;
                    if (request.getNickname() != null && !"default_nickname".equals(request.getNickname())) {
                        nickname = request.getNickname();
                    } else {
                        nickname = "user" + userId + "_nick";
                    }
                    p.setNickname(nickname);

                    if (request.getEmail() != null) {
                        p.setEmail(request.getEmail());
                    } else {
                        p.setEmail("user" + userId + "@placeholder.local");
                    }

                    p.setBio("Test addicted...");
                    p.setProfilePicturePath("default.png");
                    p.setPublicProfile(Boolean.TRUE);

                    return repository.save(p);
                });
    }

    public UserProfile upsertBasicProfile(Long userId, UpdateUserProfileRequest request) {
        UserProfile profile = getOrCreateByUserId(userId);

        profile.setName(request.getName());
        profile.setSurname(request.getSurname());
        profile.setEmail(request.getEmail());
        profile.setNickname(request.getNickname());
        profile.setBio(request.getBio());

        return repository.save(profile);
    }

    public UserProfile updateAvatar(Long userId, UpdateAvatarRequest request) {
        UserProfile profile = getOrCreateByUserId(userId);
        profile.setProfilePicturePath(request.getProfilePicturePath());
        return repository.save(profile);
    }

    public UserProfile updatePreferences(Long userId, UpdatePreferencesRequest request) {
        UserProfile profile = getOrCreateByUserId(userId);

        if (request.getThemeColor() != null) profile.setThemeColor(request.getThemeColor());
        if (request.getAccentColor() != null) profile.setAccentColor(request.getAccentColor());
        if (request.getLanguage() != null) profile.setLanguage(request.getLanguage());
        if (request.getPublicProfile() != null) profile.setPublicProfile(request.getPublicProfile());

        return repository.save(profile);
    }

    // ✅ NUOVO: aggiorna solo nickname + unicità (409)
    public UserProfile updateNickname(Long userId, String newNickname) {
        UserProfile me = getOrCreateByUserId(userId);

        repository.findByNickname(newNickname).ifPresent(other -> {
            Long otherUserId = other.getUserId();
            if (otherUserId != null && !otherUserId.equals(userId)) {
                throw new DuplicateNicknameException("Nickname already taken: " + newNickname);
            }
        });

        me.setNickname(newNickname);
        return repository.save(me);
    }

    // ✅ NUOVO: aggiorna solo bio
    public UserProfile updateBio(Long userId, String bio) {
        UserProfile me = getOrCreateByUserId(userId);
        me.setBio(bio);
        return repository.save(me);
    }

    /**
     * Ricerca profili nel DB di T9 (source of truth).
     * Se searchTerm è vuoto/blank ritorna una pagina vuota (evita di esporre l'intera lista utenti).
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> searchProfiles(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        String q = searchTerm.trim().toLowerCase();
        return repository.search(q, pageable);
    }

    /**
     * Ricerca profili, escludendo sempre l'utente autenticato (id dal gateway/header).
     * Serve per evitare che la pagina "Cerca amici" restituisca anche il profilo dell'utente stesso.
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> searchProfiles(String searchTerm, Pageable pageable, Long excludeUserId) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        String q = searchTerm.trim().toLowerCase();
        if (excludeUserId == null) {
            return repository.search(q, pageable);
        }
        return repository.searchExcluding(q, excludeUserId, pageable);
    }

    private UserProfile createDefaultProfile(Long userId) {
        UserProfile p = new UserProfile();
        p.setPlayerId(userId);
        p.setUserId(userId);

        p.setName("default_name");
        p.setSurname("default_surname");
        p.setNickname("user" + userId + "_nick");
        p.setEmail("user" + userId + "@placeholder.local");

        p.setBio("Test addicted...");
        p.setProfilePicturePath("default.png");
        p.setPublicProfile(Boolean.TRUE);

        return repository.save(p);
    }
}
