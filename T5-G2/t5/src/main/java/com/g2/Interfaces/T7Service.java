package com.g2.Interfaces;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class T7Service extends BaseService {

    private static final String BASE_URL = "http://remoteccc-app-1:1234";

    public T7Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL);

        // Registrazione di un'azione chiamata "CompileCoverage" con la definizione di
        // un'azione di servizio
        registerAction("CompileCoverage", new ServiceActionDefinition(
                // Viene passato un'operazione lambda che invoca il metodo CompileCoverage con 4
                // parametri di tipo String
                params -> CompileCoverage((String) params[0], (String) params[1], (String) params[2],
                        (String) params[3]),
                // Specifica che l'azione accetta quattro parametri di tipo String
                String.class, String.class, String.class, String.class));

    }

    /**
     * Metodo che compila il codice di test e di classe sotto test e ne esegue
     * l'analisi della copertura del codice.
     */
    private String CompileCoverage(String testingClassName, String testingClassCode,
            String underTestClassName, String underTestClassCode) {
        final String endpoint = "/compile-and-codecoverage"; // Definisce l'endpoint per l'API di compilazione e analisi
        // della copertura del codice
        try{
            // Creazione del json con i parametri della richiesta POST
            JSONObject obj = new JSONObject();
            obj.put("testingClassName", testingClassName);
            obj.put("testingClassCode", testingClassCode);
            obj.put("underTestClassName", underTestClassName);
            obj.put("underTestClassCode", underTestClassCode);

            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("Content-Type", "application/json");
            String respose = callRestPost(endpoint, obj, null, customHeaders,String.class);
            return respose;
        } catch (RestClientException e) {
            // Gestione degli errori durante la chiamata
            // Gestione delle eccezioni in caso di errore nella chiamata REST
            throw new IllegalArgumentException("Errore durante la chiamata POST: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore durante la chiamata POST: " + e.getMessage());
        }
    }

}