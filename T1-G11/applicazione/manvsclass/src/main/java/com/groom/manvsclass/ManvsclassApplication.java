package com.groom.manvsclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//MODIFICA (13/02/2024) : Autenticazione token provenienti players
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.client.RestTemplate;
//FINE MODIFICA

@SpringBootApplication
public class ManvsclassApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManvsclassApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
