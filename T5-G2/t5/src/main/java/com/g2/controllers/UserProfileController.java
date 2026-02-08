package com.g2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.g2.model.dto.NewMessageDTO;
import org.springframework.web.bind.annotation.RequestBody;
import com.g2.model.Message;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.g2.interfaces.T23Service;

import com.g2.model.dto.TeamDTO; // Assicurati che la classe esista in questo package




/*
 * Tutte le chiamate legate al profilo utente
 */
@CrossOrigin
@Controller
public class UserProfileController {


    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private final ServiceManager serviceManager;
    private final T23Service t23Service;
    private GameConfigData gameConfigData = null;

    @Value("${config.gamification.file}")
    private String gamificationConFile;

    @Autowired
    public UserProfileController(ServiceManager serviceManager, T23Service t23Service) {
        this.serviceManager = serviceManager;
        this.t23Service = t23Service;
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
    /*
    @GetMapping("/SearchFriend")
    public String showSearchFriendPage(Model model) {
        PageBuilder searchPage = new PageBuilder(serviceManager, "search", model, JwtRequestContext.getJwtToken());
        Long playerId = searchPage.getUserId();


        User user = (User) serviceManager.handleRequest("T23", "GetUser", playerId);
        Long userProfileId = null;
        if (user != null && user.getUserProfile() != null) {
            userProfileId = user.getUserProfile().getId().longValue();
        }

        model.addAttribute("currentUserId", userProfileId);  // <-- questo ora è l’id UserProfile T23

        return searchPage.handlePageRequest();
    }*/

    @GetMapping("/SearchFriend")
    public String showSearchFriendPage(Model model) {
        PageBuilder searchPage = new PageBuilder(serviceManager, "search", model, JwtRequestContext.getJwtToken());
        Long playerId = searchPage.getUserId();

        User user = (User) serviceManager.handleRequest("T23", "GetUser", playerId);
        Long userProfileId = null;
        if (user != null && user.getUserProfile() != null) {
            userProfileId = user.getUserProfile().getId().longValue();
        }

        model.addAttribute("currentUserId", userProfileId);
        model.addAttribute("mode", "friend");   // ← ricerca utenti

        return searchPage.handlePageRequest();  // "search" → search.html
    }

    /*
        @GetMapping("/profile")
        public String profilePagePersonal(Model model) {
            PageBuilder profilePage = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());

            Long userId = profilePage.getUserId();
            profilePage.setObjectComponents(new UserProfileComponent(serviceManager, false, userId));

            model.addAttribute("ownProfile", true);

            // Recupera i dati di progresso del giocatore
            PlayerProgressDTO playerProgress = (PlayerProgressDTO) serviceManager.handleRequest(
                    "T23", "getPlayerProgressAgainstAllOpponent", userId);

            if (playerProgress != null) {
                model.addAttribute("userCurrentExperience", playerProgress.getExperiencePoints());
                model.addAttribute("startingLevel", gameConfigData.getStartingLevel());
                model.addAttribute("expPerLevel", gameConfigData.getExpPerLevel());
                model.addAttribute("maxLevel", gameConfigData.getMaxLevel());
            }

            return profilePage.handlePageRequest();
        }
     */
    @GetMapping("/profile")
    public String profilePagePersonal(Model model) {
        PageBuilder profilePage = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());

        Long userId = profilePage.getUserId();
        profilePage.setObjectComponents(new UserProfileComponent(serviceManager, false, userId));

        model.addAttribute("ownProfile", true);

        PlayerProgressDTO playerProgress = (PlayerProgressDTO) serviceManager
                .handleRequest("T23", "getPlayerProgressAgainstAllOpponent", userId);

        if (playerProgress != null) {
            model.addAttribute("userCurrentExperience", playerProgress.getExperiencePoints());
            model.addAttribute("startingLevel", gameConfigData.getStartingLevel());
            model.addAttribute("expPerLevel", gameConfigData.getExpPerLevel());
            model.addAttribute("maxLevel", gameConfigData.getMaxLevel());
        }

        return profilePage.handlePageRequest();
    }



/*
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
        model.addAttribute("ownProfile", false);

        PlayerProgressDTO playerProgress = (PlayerProgressDTO) serviceManager.handleRequest(
                "T23", "getPlayerProgressAgainstAllOpponent", playerID);

        if (playerProgress != null) {
            model.addAttribute("userCurrentExperience", playerProgress.getExperiencePoints());
            model.addAttribute("startingLevel", gameConfigData.getStartingLevel());
            model.addAttribute("expPerLevel", gameConfigData.getExpPerLevel());
            model.addAttribute("maxLevel", gameConfigData.getMaxLevel());
        }

        return profile.handlePageRequest();
    }

/*
    @GetMapping("/Team")
    public String profileTeamPage(Model model) {
        PageBuilder teamPage = new PageBuilder(serviceManager, "Team", model, JwtRequestContext.getJwtToken());

        List<ResponseTeamComplete> teams = (List<ResponseTeamComplete>) serviceManager.handleRequest("TXX", "GetAllTeams", teamPage.getUserId());
        model.addAttribute("teams", teams);

        // DTO vuoto per il form
        model.addAttribute("teamForm", new TeamDTO());

        return teamPage.handlePageRequest();
    }

    @GetMapping("/searchTeam")
    public String showSearchTeamPage(Model model) {
        PageBuilder searchTeamPage = new PageBuilder(serviceManager, "SearchTeam", model, JwtRequestContext.getJwtToken());
        return searchTeamPage.handlePageRequest();
    }*/

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable("playerID") Long playerID) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());

        Long userId = profile.getUserId();

        Long viewerId = profile.getUserId();

        if (userId.equals(playerID)) {
            return "redirect:/profile";
        }

        profile.setObjectComponents(
                new UserProfileComponent(serviceManager, true, userId, playerID)
        );
        model.addAttribute("ownProfile", false);

        PlayerProgressDTO playerProgress = (PlayerProgressDTO) serviceManager.handleRequest(
                "T23", "getPlayerProgressAgainstAllOpponent", playerID);

        if (playerProgress != null) {
            model.addAttribute("userCurrentExperience", playerProgress.getExperiencePoints());
            model.addAttribute("startingLevel", gameConfigData.getStartingLevel());
            model.addAttribute("expPerLevel", gameConfigData.getExpPerLevel());
            model.addAttribute("maxLevel", gameConfigData.getMaxLevel());
        }

        return profile.handlePageRequest();
    }

    @GetMapping("/Team")
    public String showSearchTeamPage(Model model) {
        logger.info(">>> Entrato in /SearchTeam");

        PageBuilder searchPage = new PageBuilder(serviceManager, "search", model, JwtRequestContext.getJwtToken());
        Long playerId = searchPage.getUserId();

        User user = (User) serviceManager.handleRequest("T23", "GetUser", playerId);
        Long userProfileId = null;
        if (user != null && user.getUserProfile() != null) {
            userProfileId = user.getUserProfile().getId().longValue();
        }

        model.addAttribute("currentUserId", userProfileId);
        model.addAttribute("mode", "team");   // IMPORTANTE

        return searchPage.handlePageRequest();
    }


    @GetMapping("/Achievement")
    public String showAchievements(Model model) {
        PageBuilder achievement = new PageBuilder(serviceManager, "Achivement", model, JwtRequestContext.getJwtToken());
        /*
         * Richiedo a T4 lo stato del giocatore
         */
        PlayerProgressDTO playerProgress = (PlayerProgressDTO) serviceManager.handleRequest("T23", "getPlayerProgressAgainstAllOpponent", achievement.getUserId());
        List<GameProgressDTO> achievements = playerProgress.getGameProgressesDTO();
        Set<String> globalAchievements = playerProgress.getGlobalAchievements();
        model.addAttribute("gamemode_achievements", achievements);
        model.addAttribute("general_achievements", globalAchievements);
        model.addAttribute("userCurrentExperience", playerProgress.getExperiencePoints());

        model.addAttribute("startingLevel", gameConfigData.getStartingLevel());
        model.addAttribute("expPerLevel", gameConfigData.getExpPerLevel());
        model.addAttribute("maxLevel", gameConfigData.getMaxLevel());

        return achievement.handlePageRequest();
    }

    //    Handler per la costruzione della pagina contenente la classifica
