package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/creaAssignment/{idTeam}")
    public ResponseEntity<?> creaAssignment(@PathVariable("idTeam") String idTeam, @RequestBody AssignmentEntity assignmentEntity, @CookieValue(name = "jwt", required = false) String jwt) {
        return assignmentService.creaAssignment(assignmentEntity, idTeam, jwt);
    }

    @GetMapping("/visualizzaTeamAssignments/{idTeam}")
    public ResponseEntity<?> visualizzaTeamAssignments(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return assignmentService.visualizzaTeamAssignment(idTeam, jwt);
    }

    @GetMapping("/visualizzaAssignments")
    public ResponseEntity<?> visualizzaAssignments(@CookieValue(name = "jwt", required = false) String jwt) {
        return assignmentService.visualizzaAssignments(jwt);
    }

    @DeleteMapping("/deleteAssignment/{idAssignment}")
    ResponseEntity<?> deleteAssignment(@PathVariable("idAssignment") String idAssignment, @CookieValue(name = "jwt", required = false) String jwt) {
        return assignmentService.deleteAssignment(idAssignment, jwt);
    }

}
