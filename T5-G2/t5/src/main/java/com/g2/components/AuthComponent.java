/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
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

package com.g2.components;

import com.g2.interfaces.ServiceManager;

/**
 * Classe implementante l'autenticazione delle richieste per la navigazione tra le pagine web servita dal modulo.
 * Non più necessaria in quanto sostituita da un filtro unico per tutte le chiamate HTTP.
 */
/* TODO: controllare che la classe non venga più effettivamente utilizzata e poi rimuoverla */
public class AuthComponent extends ServiceLogicComponent {

    //Questo è a tutti gli effetti un wrapper per avere velocemente il check del authentication.
    public AuthComponent(ServiceManager serviceManager, String jwt) {
        super(serviceManager, "T23", "GetAuthenticated", jwt);
        setErrorCode("Auth_error");
    }

}
