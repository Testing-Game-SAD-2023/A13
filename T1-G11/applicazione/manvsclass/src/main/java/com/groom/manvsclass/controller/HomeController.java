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
import com.groom.manvsclass.model.interaction;
import com.groom.manvsclass.service.AdminService;
import com.groom.manvsclass.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class HomeController {

    private final AdminService adminService;
    private final Util utilsService;

    public HomeController(AdminService adminService, Util utilsService) {
        this.adminService = adminService;
        this.utilsService = utilsService;
    }

    //Solo x testing
    @GetMapping("/getLikes/{name}")

    public ResponseEntity<Long> likes(@PathVariable String name) {
        long likesCount = utilsService.likes(name);
        return ResponseEntity.ok(likesCount);
    }

    @PostMapping("/newinteraction")
    public ResponseEntity<interaction> uploadInteraction(@RequestBody interaction interazione) {
        interaction savedInteraction = utilsService.uploadInteraction(interazione);
        return ResponseEntity.ok(savedInteraction);
    }

    @GetMapping("/Cfilterby/{category}")
    public ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {
        return adminService.filtraClassi(category, jwt);
    }

    @GetMapping("/Cfilterby/{text}/{category}")
    public ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String text, @PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {
        return adminService.filtraClassi(text, category, jwt);
    }

    @GetMapping("/interaction")
    public List<interaction> elencaInt() {
        return utilsService.elencaInt();
    }

    @GetMapping("/findReport")
    public List<interaction> elencaReport() {
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
    public interaction eliminaInteraction(@PathVariable int id) {
        return utilsService.eliminaInteraction(id);
    }
}



