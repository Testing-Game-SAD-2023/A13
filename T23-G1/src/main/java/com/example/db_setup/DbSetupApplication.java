package com.example.db_setup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;



//cami
@SpringBootApplication
@EntityScan(basePackages = {"com.example.db_setup"})
public class DbSetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbSetupApplication.class, args);
    }
}
