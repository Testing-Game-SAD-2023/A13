/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Interfaces;

import java.util.List;

import org.springframework.stereotype.Component;

import com.commons.model.StatisticRole;
import com.g2.Model.Game;

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
