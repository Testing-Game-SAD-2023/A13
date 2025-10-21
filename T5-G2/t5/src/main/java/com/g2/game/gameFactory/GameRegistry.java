package com.g2.game.gameFactory;

import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameMode.GameLogic;
import com.g2.interfaces.ServiceManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
 *  Quando chiamiamo createGame("Sfida", ...), Spring trova automaticamente la factory corretta. grazie ai Bean
 *  Espandibilità → Per aggiungere un nuovo gioco, basta creare una nuova factory con l'interfaccia GameFactoryFunction
 *  con @Component('nome modalità')
 */
@Component
public class GameRegistry {
    /*
     * Quando si dichiara una mappa con il tipo Map<String, GameFactoryFunction>,
     * Spring popola automaticamente la mappa con tutte le istanze di GameFactoryFunction disponibili nel contesto,
     *  usando il nome del bean come chiave.
     */
    private final Map<String, GameFactoryFunction> gameFactories;

    // Spring raccoglie tutte le factory automaticamente e le inietta nella mappa
    public GameRegistry(Map<String, GameFactoryFunction> gameFactories) {
        this.gameFactories = gameFactories;
        /*
         * Stampo tutte le chiavi all'avvio
         */
        System.out.println("Factory registrate: " + gameFactories.keySet());
    }

    public GameLogic createGame(ServiceManager sm, GameParams gameParams) {

        GameFactoryFunction factory = gameFactories.get(gameParams.getGameMode().name());
        if (factory == null) {
            throw new IllegalArgumentException("Gioco non registrato: " + gameParams.getGameMode());
        }

        return factory.create(sm, gameParams);
    }
}
