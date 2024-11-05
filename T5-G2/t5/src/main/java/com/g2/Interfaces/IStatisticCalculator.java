package com.g2.Interfaces;

import com.commons.model.StatisticRole;
import com.g2.Model.Game;

import java.util.List;

public interface IStatisticCalculator {
    public StatisticRole role = null;

    public float calculate(List<Game> gamesList);
    public StatisticRole getRole();
}
