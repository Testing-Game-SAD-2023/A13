package com.example.db_setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FriendController {

    @Autowired
    private FriendRepository friendRepository;

    // Endpoint per ottenere la lista degli amici
    @PostMapping("/getFriends")
    public ResponseEntity<List<Map<String, String>>> getFriends(@RequestParam("userId") String userId) {
        try {
            // Recupera la lista degli amici tramite il repository
            List<Map<String, String>> friends = friendRepository.findFriendsByUserId(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            System.out.println("Error retrieving friends: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
