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
        services.put("T1", createService(T1Service.class, restTemplate));
        services.put("T23", createService(T23Service.class, restTemplate));
        services.put("T4", createService(T4Service.class, restTemplate));
        services.put("T7", createService(T7Service.class, restTemplate));
    }

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

    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            logger.logMessagge("ServiceNotFound", serviceName);
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        logger.logMessagge("HandleRequest", serviceName, action);
        return service.handleRequest(action, params);
    }
}