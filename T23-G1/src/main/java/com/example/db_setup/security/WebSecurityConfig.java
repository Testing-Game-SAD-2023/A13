package com.example.db_setup.security;

import com.example.db_setup.security.jwt.AuthEntryPointJwt;
import com.example.db_setup.security.jwt.AuthTokenFilter;
import com.example.db_setup.security.service.AdminDetailsServiceImpl;
import com.example.db_setup.security.service.PlayerDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Configurazione sicurezza (Boot 2 / Spring Security 5).
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final PlayerDetailsServiceImpl playerDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(playerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(userAuthenticationProvider(), adminAuthenticationProvider()));
    }

    @Bean(name = "playerAuthManager")
    public AuthenticationManager playerAuthManager() {
        return authentication -> userAuthenticationProvider().authenticate(authentication);
    }

    @Bean(name = "adminAuthManager")
    public AuthenticationManager adminAuthManager() {
        return authentication -> adminAuthenticationProvider().authenticate(authentication);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // ✅ NO redirect HTML: usiamo entrypoint JWT per 401 + handler 403 REST
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
            .and()

            // ✅ disabilita meccanismi web (per evitare comportamenti tipo login form)
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()

            .authenticationProvider(userAuthenticationProvider())
            .authenticationProvider(adminAuthenticationProvider())

            .authorizeRequests()

                // ✅ Pubblico: counts (pagina friend)
                // Nota: AntPathRequestMatcher senza method => permette anche POST/PUT se esistessero.
                // Se vuoi SOLO GET, sotto ti dico come fare.
                .requestMatchers(new AntPathRequestMatcher("/social/counts")).permitAll()

                // 🔒 Tutto il resto sotto /social/** richiede JWT
                .requestMatchers(new AntPathRequestMatcher("/social/**")).authenticated()

                // Endpoints pubblici già esistenti
                .requestMatchers(
                    new AntPathRequestMatcher("/home"),
                    new AntPathRequestMatcher("/changeLanguage"),
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/register/success"),
                    new AntPathRequestMatcher("/change_password"),
                    new AntPathRequestMatcher("/reset_password"),
                    new AntPathRequestMatcher("/admin/**"),
                    new AntPathRequestMatcher("/auth/**"),
                    new AntPathRequestMatcher("/t23/**")
                ).permitAll()

                .anyRequest().authenticated()
            .and();

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
