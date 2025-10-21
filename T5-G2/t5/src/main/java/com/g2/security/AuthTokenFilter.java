package com.g2.security;

import com.g2.interfaces.ServiceManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testrobotchallenge.commons.models.user.Role.PLAYER;

/**
 * Filtro di autenticazione basato su JWT.
 * <p>
 * Intercetta ogni richiesta HTTP, controlla la presenza del cookie {@code jwt} e ne valida la correttezza tramite
 * chiamata API al servizio T23. Se il JWT manca ma è presente un {@code refresh token}, tenta di rigenerare
 * il JWT contattando T23. In caso contrario, reindirizza alla pagina di login.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger customLogger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final ServiceManager serviceManager;
    private final RestTemplate restTemplate = new RestTemplate();

    public AuthTokenFilter(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    /**
     * Esegue il filtro di autenticazione su ogni richiesta.
     *
     * @param request  la richiesta HTTP in ingresso
     * @param response la risposta HTTP in uscita
     * @param chain    la catena dei filtri
     * @throws ServletException in caso di errore lato servlet
     * @throws IOException      in caso di errore di I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        customLogger.info("[AuthTokenFilter] Authenticating request {} {}", request.getMethod(), request.getRequestURI());

        // Estraggo JWT e refresh token dalla richiesta
        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");
        Cookie refreshCookie = WebUtils.getCookie(request, "jwt-refresh");

        String jwt = jwtCookie != null ? jwtCookie.getValue() : null;
        String refreshToken = refreshCookie != null ? refreshCookie.getValue() : null;

        try {
            /*
             * Se il jwt è nullo, ma il refresh token è presente, lancio tryRefreshAndContinue() per richiedere un
             * nuovo jwt per l'utente. Altrimenti redirecto l'utente alaa pagina di login.
             */
            if (jwt == null) {
                if (refreshCookie != null) {
                    customLogger.info("JWT missing. Attempting to refresh using refresh token...");
                    tryRefreshAndContinue(refreshToken, response, chain, request);
                    return;
                } else {
                    customLogger.info("JWT and refresh token missing. Redirecting to login.");
                    redirectToLogin(response, "unauthorized");
                    return;
                }
            }

            /*
             * Se ho ricevuto un jwt, lo valido. Se non risulta valido o non è associato al ruolo Player, redirecto al login
             */
            JwtValidationResponseDTO validation = (JwtValidationResponseDTO) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
            if (!validation.isValid() || !validation.getRole().equals(PLAYER)) {
                customLogger.info("[AuthTokenFilter] Invalid token or insufficient permissions.");
                redirectToLogin(response, "unauthorized");
                return;
            }

            /*
             * Salvo il jwt per essere usato nell'autenticazione delle chiamate interne e procedo a soddisfare la richiesta
             */
            customLogger.info("[AuthTokenFilter] Validated token for role PLAYER");
            JwtRequestContext.setJwtToken("%s=%s".formatted(jwtCookie.getName(), jwt));
            customLogger.debug("[AuthTokenFilter] JWT saved in thread context");

            chain.doFilter(request, response);

        } finally {
            /*
             * Al termine della richiesta, a prescindere che sia andata a buon fine o meno, rimuovo il jwt salvato
             */
            JwtRequestContext.clear();
        }
    }

    /**
     * Chiede a T23 di generare un nuovo JWT utilizzando il refresh token.
     * Se la rigenerazione riesce, imposta il nuovo cookie e prosegue la catena di filtri.
     * In caso contrario, reindirizza alla pagina di login.
     *
     * @param refreshToken il refresh token presente nei cookie della richiesta HTTP catturata
     * @param response     la risposta HTTP a cui aggiungere il nuovo cookie
     * @param chain        la catena dei filtri
     * @param request      la richiesta corrente
     * @throws IOException in caso di errore durante il redirect
     */
    private void tryRefreshAndContinue(String refreshToken, HttpServletResponse response, FilterChain chain, HttpServletRequest request)
            throws IOException {

        String cookies = callRefreshJwtToken(refreshToken);
        if (cookies == null) {
            customLogger.warn("Refresh token failed: JWT is null");
            redirectToLogin(response, "expired");
            return;
        }

        try {
            Map<String, String> cookieAttrs = parseCookieAttributes(cookies);
            String newJwt = cookieAttrs.get("jwt");
            int maxAge = Integer.parseInt(cookieAttrs.getOrDefault("max-age", "3600"));
            String path = cookieAttrs.getOrDefault("path", "/");

            ResponseCookie newJwtCookie = ResponseCookie.from("jwt", newJwt)
                    .path(path)
                    .maxAge(maxAge)
                    .httpOnly(true)
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, newJwtCookie.toString());
            JwtRequestContext.setJwtToken(newJwtCookie.toString());

            customLogger.info("JWT refreshed and saved in context. Proceeding with filter chain.");
            chain.doFilter(request, response);

        } catch (Exception ex) {
            customLogger.warn("Refresh token failed: {}", ex.getMessage());
            redirectToLogin(response, "expired");
        }
    }

    /**
     * Reindirizza l’utente alla pagina di login con un parametro che descrive il motivo
     * (ad esempio {@code unauthorized} o {@code expired}).
     *
     * @param response la risposta HTTP
     * @param reason   il motivo del redirect
     * @throws IOException se il redirect fallisce
     */
    private void redirectToLogin(HttpServletResponse response, String reason) throws IOException {
        response.sendRedirect("/login?" + reason + "=true");
    }

    /**
     * Effettua una chiamata al servizio T23 per ottenere un nuovo JWT a partire
     * dal refresh token.
     *
     * @param refreshToken il refresh token ricevuto nella richiesta
     * @return il valore del nuovo cookie JWT oppure {@code null} se il refresh fallisce
     */
    private String callRefreshJwtToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt-refresh=" + refreshToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "http://api_gateway-controller:8090/userService/auth/refreshToken",
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);

            if (cookies != null) {
                for (String cookie : cookies) {
                    if (cookie.startsWith("jwt=")) {
                        return cookie;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Utility per parsare l'header {@code Set-Cookie} e restituire gli attributi sotto forma di mappa.
     *
     * @param setCookieHeader l'header {@code Set-Cookie}
     * @return la mappa <nome, valore> dei cookie
     */
    private Map<String, String> parseCookieAttributes(String setCookieHeader) {
        Map<String, String> attributes = new HashMap<>();
        String[] parts = setCookieHeader.split(";");
        for (String part : parts) {
            String[] keyValue = part.trim().split("=", 2);
            if (keyValue.length == 2) {
                attributes.put(keyValue[0].toLowerCase(), keyValue[1]);
            } else {
                attributes.put(keyValue[0].toLowerCase(), "true");
            }
        }
        return attributes;
    }


}
