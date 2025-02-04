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
package com.g2.Interfaces;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.Game;
import com.g2.Model.StatisticProgress;

@Service
public class T4Service extends BaseService {

    // Costante che definisce l'URL di base per le richieste REST
    private static final String BASE_URL = "http://t4-g18-app-1:3000";

    // Costruttore della classe, inizializza il servizio con il RestTemplate e l'URL
    // di base
    public T4Service(RestTemplate restTemplate) {
        // Inizializzazione del servizio base con RestTemplate e URL specificato
        super(restTemplate, BASE_URL);

        registerAction("getGames", new ServiceActionDefinition(
                params -> getGames((int) params[0]),
                Integer.class
        ));

        registerAction("getStatisticsProgresses", new ServiceActionDefinition(
                params -> getStatisticsProgresses((int) params[0]),
                Integer.class
        ));

        registerAction("getHashStatisticsProgresses", new ServiceActionDefinition(
            params -> getHashStatisticsProgresses((int) params[0]),
            Integer.class
        ));

        registerAction("updateStatisticProgress", new ServiceActionDefinition(
                params -> updateStatisticProgress((int) params[0], (String) params[1], (float) params[2]),
                Integer.class, String.class, Float.class));

        registerAction("CreateGame", new ServiceActionDefinition(
                params -> CreateGame((String) params[0], (String) params[1], (String) params[2], (String) params[3],
                        (String) params[4], (Optional<Integer>)params[5]),
                String.class, String.class, String.class, String.class, String.class, Optional.class));

        registerAction("EndGame", new ServiceActionDefinition(
                params -> EndGame((int) params[0], (String) params[1], (String) params[2], (int) params[3],
                        (Boolean) params[4]),
                Integer.class, String.class, String.class, Integer.class, Boolean.class));
        //Aggiunta dell'ID del robot
        registerAction("CreateRound", new ServiceActionDefinition(
                params -> CreateRound((int) params[0], (String) params[1], (String) params[2], (int) params[3]),
                Integer.class, String.class, String.class, Integer.class));

        registerAction("EndRound", new ServiceActionDefinition(
                params -> EndRound((String) params[0], (int) params[1]),
                String.class, Integer.class));

        registerAction("CreateTurn", new ServiceActionDefinition(
                params -> CreateTurn((String) params[0], (int) params[1], (String) params[2]),
                String.class, Integer.class, String.class));

        registerAction("EndTurn", new ServiceActionDefinition(
                params -> EndTurn((int) params[0], (String) params[1], (String) params[2]),
                Integer.class, String.class, String.class));

        registerAction("CreateScalata", new ServiceActionDefinition(
                params -> CreateScalata((String) params[0], (String) params[1], (String) params[2], (String) params[3]),
                String.class, String.class, String.class, String.class));

        registerAction("GetRisultati", new ServiceActionDefinition(
                params -> GetRisultati((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class));
        //registro per il recupero dell'ID del robot da conservare ogni round
        registerAction("GetRobotID", new ServiceActionDefinition(
            params -> GetRobotID((String) params[0], (String) params[1], (String) params[2]),
            String.class, String.class, String.class));
        //vengono registrate le azioni per l'increamento del round di una scalata e della sua chiusura
        registerAction("UpdateScalata", new ServiceActionDefinition(
                params -> UpdateScalata((int) params[0], (int) params[1], (String)params[2]),
                Integer.class, Integer.class, String.class));
        registerAction("CloseScalata", new ServiceActionDefinition(
                    params -> CloseScalata((int) params[0], (int) params[1], (boolean) params[2], (int) params[3], (String) params[4]),
                    Integer.class, Integer.class,Boolean.class, Integer.class, String.class));
        
    }

    // usa /games per ottenere una lista di giochi
    private List<Game> getGames(int playerId) {
        final String endpoint = "/games/player/" + playerId;
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<Game>>() {
        });
    }

    private List<StatisticProgress> getStatisticsProgresses(int playerID) {
        Map<String, String> formData = new HashMap<>();
        formData.put("pid", String.valueOf(playerID));

        String endpoint = "/phca/" + playerID;

        List<StatisticProgress> response = callRestGET(endpoint, formData, new ParameterizedTypeReference<List<StatisticProgress>>() {
        });
        return response;
    }

    private Set<StatisticProgress> getHashStatisticsProgresses(int playerID) {
        Map<String, String> formData = new HashMap<>();
        formData.put("pid", String.valueOf(playerID));
        String endpoint = "/phca/" + playerID;
        // Recupera la risposta come una lista
        List<StatisticProgress> response = callRestGET( endpoint, 
                                                        formData, 
                                                        new ParameterizedTypeReference<List<StatisticProgress>>() {
                                                       });
        // Converti la lista in un HashSet per rimuovere eventuali duplicati
        Set<StatisticProgress> responseSet = new HashSet<>(response);
        return responseSet;
    }

    private String updateStatisticProgress(int playerID, String statisticID, float progress) {
        JSONObject obj = new JSONObject();
        obj.put("playerId", playerID);
        obj.put("statistic", statisticID);
        obj.put("progress", progress);

        String endpoint = "/phca/" + playerID + "/" + statisticID;
        String response = callRestPut(endpoint, obj, null, null, String.class);
        return response;
    }

    /*
    private String updateStatisticProgress(int playerID, String statisticID, float progress) {
        try {
            MultiValueMap<String, String> jsonMap = new LinkedMultiValueMap<>();
            jsonMap.put("playerId", Collections.singletonList(String.valueOf(playerID)));
            jsonMap.put("statistic", Collections.singletonList(statisticID));
            jsonMap.put("progress", Collections.singletonList(String.valueOf(progress)));

            String endpoint = "/phca/" + playerID + "/" + statisticID;

            String response = callRestPut(endpoint, jsonMap, new HashMap<>(), String.class);

            return response;
        } catch (Exception e) {
            System.out.println("[updateStatisticProgress] Errore nell'update delle statistiche: " + e.getMessage());
            return "errore UPDATESTATISTICPROGRESS";
        }
    }
     */
    // usa /robots per ottenere dati
    private String GetRisultati(String className, String robot_type, String difficulty) {
        Map<String, String> formData = new HashMap<>();
        formData.put("testClassId", className); // Nome della classe
        formData.put("type", robot_type); // Tipo di robot
        formData.put("difficulty", difficulty); // Livello di difficoltà corrente

        String response = callRestGET("/robots", formData, String.class);
        return response;
    }

    //FLAVIO 25GEN: MODIFICHE PER DARE A CALLRESTPOST DATI NEL FORMATO GIUSTO
    private int CreateGame(String Time, String difficulty, String name, String description, String id, Optional<Integer> scalataId) {
        System.out.println("CREATE GAME - T4 SERVICE");
        final String endpoint = "/games";
        JSONObject obj = new JSONObject();
        obj.put("difficulty", difficulty);
        obj.put("name", name);
        obj.put("description", description);
        obj.put("startedAt", Time);
        if(scalataId.isPresent()) {
            obj.put("selectedScalata", scalataId.get());
        }
        JSONArray playersArray = new JSONArray();
        playersArray.put(String.valueOf(id));
        obj.put("players", playersArray);
        System.out.println("Dati inviati a /games: " + obj); 
        // Questa chiamata in risposta dà anche i valori che hai fornito, quindi faccio parse per avere l'id
        String respose = callRestPost(endpoint, obj, null, null, String.class);
        System.out.println("Dati inviati a /games: " + respose); 
        // Parsing della stringa JSON
        JSONObject jsonObject = new JSONObject(respose);
        // Estrazione del valore di id
        return jsonObject.getInt("id");
    }

    //FLAVIO 26GEN: ISWINNER PASSATO COME BOOLEANO E USIAMO CALLRESTPUT
    private String EndGame(int gameid, String username, String closedAt, int Score, Boolean isWinner) {
        final String endpoint = "/games/" + String.valueOf(gameid);
        JSONObject obj = new JSONObject();
        obj.put("closedAt", closedAt);
        obj.put("username", username);
        obj.put("score", Score);
        obj.put("isWinner", isWinner);
        System.out.println("OBJ ENDGAME: " + obj);
        String respose = callRestPut(endpoint, obj, null, String.class);
        System.out.println("Dati inviati a /games: " + respose); 
        return respose;
    }

    /*
    private String EndGame(int gameid, String username, String closedAt, int Score, Boolean isWinner){
        final String endpoint = "/games/" + String.valueOf(gameid);
        JSONObject formData = new JSONObject();
        formData.put("closedAt", closedAt);
        formData.put("username", username);
        formData.put("score", Integer.toString(Score));
        formData.put("isWinner", isWinner ? "true" : "false");
        try {
            String respose = callRestPut(endpoint, formData, null, null, String.class);
            return respose;
        } catch (Exception e) {
            throw new IllegalArgumentException("[CreateGame]: " + e.getMessage());
        }
    }
     */

    //aggiunta dell'ID del robot
    //FLAVIO 25GEN: MODIFICHE PER DARE A CALLRESTPOST DATI NEL FORMATO GIUSTO
    private int CreateRound(int game_id, String ClasseUT, String Time, int robot_id) {
        final String endpoint = "/rounds";
        JSONObject obj = new JSONObject();
        obj.put("gameId", game_id);
        obj.put("testClassId", ClasseUT);
        obj.put("startedAt", Time);
        obj.put("robotID", robot_id);
        String respose = callRestPost(endpoint, obj, null, null, String.class);
        // Parsing della stringa JSON
        JSONObject jsonObject = new JSONObject(respose);
        System.out.println("Dati inviati a /rounds: " + jsonObject); 
        // Estrazione del valore di id
        return jsonObject.getInt("id");
    }

    //26GEN FLAVIO: modifiche json invece di formdata
    /*
    private String EndRound(String Time, int roundId) {
        // Anche qui non è stato previsto un parametro per la chiamata rest e quindi va costruito a mano
        final String endpoint = "rounds/" + String.valueOf(roundId);
        //MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //formData.add("closedAt", Time);
        JSONObject obj = new JSONObject();
        obj.put("closedAt", Time);
        String response = callRestPut(endpoint, obj, null, String.class);
        return response;
    }
    */

    //FLAVIO 25GEN: MODIFICHE PER DARE A CALLRESTPOST DATI NEL FORMATO GIUSTO
    private String CreateTurn(String Player_id, int Round_id, String Time) {
        final String endpoint = "/turns";
        JSONObject obj = new JSONObject();
        JSONArray playersArray = new JSONArray();
        playersArray.put(Player_id);
        obj.put("players", playersArray);
        obj.put("roundId", Round_id);
        obj.put("startedAt", Time);
    
        try {
            String response = callRestPost(endpoint, obj, null, null, String.class);
            System.out.println("Risposta ricevuta da /turns: " + response);
    
            //SALVATAGGIO IN DATABASE COME LISTA QUINDI CALCOLARE ANCHE LE QUADRE
            if (response.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    JSONObject lastObject = jsonArray.getJSONObject(jsonArray.length() - 1); // Ultimo elemento
                    return lastObject.get("id").toString();
                } else {
                    throw new IllegalStateException("La lista dei turni è vuota.");
                }
            } else {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.get("id").toString();
            }
        } catch (Exception e) {
            System.err.println("Errore durante la chiamata /turns: " + e.getMessage());
            throw e;
        }
    }

