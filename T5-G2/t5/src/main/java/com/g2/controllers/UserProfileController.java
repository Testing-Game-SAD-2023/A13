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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        ResponseTeamComplete team = (ResponseTeamComplete) serviceManager.handleRequest("T1", "OttieniTeamCompleto", teamPage.getUserId());
        if (team != null) {
            @SuppressWarnings("unchecked")
            List<User> membri = (List<User>) serviceManager.handleRequest("T23", "GetUsersByList", team.getTeam().getStudenti());
            model.addAttribute("response", team);
            model.addAttribute("membri", membri);
        }
        return teamPage.handlePageRequest();
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

    @GetMapping("/Notification")
    public String showProfileNotificationPage(Model model) {
        PageBuilder notificationPage = new PageBuilder(serviceManager, "notification", model, JwtRequestContext.getJwtToken());

        return notificationPage.handlePageRequest();
    }

    @GetMapping("/Games")
    public String showGameHistory(Model model) {
        PageBuilder gameHistoryPage = new PageBuilder(serviceManager, "GameHistory", model, JwtRequestContext.getJwtToken());
        return gameHistoryPage.handlePageRequest();
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

    @GetMapping("/edit_profile")
    public String showEditProfile(Model model) {
        PageBuilder editProfilePage = new PageBuilder(serviceManager, "Edit_Profile", model, JwtRequestContext.getJwtToken());
        User user = (User) serviceManager.handleRequest("T23", "GetUser", editProfilePage.getUserId());
        if (user == null) {
            //Qua gestisco utente sbagliato
            return "error";
        }
        // Prendiamo le risorse dal servizio UserProfileService
        List<String> images = getProfilePictures();
        editProfilePage.setObjectComponents(
                new GenericObjectComponent("user", user),
                new GenericObjectComponent("images", images)
        );
        return editProfilePage.handlePageRequest();
    }

}
