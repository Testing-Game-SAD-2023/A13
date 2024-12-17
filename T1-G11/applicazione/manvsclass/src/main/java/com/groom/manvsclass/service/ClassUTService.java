//MODIFICA 07/12/2024: Creazione nuovo Service per la business logic relativa alle classi UT

package com.groom.manvsclass.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.repository.ClassRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassUTService {

    @Autowired
    private ClassRepository repo;

    @Autowired
    private JwtService jwtService; // Servizio per la gestione e verifica del JWT

    public ResponseEntity<?> getNomiClassiUT(String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Token JWT non valido o mancante.");
        }

        // 2. Recupera tutte le ClassUT dal repository e restituisce solo i nomi
        List<String> classNames = repo.findAll()
                                      .stream()
                                      .map(ClassUT::getName) // Estrae solo i nomi
                                      .collect(Collectors.toList());

        // 3. Ritorna i nomi delle classi con lo status HTTP 200 (OK)
        return ResponseEntity.ok(classNames);
    }
}
