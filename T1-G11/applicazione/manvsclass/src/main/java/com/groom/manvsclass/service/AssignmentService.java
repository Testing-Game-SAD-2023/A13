package com.groom.manvsclass.service;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

public interface AssignmentService {

    ResponseEntity<?> creaAssignment(AssignmentEntity assignmentEntity,
                                     String idTeam,
                                     @CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> visualizzaTeamAssignment(String idTeam, @CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> visualizzaAssignments(@CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> deleteAssignment(String idAssignment, String jwt);
}
