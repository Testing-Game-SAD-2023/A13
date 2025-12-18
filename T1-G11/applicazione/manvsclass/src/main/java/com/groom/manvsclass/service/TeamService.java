package com.groom.manvsclass.service;

import com.groom.manvsclass.model.dto.TeamUpdate;
import com.groom.manvsclass.model.entity.TeamEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.List;

public interface TeamService {

    ResponseEntity<?> creaTeam(TeamEntity teamEntity, @CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> deleteTeam(String idTeam, String jwt);

    ResponseEntity<?> modificaNomeTeam(TeamUpdate request, @CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> visualizzaTeams(@CookieValue(name = "jwt", required = false) String jwt);

    ResponseEntity<?> cercaTeam(String idTeam, String jwt);

    ResponseEntity<?> aggiungiStudenti(String idTeam, List<String> idStudenti, String jwt);

    ResponseEntity<?> ottieniStudentiTeam(String idTeam, String jwt);

    ResponseEntity<?> rimuoviStudenteTeam(String idTeam, String idStudente, String jwt);

    TeamEntity getTeamByStudentId(String idStudente);

    ResponseEntity<?> GetStudentTeam(String studentId, String jwt);

}
