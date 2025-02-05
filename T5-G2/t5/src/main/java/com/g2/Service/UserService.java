/*

 */

package com.g2.Service;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService {
    private final ServiceManager serviceManager;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    public Boolean getAuthenticated(String jwt){
        return (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
    }

    public User getUserbyID(int user_id){
        return (User) serviceManager.handleRequest("T23", "GetUserbyID", user_id);
    }

    public String modifyUser(User user_updated, String old_psw){
        return (String) serviceManager.handleRequest("T23", "ModifyUser", user_updated, old_psw);
    }

    public User searchPlayer(String key_search){
        return (User) serviceManager.handleRequest("T23", "SearchPlayer", key_search);
    }

    public String addFollow(String userID_1, String userID_2){
        return (String) serviceManager.handleRequest("T23", "AddFollow", userID_1, userID_2);
    }

    public String rmFollow(String userID_1, String userID_2){
        return (String) serviceManager.handleRequest("T23", "RmFollow", userID_1, userID_2);
    }


    public boolean isUserInFollower(User user, Integer targetUserId) {
        if (user == null || user.getFollowers() == null) {
            return false;
        }
    
        return user.getFollowers()
                   .stream()
                   .anyMatch(followed -> followed.getId().equals(targetUserId));
    }

}
