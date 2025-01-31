package com.g2.Service;

import com.g2.Model.Player;
import java.util.Comparator;

public class PlayerComparatorService {

    public Comparator<Player> getComparator(String sortOrder) {
        switch (sortOrder) {
            case "totalMatches":
                return Comparator.comparing(Player::getMatches).reversed();
            case "wins":
                return Comparator.comparing(Player::getWins).reversed();
            case "totalScore":
                return Comparator.comparing(Player::getTotalScore).reversed();
            default:
                return null;
        }
    }
}