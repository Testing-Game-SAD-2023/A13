package com.g2.interfaces;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "services")
@Component
@Getter
@Setter
public class ServiceConfig {
    private String enabled;
    private Map<String, String> mapping;
}

