package com.groom.manvsclass.security;

import com.groom.manvsclass.api.ApiGatewayClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;
import testrobotchallenge.commons.models.user.Role;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testrobotchallenge.commons.models.user.Role.ADMIN;
import static testrobotchallenge.commons.models.user.Role.PLAYER;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    private static final List<String> PLAYER_ALLOWED_URIS = List.of(
            "/opponents/**",
            "/ottieniTeamByStudentId",
            "/ottieniDettagliTeamCompleto"
    );

    private final ApiGatewayClient apiGatewayClient;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("[AuthTokenFilter] {} {}", request.getMethod(), request.getRequestURI());

        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");
        Cookie refreshCookie = WebUtils.getCookie(request, "jwt-refresh");

        String jwt = jwtCookie != null ? jwtCookie.getValue() : null;
        String refreshToken = refreshCookie != null ? refreshCookie.getValue() : null;

        try {
            // 1️⃣ Nessun JWT presente → tenta refresh o reindirizza al login
            if (jwt == null) {
                if (refreshToken != null) {
                    log.info("[AuthTokenFilter] JWT mancante, provo refresh...");
                    jwt = tryRefreshAndContinue(refreshToken, response);
                    if (jwt == null) return;
                } else {
                    log.warn("[AuthTokenFilter] Nessun JWT e nessun refresh token → redirect al login");
                    redirectToLogin(response, "unauthorized");
                    return;
                }
            }

            // 2️⃣ Chiamata remota al servizio di validazione centralizzato
            JwtValidationResponseDTO validation = apiGatewayClient.callValidateJwtToken(jwt);

            Role resolvedRole = resolveRole(request, validation);
            if (resolvedRole == null) {
                log.warn("[AuthTokenFilter] Token non valido o permessi insufficienti");
                redirectToLogin(response, "unauthorized");
                return;
            }

            // 3️⃣ Solo se ADMIN → salva token nel contesto per uso nei service successivi
            if (ADMIN.equals(resolvedRole)) {
                JwtRequestContext.setJwtToken(jwt);
                log.debug("[AuthTokenFilter] JWT salvato nel thread context (ADMIN)");

                // SALVATAGGIO LOCALE
                try {
                    Admin adminFromToken = jwtService.getAdminFromJwt(jwt);
                    if (adminFromToken != null && !adminRepository.existsById(adminFromToken.getEmail())) {
                        adminRepository.save(adminFromToken);
                        log.debug("Admin {} sincronizzato nel DB locale.", adminFromToken.getEmail());
                    }
                } catch (Exception e) {
                    log.error("Impossibile salvare l'admin nel DB locale: {}", e.getMessage());
                }
            }

            chain.doFilter(request, response);

        } finally {
            JwtRequestContext.clear();
        }
    }

    /**
     * Prova a rinnovare il JWT usando il refresh token.
     */
    private String tryRefreshAndContinue(String refreshToken, HttpServletResponse response)
            throws IOException {
        try {
            Map<String, String> cookieAttrs = parseCookieAttributes(apiGatewayClient.callRefreshJwtToken(refreshToken));

            String newJwt = cookieAttrs.get("jwt");
            int maxAge = Integer.parseInt(cookieAttrs.getOrDefault("max-age", "3600"));
            String path = cookieAttrs.getOrDefault("path", "/");

            ResponseCookie newJwtCookie = ResponseCookie.from("jwt", newJwt)
                    .path(path)
                    .maxAge(maxAge)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, newJwtCookie.toString());
            JwtRequestContext.setJwtToken(newJwt);

            log.info("[AuthTokenFilter] JWT rinnovato con successo, proseguo la catena");
            return newJwt;

        } catch (Exception ex) {
            log.warn("[AuthTokenFilter] Refresh token fallito: {}", ex.getMessage());
            redirectToLogin(response, "expired");
            return null;
        }
    }

    /**
     * Reindirizza l'utente alla pagina di login con motivo specifico.
     */
    private void redirectToLogin(HttpServletResponse response, String reason) throws IOException {
        response.sendRedirect("/admin/login?" + reason + "=true");
    }

    /**
     * Determina se l'utente ha accesso in base al ruolo e all'endpoint richiesto.
     */
    private Role resolveRole(HttpServletRequest request, JwtValidationResponseDTO validation) {
        if (validation == null || !validation.isValid()) return null;

        return switch (validation.getRole()) {
            case ADMIN -> ADMIN;
            case PLAYER -> isPlayerAccessAllowed(request) ? PLAYER : null;
        };
    }

    private boolean isPlayerAccessAllowed(HttpServletRequest request) {
        if (!"GET".equals(request.getMethod())) return false;

        AntPathMatcher matcher = new AntPathMatcher();
        return PLAYER_ALLOWED_URIS.stream()
                .anyMatch(allowed -> matcher.match(allowed, request.getRequestURI()));
    }

    private Map<String, String> parseCookieAttributes(String setCookieHeader) {
        Map<String, String> attributes = new HashMap<>();
        for (String part : setCookieHeader.split(";")) {
            String[] keyValue = part.trim().split("=", 2);
            if (keyValue.length == 2)
                attributes.put(keyValue[0].toLowerCase(), keyValue[1]);
            else
                attributes.put(keyValue[0].toLowerCase(), "true");
        }
        return attributes;
    }
}

