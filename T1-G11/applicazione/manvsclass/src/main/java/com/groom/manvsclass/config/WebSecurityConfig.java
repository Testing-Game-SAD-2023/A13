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
                .csrf().disable()
                .cors().and()

                .authorizeRequests()
                // === 1. SWAGGER & OPENAPI (AGGIUNTO PER RISOLVERE ERRORE 403) ===
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // === 2. RISORSE STATICHE ===
                .antMatchers("/t1/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()

                // === 3. UPLOAD IMMAGINI (TaskR2) ===
                .antMatchers("/uploads/**").permitAll()

                // === 4. PAGINE FRONTEND ===
                .antMatchers("/dashboard/**").permitAll()
                .antMatchers("/hints/**").permitAll()
                .antMatchers("/opponents/**").permitAll()
                .antMatchers("/team/**").permitAll()
                .antMatchers("/scalata/**").permitAll()

                // === 5. AUTENTICAZIONE E SISTEMA ===
                .antMatchers("/auth/**", "/login", "/register", "/api/login").permitAll()
                .antMatchers("/error").permitAll()

                // === 6. PROTEZIONE API DI BUSINESS ===
                // Gli endpoint delle API (non le pagine) dovrebbero restare protetti
                .anyRequest().authenticated()
                .and()

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