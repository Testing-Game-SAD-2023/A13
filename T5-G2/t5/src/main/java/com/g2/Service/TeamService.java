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

}
