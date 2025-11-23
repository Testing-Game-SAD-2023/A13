package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.dto.SuggestionResponseDTO;
import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.dto.GuidelineResponseDTO;
import com.groom.manvsclass.security.JwtRequestContext;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.service.SuggestionService;
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
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/opponents/suggestions/upload")
    public ResponseEntity<?> uploadSuggestions(
            @RequestBody SuggestionDTO suggestionDTO) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {

            suggestionService.uploadSuggestions(suggestionDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Suggerimenti caricati con successo.");

        } catch (DuplicatedTitlesException e) {
            return ResponseEntity.status(HttpStatus.OK).body("Suggerimenti caricati con successo, " +
                    "i seguenti suggerimenti non sono stati aggiunti in quanto duplicati: " + e.getMessage());
        }

        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore.");
        }
    }

    @GetMapping("/opponents/suggestions/{className}")
    public ResponseEntity<?> viewSuggestions(@PathVariable("className") String className) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {

            List<SuggestionResponseDTO> suggestions = suggestionService.findSuggestions(className);
            return ResponseEntity.ok(suggestions);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel download della classe: " + e.getMessage());
        }
    }

    @DeleteMapping("/opponents/suggestions/{className}/suggestion")
    public ResponseEntity<?> deleteSuggestion(
            @PathVariable("className") String className,
            @RequestParam("suggestionTitle") String suggestionTitle)
    {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            throw new RuntimeException("Token JWT non valido o mancante.");
        }

        try {
            suggestionService.deleteSuggestion(className, suggestionTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Suggerimento eliminato con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore" );
        }
    }

    @PostMapping("/opponents/guidelines/upload")
    public ResponseEntity<?> uploadGuidelines(@RequestBody GuidelineDTO guidelineDTO) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {

            suggestionService.uploadGuidelines(guidelineDTO);
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

            List<GuidelineResponseDTO> guidelines = suggestionService.findGuidelines();
            return ResponseEntity.ok(guidelines);

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
            suggestionService.deleteGuideline(guidelineTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Linea guida eliminata con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore" );
        }
    }
}