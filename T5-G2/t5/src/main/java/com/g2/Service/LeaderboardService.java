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
            Integer numPages, Integer startPage, String email) throws Exception {

        if (startPage == null)
            startPage = 0;
        if (email == null)
            email = "";
        
        if ((startPage == 0 && email.length() == 0) || (startPage > 0 && email.length() > 0)) {
            throw new Exception();
        }
        
        try {
            LeaderboardSubInterval playerStatsList = new LeaderboardSubInterval();
            // get leaderboard interval by page
            if (startPage > 0) {
                playerStatsList = (LeaderboardSubInterval) serviceManager.handleRequest("T4",
                        "getLeaderboardSubinterval", gamemode, statistic,
                        pageSize, numPages, startPage, (long)-1);

                List<User> userList = (List<User>) serviceManager.handleRequest("T23", "GetUsers");

                for (PlayerStats playerStats : playerStatsList.getPositions()) {
                    for (User user : userList) {
                        if (playerStats.getUserId().equals(user.getId())) {
                            playerStats.setEmail(user.getEmail());
                            break;
                        }
                    }
                }
            }
            // get leaderboard interval by player email
            else { 
                User searchedUser = (User) serviceManager.handleRequest("T23", "GetUserByEmail", email);
                Long userId = searchedUser.getId();

                playerStatsList = (LeaderboardSubInterval) serviceManager.handleRequest("T4",
                        "getLeaderboardSubinterval", gamemode, statistic,
                        pageSize, numPages, 0, userId);

                List<User> userList = (List<User>) serviceManager.handleRequest("T23", "GetUsers");

                for (PlayerStats playerStats : playerStatsList.getPositions()) {
                    for (User user : userList) {
                        if (playerStats.getUserId().equals(user.getId())) {
                            playerStats.setEmail(user.getEmail());
                            break;
                        }
                    }
                }
            }

            return playerStatsList;
        } catch (Exception e) {
            throw e;
        }
    }

}
