package com.g2.Components;

import java.util.Map;

/**
 * Componente generico per inserire un oggetto nel modello.
 * @param <T> il tipo dell'oggetto da inserire nel modello.
 */
public class GenericObjectComponent<T> extends PageComponentBase {

    private String key;
    private T object;

    /**
     * Costruttore per creare un componente con un oggetto e una chiave specificata.
     * @param key la chiave per inserire l'oggetto nel modello.
     * @param object l'oggetto da inserire nel modello.
     */
    public GenericObjectComponent(String key, T object) {
        this.key = key;
        this.object = object;
    }

    /**
     * Metodo per eseguire logica specifica del componente.
     * In questo caso, semplicemente aggiunge l'oggetto al modello.
     * @return true se l'oggetto è stato aggiunto correttamente al modello.
     */
    @Override
    public boolean executeLogic() {
        if (key != null && object != null) {
            addModelAttribute(key, object);
            return true;
        }
        return false;
    }

    /**
     * Restituisce il modello con l'oggetto inserito.
     * @return una mappa contenente la chiave e l'oggetto inseriti.
     */
    @Override
    public Map<String, Object> getModel() {
        return super.getModel();
    }

    /**
     * Metodo per aggiornare l'oggetto.
     * @param object il nuovo oggetto da inserire nel modello.
     */
    public void setObject(T object) {
        this.object = object;
    }

    /**
     * Metodo per aggiornare la chiave.
     * @param key la nuova chiave per l'oggetto nel modello.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
