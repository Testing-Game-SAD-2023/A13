/*
 * Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.springframework.web.server.ServerWebExchange;

import com.gateway.apiGateway.Factory.AuthenticationFilterGatewayFilterFactory.Config;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final AuthTokenService authTokenService;

    /*
     * Ordine d'esecuzione del filtro
     */
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
        // logger.info("[Gateway auth filter] Received request from: {}", request.getPath()); // Decommentare se serve log verboso

        String token;
        try {
            token = authTokenService.extractToken(request);
            if (token == null) {
                logger.warn("Token mancante nella richiesta per l'utente: {}", request.getRemoteAddress());
                return unauthorized(exchange);
            }
        } catch (Exception e) {
            // Se l'estrazione fallisce per motivi tecnici imprevisti
            logger.error("Errore nell'estrazione del token per l'utente {}", request.getRemoteAddress(), e);
            return error(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return authTokenService.validateToken(token).flatMap(isValid -> {
            if (!isValid) {
                logger.warn("Token non valido ricevuto dalla richiesta: {}", request.getRemoteAddress());
                return unauthorized(exchange);
            }
            
            // Estrai informazioni dal token e aggiungile all'header
            String userId = authTokenService.extractUserId(token);
            logger.info("Utente autenticato con successo: {}", userId);

            // Creare un nuovo exchange con la richiesta mutata
            ServerWebExchange mutatedExchange = exchange.mutate()
                                                .request(
                                                    request.mutate()
                                                        .header("X-Authenticated-UserId", userId)
                                                        .build()
                                                )
                                                .build();
            return chain.filter(mutatedExchange);
        }).onErrorResume(e -> {
            // QUI LA MODIFICA IMPORTANTE: Gestione dell'errore tecnico (es. Redis down, Auth Service down)
            logger.error("Errore tecnico durante la validazione del token: {}", e.getMessage(), e);
            return error(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        return error(exchange, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> error(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return Mono.empty();
    }
}
