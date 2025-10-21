/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.game.gameFactory;

import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameMode.GameLogic;
import com.g2.game.gameMode.Sfida;
import com.g2.interfaces.ServiceManager;
import org.springframework.stereotype.Component;

/*
 * La classe SfidaFactory è la fabbrica che si occupa di creare un'istanza di Sfida.
 * Quando il metodo create viene invocato, la factory costruisce un nuovo oggetto Sfida,
 * passando i parametri necessari per il suo costruttore.
 */

@Component("Sfida")  // Il nome del bean è la chiave nel registro
public class SfidaFactory implements GameFactoryFunction {
    /*
    @Override
    public GameLogic create(ServiceManager sm, String playerId, String underTestClassName, 
                            String type_robot, String difficulty, String mode) {
        return new Sfida(sm, playerId, underTestClassName, type_robot, difficulty, mode);
    }

     */

    @Override
    public GameLogic create(ServiceManager serviceManager, GameParams params) {
        return new Sfida(serviceManager, params.getPlayerId(), params.getClassUTName(), params.getOpponentType(),
                params.getOpponentDifficulty(), params.getGameMode(), params.getTestClassCode());
    }

}
