package com.example.db_setup.interceptor;

import com.example.db_setup.security.jwt.JwtProvider;
import com.example.db_setup.service.AuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe di configurazione che implementa WebMvcConfigurer di Spring MVC per la registrazione degli interceptor.
 * <p>
 * Aggiunge {@link AuthenticatedUserInterceptor} per intercettare
 * tutte le richieste HTTP e gestire i redirect.
 * </p>
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public InterceptorConfig(JwtProvider jwtProvider, AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    /**
     * Sovrascrive il metodo per registrare i nuovi interceptor.
     *
     * @param registry il registro degli interceptor di Spring
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticatedUserInterceptor(jwtProvider, authService));
    }
}
