package com.example.EvoSuiteCompiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ServletComponentScan
public class EvoSuiteCompilerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvoSuiteCompilerApplication.class, args);
		System.out.println("[APP] Inizializzazione del Sistema.");
	}

}
