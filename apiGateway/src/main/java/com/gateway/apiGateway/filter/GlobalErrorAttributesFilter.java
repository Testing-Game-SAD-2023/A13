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

        // Creiamo il decoratore
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // In Spring 6 / Boot 3 getStatusCode ritorna HttpStatusCode
                HttpStatusCode statusCode = getStatusCode();

                // Controlliamo i codici 401 e 429
                if (statusCode != null && (statusCode.value() == HttpStatus.UNAUTHORIZED.value() || statusCode.value() == HttpStatus.TOO_MANY_REQUESTS.value())) {

                    // FLUSSO DI ERRORE:
                    // 1. Convertiamo il body originale in Flux per consumarlo
                    // 2. Usiamo doOnNext per rilasciare la memoria (DataBufferUtils.release)
                    // 3. Usiamo .then() per eseguire la nostra scrittura custom DOPO aver pulito il vecchio body
                    return Flux.from(body)
                            .doOnNext(DataBufferUtils::release)
                            .then(Mono.defer(() -> writeCustomBody(statusCode, originalResponse, bufferFactory, exchange)));
                }

                // FLUSSO NORMALE:
                // Se non è un errore gestito, lasciamo fare a Spring (super.writeWith)
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMap(p -> p));
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    // Metodo helper che scrive direttamente sulla response originale
    private Mono<Void> writeCustomBody(HttpStatusCode statusCode, ServerHttpResponse response, DataBufferFactory bufferFactory, ServerWebExchange exchange) {
        try {
            Map<String, Object> problemDetails = new LinkedHashMap<>();
            problemDetails.put("type", "about:blank");

            // Gestione titolo basata sul valore dello status
            String reasonPhrase = (statusCode instanceof HttpStatus) ? ((HttpStatus) statusCode).getReasonPhrase() : "Error";
            problemDetails.put("title", reasonPhrase);
            problemDetails.put("status", statusCode.value());
            problemDetails.put("detail", getDetailMessage(statusCode));
            problemDetails.put("instance", exchange.getRequest().getPath().value());

            byte[] jsonBytes = objectMapper.writeValueAsBytes(problemDetails);

            // Impostiamo gli header sulla risposta originale
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getHeaders().setContentLength(jsonBytes.length);

            // Scriviamo il buffer sulla risposta passata come argomento
            return response.writeWith(Mono.just(bufferFactory.wrap(jsonBytes)));

        } catch (JsonProcessingException e) {
            logger.error("Errore nella serializzazione JSON dell'errore", e);
            return response.writeWith(Mono.empty());
        }
    }

    private String getDetailMessage(HttpStatusCode status) {
        if (status.value() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            return "Il limite di richieste è stato superato. Riprova più tardi.";
        } else if (status.value() == HttpStatus.UNAUTHORIZED.value()) {
            return "Autenticazione mancante o non valida.";
        }
        return "Errore nella richiesta.";
    }

    @Override
    public int getOrder() {
        // Ordine prioritario per avvolgere gli altri filtri (-5)
        return -5;
    }
}