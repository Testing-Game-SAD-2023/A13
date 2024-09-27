package com.g2.Game;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.g2.Interfaces.ServiceManager;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
@CrossOrigin
@RestController
public class GameController {

    //Gestisco qui tutti i giochi aperti 
    private final Map<String, GameLogic> activeGames;
    private final ServiceManager serviceManager;

    public GameController(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
        this.activeGames = new HashMap<>();
    }

    private Map<String, String> GetUserData(String testingClassName, String testingClassCode, String underTestClassName) {
        try {
            //Prendo underTestClassCode dal task T1
            String underTestClassCode = (String) serviceManager.handleRequest("T1", "getClassUnderTest",
                    underTestClassName);

            //Chiato T7 per valutare coverage e userscore
            String response_T7 = (String) serviceManager.handleRequest("T7", "CompileCoverage",
                    testingClassName, testingClassCode, underTestClassName, underTestClassCode);

            System.out.println(response_T7);

            JSONObject responseObj = new JSONObject(response_T7);
            System.out.println(responseObj);

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

    private int GetRobotScore(String testClass, String robot_type, String difficulty) {
        String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati",
                testClass, robot_type, difficulty);
        return Integer.parseInt(response_T4);
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
    public ResponseEntity<String> StartGame(@RequestParam String playerID,
                                            @RequestParam String type_robot,
                                            @RequestParam String difficulty,
                                            @RequestParam String mode,
                                            @RequestParam String underTestClassName) {

        try {
            GameLogic gameLogic = activeGames.get(playerID);
            if (gameLogic == null) {
                //Creo la nuova partita 
                gameLogic = new TurnBasedGameLogic(this.serviceManager, playerID, underTestClassName, type_robot, difficulty);
                activeGames.put(playerID, gameLogic);
                return ResponseEntity.ok().build();
            } else {
                return createErrorResponse("[/StartGame] errore esiste già la partita");
            }
        } catch (Exception e) {
            return createErrorResponse("[/StartGame]" + e.getMessage());
        }
    }

    @GetMapping("/StartGame")
    public Map<String, GameLogic> GetGame() {
        return activeGames;
    }

    @PostMapping(value = "/run", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> Runner(@RequestParam("testingClassCode") String testingClassCode,
                                         @RequestParam("playerId") String playerId,
                                         @RequestParam("isGameEnd") Boolean isGameEnd) {

        try {
            //String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            //retrive gioco attivo
            GameLogic gameLogic = activeGames.get(playerId);
            if (gameLogic == null) {
                return createErrorResponse("[/Run] errore non esiste partita");
            }
            //Preparazione dati per i task 
            String testingClassName = "Test" + gameLogic.getClasseUT() + ".java";
            String underTestClassName = gameLogic.getClasseUT() + ".java";

            //Calcolo dati utente
            Map<String, String> UserData = GetUserData(testingClassName, testingClassCode, gameLogic.getClasseUT());
            //Calcolo punteggio robot
            int RobotScore = GetRobotScore(underTestClassName, gameLogic.getType_robot(), gameLogic.getDifficulty());
            //aggiorno il turno
            int LineCov = LineCoverage(UserData.get("coverage"));
            int user_score = gameLogic.GetScore(LineCov);

            gameLogic.playTurn(user_score, RobotScore);

            if (isGameEnd) {
                //Qua partita finita quindi lo segnalo
                gameLogic.EndRound(playerId);
                gameLogic.EndGame(playerId, user_score, user_score > RobotScore);
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
