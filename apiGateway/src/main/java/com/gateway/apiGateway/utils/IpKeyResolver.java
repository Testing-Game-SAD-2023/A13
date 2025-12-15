/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.gateway.apiGateway.utils;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class IpKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // Estrai l'IP dall'header X-Forwarded-For
        String ip = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");

        // Se non è presente, usa l'IP remoto del client
        if (ip == null || ip.isEmpty()) {
            ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        // Restituisce l'IP come chiave unica per il rate limiting
        return Mono.just(ip);
    }
}
