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
package com.g2.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.interfaces.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class PageBuilder {

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(PageBuilder.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // Lista di componenti di pagina
    private final List<GenericLogicComponent> logicComponents;
    private final List<GenericObjectComponent> objectComponents;
    /*
     * Mappa che associa codici di errore a pagine di errore
     * Così si può personalizzare il comportamento della pagina
     * in termini di redirect
     */
    private final Map<String, String> errorPageMap = new HashMap<>();
    // Manager dei servizi,
    private final ServiceManager serviceManager;
    // Modello per il rendering della pagina
    private final Model modelHtml;
    // Nome della pagina (template) da utilizzare
    private final String pageName;
    //Lista codici d'errore ottenuti
    private List<String> errorCode;
    // Aggiunto per memorizzare l'utente dal JWT
    private Long userId;
    private String jwt;

    //COSTRUTTORI 

    /**
     * @param serviceManager Gestisce la chiamate REST ai vari task
     * @param pageName       nome della pagina da implementare
     * @param pageComponents lista dei componenti che fanno parte della pagina
     */
    public PageBuilder(ServiceManager serviceManager,
                       String pageName,
                       Model modelHtml,
                       List<GenericObjectComponent> objectComponents,
                       List<GenericLogicComponent> logicComponents) {
        this.serviceManager = serviceManager;
        this.objectComponents = objectComponents;
        this.logicComponents = logicComponents;
        this.pageName = pageName;
        this.modelHtml = modelHtml;
        setStandardErrorPage();
        logger.info("[PageBuilder] Builder costruito con successo");
    }

    public PageBuilder(ServiceManager serviceManager, String pageName, Model modelHtml) {
        this.serviceManager = serviceManager;
        this.objectComponents = new ArrayList<>();
        this.logicComponents = new ArrayList<>();
        this.pageName = pageName;
        this.modelHtml = modelHtml;
        setStandardErrorPage();
        logger.info("[PAGEBULDER] Builder costruito con successo");
    }

    // COSTRUTTORE CON JWT
    public PageBuilder(ServiceManager serviceManager, String pageName, Model modelHtml, String jwt) {
        this(serviceManager, pageName, modelHtml);
        this.userId = extractUserId(jwt);
        this.jwt = jwt;
        logger.info("[PageBuilder] Costruttore con JWT completato per userId: {}", this.userId);
    }

    // COSTRUTTORE CON JWT + COMPONENTI
    public PageBuilder(ServiceManager serviceManager, String pageName, Model modelHtml,
                       List<GenericObjectComponent> objectComponents,
                       List<GenericLogicComponent> logicComponents, String jwt) {
        this(serviceManager, pageName, modelHtml, objectComponents, logicComponents);
        this.userId = extractUserId(jwt);
        this.jwt = jwt;
        logger.info("[PageBuilder] Costruttore con JWT e componenti completato per userId: {}", this.userId);
    }

    // METODO PER ESTRARRE USER ID DAL JWT
    private static Long extractUserId(String jwt) {
        try {
            if (jwt == null || jwt.split("\\.").length < 2) {
                throw new IllegalArgumentException("JWT non valido: formato errato");
            }
            byte[] decodedBytes = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            String decodedJson = new String(decodedBytes, StandardCharsets.UTF_8);
            Map<String, Object> payload = OBJECT_MAPPER.readValue(decodedJson, Map.class);
            Object userId = payload.get("userId");
            if (userId == null) {
                throw new IllegalArgumentException("JWT non valido: userId mancante");
            }
            return Long.parseLong(userId.toString());
        } catch (Exception e) {
            logger.error("[PageBuilder] Errore nella decodifica del JWT: {}", e.getMessage());
            return null;
        }
    }

    //HANDLE PAGE REQUEST
    // Metodo per eseguire la logica di tutti i componenti
    private List<String> executeComponentsLogic() {
        // Lista per raccogliere eventuali errori
        List<String> errorCodes = new ArrayList<>();
        for (GenericLogicComponent Component : logicComponents) {
            if (!Component.executeLogic()) {
                errorCodes.add(Component.getErrorCode()); // Aggiunge il codice d'errore alla lista
                logger.error("[PAGEBULDER][executeComponentsLogic] Logica fallita per il componente: " + Component.getClass().getSimpleName());
            }
        }
        logger.info("[PAGEBULDER][executeComponentsLogic] Lista error code: {}", errorCodes);
        return errorCodes;
    }

    // Metodo per costruire la mappa combinata dei dati per il modello
    /*
        Assicurati che i dati restituiti da getModel non sovrascrivano informazioni importanti.
        Se due componenti restituiscono dati con la stessa chiave,
        l'ultimo componente che aggiorna la mappa sovrascriverà i dati precedenti.
     */
    private Map<String, Object> buildModel() {
        Map<String, Object> combinedModel = new HashMap<>();
        for (GenericObjectComponent component : objectComponents) {
            Map<String, Object> model = component.getModel();
            model.forEach((key, value) -> {
                if (combinedModel.put(key, value) != null) {
                    // Puoi decidere se lanciare un'eccezione o gestire la duplicazione come preferisci
                    throw new IllegalStateException("[PAGEBULDER][buildModel] individuate chiavi duplicate: " + key);
                }
            });
        }
        return combinedModel;
    }

    // Metodo principale flusso per una richiesta di pagina
    // Esegue la logica di ogni componente, poi elabora i dati da inserire nel template
    public String handlePageRequest() {
        String returnPageError = null;
        if (logicComponents != null && !logicComponents.isEmpty()) {
            // Esegui la logica di tutti i componenti
            this.errorCode = executeComponentsLogic();
            // Gestisco le situazioni d'errore
            returnPageError = executeError(errorCode);
        }
        // Restituisco il nome del template da usare
        if (returnPageError != null) {
            return returnPageError;
        }
        if (objectComponents != null && !objectComponents.isEmpty()) {
            // Costruisci la mappa combinata dei dati dei componenti
            Map<String, Object> combinedModel = buildModel();
            modelHtml.addAllAttributes(combinedModel);
        }
        return this.pageName;
    }

    //COMPONENTI
    // Questo metodo serve per attivare l'autenticazione per la pagina
    public void setAuth(String jwt) {
        if (serviceManager != null) {
            setLogicComponents(new AuthComponent(serviceManager, jwt));
        }
    }

    public void setAuth() {
        if (serviceManager != null) {
            setLogicComponents(new AuthComponent(serviceManager, jwt));
        }
    }

    //CODICI D'ERRORE 
    // Metodo per permettere la personalizzazione della mappa
    // il metodo put di una HashMap sovrascrive il valore associato a
    // una chiave esistente se la chiave è già presente nella mappa.
    public void setErrorPage(String errorCode, String pageName) {
        errorPageMap.put(errorCode, pageName);
    }

    //overload nel caso in cui l'utente fornisce una lista intera 
    public void setErrorPage(Map<String, String> userErrorPageMap) {
        errorPageMap.putAll(userErrorPageMap);
    }

    //Qui setto il comportamento Standard agli errori 
    private void setStandardErrorPage() {
        errorPageMap.put("Auth_error", "redirect:/login");
        errorPageMap.put("default", "redirect:/error");
    }

    // Metodo per ottenere i messaggi d'errore basati sui codici d'errore
    private String executeError(List<String> errorCodes) {
        if (errorCodes != null && !errorCodes.isEmpty()) {
            for (String code : errorCodes) {
                // per ora si ferma al primo errore, ma qui posso implementare ogni tipo di logica 
                // anche in base alla combinazione o if-else
                return errorPageMap.getOrDefault(code, errorPageMap.get("default"));
            }
        }
        return null;
    }

    //GET E SET 
    public List<GenericLogicComponent> getLogicComponents() {
        return new ArrayList<>(logicComponents); // Ritorna una copia per evitare modifiche esterne
    }

    public void setLogicComponents(List<GenericLogicComponent> pageComponents) {
        this.logicComponents.addAll(pageComponents);
    }

    public void setLogicComponents(GenericLogicComponent... components) {
        this.logicComponents.addAll(Arrays.asList(components));
    }

    public List<GenericObjectComponent> getObjectComponents() {
        return new ArrayList<>(objectComponents); // Ritorna una copia per evitare modifiche esterne
    }

    public void setObjectComponents(List<GenericObjectComponent> pageComponents) {
        this.objectComponents.addAll(pageComponents);
    }

    public void setObjectComponents(GenericObjectComponent... components) {
        this.objectComponents.addAll(Arrays.asList(components));
    }

    public List<String> getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getErrorPageMap() {
        return errorPageMap;
    }

    public Long getUserId() {
        return this.userId;
    }
}
