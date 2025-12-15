package com.groom.manvsclass.controller;

import com.groom.manvsclass.dto.AssignmentDTO;
import com.groom.manvsclass.service.AssignmentService;
import com.groom.manvsclass.service.SecurityService;
import com.groom.manvsclass.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.groom.manvsclass.exception.UnauthorizedException;

import java.util.List;

@CrossOrigin
@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SecurityService securityService;

    @PostMapping("/creaAssignment/{teamName}")
    public ResponseEntity<?> creaAssignment(@PathVariable String teamName, @RequestBody AssignmentDTO assignmentDTO) {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        assignmentService.createAssignment(teamName, assignmentDTO, adminEmail);
        return ResponseEntity.status(HttpStatus.OK).body("Assignment creato con successo.");
    }

    @GetMapping("/visualizzaTeamAssignments/{teamName}")
    public ResponseEntity<?> viewAdminSingleTeamAssignments(@PathVariable String teamName) {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        List<AssignmentDTO> assignmentDTOs = assignmentService.findAdminSingleTeamAssignments(teamName, adminEmail);
        return ResponseEntity.ok(assignmentDTOs);
    }

    @GetMapping("/visualizzaAssignments")
    public ResponseEntity<?> viewAdminTeamsAssignments() {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        List<AssignmentDTO> assignmentDTOs = assignmentService.findAdminTeamsAssignments(adminEmail);
        return ResponseEntity.ok(assignmentDTOs);
    }

    @DeleteMapping("/deleteAssignment/{assignmentTitle}")
    ResponseEntity<?> deleteAssignment(@PathVariable("assignmentTitle") String assignmentTitle) {

        String jwt = securityService.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        assignmentService.deleteAssignment(assignmentTitle, adminEmail);
        return ResponseEntity.ok("Assignment eliminato con successo.");
    }
}
