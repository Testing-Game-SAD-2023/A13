package com.g2.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.Game;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
public class GameController {

    private final ServiceManager serviceManager; 
    public GameController(RestTemplate restTemplate){
        this.serviceManager = new ServiceManager(restTemplate);
    }

    //Refactor del vecchio GameDataWriter e metodo saveGame, in effetti più che salvarlo lo stiamo creando
    //farne una classe separata con un solo metodo e poi chiamarla nel GUIcontroller era un po' inutile 
    private MultiValueMap<String, String> SaveGame(Game game, String Time, String ClasseUT, String player_id){
        try {
            //questo è l'ordine delle cose, non cambiarlo 
            //Creo un game 
            String game_id  = (String) serviceManager.handleRequest("T4", "CreateGame", game, Time);
            //Creo un round  
            String round_id = (String) serviceManager.handleRequest("T4", "CreateRound", game_id, ClasseUT, Time);
            //CReo un turno 
            String turn_id  = (String) serviceManager.handleRequest("T4", "CreateTurn", player_id, round_id, Time);
        
            //incapsulo i 3 id 
            MultiValueMap<String, String> return_data = new LinkedMultiValueMap<>();
            return_data.add("game_id", game_id);
            return_data.add("round_id", round_id);
            return_data.add("turn_id", turn_id);
            return return_data;
        } catch (Exception e) {
            return null;
        }
    }

    //in maniera analoga qui ho fatto refactor del save scalata e della classe ScalataDataWriter
    private String SaveScalata(String player_id, String Scalata_name){
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
}
