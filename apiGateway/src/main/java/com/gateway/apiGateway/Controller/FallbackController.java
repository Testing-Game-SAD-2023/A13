package com.gateway.apiGateway.Controller;

import jakarta.servlet.http.HttpServletRequest; // Usa javax.servlet.http.HttpServletRequest se usi Spring Boot < 3
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // Fallback specifico per T7 (Student Test Runner / Compilazione)
    @RequestMapping("/t7")
    public ResponseEntity<Map<String, Object>> fallbackT7(HttpServletRequest request) {
        String detail = "Il servizio di compilazione (T7) è momentaneamente sovraccarico o non raggiungibile. Riprova tra qualche istante.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, request);
    }

    // Fallback specifico per T8 (EvoSuite / Generazione Test)
    @RequestMapping("/t8")
    public ResponseEntity<Map<String, Object>> fallbackT8(HttpServletRequest request) {
        String detail = "Il servizio di generazione automatica test (T8) ha superato il tempo limite o non è disponibile. La generazione EvoSuite richiede molte risorse.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, request);
    }

    // Fallback generico
    @RequestMapping("")
    public ResponseEntity<Map<String, Object>> fallbackGeneric(HttpServletRequest request) {
        String detail = "Il servizio richiesto non è al momento disponibile.";
        return createProblemDetails(HttpStatus.SERVICE_UNAVAILABLE, detail, request);
    }

    // Metodo helper per uniformare la struttura della risposta (Problem Details)
    private ResponseEntity<Map<String, Object>> createProblemDetails(HttpStatus status, String detail, HttpServletRequest request) {
        Map<String, Object> problemDetails = new HashMap<>();

        problemDetails.put("type", "about:blank");
        problemDetails.put("title", status.getReasonPhrase());
        problemDetails.put("status", status.value());
        problemDetails.put("detail", detail);
        problemDetails.put("instance", request.getRequestURI());

        return ResponseEntity.status(status).body(problemDetails);
    }
}
