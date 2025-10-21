package com.example.db_setup.controller;

import com.example.db_setup.model.dto.gamification.AdminSummaryDTO;
import com.example.db_setup.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(
            summary = "Get all administrators",
            description = "Returns a list of administrators including their first name, last name, and email address",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the list of administrators",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AdminSummaryDTO.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<List<AdminSummaryDTO>> getAllAdmins() {
        List<AdminSummaryDTO> adminSummaryDTOs = adminService.getAllAdmins();
        return ResponseEntity.ok(adminSummaryDTOs);
    }
}
