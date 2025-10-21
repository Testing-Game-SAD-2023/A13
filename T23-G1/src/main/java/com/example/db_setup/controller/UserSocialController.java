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

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserSocialController {

    @Autowired
    private UserSocialService userSocialService;
    @Autowired
    private PlayerService playerService;

    /*
     *  Sezione ricerca utente
     *
     */
    @GetMapping("/searchUserProfiles")
    public Page<UserProfile> searchUserProfiles(
            @RequestParam String searchTerm,
            @RequestParam int page,
            @RequestParam int size) {

        return userSocialService.searchUserProfiles(searchTerm, page, size);
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
    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(userSocialService.getFollowers(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore imprevisto.");
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(@RequestParam String userId) {
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
            @RequestParam String followerId,
            @RequestParam String followingId
    ) {
        try {
            /*
             *   False - Smesso di seguire
             *   True  - Iniziato a seguire
             */
            boolean FollowState = userSocialService.toggleFollow(followerId, followingId);
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
    public ResponseEntity<Boolean> editProfile(@RequestParam("email") String email,
                                               @RequestParam("bio") String bio,
                                               @RequestParam("profilePicturePath") String profilePicturePath,
                                               @RequestParam("nickname") String nickname) {

        UserProfile profile = playerService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Ritorna false in caso di errore
        }
        profile.setBio(bio);
        profile.setProfilePicturePath(profilePicturePath);
        profile.setNickname(nickname);
        playerService.saveProfile(profile);
        return ResponseEntity.ok(true); // Ritorna true se l'operazione ha avuto successo
    }


}
