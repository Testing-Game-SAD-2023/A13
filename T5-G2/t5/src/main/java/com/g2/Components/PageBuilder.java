package com.g2.Components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.g2.Interfaces.ServiceManager;

public class PageBuilder {

    // Lista di componenti di pagina
    private final List<PageComponentBase> pageComponents;
        
    // Manager dei servizi, annotato come non utilizzato nel codice
    private final ServiceManager serviceManager;
    
    // Modello per il rendering della pagina
    private final Model model_html;
    
    // Nome della pagina (template) da utilizzare
    private final String PageName;

    //Auth
    private Boolean Auth = false;
    private ServiceLogicComponent AuthComponent;

    /**
     *  @param serviceManager Gestisce la chiamate REST ai vari task 
     *  @param PageName nome della pagina da implementare 
     *  @param pageComponents lista dei componenti che fanno parte della pagina
     */
    public PageBuilder(ServiceManager serviceManager,String PageName, Model model_html, List<PageComponentBase> pageComponents) {
        this.serviceManager  = serviceManager;
        this.pageComponents  = pageComponents;
        this.PageName   = PageName;
        this.model_html = model_html;
    }

    public PageBuilder(ServiceManager serviceManager,String PageName, Model model_html) {
        this.serviceManager  = serviceManager;
        this.pageComponents  = new ArrayList<>();
        this.PageName   = PageName;
        this.model_html = model_html;
    }
    
    // Metodo per eseguire la logica di tutti i componenti
    private void executeComponentsLogic() {
        for (PageComponentBase pageComponent : pageComponents) {
            if (pageComponent.executeLogic()) {
                System.out.println("Logica eseguita con successo per il componente: " + pageComponent.getClass().getSimpleName());
            } else {
                System.out.println("Logica fallita per il componente: " + pageComponent.getClass().getSimpleName());
            }
        }
    }

    // Metodo per costruire la mappa combinata dei dati per il modello
    /* 
        Assicurati che i dati restituiti da getModel non sovrascrivano informazioni importanti.
        Se due componenti restituiscono dati con la stessa chiave, 
        l'ultimo componente che aggiorna la mappa sovrascriverà i dati precedenti.
    */
    private Map<String, Object> buildModel() {
        Map<String, Object> combinedModel = new HashMap<>();
    
        for (PageComponentBase component : pageComponents) {
            Map<String, Object> model = component.getModel();
            model.forEach((key, value) -> {
                if (combinedModel.put(key, value) != null) {
                    System.err.println("[PAGEBULDER] individuate chiavi duplicate: " + key);
                    // Puoi decidere se lanciare un'eccezione o gestire la duplicazione come preferisci
                }
            });
        }
        return combinedModel;
    }

    // Questo metodo serve per attivare/disattivare l'autenticazione per la pagina
    public void SetAuth(String jwt){
        this.Auth = true;
        this.AuthComponent = new ServiceLogicComponent(this.serviceManager, "T23", "GetAuthenticated", jwt);
    }

    // Metodo principale flusso per una richiesta di pagina
    // Esegue la logica di ogni componente, poi elabora i dati da inserire nel template
    public String handlePageRequest() {
        if(Auth && !AuthComponent.executeLogic()){
            return "redirect:/login";
        }
        
        if (pageComponents != null || pageComponents.isEmpty()) {
            // Esegui la logica di tutti i componenti
            executeComponentsLogic();
            // Costruisci la mappa combinata dei dati dei componenti
            Map<String, Object> combinedModel = buildModel();
            model_html.addAllAttributes(combinedModel);
        }
        // Restituisco il nome del template da usare
        return this.PageName;
    }
    
    public List<PageComponentBase> getPageComponents() {
        return new ArrayList<>(pageComponents); // Ritorna una copia per evitare modifiche esterne
    }

    public void setPageComponents(List<PageComponentBase> pageComponents){
        this.pageComponents.addAll(pageComponents); 
    }
    public void setPageComponents(PageComponentBase... components) {
        this.pageComponents.addAll(Arrays.asList(components));
    }

}
