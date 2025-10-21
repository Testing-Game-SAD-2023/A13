package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username, @CookieValue(name = "jwt", required = false) String jwt) {
        return adminService.getAdminByUsername(username, jwt);
    }
}
