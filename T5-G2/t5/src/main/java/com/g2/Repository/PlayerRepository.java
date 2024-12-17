package com.g2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.g2.Model.Player;


public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByMail(String mail);
}
