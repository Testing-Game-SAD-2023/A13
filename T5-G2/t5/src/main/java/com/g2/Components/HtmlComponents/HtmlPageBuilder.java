package com.g2.Components.HtmlComponents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Interfaces.ServiceManager;

@Service
public class HtmlPageBuilder {

    //per richiamare servizi con cui riempire i componenti, da non usare esplicitamente qui
    private final ServiceManager serviceManager;
    //lista dei componenti all'interno della pagina
    private final List<HtmlComponent> page_components = new ArrayList<>();


    @Autowired
    public HtmlPageBuilder(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    //HtmlSection section1 = new HtmlSection(serviceManager, "Header");
    //HtmlSection section2 = new HtmlSection(serviceManager, "Content");
    
    public void createPage(List<HtmlComponent> components) {
        page_components.clear(); // Resetta i componenti esistenti
        page_components.addAll(components); // Aggiunge tutti i componenti dalla lista
    }

    public String getPageRender(){
        StringBuilder html = new StringBuilder("<html><body>");
        for (HtmlComponent component : page_components) {
            html.append(component.render());
        }
        html.append("</body></html>");
        return html.toString();
    }

}