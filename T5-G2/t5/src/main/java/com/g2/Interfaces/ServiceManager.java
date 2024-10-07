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
    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.error("[SERVICE MANAGER] ServiceNotFound "+ serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        try {
            logger.info("[SERVICE MANAGER] HandleRequest: " + serviceName + " - " + action);
            return service.handleRequest(action, params);
        } catch (Exception e) {
            logger.error("[SERVICE MANAGER]  Errore HandleRequest: " + serviceName + "Exception: " + e.getMessage());
            return null; //se c'è un errore nel servizio lo segnalo e poi introduco al livello successivo una gestione del null
        }
    }
}
