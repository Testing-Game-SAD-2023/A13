package com.gateway.apiGateway.config; // Adatta il nome del pacchetto

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class PrincipalNameKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 1. Recupera il Principal (l'identità dell'utente) dal contesto della richiesta.
        // Questo oggetto è impostato dall'AuthenticationFilter se l'autenticazione ha successo.
        return exchange.getPrincipal()
                // 2. Mappa il Principal per ottenere il suo nome (l'ID utente/username)
                .map(principal -> principal.getName())
                // 3. (Fallback di sicurezza) Se per qualche motivo il Principal è assente (es. filtro non eseguito),
                // potremmo usare un fallback, ma per rotte protette è meglio che il Rate Limiter fallisca o usi una chiave generica.
                // In questo contesto, useremo 'anonymous' come fallback, ma di norma le richieste non autenticate saranno bloccate PRIMA dal filtro 401.
                .defaultIfEmpty("anonymous"); 
    }
}
