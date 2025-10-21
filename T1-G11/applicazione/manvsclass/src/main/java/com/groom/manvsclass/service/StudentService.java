package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class StudentService {

    private final ApiGatewayClient apiGatewayClient;

    public StudentService(ApiGatewayClient apiGatewayClient) {
        this.apiGatewayClient = apiGatewayClient;
    }

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
            HttpResponse httpResponse = apiGatewayClient.callOttieniStudentiDettagli(studentiIds, jwt);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("Risposta HTTP ricevuta. Status code: " + statusCode);

            // 5. Gestisci la risposta
            if (statusCode >= 200 && statusCode < 300) { // Successo
                HttpEntity responseEntity = httpResponse.getEntity();
                String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

                // Converte la stringa in un oggetto JSON (array)
                JSONArray jsonResponse = new JSONArray(responseBody);

                // Restituisci l'array come è (Spring lo serializzerà in JSON correttamente)
                System.out.println("Risposta positiva ricevuta: " + jsonResponse.toString());
                return ResponseEntity.ok(jsonResponse.toList());
            } else { // Errore
                HttpEntity responseEntity = httpResponse.getEntity();
                String errorResponse = responseEntity != null ? EntityUtils.toString(responseEntity, StandardCharsets.UTF_8) : "Errore sconosciuto";
                System.out.println("Errore durante la richiesta: " + errorResponse);
                return ResponseEntity.ok(errorResponse); //Da modificare
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
