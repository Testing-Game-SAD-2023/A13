package com.g2.Interfaces;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpPost httpPost = new HttpPost(BASE_URL + endpoint);
            // Creazione del formData con i parametri della richiesta POST
            JSONObject obj = new JSONObject();
            obj.put("testingClassName", testingClassName);
            obj.put("testingClassCode", testingClassCode);
            obj.put("underTestClassName", underTestClassName);
            obj.put("underTestClassCode", underTestClassCode);
            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(jsonEntity);
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            // Ritorna la risposta ottenuta dalla chiamata REST
            return responseBody;
        } catch (RestClientException e) {
            // Gestione degli errori durante la chiamata
            // Gestione delle eccezioni in caso di errore nella chiamata REST
            throw new IllegalArgumentException("Errore durante la chiamata POST: " + e.getMessage());
        } catch (Exception e){
            throw new IllegalArgumentException("Errore durante la chiamata POST: " + e.getMessage());
        }
    }

}
