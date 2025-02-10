package com.g2.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.AchievementProgress;
import com.g2.Model.User;
import com.g2.Model.DTO.GameResponseDTO;
import com.g2.Service.AchievementService;

@Service
public class GameService {

    private final Map<String, GameLogic> activeGames;

    private final ServiceManager serviceManager;
    private final AchievementService achievementService;
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    public GameService(ServiceManager serviceManager, AchievementService achievementService) {
        this.serviceManager = serviceManager;
        this.achievementService = achievementService;
        this.activeGames = new ConcurrentHashMap<>();
    }

    // Metodo per recuperare i dati dell'utente (compilazione, copertura)
    public Map<String, String> getUserData(String testingClassName, String testingClassCode, String underTestClassNameNoJava, String underTestClassName) {
        try {
            //Prendo underTestClassCode dal task T1
            String underTestClassCode = (String) serviceManager.handleRequest("T1", "getClassUnderTest",
                    underTestClassNameNoJava);

            logger.info("[GAMECONTROLLER] GetUserData - underTestClassCode: {}", underTestClassCode);

            //Chiato T7 per valutare coverage e userscore
            String response_T7 = (String) serviceManager.handleRequest("T7", "CompileCoverage",
                    testingClassName, testingClassCode, underTestClassName, underTestClassCode);

            //Leggo la risposta da T7
            JSONObject responseObj = new JSONObject(response_T7);
            // Restituisce null se "coverage" non esiste
            String xml_string = responseObj.optString("coverage", null);
            String outCompile = responseObj.optString("outCompile", null);

            if (xml_string == null || xml_string.isEmpty()) {
                logger.error("[GAMECONTROLLER] GetUserData: Valore 'coverage' vuoto/non valido.");
            }

            if (outCompile == null || outCompile.isEmpty()) {
                logger.error("[GAMECONTROLLER] GetUserData: Valore 'outCompile' vuoto/non valido.");
            }

            Map<String, String> return_data = new HashMap<>();
            return_data.put("coverage", xml_string);
            return_data.put("outCompile", outCompile);
            return return_data;
        } catch (JSONException e) {
            logger.error("[GAMECONTROLLER] GetUserData: Errore nella lettura del JSON", e);
            return null;
        }
    }

    // Metodo per ottenere il punteggio del robot
    public int getRobotScore(String testClass, String robotType, String difficulty) {
        try {
            String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati", testClass, robotType, difficulty);
            JSONObject jsonObject = new JSONObject(response_T4);
            //anche se scritto al plurale scores è un solo punteggio, cioè quello del robot
            return jsonObject.getInt("scores");
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] GetRobotScore:", e);
            return 0;
        }
    }

    // Recupero la sessioen di gioco 
    public GameLogic getActiveGame(String playerId) {
        GameLogic gameLogic = activeGames.get(playerId);
        if (gameLogic == null) {
            logger.error("[GAMECONTROLLER] /run: errore non esiste partita");
            //throw new GameNotFoundException("[/RUN] errore non esiste partita");
        }
        return gameLogic;
    }
    
    private ResponseEntity<String> RemoveActiveGame(String playerId) {
        activeGames.remove(playerId);
        logger.info("[GAMECONTROLLER] /run: partita eliminata con successo");
        //return createErrorResponse("[/RUN] partita eliminata", "5");
        return null;
    }

    //Logica di fine partita 
    private ResponseEntity<String> handleGameEnd(GameLogic gameLogic, boolean isGameEnd, String playerId, int userScore, int robotScore, Map<String, String> userData) {
        activeGames.remove(playerId);
        logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd true");
        updateProgressAndNotifications(playerId);
        //return createResponseRun(userData, robotScore, userScore, true);
        return null;
    }


    public ResponseEntity<String> Runner(@RequestParam(value = "testingClassCode", required = false, defaultValue = "") String testingClassCode,
                                        @RequestParam("playerId") String playerId,
                                        @RequestParam("isGameEnd") Boolean isGameEnd,
                                        @RequestParam(value = "eliminaGame", required = false, defaultValue = "false") Boolean eliminaGame) {

        try {
            GameLogic gameLogic = getActiveGame(playerId);
            // Se il flag eliminaGame è true, elimina il gioco e restituisci la risposta
            if (eliminaGame) {
                return RemoveActiveGame(playerId);
            }

            // Preparazione dati per i task
            String testingClassName = "Test" + gameLogic.getClasseUT() + ".java";
            String underTestClassName = gameLogic.getClasseUT() + ".java";

            logger.info("[GAMECONTROLLER] /run: {}", testingClassName);
            logger.info("[GAMECONTROLLER] /run: {}", underTestClassName);

            // Calcolo dati utente
            Map<String, String> userData = getUserData(testingClassName, testingClassCode, gameLogic.getClasseUT(), underTestClassName);
            
            // Calcolo punteggio robot
            int robotScore = getRobotScore(gameLogic.getClasseUT(), gameLogic.getType_robot(), gameLogic.getDifficulty());
            logger.info("[GAMECONTROLLER] /run: RobotScore {}", robotScore);

            // Gestione copertura di linea e punteggio utente
            return gestisciPartita(userData, gameLogic, isGameEnd, robotScore, playerId);
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] /run: errore", e);
            //return createErrorResponse("[/RUN] " + e.getMessage(), "2");
            return null;
        }
    }


    private void updateProgressAndNotifications(String playerId) {
        List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
        Long userId = Long.parseLong(playerId);
        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        String email = user.getEmail();
        List<AchievementProgress> newAchievements = achievementService.updateProgressByPlayer(userId.intValue());
        achievementService.updateNotificationsForAchievements(email, newAchievements);
    }

    private ResponseEntity<String> gestisciPartita(Map<String, String> userData, GameLogic gameLogic, Boolean isGameEnd, int robotScore, String playerId) {
        if (userData.get("coverage") != null && !userData.get("coverage").isEmpty()) {
            //int lineCov = lineCoverage(userData.get("coverage"));
            //logger.info("[GAMECONTROLLER] /run: LineCov {}", lineCov);
    
            // messo 0 ma c'è linecov qui 
            int userScore = gameLogic.GetScore(0);
            logger.info("[GAMECONTROLLER] /run: user_score {}", userScore);
    
            gameLogic.playTurn(userScore, robotScore);
    
            // Controllo fine partita
            if (isGameEnd || gameLogic.isGameEnd()) {
                //return handleGameEnd(gameLogic, playerId, robotScore, userScore);
                return null;
            } else {
                logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd false");
                //return createResponseRun(userData, robotScore, userScore, false, lineCoverage, branchCoverage, instructionCoverage);
                return null;
            }
        } else {
            // Errori di compilazione
            logger.info("[GAMECONTROLLER] /run: risposta inviata errori di compilazione");
            //return createResponseRun(userData, 0, 0, false, null, null, null);
            return null;
        }
    }
    
    private ResponseEntity<String> createResponseRun(
            Map<String, String> userData, int robotScore,
            int userScore, boolean gameOver) throws JsonProcessingException {

        // Creazione del DTO finale con la logica di copertura già integrata
        GameResponseDTO responseDTO = new GameResponseDTO(
                userData.get("outCompile"),
                userData.get("coverage"),
                robotScore,
                userScore,
                gameOver
        );

        // Serializzazione del DTO in JSON
        String responseJson = new ObjectMapper().writeValueAsString(responseDTO);

        return ResponseEntity
                .status(HttpStatus.OK) // Codice di stato HTTP 200
                .header("Content-Type", "application/json") // Imposta l'intestazione Content-Type
                .body(responseJson);
    }

}
