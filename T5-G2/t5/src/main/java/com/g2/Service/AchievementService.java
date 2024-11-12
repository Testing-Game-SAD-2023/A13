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

package com.g2.Service;

import com.commons.model.Gamemode;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AchievementService {
    private final ServiceManager serviceManager;

    @Autowired
    public AchievementService(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    /**
     * @param playerID
     * @return a list of achievements obtained after this update.
     */
    public List<AchievementProgress> updateProgressByPlayer(int playerID) {
        List<Game> gamesList = getGames(playerID);
        List<AchievementProgress> achievementProgressesPrevious = getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList();
        List<Statistic> statisticList = getStatistics();

        for (Statistic statistic : statisticList) {
            List<Game> filteredGameList = gamesList;
            if(statistic.getGamemode() != Gamemode.All)
                filteredGameList = gamesList.stream().filter(game -> Objects.equals(game.getDescription(), statistic.getGamemode().toString())).toList();

            //TODO: filtrare anche per Robot (allo stato attuale non vi è associazione in DB tra robot e partita)

            float statisticValue = statistic.calculate(filteredGameList);
            System.out.println("[CALCULATION] Calculating for games: " + filteredGameList);
            System.out.println("Updated " + statistic.getName() + ": " + statisticValue);
            setProgress(playerID, statistic.getID(), statisticValue);
        }

        List<AchievementProgress> obtainedAchievements = new ArrayList<>(getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList());
        // filter out all the achievements already obtained, and return the others
        obtainedAchievements.removeIf(x -> achievementProgressesPrevious.stream().anyMatch(y -> y.ID.equals(x.ID)));
        System.out.println("Obtained achievements: " + obtainedAchievements);
        return obtainedAchievements;
    }

    private List<Game> getGames(int playerId) {
        return (List<Game>) serviceManager.handleRequest("T4", "getGames", playerId);
    }

    public List<Statistic> getStatistics() {
        return (List<Statistic>) serviceManager.handleRequest("T1", "getStatistics", null);
    }

    private void setProgress(int playerID, String statisticID, float progress) {
        //restTemplate.put("http://t4-g18-app-1:3000/phca/" + playerID + "/" + statisticID, new StatisticProgress(playerID, statisticID, progress));
        serviceManager.handleRequest("T4", "updateStatisticProgress", playerID, statisticID, progress);
    }

    public List<AchievementProgress> getProgressesByPlayer(int playerID) {
        List<Achievement> achievementList = (List<Achievement>) serviceManager.handleRequest("T1", "getAchievements", null);
        List<StatisticProgress> categoryProgressList = getStatisticsByPlayer(playerID);
        List<AchievementProgress> achievementProgresses = new ArrayList<>();
        for (Achievement a : achievementList)
        {
            List<StatisticProgress> filteredList = categoryProgressList.stream().filter(cat -> Objects.equals(cat.getStatisticID(), a.getStatisticID())).toList();
            for (StatisticProgress c : filteredList)
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), c.getProgress()));
            if (filteredList.size() == 0) // if there is no progress recorded, just put progress 0
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), 0));
        }

        return achievementProgresses;
    }

    public List<StatisticProgress> getStatisticsByPlayer(int playerID) {
        List<StatisticProgress> statisticProgresses = (List<StatisticProgress>) serviceManager.handleRequest("T4", "getStatisticsProgresses", playerID);

        if (statisticProgresses == null)
            throw new RuntimeException("Errore nel fetch delle statistiche del giocatore.");

        List<Statistic> statisticList = getStatistics();

        statisticProgresses.removeIf(x -> !statisticList.stream().anyMatch(y -> Objects.equals(y.getID(), x.getStatisticID())));

        for (Statistic statistic : statisticList) {
            // se non c'è il progresso salvato in db, aggiungilo manualmente impostandolo a 0
            if (!statisticProgresses.stream().anyMatch(progress -> Objects.equals(progress.getStatisticID(), statistic.getID())))
                statisticProgresses.add(new StatisticProgress(playerID, statistic.getID(), 0));
        }

        return statisticProgresses;
    }
}