    //FLAVIO 26GEN: modificata in versione json
    private String EndTurn(int user_score, String Time, String turnId) {
        // Anche qui non è stato previsto un parametro per la chiamata rest e quindi va costruito a mano
        final String endpoint = "turns/" + turnId;
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("scores", String.valueOf(user_score));
        jsonBody.put("closedAt", Time);
        System.out.println("JSON ENDTURN: " + jsonBody);
        String response = callRestPut(endpoint, jsonBody, null, String.class);
        System.out.println("Dati di endturn inviati a /turns: " + response);
        return response;
    }

    //Servizio API per recuperare l'ID del robot
    //FLAVIO 25GEN: MODIFICHE PER DARE A CALLRESTGET DATI NEL FORMATO GIUSTO
private int GetRobotID(String classUT, String robot_type, String difficulty){
    final String endpoint = "/robots";
    Map<String, String> formData = new HashMap<>();
    formData.put("testClassID", classUT);
    formData.put("type", robot_type);
    formData.put("difficulty", difficulty);
    System.out.println("FORMDATA GETROBOTID: " + formData); 
    Map<String, Object> response = callRestGET(endpoint, formData, Map.class);
    int robotId = (int) response.get("id"); // Estrai il valore numerico
    System.out.println("Dati inviati a /robots: " + robotId);
    return robotId;
}

