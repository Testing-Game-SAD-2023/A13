package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.SuggestionDTO;

import com.groom.manvsclass.security.JwtRequestContext;
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
import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin
@RestController
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @PostMapping("/opponents/suggestions/upload/{className}")
    public ResponseEntity<?> uploadSuggestions(
            @PathVariable("className") String className,
            @Valid @RequestBody List<SuggestionDTO> suggestionDTOs) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            suggestionService.uploadSuggestions(className, suggestionDTOs);
            return ResponseEntity.status(HttpStatus.OK).body("Suggerimenti caricati con successo.");

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore nell'upload dei suggerimenti: " + e.getMessage());
        }
    }

    @GetMapping("/opponents/suggestions/{className}")
    public ResponseEntity<?> viewSuggestions(@PathVariable("className") String className) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            List<SuggestionDTO> suggestionDTOs = suggestionService.findSuggestions(className);
            return ResponseEntity.ok(suggestionDTOs);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero dei suggerimenti: " + e.getMessage());
        }
    }

    @DeleteMapping("/opponents/suggestions/{className}/suggestion")
    public ResponseEntity<?> deleteSuggestion(
            @PathVariable("className") String className,
            @RequestParam("suggestionTitle") String suggestionTitle)
    {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            suggestionService.deleteSuggestion(className, suggestionTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Suggerimento eliminato con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nella cancellazione del suggerimento: " + e.getMessage());
        }
    }

}