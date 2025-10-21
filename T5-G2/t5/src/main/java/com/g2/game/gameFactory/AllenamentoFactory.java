package com.g2.game.gameFactory;

import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameMode.Allenamento;
import com.g2.game.gameMode.GameLogic;
import com.g2.interfaces.ServiceManager;
import org.springframework.stereotype.Component;

@Component("Allenamento")
public class AllenamentoFactory implements GameFactoryFunction {
    /*
    @Override
    public GameLogic create(ServiceManager sm, String playerId, String underTestClassName,
                            String type_robot, String difficulty, String mode) {
        /*
        * ServiceManager è null, poiché Allenamento non deve salvare

        return new Allenamento(null, playerId, underTestClassName, type_robot, difficulty, mode);
    }
    */

    @Override
    public GameLogic create(ServiceManager serviceManager, GameParams params) {
        return new Allenamento(null, params.getPlayerId(), params.getClassUTName(), params.getOpponentType(),
                params.getOpponentDifficulty(), params.getGameMode(), params.getTestClassCode());
    }
}

