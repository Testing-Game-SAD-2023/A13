package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableRabbit
public class NotificationApplication {

    public static void main(String[] args) {

        SpringApplication.run(NotificationApplication.class, args);
    }
}
