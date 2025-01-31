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

        // Registrazione di un'azione chiamata "RobotCoverage" con la definizione di
        // un'azione di servizio
        registerAction("RobotCoverage", new ServiceActionDefinition(
                // Viene passato un'operazione lambda che invoca il metodo RobotCoverage con 4
                // parametri di tipo String
                params -> RobotCoverage((String) params[0], (String) params[1], (String) params[2],
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

    /**
     * Metodo che compila il codice di test e di classe sotto test e ne esegue
     * l'analisi della copertura del codice.
     */
    private String RobotCoverage(String path, String underTestClassName, String underTestClassCode, String robotType) {
        final String endpoint = "/compile-and-analyze"; // Definisce l'endpoint per l'API di compilazione e analisi
        // della copertura del codice
        try{
            // Creazione del json con i parametri della richiesta POST
            JSONObject obj = new JSONObject();
            obj.put("testFilesDir", path);
            obj.put("underTestClassName", underTestClassName);
            obj.put("underTestClassCode", underTestClassCode);
            obj.put("robotType", robotType);

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