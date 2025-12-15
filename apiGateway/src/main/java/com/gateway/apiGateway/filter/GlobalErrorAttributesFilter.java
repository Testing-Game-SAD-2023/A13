package com.gateway.apiGateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributesFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorAttributesFilter.class);
    private final ObjectMapper objectMapper;

    public GlobalErrorAttributesFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

            // CASO 1: Risposte con body (es. da downstream o errori scritti esplicitamente)
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                HttpStatusCode statusCode = getStatusCode();
                if (shouldIntercept(statusCode)) {
                    HttpStatusCode effectiveStatus = (statusCode != null) ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR;
                    return Flux.from(body)
                            .doOnNext(DataBufferUtils::release)
                            .then(Mono.defer(() -> writeCustomBody(effectiveStatus, getDelegate(), bufferFactory, exchange)));
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMap(p -> p));
            }

            // CASO 2: Risposte vuote chiuse "gentilmente" (es. Rate Limiter 429)
            @Override
            public Mono<Void> setComplete() {
                HttpStatusCode statusCode = getStatusCode();
                if (shouldIntercept(statusCode)) {
                    HttpStatusCode effectiveStatus = (statusCode != null) ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR;
                    return writeCustomBody(effectiveStatus, getDelegate(), bufferFactory, exchange);
                }
                return super.setComplete();
            }
        };

        // Esecuzione catena con gestione ECCEZIONI
        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .onErrorResume(ex -> {
                    // CASO 3: Eccezioni lanciate dai filtri (es. Auth Filter che lancia 401 invece di settarlo)
                    HttpStatusCode status = determineStatusFromException(ex);
                    
                    if (shouldIntercept(status) && !originalResponse.isCommitted()) {
                        return writeCustomBody(status, originalResponse, bufferFactory, exchange);
                    }
                    // Se non è un errore che gestiamo noi, rilanciamo l'eccezione
                    return Mono.error(ex);
                });
    }

    /**
     * Determina se intervenire. 
     * Intercetta: 401, 429, 500, NULL.
     * Ignora: Circuit Breaker Fallback (es. 200, 503).
     */
    private boolean shouldIntercept(HttpStatusCode status) {
        if (status == null) return true;
        int val = status.value();
        return val == HttpStatus.UNAUTHORIZED.value() || 
               val == HttpStatus.TOO_MANY_REQUESTS.value() ||
               val == HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    /**
     * Estrae lo status code dall'eccezione se possibile
     */
    private HttpStatusCode determineStatusFromException(Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            return ((ResponseStatusException) ex).getStatusCode();
        }
        // Se è un'altra eccezione generica, assumiamo sia un errore server
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private Mono<Void> writeCustomBody(HttpStatusCode statusCode, ServerHttpResponse response, DataBufferFactory bufferFactory, ServerWebExchange exchange) {
        try {
            // Assicuriamoci che lo status sia corretto sulla response
            if (response.getStatusCode() == null || response.getStatusCode().value() != statusCode.value()) {
                response.setStatusCode(statusCode);
            }

            Map<String, Object> problemDetails = new LinkedHashMap<>();
            problemDetails.put("type", "about:blank");
            
            String reasonPhrase = (statusCode instanceof HttpStatus) ? ((HttpStatus) statusCode).getReasonPhrase() : "Error";
            problemDetails.put("title", reasonPhrase);
            problemDetails.put("status", statusCode.value());
            problemDetails.put("detail", getDetailMessage(statusCode));
            problemDetails.put("instance", exchange.getRequest().getPath().value());

            byte[] jsonBytes = objectMapper.writeValueAsBytes(problemDetails);

            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getHeaders().setContentLength(jsonBytes.length);

            return response.writeWith(Mono.just(bufferFactory.wrap(jsonBytes)));

        } catch (JsonProcessingException e) {
            logger.error("Errore serializzazione JSON", e);
            return response.setComplete();
        }
    }

    private String getDetailMessage(HttpStatusCode status) {
        if (status.value() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            return "Il limite di richieste è stato superato. Riprova più tardi.";
        } else if (status.value() == HttpStatus.UNAUTHORIZED.value()) {
            return "Autenticazione mancante o non valida.";
        }
        return "Si è verificato un errore interno al server.";
    }

    @Override
    public int getOrder() {
        return -5;
    }
}
