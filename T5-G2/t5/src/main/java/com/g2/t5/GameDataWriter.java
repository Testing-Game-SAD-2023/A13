package com.g2.t5;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.g2.Model.Game;

public class GameDataWriter {

    private final HttpClient httpClient = HttpClientBuilder.create().build();
    // private static String CSV_FILE_PATH =
    // "/app/AUTName/StudentLogin/GameId/GameData.csv";
    // private static final String[] CSV_HEADER = { "GameId", "Username",
    // "PlayerClass", "Robot" };
    // public long getGameId() {
    // long gameId = 0;

    // try {
    // // Crea il Reader per il file CSV
    // Reader reader = new FileReader(CSV_FILE_PATH);

    // // Crea il CSVParser con il Reader e il formato CSV
    // CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

    // // Ottieni tutte le righe del file CSV
    // List<CSVRecord> records = csvParser.getRecords();

    // // Verifica se ci sono righe nel file CSV
    // if (!records.isEmpty()) {
    // // Prendi l'ultima riga del file CSV
    // CSVRecord lastRecord = records.get(records.size() - 1);

    // // Ottieni l'ID dalla prima colonna della riga
    // gameId = Long.parseLong(lastRecord.get(0));
    // }

    // // Chiudi il CSVParser e il Reader
    // csvParser.close();
    // reader.close();
    // } catch (IOException e) {
    // System.err.println("Errore durante la lettura del file CSV: " +
    // e.getMessage());
    // }

    // return gameId;
    // }
    public JSONObject saveGame(Game game) {
        try {
            String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            JSONObject obj = new JSONObject();

            obj.put("difficulty", game.getDifficulty());
            obj.put("name", game.getName());
            obj.put("description", game.getDescription());
            obj.put("startedAt", time);

            JSONArray playersArray = new JSONArray(); 
            playersArray.put(String.valueOf(game.getPlayerId()));

            obj.put("players", playersArray);

            HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/games");
            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if(statusCode > 299) {
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
            
            if(statusCode > 299) {
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
            
            if(statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            responseEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(responseEntity);

            JSONArray responseArrayObj = new JSONArray(responseBody);
            Integer turn_id = responseArrayObj.getJSONObject(0).getInt("id"); // salvo il turn id che l'Api mi restituisce

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
        // try {
        // // Crea il file CSV se non esiste
        // File file = new File(CSV_FILE_PATH);
        // CSV_FILE_PATH = file.getAbsolutePath();

        // // Crea il Writer per il file CSV
        // Writer writer = new FileWriter(CSV_FILE_PATH, true);

        // // Crea il CSVPrinter con il Writer e il formato CSV
        // CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        // // Scrivi i dati dell'oggetto Game come tupla CSV nel file
        // csvPrinter.printRecord(game.getGameId(), game.getUsername(),
        // game.getPlayerClass(), game.getRobot(), game.getData_creazione(),
        // game.getOra_creazione());

        // // Chiudi il CSVPrinter e il Writer
        // csvPrinter.flush();
        // csvPrinter.close();
        // writer.close();

        // System.out.println("L'oggetto Game è stato salvato correttamente nel file
        // CSV.");
        // } catch (IOException e) {
        // System.err.println("Errore durante la scrittura del file CSV: " +
        // e.getMessage());
        // }
    }
}