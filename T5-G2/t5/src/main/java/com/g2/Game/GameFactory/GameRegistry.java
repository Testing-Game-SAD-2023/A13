package com.g2.Game.GameFactory;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.g2.Game.GameModes.GameLogic;
import com.g2.Interfaces.ServiceManager;

/*
 *  Quando chiamiamo createGame("Sfida", ...), Spring trova automaticamente la factory corretta. grazie ai Bean
 *  Espandibilità → Per aggiungere un nuovo gioco, basta creare una nuova factory con @Component.
 */


@Component
public class GameRegistry {
    private final Map<String, GameFactoryFunction> gameRegistry;

    // Spring raccoglie tutte le factory automaticamente e le inietta nella mappa
    public GameRegistry(Map<String, GameFactoryFunction> gameFactories) {
        this.gameRegistry = gameFactories;
    }

    public GameLogic createGame(String mode, ServiceManager sm, 
                                String playerId, String underTestClassName, 
                                String type_robot, String difficulty) {
        GameFactoryFunction factory = gameRegistry.get(mode);
        if (factory == null) {
            throw new IllegalArgumentException("Gioco non registrato: " + mode);
        }
        return factory.create(sm, playerId, underTestClassName, type_robot, difficulty, mode);
    }
}
