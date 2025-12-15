package com.g2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.components.GenericObjectComponent;
import com.g2.components.PageBuilder;
import com.g2.components.UserProfileComponent;
import com.g2.components.LeaderboardComponent;
import com.g2.interfaces.ServiceManager;
import com.g2.model.GameConfigData;
import com.g2.model.User;
import com.g2.model.dto.GameProgressDTO;
import com.g2.model.dto.PlayerProgressDTO;
import com.g2.model.dto.ResponseTeamComplete;
import com.g2.security.JwtRequestContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * Tutte le chiamate legate al profilo utente
 */
@CrossOrigin
@Controller

public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private final ServiceManager serviceManager;
    private GameConfigData gameConfigData = null;
    @Value("${config.gamification.file}")
    private String gamificationConFile;

    @Autowired
    public UserProfileController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @PostConstruct
    public void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("%s/%s".formatted(System.getProperty("user.dir"), gamificationConFile.replace("/", File.separator)));
            this.gameConfigData = objectMapper.readValue(file, GameConfigData.class);
        } catch (IOException e) {
            logger.info("[PostConstruct init] Error in loading gamification_config.json, using default values: {}", e.getMessage());
            this.gameConfigData = new GameConfigData(10, 5, 1);
        }
    }

    @GetMapping("/SearchFriend")
    public String showSearchFriendPage(Model model) {
        PageBuilder searchPage = new PageBuilder(serviceManager, "search", model, JwtRequestContext.getJwtToken());
        // search_page.SetAuth();  // Gestisce l'autenticazione
        return searchPage.handlePageRequest();
    }

    @GetMapping("/profile")
    public String profilePagePersonal(Model model) {
        PageBuilder profilePage = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());

        Long userId = profilePage.getUserId();
        profilePage.setObjectComponents(new UserProfileComponent(serviceManager, false, userId));
        return profilePage.handlePageRequest();
    }

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable("playerID") Long playerID) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());

        Long userId = profile.getUserId();
        if (userId.equals(playerID)) {
            return "redirect:/profile";
        }

        profile.setObjectComponents(
                new UserProfileComponent(serviceManager, true, userId, playerID)
        );
        return profile.handlePageRequest();
    }

    @GetMapping("/Team")
    public String profileTeamPage(Model model) {
        PageBuilder teamPage = new PageBuilder(serviceManager, "Team", model, JwtRequestContext.getJwtToken());

        //ResponseTeamComplete team = (ResponseTeamComplete) serviceManager.handleRequest("T1", "OttieniTeamCompleto", teamPage.getUserId());
        ResponseTeamComplete team = (ResponseTeamComplete) serviceManager.handleRequest("T1", "OttieniTeamCompleto", String.valueOf(teamPage.getUserId())); //=================== Richiesto un parametro String nella chiamata


        if (team != null) {
            @SuppressWarnings("unchecked")
            List<User> membri = (List<User>) serviceManager.handleRequest("T23", "GetUsersByList", team.getTeam().getStudenti());
            model.addAttribute("response", team);
            model.addAttribute("membri", membri);
        }
        return teamPage.handlePageRequest();
    }



    // ============================== Aggiunta funzione per recuperare i dati quando si carica la pagina achievement
    @GetMapping("/Achievement/{userId}")
    @ResponseBody
    public Map<String, Object> getAchievementData(@PathVariable Long userId) {
 
        PlayerProgressDTO p = (PlayerProgressDTO) serviceManager
                .handleRequest("T23", "getPlayerProgressAgainstAllOpponent", userId);
 
        Map<String, Object> map = new HashMap<>();
 
        map.put("experiencePoints", p.getExperiencePoints());
        map.put("gameProgressesDTO", p.getGameProgressesDTO());
        map.put("globalAchievements", p.getGlobalAchievements());
 
        // aggiungi anche i valori di configurazione (obbligatori)
        map.put("startingLevel", gameConfigData.getStartingLevel());
        map.put("expPerLevel", gameConfigData.getExpPerLevel());
        map.put("maxLevel", gameConfigData.getMaxLevel());
 
        return map;
    }




    //=========================== AGGIUNTA: Nuova rotta aggiunta per il caricamento di dati json da passare al profilo
    //che è ora responsabile di visualizzare la rotta.
    @GetMapping("/leaderboard/{userId}")
    @ResponseBody
    public Map<String, Object> getLeaderboardData(@PathVariable Long userId) {

        // 1. Component contenitore (identico alla rotta HTML)
        GenericObjectComponent leaderboardObjectComponent = new GenericObjectComponent(null, null);

        // 2. LogicComponent che costruisce la classifica
        LeaderboardComponent leaderboardComponent =
                new LeaderboardComponent(leaderboardObjectComponent, userId, serviceManager);

        // 3. Esegui la logica
        boolean ok = leaderboardComponent.executeLogic();
        if (!ok) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Unable to load leaderboard");
            return error;
        }

        // 4. Ritorna direttamente la map popolata
        return leaderboardObjectComponent.getModel();
    }


 

    @GetMapping("/Notification")
    public String showProfileNotificationPage(Model model) {
        PageBuilder notificationPage = new PageBuilder(serviceManager, "notification", model, JwtRequestContext.getJwtToken());

        return notificationPage.handlePageRequest();
    }


    /*
     *    TENERE QUESTA CHIAMATA SOLO PER DEBUG DA DISATTIVARE
     *
     */
    @GetMapping("/profile/{playerID}")
    public String profilePage(Model model,
                              @PathVariable(value = "playerID") Long playerID) {

        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());
        profile.setObjectComponents(
                new UserProfileComponent(serviceManager, false, playerID)
        );
        return profile.handlePageRequest();
    }

    /*
     * Andrebbe gestito che ogni uno può mettere la foto che vuole con i tipi Blob nel DB
     */
    private List<String> getProfilePictures() {
        List<String> images = new ArrayList<>();
        images.add("default.png");
        images.add("men-1.png");
        images.add("men-2.png");
        images.add("men-3.png");
        images.add("men-4.png");
        images.add("women-1.png");
        images.add("women-2.png");
        images.add("women-3.png");
        images.add("women-4.png");
        return images;
    }



    // =================================================== ok
    @GetMapping("/followers")
    @ResponseBody
    public ResponseEntity<?> getFollowers(@RequestParam String userId) {
        try {
            Object result = serviceManager.handleRequest(
                    "T23",
                    "getFollowers",
                    userId
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(List.of()); // niente 500 verso front
        }
    }



    // ================================================================== ok
    @GetMapping("/following")
    @ResponseBody
    public ResponseEntity<?> getFollowing(@RequestParam String userId) {
        try {
            Object result = serviceManager.handleRequest(
                    "T23",
                    "getFollowing",
                    userId
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(List.of()); // niente 500 verso front
        }
    }



    // NEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEW ok
    @GetMapping("/isFollowing")
    @ResponseBody
    public ResponseEntity<Boolean> isFollowing(
            @RequestParam String followerId,
            @RequestParam String followingId
    ){
        try {
    
            Object result = serviceManager.handleRequest(
                    "T23",
                    "isFollowing",
                    followerId,
                    followingId
            );
    
            boolean value = result instanceof Boolean && (Boolean) result;
    
            return ResponseEntity.ok(value);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false); // NON ritornare 500 al frontend
        }
    }
    


    // NEEEEEEEEEEEEEEEEEEEEEEEEW ok
    @PostMapping("/toggle_follow")
    @ResponseBody
    public ResponseEntity<Boolean> toggleFollow(
            @RequestParam String followerId,
            @RequestParam String followingId
    ){
    
        try {
    
            Object result = serviceManager.handleRequest(
                    "T23",
                    "toggle_follow", 
                    followerId,
                    followingId
            );
    
            // se null → errore logico → ritorna false
            boolean followState = result instanceof Boolean && (Boolean) result;
    
            return ResponseEntity.ok(followState);
    
        } catch (Exception e) {
            e.printStackTrace();
    
            // in caso di errore NON mandare 500 al frontend
            return ResponseEntity.ok(false);
        }
    }
    
    


    


    // ============================== MODIFICA ok
    @GetMapping("/edit_profile")
    public String showEditProfile(Model model) {
        try {
            PageBuilder editProfilePage = new PageBuilder(serviceManager, "Edit_Profile", model, JwtRequestContext.getJwtToken());
    
            Long userId = editProfilePage.getUserId();
            User user = (User) serviceManager.handleRequest("T23", "GetUser", userId);
    
            if (user == null) {
                return "error";
            }
    
            List<String> images = getProfilePictures();
    
            editProfilePage.setObjectComponents(
                    new GenericObjectComponent("player", user),
                    new GenericObjectComponent("images", images)
            );
    
            return editProfilePage.handlePageRequest();
    
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    


    // ============================== ok
    @PostMapping("/update_profile")
    public ResponseEntity<Boolean> editProfile(

            @RequestParam("email") String email,
            @RequestParam("bio") String bio,
            @RequestParam("profilePicturePath") String profilePicturePath,
            @RequestParam("nickname") String nickname) {

                System.out.println("[DEBUG] update_profile chiamato per email: " + email);

        try {
            Boolean updated = (Boolean) serviceManager.handleRequest(
                    "T23",
                    "UpdateProfile",
                    email,
                    bio,
                    profilePicturePath,
                    nickname
            );

            return ResponseEntity.ok(updated != null ? updated : false);

        } catch (Exception e) {
            e.printStackTrace();
            // Basta ritornare ok(false) senza usare HttpStatus
            return ResponseEntity.ok(false);
        }



}



}
