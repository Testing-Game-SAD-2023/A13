package com.g2.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    private LeaderboardSubInterval getUserEmailsById(LeaderboardSubInterval playerStatsList) throws Exception {
        try {
            List<User> userList = (List<User>) serviceManager.handleRequest("T23", "GetUsers");

            Map<Long,User> userMap=new HashMap<>();
            for(User user: userList){
                userMap.put(user.getId(),user);
            }

            for (PlayerStats playerStats : playerStatsList.getPositions()) {
                User matchingUser = userMap.get(playerStats.getUserId());
                if(matchingUser!=null){
                    playerStats.setEmail(matchingUser.getEmail());
                }
            }
            return playerStatsList;
        } catch (Exception e) {
            throw e;
        }
    }

    public LeaderboardSubInterval getLeaderboardSubIntervalByPage(String gamemode, String statistic, Integer pageSize,
            Integer numPages, Integer startPage) throws Exception {

        try {
            LeaderboardSubInterval playerStatsList = new LeaderboardSubInterval();

            playerStatsList = (LeaderboardSubInterval) serviceManager.handleRequest("T4",
                    "getLeaderboardSubinterval", gamemode, statistic,
                    pageSize, numPages, startPage, (long) -1);

            playerStatsList = getUserEmailsById(playerStatsList);

            return playerStatsList;
        } catch (Exception e) {
            throw e;
        }
    }

    public LeaderboardSubInterval getLeaderboardSubIntervalByEmail(String gamemode, String statistic, Integer pageSize,
            Integer numPages, String email) throws Exception {
        try {
            LeaderboardSubInterval playerStatsList = new LeaderboardSubInterval();

            User searchedUser = (User) serviceManager.handleRequest("T23", "GetUserByEmail", email);
            
            if (searchedUser != null) {
                Long userId = searchedUser.getId();
                playerStatsList = (LeaderboardSubInterval) serviceManager.handleRequest("T4",
                        "getLeaderboardSubinterval", gamemode, statistic,
                        pageSize, numPages, 0, userId);

                playerStatsList = getUserEmailsById(playerStatsList);
            }

            return playerStatsList;
        } catch (Exception e) {
            throw e;
        }
    }

}
