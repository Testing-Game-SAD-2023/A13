package com.g2.Components.HtmlComponents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.g2.Interfaces.ServiceManager;

public class HtmlFileComponent extends HtmlComponent {

    private String filePath;
    private String htmlContent;

    public HtmlFileComponent(ServiceManager serviceManager, String filePath) {
        super(serviceManager);
        this.filePath = filePath;
        this.htmlContent = loadHtmlFromFile(filePath);
    }

    // Metodo per leggere il file HTML dal percorso specificato
    private String loadHtmlFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readString(path); // Legge il contenuto del file
        } catch (IOException e) {
            e.printStackTrace();
            return "<!-- Errore durante il caricamento del file HTML -->";
        }
    }

    @Override
    public String render() {
        // Renderizza il contenuto HTML letto dal file
        return htmlContent;
    }

    @Override
    protected String getServiceData(String serviceName, String action, Object... params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServiceData'");
    }
}