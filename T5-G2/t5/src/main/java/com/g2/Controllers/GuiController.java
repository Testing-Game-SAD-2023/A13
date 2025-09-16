/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.g2.Controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.g2.Components.GenericObjectComponent;
import com.g2.security.JwtRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.g2.Components.PageBuilder;
import com.g2.Components.ServiceObjectComponent;
import com.g2.Components.VariableValidationLogicComponent;
import com.g2.Interfaces.ServiceManager;
import com.g2.Session.Exceptions.SessionAlredyExist;
import com.g2.Session.Exceptions.SessionDontExist;
import com.g2.Session.SessionService;
import com.g2.Session.Sessione;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import testrobotchallenge.commons.models.opponent.GameMode;

@CrossOrigin
@Controller
public class GuiController {

    private final ServiceManager serviceManager;
    private final LocaleResolver localeResolver;
    private final SessionService sessionService;

    @Autowired
    public GuiController(ServiceManager serviceManager, LocaleResolver localeResolver, SessionService sessionService) {
        this.serviceManager = serviceManager;
        this.localeResolver = localeResolver;
        this.sessionService = sessionService;
    }

    // Gestione della lingua
    @PostMapping("/changeLanguage")
    public ResponseEntity<Void> changeLanguage(@RequestParam("lang") String lang,
            HttpServletRequest request,
            HttpServletResponse response) {
        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(3600); // Imposta la durata del cookie a 1 ora
        cookie.setPath("/"); // Imposta il percorso per il cookie
        response.addCookie(cookie); // Aggiungi il cookie alla risposta
        Locale locale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, locale);
        // Restituisce una risposta vuota con codice di stato 200 OK
        return ResponseEntity.ok().build();
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, "main", model, jwt);
        //main.SetAuth();
        try{
            sessionService.createSession(main.getUserId());
        } catch(SessionAlredyExist e){
            System.out.println("Esiste già una sessione per playerId" + main.getUserId());
        }
        return main.handlePageRequest();
    }

    @GetMapping("/gamemode")
    public String gamemodePage(Model model,
            @RequestParam(value = "mode", required = false) String mode) {

        if ("Sfida".equals(mode) || "Allenamento".equals(mode) || "PartitaSingola".equals(mode)) {
            PageBuilder gamemode = new PageBuilder(serviceManager, "gamemode", model);
            VariableValidationLogicComponent valida = new VariableValidationLogicComponent(mode);
            valida.setCheckNull();
            List<String> list_mode = Arrays.asList("Sfida", "Allenamento");
            valida.setCheckAllowedValues(list_mode);
            ServiceObjectComponent lista_classi = new ServiceObjectComponent(serviceManager, "lista_classi", "T1", "getClasses");
            ServiceObjectComponent availableRobots = new ServiceObjectComponent(serviceManager, "available_robots", "T1", "getOpponentsSummary");
            gamemode.setObjectComponents(lista_classi, availableRobots);
            return gamemode.handlePageRequest();
        }
        if ("Scalata".equals(mode)) {
            PageBuilder gamemode = new PageBuilder(serviceManager, "gamemode_scalata", model);
            return gamemode.handlePageRequest();
        }
        return "main";
    }

    // Mapping per l'editor: l'accesso è consentito solo se nella sessione esiste almeno una modalità;
    // altrimenti, l'utente viene reindirizzato a /main.
    @GetMapping("/editor")
    public String editorPage(Model model,
                            @RequestParam(value = "ClassUT") String ClassUT,
                            @RequestParam(value = "mode") GameMode mode)
    {

        PageBuilder editor = new PageBuilder(serviceManager, "editor", model, JwtRequestContext.getJwtToken());
        //editor.SetAuth();
        /*
        *   Se la sessione contiene almeno una modalità, 
        *    prosegui normalmente con la costruzione 
        *    della pagina editor.
         */
        try {
            Sessione sessione = sessionService.getSession(editor.getUserId());
            System.out.println("loading sessione");
            System.out.println("sessione classUT: " + sessione.getGame(mode).getClasseUT());
            System.out.println("sessione testingClassCode: " + sessione.getGame(mode).getTestingClassCode());
            editor.setObjectComponents(new GenericObjectComponent("previousGameObject", sessione.getGame(mode)));
            /*
             *  se non è settata la modalità che si sta provando a usare
             */
            // if (!sessione.hasModalita(mode)) {
            //    return "redirect:/main";
            // }
        } catch (SessionDontExist e) {
            return "redirect:/main";
        }
        
        ServiceObjectComponent classeUT = new ServiceObjectComponent(serviceManager, "classeUT", "T1", "getClassUnderTest", ClassUT);
        editor.setObjectComponents(classeUT);
        return editor.handlePageRequest();
    }

//    @GetMapping("/leaderboard")
//    public String leaderboard(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
//        PageBuilder leaderboard = new PageBuilder(serviceManager, "leaderboard", model);
//        ServiceObjectComponent listaUtenti = new ServiceObjectComponent(serviceManager, "listaPlayers", "T23", "GetUsers");
//        leaderboard.setObjectComponents(listaUtenti);
//        //leaderboard.SetAuth(jwt);
//        return leaderboard.handlePageRequest();
//    }
    /* 
    @PostMapping("/save-scalata")
    public ResponseEntity<String> saveScalata(@RequestParam("playerID") int playerID,
            @RequestParam("scalataName") String scalataName,
            HttpServletRequest request) {
        if (!request.getHeader("X-UserID").equals(String.valueOf(playerID))) {
            System.out.println("(/save-scalata)[T5] Player non autorizzato.");
            return ResponseEntity.badRequest().body("Unauthorized");
        } else {
            System.out.println("(/save-scalata)[T5] Player autorizzato.");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            String formattedHour = currentHour.format(formatter);
            System.out.println("(/save-scalata)[T5] Data ed ora di inizio recuperate con successo.");
            ScalataDataWriter scalataDataWriter = new ScalataDataWriter();
            ScalataGiocata scalataGiocata = new ScalataGiocata();
            scalataGiocata.setPlayerID(playerID);
            scalataGiocata.setScalataName(scalataName);
            scalataGiocata.setCreationDate(currentDate);
            scalataGiocata.setCreationTime(formattedHour);
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
        Game g = new Game(playerId, gamemode, "nome", difficulty, username);
        g.setData_creazione(LocalDate.now());
        g.setOra_creazione(oraFormattata);
        g.setClasse(classe);
        g.setUsername(username);
        System.out.println("ECCO LO USERNAME : " + username);
        JSONObject ids = gameDataWriter.saveGame(g, username, selectedScalata);
        if (ids == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        System.out.println("Checking achievements...");
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
        User user = users.stream().filter(u -> u.getId() == playerId).findFirst().orElse(null);
        String email = user.getEmail();
        List<AchievementProgress> newAchievements = achievementService.updateProgressByPlayer(playerId);
        achievementService.updateNotificationsForAchievements(email, newAchievements);
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
        main.SetAuth(jwt);
        return main.handlePageRequest();
    }
     */
}
