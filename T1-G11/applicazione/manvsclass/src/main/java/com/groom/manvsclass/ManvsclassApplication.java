package com.groom.manvsclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

//MODIFICA (13/02/2024) : Autenticazione token provenienti players
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.client.RestTemplate;
//FINE MODIFICA

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.groom.manvsclass.model.repository")
@EntityScan(basePackages = "com.groom.manvsclass.model.entity")
public class ManvsclassApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManvsclassApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
