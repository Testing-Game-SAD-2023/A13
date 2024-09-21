package com.g2.Components;

import java.util.Map;

/**
 * Interfaccia comune per tutti i componenti di una pagina.
 */
public interface PageComponentInterface{

    /**
     * Fornisce i dati necessari per la view.
     * 
     * @return una mappa contenente i dati per il template.
     * una mappa è formata dalla key che è il nome del campo HTML 
     * e un oggetto che è il datto da inserire
     */
    Map<String, Object> getModel();

    /**
     * Esegue logica specifica del componente, se necessario.
     * Ad esempio, autenticazione, verifica di permessi, ecc.
     * 
     * @return true se l'esecuzione è avvenuta correttamente, altrimenti false.
     */
    boolean executeLogic();
}