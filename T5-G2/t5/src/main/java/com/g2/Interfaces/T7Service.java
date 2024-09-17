package com.g2.Interfaces;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class T7Service implements ServiceInterface{
    private final RestService restService;
    private static final String BASE_URL = "http://remoteccc-app-1:1234";

    public T7Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    @Override
    public Object handleRequest(String action, Object... params) {
        switch (action) {
            case "compile-and-codecoverage" -> {
                if (params.length != 4) {
                    throw new IllegalArgumentException("[HANDLEREQUEST] Per 'compile-and-codecoverage' sono richiesti 4 parametri.");
                }
                if (!(params[0] instanceof String) | !(params[1] instanceof String)
                        | !(params[2] instanceof String) | !(params[3] instanceof String)) {
                    throw new IllegalArgumentException("[HANDLEREQUEST] I parametri per 'compile-and-codecoverage' devono essere una stringa.");
                }
                return CompileCoverage((String) params[0], (String)params[1], (String)params[2], (String)params[3]);
            }
            default -> throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
        }
        // Aggiungi altri casi per altre azioni
    }

    //T7 espone solo questo servizio, codice rubato un po' da T6
    /* Compilazione dei due files Java ricevuti in ingresso(codice prodotto
        dal giocatore e test prodotti dal robot),e restituzione dei risultati di copertura prodotti da JaCoCo  */
    //QUA VA TESTATO E CAPITO IL TIPO DI RITORNO PIù COMODO 
    private String CompileCoverage(String testingClassName, String testingClassCode, 
    String underTestClassName, String underTestClassCode){
        final String endpoint = "/compile-and-codecoverage";
         try {
            // Creazione del formData con i parametri della richiesta
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("testingClassName", testingClassName);
            formData.add("testingClassCode", testingClassCode);
            formData.add("underTestClassName", underTestClassName);
            formData.add("underTestClassCode", underTestClassCode);

            // Utilizza la classe RestService per eseguire la chiamata POST
            String out_string = restService.CallRestPost(endpoint, formData, null, String.class);

            // Ritorna la risposta
            return out_string;

        } catch (RestClientException e) {
            // Gestione degli errori durante la chiamata
            System.out.println("Errore durante la chiamata POST: " + e.getMessage());
            return null;
        }
    }

}
