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

package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.InteractionDTO;
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
public class InteractionController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private Util utilsService;

    @GetMapping("/getLikes/{className}")
    public ResponseEntity<?> likes(@PathVariable String className) {

        try {
            long likesCount = utilsService.getClassLikes(className);
            return ResponseEntity.ok(likesCount);
        } catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/interaction")
    public List<InteractionDTO> elencaInt() {

        return utilsService.elencaInt();
    }

    @GetMapping("/findReport")
    public List<InteractionDTO> elencaReport() {

        return utilsService.elencaReport();
    }

    @PostMapping("/interaction/upload")
    public ResponseEntity<?> uploadInteraction(@RequestBody InteractionDTO interactionDTO) {

        try {
            utilsService.uploadInteraction(interactionDTO);
            return ResponseEntity.ok().body("");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/deleteint/{interactionId}")
    public void eliminaInteraction(@PathVariable String interactionId) {

        utilsService.eliminaInteraction(Long.parseLong(interactionId));
    }
}