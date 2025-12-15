package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.service.GuidelineService;
import com.groom.manvsclass.validation.ValidOrder;

import com.groom.manvsclass.exception.UnauthorizedException;

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

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

@Validated
@CrossOrigin
@RestController
public class GuidelineController {

    @Autowired
    private GuidelineService guidelineService;

    @PostMapping("/opponents/guidelines/upload")
    public ResponseEntity<?> uploadGuidelines(@Valid @ValidOrder @RequestBody List<GuidelineDTO> guidelineDTOs) {

        guidelineService.uploadGuidelines(guidelineDTOs);
        return ResponseEntity.status(HttpStatus.OK).body("Linee guida caricate con successo.");
    }

    @PostMapping("/opponents/guidelines/upload/{order}")
    public ResponseEntity<?> uploadGuidelineImage(
            @PathVariable("order") @Positive int order,
            @RequestParam("image") @NotNull MultipartFile image) {

        guidelineService.uploadGuidelineImage(order, image);

        return ResponseEntity.ok("Immagine caricata con successo.");

    }

    @GetMapping("/opponents/guidelines")
    public ResponseEntity<?> viewGuidelines() {

        List<GuidelineDTO> guidelines = guidelineService.findGuidelines();
        return ResponseEntity.ok(guidelines);
    }

    @DeleteMapping("/opponents/guidelines/{order}")
    public ResponseEntity<?> deleteGuideline(@PathVariable("order") @Positive int order) {

        guidelineService.deleteGuideline(order);
        return ResponseEntity.status(HttpStatus.OK).body("Linea guida eliminata con successo.");
    }

    @DeleteMapping("/opponents/guidelines/image/{order}")
    public ResponseEntity<?> deleteSuggestionImage(@PathVariable("order") @Positive int order) {

        guidelineService.deleteGuidelineImage(order);

        return ResponseEntity.ok("Immagine eliminata con successo.");
    }

}