package com.g2.Components.HtmlComponents;
import com.g2.Interfaces.ServiceManager;


//classe astratta che definisce un componente html, unendo componenti posso definire poi una pagina 
public abstract class HtmlComponent {
    protected final ServiceManager serviceManager;

    public HtmlComponent(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public abstract String render(); // Metodo per renderizzare il componente HTML
    protected abstract String getServiceData(String serviceName, String action, Object... params);
}