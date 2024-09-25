package com.g2.Service;

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
    private RestTemplate restTemplate;

    @Autowired
    public AchievementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
    public List<Achievement> updateProgressByPlayer(int playerID) {
        List<Achievement> obtainedAchievements = new ArrayList<>();

        ResponseEntity<List<Game>> gamesResponseEntity = restTemplate.exchange("http://t4-g18-app-1:3000/games/player/" + playerID,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<String> gamemodes = Arrays.asList("Robot-Randoop", "Robot-Evosuite", "Multiplayer", "Training", "Scalata");

        //System.out.println(progressesResponseEntity.getBody());
        List<Game> gamesList = gamesResponseEntity.getBody();

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

        List<AchievementProgress> achievementProgresses = getProgressesByPlayer(playerID);

        // filter out all the achievements already obtained, and return the others

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

        ResponseEntity<List<CategoryProgress>> progressesResponseEntity = restTemplate.exchange("http://t4-g18-app-1:3000/phca/" + playerID,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<Achievement> achievementList = achievementResponseEntity.getBody();
        List<CategoryProgress> categoryProgressList = progressesResponseEntity.getBody();

        List<AchievementProgress> achievementProgresses = new ArrayList<>();

        for (Achievement a : achievementList)
        {
            List<CategoryProgress> filteredList = categoryProgressList.stream().filter(cat -> cat.Category == a.getCategory()).toList();
            for (CategoryProgress c : filteredList)
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), c.getProgress()));

            if (filteredList.size() == 0) // if there is no progress recorded, just put progress 0
                achievementProgresses.add(new AchievementProgress(a.getID(), a.getName(), a.getDescription(), a.getProgressRequired(), 0));
        }

        return achievementProgresses;
    }
}
