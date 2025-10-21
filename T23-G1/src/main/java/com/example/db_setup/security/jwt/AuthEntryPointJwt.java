package com.example.db_setup.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe che gestisce le risposte HTTP per gli utenti non autenticati.
 * Implementa {@link AuthenticationEntryPoint} e viene invocata da Spring Security
 * quando un client tenta di accedere a una risorsa protetta senza credenziali valide.
 * <p>
 * La risposta generata è in formato JSON e contiene un messaggio di errore localizzato
 * in base alla lingua richiesta dal client.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    /**
     * Costruttore della classe.
     *
     * @param messageSource  bean per la gestione dei messaggi internazionalizzati
     * @param localeResolver bean per determinare il locale del client
     */
    public AuthEntryPointJwt(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }


    /**
     * Metodo invocato da Spring Security quando si verifica un tentativo di accesso non autorizzato.
     * Imposta lo status HTTP 401 (Unauthorized) e restituisce un JSON con il messaggio di errore localizzato in base
     * alla lingua del client.
     *
     * @param request       richiesta HTTP in ingresso
     * @param response      risposta HTTP da inviare al client
     * @param authException eccezione generata dall'autenticazione fallita
     * @throws IOException in caso di errori durante la scrittura del body della risposta
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        logger.error("Unauthorized error: ", authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Locale locale = localeResolver.resolveLocale(request);

        final Map<String, Object> body = Map.of("errors", List.of(Map.of("field", "",
                "message", messageSource.getMessage("auth.badCredentials", null, locale))));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

}
