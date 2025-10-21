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

package com.g2.t5;

import com.g2.model.Game;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Codice legacy
 */
public class GameDataWriter {

    private final HttpClient httpClient = HttpClientBuilder.create().build();


    /*
     * Questo è codice legacy non più utilizzato !
     */
    public JSONObject saveGame(Game game, String username, Optional<Integer> selectedScalata) {
        try {
            String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            JSONObject obj = new JSONObject();

            System.out.println("SONO IN SAVEGAME, GUARDA LO USERNAME : " + game.getUsername());

            obj.put("difficulty", game.getDifficulty());
            obj.put("name", game.getName());
            obj.put("description", game.getDescription());
            obj.put("username", game.getUsername());
            obj.put("startedAt", time);
            if (selectedScalata.isPresent()) {
                obj.put("selectedScalata", selectedScalata.get());
            }

            JSONArray playersArray = new JSONArray();
            playersArray.put(String.valueOf(game.getPlayerId()));

            obj.put("players", playersArray);

            HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/games");
            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);
            System.out.println("(GameDataWriter) saveGame sending Json to t4: " + obj);

            httpPost.setEntity(jsonEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            JSONObject responseObj = new JSONObject(responseBody);

            Integer game_id = responseObj.getInt("id"); // salvo il game id che l'Api mi restituisce

            JSONObject round = new JSONObject();
            round.put("gameId", game_id);
            round.put("testClassId", game.getClasse());
            round.put("startedAt", time);

            httpPost = new HttpPost("http://t4-g18-app-1:3000/rounds");
            jsonEntity = new StringEntity(round.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            responseEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(responseEntity);
            responseObj = new JSONObject(responseBody);

            Integer round_id = responseObj.getInt("id"); // salvo il round id che l'Api mi restituisce

            JSONObject turn = new JSONObject();

            turn.put("players", playersArray);
            turn.put("roundId", round_id);
            turn.put("startedAt", time);

            httpPost = new HttpPost("http://t4-g18-app-1:3000/turns");
            jsonEntity = new StringEntity(turn.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            responseEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(responseEntity);

            JSONArray responseArrayObj = new JSONArray(responseBody);
            Integer turn_id = responseArrayObj.getJSONObject(0).getInt("id"); // salvo il turn id che l'Api mi
            // restituisce

            JSONObject resp = new JSONObject();
            resp.put("game_id", game_id);
            resp.put("round_id", round_id);
            resp.put("turn_id", turn_id);

            return resp;
        } catch (IOException e) {
            // Gestisci l'eccezione o restituisci un errore appropriato
            System.err.println(e);
            return null;
        }

    }
}