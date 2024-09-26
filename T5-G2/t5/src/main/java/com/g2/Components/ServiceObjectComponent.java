package com.g2.Components;

import java.util.Map;

import com.g2.Interfaces.ServiceManager;

/**
 * Componente che utilizza il ServiceManager per recuperare un oggetto da un
 * servizio e inserirlo nel modello.
 */
public class ServiceObjectComponent extends GenericObjectComponent {

    private final String serviceName;
    private final String action;
    private String modelKey;
    private final ServiceManager serviceManager;
    private Object[] params;

    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param serviceName il nome del servizio da cui recuperare l'oggetto.
     * @param action l'azione da eseguire sul servizio per ottenere l'oggetto.
     * @param modelKey la chiave con cui inserire l'oggetto nel modello.
     * @param params eventuali parametri per l'azione del servizio.
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
    //getter e setter
    /**
     * Esegue la logica per recuperare l'oggetto dal servizio e inserirlo nel
     * modello.
     *
     */

    @Override
    public Map<String, Object> getModel() {
        if (Model.isEmpty() || Model == null) {
            setObject();
        } 
        return this.Model;
    } 

    public void setObject(){
        try {
            // Recupera l'oggetto dal servizio utilizzando il ServiceManager
            Object object = serviceManager.handleRequest(serviceName, action, params);
            if (object != null) {
                // Inserisce l'oggetto nel modello con la chiave specificata
                this.Model.put(modelKey, object);
            } else {
                // Logica in caso di oggetto nullo (può essere personalizzata)
                this.Model.put(modelKey, "Object not found");
            }
        } catch (Exception e) {
            // Gestione dell'eccezione, ad esempio log dell'errore
            System.err.println("Errore durante il recupero dell'oggetto dal servizio: " + e.getMessage());
        }
    }


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
