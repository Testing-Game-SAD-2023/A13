package com.g2.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.Game;

public class T4Service extends BaseService {

    // Costante che definisce l'URL di base per le richieste REST
    private static final String BASE_URL = "http://t4-g18-app-1:3000";

    // Costruttore della classe, inizializza il servizio con il RestTemplate e l'URL
    // di base
    public T4Service(RestTemplate restTemplate) {
        // Inizializzazione del servizio base con RestTemplate e URL specificato
        super(restTemplate, BASE_URL);

        // Registrazione dell'azione "getLevels" con una definizione specifica per
        // questa azione
        registerAction("getLevels", new ServiceActionDefinition(
                // Definizione di un'operazione lambda che invoca il metodo getLevels con un
                // parametro di tipo String
                params -> getLevels((String) params[0]),
                // L'azione è definita per accettare un parametro di tipo String
                String.class
        ));

        registerAction("CreateGame", new ServiceActionDefinition(
            params -> CreateGame((Game) params[0], (String) params[1]),
             Game.class, String.class
        ));

        registerAction("CreateRound", new ServiceActionDefinition(
            params -> CreateRound((String) params[0], (String) params[1], (String) params[2]), 
            String.class, String.class, String.class
        ));

        registerAction("CreateTurn", new ServiceActionDefinition(
            params -> CreateTurn((String)params[0], (String)params[1], (String)params[2]), 
            String.class, String.class, String.class
        ));

        registerAction("CreateScalata", new ServiceActionDefinition(
            params -> CreateScalata((String) params[0], (String) params[1], (String) params[2], (String) params[3]), 
            String.class, String.class, String.class, String.class
        ));
    }

    /**
     * Metodo che invia richieste per ottenere diversi "livelli" (levels) in base al
     * nome della classe.
     * Per ogni livello (da 0 a 10) e per ciascun tipo di robot ("randoop",
     * "evosuite"),
     * viene effettuata una chiamata REST per verificare la presenza di dati
     * associati.
     * 
     */
    private List<String> getLevels(String className) {
        // Inizializzazione di una lista per conservare i risultati
        List<String> result = new ArrayList<>();

        // Definizione dei tipi di robot che verranno utilizzati nella chiamata
        List<String> robot_type = List.of("randoop", "evosuite");

        // Iterazione su 11 livelli di difficoltà (da 0 a 10)
        for (int i = 0; i < 11; i++) {
            // Per ogni tipo di robot definito
            for (String robot_string : robot_type) {
                try {
                    // Creazione di una mappa per i parametri del form da inviare nella richiesta
                    // GET
                    Map<String, String> formData = new HashMap<>();
                    formData.put("testClassId", className); // Nome della classe
                    formData.put("type", robot_string); // Tipo di robot
                    formData.put("difficulty", String.valueOf(i)); // Livello di difficoltà corrente

                    // Invio della richiesta GET tramite il servizio Rest, con i parametri e attesa
                    // di una risposta di tipo String
                    // <<<<<Nota:Non sappiamo il motivo per il quale è stato implementato in questo
                    // modo>>>>
                    String response = callRestGET("/robots", formData, String.class);

                    // Se la risposta non è nulla, aggiungi il livello corrente alla lista dei
                    // risultati
                    if (response != null) {
                        result.add(String.valueOf(i));
                    }
                } catch (Exception e) {
                    // Gestione delle eccezioni, lancia un'eccezione personalizzata in caso di
                    // errore
                    break;
                }
            }
        }

        // Ritorna la lista dei livelli trovati
        return result;
    }

    private String CreateGame(Game game, String Time){
        final String endpoint = "/games";
        //String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("difficulty", game.getDifficulty());
        formData.add("name", game.getName());
        formData.add("description", game.getDescription());
        formData.add("username", game.getUsername());
        formData.add("startedAt", Time);
        try{
            String respose = callRestPost(endpoint, formData, null, String.class);
            return respose; 
        }catch(Exception e){
            throw new IllegalArgumentException("[CreateGame] Errore creazione partita: " + e.getMessage());
        }
    }

    private String CreateRound(String game_id, String ClasseUT, String Time){
        final String endpoint = "/rounds";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("gameId", game_id);
        formData.add("testClassId", ClasseUT);
        formData.add("startedAt", Time);
        try {
            String respose = callRestPost(endpoint, formData, null, String.class);
            return respose; 
        } catch (Exception e) {
            throw new IllegalArgumentException("[CreateTurn] Errore creazione turno: " + e.getMessage());
        }
    }
    
    private String CreateTurn(String Player_id, String Round_id, String Time){
        final String endpoint = "/turns";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("players", Player_id);
        formData.add("roundId", Round_id);
        formData.add("startedAt", Time);
        try {
            String respose = callRestPost(endpoint, formData, null, String.class);
            return respose; 
        } catch (Exception e) {
            throw new IllegalArgumentException("[CreateTurn] Errore creazione turno: " + e.getMessage());
        }
    }

    //Questa chiamata non è documentata nel materiale di caterina
    private String CreateScalata(String player_id, String scalata_name, String creation_Time, String creation_date){
        final String endpoint = "/turns";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("playerID", player_id);
        formData.add("scalataName", scalata_name);
        formData.add("creationTime", creation_Time);
        formData.add("creationDate", creation_date);

        try {
            String respose = callRestPost(endpoint, formData, null, String.class);
            return respose; 
        } catch (Exception e) {
            throw new IllegalArgumentException("[CreateTurn] Errore creazione scalata: " + e.getMessage());
        }
    }

}