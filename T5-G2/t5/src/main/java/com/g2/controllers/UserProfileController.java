package com.g2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.components.GenericObjectComponent;
import com.g2.components.PageBuilder;
import com.g2.components.UserProfileComponent;
import com.g2.interfaces.NotificationService;
import com.g2.interfaces.ServiceManager;
import com.g2.model.GameConfigData;
import com.g2.model.Notification;
import com.g2.model.NotificationResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            logger.info("[PostConstruct init] Error in loading game_config.json, using default values: {}", e.getMessage());
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

    /**
     * Gestisce la richiesta GET per la pagina delle notifiche.
     * Costruisce la pagina base 'notification' e la restituisce.
     * Il caricamento effettivo delle notifiche avviene tramite una chiamata AJAX separata
     * gestita dall'endpoint /get_notifications.
     *
     * @param model Il modello a cui aggiungere attributi per la vista.
     * @return Il nome del template della pagina delle notifiche.
     */
    @GetMapping("/Notification")
    public String showProfileNotificationPage(Model model) {
        PageBuilder notificationPage = new PageBuilder(serviceManager, "notification", model, JwtRequestContext.getJwtToken());
        Long userID = notificationPage.getUserId();
        User user = serviceManager.handleRequest("T23","GetUser",User.class,
                String.valueOf(userID));
//        serviceManager.handleRequest("T23", "NewNotification", String.class,
//        user.getEmail(), "Notifica di Test", "Questa è una notifica di test generata automaticamente.");
        serviceManager.handleRequest("Notification", "getNotifications", NotificationResponse.class,
                user.getEmail(), 0, 10);
        return notificationPage.handlePageRequest();
    }

    /*
     * Per testare le notifiche, consigliamo di utilizzare i WebDevTool
     * del vostro browser (che potete aprire premendo F12 sul browser).
     * Con questo tool, potrete inviare richieste di Get/Post/Delete tramite
     * degli script JavaScript
     * */

    /*
    * Get delle notifiche:
    {
        const email = "test@email.com";
        const size = 10;
        const page = 0;
        let res = await fetch("/get_notifications?" + new URLSearchParams({
            email : email,
            size : size,
            page : page
        }), { method: 'GET' });
        res.status + " " + await res.text();
    }
    * */
    /**
     * Endpoint per recuperare le notifiche in formato JSON.
     * Viene chiamato tramite AJAX dallo script nella pagina 'notification.html'.
     *
     * @param email L'email dell'utente per cui recuperare le notifiche.
     * @param page  Il numero di pagina (per la paginazione).
     * @param size  La dimensione della pagina.
     * @return Un ResponseEntity contenente un NotificationResponse con le notifiche o un errore.
     */
    @GetMapping("/get_notifications")
    @ResponseBody
    public ResponseEntity<NotificationResponse> getNotifications(@RequestParam("email") String email,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            // Chiama il ServiceManager per inoltrare la richiesta al T23Service, che a sua volta usa RabbitMQ.
            NotificationResponse response = serviceManager.handleRequest("Notification", "getNotifications", NotificationResponse.class, email, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    * Invio della notifica:
    {
        const email = "test@email.com";
        const subject = "Oggetto Test";
        const message = "Messaggio";
        // "await" aspetta che la richiesta finisca
        let res = await fetch("/notification/new?" + new URLSearchParams({
            email: email,
            subject: subject,
            message: message
        }), { method: 'POST' });
        res.status + " " + await res.text();
    }
    */
    /**
     * Endpoint per inviare una nuova notifica.
     *
     * @param email   L'email del destinatario.
     * @param subject Il titolo della notifica.
     * @param message Il corpo del messaggio.
     * @return Una conferma dell'invio della richiesta.
     */
    @PostMapping("/notification/new")
    @ResponseBody
    public ResponseEntity<String> sendNotification(@RequestParam("email") String email, @RequestParam("subject") String subject, @RequestParam("message") String message) {
        try {
            logger.info("Received new notification request for user: {}", email);
            serviceManager.handleRequest("Notification", "newNotification", String.class, email, subject, message);
            return ResponseEntity.ok("New notification request sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error sending new notification request to RabbitMQ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending new notification request");
        }
    }

    /*
    * Delete della notifica:
    {
        const email = "test@email.com";
        const notificationID = "12345";
        let res = await fetch("/notification/delete?" + new URLSearchParams({
            email: email,
            notificationID: notificationID
        }), { method: 'DELETE' });
        res.status + " " + await res.text();
    }
    */
    /**
     * Endpoint per eliminare una singola notifica.
     *
     * @param email          L'email dell'utente proprietario della notifica.
     * @param notificationID L'ID della notifica da eliminare.
     * @return Una conferma dell'invio della richiesta di eliminazione.
     */
    @DeleteMapping("/notification/delete")
    @ResponseBody
    public ResponseEntity<String> deleteNotification(@RequestParam("email") String email,
                                                     @RequestParam("notificationID") String notificationID) {
        try {
            logger.info("Received delete request for notification ID: {} for user: {}", notificationID, email);
            serviceManager.handleRequest("Notification", "deleteNotification", String.class, email, notificationID);
            return ResponseEntity.ok("Delete request sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error sending delete request to RabbitMQ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending delete request");
        }
    }

    /*
    * Clear delle notifiche di un utente:
    {
        const email = "test@email.com";
        let res = await fetch("/notification/clear?" + new URLSearchParams({
            email: email
        }), { method: 'DELETE' });
        res.status + " " + await res.text();
    }
    */
    /**
     * Endpoint per eliminare tutte le notifiche di un utente.
     *
     * @param email L'email dell'utente.
     * @return Una conferma dell'invio della richiesta di pulizia.
     */
    @DeleteMapping("/notification/clear")
    @ResponseBody
    public ResponseEntity<String> clearAllNotifications(@RequestParam("email") String email) {
        try {
            logger.info("Received clear all notifications request for user: {}", email);
            serviceManager.handleRequest("Notification", "clearNotifications", String.class, email);
            return ResponseEntity.ok("Clear all notifications request sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error sending clear all notifications request to RabbitMQ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending clear all notifications request");
        }
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
