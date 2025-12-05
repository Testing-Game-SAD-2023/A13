/*
 * Copyright (c) 2025
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

package com.gateway.apiGateway.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // Fallback specifico per T7 (Student Test Runner / Compilazione)
    @GetMapping("/t7")
    public ResponseEntity<Map<String, String>> fallbackT7() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "T7 Service Unavailable");
        response.put("message", "Il servizio di compilazione (T7) è momentaneamente sovraccarico o non raggiungibile.");
        response.put("suggestion", "Riprova tra qualche istante. Se il problema persiste, contatta l'amministratore.");
        response.put("status", "503");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Fallback specifico per T8 (EvoSuite / Generazione Test)
    @GetMapping("/t8")
    public ResponseEntity<Map<String, String>> fallbackT8() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "T8 Service Unavailable");
        response.put("message", "Il servizio di generazione automatica test (T8) ha superato il tempo limite o non è disponibile.");
        response.put("suggestion", "La generazione EvoSuite richiede molte risorse. Riprova più tardi.");
        response.put("status", "503");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Fallback generico per altre rotte (mantiene la compatibilità con il vecchio metodo)
    @GetMapping("")
    public ResponseEntity<Map<String, String>> fallbackGeneric() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Service Unavailable");
        response.put("message", "Il servizio richiesto non è al momento disponibile.");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}