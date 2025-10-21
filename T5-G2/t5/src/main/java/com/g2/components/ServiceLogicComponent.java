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
 * Componente che utilizza il ServiceManager per eseguire logica di controllo o
 * di business, senza restituire oggetti da inserire nel modello.
 */
public class ServiceLogicComponent extends GenericLogicComponent {

    private final String serviceName;
    private final String action;
    private final Object[] params;
    protected ServiceManager serviceManager;

    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param serviceName    il nome del servizio su cui eseguire la logica.
     * @param action         l'azione da eseguire sul servizio.
     * @param params         eventuali parametri per l'azione del servizio.
     */
    public ServiceLogicComponent(ServiceManager serviceManager, String serviceName, String action, Object... params) {
        this.serviceManager = serviceManager;
        this.serviceName = serviceName;
        this.action = action;
        this.params = params;
        errorCode = "Service_Logic_error_1";
    }

    /**
     * Esegue la logica utilizzando il ServiceManager. il risultato è
     * considerato booleano. per altre implementazioni va sovraccaricato questo
     * metodo.
     *
     * @return true se la logica è stata eseguita con successo, altrimenti
     * false.
     */
    @Override
    public boolean executeLogic() {
        try {
            // Esegue la logica richiesta tramite il ServiceManager
            return (Boolean) serviceManager.handleRequest(serviceName, action, params);
        } catch (Exception e) {
            // Gestione dell'eccezione, ad esempio log dell'errore
            return false;
        }
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
