package com.g2.Components;

import java.util.HashMap;
import java.util.Map;

/**
 * Componente generico per inserire un oggetto nel modello.
 */
public class GenericObjectComponent {

    // Mappa per memorizzare i dati del modello
    protected Map<String, Object> Model;
    /**
     * Costruttore per creare un componente con un oggetto e una chiave
     * specificata.
     *
     * @param key la chiave per inserire l'oggetto nel modello.
     * @param object l'oggetto da inserire nel modello.
     */
    public GenericObjectComponent(String key, Object object) {
        this.Model.put(key, object);
    }

    /**
     * Restituisce il modello con l'oggetto inserito.
     *
     * @return una mappa contenente la chiave e l'oggetto inseriti.
     */
    public Map<String, Object> getModel() {
        if (Model.isEmpty()) {
            return null;
        } else {
            return this.Model;
        }
    }

    /**
     * Metodo per aggiornare l'oggetto.
     *
     * @param object il nuovo oggetto da inserire nel modello.
     */
    public void setObject(String key, Object object) {
        this.Model.put(key, object);
    }
}
