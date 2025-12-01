package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.GuidelineDTO;

import com.groom.manvsclass.service.SecurityService;
import com.groom.manvsclass.service.GuidelineService;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.DuplicatedTitlesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin
@RestController
public class GuidelineController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private GuidelineService guidelineService;

    @PostMapping("/opponents/guidelines/upload")
    public ResponseEntity<?> uploadGuidelines(@Valid @RequestBody List<GuidelineDTO> guidelineDTOs) {

        String jwt = securityService.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            guidelineService.uploadGuidelines(guidelineDTOs);
            return ResponseEntity.status(HttpStatus.OK).body("Linee guida caricate con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'upload delle linee guida: " + e.getMessage());
        }
    }

    @GetMapping("/opponents/guidelines")
    public ResponseEntity<?> viewGuidelines() {

        String jwt = securityService.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            List<GuidelineDTO> guidelineDTOs = guidelineService.findGuidelines();
            return ResponseEntity.ok(guidelineDTOs);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero delle linee guida: " + e.getMessage());
        }
    }

    @DeleteMapping("/opponents/guidelines/{guidelineTitle}")
    public ResponseEntity<?> deleteGuideline(@PathVariable("guidelineTitle") String guidelineTitle)
    {

        String jwt = securityService.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token JWT non valido o mancante.");
        }

        try {
            guidelineService.deleteGuideline(guidelineTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Linea guida eliminata con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nella cancellazione della linea guida: " + e.getMessage());
        }
    }
}