package com.groom.manvsclass.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentService {

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    public ResponseEntity<?> ottieniStudentiDettagli(List<String> studentiIds, String jwt) {
        System.out.println("Inizio metodo ottieniStudentiDettagli");

        // 1. Verifica validità del token JWT
        if (jwt == null || jwt.isEmpty()) {
            System.out.println("Token JWT non valido o mancante.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        if (studentiIds == null || studentiIds.isEmpty()) {
            System.out.println("La lista degli studenti è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista degli studenti è vuota.");
        }

        try {
            // 2. Prepara il corpo JSON
            System.out.println("Preparazione del corpo JSON...");
            JSONObject requestBody = new JSONObject();
            JSONArray studentiArray = new JSONArray(studentiIds);
            StringEntity entity = new StringEntity(studentiArray.toString());
            System.out.println("Corpo JSON preparato: " + entity.toString());

            // 3. Configura la richiesta HTTP POST
            System.out.println("Configurazione della richiesta HTTP POST...");
            HttpPost httpPost = new HttpPost("http://t23-g1-app-1:8080/studentiTeam");
            httpPost.setHeader("Authorization", "Bearer " + jwt);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(entity.toString()));

            // 4. Esegui la richiesta
            System.out.println("Esecuzione della richiesta...");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("Risposta HTTP ricevuta. Status code: " + statusCode);

            // 5. Gestisci la risposta
            if (statusCode >= 200 && statusCode < 300) { // Successo
                HttpEntity responseEntity = httpResponse.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println("Risposta positiva ricevuta: " + responseBody);
                return ResponseEntity.ok(responseBody);
            } else { // Errore
                HttpEntity responseEntity = httpResponse.getEntity();
                String errorResponse = responseEntity != null ? EntityUtils.toString(responseEntity) : "Errore sconosciuto";
                System.out.println("Errore durante la richiesta: " + errorResponse);
                return ResponseEntity.status(statusCode).body(errorResponse);
            }
        } catch (IOException e) {
            // Gestione delle eccezioni con log aggiuntivi
            System.out.println("Eccezione durante la comunicazione con il server:");
            e.printStackTrace(); // Mostra la traccia completa dell'eccezione
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore di comunicazione con il container remoto: " + e.getMessage());
        }
    }
}
