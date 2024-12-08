package com.g2.Service;

import java.util.List;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.LeaderboardSubInterval;
import com.g2.Model.PlayerStats;
import com.g2.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LeaderboardService {
    private final ServiceManager serviceManager;

    @Autowired
    public LeaderboardService(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    public LeaderboardSubInterval getLeaderboardSubInterval(String gamemode, String statistic, Integer pageSize,
            Integer numPages, Integer startPage, Integer playerId) {
        try {
            if (startPage == null)
                startPage = 0;
            if (playerId == null)
                playerId = -1;

            LeaderboardSubInterval playerStatsList = (LeaderboardSubInterval) serviceManager.handleRequest("T4",
                    "getPositions", gamemode, statistic,
                    pageSize, numPages, startPage, playerId);

            List<User> userList = (List<User>) serviceManager.handleRequest("T23", "GetUsers");

            for (PlayerStats playerStats : playerStatsList.getPositions()) {
                for (User user : userList) {
                    if (playerStats.getUserId().equals(user.getId())) {
                        playerStats.setEmail(user.getEmail());
                        break;
                    }
                }
            }

            return playerStatsList;
        } catch (Exception e) {
            throw e;
        }
    }

}
