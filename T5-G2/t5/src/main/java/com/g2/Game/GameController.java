package com.g2.Game;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.g2.Interfaces.ServiceManager;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
@RestController
public class GameController {

    //Gestisco qui tutti i giochi aperti 
    private final Map<String, GameLogic> activeGames;
    private final ServiceManager serviceManager;

    public GameController(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
        this.activeGames = new HashMap<>();
    }

    //Refactor del vecchio GameDataWriter e metodo saveGame, in effetti più che salvarlo lo stiamo creando
    //farne una classe separata con un solo metodo e poi chiamarla nel GUIcontroller era un po' inutile 
    private Map<String, String> CreateGame(String Time,
            String ClasseUT, String player_id, String difficulty, String name, String description, String username) {
        try {
            //questo è l'ordine delle cose, non cambiarlo 
            //Creo un game 
            String game_id = (String) serviceManager.handleRequest("T4", "CreateGame", Time, difficulty, name, description, username);
            //Creo un round  
            String round_id = (String) serviceManager.handleRequest("T4", "CreateRound", game_id, ClasseUT, Time);
            //CReo un turno 
            String turn_id = (String) serviceManager.handleRequest("T4", "CreateTurn", player_id, round_id, Time);

            //incapsulo i 3 id 
            Map<String, String> return_data = new HashMap<>();
            return_data.put("game_id", game_id);
            return_data.put("round_id", round_id);
            return_data.put("turn_id", turn_id);
            return return_data;
        } catch (Exception e) {
            return null;
        }
    }

    //in maniera analoga qui ho fatto refactor del save scalata e della classe ScalataDataWriter
    private String CreateScalata(String player_id, String Scalata_name) {
        try {
            // Recupero della data e dell'ora di inizio associata alla ScalataGiocata
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            String creation_Time = currentHour.format(formatter);
            String creation_date = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            String scalata_id = (String) serviceManager.handleRequest("T4", "CreateScalata",
                    player_id, Scalata_name, creation_Time, creation_date);
            return scalata_id;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> GetUserData(String testingClassName, String testingClassCode, String underTestClassName) {
        try {
            //Prendo underTestClassCode dal task T1
            String underTestClassCode = (String) serviceManager.handleRequest("T1", "getClassUnderTest",
                    underTestClassName);

            //Chiato T7 per valutare coverage e userscore
            String response_T7 = (String) serviceManager.handleRequest("T7", "CompileCoverage",
                    testingClassName, testingClassCode, underTestClassName, underTestClassCode);

            JSONObject responseObj = new JSONObject(response_T7);
            String xml_string = responseObj.getString("coverage");
            String outCompile = responseObj.getString("outCompile");

            if (xml_string == null || xml_string.isEmpty()) {
                System.out.println("Valore 'coverage' non valido.");
            }

            if (outCompile == null || outCompile.isEmpty()) {
                System.out.println("Valore 'outCompile' non valido.");
            }

            Map<String, String> return_data = new HashMap<>();
            return_data.put("coverage", xml_string);
            return_data.put("outCompile", outCompile);
            return return_data;
        } catch (JSONException e) {
            System.out.println("Errore nella lettura del JSON: " + e.getMessage());
            return null;
        }
    }

    private int GetRobotScore(String testClassId, String robot_type, String difficulty) {
        String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati",
                testClassId, robot_type, difficulty);

        //come sopra devo capire come convertire la risposta bene
        //Integer roboScore = Integer.parseInt(response_T4);
        //int numTurnsPlayed = Integer.parseInt(request.getParameter("order"));
        return 10;
    }

    public int LineCoverage(String cov) {
        try {
            // Creazione del DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Creazione del DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parsing della stringa XML in un documento
            Document doc = builder.parse(new ByteArrayInputStream(cov.getBytes()));

            // Ricerca di tutti gli elementi "counter"
            NodeList counters = doc.getElementsByTagName("counter");
            Element line = null;

            // Itera attraverso tutti gli elementi "counter"
            for (int i = 0; i < counters.getLength(); i++) {
                Element counter = (Element) counters.item(i);
                // Controlla se il tipo è "LINE"
                if ("LINE".equals(counter.getAttribute("type"))) {
                    line = counter;
                    break;
                }
            }

            // Se non è stato trovato un elemento di tipo "LINE"
            if (line == null) {
                throw new IllegalArgumentException("L'elemento counter di tipo LINE non è stato trovato nel documento XML.");
            }

            // Lettura degli attributi "covered" e "missed"
            String coveredStr = line.getAttribute("covered");
            String missedStr = line.getAttribute("missed");

            // Conversione degli attributi da String a int
            int covered = Integer.parseInt(coveredStr);
            int missed = Integer.parseInt(missedStr);

            // Calcolo della percentuale di copertura
            return 100 * covered / (covered + missed);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Gli attributi covered e missed devono essere numeri interi validi.", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }

    @PostMapping("/StartGame")
    public ResponseEntity<String> StartGame(@RequestParam("playerID") String playerId,
            @RequestParam("type_robot") String type_robot,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("mode") String mode,
            @RequestParam("underTestClassName") String underTestClassName) {

        try {
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                //Creo la nuova partita 
                gameLogic = new TurnBasedGameLogic(this.serviceManager, playerId, underTestClassName, type_robot, difficulty);
                activeGames.put(playerId, gameLogic);
                return ResponseEntity.ok().build();
            } else {
                return createErrorResponse("[/StartGame] errore esiste già la partita");
            }
        } catch (Exception e) {
            return createErrorResponse("[/StartGame]" + e.getMessage());
        }
    }

    @PostMapping("/run")
    public ResponseEntity<String> Runner(@RequestParam("testingClassName") String testingClassName,
            @RequestParam("testingClassCode") String testingClassCode,
            @RequestParam("testClassId") String testClassId,
            @RequestParam("playerID") String playerId) {
        try {
            String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            //retrive gioco attivo
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                //qua errore 
            }
            //Calcolo dati utente
            Map<String, String> UserData = GetUserData(testingClassName, testingClassCode, gameLogic.getClasseUT());
            //Calcolo punteggio robot
            int RobotScore = GetRobotScore(gameLogic.getClasseUT(), gameLogic.getType_robot(), gameLogic.getDifficulty());
            //aggiorno il turno
            int LineCov = LineCoverage(UserData.get("coverage"));
            int user_score = gameLogic.GetScore(LineCov);
            gameLogic.playTurn(user_score, RobotScore);

            if (gameLogic.isGameEnd()) {
                //Qua partita finita quindi lo segnalo
                return createResponseRun(UserData, RobotScore, user_score, true);
            } else {
                return createResponseRun(UserData, RobotScore, user_score, false);
            }
        } catch (Exception e) {
            return createErrorResponse("[/RUN]" + e.getMessage());
        }
    }

    //metodo di supporto per creare la risposta di /run
    private ResponseEntity<String> createResponseRun(Map<String, String> userData, int robotScore, int userScore, boolean gameOver) {
        JSONObject result = new JSONObject();
        result.put("outCompile", userData.get("outCompile"));
        result.put("coverage", userData.get("coverage"));
        result.put("robotScore", robotScore);
        result.put("userScore", userScore);
        result.put("GameOver", gameOver);
        return ResponseEntity
                .status(HttpStatus.OK) // Codice di stato HTTP 200
                .header("Content-Type", "application/json") // Imposta l'intestazione Content-Type
                .body(result.toString());
    }

    // Metodo per creare una risposta di errore
    private ResponseEntity<String> createErrorResponse(String errorMessage) {
        JSONObject error = new JSONObject();
        error.put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
    }

}
