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

package com.g2.interfaces;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class T7Service extends BaseService {

    /*
     * Per il test locale senza container docker usare:
     * BASE_URL = "http://127.0.0.1:8090"
     */
    private static final String BASE_URL = "http://api_gateway-controller:8090";
    private static final String SERVICE_PREFIX = "compile/jacoco";

    public T7Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL + "/" + SERVICE_PREFIX);

        // Registrazione di un'azione chiamata "CompileCoverage" con la definizione di
        // un'azione di servizio
        registerAction("CompileCoverage", new ServiceActionDefinition(
                // Viene passato un'operazione lambda che invoca il metodo CompileCoverage con 4
                // parametri di tipo String
                params -> compileCoverage((String) params[0], (String) params[1], (String) params[2],
                        (String) params[3]),
                // Specifica che l'azione accetta quattro parametri di tipo String
                String.class, String.class, String.class, String.class));

    }

    /**
     * Metodo che compila il codice di test e di classe sotto test e ne esegue
     * l'analisi della copertura del codice.
     */
    private JacocoCoverageDTO compileCoverage(String testingClassName, String testingClassCode,
                                              String underTestClassName, String underTestClassCode) {
        final String endpoint = "/coverage/player"; // Definisce l'endpoint per l'API di compilazione e analisi
        // della copertura del codice

        // Creazione del json con i parametri della richiesta POST
        JSONObject obj = new JSONObject();
        obj.put("testingClassName", testingClassName);
        obj.put("testingClassCode", testingClassCode);
        obj.put("underTestClassName", underTestClassName);
        obj.put("underTestClassCode", underTestClassCode);

        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Content-Type", "application/json");
        return callRestPost(endpoint, obj, null, customHeaders, JacocoCoverageDTO.class);
    }

}