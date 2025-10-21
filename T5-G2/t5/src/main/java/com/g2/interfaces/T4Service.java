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
package com.g2.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.model.Game;
import com.g2.model.PlayerResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.score.basic.CoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.List;
import java.util.Map;

@Service
public class T4Service extends BaseService {

    // Costante che definisce l'URL di base per le richieste REST
    private static final String BASE_URL = "http://api_gateway-controller:8090";
    //private static final String BASE_URL = "http://127.0.0.1:8090";

    private static final String SERVICE_PREFIX = "gamerepo";
    private final ObjectMapper mapper = new ObjectMapper();

    // Costruttore della classe, inizializza il servizio con il RestTemplate e l'URL
    // di base
    public T4Service(RestTemplate restTemplate) {
        // Inizializzazione del servizio base con RestTemplate e URL specificato
        super(restTemplate, BASE_URL + "/" + SERVICE_PREFIX);


        registerAction("getGames", new ServiceActionDefinition(
                params -> getGames((long) params[0]),
                Long.class
        ));


        registerAction("CreateGame", new ServiceActionDefinition(
                params -> CreateGame((GameMode) params[0], (long) params[1]),
                GameMode.class, Long.class));

        registerAction("CreateRound", new ServiceActionDefinition(
                params -> CreateRound((long) params[0], (String) params[1], (String) params[2], (OpponentDifficulty) params[3], (int) params[4]),
                Long.class, String.class, String.class, OpponentDifficulty.class, Integer.class));

        registerAction("CreateTurn", new ServiceActionDefinition(
                params -> CreateTurn((long) params[0], (long) params[1], (int) params[2]),
                Long.class, Long.class, Integer.class));

        registerAction("EndTurn", new ServiceActionDefinition(
                params -> EndTurn((long) params[0], (Long) params[1], (int) params[2], (CompileResult) params[3]),
                Long.class, Long.class, Integer.class, CompileResult.class));

        registerAction("EndRound", new ServiceActionDefinition(
                params -> EndRound((long) params[0]),
                Long.class));

        registerAction("EndGame", new ServiceActionDefinition(
                params -> EndGame((long) params[0], (Map<Long, PlayerResult>) params[1], (boolean) params[2]),
                Long.class, Map.class, Boolean.class));


        registerAction("CreateScalata", new ServiceActionDefinition(
                params -> CreateScalata((String) params[0], (String) params[1], (String) params[2], (String) params[3]),
                String.class, String.class, String.class, String.class));


    }


