package com.g2.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security override for T5.
 *
 * Problem fixed:
 *  - /social/** endpoints are called via fetch/XHR from the UI.
 *  - A "web-style" AuthenticationEntryPoint was redirecting to /login,
 *    causing an infinite 302 loop (wget: too many redirections) and the UI failing.
 *
 * Solution:
 *  - For API-like routes (/social/**) we disable redirect-based login and return
 *    standard HTTP errors instead (401/403), keeping the REST contract for the frontend.
 *  - We also keep T5 pages accessible as before (controllers already do auth checks
 *    via the jwt cookie and redirect when needed).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * API filter chain: never redirect to HTML login.
     */
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
            // Apply only to the social proxy endpoints exposed by T5
            .securityMatcher("/social/**")

            // API calls: no sessions, no CSRF
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())

            // IMPORTANT: CORS must be enabled here too, because this chain handles /social/**
            .cors(Customizer.withDefaults())

            // Controller methods handle auth via jwt cookie (requireAuth()),
            // so Spring Security must not block/redirect.
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

            // Never send 302 to /login for API calls
            .exceptionHandling(ex -> ex.authenticationEntryPoint(
                (req, res, authEx) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            ))

            // Disable web auth mechanisms for these endpoints
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());

        return http.build();
    }

    /**
     * Default filter chain: keep the app usable even if Spring Security is on the classpath.
     *
     * Note: controllers in T5 already redirect to /login when needed based on jwt,
     * so we permit requests and avoid double-handling authentication at framework level.
     */
    @Bean
    @Order(2)
    SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable())
            .cors(Customizer.withDefaults());

        return http.build();
    }
}
