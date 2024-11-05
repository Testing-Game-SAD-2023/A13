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
package com.g2.Interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g2.Game.GameController;

@Service
public class ServiceManager {
    protected final Map<String, ServiceInterface> services = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public ServiceManager(RestTemplate restTemplate) {
        /*
         * Registrazione dinamica dei servizi, si occupa lui di instanziare in
         * automatico
         */
        registerService("T1", T1Service.class, restTemplate);
        registerService("T23", T23Service.class, restTemplate);
        registerService("T4", T4Service.class, restTemplate);
        registerService("T7", T7Service.class, restTemplate);
    }

    // Metodo helper per registrare i servizi
    protected <T extends ServiceInterface> void registerService(String serviceName, Class<T> serviceClass, RestTemplate restTemplate) {
        if (serviceClass == null){
            throw new IllegalArgumentException("[SERVICE MANAGER] serviceClass Nullo !");
        }
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del servizio non può essere nullo o vuoto.");
        }
        if (restTemplate == null) {
            throw new IllegalArgumentException("[SERVICE MANAGER] RestTemplate Nullo !");
        }
        if (services.containsKey(serviceName)) {
            logger.error("[SERVICE MANAGER] Il servizio: " + serviceName + " è già registrato.");
            throw new IllegalArgumentException("Il servizio: " + serviceName + " è già registrato.");
        }
        // Creo il servizio da registrare nel manager
        T service = createService(serviceClass, restTemplate);
        if (service != null) {
            services.put(serviceName, service);
            logger.info("[SERVICE MANAGER] Servizio registrato: " + serviceName);
        }
    }

    // Metodo per la creazione di un servizio
    protected <T extends ServiceInterface> T createService(Class<T> serviceClass, RestTemplate restTemplate) {
        try {
            T service = serviceClass.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);
            logger.info("[SERVICE MANAGER] \"ServiceCreation: " + serviceClass.getSimpleName());
            return service;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException  e) {
            logger.error("[SERVICE MANAGER] Errore nella creazione del servizio: " + serviceClass.getName() + " Exception: " + e.getMessage());
            throw new RuntimeException("Impossibile creare l'istanza del servizio: " + serviceClass.getName(), e);
        }
    }

    // Metodo per gestire le richieste
    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.error("[SERVICE MANAGER][HandleRequest] ServiceNotFound " + serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        logger.info("[SERVICE MANAGER][HandleRequest]: " + serviceName + " - " + action);
        return service.handleRequest(action, params);
    }

    protected ServiceInterface getServices(String key) {
        return services.get(key);
    }

}