    //SERVIZI SCALATA
    //FLAVIO 25GEN: MODIFICHE PER DARE A CALLRESTPOST DATI NEL FORMATO GIUSTO
    private String CreateScalata(String player_id, String scalata_name, String creation_Time, String creation_date) {
        final String endpoint = "/scalates";
        JSONObject obj = new JSONObject();
        obj.put("playerID", Integer.parseInt(player_id));
        obj.put("scalataName", scalata_name);
        obj.put("creationTime", creation_Time);
        obj.put("creationDate", creation_date);
        System.out.println("Dati inviati a /scalates: " + obj);
        //SIMONE: lascio un attimo a string ma serve int (?)
        String respose = callRestPost(endpoint, obj, null, null, String.class);
        // Parsing della stringa JSON
        JSONObject jsonObject = new JSONObject(respose);
        System.out.println("Dati inviati da createscalata a /scalates: " + jsonObject); 
        //Estrazione del valore di id
        System.out.println("ID DI CREA SCALATA" + jsonObject.get("id").toString());
        return jsonObject.get("id").toString();
    }

    //27GEN MODIFICHE
    private String UpdateScalata(int scalata_id, int round_id, String update_date){
        final String endpoint = "/scalates/" + String.valueOf(scalata_id);
        JSONObject obj = new JSONObject();
        obj.put("CurrentRound", round_id);
        obj.put("updateDate", update_date);
    
        String response = callRestPut(endpoint, obj, null, String.class);
        return response;
    }

    private String CloseScalata(int scalata_id, int round_id, boolean is_win, int final_score, String close_time){
        System.out.println("Valore di scalataid in T4 service closescalata: " + scalata_id);
        final String endpoint = "/scalates/" + String.valueOf(scalata_id);
        System.out.println(endpoint);
        JSONObject obj = new JSONObject();
        obj.put("CurrentRound", round_id);
        obj.put("isFinished", is_win);
        obj.put("FinalScore", final_score);
        obj.put("ClosedAt", close_time);
        System.out.println("Dati inviati da closescalata a /scalates: " + obj);
        String response = callRestPut(endpoint, obj, null, String.class);
        System.out.println("CLOSESCLATA - callrestput ha funziona correttamente");
        System.out.println("CLOSESCLATA - response: " + response);
        return response;
    }

}
