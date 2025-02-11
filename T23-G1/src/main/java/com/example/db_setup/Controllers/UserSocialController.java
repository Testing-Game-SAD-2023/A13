package com.example.db_setup.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.db_setup.User;
import com.example.db_setup.UserProfile;
import com.example.db_setup.Service.UserService;

@RestController
public class UserSocialController {
    
    @Autowired
    private UserService userService;

    //Rotta per Seguire o smettere di seguire un utente
    @PostMapping("/add-follow")
    public ResponseEntity<?> toggleFollow(@RequestParam("targetUserId") String targetUserId,
                                        @RequestParam("authUserId") String authUserId) {
        System.out.println(targetUserId);
        System.out.println(authUserId);
        return userService.toggleFollow(targetUserId, authUserId);
    }

    //Rotta per ottenere i followers di un utente
    @GetMapping("/get-followers")
    public List<User> getFollowers(@RequestParam("userId") String userId) {
        return userService.getFollowers(userId);
    }

    //Rotta per ottenere gli utenti seguiti da un utente
    @GetMapping("/get-following")
    public List<User> getFollowing(@RequestParam("userId") String userId) {
        return userService.getFollowing(userId);
    }

    //Modifica 04/12/2024 Giuleppe: Aggiunta rotta
    @PostMapping("/getStudentiTeam")
    public ResponseEntity<?> getStudentiTeam(@RequestBody List<String> idsStudenti){
        return userService.getStudentiTeam(idsStudenti);
    }

    @PostMapping("/update_profile")
    public ResponseEntity<Boolean> editProfile(@RequestParam("email") String email,
                                              @RequestParam("bio") String bio,
                                              @RequestParam("profilePicturePath") String profilePicturePath) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Ritorna false in caso di errore
        }
        profile.setBio(bio);
        profile.setProfilePicturePath(profilePicturePath);
        userService.saveProfile(profile);
        return ResponseEntity.ok(true); // Ritorna true se l'operazione ha avuto successo
    }

    @GetMapping("/user_by_email")
    @ResponseBody
    public List<User> getUserByEmail(@RequestParam("email") String email) {
        List<User> users = userService.GetUserListByEmail(email);
        if (users.isEmpty()) {
            return null;
        }    
        return users; // 200 OK con i risultati
    }

}
