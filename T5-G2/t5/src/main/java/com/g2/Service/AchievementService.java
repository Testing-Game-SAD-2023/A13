package com.g2.Service;

import com.commons.model.Gamemode;
import com.commons.model.Robot;
import com.commons.model.StatisticRole;
import com.g2.Interfaces.IStatisticCalculator;
import com.g2.Model.*;
import com.g2.factory.StatisticCalculatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AchievementService {
    private RestTemplate restTemplate;

    @Autowired
    public AchievementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private List<Statistic> testStatistics = Arrays.asList(
            new Statistic(1, "Test1", Robot.None, Gamemode.All, StatisticRole.GamesWon),
            new Statistic(2, "Test2", Robot.None, Gamemode.All, StatisticRole.GamesPlayed),
            new Statistic(3, "Test3", Robot.None, Gamemode.All, StatisticRole.Score)
    );

    Map<String, Integer> GamemodeToInt = Map.ofEntries(
        Map.entry("Games-Total" , 1),
        Map.entry("Games-Robot-Randoop" , 2),
        Map.entry("Games-Robot-Evosuite" , 3),
        Map.entry("Games-Multiplayer" , 4),
        Map.entry("Games-Training" , 5),
        Map.entry("Games-Scalata" , 6),
        Map.entry("Scores-Robot-Randoop" , 7),
        Map.entry("Scores-Robot-Evosuite" , 8),
        Map.entry("Scores-Multiplayer" , 9),
        Map.entry("Scores-Training" , 10),
        Map.entry("Scores-Scalata" , 11)
    );

    /**
     * @param playerID
     * @return a list of achievements obtained after this update.
     */
    public List<AchievementProgress> updateProgressByPlayer(int playerID) {
        ResponseEntity<List<Game>> gamesResponseEntity = restTemplate.exchange("http://t4-g18-app-1:3000/games/player/" + playerID,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<String> gamemodes = Arrays.asList("Robot-Randoop", "Robot-Evosuite", "Multiplayer", "Training", "Scalata");

        //System.out.println(progressesResponseEntity.getBody());
        List<Game> gamesList = gamesResponseEntity.getBody();

        List<AchievementProgress> achievementProgressesPrevious = getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList();

        for (Statistic statistic : testStatistics)
            System.out.println("CALCOLATO: " + statistic.calculate(gamesList));

        int totalGamesCount = gamesList.size();

        setProgress(playerID, GamemodeToInt.get("Games-Total"), totalGamesCount);

        for (String gm : gamemodes) {
            int gamemodeGamesCount = (int) gamesList.stream().filter(l -> Objects.equals(l.getDescription(), gm)).count();
            int category = GamemodeToInt.get("Games-" + gm);

            setProgress(playerID, category, gamemodeGamesCount);
        }

        for (String gm : gamemodes) {
            float scoreSum = (float) gamesList.stream().filter(l -> Objects.equals(l.getDescription(), gm)).mapToDouble(Game::getScore).sum();
            int category = GamemodeToInt.get("Scores-" + gm);

            setProgress(playerID, category, scoreSum);
        }

        List<AchievementProgress> obtainedAchievements = new ArrayList<>(getProgressesByPlayer(playerID).stream().filter(a -> a.Progress >= a.ProgressRequired).toList());

        // filter out all the achievements already obtained, and return the others
        obtainedAchievements.removeIf(x -> achievementProgressesPrevious.stream().anyMatch(y -> y.ID.equals(x.ID)));

        System.out.println("Obtained achievements: " + obtainedAchievements);
        return obtainedAchievements;
    }

    private void setProgress(int playerID, int category, float progress) {
        restTemplate.put("http://t4-g18-app-1:3000/phca/" + playerID + "/" + category,
                new StatisticProgress(playerID, category, progress));
    }

    public List<AchievementProgress> getProgressesByPlayer(int playerID) {
        ResponseEntity<List<Achievement>> achievementResponseEntity = restTemplate.exchange("http://manvsclass-controller-1:8080/achievements/list",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        ResponseEntity<List<StatisticProgress>> progressesResponseEntity = restTemplate.exchange("http://t4-g18-app-1:3000/phca/" + playerID,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<Achievement> achievementList = achievementResponseEntity.getBody();
        List<StatisticProgress> categoryProgressList = progressesResponseEntity.getBody();

        List<AchievementProgress> achievementProgresses = new ArrayList<>();

        for (Achievement a : achievementList)
        {
            List<StatisticProgress> filteredList = categoryProgressList.stream().filter(cat -> cat.getStatistic() == a.getStatistic()).toList();

            for (StatisticProgress c : filteredList)
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), c.getProgress()));

            if (filteredList.size() == 0) // if there is no progress recorded, just put progress 0
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), 0));
        }

        return achievementProgresses;
    }
}
