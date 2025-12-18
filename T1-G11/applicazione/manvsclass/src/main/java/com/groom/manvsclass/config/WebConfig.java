package com.groom.manvsclass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${t1.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Normalizza e assicura lo slash finale
        String normalizedUploadDir = uploadDir.replace("\\", "/");
        if (!normalizedUploadDir.endsWith("/")) {
            normalizedUploadDir += "/";
        }

        String locationUri = "file:///" + normalizedUploadDir;

        // --- DEBUG LOG ---
        System.out.println("========================================");
        System.out.println("CONFIGURAZIONE RISORSE STATICHE ATTIVATA");
        System.out.println("Percorso originale: " + uploadDir);
        System.out.println("Mapping URL: /uploads/**");
        System.out.println("Punta a Location: " + locationUri);
        System.out.println("========================================");
        // -----------------

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(locationUri);
    }

}