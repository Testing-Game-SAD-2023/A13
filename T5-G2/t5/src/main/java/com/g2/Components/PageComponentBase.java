package com.g2.Components;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe astratta che implementa l'interfaccia PageComponent e fornisce 
 * una base per i componenti della pagina.
 */
public abstract class PageComponentBase implements PageComponentInterface {

    // Mappa per memorizzare i dati del modello
    protected Map<String, Object> model = new HashMap<>();

    @Override
    public Map<String, Object> getModel() {
        return model;
    }

    /**
     * Metodo di default per eseguire logica specifica.
     * Le classi concrete possono sovrascrivere questo metodo.
     * 
     * @return true se l'esecuzione è avvenuta correttamente, altrimenti false.
     */
    @Override
    public boolean executeLogic() {
        // Logica di default, sovrascrivere nelle classi concrete
        return true;
    }

    /**
     * Aggiunge un dato al modello.
     * 
     * @param key la chiave del dato.
     * @param value il valore del dato.
     */
    protected void addModelAttribute(String key, Object value) {
        model.put(key, value);
    }
}