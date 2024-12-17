package com.g2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.g2.Model.Player;
import com.g2.Repository.PlayerRepository;

import java.util.List;


//servizio che implementa le funzioni CRUD (CREATE, REMOVE, UPDATE, DELETE) verso il database LeaderboardStats
@Service
public class LeaderboardDatabaseService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player createOrUpdatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player getPlayerByMail(String mail) {
        return playerRepository.findByMail(mail);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public void deletePlayerByMail(String mail) {
        playerRepository.deleteById(mail);
    }
}
