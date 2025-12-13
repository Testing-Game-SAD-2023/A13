/*
 * Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 * All rights reserved.
 * ... (License header standard) ...
 */
package com.gateway.apiGateway.filter.authenticationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException; // IMPORTANTE
import org.springframework.web.server.ServerWebExchange;

import com.gateway.apiGateway.Factory.AuthenticationFilterGatewayFilterFactory.Config;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final AuthTokenService authTokenService;

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    public AuthenticationFilter(ReactiveStringRedisTemplate redisTemplate,
            Config config, WebClient.Builder webClientBuilder) {
        this.authTokenService = new AuthTokenService(webClientBuilder,
                config.getAuthServiceUrl(),
                config.getCachePrefix(),
                config.getBUFFER_TIME_SECONDS(),
                config.getCACHE_TTL_THRESHOLD(),
                redisTemplate);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String token;
        try {
            token = authTokenService.extractToken(request);
            if (token == null) {
                logger.warn("Token mancante nella richiesta per l'utente: {}", request.getRemoteAddress());
                // MODIFICA QUI: Non chiamare metodi che ritornano Mono.empty(), lancia l'errore!
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token mancante"));
            }
        } catch (Exception e) {
            logger.error("Errore nell'estrazione del token per l'utente {}", request.getRemoteAddress(), e);
            // MODIFICA QUI
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore estrazione token"));
        }

        return authTokenService.validateToken(token).flatMap(isValid -> {
            if (!isValid) {
                logger.warn("Token non valido ricevuto dalla richiesta: {}", request.getRemoteAddress());
                // MODIFICA QUI
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido"));
            }
            
            String userId = authTokenService.extractUserId(token);
            logger.info("Utente autenticato con successo: {}", userId);

            ServerWebExchange mutatedExchange = exchange.mutate()
                                                .request(
                                                    request.mutate()
                                                        .header("X-Authenticated-UserId", userId)
                                                        .build()
                                                )
                                                .build();
            return chain.filter(mutatedExchange);
        }).onErrorResume(e -> {
            // Se l'errore è già una ResponseStatusException (lanciata sopra), la lasciamo passare
            // così il GlobalErrorAttributesFilter la prende.
            if (e instanceof ResponseStatusException) {
                return Mono.error(e);
            }
            
            logger.error("Errore tecnico durante la validazione del token: {}", e.getMessage(), e);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno durante la validazione"));
        });
    }
    
    // I metodi privati 'unauthorized' ed 'error' non servono più e possono essere rimossi
}
