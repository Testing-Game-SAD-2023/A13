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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g2.Game.GameController;
import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;

@Service
public class ServiceManager {

    private final Map<String, ServiceInterface> services = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public ServiceManager(RestTemplate restTemplate) {
        /*
        *   Registrazione dinamica dei servizi, si occupa lui di instanziare in automatico 
        */ 
        registerService("T1", T1Service.class, restTemplate);
        registerService("T23", T23Service.class, restTemplate);
        registerService("T4", T4Service.class, restTemplate);
        registerService("T7", T7Service.class, restTemplate);
    }

    // Metodo helper per registrare i servizi
    private <T extends ServiceInterface> void registerService(String serviceName, Class<T> serviceClass, RestTemplate restTemplate) {
        if (!ServiceInterface.class.isAssignableFrom(serviceClass)) {
            logger.error("[SERVICE MANAGER] La Classe: " + serviceName + "deve implementare la ServiceInterface");
            throw new IllegalArgumentException("La classe: " + serviceName + " deve implementare la ServiceInterface");
        }
        //creo il servizio da registrare nel manager
        T service = createService(serviceClass, restTemplate);
        if (service != null) {
            services.put(serviceName, service);
        } else {
            logger.error("[SERVICE MANAGER] Errore nell'instanziare il servizio: " + serviceName + "è nullo");
            throw new IllegalArgumentException("Errore nell'instanziare il servizio: " + serviceName + "è nullo");
        }
    }

    // Metodo per la creazione di un servizio
    private <T extends ServiceInterface> T createService(Class<T> serviceClass, RestTemplate restTemplate) {
        try {
            T service = serviceClass.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);
            logger.info("[SERVICE MANAGER] \"ServiceCreation: " + serviceClass.getSimpleName());
            return service;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("[SERVICE MANAGER] Errore nella creazione del servizio: " + serviceClass.getName() + " Exception: " + e.getMessage());
            throw new RuntimeException("Impossibile creare l'istanza del servizio: " + serviceClass.getName(), e);
        }
    }

    // Metodo per gestire le richieste
    public Object handleRequest(String serviceName, String action, Object... params){
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.error("[SERVICE MANAGER] ServiceNotFound "+ serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        try {
            logger.info("[SERVICE MANAGER] HandleRequest: " + serviceName + " - " + action);
            return service.handleRequest(action, params);
        } catch (MissingParametersException | InvalidParameterTypeException e) {
            logger.error("[SERVICE MANAGER] Servizio: " + serviceName + " " + e.getMessage());
            return null; //se c'è un errore nel servizio lo segnalo e poi introduco al livello successivo una gestione del null
        }
    }

    public <T> T handleRequest(String serviceName, String action, Class<T> responseType, Object... params){
        Object obj = this.handleRequest(serviceName, action, params);
        if (responseType.isInstance(obj)) {
            return responseType.cast(obj); // Esegui il cast
        } else {
            throw new ClassCastException("[SERVICE MANAGER] Impossibile eseguire il cast dell'oggetto a " + responseType.getName());
        }
    }

    public <T> List<T> handleRequest(String serviceName, String action, ParameterizedTypeReference<List<T>> responseType, Class<T> clazz, Object... params) {
        Object obj = this.handleRequest(serviceName, action, params);
        // Verifica se obj è un'istanza di List
        if (obj instanceof List<?>) {
            List<?> rawList = (List<?>) obj; // Cast a List generica
            // Crea una nuova lista per il risultato
            List<T> resultList = new ArrayList<>();
            // Esegui il cast per ogni elemento della lista
            for (Object element : rawList) {
                if (clazz.isInstance(element)) {
                    resultList.add(clazz.cast(element));
                } else {
                    throw new ClassCastException("[SERVICE MANAGER] Impossibile eseguire il cast dell'elemento a " + clazz.getName());
                }
            }
            return resultList; // Restituisci la lista
        } else {
            throw new ClassCastException("[SERVICE MANAGER] Impossibile eseguire il cast dell'oggetto a List<" + responseType.getType().getTypeName() + ">");
        }
    }
    

}
