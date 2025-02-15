package com.g2.Game.GameFactory;

import org.springframework.stereotype.Component;

import com.g2.Game.GameModes.Allenamento;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Interfaces.ServiceManager;

@Component("Allenamento")
public class AllenamentoFactory implements GameFactoryFunction {

    @Override
    public GameLogic create(ServiceManager sm, String playerId, String underTestClassName,
            String type_robot, String difficulty, String mode) {
        /*
        * ServiceManager è null, poiché Allenamento non deve salvare
         */
        return new Allenamento(null, playerId, underTestClassName, type_robot, difficulty, "Allenamento");
    }
}
