package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins/{username}")
    public ResponseEntity<?> getAdminByUsername(@PathVariable String username) {

        Admin admin = adminService.getAdminByUsername(username);
        return ResponseEntity.ok(admin);
    }
}
