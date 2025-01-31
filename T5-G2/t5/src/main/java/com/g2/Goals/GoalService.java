package com.g2.Goals;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Game.GameLogic;

@Service("goalService")
/**
 * Servizio contenente vari overload da chiamare quando avviene un evento interessante per gli achievement
 */
public class GoalService {
    private static final Logger logger = LoggerFactory.getLogger(GoalService.class);
    @Autowired
    private GoalRepository goalRepository;
    /**
     * Procedura da chiamare quando termina una partita. 
     * 
     * @param userData
     * @param gameLogic
     * @param playerId
     * @param robotScore
     * @param playerScore
     */
    public void updateGoalProgresses(Map<String, String> userData, GameLogic gameLogic, int playerId, int robotScore, int playerScore){
        
        List<Goal> goals = goalRepository.findAll(
            playerId,
            null,
            false,
            true
        );
        logger.info("Called updateGoalProgresses. Found "+goals.size()+" goals to update...");
        for(Goal goal : goals){
            boolean esito = goal.match(userData, gameLogic, robotScore, playerScore);
            if(esito){
                goalRepository.update(goal);
                //propagazione evento, email, ecc
            }
        }
    }

}
