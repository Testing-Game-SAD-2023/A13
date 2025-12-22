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
package com.g2.controllers;

import com.g2.components.GenericObjectComponent;
import com.g2.components.PageBuilder;
import com.g2.components.ServiceObjectComponent;
import com.g2.components.VariableValidationLogicComponent;
import com.g2.interfaces.ServiceManager;
import com.g2.security.JwtRequestContext;
import com.g2.session.SessionService;
import com.g2.session.Sessione;
import com.g2.session.exception.SessionAlredyExist;
import com.g2.session.exception.SessionDoesntExistException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@Controller
@AllArgsConstructor
public class GuiController {
    private static final Logger logger = LoggerFactory.getLogger(GuiController.class);
    private final ServiceManager serviceManager;
    private final SessionService sessionService;

    @GetMapping("/main")
    public String showMain(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, "main", model, jwt);

        try {
            sessionService.createSession(main.getUserId());
        } catch (SessionAlredyExist e) {
            logger.info("Esiste già una sessione per playerId {}", main.getUserId());
        }
        return main.handlePageRequest();
    }

    @GetMapping("/gamemode")
    public String showGameMode(Model model,
                               @RequestParam(value = "mode", required = false) String mode) {

        if ("Sfida".equals(mode) || "Allenamento".equals(mode) || "PartitaSingola".equals(mode)) {
            PageBuilder gameModePage = new PageBuilder(serviceManager, "gamemode", model);
            VariableValidationLogicComponent valida = new VariableValidationLogicComponent(mode);
            valida.setCheckNull();
            List<String> gameModes = Arrays.asList("Sfida", "Allenamento");
            valida.setCheckAllowedValues(gameModes);
            ServiceObjectComponent availableClasses = new ServiceObjectComponent(serviceManager, "lista_classi", "T1", "getClasses");
            ServiceObjectComponent availableRobots = new ServiceObjectComponent(serviceManager, "available_robots", "T1", "getOpponentsSummary");
            gameModePage.setObjectComponents(availableClasses, availableRobots);
            return gameModePage.handlePageRequest();
        }
        if ("Scalata".equals(mode)) {
            PageBuilder gameModePage = new PageBuilder(serviceManager, "gamemode_scalata", model);
            return gameModePage.handlePageRequest();
        }
        return "main";
    }

    /*
     * Mapping per l'editor: l'accesso è consentito solo se nella sessione esiste almeno una modalità.
     * Altrimenti, l'utente viene reindirizzato a /main.
     */
    @GetMapping("/editor")
    public String editorPage(Model model,
                             @RequestParam(value = "ClassUT") String ClassUT,
                             @RequestParam(value = "mode") GameMode mode) {

        PageBuilder editor = new PageBuilder(serviceManager, "editor", model, JwtRequestContext.getJwtToken());
        /*
         *   Se la sessione contiene almeno una modalità,
         *    prosegui normalmente con la costruzione
         *    della pagina editor.
         */
        try {
            Sessione sessione = sessionService.getSession(editor.getUserId());
            logger.info("loading sessione");
            logger.info("sessione classUT: {}", sessione.getGame(mode).getClassUTName());
            logger.info("sessione testingClassCode: {}", sessione.getGame(mode).getTestingClassCode());
            editor.setObjectComponents(new GenericObjectComponent("previousGameObject", sessione.getGame(mode)));
        } catch (SessionDoesntExistException e) {
            return "redirect:/main";
        }

        ServiceObjectComponent classeUT = new ServiceObjectComponent(serviceManager, "classeUT", "T1", "getClassUnderTest", ClassUT);
        editor.setObjectComponents(classeUT);
        return editor.handlePageRequest();
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder leaderboard = new PageBuilder(serviceManager, "leaderboard", model);
        ServiceObjectComponent listaUtenti = new ServiceObjectComponent(serviceManager, "listaPlayers", "T23", "GetUsers");
        leaderboard.setObjectComponents(listaUtenti);
        return leaderboard.handlePageRequest();
    }

    /* 
    @PostMapping("/save-scalata")
    public ResponseEntity<String> saveScalata(@RequestParam("playerID") int playerID,
            @RequestParam("scalataName") String scalataName,
            HttpServletRequest request) {
        if (!request.getHeader("X-UserID").equals(String.valueOf(playerID))) {
            logger.info("(/save-scalata)[T5] Player non autorizzato.");
            return ResponseEntity.badRequest().body("Unauthorized");
        } else {
            logger.info("(/save-scalata)[T5] Player autorizzato.");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            String formattedHour = currentHour.format(formatter);
            logger.info("(/save-scalata)[T5] Data ed ora di inizio recuperate con successo.");
            ScalataDataWriter scalataDataWriter = new ScalataDataWriter();
            ScalataGiocata scalataGiocata = new ScalataGiocata();
            scalataGiocata.setPlayerID(playerID);
            scalataGiocata.setScalataName(scalataName);
            scalataGiocata.setCreationDate(currentDate);
            scalataGiocata.setCreationTime(formattedHour);
            JSONObject ids = scalataDataWriter.saveScalata(scalataGiocata);
            logger.info("(/save-scalata)[T5] Creazione dell'oggetto scalataDataWriter avvenuta con successo.");
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
        logger.info("ECCO LO USERNAME : " + username);
        JSONObject ids = gameDataWriter.saveGame(g, username, selectedScalata);
        if (ids == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        logger.info("Checking achievements...");
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
