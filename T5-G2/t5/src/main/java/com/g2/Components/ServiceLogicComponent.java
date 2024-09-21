package com.g2.Components;

import java.util.Map;

import com.g2.Interfaces.ServiceManager;

/**
 * Componente che utilizza il ServiceManager per eseguire logica di controllo
 * o di business, senza restituire oggetti da inserire nel modello.
 */
public class ServiceLogicComponent extends PageComponentBase {

    private final String serviceName;
    private final String action;
    protected ServiceManager serviceManager;
    private final Object[] params;
    private boolean success;

    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param serviceName il nome del servizio su cui eseguire la logica.
     * @param action l'azione da eseguire sul servizio.
     * @param params eventuali parametri per l'azione del servizio.
     */
    public ServiceLogicComponent(ServiceManager serviceManager, String serviceName, String action, Object... params) {
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.params = params;
    }

    /**
     * Esegue la logica utilizzando il ServiceManager. In questo caso, il risultato
     * non viene inserito nel modello, ma memorizzato in un campo booleano `success`.
     *
     * @return true se la logica è stata eseguita con successo, altrimenti false.
     */
    @Override
    public boolean executeLogic() {
        try {
            // Esegue la logica richiesta tramite il ServiceManager
            Object result = serviceManager.handleRequest(serviceName, action, params);
            
            // Supponiamo che il risultato sia un booleano che indica il successo o il fallimento
            if (result instanceof Boolean aBoolean) {
                this.success = aBoolean;
                return success;
            } else {
                // Logica di fallback nel caso in cui il risultato non sia un booleano
                 System.err.println("Errore LogicComponent ");
                return false;
            }
        } catch (Exception e) {
            // Gestione dell'eccezione, ad esempio log dell'errore
            System.err.println("Errore durante l'esecuzione della logica: " + e.getMessage());
            this.success = false;
            return false;
        }
    }

    /**
     * Restituisce una mappa vuota poiché non ci sono dati da aggiungere al modello.
     * @return una mappa vuota.
     */
    @Override
    public Map<String, Object> getModel() {
        return super.getModel(); // Restituisce semplicemente una mappa vuota
    }

    /**
     * Restituisce il risultato della logica eseguita.
     * 
     * @return true se la logica è stata eseguita con successo, altrimenti false.
     */
    public boolean isSuccess() {
        return success;
    }

    //getter e setter
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
