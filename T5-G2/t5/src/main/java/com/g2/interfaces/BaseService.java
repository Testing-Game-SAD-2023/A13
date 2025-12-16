package com.g2.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BaseService implements ServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
    protected final Map<String, ServiceActionDefinition> actions = new HashMap<>();

    protected void registerAction(String actionName, ServiceActionDefinition actionDefinition) {
        actions.put(actionName, actionDefinition);
    }

    @Override
    public Object handleRequest(String action, Object... params) {
        ServiceActionDefinition actionDefinition = actions.get(action);
        if (actionDefinition == null) {
            throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
        }
        // Esegui la funzione associata all'azione con i parametri validati
        return actionDefinition.execute(params);
    }
}
