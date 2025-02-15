package com.g2.Game.GameFactory;

import org.springframework.stereotype.Component;

import com.g2.Game.GameModes.GameLogic;
import com.g2.Game.GameModes.Sfida;
import com.g2.Interfaces.ServiceManager;

/*
 * La classe SfidaFactory è la fabbrica che si occupa di creare un'istanza di Sfida. 
 * Quando il metodo create viene invocato, la factory costruisce un nuovo oggetto Sfida, 
 * passando i parametri necessari per il suo costruttore.
 */

@Component("Sfida")  // Il nome del bean è la chiave nel registro
public class SfidaFactory implements GameFactoryFunction {
    @Override
    public GameLogic create(ServiceManager sm, String playerId, String underTestClassName, 
                            String type_robot, String difficulty, String mode) {
        return new Sfida(sm, playerId, underTestClassName, type_robot, difficulty, "sfida");
    }
}
