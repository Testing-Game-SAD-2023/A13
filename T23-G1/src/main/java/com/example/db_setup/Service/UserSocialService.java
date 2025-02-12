package com.example.db_setup.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.db_setup.UserFollowRepository;
import com.example.db_setup.UserProfileRepository;
import com.example.db_setup.model.UserFollow;
import com.example.db_setup.model.UserProfile;

@Service
public class UserSocialService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserFollowRepository userFollowRepository;
    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(UserSocialService.class);

    // Verifica se un utente ne segue un altro
    public boolean isFollowing(String followerId, String followingId) {
        Integer followerId_int = Integer.valueOf(followerId);
        Integer followingId_int = Integer.valueOf(followingId);
        UserProfile follower = userProfileRepository.findById(followerId_int)
                .orElseThrow(() -> new UserNotFoundException("Follower con ID " + followerId + " non trovato"));
        UserProfile following = userProfileRepository.findById(followingId_int)
                .orElseThrow(() -> new UserNotFoundException("Following con ID " + followingId + " non trovato"));

        return userFollowRepository.existsByFollowerAndFollowing(follower, following);
    }

    /**
     * Effettua il toggle della relazione di follow: - Se l'utente sta già
     * seguendo il target, rimuove il follow (unfollow) - Se l'utente NON sta
     * seguendo il target, aggiunge il follow
     */
    @Transactional
    public boolean toggleFollow(String followerId, String followingId) {

        Integer followerId_int = Integer.valueOf(followerId);
        Integer followingId_int = Integer.valueOf(followingId);

        UserProfile follower = userProfileRepository.findById(followerId_int)
                .orElseThrow(() -> new UserNotFoundException("Follower con ID " + followerId + " non trovato"));
        UserProfile following = userProfileRepository.findById(followingId_int)
                .orElseThrow(() -> new UserNotFoundException("Following con ID " + followingId + " non trovato"));

        if (userFollowRepository.existsByFollowerAndFollowing(follower, following)) {
            // Se già segue, rimuovilo (unfollow)
            userFollowRepository.deleteByFollowerAndFollowing(follower, following);
            return false; // Ora NON segue più
        } else {
            // Se non segue, aggiungilo (follow)
            userFollowRepository.save(new UserFollow(follower, following));
            /*
             * Notifica 
             */
            String Titolo = "Hai un nuovo follower";
            String Messagge = "L'utente " + follower.getNickname() + "ha inizato a seguirti";

            notificationService.saveNotification(following.getUser().getID(), Titolo, Messagge, "info"
            );
            return true; // Ora sta seguendo
        }
    }

    public List<UserProfile> getFollowers(String userId) {
        try {
            Integer userId_int = Integer.valueOf(userId);
            UserProfile user = userProfileRepository.findById(userId_int)
                    .orElseThrow(() -> new UserNotFoundException("User con ID " + userId + " non trovato"));
            return userFollowRepository.findFollowersByUserProfile(user);
        } catch (UserNotFoundException e) {
            // Log dell'eccezione
            logger.error("Eccezione durante il recupero dei follower per l'utente con ID " + userId, e);
            // Rilancio dell'eccezione
            throw e;
        } catch (Exception e) {
            // Gestione di altre eccezioni generiche, se necessario
            logger.error("Errore imprevisto durante il recupero dei follower per l'utente con ID " + userId, e);
            throw e; // Rilancia l'eccezione generica
        }
    }

    public List<UserProfile> getFollowing(String userId) {
        try {
            Integer userId_int = Integer.valueOf(userId);
            UserProfile user = userProfileRepository.findById(userId_int)
                    .orElseThrow(() -> new UserNotFoundException("User con ID " + userId + " non trovato"));
            return userFollowRepository.findFollowingByUserProfile(user);
        } catch (UserNotFoundException e) {
            // Log dell'eccezione
            logger.error("Eccezione durante il recupero degli utenti seguiti per l'utente con ID " + userId, e);
            // Rilancio dell'eccezione
            throw e;
        } catch (Exception e) {
            // Gestione di altre eccezioni generiche, se necessario
            logger.error("Errore imprevisto durante il recupero degli utenti seguiti per l'utente con ID " + userId, e);
            throw e; // Rilancia l'eccezione generica
        }
    }

    // Metodo di ricerca per nome, cognome, email o nickname
    public Page<UserProfile> searchUserProfiles(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userProfileRepository.searchByNameSurnameEmailOrNickname(searchTerm, pageable);
    }

}
