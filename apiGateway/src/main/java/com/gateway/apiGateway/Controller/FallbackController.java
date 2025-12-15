package com.gateway.apiGateway.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange; 

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // Fallback specifico per T7
    @RequestMapping("/t7")
    public ResponseEntity<Map<String, Object>> fallbackT7(ServerWebExchange exchange) {
        String detail = "Il servizio di compilazione (T7) è momentaneamente sovraccarico o non raggiungibile. Riprova tra qualche istante.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, exchange);
    }

    // Fallback specifico per T8
    @RequestMapping("/t8")
    public ResponseEntity<Map<String, Object>> fallbackT8(ServerWebExchange exchange) {
        String detail = "Il servizio di generazione automatica test (T8) ha superato il tempo limite o non è disponibile. La generazione EvoSuite richiede molte risorse.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, exchange);
    }

    // Fallback generico
    @RequestMapping("")
    public ResponseEntity<Map<String, Object>> fallbackGeneric(ServerWebExchange exchange) {
        String detail = "Il servizio richiesto non è al momento disponibile.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, exchange);
    }

    // Metodo helper
    private ResponseEntity<Map<String, Object>> createProblemDetails(HttpStatus status, String detail, ServerWebExchange exchange) {
        Map<String, Object> problemDetails = new HashMap<>();

        problemDetails.put("type", "about:blank");
        problemDetails.put("title", status.getReasonPhrase());
        problemDetails.put("status", status.value());
        problemDetails.put("detail", detail);
        

        problemDetails.put("instance", exchange.getRequest().getPath().value());

        return ResponseEntity.status(status).body(problemDetails);
    }
}
