// ServiceManager.java
package com.g2.Interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceManager {

    private final Map<String, ServiceInterface> services = new HashMap<>();
    private final ServiceManagerLogger logger;

    @Autowired
    public ServiceManager(RestTemplate restTemplate) {
        this.logger = new ServiceManagerLogger();
        // Registrazione dinamica dei servizi
        registerService("T1", T1Service.class, restTemplate);
        registerService("T23", T23Service.class, restTemplate);
        registerService("T4", T4Service.class, restTemplate);
        registerService("T7", T7Service.class, restTemplate);
    }

    // Metodo helper per registrare i servizi
    private <T extends ServiceInterface> void registerService(String serviceName, Class<T> serviceClass, RestTemplate restTemplate) {
        T service = createService(serviceClass, restTemplate);
        if (service != null) {
            services.put(serviceName, service);
        }
    }

    // Metodo per la creazione di un servizio
    private <T extends ServiceInterface> T createService(Class<T> serviceClass, RestTemplate restTemplate) {
        try {
            T service = serviceClass.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);
            logger.logMessagge("ServiceCreation", serviceClass.getSimpleName());
            return service;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            logger.logMessagge("Errore nella creazione del servizio: " + serviceClass.getName(), e);
            throw new RuntimeException("Impossibile creare l'istanza del servizio: " + serviceClass.getName(), e);
        }
    }

    // Metodo per gestire le richieste
    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.logMessagge("ServiceNotFound", serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        try {
            logger.logMessagge("HandleRequest", serviceName, action);
            return service.handleRequest(action, params);
        } catch (Exception e) {
            logger.logMessagge("[HANDLE REQUEST GAY]" + serviceName + ": ", e);
            return null; //se c'è un errore nel servizio lo segnalo e poi introduco al livello successivo una gestione del null
        }
    }
}
