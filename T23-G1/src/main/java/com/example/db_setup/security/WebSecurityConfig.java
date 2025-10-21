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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;


/**
 * Classe di configurazione della sicurezza del sistema.
 * <p>
 * Definisce:
 * <ul>
 *     <li>Autenticazione per utenti e amministratori.</li>
 *     <li>Gestione degli errori di autenticazione tramite {@link AuthEntryPointJwt}.</li>
 *     <li>Filtri JWT tramite {@link AuthTokenFilter}.</li>
 *     <li>Regole di accesso alle risorse.</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final PlayerDetailsServiceImpl playerDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    /**
     * Configura l'authentication provider per i giocatori.
     *
     * @return un {@link DaoAuthenticationProvider} per gioatori.
     */
    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(playerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura l'authentication provider per gli amministratori.
     *
     * @return un {@link DaoAuthenticationProvider} per admin.
     */
    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Definisce il gestore di autenticazione primario che utilizza entrambi i provider
     * (giocatore e admin).
     *
     * @return {@link AuthenticationManager}
     */
    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(
                List.of(userAuthenticationProvider(), adminAuthenticationProvider())
        );
    }

    /**
     * Definisce l'AuthenticationManager dedicato ai giocatori.
     *
     * @return {@link AuthenticationManager} configurato per utenti.
     */
    @Bean(name = "playerAuthManager")
    public AuthenticationManager playerAuthManager() {
        return authentication -> userAuthenticationProvider().authenticate(authentication);
    }

    /**
     * Definisce l'AuthenticationManager dedicato agli admin.
     *
     * @return {@link AuthenticationManager} configurato per admin.
     */
    @Bean(name = "adminAuthManager")
    public AuthenticationManager adminAuthManager() {
        return authentication -> adminAuthenticationProvider().authenticate(authentication);
    }

    /**
     * Definisce l'algoritmo di hashing per le password.
     *
     * @return {@link PasswordEncoder} con BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Configura la catena di filtri della sicurezza:
     * <ul>
     *     <li>Disabilita CSRF.</li>
     *     <li>Gestisce le eccezioni con {@link AuthEntryPointJwt}.</li>
     *     <li>Definisce la sessione come STATELESS.</li>
     *     <li>Registra i due provider (utente e admin).</li>
     *     <li>Definisce quali endpoint sono pubblici e quali protetti da autenticazione.</li>
     *     <li>Aggiunge il filtro JWT prima del {@link UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     *
     * @param http configurazione di {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(userAuthenticationProvider())
                .authenticationProvider(adminAuthenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints pubblici
                        .requestMatchers(
                                // player views
                                new AntPathRequestMatcher("/home"),
                                new AntPathRequestMatcher("/changeLanguage"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/register"),
                                new AntPathRequestMatcher("/register/success"),
                                new AntPathRequestMatcher("/change_password"),
                                new AntPathRequestMatcher("/reset_password"),
                                new AntPathRequestMatcher("/admin/**"), // admin views
                                new AntPathRequestMatcher("/auth/**"),  // auth api
                                new AntPathRequestMatcher("/t23/**")  // file statici
                        ).permitAll()
                        // Qualsiasi altra richiesta richiede autenticazione
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}