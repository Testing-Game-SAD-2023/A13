package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.dto.ClassUTSuggestionDTO;
import com.groom.manvsclass.service.SuggestionService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@CrossOrigin
@RestController
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @PostMapping("/opponents/suggestions/upload")
    public ResponseEntity<?> uploadSuggestions(@Valid @RequestBody ClassUTSuggestionDTO classUTSuggestionDTO) {

        String className = classUTSuggestionDTO.getClassName();
        List<SuggestionDTO> suggestionDTOs = classUTSuggestionDTO.getSuggestions();

        suggestionService.uploadSuggestions(className, suggestionDTOs);

        return ResponseEntity.ok("Suggerimenti caricati con successo.");

    }

    @PostMapping("/opponents/suggestions/upload/{className}/{order}")
    public ResponseEntity<?> uploadSuggestionImage(
            @PathVariable("className") @NotBlank String className,
            @PathVariable("order") @Positive int order,
            @RequestParam("image") @NotNull MultipartFile image) {

        suggestionService.uploadSuggestionImage(className, order, image);

        return ResponseEntity.ok("Immagine caricata con successo.");

    }

    @GetMapping("/opponents/suggestions/{className}")
    public ResponseEntity<?> viewSuggestions(@PathVariable("className") @NotBlank String className) {

        List<SuggestionDTO> suggestionDTOs = suggestionService.findSuggestions(className);

        ClassUTSuggestionDTO classUTSuggestionDTO = new ClassUTSuggestionDTO();
        classUTSuggestionDTO.setClassName(className);
        classUTSuggestionDTO.setSuggestions(suggestionDTOs);

        return ResponseEntity.ok(classUTSuggestionDTO);

    }

    @DeleteMapping("/opponents/suggestions/{className}/{order}")
    public ResponseEntity<?> deleteSuggestion(
            @PathVariable("className") @NotBlank String className,
            @PathVariable("order") @Positive int order) {

        suggestionService.deleteSuggestion(className, order);

        return ResponseEntity.ok("Suggerimento eliminato con successo.");
    }

    @DeleteMapping("/opponents/suggestions/image/{className}/{order}")
    public ResponseEntity<?> deleteSuggestionImage(
            @PathVariable("className") @NotBlank String className,
            @PathVariable("order") @Positive int order) {

        suggestionService.deleteSuggestionImage(className, order);

        return ResponseEntity.ok("Immagine eliminata con successo.");
    }

}