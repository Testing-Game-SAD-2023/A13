package com.g2.game.gameMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.PartitaSingolaParams;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Getter
@Setter
@ToString
public class PartitaSingola extends TurnBasedGame {

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(PartitaSingola.class);

    public PartitaSingola() {
        //Costruttore vuoto
    }

    //Questa classe si specializza in una partita singola basata sui turni, prende il nome di Partita Singola nella UI
    public PartitaSingola(ServiceManager serviceManager, Long playerId, String classUT,
                          String opponentType, OpponentDifficulty difficulty, GameMode gameMode, String testingClassCode) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gameMode, testingClassCode);
    }

    public PartitaSingola(ServiceManager serviceManager, Long playerId, String classUT,
                          String opponentType, OpponentDifficulty difficulty, GameMode gamemode, String testingClassCode, int remainingTime) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gamemode, testingClassCode, remainingTime);
    }

    @Override
    public void updateState(GameParams gameParams, CompileResult userCompileResult, CompileResult robotCompileResult) {
        if (!(gameParams instanceof PartitaSingolaParams))
            throw new IllegalArgumentException("Impossibile aggiornare la logica corrente, i parametri ricevuti non son istanza di PartitaSingolaParams");
        super.updateState(gameParams, userCompileResult, robotCompileResult);
        this.remainingTime = ((PartitaSingolaParams) gameParams).getRemainingTime();
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }
}
