package com.example.db_setup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DbSetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbSetupApplication.class, args);
    }

    //MODIFICA GIULIO
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
    //FINE MODIFICA GIULIO
}
