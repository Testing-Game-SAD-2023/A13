package com.g2.Interfaces;

import com.commons.model.StatisticRole;
import com.g2.Model.Game;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamesWonCalculator implements IStatisticCalculator{

    public StatisticRole role = StatisticRole.GamesWon;

    @Override
    public float calculate(List<Game> gamesList) {
        // TODO: filter for won games
        return gamesList.size();
    }

    @Override
    public StatisticRole getRole() {
        return StatisticRole.GamesWon;
    }
}
