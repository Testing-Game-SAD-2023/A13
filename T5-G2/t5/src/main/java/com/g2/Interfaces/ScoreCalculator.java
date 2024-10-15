package com.g2.Interfaces;

import com.commons.model.StatisticRole;
import com.g2.Model.Game;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ScoreCalculator implements IStatisticCalculator{
    public StatisticRole role = StatisticRole.Score;

    @Override
    public float calculate(List<Game> gamesList) {
        return (float) gamesList.stream().mapToDouble(Game::getScore).sum();
    }

    @Override
    public StatisticRole getRole() {
        return StatisticRole.Score;
    }
}
