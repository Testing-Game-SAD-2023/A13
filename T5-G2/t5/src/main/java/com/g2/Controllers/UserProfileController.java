package com.g2.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.g2.Components.GenericObjectComponent;
import com.g2.Components.PageBuilder;
import com.g2.Components.UserProfileComponent;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.AchievementProgress;
import com.g2.Model.DTO.ResponseTeamComplete;
import com.g2.Model.Statistic;
import com.g2.Model.StatisticProgress;
import com.g2.Model.User;
import com.g2.Service.AchievementService;


/*
 * Tutte le chiamate legate al profilo utente 
 */
@CrossOrigin
@Controller
public class UserProfileController {

    private final ServiceManager serviceManager;
    private AchievementService achievementService;

    @Autowired
    public UserProfileController(ServiceManager serviceManager, AchievementService achievementService) {
        this.serviceManager = serviceManager;
        this.achievementService = achievementService;
    }

    @GetMapping("/SearchFriend")
    public String search_page(Model model, @CookieValue(name = "jwt", required = false) String jwt){
        PageBuilder search_page = new PageBuilder(serviceManager, "search", model, jwt);
        search_page.SetAuth();  // Gestisce l'autenticazione
        return search_page.handlePageRequest();
    }

    @GetMapping("/profile")
    public String profilePagePersonal(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, jwt);
        profile.SetAuth();  // Gestisce l'autenticazione
        String userId = profile.getUserId();
        profile.setObjectComponents(new UserProfileComponent(serviceManager,false, userId));
        return profile.handlePageRequest();
    }

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable("playerID") String playerID, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, jwt);
        profile.SetAuth();  // Gestisce l'autenticazione
        
        String userId = profile.getUserId();
        if(userId.equals(playerID)){
            return "redirect:/profile";
        }

        profile.setObjectComponents(
            new UserProfileComponent(serviceManager, true, userId, playerID)
        );    
        return profile.handlePageRequest();      
    }

    @GetMapping("/Team")
    public String ProfileTeamPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder TeamPage = new PageBuilder(serviceManager, "Team", model, jwt);
        TeamPage.SetAuth();

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
    public String Profileachievement(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder achievement = new PageBuilder(serviceManager, "Achivement", model, jwt);
        achievement.SetAuth();
        int playerID_int = Integer.parseInt(achievement.getUserId());
        //Ottengo lista di Achievement
        //Qui probabilmente ci sono delle inefficienze, andare a vedere achievementService
        List<AchievementProgress> achievementProgresses = achievementService.getProgressesByPlayer(playerID_int);
        List<StatisticProgress> statisticProgresses = achievementService.getStatisticsByPlayer(playerID_int);
        Map<String, Statistic> IdToStatistic = achievementService.GetIdToStatistic();

        achievement.setObjectComponents(
                new GenericObjectComponent("unlockedAchievements", achievementService.getUnlockedAchievementProgress(achievementProgresses)),
                new GenericObjectComponent("lockedAchievements", achievementService.getLockedAchievementProgress(achievementProgresses)),
                new GenericObjectComponent("statisticProgresses", statisticProgresses),
                new GenericObjectComponent("IdToStatistic", IdToStatistic)
        );

        return achievement.handlePageRequest();
    }

    @GetMapping("/Notification")
    public String ProfileNotificationPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder notification = new PageBuilder(serviceManager, "notification", model, jwt);
        notification.SetAuth();
        return "notification";
    }

    @GetMapping("/Games")
    public String profile_game(Model model, @CookieValue(name = "jwt", required = false) String jwt){
        PageBuilder Games = new PageBuilder(serviceManager, "GameHistory", model, jwt);
        Games.SetAuth();
        return "GameHistory"; 
    }
    /*
     *    TENERE QUESTA CHIAMATA SOLO PER DEBUG DA DISATTIVARE 
     * 
     */
    @GetMapping("/profile/{playerID}")
    public String profilePage(Model model,
            @PathVariable(value = "playerID") String playerID,
            @CookieValue(name = "jwt", required = false) String jwt) {

        PageBuilder profile = new PageBuilder(serviceManager, "profile", model);
        profile.SetAuth(jwt);
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
    public String aut_edit_profile(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder Edit_Profile = new PageBuilder(serviceManager, "Edit_Profile", model, jwt);
        Edit_Profile.SetAuth();
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
