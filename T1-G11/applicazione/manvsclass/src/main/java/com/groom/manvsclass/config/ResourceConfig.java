package com.groom.manvsclass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${images.storage-path}")
    private String imagesStoragePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // url base usato dal frontend
        String urlPath = "/t1/images/**";

        // directory del volume t0
        String location = "file:" + imagesStoragePath;

        registry.addResourceHandler(urlPath)
                .addResourceLocations(location);
    }
}