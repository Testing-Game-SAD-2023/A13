package com.groom.manvsclass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.service.AssignmentService;

@CrossOrigin
@Controller
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;

    //Modifica 07/12/2024
    @PostMapping("/creaAssignment/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> creaAssignment(@PathVariable("idTeam") String idTeam,@RequestBody Assignment assignment, @CookieValue(name = "jwt", required = false) String jwt){
        return assignmentService.creaAssignment(assignment, idTeam, jwt);
    }

    //Modifica 08/12/2024 aggiunta nuove rotte
    @GetMapping("/visualizzaTeamAssignments/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> visualizzaTeamAssignments(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt){
        return assignmentService.visualizzaTeamAssignment(idTeam,jwt);
    }
    
    @GetMapping("/visualizzaAssignments")
    @ResponseBody
    public ResponseEntity<?> visualizzaAssignments(@CookieValue(name = "jwt", required = false) String jwt){
        return assignmentService.visualizzaAssignments(jwt);
    }

    @DeleteMapping("/deleteAssignment/{idAssignment}")
    @ResponseBody ResponseEntity<?> deleteAssignment(@PathVariable("idAssignment") String idAssignment,@CookieValue(name = "jwt", required = false) String jwt){
        return assignmentService.deleteAssignment(idAssignment,jwt);
    }


}
