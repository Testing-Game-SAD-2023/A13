package com.g2.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g2.Interfaces.ServiceManager;


public class TableComponent extends PageComponent {

    private final ServiceManager serviceManager;
    private final String serviceName = "T23";
    private final String action      = "GetUsers";
    private final String thymeleafField;

    // Costruttore
    public TableComponent(ServiceManager serviceManager, String thymeleafField) {
        this.serviceManager = serviceManager;
        this.thymeleafField = thymeleafField;
    }

    @Override
    public Map<String, Object> getModel() {
        Map<String, Object> model = new HashMap<>();

        // Esegue una chiamata al ServiceManager per ottenere la lista della leaderboard
        Object result = serviceManager.handleRequest(serviceName, action);

        // Se il risultato è una lista, la converte e la mette nella mappa
        if (result instanceof List) {
            model.put(thymeleafField, result);
        } else {
            model.put(thymeleafField, List.of()); // Lista vuota se non ci sono dati disponibili
        }

        return model;
    }
}