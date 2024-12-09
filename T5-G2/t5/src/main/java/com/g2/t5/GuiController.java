/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.t5;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Components.GenericObjectComponent;
import com.g2.Components.PageBuilder;
import com.g2.Components.ServiceObjectComponent;
import com.g2.Components.VariableValidationLogicComponent;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.AchievementProgress;
import com.g2.Model.ClassUT;
import com.g2.Model.Game;
import com.g2.Model.Notification;
import com.g2.Model.ScalataGiocata;
import com.g2.Model.Statistic;
import com.g2.Model.StatisticProgress;
import com.g2.Model.User;
import com.g2.Service.AchievementService;
import com.g2.Service.UserProfileService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@Controller
public class GuiController {

    private final ServiceManager serviceManager;
    private final LocaleResolver localeResolver;

    @Autowired
    private AchievementService achievementService;
    @Autowired
    private UserProfileService userProfileService;

    public GuiController(RestTemplate restTemplate, LocaleResolver localeResolver) {
        this.serviceManager = new ServiceManager(restTemplate);
        this.localeResolver = localeResolver;
    }

    //Gestione lingua
    @PostMapping("/changeLanguage")
    public ResponseEntity<Void> changeLanguage(@RequestParam("lang") String lang,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(3600); // Imposta la durata del cookie a 1 ora
        cookie.setPath("/"); // Imposta il percorso per il cookie
        response.addCookie(cookie); // Aggiungi il cookie alla risposta

        Locale locale = new Locale(lang);
        localeResolver.setLocale(request, response, locale);
        // Restituisce una risposta vuota con codice di stato 200 OK
        return ResponseEntity.ok().build();
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, "main", model);
        main.SetAuth(jwt); //con questo metodo abilito l'autenticazione dell'utente
        return main.handlePageRequest();
    }

    // Ricevuta chiamata a profile, effettuo l'autenticazione
    @GetMapping("/profile")
    public String profilePagePersonal(Model model, @CookieValue(name = "jwt", required = false) String jwt)
    {
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        try {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(decodedUserJson, Map.class);
            String userId = map.get("userId").toString(); //Identifico l'utente
            //Passo l'holder del modello, l'id dell'utente e il token alla pagina effettiva
            return profilePage(model, userId, jwt);
        }
        catch (Exception e) {
            System.out.println("(/profile) Error requesting profile: " + e.getMessage());
        }

        return "error";
    }

    //Definisco la mia pagina utente
    @GetMapping("/profile/{playerID}")
    public String profilePage(Model model,
                              @PathVariable(value="playerID") String playerID,
                              @CookieValue(name = "jwt", required = false) String jwt) {
        //Mi sto istanziando il profilo come PageBuilder
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model);
        //Do l'autenticazione
        profile.SetAuth(jwt);

        //Mi prendo l'id del giocatore, così da filtrare per il suo id i suoi progressi degli achievement e le sue statistiche
        int userId = Integer.parseInt(playerID);

        // PROVARE A RENDERE REALE IL PASSAGGIO DEI DATI ALLA PAGINA PROFILO
        //UserProfile profileDTO=new UserProfile(userId,"Inserisci qui la tua bio...","sample_propic.jpg",null,null);
        // Mi prendo prima tutti gli utenti e poi l'utente che mi interessa con l'id con un filtraggio
        List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));

        // Mi prendo i suoi dati da passare alla pagina
        String email = user.getEmail();
        String studies = user.getStudies();
        String username = user.getName();
        String surname = user.getSurname();

        // Mi prendo immagine e bio
        String image = userProfileService.getProfilePicture(userId);
        String bio = userProfileService.getProfileBio(userId);

        // Mi prendo le notifiche
        List<Notification> notifications = (List<Notification>) serviceManager.handleRequest("T23", "getNotifications", email);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Notification notification : notifications) {
            String formattedate = notification.getTimestamp().format(formatter).toString();
            model.addAttribute("formattedDate", formattedate);
            System.out.println("Notification ID: " + notification.getId() + ", isRead: " + notification.getIsRead());
        }

        // Mi prendo i progressi degli achievement
        List<AchievementProgress> achievementProgresses = achievementService.getProgressesByPlayer(userId);

        // Divido i progressi degli achievement in "unlocked" e "locked"
        List<AchievementProgress> unlockedAchievements = achievementProgresses.stream().filter(a -> a.getProgress() >= a.getProgressRequired()).toList();
        List<AchievementProgress> lockedAchievements = achievementProgresses.stream().filter(a -> a.getProgress() < a.getProgressRequired()).toList();

        // Mi prendo le statistiche del giocatore
        List<StatisticProgress> statisticProgresses = achievementService.getStatisticsByPlayer(userId);
        List<Statistic> allStatistics = achievementService.getStatistics();
        Map<String, Statistic> IdToStatistic = new HashMap<>();

        for (Statistic stat : allStatistics)
            IdToStatistic.put(stat.getID(), stat);

        // Mi prendo la lista dei follower e dei following
        List<User> followersList = new ArrayList<>();
        List<User> followingList = new ArrayList<>();
        followersList = (List<User>) serviceManager.handleRequest("T23", "getFollowers", String.valueOf(userId));
        followingList = (List<User>) serviceManager.handleRequest("T23", "getFollowing", String.valueOf(userId));

        Integer followersListSize = followersList.size();
        Integer followingListSize = followingList.size();

        //DEBUG
        System.out.println("Following list: " + followingList);
        System.out.println("Followers list: " + followersList);
        System.out.println("Following list size: " + followingListSize);
        System.out.println("Followers list size: " + followersListSize);

        // Creo i componenti per passare i dati alla pagina
        GenericObjectComponent objEmail = new GenericObjectComponent("email", email);
        GenericObjectComponent objStudies = new GenericObjectComponent("studies", studies);
        GenericObjectComponent objUsername = new GenericObjectComponent("username", username);
        GenericObjectComponent objSurname = new GenericObjectComponent("surname", surname);
        GenericObjectComponent objImage = new GenericObjectComponent("propic", image);
        GenericObjectComponent objBio = new GenericObjectComponent("bio", bio);
        GenericObjectComponent objFollowingList = new GenericObjectComponent("followingList", followingList);
        GenericObjectComponent objFollowersList = new GenericObjectComponent("followersList", followersList);
        GenericObjectComponent objFollowingListSize = new GenericObjectComponent("followingListSize", followingListSize);
        GenericObjectComponent objFollowersListSize = new GenericObjectComponent("followersListSize", followersListSize);

        GenericObjectComponent objUnlockedAchievements = new GenericObjectComponent("unlockedAchievements", unlockedAchievements);
        GenericObjectComponent objLockedAchievements = new GenericObjectComponent("lockedAchievements", lockedAchievements);
        GenericObjectComponent objAchievementProgresses = new GenericObjectComponent("achievementProgresses", achievementProgresses);
        GenericObjectComponent objStatisticProgresses = new GenericObjectComponent("statisticProgresses", statisticProgresses);
        GenericObjectComponent objIdToStatistic = new GenericObjectComponent("IdToStatistic", IdToStatistic);
        GenericObjectComponent objUserID = new GenericObjectComponent("userID", userId);

        GenericObjectComponent objNotifications = new GenericObjectComponent("notifications", notifications);

        // Aggiungo i componenti alla pagina
        profile.setObjectComponents(objEmail);
        profile.setObjectComponents(objStudies);
        profile.setObjectComponents(objUsername);
        profile.setObjectComponents(objSurname);
        profile.setObjectComponents(objImage);
        profile.setObjectComponents(objBio);
        profile.setObjectComponents(objFollowingList);
        profile.setObjectComponents(objFollowersList);
        profile.setObjectComponents(objFollowingListSize);
        profile.setObjectComponents(objFollowersListSize);
        profile.setObjectComponents(objUnlockedAchievements);
        profile.setObjectComponents(objLockedAchievements);
        profile.setObjectComponents(objAchievementProgresses);
        profile.setObjectComponents(objStatisticProgresses);
        profile.setObjectComponents(objIdToStatistic);
        profile.setObjectComponents(objUserID);
        profile.setObjectComponents(objNotifications);
        //TODO: Aggiungere componenti missioni

        return profile.handlePageRequest();
    }

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable(value="playerID") String playerID, @CookieValue(name = "jwt", required = false) String jwt) {
        try {
            // Istanzio il profilo come PageBuilder
            PageBuilder profile = new PageBuilder(serviceManager, "friend_profile", model);

            // Autenticazione
            profile.SetAuth(jwt);

            // Converto l'ID del giocatore
            int userId = Integer.parseInt(playerID);

            // Recupero l'utente
            List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
            User user = users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            // Recupero i dati pubblici da visualizzare
            String username = user.getName();
            String surname = user.getSurname();

            // Recupero immagine e bio pubbliche
            String image = userProfileService.getProfilePicture(userId);
            String bio = userProfileService.getProfileBio(userId);

            // Recupero i progressi degli achievement pubblici
            List<AchievementProgress> achievementProgresses = achievementService.getProgressesByPlayer(userId);

            // Divido i progressi degli achievement in "unlocked" e "locked"
            List<AchievementProgress> unlockedAchievements = achievementProgresses.stream()
                .filter(a -> a.getProgress() >= a.getProgressRequired())
                .toList();
            List<AchievementProgress> lockedAchievements = achievementProgresses.stream()
                .filter(a -> a.getProgress() < a.getProgressRequired())
                .toList();

            // Recupero le statistiche pubbliche del giocatore
            List<StatisticProgress> statisticProgresses = achievementService.getStatisticsByPlayer(userId);
            List<Statistic> allStatistics = achievementService.getStatistics();
            Map<String, Statistic> IdToStatistic = new HashMap<>();

            for (Statistic stat : allStatistics) {
                IdToStatistic.put(stat.getID(), stat);
            }

            // Decodifica JWT per ottener l'ID dell'utente autenticato
            byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(decodedUserJson, Map.class);
            String authUserId = map.get("userId").toString();

            //Ottengo il profilo dell'utente autenticato
            User authUser = users.stream()
                .filter(u -> u.getId() == Integer.parseInt(authUserId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            // Ottengo l'ID dello UserProfile dell'utente autenticato
            Integer authUserProfileID = userProfileService.getProfileID(Integer.parseInt(authUserId));
            // Ottengo l'ID del profilo dell'utente visualizzato
            Integer userProfileID = userProfileService.getProfileID(userId);

            // Verifichiamo se l'utente autenticato è tra i follower dell'utente visualizzato
            boolean isFollowing = userProfileService.getFollowersList(userId) != null &&
                            userProfileService.getFollowersList(userId).stream()
                                .anyMatch(f -> f.equals(authUserProfileID));

            System.out.println("Sto cercando le informazioni dell'utente: "+userId);
            System.out.println("Id profilo: "+userProfileID);
            System.out.println("Lista dei follower:"+userProfileService.getFollowersList(userId));
            System.out.println("Lista dei following: "+userProfileService.getFollowingList(userId));
            System.out.println("isFollowing: " + isFollowing);

            // Creo i componenti per passare i dati pubblici alla pagina
            GenericObjectComponent objUsername = new GenericObjectComponent("username", username);
            GenericObjectComponent objSurname = new GenericObjectComponent("surname", surname);
            GenericObjectComponent objImage = new GenericObjectComponent("propic", image);
            GenericObjectComponent objBio = new GenericObjectComponent("bio", bio);

            GenericObjectComponent objUnlockedAchievements = new GenericObjectComponent("unlockedAchievements", unlockedAchievements);
            GenericObjectComponent objLockedAchievements = new GenericObjectComponent("lockedAchievements", lockedAchievements);
            GenericObjectComponent objStatisticProgresses = new GenericObjectComponent("statisticProgresses", statisticProgresses);
            GenericObjectComponent objIdToStatistic = new GenericObjectComponent("IdToStatistic", IdToStatistic);
            GenericObjectComponent objIsFollowing = new GenericObjectComponent("isFollowing", isFollowing);
            GenericObjectComponent objUserId = new GenericObjectComponent("userId", userId); //frienId

            // Aggiungo i componenti alla pagina
            profile.setObjectComponents(objUsername);
            profile.setObjectComponents(objSurname);
            profile.setObjectComponents(objImage);
            profile.setObjectComponents(objBio);
            profile.setObjectComponents(objUnlockedAchievements);
            profile.setObjectComponents(objLockedAchievements);
            profile.setObjectComponents(objStatisticProgresses);
            profile.setObjectComponents(objIdToStatistic);
            profile.setObjectComponents(objIsFollowing);
            profile.setObjectComponents(objUserId);

            return profile.handlePageRequest();
        } catch (NumberFormatException e) {
            System.out.println("(/friend) ID utente non valido: " + e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            System.out.println("(/friend) Utente non trovato: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            System.out.println("(/friend) Errore generico: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/follow/{playerID}")
    @ResponseBody
    public ResponseEntity<?> toggleFollow(@PathVariable(value="playerID") String playerID,
                                          @CookieValue(name = "jwt", required = false) String jwt) {

        try{
            // Converto l'ID del giocatore di cui voglio fare il follow/unfollow
            Integer userId = Integer.parseInt(playerID);

            // Recupero l'utente
            /*
            List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
            User user = users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
                */

            // Decodifica JWT per ottener l'ID dell'utente autenticato
            byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(decodedUserJson, Map.class);
            String authUserIdString = map.get("userId").toString();
            Integer authUserId = Integer.parseInt(authUserIdString);

            // Chiamo il servizio per seguire o smettere di seguire l'utente
            String result = (String) serviceManager.handleRequest("T23", "followUser", userId, authUserId);

            return ResponseEntity.ok(result);

        }catch(Exception e){
            System.out.println("(/follow) Errore generico: " + e.getMessage());
            return ResponseEntity.badRequest().body("Errore generico");
        }
    }

    @GetMapping("/gamemode")
    public String gamemodePage(Model model,
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestParam(value = "mode", required = false) String mode) {

        if("Sfida".equals(mode) || "Allenamento".equals(mode)){
            PageBuilder gamemode = new PageBuilder(serviceManager, "gamemode", model);
            //controllo che sia stata fornita una modalità valida dall'utente
            VariableValidationLogicComponent Valida_classeUT = new VariableValidationLogicComponent(mode);
            Valida_classeUT.setCheckNull();
            List<String> list_mode = Arrays.asList("Sfida", "Allenamento");
            Valida_classeUT.setCheckAllowedValues(list_mode); //Se il request param non è in questa lista è un problema
            ServiceObjectComponent lista_classi = new ServiceObjectComponent(serviceManager, "lista_classi", "T1", "getClasses");
            gamemode.setObjectComponents(lista_classi);
            List<String> list_robot = new ArrayList<>();
            // Aggiungere elementi alla lista
            list_robot.add("Randoop");
            list_robot.add("EvoSuite");
            GenericObjectComponent lista_robot = new GenericObjectComponent("lista_robot", list_robot);
            gamemode.setObjectComponents(lista_robot);
            gamemode.SetAuth(jwt);
            return gamemode.handlePageRequest();
        }
        if("Scalata".equals(mode)){
            PageBuilder gamemode = new PageBuilder(serviceManager, "gamemode_scalata", model);
            gamemode.SetAuth(jwt);
            return gamemode.handlePageRequest();
        }
            return "main";
    }

    @GetMapping("/editor")
    public String editorPage(Model model,
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestParam(value = "ClassUT", required = false) String ClassUT) {

        PageBuilder editor = new PageBuilder(serviceManager, "editor", model);
        VariableValidationLogicComponent Valida_classeUT = new VariableValidationLogicComponent(ClassUT);
        Valida_classeUT.setCheckNull();
        @SuppressWarnings("unchecked")
        List<ClassUT> Lista_classi_UT = (List<com.g2.Model.ClassUT>) serviceManager.handleRequest("T1", "getClasses");
        List<String>  Lista_classi_UT_nomi =  new ArrayList<>();
        for(ClassUT element : Lista_classi_UT){
            Lista_classi_UT_nomi.add(element.getName());
        }

        System.out.println(Lista_classi_UT_nomi);

        Valida_classeUT.setCheckAllowedValues(Lista_classi_UT_nomi); //Se il request param non è in questa lista è un problema
        ServiceObjectComponent ClasseUT = new ServiceObjectComponent(serviceManager, "classeUT","T1", "getClassUnderTest", ClassUT);
        editor.setObjectComponents(ClasseUT);
        editor.SetAuth(jwt);
        editor.setLogicComponents(Valida_classeUT);
        //Se l'utente ha inserito un campo nullo o un valore non consentito vuol dire che non è passato da gamemode
        editor.setErrorPage( "NULL_VARIABLE",  "redirect:/main");
        editor.setErrorPage( "VALUE_NOT_ALLOWED",  "redirect:/main");
        return editor.handlePageRequest();
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder leaderboard = new PageBuilder(serviceManager, "leaderboard", model);
        ServiceObjectComponent lista_utenti = new ServiceObjectComponent(serviceManager, "listaPlayers",
                "T23", "GetUsers");
        leaderboard.setObjectComponents(lista_utenti);
        leaderboard.SetAuth(jwt);
        return leaderboard.handlePageRequest();
    }

    @GetMapping("/edit_profile")
    public String aut_edit_profile(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        try {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(decodedUserJson, Map.class);
            String userId = map.get("userId").toString(); //Identifico l'utente
            //Passo l'holder del modello, l'id dell'utente e il token alla pagina effettiva
            return edit_profile(model, userId, jwt);
        }
        catch (Exception e) {
            System.out.println("(/edit_profile) Error requesting edit_profile: " + e.getMessage());
        }

        return "error";
}

    @GetMapping("/edit_profile/{playerID}")
    public String edit_profile(Model model, @PathVariable(value="playerID") String playerID,@CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, "Edit_Profile", model);

        // Mi prendo l'id del giocatore, così forse carico la foto e la bio che già ci sono
        int userId = Integer.parseInt(playerID);

        // Mi prendo prima tutti gli utenti
        @SuppressWarnings("unchecked")
        List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

        // Mi prendo l'utente che mi interessa con l'id
        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        String email = user.getEmail();
        String name = user.getName();
        String surname = user.getSurname();

        // Prendiamo le risorse dal servizio UserProfileService
        List<String> list_images = userProfileService.getAllProfilePictures();
        String image = userProfileService.getProfilePicture(userId);
        String bio = userProfileService.getProfileBio(userId);

        GenericObjectComponent userObject = new GenericObjectComponent("user", userId);
        GenericObjectComponent surnameObject = new GenericObjectComponent("surname", surname);
        GenericObjectComponent nameObject = new GenericObjectComponent("name", name);
        GenericObjectComponent emailObject = new GenericObjectComponent("email", email);
        GenericObjectComponent imagesObject = new GenericObjectComponent("images", list_images);
        GenericObjectComponent propicObject = new GenericObjectComponent("propic", image);
        GenericObjectComponent bioObject = new GenericObjectComponent("bio", bio);


        main.setObjectComponents(userObject);
        main.setObjectComponents(surnameObject);
        main.setObjectComponents(nameObject);
        main.setObjectComponents(emailObject);
        main.setObjectComponents(imagesObject);
        main.setObjectComponents(propicObject);
        main.setObjectComponents(bioObject);
        main.SetAuth(jwt);
        return main.handlePageRequest();
        }

    @GetMapping("/getUserByEMail")
    public ResponseEntity<User> getUserByEMail(Model model, @RequestParam(value = "email", required = true) String email) {
        User user = (User) serviceManager.handleRequest("T23", "GetUserByEmail", email);
        return ResponseEntity.ok(user);
    }

    // Salvataggio delle modifiche al profilo
    @PostMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestParam("email") String email,
                                                @RequestParam("bio") String bio,
                                                @RequestParam("profilePicturePath") String profilePicturePath){

        System.out.println("Email: " + email);
        System.out.println("Bio: " + bio);
        System.out.println("Profile Picture Path: " + profilePicturePath);
        // Chiamata al servizio T23 per modificare il profilo
        Boolean result = (Boolean) serviceManager.handleRequest("T23", "EditProfile", email, bio, profilePicturePath);

        if(result){
            return ResponseEntity.ok("Profile updated successfully");
        }
        else{
            return ResponseEntity.badRequest().body("Error updating profile");
        }
    }

    @PostMapping("/read-notification")
    public ResponseEntity<String> readNotification(@RequestParam("email") String userEmail,
                                                   @RequestParam("id") String notificationID) {
        String result = (String) serviceManager.handleRequest("T23", "updateNotification", userEmail, notificationID);
        if(result != null){
            return ResponseEntity.ok("Notification read successfully");
        }
        else{
            return ResponseEntity.badRequest().body("Error reading notification");
        }
    }

    @DeleteMapping("/delete-notification")
    public ResponseEntity<String> deleteNotification(@RequestParam("email") String userEmail,
                                                     @RequestParam("id") String notificationID) {
        System.out.println(notificationID);
        System.out.println(userEmail);
        String result = (String) serviceManager.handleRequest("T23", "deleteNotification", userEmail, notificationID);
        if(result != null){
            return ResponseEntity.ok("Notification deleted successfully");
        }
        else{
            return ResponseEntity.badRequest().body("Error deleting notification");
        }
    }

    @GetMapping("/report")
    public String reportPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if (Auth) {
            return "report";
        }
        return "redirect:/login";
    }

    // TODO: Salvataggio della ScalataGiocata
    @PostMapping("/save-scalata")
    public ResponseEntity<String> saveScalata(@RequestParam("playerID") int playerID,
            @RequestParam("scalataName") String scalataName,
            HttpServletRequest request) {
        /*
         * Nella schermata /gamemode_scalata, il player non dovrà far altro che che selezionare una delle
         * "Scalate" presenti nella lista e dunque, le informazioni da elaborare saranno esclusivamente:
         * playerID
         * scalataName, dal quale è possibile risalire a tutte le informazioni relative quella specifica "Scalata"
         */

 /*
        * Verifica dell'autenticità del player controllando che l'header identificato dal: "X-UserID" sia lo stesso
        * associato all'utente identificato da "playerID"
         */
        if (!request.getHeader("X-UserID").equals(String.valueOf(playerID))) {

            System.out.println("(/save-scalata)[T5] Player non autorizzato.");
            return ResponseEntity.badRequest().body("Unauthorized");
        } else {

            // Player autorizzato.
            System.out.println("(/save-scalata)[T5] Player autorizzato.");

            // Recupero della data e dell'ora di inizio associata alla ScalataGiocata
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            String fomattedHour = currentHour.format(formatter);
            System.out.println("(/save-scalata)[T5] Data ed ora di inizio recuperate con successo.");

            // Creazione di un oggetto scalataDataWriter
            ScalataDataWriter scalataDataWriter = new ScalataDataWriter();

            // Creazione di un oggetto ScalataGiocata
            ScalataGiocata scalataGiocata = new ScalataGiocata();

            // Inizializzazione dell'oggetto ScalataGiocata
            scalataGiocata.setPlayerID(playerID);
            scalataGiocata.setScalataName(scalataName);
            scalataGiocata.setCreationDate(currentDate);
            scalataGiocata.setCreationTime(fomattedHour);

            JSONObject ids = scalataDataWriter.saveScalata(scalataGiocata);
            System.out.println("(/save-scalata)[T5] Creazione dell'oggetto scalataDataWriter avvenuta con successo.");

            if (ids == null) {
                return ResponseEntity.badRequest().body("Bad Request");
            }

            return ResponseEntity.ok(ids.toString());
        }
    }

    @PostMapping("/save-data")
    public ResponseEntity<String> saveGame(@RequestParam("playerId") int playerId,
                                            @RequestParam("robot") String robot,
                                            @RequestParam("classe") String classe,
                                            @RequestParam("difficulty") String difficulty,
                                            @RequestParam("gamemode") String gamemode,
                                            @RequestParam("username") String username,
                                            @RequestParam("selectedScalata") Optional<Integer> selectedScalata,
                                            HttpServletRequest request) {

        if (!request.getHeader("X-UserID").equals(String.valueOf(playerId))) {
            return ResponseEntity.badRequest().body("Unauthorized");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime oraCorrente = LocalTime.now();
        String oraFormattata = oraCorrente.format(formatter);

        GameDataWriter gameDataWriter = new GameDataWriter();
        // g.setGameId(gameDataWriter.getGameId());
        Game g = new Game(playerId, gamemode, "nome", difficulty, username);
        // g.setPlayerId(pl);
        // g.setPlayerClass(classe);
        // g.setRobot(robot);
        g.setData_creazione(LocalDate.now());
        g.setOra_creazione(oraFormattata);
        g.setClasse(classe);
        g.setUsername(username);
        // System.out.println(g.getUsername() + " " + g.getGameId());

        System.out.println("ECCO LO USERNAME : " + username);       //in realtà stampa l'indirizzo e-mail del player...

        // globalID = g.getGameId();
        JSONObject ids = gameDataWriter.saveGame(g, username, selectedScalata);
        if (ids == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }

        System.out.println("Checking achievements...");

        //Voglio notificare l'utente dei nuovi achievement

        // Mi prendo prima tutti gli utenti
        @SuppressWarnings("unchecked")
        List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

        // Mi prendo l'utente che mi interessa con l'id
        User user = users.stream().filter(u -> u.getId() == playerId).findFirst().orElse(null);
        String email = user.getEmail();
        List<AchievementProgress> newAchievements = achievementService.updateProgressByPlayer(playerId);
        achievementService.updateNotificationsForAchievements(email,newAchievements);

        return ResponseEntity.ok(ids.toString());
    }

    @GetMapping("/leaderboardScalata")
    public String getLeaderboardScalata(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if (Auth) {
            return "leaderboardScalata";
        }
        return "redirect:/login";
    }

    @GetMapping("/editor_old")
    public String getEditorOld(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, "editor_old", model);
        main.SetAuth(jwt); //con questo metodo abilito l'autenticazione dell'utente
        return main.handlePageRequest();
    }
}
