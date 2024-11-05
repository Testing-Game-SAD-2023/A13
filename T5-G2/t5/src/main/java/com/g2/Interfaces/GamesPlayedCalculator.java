package com.g2.Interfaces;

import com.commons.model.StatisticRole;
import com.g2.Model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GamesPlayedCalculator implements IStatisticCalculator{
    @Override
    public float calculate(List<Game> gamesList) {
        return gamesList.size();
    }

    @Override
    public StatisticRole getRole() {
        return StatisticRole.GamesPlayed;
    }
}
