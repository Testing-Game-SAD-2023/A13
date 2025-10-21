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

package com.g2.components;

import com.g2.interfaces.ServiceManager;

import java.util.Collections;
import java.util.Map;

/**
 * Componente che utilizza il ServiceManager per recuperare un oggetto da un
 * servizio e inserirlo nel modello.
 */
public class ServiceObjectComponent extends GenericObjectComponent {

    private final String serviceName;
    private final String action;
    private final ServiceManager serviceManager;
    private String modelKey;
    private Object[] params;

    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param serviceName    il nome del servizio da cui recuperare l'oggetto.
     * @param action         l'azione da eseguire sul servizio per ottenere l'oggetto.
     * @param modelKey       la chiave con cui inserire l'oggetto nel modello.
     * @param params         eventuali parametri per l'azione del servizio.
     */
    public ServiceObjectComponent(ServiceManager serviceManager, String modelKey, String serviceName, String action, Object... params) {
        super(null, null);
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.modelKey = modelKey;
        this.params = params;
    }

    public ServiceObjectComponent(ServiceManager serviceManager, String modelKey, String serviceName, String action) {
        super(null, null);
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.modelKey = modelKey;
        this.params = null;
    }

    /**
     * Esegue la logica per recuperare l'oggetto dal servizio e inserirlo nel
     * modello.
     */
    @Override
    public Map<String, Object> getModel() {
        try {
            // Recupera l'oggetto dal servizio utilizzando il ServiceManager
            Object object = serviceManager.handleRequest(serviceName, action, params);
            if (object != null) {
                // Inserisce l'oggetto nel modello con la chiave specificata
                this.model.put(modelKey, object);
            } else {
                // Logica in caso di oggetto nullo (può essere personalizzata)
                this.model.put(modelKey, "Object not found");
            }
            return this.model;
        } catch (Exception e) {
            // Gestione dell'eccezione, ad esempio log dell'errore
            return Collections.emptyMap();
        }
    }

    //getter e setter
    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
