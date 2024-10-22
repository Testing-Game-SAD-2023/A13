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

import com.commons.model.Gamemode;
import com.g2.Service.AchievementService;
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

import com.g2.Interfaces.ServiceManager;

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
        this.activeGames = new HashMap<>();
        this.gameRegistry = new HashMap<>();
        registerGames();
    }

    @FunctionalInterface
    interface GameFactoryFunction {
        GameLogic create(ServiceManager serviceManager, 
                            String playerId, String underTestClassName, 
                            String type_robot, String difficulty);
    }

    /*
    *  Registra i tipi di giochi con il loro costruttore, in questo modo non ci si deve preoccupare di instanziarli, viene
    *  fatto in automatico.
    */ 
    private void registerGames() {
        //Attenzione le chiavi sono CaseSensitive
        gameRegistry.put(Gamemode.Sfida.toString(), (sm, playerId, underTestClassName, type_robot, difficulty) ->
            new TurnBasedGameLogic(sm, playerId, underTestClassName, type_robot, difficulty, Gamemode.Sfida.toString()));
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

            JSONObject responseObj = new JSONObject(response_T7);
            String xml_string = responseObj.getString("coverage");
            String outCompile = responseObj.getString("outCompile");

            if (xml_string == null || xml_string.isEmpty()) {
                logger.error("[GAMECONTROLLER] GetUserData: Valore 'coverage' non valido.");
            }

            if (outCompile == null || outCompile.isEmpty()) {
                logger.error("[GAMECONTROLLER] GetUserData: Valore 'outCompile' non valido.");
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

    /*
     *  Chiamata che l'editor fa appena instanzia un nuovo gioco, controllo se la partita quindi esisteva già o meno
     */
    @PostMapping("/StartGame")
    public ResponseEntity<String> StartGame(@RequestParam String playerId,
                                            @RequestParam String type_robot,
                                            @RequestParam String difficulty,
                                            @RequestParam String mode,
                                            @RequestParam String underTestClassName) {

        try {
            GameFactoryFunction gameConstructor = gameRegistry.get(mode);
            if (gameConstructor == null){
                logger.error("[GAMECONTROLLER] /StartGame errore modalità non esiste/non registrata");
                return createErrorResponse("[/StartGame] errore modalità non esiste/non registrata");
            }
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                //Creo la nuova partita 
                gameLogic = gameConstructor.create(this.serviceManager, playerId, underTestClassName, type_robot, difficulty);
                gameLogic.CreateGame();
                activeGames.put(playerId, gameLogic);
                logger.info("[GAMECONTROLLER] /StartGame Partita creata con successo.");
                return ResponseEntity.ok().build();
            } else {
                //La partita già esiste 
                logger.error("[GAMECONTROLLER] /StartGame errore esiste già la partita");
                return createErrorResponse("[/StartGame] errore esiste già la partita");
            }
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] /StartGame errore: ", e);
            return createErrorResponse("[/StartGame]" + e.getMessage());
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
    public ResponseEntity<String> Runner(@RequestParam("testingClassCode") String testingClassCode,
            @RequestParam("playerId") String playerId,
            @RequestParam("isGameEnd") Boolean isGameEnd) {

        try {
            //retrive gioco attivo
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                logger.error("[GAMECONTROLLER] /run: errore non esiste partita");
                return createErrorResponse("[/RUN] errore non esiste partita");
            }

            //Preparazione dati per i task, hanno bisogno di questi nomi in modo preciso e non li calcolano internamente
            String testingClassName = "Test" + gameLogic.getClasseUT() + ".java";
            String underTestClassName = gameLogic.getClasseUT() + ".java";

            logger.info("[GAMECONTROLLER] /run: {}", testingClassName);
            logger.info("[GAMECONTROLLER] /run: {}", underTestClassName);

            //Calcolo dati utente
            Map<String, String> UserData = GetUserData(testingClassName, testingClassCode, gameLogic.getClasseUT(), underTestClassName);
            //Calcolo punteggio robot
            int RobotScore = GetRobotScore(gameLogic.getClasseUT(), gameLogic.getType_robot(), gameLogic.getDifficulty());
            logger.info("[GAMECONTROLLER] /run: RobotScore {}", RobotScore);

            if(UserData.get("coverage") != null && !UserData.get("coverage").isEmpty()){
                //Non ci sono errori di compilazione
                //aggiorno il turno
                int LineCov = LineCoverage(UserData.get("coverage"));
                logger.info("[GAMECONTROLLER] /run: LineCov {}", LineCov);
                //Crea lo score per l'utente in base al gioco
                int user_score = gameLogic.GetScore(LineCov);
                logger.info("[GAMECONTROLLER] /run: user_score {}", user_score);
                //Salvo i dati del turno appena giocato
                gameLogic.playTurn(user_score, RobotScore);

                boolean gameOver = false;

                if (isGameEnd || gameLogic.isGameEnd()) {
                    //Qua partita finita quindi lo segnalo
                    gameLogic.EndRound(playerId);
                    gameLogic.EndGame(playerId, user_score, user_score > RobotScore);
                    activeGames.remove(playerId);
                    logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd true");
                    gameOver = true;
                } else {
                    //Qua partita ancora in corso
                    logger.info("[GAMECONTROLLER] /run: risposta inviata con GameEnd false");
                }

                achievementService.updateProgressByPlayer(Integer.parseInt(playerId));
                return createResponseRun(UserData, RobotScore, user_score, gameOver);
            }else{
                //Ci sono errori di compilazione, invio i dati della console, ma impedisco all'utente di fare submit 
                logger.info("[GAMECONTROLLER] /run: risposta inviata errori di compilazione");
                return createResponseRun(UserData, 0, 0, false);
            }
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] /run: errore non esiste partita", e);
            return createErrorResponse("[/RUN]" + e.getMessage());
        }
    }

    //metodo di supporto per creare la risposta di /run
    private ResponseEntity<String> createResponseRun(Map<String, String> userData, int robotScore, 
    int userScore, boolean gameOver) {
        JSONObject result = new JSONObject();
        result.put("outCompile", userData.get("outCompile"));
        result.put("coverage",   userData.get("coverage"));
        result.put("robotScore", robotScore);
        result.put("userScore",  userScore);
        result.put("GameOver",   gameOver);
        return ResponseEntity
                .status(HttpStatus.OK) // Codice di stato HTTP 200
                .header("Content-Type", "application/json") // Imposta l'intestazione Content-Type
                .body(result.toString());
    }

    // Metodo per creare una risposta di errore di /run
    private ResponseEntity<String> createErrorResponse(String errorMessage) {
        JSONObject error = new JSONObject();
        error.put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
    }

}
