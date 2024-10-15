package com.g2.Service;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AchievementService {
    private final ServiceManager serviceManager;
    private RestTemplate restTemplate;

    @Autowired
    public AchievementService(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
        this.restTemplate = restTemplate;
    }

    /**
     * @param playerID
     * @return a list of achievements obtained after this update.
     */
    public List<AchievementProgress> updateProgressByPlayer(int playerID) {
        List<Game> gamesList = getGames();

        List<AchievementProgress> achievementProgressesPrevious = getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList();
        List<Statistic> statisticList = getStatistics();

        for (Statistic statistic : statisticList) {
            float statisticValue = statistic.calculate(gamesList);
            System.out.println("Updated " + statistic.getName() + ": " + statisticValue);
            setProgress(playerID, statistic.getID(), statisticValue);
        }

        List<AchievementProgress> obtainedAchievements = new ArrayList<>(getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList());

        // filter out all the achievements already obtained, and return the others
        obtainedAchievements.removeIf(x -> achievementProgressesPrevious.stream().anyMatch(y -> y.ID.equals(x.ID)));

        System.out.println("Obtained achievements: " + obtainedAchievements);
        return obtainedAchievements;
    }

    private List<Game> getGames() {
        return (List<Game>) serviceManager.handleRequest("T4", "getGames", null);
    }

    public List<Statistic> getStatistics() {
        return (List<Statistic>) serviceManager.handleRequest("T1", "getStatistics", null);
    }

    private void setProgress(int playerID, String statisticID, float progress) {
        //TODO: integrare con Service Manager
        restTemplate.put("http://t4-g18-app-1:3000/phca/" + playerID + "/" + statisticID,
                new StatisticProgress(playerID, statisticID, progress));
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

        System.out.println(statisticList);
        System.out.println(statisticProgresses);
        statisticProgresses.removeIf(x -> !statisticList.stream().anyMatch(y -> Objects.equals(y.getID(), x.getStatisticID())));

        return statisticProgresses;
    }
}
