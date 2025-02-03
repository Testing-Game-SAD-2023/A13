package com.g2.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.Assignment;
import com.g2.Model.DTO.ResponseTeamComplete;
import com.g2.Model.Team;
import com.g2.Model.User;

public class TeamService {

        private final ServiceManager serviceManager;

        @Autowired
        public TeamService(RestTemplate restTemplate) {
            this.serviceManager = new ServiceManager(restTemplate);
        } 

        /*
        *    Fornisco Id del student e ottengo il team e recupero gli studenti che ne fanno parte da T23
        */
        public Team GetUsersByTeam(String IdStudent){
            ResponseTeamComplete Team_all_info = (ResponseTeamComplete) serviceManager.handleRequest("T1", "OttieniTeamCompleto", IdStudent);
            //Ora dagli Id recupero i profili degli users
            Team team = Team_all_info.getTeam();
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsersByList", team.getStudenti());
            team.setUserList(users);
            List<Assignment> assignments = Team_all_info.getAssignments();
            team.setAssignments(assignments);
            return team;
        }
}