//    La pagina è costruita utilizzando un ObjectComponent "riempito" da un LogicComponent
    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        PageBuilder leaderboardPage = new PageBuilder(serviceManager, "Leaderboard", model, JwtRequestContext.getJwtToken());
        GenericObjectComponent leaderboardObjectComponent = new GenericObjectComponent(null, null);
        LeaderboardComponent leaderboardComponent = new LeaderboardComponent(leaderboardObjectComponent, leaderboardPage.getUserId(), serviceManager);
        leaderboardPage.setLogicComponents(leaderboardComponent);
        leaderboardPage.setObjectComponents(leaderboardObjectComponent);
        return leaderboardPage.handlePageRequest();
    }



    @PostMapping("/update_profile")
    public ResponseEntity<String> updateProfile(
            @RequestParam String email,
            @RequestParam String bio,
            @RequestParam String profilePicturePath) {
        try {
            // Chiama direttamente il servizio T23 per aggiornare il profilo
            Boolean success = (Boolean) serviceManager.handleRequest(
                    "T23",
                    "UpdateProfile",
                    email,
                    bio,
                    profilePicturePath
            );

            if (success != null && success) {
                return ResponseEntity.ok("Profilo aggiornato con successo");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Errore durante l'aggiornamento del profilo");
            }
        } catch (IllegalArgumentException e) {
            if ("BIO_TOO_LONG".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("BIO_TOO_LONG");
            }
            // altro caso di IllegalArgumentException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Richiesta non valida");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento del profilo: " + e.getMessage());
        }
    }





    @GetMapping("/Notification")
    public String showProfileNotificationPage(Model model) {
        PageBuilder notificationPage = new PageBuilder(serviceManager, "notification", model, JwtRequestContext.getJwtToken());

        return notificationPage.handlePageRequest();
    }

    @GetMapping("/Games")
    public String showGameHistory(Model model) {
        PageBuilder gameHistoryPage = new PageBuilder(serviceManager, "GameHistory", model, JwtRequestContext.getJwtToken());

        Long userId = gameHistoryPage.getUserId();
        User user = (User) serviceManager.handleRequest("T23", "GetUser", userId);
        model.addAttribute("user", user);

        return gameHistoryPage.handlePageRequest();
    }



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

    @GetMapping("/edit_profile")
    public String showEditProfile(Model model) {
        PageBuilder editProfilePage = new PageBuilder(serviceManager, "Edit_Profile", model, JwtRequestContext.getJwtToken());
        User user = (User) serviceManager.handleRequest("T23", "GetUser", editProfilePage.getUserId());

        if (user == null) {
            return "error";
        }

        List<String> images = getProfilePictures();

        model.addAttribute("player", user);
        model.addAttribute("bio", user.getUserProfile().getBio());
        model.addAttribute("propic", user.getUserProfile().getProfilePicturePath());
        model.addAttribute("images", images);

        return editProfilePage.handlePageRequest();
    }
    @PostMapping("/messages/new")
    public ResponseEntity<Void> newMessage(@RequestBody NewMessageDTO dto) {
        try {
            PageBuilder tmp = new PageBuilder(serviceManager, "profile", null, JwtRequestContext.getJwtToken());
            Long senderId = tmp.getUserId();

            dto.setSenderId(senderId);

            t23Service.sendMessage(
                    dto.getSenderId(),
                    dto.getReceiverId(),
                    dto.getContent()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/messages/inbox")
    public ResponseEntity<List<Message>> getInboxMessages() {
        try {
            PageBuilder page = new PageBuilder(serviceManager, "profile", null, JwtRequestContext.getJwtToken());
            Long userId = page.getUserId();
            logger.info("Caricamento inbox per userId: {}", userId); // DEBUG


            @SuppressWarnings("unchecked")
            List<Message> messages = (List<Message>) serviceManager
                    .handleRequest("T23", "GetInboxMessages", userId);

            logger.info("T23 ha restituito {} messaggi", messages != null ? messages.size() : 0);

            // SE VUOTA, fallback locale
            if (messages == null || messages.isEmpty()) {
                logger.warn("T23 non ha messaggi, usando fallback locale");
                // TODO: implementa MessageRepository.findByReceiverId(userId)
            }

            return ResponseEntity.ok(messages != null ? messages : new ArrayList<>());
        } catch (Exception e) {
            logger.error("Errore getInboxMessages", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

   /* @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteInboxMessage(@PathVariable Long id,
                                                   @RequestParam Long userId) {
        logger.info("DELETE /messages/{} per userId={}", id, userId);
        serviceManager.handleRequest("T23", "DeleteMessage", id, userId);
        return ResponseEntity.noContent().build();
    } */

    @PostMapping("/messages/delete")
    public ResponseEntity<Void> deleteInboxMessagePost(@RequestParam Long id) {
        PageBuilder page = new PageBuilder(serviceManager, "profile", null, JwtRequestContext.getJwtToken());
        Long userId = page.getUserId();   // ricavato dal token/sessione

        logger.info("POST /messages/delete id={} userId={}", id, userId);
        serviceManager.handleRequest("T23", "DeleteMessage", id, userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/messages/outbox")
    public ResponseEntity<List<Message>> getOutboxMessages() {
        try {
            PageBuilder page = new PageBuilder(serviceManager, "profile", null, JwtRequestContext.getJwtToken());
            Long userId = page.getUserId();

            @SuppressWarnings("unchecked")
            List<Message> messages = (List<Message>) serviceManager
                    .handleRequest("T23", "GetOutboxMessages", userId);

            return ResponseEntity.ok(messages != null ? messages : new ArrayList<>());
        } catch (Exception e) {
            logger.error("Errore getOutboxMessages", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }









}
