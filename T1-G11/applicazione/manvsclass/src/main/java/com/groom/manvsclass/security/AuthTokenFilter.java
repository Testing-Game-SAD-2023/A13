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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testrobotchallenge.commons.models.user.Role.ADMIN;
import static testrobotchallenge.commons.models.user.Role.PLAYER;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger customLogger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static final List<String> PLAYER_ALLOWED_URIS = List.of(
            "/opponents/**",
            "/ottieniTeamByStudentId",
            "/ottieniDettagliTeamCompleto"
    );
    private final ApiGatewayClient apiGatewayClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        customLogger.info("[AuthTokenFilter] Authenticating request {} {}", request.getMethod(), request.getRequestURI());

        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");
        Cookie refreshCookie = WebUtils.getCookie(request, "jwt-refresh");

        String jwt = jwtCookie != null ? jwtCookie.getValue() : null;
        String refreshToken = refreshCookie != null ? refreshCookie.getValue() : null;

        try {
            if (jwt == null) {
                if (refreshCookie != null) {
                    customLogger.info("JWT missing. Attempting to refresh using refresh token...");
                    jwt = tryRefreshAndContinue(refreshToken, response);

                    if (jwt == null)
                        return;
                } else {
                    customLogger.info("JWT and refresh token missing. Redirecting to login.");
                    redirectToLogin(response, "unauthorized");
                    return;
                }
            }

            JwtValidationResponseDTO validation = apiGatewayClient.callValidateJwtToken(jwt);
            Role role = resolveRole(request, validation);

            if (role == null) {
                customLogger.info("[AuthTokenFilter] Invalid token or insufficient permissions.");
                redirectToLogin(response, "unauthorized");
                return;
            }

            customLogger.info("[AuthTokenFilter] Validated token for role {}", role);
            if (ADMIN.equals(role)) {
                JwtRequestContext.setJwtToken("%s=%s".formatted(jwtCookie.getName(), jwt));
                customLogger.debug("[AuthTokenFilter] JWT saved in thread context");
            }

            chain.doFilter(request, response);

        } finally {
            JwtRequestContext.clear();
        }
    }

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
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, newJwtCookie.toString());
            JwtRequestContext.setJwtToken(newJwtCookie.toString());

            customLogger.info("JWT refreshed and saved in context. Proceeding with filter chain.");
            return newJwt;

        } catch (Exception ex) {
            customLogger.warn("Refresh token failed: {}", ex.getMessage());
            redirectToLogin(response, "expired");
            return null;
        }
    }

    private void redirectToLogin(HttpServletResponse response, String reason) throws IOException {
        response.sendRedirect("/admin/login?" + reason + "=true");
    }

    private Role resolveRole(HttpServletRequest request, JwtValidationResponseDTO validation) {
        if (validation == null || !validation.isValid())
            return null;

        return switch (validation.getRole()) {
            case ADMIN -> ADMIN;
            case PLAYER -> isPlayerAccessAllowed(request) ? PLAYER : null;
        };
    }

    private boolean isPlayerAccessAllowed(HttpServletRequest request) {
        AntPathMatcher uriMatcher = new AntPathMatcher();

        if (!request.getMethod().equals("GET"))
            return false;

        return PLAYER_ALLOWED_URIS.stream().anyMatch(allowedURI -> uriMatcher.match(allowedURI, request.getRequestURI()));
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

