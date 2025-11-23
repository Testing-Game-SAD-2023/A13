package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.service.AdminService;
import com.groom.manvsclass.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

@CrossOrigin
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/admins/{username}")
    public ResponseEntity<?> getAdminByUsername(@PathVariable String username, @CookieValue(name = "jwt", required = false) String jwt) {

        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            Admin admin = adminService.getAdminByUsername(username);
            return ResponseEntity.ok().body(admin);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
