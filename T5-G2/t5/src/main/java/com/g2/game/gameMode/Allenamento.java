package com.g2.game.gameMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

public class Allenamento extends GameLogic {
    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(Allenamento.class);

    @JsonProperty("userScore")
    private int userScore;

    public Allenamento() {
    }

    public Allenamento(ServiceManager serviceManager, Long PlayerID, String ClasseUT,
                       String type_robot, OpponentDifficulty difficulty, GameMode gamemode, String testingClassCode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode, testingClassCode);
    }

    @Override
    public void nextTurn(CompileResult userScore, CompileResult robotScore) {
        startTurn();
        logger.info("Created turn {} for game {}", this.getCurrentTurn(), this.getGameID());
        endTurn(userScore);
        System.out.println("[GAME] Turn " + this.getCurrentTurn() + " played. User Score: " + userScore.getInstructionCoverage().getPercentage() + ", Robot Score: " + robotScore.getInstructionCoverage().getPercentage());
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }

    @Override
    public boolean isWinner() {
        return true;
    }

    @Override
    public void startGame() {
        // Il metodo è intenzionalmente vuoto: L'allenamento non deve creare 
    }

    @Override
    public void endGame(boolean isGameSurrendered) {
        // Il metodo è intenzionalmente vuoto: L'allenamento non deve creare 
    }

    @Override
    public int getScore(CompileResult compileResult) {
        // Se loc è 0, il punteggio è sempre 0
        int coverage = compileResult.getLineCoverage().getCovered();
        if (coverage == 0) {
            return 0;
        }
        // Calcolo della percentuale
        double locPerc = ((double) coverage) / 100;
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, this.getCurrentRound());
        // Calcolo del punteggio
        double score = locPerc * 100 * penaltyFactor;
        this.userScore = (int) Math.ceil(score);
        return userScore;
    }
}
