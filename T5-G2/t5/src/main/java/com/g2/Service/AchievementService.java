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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Map<String, Statistic> GetIdToStatistic(){
        return getStatistics().stream().collect(Collectors.toMap(Statistic::getID, stat -> stat));
    } 
    
    public List<AchievementProgress> getUnlockedAchievementProgress(List<AchievementProgress> achievementProgresses){
        return achievementProgresses.stream().filter(a -> a.getProgress() >= a.getProgressRequired()).toList();
    }
    
    public List<AchievementProgress> getLockedAchievementProgress(List<AchievementProgress> achievementProgresses){
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
        for (AchievementProgress achievement : newAchievements) {
            String titolo = "Nuovo Achievement";
            String message = "Congratulazioni! Hai ottenuto il nuovo achievement: " + achievement.Name + "!";
            serviceManager.handleRequest("T23", "NewNotification", userEmail, titolo, message);

        }
    }
}