    // usa /games per ottenere una lista di giochi
    private List<Game> getGames(long playerId) {
        final String endpoint = "/games/player/" + playerId;
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<Game>>() {
        });
    }


    private long CreateGame(GameMode gameMode, long playerId) {
        final String endpoint = "/games";

        JSONObject obj = new JSONObject();

        JSONArray playersArray = new JSONArray();
        playersArray.put(String.valueOf(playerId));

        obj.put("gameMode", gameMode);
        obj.put("players", playersArray);

        // Questa chiamata in risposta dà anche i valori che hai fornito, quindi faccio
        // parse per avere l'id
        String respose = callRestPost(endpoint, obj, null, null, String.class);
        // Parsing della stringa JSON
        JSONObject jsonObject = new JSONObject(respose);
        // Estrazione del valore di id
        return jsonObject.getInt("id");
    }


    private int CreateRound(long gameId, String ClasseUT, String type, OpponentDifficulty difficulty, int roundNumber) {
        final String endpoint = "/games/%s/rounds".formatted(gameId);

        JSONObject requestBody = new JSONObject();
        requestBody.put("classUT", ClasseUT);
        requestBody.put("type", type);
        requestBody.put("difficulty", difficulty);
        requestBody.put("roundNumber", roundNumber);

        String response = callRestPost(endpoint, requestBody, null, null, String.class);
        // Parsing della stringa JSON
        JSONObject jsonObject = new JSONObject(response);
        // Estrazione del valore di id
        return jsonObject.getInt("roundNumber");
    }

    private int CreateTurn(long gameId, long playerId, int turnNumber) {
        final String endpoint = "/games/%s/rounds/last/turns".formatted(gameId);

        JSONObject requestBody = new JSONObject();
        requestBody.put("playerId", playerId);
        requestBody.put("turnNumber", turnNumber);

        String response = callRestPost(endpoint, requestBody, null, null, String.class);

        JSONObject jsonObject = new JSONObject(response);
        // Estrazione del valore di id
        return jsonObject.getInt("turnNumber");
    }

    private String EndTurn(long gameId, long playerId, int turnNumber, CompileResult userCompileResult) {
        final String endpoint = "/games/%s/rounds/last/turns/%s".formatted(gameId, turnNumber);

        JSONObject requestBody = new JSONObject();
        requestBody.put("playerId", playerId);

        JacocoScoreDTO jacocoScoreDTO = new JacocoScoreDTO();
        jacocoScoreDTO.setLineCoverageDTO(new CoverageDTO(
                userCompileResult.getLineCoverage().getCovered(),
                userCompileResult.getLineCoverage().getMissed()
        ));
        jacocoScoreDTO.setBranchCoverageDTO(new CoverageDTO(
                userCompileResult.getBranchCoverage().getCovered(),
                userCompileResult.getBranchCoverage().getMissed()
        ));
        jacocoScoreDTO.setInstructionCoverageDTO(new CoverageDTO(
                userCompileResult.getBranchCoverage().getCovered(),
                userCompileResult.getBranchCoverage().getMissed()
        ));

        EvosuiteScoreDTO evosuiteScoreDTO = new EvosuiteScoreDTO();
        evosuiteScoreDTO.setLineCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteLine().getCovered(),
                userCompileResult.getEvosuiteLine().getMissed()
        ));
        evosuiteScoreDTO.setBranchCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteBranch().getCovered(),
                userCompileResult.getEvosuiteBranch().getMissed()
        ));
        evosuiteScoreDTO.setCBranchCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteCBranch().getCovered(),
                userCompileResult.getEvosuiteCBranch().getMissed()
        ));
        evosuiteScoreDTO.setMethodCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteMethod().getCovered(),
                userCompileResult.getEvosuiteMethod().getMissed()
        ));
        evosuiteScoreDTO.setExceptionCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteException().getCovered(),
                userCompileResult.getEvosuiteException().getMissed()
        ));
        evosuiteScoreDTO.setOutputCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteOutput().getCovered(),
                userCompileResult.getEvosuiteOutput().getMissed()
        ));
        evosuiteScoreDTO.setWeakMutationCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteWeakMutation().getCovered(),
                userCompileResult.getEvosuiteWeakMutation().getMissed()
        ));
        evosuiteScoreDTO.setMethodNoExceptionCoverageDTO(new CoverageDTO(
                userCompileResult.getEvosuiteMethodNoException().getCovered(),
                userCompileResult.getEvosuiteMethodNoException().getMissed()
        ));

        try {
            requestBody.put("jacocoScoreDTO", new JSONObject(mapper.writeValueAsString(jacocoScoreDTO)));
            requestBody.put("evosuiteScoreDTO", new JSONObject(mapper.writeValueAsString(evosuiteScoreDTO)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String response = callRestPut(endpoint, requestBody, null, null, String.class);
        return response;
    }

    private String EndRound(long gameId) {
        //Anche qui non è stato previsto un parametro per la chiamata rest e quindi va costruito a mano
        final String endpoint = "/games/%s/rounds/last".formatted(gameId);
        try {
            String response = callRestPut(endpoint, new JSONObject(), null, null, String.class);
            return response;
        } catch (Exception e) {
            throw new IllegalArgumentException("[EndRound]: " + e.getMessage());
        }
    }

    private String EndGame(long gameId, Map<Long, PlayerResult> scores, boolean isGameSurrendered) {
        final String endpoint = "/games/%s".formatted(gameId);

        JSONObject requestBody = new JSONObject();
        JSONObject results;

        try {
            results = new JSONObject(mapper.writeValueAsString(scores));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[EndGame]: " + e.getMessage());
        }

        requestBody.put("results", results);
        requestBody.put("isGameSurrendered", isGameSurrendered);

        String respose = callRestPut(endpoint, requestBody, null, null, String.class);
        return respose;
    }

    // Questa chiamata non è documentata nel materiale di caterina
    private String CreateScalata(String player_id, String scalata_name, String creation_Time, String creation_date) {
        final String endpoint = "/turns";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("playerID", player_id);
        formData.add("scalataName", scalata_name);
        formData.add("creationTime", creation_Time);
        formData.add("creationDate", creation_date);
        String respose = callRestPost(endpoint, formData, null, String.class);
        return respose;
    }

}
