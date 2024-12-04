package com.groom.manvsclass.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class restconfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
