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

// ServiceManager.java
package com.g2.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceManager {
    private static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    protected final Map<String, ServiceInterface> services = new HashMap<>();
    private final RestTemplate restTemplate;

    @Autowired
    public ServiceManager(ServiceConfig config, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // Carica i servizi definiti nella configurazione
        String[] enabledServices = config.getEnabled().split(",");
        Map<String, String> serviceMapping = config.getMapping();
        for (String serviceName : enabledServices) {
            serviceName = serviceName.trim();
            String className = serviceMapping.get(serviceName);
            if (className == null) {
                logger.warn("[SERVICE MANAGER] Nessuna classe associata a {}", serviceName);
                continue;
            }
            try {
                Class<?> clazz = Class.forName(className);
                if (ServiceInterface.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends ServiceInterface> serviceClass = (Class<? extends ServiceInterface>) clazz;
                    registerService(serviceName, serviceClass);
                } else {
                    logger.error("[SERVICE MANAGER] La classe {} non implementa ServiceInterface", className);
                }
            } catch (ClassNotFoundException e) {
                logger.error("[SERVICE MANAGER] Classe {} non trovata", className, e);
            }
        }
    }

    // Metodo per registrare un servizio
    private void registerService(String serviceName, Class<? extends ServiceInterface> serviceClass) {
        if (services.containsKey(serviceName)) {
            logger.warn("[SERVICE MANAGER] Servizio già registrato: {}", serviceName);
            return;
        }
        try {
            ServiceInterface service = serviceClass.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);
            services.put(serviceName, service);
            logger.info("[SERVICE MANAGER] Servizio registrato: {} -> {}", serviceName, serviceClass.getSimpleName());
        } catch (Exception e) {
            logger.error("[SERVICE MANAGER] Errore durante la registrazione di {}", serviceName, e);
        }
    }

    // Metodo per gestire le richieste
    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.error("[SERVICE MANAGER][HandleRequest] ServiceNotFound {}", serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        logger.info("[SERVICE MANAGER][HandleRequest]: {} - {}", serviceName, action);
        return service.handleRequest(action, params);
    }

    // Metodo per gestire le richieste in modo generico
    public <T> T handleRequest(String serviceName, String action, Class<T> returnType, Object... params) {
        // Ottenere la risposta dal servizio
        Object response = handleRequest(serviceName, action, params);
        // Verifica che la risposta sia compatibile con il tipo generico richiesto
        if (returnType.isInstance(response)) {
            return returnType.cast(response);
        } else {
            logger.error("[SERVICE MANAGER][HandleRequest] Tipo di ritorno incompatibile per il servizio: {}", serviceName);
            throw new ClassCastException("Il tipo di ritorno atteso è " + returnType.getName() +
                    ", ma è stato restituito " + (response != null ? response.getClass().getName() : "null"));
        }
    }

    protected ServiceInterface getServices(String key) {
        return services.get(key);
    }

}
