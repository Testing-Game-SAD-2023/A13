package com.example.db_setup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableRabbit
public class DbSetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbSetupApplication.class, args);
    }
}
