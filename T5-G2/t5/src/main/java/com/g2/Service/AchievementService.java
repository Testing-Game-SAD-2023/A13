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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.model.Gamemode;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.Achievement;
import com.g2.Model.AchievementProgress;
import com.g2.Model.Game;
import com.g2.Model.Statistic;
import com.g2.Model.StatisticProgress;

@Service
public class AchievementService {

    private final ServiceManager serviceManager;

    @Autowired
    public AchievementService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    /**
     * @param playerID
     * @return a list of achievements obtained after this update.
     */
    public List<AchievementProgress> updateProgressByPlayer(int playerID) {
        List<Game> gamesList = getGames(playerID);
        Set<String> alreadyObtainedIDs = getProgressesByPlayer(playerID).stream()
                .filter(a -> a.Progress >= a.ProgressRequired)
                .map(a -> a.ID)
                .collect(Collectors.toSet());

        List<Statistic> statisticList = getStatistics();

        for (Statistic statistic : statisticList) {
            List<Game> filteredGameList = (statistic.getGamemode() != Gamemode.All)
                    ? gamesList.stream()
                            .filter(game -> game.getDescription().equals(statistic.getGamemode().toString()))
                            .toList()
                    : gamesList;

            float statisticValue = statistic.calculate(filteredGameList);
            setProgress(playerID, statistic.getID(), statisticValue);
        }

        List<AchievementProgress> obtainedAchievements = getProgressesByPlayer(playerID).stream()
                .filter(a -> a.Progress >= a.ProgressRequired && !alreadyObtainedIDs.contains(a.ID))
                .toList();

        return obtainedAchievements;
    }

    @SuppressWarnings("unchecked")
    private List<Game> getGames(int playerId) {
        return (List<Game>) serviceManager.handleRequest("T4", "getGames", playerId);
    }

    @SuppressWarnings("unchecked")
    public List<Statistic> getStatistics() {
        return (List<Statistic>) serviceManager.handleRequest("T1", "getStatistics");
    }

    private void setProgress(int playerID, String statisticID, float progress) {
        //restTemplate.put("http://t4-g18-app-1:3000/phca/" + playerID + "/" + statisticID, new StatisticProgress(playerID, statisticID, progress));
        serviceManager.handleRequest("T4", "updateStatisticProgress", playerID, statisticID, progress);
    }

    public List<AchievementProgress> getProgressesByPlayer(int playerID) {
        @SuppressWarnings("unchecked")
        List<Achievement> achievementList = (List<Achievement>) serviceManager.handleRequest("T1", "getAchievements");
        List<StatisticProgress> categoryProgressList = getStatisticsByPlayer(playerID);
        List<AchievementProgress> achievementProgresses = new ArrayList<>();

        Map<String, Float> progressMap = categoryProgressList.stream()
                .collect(Collectors.toMap(StatisticProgress::getStatisticID, StatisticProgress::getProgress));

        for (Achievement a : achievementList) {
            float progress = progressMap.getOrDefault(a.getStatisticID(), 0f);
            achievementProgresses.add(
                    new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), progress)
            );
        }
        return achievementProgresses;
    }

    public Map<String, Statistic> GetIdToStatistic() {
        return getStatistics().stream().collect(Collectors.toMap(Statistic::getID, stat -> stat));
    }

    public List<AchievementProgress> getUnlockedAchievementProgress(List<AchievementProgress> achievementProgresses) {
        return achievementProgresses.stream().filter(a -> a.getProgress() >= a.getProgressRequired()).toList();
    }

    public List<AchievementProgress> getLockedAchievementProgress(List<AchievementProgress> achievementProgresses) {
        return achievementProgresses.stream().filter(a -> a.getProgress() < a.getProgressRequired()).toList();
    }

    public List<StatisticProgress> getStatisticsByPlayer(int playerID) {
        @SuppressWarnings("unchecked")
        List<StatisticProgress> statisticProgresses = (List<StatisticProgress>) serviceManager.handleRequest("T4", "getStatisticsProgresses", playerID);
        if (statisticProgresses == null) {
            throw new RuntimeException("Errore nel fetch delle statistiche del giocatore.");
        }
        List<Statistic> statisticList = getStatistics();
        // Ottimizzazione con Set per rimuovere statistiche non valide
        Set<String> validStatisticIDs = statisticList.stream()
                .map(Statistic::getID)
                .collect(Collectors.toSet());
        statisticProgresses.removeIf(x -> !validStatisticIDs.contains(x.getStatisticID()));
        // Ottimizzazione con Set per aggiungere statistiche mancanti
        Set<String> existingStatisticIDs = statisticProgresses.stream()
                .map(StatisticProgress::getStatisticID)
                .collect(Collectors.toSet());
        for (Statistic statistic : statisticList) {
            if (!existingStatisticIDs.contains(statistic.getID())) {
                // Aggiungi la statistica con il nome associato
                statisticProgresses.add(new StatisticProgress(playerID, statistic.getID(), 0, statistic.getName()));
            }
        }

        return statisticProgresses;
    }

    public void updateNotificationsForAchievements(String userEmail, List<AchievementProgress> newAchievements) {
        if (newAchievements.isEmpty()) {
            return; // Evita operazioni inutili

                }newAchievements.forEach(achievement -> {
            String titolo = "Nuovo Achievement";
            String message = "Congratulazioni! Hai ottenuto il nuovo achievement: " + achievement.getName() + "!";
            serviceManager.handleRequest("T23", "NewNotification", userEmail, titolo, message);
        });
    }

}
