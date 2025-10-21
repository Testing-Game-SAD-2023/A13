package com.g2.game.gameFactory;

import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.PartitaSingolaParams;
import com.g2.game.gameMode.GameLogic;
import com.g2.game.gameMode.PartitaSingola;
import com.g2.interfaces.ServiceManager;
import org.springframework.stereotype.Component;

@Component("PartitaSingola")
public class PartitaSingolaFactory implements GameFactoryFunction {
    /*
    @Override
    public GameLogic create(ServiceManager sm, String playerId, String underTestClassName,
                            String type_robot, String difficulty, String mode) {
        return new PartitaSingola(sm, playerId, underTestClassName, type_robot, difficulty, mode);
    }

     */

    @Override
    public GameLogic create(ServiceManager serviceManager, GameParams params) {
        if (!(params instanceof PartitaSingolaParams))
            throw new IllegalArgumentException("Impossibile creare PartitaSingola, params non è del tipo atteso");
        return new PartitaSingola(serviceManager, params.getPlayerId(), params.getClassUTName(), params.getOpponentType(),
                params.getOpponentDifficulty(), params.getGameMode(), params.getTestClassCode(), ((PartitaSingolaParams) params).getRemainingTime());
    }
}
