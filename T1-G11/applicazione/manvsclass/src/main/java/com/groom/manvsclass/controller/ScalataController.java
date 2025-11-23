package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.dto.ScalataDTO;
import com.groom.manvsclass.service.ScalataService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.security.JwtRequestContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class ScalataController {

    @Autowired
    private ScalataService scalataService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/scalata/upload")
    public ResponseEntity<?> uploadScalata(@RequestBody ScalataDTO scalataDTO) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'admin dal token JWT.");
        }

        try {
            scalataService.uploadScalata(scalataDTO, adminEmail);
            return ResponseEntity.ok().body("");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la creazione: " + e.getMessage());
        }
    }

    @GetMapping("/scalata")
    @ResponseBody
    public ResponseEntity<?> listScalate() {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'admin dal token JWT");
        }

        try {
            List<ScalataDTO> scalataDTOList = scalataService.listScalate();
            return ResponseEntity.ok(scalataDTOList);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero delle scalate: " + e.getMessage());
        }
    }

    @DeleteMapping("/scalata/{scalataName}")
    @ResponseBody
    public ResponseEntity<?> deleteScalataByName(@PathVariable String scalataName) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'admin dal token JWT");
        }

        try {
            scalataService.deleteScalataByName(scalataName, adminEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Scalata con nome: " + scalataName + " rimossa");

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ForbiddenException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    @GetMapping("/scalata/{scalataName}")
    @ResponseBody
    public ResponseEntity<?> retrieveScalataByName(@PathVariable String scalataName) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        try {
            ScalataDTO scalataDTO = scalataService.findScalataByName(scalataName);
            return ResponseEntity.status(HttpStatus.OK).body(scalataDTO);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero della scalata: " + e.getMessage());
        }
    }
}
