package com.g2.Components;

import java.util.HashMap;
import java.util.Map;

import com.g2.Interfaces.ServiceManager;

public class TextComponent extends PageComponent {

    private final ServiceManager serviceManager;
    private final String serviceName;           // Nome del servizio da chiamare
    private final String action;                // Azione da eseguire con il service manager
    private final String thymeleafField;        // Il campo Thymeleaf a cui associare il testo
    private final Object[] params;

    // Costruttore
    public TextComponent(ServiceManager serviceManager, String serviceName, String action, String thymeleafField) {
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.thymeleafField = thymeleafField;
        this.params = null;
    }

    public TextComponent(ServiceManager serviceManager, String serviceName, String action, String thymeleafField, Object ...params) {
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.thymeleafField = thymeleafField;
        this.params = params;
    }

    @Override
    public Map<String, Object> getModel() {
        Map<String, Object> model = new HashMap<>();

        // Esegue una chiamata al ServiceManager per ottenere i dati
        Object result = serviceManager.handleRequest(serviceName, action, params);

        // Converte il risultato in una stringa e lo associa al campo Thymeleaf
        if (result != null) {
            model.put(thymeleafField, result);
        } else {
            model.put(thymeleafField, "Nessun dato disponibile");
        }

        return model;
    }
}