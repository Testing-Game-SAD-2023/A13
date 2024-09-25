package com.g2.Game;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceManager;


//Qui introduco tutte le chiamate REST per la logica di gioco/editor
public class GameController {
    //Gestisco qui tutti i giochi aperti 
    private final Map<String, GameLogic> activeGames;
    private final ServiceManager serviceManager; 

    public GameController(RestTemplate restTemplate){
        this.serviceManager = new ServiceManager(restTemplate);
        this.activeGames = new HashMap<>();
    }

    //Refactor del vecchio GameDataWriter e metodo saveGame, in effetti più che salvarlo lo stiamo creando
    //farne una classe separata con un solo metodo e poi chiamarla nel GUIcontroller era un po' inutile 
    private Map<String, String> CreateGame(String Time, 
                String ClasseUT, String player_id, String difficulty, String name, String description, String username){
        try {
            //questo è l'ordine delle cose, non cambiarlo 
            //Creo un game 
            String game_id  = (String) serviceManager.handleRequest("T4", "CreateGame", Time, difficulty, name, description, username);
            //Creo un round  
            String round_id = (String) serviceManager.handleRequest("T4", "CreateRound", game_id, ClasseUT, Time);
            //CReo un turno 
            String turn_id  = (String) serviceManager.handleRequest("T4", "CreateTurn", player_id, round_id, Time);
        
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
    private String CreateScalata(String player_id, String Scalata_name){
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

    private int GetUserScore(String testingClassName, String testingClassCode,
                                String underTestClassName, String underTestClassCode){

        String response_T7 = (String) serviceManager.handleRequest("T7", "CompileCoverage", 
        testingClassName, testingClassCode,underTestClassName, underTestClassCode);

        //qua testare il tipo di ritorno della chiamata e capire come convertirlo facilmente
        JSONObject responseObj = new JSONObject(response_T7);
        String xml_string = responseObj.getString("coverage");
        String outCompile = responseObj.getString("outCompile");

        //Questo si deve vedere dove riconvertirlo 
        //int userScore = ParseUtil.LineCoverage(xml_string);
        return 0;
    }

    private int GetRobotScore(String testClassId, String robot_type, String difficulty){
        String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati", 
        testClassId, robot_type, difficulty);

        //come sopra devo capire come convertire la risposta bene
        //Integer roboScore = Integer.parseInt(response_T4);
        //int numTurnsPlayed = Integer.parseInt(request.getParameter("order"));
        return "prova";
    }
    
    
    @PostMapping("/StartGame")
    public ResponseEntity<String> StartGame(){
        GameLogic gameLogic = activeGames.get(playerId);
        if(gameLogic == null){
            //Creo la nuova partita 
            gameLogic = new TurnBasedGameLogic(this.serviceManager, playerId, underTestClassName); 
            activeGames.put(playerID, gameLogic);
        }
    }

    @PostMapping("/run") 
    public ResponseEntity<String> Runner(@RequestParam("testingClassName")   String testingClassName, 
                                         @RequestParam("testingClassCode")   String testingClassCode,
                                         @RequestParam("underTestClassName") String underTestClassName,
                                         @RequestParam("underTestClassCode") String underTestClassCode,
                                         @RequestParam("testClassId")        String testClassId,
                                         @RequestParam("type")               String type,
                                         @RequestParam("difficulty")         String difficulty,
                                         @RequestParam("order")              int  numTurnsPlayed,
                                         @RequestParam("playerID")           String playerId){
        try {

            String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            GameLogic gameLogic = activeGames.get(playerId);

            int UserScore  = GetUserScore(testingClassName, testingClassCode, underTestClassName, underTestClassCode);
            int RobotScore = GetRobotScore(testClassId, type, difficulty);
            gameLogic.playTurn(UserScore, RobotScore);

            if(gameLogic.isGameOver()){
                //Qua risposta diversa
            }else{

            }

            /*
            // costruzione risposta verso task5
            System.out.println("Costruzione risposta verso task 5...");
            JSONObject result = new JSONObject();
            result.put("outCompile", outCompile);
            result.put("coverage", xml_string);
            result.put("win", userScore >= roboScore);
            result.put("robotScore", roboScore);
            result.put("score", userScore);
            result.put("gameScore", gameScore);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
            */

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("La richiesta non è valida."); 
        }
    }


}
