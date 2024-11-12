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
package com.g2.Game;

import java.util.HashMap;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.commons.model.Gamemode;
import com.g2.Interfaces.ServiceManager;
import com.g2.Service.AchievementService;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
@CrossOrigin
@RestController
public class GameController {

    //Gestisco qui tutti i giochi aperti 
    private final Map<String, GameLogic> activeGames;
    private final Map<String, GameFactoryFunction> gameRegistry;
    private final ServiceManager serviceManager;

    @Autowired
    private AchievementService achievementService;

    //Logger 
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
        this.activeGames = new ConcurrentHashMap<>();
        this.gameRegistry = new ConcurrentHashMap<>();
        registerGames();
    }

    @FunctionalInterface
    interface GameFactoryFunction {

        GameLogic create(ServiceManager serviceManager,
                String playerId, String underTestClassName,
                String type_robot, String difficulty, String mode);
    }

    /*
    *  Registra i tipi di giochi con il loro costruttore, in questo modo non ci si deve preoccupare di instanziarli, viene
    *  fatto in automatico.
     */
    private void registerGames() {
        //Attenzione le chiavi sono CaseSensitive
        gameRegistry.put(Gamemode.Sfida.toString(), (sm, playerId, underTestClassName, type_robot, difficulty, mode)
                -> new Sfida(sm, playerId, underTestClassName, type_robot, difficulty, Gamemode.Sfida.toString()));
        gameRegistry.put(Gamemode.Allenamento.toString(), (sm, playerId, underTestClassName, type_robot, difficulty, mode)
                -> new Sfida(sm, playerId, underTestClassName, type_robot, difficulty, Gamemode.Allenamento.toString()));
        // Aggiungi altri giochi qui
    }

    /*
     *  Prendo da t1 la classe UT, poi fornisco al T7 tutto ciò di cui ha bisogno per fare una compilazione
     *  infine controllo che tutto sia andato a buon fine e fornisco i dati 
     */
    private Map<String, String> GetUserData(String testingClassName, String testingClassCode, String underTestClassNameNoJava, String underTestClassName) {
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

    /*
     *  Sfrutto T4 per avere i risultati dei robot 
     */
    private int GetRobotScore(String testClass, String robot_type, String difficulty) {
        try {
            String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati",
                    testClass, robot_type, difficulty);

            JSONObject jsonObject = new JSONObject(response_T4);
            //anche se scritto al plurale scores è un solo punteggio, cioè quello del robot
            return jsonObject.getInt("scores");
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] GetRobotScore:", e);
            return 0;
        }
    }

    /*
     *  Partendo dai dati dell'utente ottengo la sua percentuale di coverage 
     */
    public int LineCoverage(String cov) {
        try {
            // Parsing del documento XML con Jsoup
            Document doc = Jsoup.parse(cov, "", Parser.xmlParser());
            // Selezione dell'elemento counter di tipo "LINE"
            Element line = doc.selectFirst("report > counter[type=LINE]");
            // Verifica se l'elemento è stato trovato
            if (line == null) {
                throw new IllegalArgumentException("Elemento 'counter' di tipo 'LINE' non trovato nel documento XML.");
            }
            // Lettura degli attributi "covered" e "missed" e calcolo della percentuale di copertura
            int covered = Integer.parseInt(line.attr("covered"));
            int missed = Integer.parseInt(line.attr("missed"));
            // Calcolo della percentuale di copertura
            return 100 * covered / (covered + missed);
        } catch (NumberFormatException e) {
            logger.error("[GAMECONTROLLER] LineCoverage:", e);
            throw new IllegalArgumentException("Gli attributi 'covered' e 'missed' devono essere numeri interi validi.", e);
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] LineCoverage:", e);
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }

    public int[] getCoverage(String cov, String coverageType) {
        try {
            Document doc = Jsoup.parse(cov, "", Parser.xmlParser());
            // Selezione dell'elemento counter in base al tipo di copertura
            Element counter = doc.selectFirst("report > counter[type=" + coverageType + "]");

            if (counter == null) {
                throw new IllegalArgumentException("Elemento 'counter' di tipo '" + coverageType + "' non trovato nel documento XML.");
            }

            int covered = Integer.parseInt(counter.attr("covered"));
            int missed = Integer.parseInt(counter.attr("missed"));

            // Restituisce i due valori come array: [covered, missed]
            return new int[]{covered, missed};
        } catch (NumberFormatException e) {
            logger.error("[GAMECONTROLLER] getCoverage:", e);
            throw new IllegalArgumentException("Gli attributi 'covered' e 'missed' devono essere numeri interi validi.", e);
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] getCoverage:", e);
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }


    /*
     *  Chiamata che l'editor fa appena instanzia un nuovo gioco, controllo se la partita quindi esisteva già o meno
     *  
     */
    @PostMapping("/StartGame")
    public ResponseEntity<String> StartGame(@RequestParam String playerId,
            @RequestParam String type_robot,
            @RequestParam String difficulty,
            @RequestParam String mode,
            @RequestParam String underTestClassName) {

        try {
            GameFactoryFunction gameConstructor = gameRegistry.get(mode);
            if (gameConstructor == null) {
                logger.error("[GAMECONTROLLER] /StartGame errore modalità non esiste/non registrata");
                return createErrorResponse("[/StartGame] errore modalità non esiste/non registrata", "0");
            }
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                //Creo la nuova partita 
                gameLogic = gameConstructor.create(this.serviceManager, playerId, underTestClassName, type_robot, difficulty, mode);
                //gameLogic.CreateGame();
                activeGames.put(playerId, gameLogic);
                logger.info("[GAMECONTROLLER][StartGame] Partita creata con successo.");
                return ResponseEntity.ok().build();
            }
            // Ottieni la modalità della partita trovata
            String currentMode = gameLogic.getClass().getSimpleName();
            logger.info("[GAMECONTROLLER][StartGame] Partita già esistente modalità: " + currentMode);

            //Condizione logica per vedere se la partita è cambiata
            boolean isGameExisting = currentMode.equals(mode) && gameLogic.CheckGame(type_robot, difficulty, underTestClassName);

            String errorMessage = null;
            String errorCode = null;
            if (isGameExisting) {
                errorMessage = "errore esiste già la partita";
                errorCode = "2";
            } else {
                errorMessage = "errore l'utente ha cambiato le impostazioni della partita";
                errorCode = "1";
                //Rimuovo il vecchio game e ne creo uno nuovo 
                activeGames.remove(playerId);
                gameLogic = gameConstructor.create(this.serviceManager, playerId, underTestClassName, type_robot, difficulty, mode);
                activeGames.put(playerId, gameLogic);
            }
            //Setto messaggio d'errore e codice di conseguenza 
            logger.error("[GAMECONTROLLER][StartGame] " + errorMessage);
            return createErrorResponse(errorMessage, errorCode);
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] [StartGame] errore: ", e);
            return createErrorResponse("[StartGame]" + e.getMessage(), "3");
        }
    }

    /*
     *  chiamata Rest di debug, serve solo per vedere le partite attive 
     */
    @GetMapping("/StartGame")
    public Map<String, GameLogic> GetGame() {
        return activeGames;
    }

    /*
     *  Chiamata principale del game engine, l'utente ogni volta può comunicare la sua richiesta di 
     *  calcolare la coverage/compilazione, il campo isGameEnd è da utilizzato per indicare se è anche un submit e 
     *  quindi vuole terminare la partita ed ottenere i risultati del robot
     */
    @PostMapping(value = "/run", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> Runner(@RequestParam(value = "testingClassCode", required = false, defaultValue = "") String testingClassCode,
            @RequestParam("playerId") String playerId,
            @RequestParam("isGameEnd") Boolean isGameEnd,
            @RequestParam(value = "eliminaGame", required = false, defaultValue = "false") Boolean eliminaGame) {

        try {
            // Recupero gioco attivo
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                logger.error("[GAMECONTROLLER] /run: errore non esiste partita");
                return createErrorResponse("[/RUN] errore non esiste partita", "4");
            }

            // Se il flag eliminaGame è true, elimina il gioco e restituisci la risposta
            if (eliminaGame) {
                return eliminaGame(playerId);
            }

            // Preparazione dati per i task
            String testingClassName = "Test" + gameLogic.getClasseUT() + ".java";
            String underTestClassName = gameLogic.getClasseUT() + ".java";

            logger.info("[GAMECONTROLLER] /run: {}", testingClassName);
            logger.info("[GAMECONTROLLER] /run: {}", underTestClassName);

            // Calcolo dati utente
            Map<String, String> userData = GetUserData(testingClassName, testingClassCode, gameLogic.getClasseUT(), underTestClassName);
            // Calcolo punteggio robot
            int robotScore = GetRobotScore(gameLogic.getClasseUT(), gameLogic.getType_robot(), gameLogic.getDifficulty());
            logger.info("[GAMECONTROLLER] /run: RobotScore {}", robotScore);

            // Gestione copertura di linea e punteggio utente
            return gestisciPartita(userData, gameLogic, isGameEnd, robotScore, playerId);
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] /run: errore", e);
            return createErrorResponse("[/RUN] " + e.getMessage(), "2");
        }
    }

    private ResponseEntity<String> eliminaGame(String playerId) {
        activeGames.remove(playerId);
        logger.info("[GAMECONTROLLER] /run: partita eliminata con successo");
        return createErrorResponse("[/RUN] partita eliminata", "5");
    }

    private ResponseEntity<String> gestisciPartita(Map<String, String> userData, GameLogic gameLogic, Boolean isGameEnd, int robotScore, String playerId) {
        if (userData.get("coverage") != null && !userData.get("coverage").isEmpty()) {
            // Calcolo copertura e punteggio utente
            // il primo è covered e il secondo è missed
            int[] lineCoverage = getCoverage(userData.get("coverage"), "LINE");
            int[] branchCoverage = getCoverage(userData.get("coverage"), "BRANCH");
            int[] instructionCoverage = getCoverage(userData.get("coverage"), "INSTRUCTION");

            int lineCov = LineCoverage(userData.get("coverage"));
            logger.info("[GAMECONTROLLER] /run: LineCov {}", lineCov);

            int userScore = gameLogic.GetScore(lineCov);
            logger.info("[GAMECONTROLLER] /run: user_score {}", userScore);

            // Salvo i dati del turno
            gameLogic.playTurn(userScore, robotScore);

            // Controllo fine partita
            if (isGameEnd || gameLogic.isGameEnd()) {
                activeGames.remove(playerId);
                logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd true");
                return createResponseRun(userData, robotScore, userScore, true, lineCoverage, branchCoverage, instructionCoverage);
            } else {
                logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd false");
                return createResponseRun(userData, robotScore, userScore, false, lineCoverage, branchCoverage, instructionCoverage);
            }
        } else {
            // Errori di compilazione
            logger.info("[GAMECONTROLLER] /run: risposta inviata errori di compilazione");
            return createResponseRun(userData, 0, 0, false, null, null, null);
        }
    }

    //metodo di supporto per creare la risposta
    private ResponseEntity<String> createResponseRun(
            Map<String, String> userData, int robotScore,
            int userScore, boolean gameOver,
            int[] lineCoverageValues,
            int[] branchCoverageValues,
            int[] instructionCoverageValues) {

        // Valori di default per le coperture se gli array sono nulli
        if (lineCoverageValues == null) {
            lineCoverageValues = new int[]{0, 0}; // Default: 0 coperti, 0 non coperti
        }
        if (branchCoverageValues == null) {
            branchCoverageValues = new int[]{0, 0}; // Default: 0 coperti, 0 non coperti
        }
        if (instructionCoverageValues == null) {
            instructionCoverageValues = new int[]{0, 0}; // Default: 0 coperti, 0 non coperti
        }

        JSONObject result = new JSONObject();
        result.put("outCompile", userData.get("outCompile"));
        result.put("coverage", userData.get("coverage"));
        result.put("robotScore", robotScore);
        result.put("userScore", userScore);
        result.put("GameOver", gameOver);
        // Aggiungi i valori di copertura (covered, missed) per Line, Branch, Instruction
        JSONObject coverageDetails = new JSONObject();
        coverageDetails.put("line", new JSONObject()
                .put("covered", lineCoverageValues[0])
                .put("missed", lineCoverageValues[1]));
        coverageDetails.put("branch", new JSONObject()
                .put("covered", branchCoverageValues[0])
                .put("missed", branchCoverageValues[1]));
        coverageDetails.put("instruction", new JSONObject()
                .put("covered", instructionCoverageValues[0])
                .put("missed", instructionCoverageValues[1]));
        // Aggiungi l'oggetto di copertura al risultato finale
        result.put("coverageDetails", coverageDetails);

        return ResponseEntity
                .status(HttpStatus.OK) // Codice di stato HTTP 200
                .header("Content-Type", "application/json") // Imposta l'intestazione Content-Type
                .body(result.toString());
    }

    // Metodo per creare una risposta di errore 
    /*
     * ERROR CODE che mando al client 
     *  0 - modalità non esiste
     *  1 -  l'utente ha cambiato le impostazioni della partita
     *  2 -  esiste già la partita
     *  3 -  è avvenuta un eccezione 
     *  4 -  non esiste la partita
     *  5 -  partita eliminata
     */
    private ResponseEntity<String> createErrorResponse(String errorMessage, String errorCode) {
        JSONObject error = new JSONObject();
        error.put("error", errorMessage);
        error.put("errorCode", errorCode);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
    }

}
