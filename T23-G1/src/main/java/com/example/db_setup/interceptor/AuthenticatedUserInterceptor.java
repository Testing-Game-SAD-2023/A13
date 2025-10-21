package com.example.db_setup.interceptor;

import com.example.db_setup.security.jwt.JwtProvider;
import com.example.db_setup.service.AuthService;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;
import testrobotchallenge.commons.models.user.Role;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interceptor delle richieste HTTP che verifica se l'utente è già autenticato e ne redirige in caso la richiesta.
 *
 * <p>
 * Funzionalità principali:
 * <ul>
 *   <li>Controlla se un utente è già autenticato tramite il cookie JWT;</li>
 *   <li>Se l'utente è autenticato, lo reindirizza automaticamente alla pagina principale
 *       appropriata (/main per PLAYER, /dashboard per ADMIN) quando prova ad accedere
 *       alle pagine di login o registrazione;</li>
 *   <li>Permette l'accesso alle pagine di login/registrazione se l'utente non è autenticato
 *       o se i parametri di query 'unauthorized' o 'expired' sono presenti;</li>
 *   <li>Determina il ruolo dell'utente (PLAYER o ADMIN) tramite il JWT.</li>
 * </ul>
 * </p>
 */
public class AuthenticatedUserInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedUserInterceptor.class);

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    private final List<String> playerUrls;
    private final List<String> adminUrls;

    public AuthenticatedUserInterceptor(JwtProvider jwtProvider, AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;

        playerUrls = new ArrayList<>();
        playerUrls.add("/login");
        playerUrls.add("/register");
        playerUrls.add("/change_password");
        playerUrls.add("/reset_password");

        adminUrls = new ArrayList<>();
        adminUrls.add("/admin/login");
        adminUrls.add("/admin/register");
        adminUrls.add("/admin/change_password");
        adminUrls.add("/admin/reset_password");
    }


    /**
     * Metodo invocato prima dell'esecuzione di un handler. Gestisce il redirect
     * automatico per utenti già autenticati che tentano di accedere alle pagine di login/registrazione.
     *
     * @param request  la richiesta HTTP
     * @param response la risposta HTTP
     * @param handler  handler della richiesta
     * @return true se la richiesta può procedere normalmente, false se viene effettuato un redirect
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies == null)
            return true;

        Cookie authCookie = Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals("jwt"))
                .findFirst()
                .orElse(null);


        if (!playerUrls.contains(urlPathHelper.getLookupPathForRequest(request)) && !adminUrls.contains(urlPathHelper.getLookupPathForRequest(request)))
            return true;

        String unauthorizedParam = request.getParameter("unauthorized");
        String expiredParam = request.getParameter("expired");
        if (unauthorizedParam != null) {
            logger.warn("Found query param 'unauthorized' in request");
            return true;
        } else if (expiredParam != null) {
            logger.warn("Found query param 'expired' in request");
            return true;
        }

        logger.trace("[AuthenticatedUserInterceptor] Intercepting request {} for redirect", urlPathHelper.getLookupPathForRequest(request));

        if (isAuthenticated(authCookie)) {
            String encodedRedirectURL;
            Role userRole = extractUserRole(authCookie);

            logger.trace("[AuthenticatedUserInterceptor] User is authenticated with role {}", userRole);

            if (userRole.equals(Role.PLAYER) && playerUrls.contains(urlPathHelper.getLookupPathForRequest(request))) {
                encodedRedirectURL = response.encodeRedirectURL(
                        request.getContextPath() + "/main");
                response.setStatus(HttpStatus.SC_TEMPORARY_REDIRECT);
                response.setHeader("Location", encodedRedirectURL);
                logger.trace("[AuthenticatedUserInterceptor] Redirecting to /main");
                return false;
            } else if (userRole.equals(Role.ADMIN) && adminUrls.contains(urlPathHelper.getLookupPathForRequest(request))) {
                encodedRedirectURL = response.encodeRedirectURL(
                        request.getContextPath() + "/dashboard");
                response.setStatus(HttpStatus.SC_TEMPORARY_REDIRECT);
                response.setHeader("Location", encodedRedirectURL);
                logger.trace("[AuthenticatedUserInterceptor] Redirecting to /dashboard");
                return false;
            }

            logger.trace("[AuthenticatedUserInterceptor] Role incompatible with redirect, proceeding without redirecting");
            return true;
        } else {
            logger.trace("[AuthenticatedUserInterceptor] User is not authenticated, proceeding without redirecting");
            return true;
        }
    }

    /**
     * Verifica se l'utente è autenticato controllando la validità del cookie JWT.
     *
     * @param authCookie cookie contenente il token JWT
     * @return true se il token è valido, false altrimenti
     */
    private boolean isAuthenticated(Cookie authCookie) {
        return authCookie != null && authService.validateToken(authCookie.getValue()).isValid();
    }

    /**
     * Estrae il ruolo dell'utente dal JWT.
     *
     * @param authCookie cookie contenente il token JWT
     * @return ruolo dell'utente
     */
    private Role extractUserRole(Cookie authCookie) {
        return jwtProvider.getUserRoleFromJwtToken(authCookie.getValue());
    }
}
