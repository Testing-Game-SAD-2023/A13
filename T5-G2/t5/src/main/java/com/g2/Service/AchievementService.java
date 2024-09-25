package com.g2.Service;

import com.g2.Model.Achievement;
import com.g2.Model.AchievementProgress;
import com.g2.Model.CategoryProgress;
import com.g2.Model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AchievementService {
    private RestTemplate restTemplate;

    @Autowired
    public AchievementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param playerID
     * @return a list of achievements obtained after this update.
     */
    public List<Achievement> updateProgressByPlayer(int playerID) {
        // Global progress updater. It should get:
        // 1. Number of total games by the player (played + won)
        // 2. Number of games of the player by gamemodes: (played + won)
        //      a. Robot game (for every robot)
        //      b. Multiplayer game
        //      c. Training
        //      d. Scalata game
        // 3. Same as 2 but with score

        List<Achievement> obtainedAchievements = new ArrayList<>();

        ResponseEntity<List<Game>> progressesResponseEntity = restTemplate.exchange("http://t4-g18-app-1:3000/games/player/" + playerID,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<String> gamemodes = Arrays.asList("Robot-Randoop", "Robot-Evosuite", "Multiplayer", "Training", "Scalata-Game");

        //System.out.println(progressesResponseEntity.getBody());
        List<Game> gamesList = progressesResponseEntity.getBody();

        System.out.println("Total games played: " + gamesList.size());

        double globalScore = gamesList.stream().mapToDouble(Game::getScore).sum();
        System.out.println("Total score (useless): " + globalScore);

        for (String gm : gamemodes)
            System.out.println("Games played of gamemode " + gm + ": " + gamesList.stream().filter(l -> Objects.equals(l.getDescription(), "Robot")).count());

        for (String gm : gamemodes) {
            double scoreSum = gamesList.stream().filter(l -> Objects.equals(l.getDescription(), "Robot")).mapToDouble(Game::getScore).sum();
            System.out.println("Score obtained by gamemode " + gm + ": " + scoreSum);
        }

        return  obtainedAchievements;
    }

    private void setProgress(int playerID, int category) {
        // TODO: set current progress by pid and category
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
        }

        return achievementProgresses;
    }
}
