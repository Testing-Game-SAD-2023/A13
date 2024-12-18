/*MODIFICA (5/11/2024) - Refactoring task T1
 * AchievementService ora si occupa di implementare i servizi relativi alle statistiche
 */
package com.groom.manvsclass.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import com.groom.manvsclass.model.Achievement;
import com.groom.manvsclass.model.Statistic;
import com.groom.manvsclass.repository.AchievementRepository;
import com.groom.manvsclass.repository.StatisticRepository;
import com.commons.model.Gamemode;
import com.commons.model.Robot;
import com.commons.model.StatisticRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    @Autowired
    private JwtService jwtService;

    // MODIFICA (07/10/2024) : Inizializzazione del repository per le Statistiche
    @Autowired
    private StatisticRepository statisticRepository;

    // MODIFICA (18/09/2024) : Inizializzazione del repository per gli Achievement
    @Autowired
    private AchievementRepository achievementRepository;

    public ModelAndView showAchievementsPage(HttpServletRequest request, String jwt) {
        System.out.println("(GET /achievements) Token JWT valido?");
        if (jwtService.isJwtValid(jwt)) {
            ModelAndView model = new ModelAndView("achievements");

            List<Gamemode> allGamemodes = Arrays.asList(Gamemode.values());
            List<StatisticRole> allRoles = Arrays.asList(StatisticRole.values());
            List<Robot> allRobots = Arrays.asList(Robot.values());

            List<Statistic> allStatistics = statisticRepository.findAll();

            model.addObject("gamemodesList", allGamemodes);
            model.addObject("rolesList", allRoles);
            model.addObject("robotsList", allRobots);
            model.addObject("statisticsList", allStatistics);

            return model;
        }

        System.out.println("(GET /achievements) Token JWT invalido");
        return new ModelAndView("login_admin");
    }

    public ResponseEntity<?> listAchievements() {
        System.out.println("(GET /achievements/list) Recupero degli achievement memorizzati nel sistema.");
        List<Achievement> achievements = achievementRepository.findAll();
        System.out.println("(GET /achievements/list) Recupero achievement avvenuto con successo.");
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    public Object createAchievement(Achievement achievement, String jwt, HttpServletRequest request) {
        System.out.println("(POST /createAchievement) Token JWT valido?");
        if (!jwtService.isJwtValid(jwt)) {
            System.out.println("(POST /createAchievement) Token non valido");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("(POST /createAchivement) Attenzione, non sei loggato!");
        }

        System.out.println(
                "(POST /createAchievement) Token valido, procedere con la configurazione della propria 'Scalata'.");
        achievementRepository.save(achievement);
        System.out.println("(POST /createAchievement) Salvataggio avvenuto correttamente all'interno del DB");

        return showAchievementsPage(request, jwt);
    }

    public ResponseEntity<?> listStatistics() {
        System.out.println("(GET /statistics/list) Recupero delle statistiche memorizzate nel sistema.");
        List<Statistic> statistics = statisticRepository.findAll();
        System.out.println("(GET /statistics/list) Recupero statistiche avvenuto con successo.");
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    public Object createStatistic(Statistic statistic, String jwt, HttpServletRequest request) {
        System.out.println("(POST /createStatistic) Token JWT valido?");
        if (!jwtService.isJwtValid(jwt)) {
            System.out.println("(POST /createStatistic) Token non valido");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("(POST /createStatistic) Attenzione, non sei loggato!");
        }

        System.out.println("(POST /createStatistic) Token valido.");
        statisticRepository.save(statistic);
        System.out.println("(POST /createStatistic) Salvataggio avvenuto correttamente all'interno del DB");

        return showAchievementsPage(request, jwt);
    }

    public Object deleteStatistic(String Id, String jwt, HttpServletRequest request) {
        System.out.println("(DELETE /deleteStatistic) Token JWT valido?");
        if (!jwtService.isJwtValid(jwt)) {
            System.out.println("(DELETE /deleteStatistic) Token non valido");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("(POST /deleteStatistic) Attenzione, non sei loggato!");
        }

        System.out.println("(DELETE /deleteStatistic) Token valido.");
        System.out.println("(DELETE /deleteStatistic) Deleting by Id:" + Id + ".");
        statisticRepository.deleteById(Id);
        System.out.println("(DELETE /deleteStatistic) Salvataggio avvenuto correttamente all'interno del DB");

        return new ModelAndView("achievements");
    }
}
