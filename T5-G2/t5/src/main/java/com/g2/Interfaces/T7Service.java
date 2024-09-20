package com.g2.Interfaces;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
        try {
            // Creazione del formData con i parametri della richiesta POST
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("testingClassName", testingClassName);
            formData.add("testingClassCode", testingClassCode);
            formData.add("underTestClassName", underTestClassName);
            formData.add("underTestClassCode", underTestClassCode);

            // Esegue la chiamata POST utilizzando il metodo callRestPost passando
            // all'endpoint il formData con i parametri richiesti
            String out_string = callRestPost(endpoint, formData, null, String.class);

            // Ritorna la risposta ottenuta dalla chiamata REST
            return out_string;

        } catch (RestClientException e) {
            // Gestione degli errori durante la chiamata
            // Gestione delle eccezioni in caso di errore nella chiamata REST
            throw new IllegalArgumentException("Errore durante la chiamata POST: " + e.getMessage());

        }
    }

}
