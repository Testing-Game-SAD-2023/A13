package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.GuidelineDTO;

import com.groom.manvsclass.security.JwtRequestContext;
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

import java.util.List;

@CrossOrigin
@RestController
public class GuidelineController {

    @Autowired
    private GuidelineService guidelineService;

    @PostMapping("/opponents/guidelines")
    public ResponseEntity<?> uploadGuidelines(@RequestBody List<GuidelineDTO> guidelinesDTO) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            guidelineService.uploadGuidelines(guidelinesDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Linee guida caricate con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore.");
        }
    }

    @GetMapping("/opponents/guidelines")
    public ResponseEntity<?> viewGuidelines() {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            List<GuidelineDTO> guidelinesDTO = guidelineService.findGuidelines();
            return ResponseEntity.ok(guidelinesDTO);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel download della classe: " + e.getMessage());
        }
    }

    @DeleteMapping("/opponents/guidelines/{guidelineTitle}")
    public ResponseEntity<?> deleteGuideline(@PathVariable("guidelineTitle") String guidelineTitle)
    {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            throw new RuntimeException("Token JWT non valido o mancante.");
        }

        try {
            guidelineService.deleteGuideline(guidelineTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Linea guida eliminata con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore" );
        }
    }
}