package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.ScalataDTO;
import com.groom.manvsclass.service.ScalataService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin
@RestController
public class ScalataController {

    @Autowired
    private ScalataService scalataService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SecurityService securityService;

    @PostMapping("/scalata/upload")
    public ResponseEntity<?> uploadScalata(@Valid @RequestBody ScalataDTO scalataDTO) {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        scalataService.uploadScalata(scalataDTO, adminEmail);
        return ResponseEntity.ok("Scalata caricata con successo.");
    }

    @GetMapping("/scalata")
    public ResponseEntity<?> listScalate() {

        List<ScalataDTO> scalataDTOList = scalataService.listScalate();
        return ResponseEntity.ok(scalataDTOList);
    }

    @GetMapping("/scalata/{scalataName}")
    public ResponseEntity<?> findScalataByName(@PathVariable String scalataName) {

        ScalataDTO scalataDTO = scalataService.findScalataByName(scalataName);
        return ResponseEntity.ok(scalataDTO);
    }

    @DeleteMapping("/scalata/{scalataName}")
    public ResponseEntity<?> deleteScalataByName(@PathVariable String scalataName) {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        scalataService.deleteScalataByName(scalataName, adminEmail);
        return ResponseEntity.ok("Scalata eliminata con successo.");
    }

}