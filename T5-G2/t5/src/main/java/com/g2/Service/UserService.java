/*

 */

package com.g2.Service;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService {
    private final ServiceManager serviceManager;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    public User getUserbyID(int user_id){
        return (User) serviceManager.handleRequest("T23", "GetUserbyID", user_id);
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
