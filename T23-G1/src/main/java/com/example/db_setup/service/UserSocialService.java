package com.example.db_setup.service;

import com.example.db_setup.model.UserFollow;
import com.example.db_setup.model.UserProfile;
import com.example.db_setup.model.repository.UserFollowRepository;
import com.example.db_setup.model.repository.UserProfileRepository;
import com.example.db_setup.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserSocialService {

    private static final Logger logger = LoggerFactory.getLogger(UserSocialService.class);
    private final UserProfileRepository userProfileRepository;
    private final UserFollowRepository userFollowRepository;
    private final NotificationService notificationService;

    public UserSocialService(UserProfileRepository userProfileRepository, UserFollowRepository userFollowRepository,
                             NotificationService notificationService) {
        this.userProfileRepository = userProfileRepository;
        this.userFollowRepository = userFollowRepository;
        this.notificationService = notificationService;
    }

    // Verifica se un utente ne segue un altro
    public boolean isFollowing(String followerIdStr, String followingIdStr) {
        Integer followerId = Integer.valueOf(followerIdStr);
        Integer followingId = Integer.valueOf(followingIdStr);
        UserProfile follower = userProfileRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException(generateFollowerNotFoundMessage(followerIdStr)));
        UserProfile following = userProfileRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException(generateFollowingNotFoundMessage(followingIdStr)));

        return userFollowRepository.existsByFollowerAndFollowing(follower, following);
    }

    /**
     * Effettua il toggle della relazione di follow: - Se l'utente sta già
     * seguendo il target, rimuove il follow (unfollow) - Se l'utente NON sta
     * seguendo il target, aggiunge il follow
     */
    @Transactional
    public boolean toggleFollow(String profileIdStr, String targetUserIdStr) {

        Integer profileId = Integer.valueOf(profileIdStr);
        Integer targetUserId = Integer.valueOf(targetUserIdStr);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new UserNotFoundException(generateFollowerNotFoundMessage(profileIdStr)));
        UserProfile targetUser = userProfileRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(generateFollowingNotFoundMessage(targetUserIdStr)));

        if (userFollowRepository.existsByFollowerAndFollowing(profile, targetUser)) {
            // Se già segue, rimuovilo (unfollow)
            userFollowRepository.deleteByFollowerAndFollowing(profile, targetUser);
            return false; // Ora NON segue più
        } else {
            // Se non segue, aggiungilo (follow)
            userFollowRepository.save(new UserFollow(profile, targetUser));
            /*
             * Notifica
             */
            String title = "Hai un nuovo follower";
            String message = "L'utente " + profile.getNickname() + " ha inizato a seguirti";

            notificationService.saveNotification(targetUser.getPlayer().getID(), title, message, "info"
            );
            return true; // Ora sta seguendo
        }
    }

    public List<UserProfile> getFollowers(Long userIdL) {
        System.out.println("[DEBUG] getFollowers chiamato con userIdL=" + userIdL);
        try {
            UserProfile user = userProfileRepository.findByPlayerID(userIdL);

            List<UserProfile> followers = userFollowRepository.findFollowersByUserProfile(user);
            // Log dei risultati
            logger.info("[DEBUG] Follower trovati per userId {}: {}", userIdL, followers);

            return followers;
        } catch (UserNotFoundException e) {
            logger.error("Eccezione durante il recupero dei follower per l'utente con ID " + userIdL, e);
            throw e;
        } catch (Exception e) {
            logger.error("Errore imprevisto durante il recupero dei follower per l'utente con ID " + userIdL, e);
            throw e;
        }
    }

    public List<UserProfile> getFollowing(Long userIdL) {
        System.out.println("[DEBUG] getFollowing chiamato con userIdL=" + userIdL);
        try {

            UserProfile user = userProfileRepository.findByPlayerID(userIdL);

            List<UserProfile> following = userFollowRepository.findFollowingByUserProfile(user);
            // Log dei risultati
            logger.info("[DEBUG] Utenti seguiti da userId {}: {}", userIdL, following);

            return following;
        } catch (UserNotFoundException e) {
            logger.error("Eccezione durante il recupero degli utenti seguiti per l'utente con ID " + userIdL, e);
            throw e;
        } catch (Exception e) {
            logger.error("Errore imprevisto durante il recupero degli utenti seguiti per l'utente con ID " + userIdL, e);
            throw e;
        }
    }


    // Metodo di ricerca per nome, cognome, email o nickname
    public List<UserProfile> searchUserProfiles(String searchTerm) {
        return userProfileRepository.searchByNameSurnameEmailOrNickname(searchTerm);
    }

    /*
     * Di seguito sono riportati i metodi per la generazione del messaggio di errore per UserNotFoundException.
     * Sono necessari per risolvere l'issue identificata da SonarQube riguardo duplicazione della stringa "non trovato"
     */
    private String generateFollowerNotFoundMessage(String followerIdStr) {
        return "Follower con ID %s non trovato".formatted(followerIdStr);
    }

    private String generateFollowingNotFoundMessage(String followingIdStr) {
        return "Following con ID %s non trovato".formatted(followingIdStr);
    }

    private String generateUserNotFoundMessage(String userIdStr) {
        return "User con ID %s non trovato".formatted(userIdStr);
    }

}
