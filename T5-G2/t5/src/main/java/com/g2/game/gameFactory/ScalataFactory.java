package com.g2.game.gameFactory;

import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.ScalataParams;
import com.g2.game.gameMode.GameLogic;
import com.g2.game.gameMode.ScalataGame;
import com.g2.interfaces.ServiceManager;
import org.springframework.stereotype.Component;

@Component("Scalata")
public class ScalataFactory implements GameFactoryFunction {

    @Override
    public GameLogic create(ServiceManager serviceManager, GameParams params) {
        if (!(params instanceof ScalataParams))
            throw new IllegalArgumentException("Impossibile creare Scalata, params non è del tipo atteso");
        return new ScalataGame( serviceManager, params.getPlayerId(),  params.getClassUTName(),  params.getOpponentType(),
                params.getOpponentDifficulty(), params.getGameMode(), params.getTestClassCode(), ((ScalataParams) params).getRemainingTime(),
                ((ScalataParams) params).getScalataName(), ((ScalataParams) params).getCurrentLevel(), ((ScalataParams) params).getTotalLevels(),
                ((ScalataParams) params).getTimeMaxPerLevel()
        );
    }
}
