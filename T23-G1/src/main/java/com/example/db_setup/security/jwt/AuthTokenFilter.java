package com.example.db_setup.security.jwt;

import com.example.db_setup.security.AuthenticationPropertiesConfig;
import com.example.db_setup.security.service.AdminDetailsServiceImpl;
import com.example.db_setup.security.service.PlayerDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import testrobotchallenge.commons.models.user.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro di autenticazione JWT che viene eseguito una volta per ogni richiesta HTTP (sia navigazione che API).
 * <p>
 * Questo filtro intercetta ogni richiesta HTTP in arrivo, estrae il cookie JWT dal client,
 * valida il token, estrae le informazioni dell'utente (email e ruolo), e se il token è valido
 * popola il contesto di sicurezza di Spring (SecurityContext) con le credenziali dell'utente.
 */
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger customLogger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final JwtProvider jwtProvider;
    private final PlayerDetailsServiceImpl playerDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;
    private final AuthenticationPropertiesConfig authProperties;

    /**
     * Metodo di implementazione del filtro che intercetta ogni richiesta HTTP e:
     * <p>
     * - Estrae il cookie JWT dalla richiesta.
     * - Valida il token tramite {@link JwtProvider}.
     * - Se il token è valido, carica i dettagli dell'utente corrispondente (admin o player) e
     * popola il {@link SecurityContextHolder} con l'autenticazione.
     * - In caso di errore, registra il problema nei log senza bloccare la richiesta.
     *
     * @param request     la richiesta HTTP in ingresso
     * @param response    la risposta HTTP in uscita
     * @param filterChain la catena di filtri da invocare dopo l'elaborazione
     * @throws ServletException in caso di errori generici del servlet
     * @throws IOException      in caso di errori di I/O durante la gestione della richiesta
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Estraggo il JWT dai cookie della richiesta
            Cookie cookie = WebUtils.getCookie(request, authProperties.getJwtCookieName());
            customLogger.trace("jwt cookie found: {}", cookie);
            String jwt = cookie != null ? cookie.getValue() : null;

            // Loggo la richiesta per debugging
            customLogger.trace("[AuthTokenFilter] Filtering request with jwt {} on request {}", jwt, request);
            logRequestInfo(request);

            // Verifico se il JWT è presente e se è valido
            if (jwt != null && jwtProvider.validateJwtToken(jwt).isValid()) {
                // Estraggo i dati dell'user embeddati nel token
                String email = jwtProvider.getUserEmailFromJwtToken(jwt);
                Role role = jwtProvider.getUserRoleFromJwtToken(jwt);

                // Carico i dettagli dell'utente. Il servizio usato dipende dal suo ruolo
                UserDetails userDetails = role.equals(Role.ADMIN) ?
                        adminDetailsService.loadUserByUsername(email) :
                        playerDetailsService.loadUserByUsername(email);

                // Costruisco l'autenticazione utente
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                // Associo le informazioni di autenticazione alla richiesta e imposto il token nel Security Context di Spring.
                // Fino alla fine della richiesta, l'utente sarà autenticato e i suoi dati saranno recuperabili dal contesto.
                customLogger.trace("[AuthTokenFilter] Jwt is valid, setting authentication context");
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            customLogger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // Proseguo con l'esecuzione dei filtri a prescindere che l'utente sia autenticato o meno. La mancata/fallita
        // autenticazione sarà gestita da AuthEntryPointJwt
        filterChain.doFilter(request, response);
    }

    /**
     * Metodo che logga una richiesta HTTP in entrata. Usato per debugging.
     *
     * @param request la richiesta HTTP in entrata.
     */
    private void logRequestInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("[AuthTokenFilter] Request info:\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("Request URI: ").append(request.getRequestURI()).append("\n");
        sb.append("Query String: ").append(request.getQueryString()).append("\n");
        sb.append("Remote Addr: ").append(request.getRemoteAddr()).append("\n");
        sb.append("Headers: \n");

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("  ").append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n");
        }

        customLogger.trace("{}", sb);
    }

}