package com.groom.manvsclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disabilita CSRF
                .cors().and()     // Abilita CORS

                .authorizeRequests()
                // === 1. RISORSE STATICHE (CSS, JS, IMMAGINI) ===
                .antMatchers("/t1/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()

                // === 2. UPLOAD IMMAGINI (CRUCIALE PER IL TUO PROBLEMA) ===
                .antMatchers("/uploads/**").permitAll()

                // === 3. PAGINE FRONTEND (DASHBOARD, HINTS, ETC.) ===
                // Aggiungi qui tutte le pagine che vuoi vedere senza essere bloccato
                .antMatchers("/dashboard/**").permitAll()
                .antMatchers("/hints/**").permitAll()      // Sblocca /hints/main e /hints/upload
                .antMatchers("/opponents/**").permitAll()
                .antMatchers("/team/**").permitAll()
                .antMatchers("/scalata/**").permitAll()

                // === 4. AUTENTICAZIONE E SISTEMA ===
                .antMatchers("/auth/**", "/login", "/register", "/api/login").permitAll()
                .antMatchers("/error").permitAll() // Fondamentale per vedere i messaggi di errore

                // === 5. TUTTO IL RESTO RICHIEDE LOGIN ===
                .anyRequest().authenticated()
                .and()

                // Gestione sessione stateless (se usi JWT gestito manualmente)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // In produzione limita le origini, ma per sviluppo "*" va bene
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}