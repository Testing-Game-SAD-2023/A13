/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*MODIFICA (5/11/2024) - Refactoring task T1
 * HomeController ora si occupa solo del mapping dei servizi aggiunti.
 */

package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.service.AdminService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import java.util.List;

@CrossOrigin
@RestController
public class HomeController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private Util utilsService;

    //Solo x testing
    @GetMapping("/getLikes/{name}")

    public ResponseEntity<Long> likes(@PathVariable String name) {
        long likesCount = utilsService.likes(name);
        return ResponseEntity.ok(likesCount);
    }

    @PostMapping("/newinteraction")
    public ResponseEntity<Interaction> uploadInteraction(@RequestBody Interaction interazione) {
        Interaction savedInteraction = utilsService.uploadInteraction(interazione);
        return ResponseEntity.ok(savedInteraction);
    }

    @GetMapping("/Cfilterby/{category}")
    public ResponseEntity<?> filtraClassi(@PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {

        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            List<ClassUT> filteredClasses = adminService.filtraClassi(category);
            return ResponseEntity.ok(filteredClasses);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero delle classi: " + e.getMessage());
        }

    }

    @GetMapping("/Cfilterby/{text}/{category}")
    public ResponseEntity<?> filtraClassi(@PathVariable String text, @PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {

        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            List<ClassUT> filteredClasses = adminService.filtraClassi(text, category);
            return ResponseEntity.ok(filteredClasses);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero delle classi: " + e.getMessage());
        }

    }

    @GetMapping("/interaction")
    public List<Interaction> elencaInt() {
        return utilsService.elencaInt();
    }

    @GetMapping("/findReport")
    public List<Interaction> elencaReport() {
        return utilsService.elencaReport();
    }

    @PostMapping("/newLike/{name}")
    public String newLike(@PathVariable String name) {
        return utilsService.newLike(name);
    }

    @PostMapping("/newReport/{name}")
    public String newReport(@PathVariable String name, @RequestBody String commento) {
        return utilsService.newReport(name, commento);
    }

    @PostMapping("/deleteint/{id}")
    public Interaction eliminaInteraction(@PathVariable String id) {

        Long interactionId = Long.parseLong(id);
        return utilsService.eliminaInteraction(interactionId);
    }
}



