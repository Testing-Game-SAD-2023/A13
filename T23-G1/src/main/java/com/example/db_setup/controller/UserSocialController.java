package com.example.db_setup.controller;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.UserProfile;
import com.example.db_setup.service.PlayerService;
import com.example.db_setup.service.UserSocialService;
import com.example.db_setup.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.db_setup.security.jwt.JwtProvider;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserSocialController {

    @Autowired
    private UserSocialService userSocialService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private JwtProvider jwtProvider;

    /*
     *  Sezione ricerca utente
     *
     */
    @GetMapping("/searchUserProfiles")
    public List<UserProfile> searchUserProfiles(
            @RequestParam String searchTerm
            ) {
        return userSocialService.searchUserProfiles(searchTerm);
    }

    @GetMapping("/user_by_email")
    @ResponseBody
    public List<Player> getUserByEmail(@RequestParam("email") String email) {
        List<Player> players = playerService.getUserListByEmail(email);
        if (players.isEmpty()) {
            return null;
        }
        return players; // 200 OK con i risultati
    }

    //Modifica 04/12/2024 Giuleppe: Aggiunta rotta
    @PostMapping("/getStudentiTeam")
    public ResponseEntity<?> getStudentiTeam(@RequestBody List<String> idsStudenti) {
        return playerService.getStudentiTeam(idsStudenti);
    }

    /*
     * Sezione following
     */
    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(userSocialService.getFollowers(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore imprevisto.");
        }
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getFollowing(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(userSocialService.getFollowing(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore imprevisto.");
        }
    }

    @GetMapping("/isFollowing")
    public ResponseEntity<?> isFollowing(
            @RequestParam String followerId,
            @RequestParam String followingId) {
        try {
            boolean result = userSocialService.isFollowing(followerId, followingId);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore imprevisto.");
        }
    }

    @PostMapping("/toggle_follow")
    public ResponseEntity<?> toggleFollow(
            @RequestParam String profileId,
            @RequestParam String targetUserId
    ) {
        try {
            /*
             *   False - Smesso di seguire
             *   True  - Iniziato a seguire
             */
            boolean FollowState = userSocialService.toggleFollow(profileId, targetUserId);
            return ResponseEntity.ok(FollowState);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore imprevisto.");
        }
    }

    /*
     * Gestione Profilo
     */
    @PostMapping("/update_profile")
    public ResponseEntity<Boolean> editProfile(
            @RequestParam("email") String email,
            @RequestParam("nickname") String nick,
            @RequestParam("bio") String bio,
            @RequestParam("profilePicturePath") String profilePicturePath) {

        System.out.println("[DEBUG] /update_profile ricevuto");

        UserProfile profile = playerService.findProfileByEmail(email);
        if (profile == null) {
            System.out.println("Profilo non trovato per email: " + email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        profile.setBio(bio);
        profile.setNickname(nick);
        profile.setProfilePicturePath(profilePicturePath);
        playerService.saveProfile(profile);

        System.out.println("Profilo aggiornato con successo per: " + email);
        return ResponseEntity.ok(true);
    }



}
