package com.g2.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
                String.class));
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
                    throw new IllegalArgumentException("Errore getLevels: " + e.getMessage());
                }
            }
        }

        // Ritorna la lista dei livelli trovati
        return result;
    }
}