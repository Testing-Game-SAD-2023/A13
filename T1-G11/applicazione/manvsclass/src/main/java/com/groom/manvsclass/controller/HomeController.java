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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@CrossOrigin
@RestController
public class HomeController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/Cfilterby/{category}")
    public ResponseEntity<?> filtraClassi(@PathVariable String category) {

        List<ClassUT> filteredClasses = adminService.filtraClassi(category);
        return ResponseEntity.ok(filteredClasses);
    }

    @GetMapping("/Cfilterby/{text}/{category}")
    public ResponseEntity<?> filtraClassi(@PathVariable String text, @PathVariable String category) {

        List<ClassUT> filteredClasses = adminService.filtraClassi(text, category);
        return ResponseEntity.ok(filteredClasses);
    }

}



