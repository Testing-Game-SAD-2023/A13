package com.g2.Controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Components.LeaderboardComponent;
import com.g2.Model.*;
import com.g2.Model.DTO.*;
import com.g2.security.JwtRequestContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.g2.Components.GenericObjectComponent;
import com.g2.Components.PageBuilder;
import com.g2.Components.UserProfileComponent;
import com.g2.Interfaces.ServiceManager;


/*
 * Tutte le chiamate legate al profilo utente 
 */
@CrossOrigin
@Controller
public class UserProfileController {

    private final ServiceManager serviceManager;
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private GameConfigData gameConfigData = null;

    @Autowired
    public UserProfileController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @PostConstruct
    public void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("game_config.json");
            this.gameConfigData = objectMapper.readValue(file, GameConfigData.class);
        } catch (IOException e) {
            System.out.println("[PostConstruct init] Error in loading game_config.json, using default values.");
            this.gameConfigData = new GameConfigData(10, 5, 1);
        }
    }

    @GetMapping("/SearchFriend")
    public String search_page(Model model){
        PageBuilder search_page = new PageBuilder(serviceManager, "search", model, JwtRequestContext.getJwtToken());
        // search_page.SetAuth();  // Gestisce l'autenticazione
        return search_page.handlePageRequest();
    }

    @GetMapping("/profile")
    public String profilePagePersonal(Model model) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());
        // profile.SetAuth();  // Gestisce l'autenticazione
        Long userId = profile.getUserId();
        profile.setObjectComponents(new UserProfileComponent(serviceManager,false, userId));
        return profile.handlePageRequest();
    }

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable("playerID") Long playerID) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());
        // profile.SetAuth();  // Gestisce l'autenticazione

        Long userId = profile.getUserId();
        if(userId.equals(playerID)){
            return "redirect:/profile";
        }

        profile.setObjectComponents(
            new UserProfileComponent(serviceManager, true, userId, playerID)
        );
        return profile.handlePageRequest();
    }

    @GetMapping("/Team")
    public String ProfileTeamPage(Model model) {
        PageBuilder TeamPage = new PageBuilder(serviceManager, "Team", model, JwtRequestContext.getJwtToken());
        // TeamPage.SetAuth();

        ResponseTeamComplete team = (ResponseTeamComplete) serviceManager.handleRequest("T1", "OttieniTeamCompleto", TeamPage.getUserId());
        if(team != null){
            @SuppressWarnings("unchecked")
            List<User> membri = (List<User>) serviceManager.handleRequest("T23", "GetUsersByList", team.getTeam().getStudenti());
            model.addAttribute("response", team);
            model.addAttribute("membri", membri);
        }
        return TeamPage.handlePageRequest();
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

//    added by GaetanoM
//    Handler per la costruzione della pagine contenente la classifica
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
    public String ProfileNotificationPage(Model model) {
        PageBuilder notification = new PageBuilder(serviceManager, "notification", model, JwtRequestContext.getJwtToken());
        // notification.SetAuth();
        return "notification";
    }


    @GetMapping("/Games")
    public String profile_game(Model model){
        PageBuilder Games = new PageBuilder(serviceManager, "GameHistory", model, JwtRequestContext.getJwtToken());
        // Games.SetAuth();
        return "GameHistory";
    }
    /*
     *    TENERE QUESTA CHIAMATA SOLO PER DEBUG DA DISATTIVARE
     *
     */
    @GetMapping("/profile/{playerID}")
    public String profilePage(Model model,
            @PathVariable(value = "playerID") Long playerID) {

        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, JwtRequestContext.getJwtToken());
        // profile.SetAuth(null);
        profile.setObjectComponents(
                new UserProfileComponent(serviceManager, false, playerID)
        );
        return profile.handlePageRequest();
    }

    /*
         * Andrebbe gestito che ogni uno può mettere la foto che vuole con i tipi Blob nel DB
     */
    private List<String> getProfilePictures() {
        List<String> list_images = new ArrayList<>();
        list_images.add("default.png");
        list_images.add("men-1.png");
        list_images.add("men-2.png");
        list_images.add("men-3.png");
        list_images.add("men-4.png");
        list_images.add("women-1.png");
        list_images.add("women-2.png");
        list_images.add("women-3.png");
        list_images.add("women-4.png");
        return list_images;
    }

    @GetMapping("/edit_profile")
    public String aut_edit_profile(Model model) {
        PageBuilder Edit_Profile = new PageBuilder(serviceManager, "Edit_Profile", model, JwtRequestContext.getJwtToken());
        // Edit_Profile.SetAuth();
        User user = (User) serviceManager.handleRequest("T23", "GetUser", Edit_Profile.getUserId());
        if (user == null) {
            //Qua gestisco utente sbagliato
            return "error";
        }
        // Prendiamo le risorse dal servizio UserProfileService
        List<String> list_images = getProfilePictures();
        Edit_Profile.setObjectComponents(
                new GenericObjectComponent("user", user),
                new GenericObjectComponent("images", list_images)
        );
        return Edit_Profile.handlePageRequest();
    }

}
