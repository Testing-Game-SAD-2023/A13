package com.groom.manvsclass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.groom.manvsclass.service.ClassUTService;

@CrossOrigin
@Controller
public class ClassUTController {
    
    @Autowired
    private ClassUTService classUTService;

    @GetMapping("/elencoNomiClassiUT")
    public ResponseEntity<?> getNomiClassiUT(@CookieValue(name = "jwt", required = false) String jwt){
        return classUTService.getNomiClassiUT(jwt);
    }

}
