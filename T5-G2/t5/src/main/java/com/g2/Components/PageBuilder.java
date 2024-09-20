package com.g2.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.g2.Interfaces.ServiceManager;

public class PageBuilder {
     private final ServiceManager serviceManager;
     private List<PageComponent> pageComponents;

    @Autowired
    public PageBuilder(ServiceManager serviceManager, List<PageComponent> pageComponents) {
        this.serviceManager  = serviceManager;
        this.pageComponents  = pageComponents;
    }

    public PageBuilder(ServiceManager serviceManager){
        this.serviceManager = serviceManager;
        this.pageComponents = new ArrayList<>(); 
    }

    // Metodo principale flusso per una richiesta di pagina
    public String handlePageRequest(Model model_html, String pageName, String jwt) {
        if (!authenticateUser(jwt)) {
            //se non sono autenticato viene renderizzato al login
            return "redirect:/login"; 
        }
        // Costruisci la mappa combinata dei dati dei componenti
        if(pageComponents != null){
            Map<String, Object> combinedModel  = build();
            model_html.addAllAttributes(combinedModel);
        }
        // Istanzia e gestisce i componenti della pagina
        // restituisco il nome del template da usare
        return pageName;
    }

    public void AttachComponents(List<PageComponent> pageComponents){
        this.pageComponents.addAll(pageComponents);
    }
    // Metodo per autenticare l'utente utilizzando ServiceManager
    private boolean authenticateUser(String jwt) {
        return (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
    }

    // Metodo per istanziare e costruire i componenti della pagina
    /* 
        Assicurati che i dati restituiti da getModel non sovrascrivano informazioni importanti.
        Se due componenti restituiscono dati con la stessa chiave, 
        l'ultimo componente che aggiorna la mappa sovrascriverà i dati precedenti.
    */
    private Map<String, Object> build() {
        Map<String, Object> combinedModel = new HashMap<>();

        for (PageComponent pageComponent : pageComponents) {
            combinedModel.putAll(pageComponent.getModel()); // Combina i dati
        }
        return combinedModel;
    }

    

}
