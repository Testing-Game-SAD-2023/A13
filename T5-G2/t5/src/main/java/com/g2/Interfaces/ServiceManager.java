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

    // Costruttore per configurare e istanziare i servizi con RestTemplate
    @Autowired
    public ServiceManager(RestTemplate restTemplate) {
        services.put("T1", createService(T1Service.class, restTemplate));
        services.put("T23", createService(T23Service.class, restTemplate));
        services.put("T4", createService(T4Service.class, restTemplate));
        services.put("T7", createService(T7Service.class, restTemplate));
        // Aggiungi altri servizi se necessario
    }

    // Metodo per creare l'istanza del servizio utilizzando il costruttore con RestTemplate
    private <T extends ServiceInterface> T createService(Class<T> serviceClass, RestTemplate restTemplate) {
        try {
            return serviceClass.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException("Impossibile creare l'istanza del servizio: " + serviceClass.getName(), e);
        }
    }

    // Metodo per gestire le richieste
    public Object handleRequest(String serviceName, String action, Object... params) {
        ServiceInterface service = services.get(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("Servizio non trovato: " + serviceName);
        }
        return service.handleRequest(action, params);
    }
}