package com.g2.Game.GameFactory;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Interfaces.ServiceManager;

/*
 * Interfaccia con cui si crea un nuovo gioco di tipo GameLogic
 */
@FunctionalInterface
public interface GameFactoryFunction {
    GameLogic create(ServiceManager serviceManager,
                     String playerId, String underTestClassName,
                     String type_robot, String difficulty, String mode);
}
