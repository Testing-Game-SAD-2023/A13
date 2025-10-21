/*MODIFICA (5/11/2024) - Refactoring task T1
 * ScalataService ora si occupa di implementare i servizi relativi alla modalità scalata
 */
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.repository.ScalataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScalataService {

    private static final Logger logger = LoggerFactory.getLogger(ScalataService.class);
    @Autowired
    private ScalataRepository scalata_repo;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> uploadScalata(Scalata scalata, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(POST /configureScalata) Attenzione, non sei loggato!");
        }

        Scalata new_scalata = new Scalata();
        new_scalata.setUsername(scalata.getUsername());
        new_scalata.setScalataName(scalata.getScalataName());
        new_scalata.setScalataDescription(scalata.getScalataDescription());
        new_scalata.setNumberOfRounds(scalata.getNumberOfRounds());
        new_scalata.setSelectedClasses(scalata.getSelectedClasses());

        scalata_repo.save(new_scalata);
        return ResponseEntity.ok().body(new_scalata);
    }

    public ResponseEntity<?> listScalate() {
        List<Scalata> scalate = scalata_repo.findAll();
        return new ResponseEntity<>(scalate, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteScalataByName(String scalataName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(DELETE /delete_scalata/{scalataName}) Attenzione, non sei loggato!");
        }

        List<Scalata> scalata = scalata_repo.findByScalataNameContaining(scalataName);
        if (scalata.isEmpty()) {
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " non trovata", HttpStatus.NOT_FOUND);
        } else {
            scalata_repo.delete(scalata.get(0));
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " rimossa", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> retrieveScalataByName(String scalataName) {
        List<Scalata> scalata = scalata_repo.findByScalataNameContaining(scalataName);
        if (scalata.isEmpty()) {
            return new ResponseEntity<>("Scalata with name: " + scalataName + " not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(scalata, HttpStatus.OK);
        }
    }
}