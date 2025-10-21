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

public abstract class GenericLogicComponent {
    /**
     * L'error code deve essere una stringa esplicitamente definita nella classe
     */
    protected String errorCode;

    /**
     * Questo metodo esegue la logica del componente nel modo in cui è stata
     * definita quindi se restituisce True non ci sono stati problemi, nel caso
     * false invece devo segnalare.
     *
     * @return
     */
    public abstract boolean executeLogic();

    /**
     * Con questo metodo segnalo che una logica all'esecuzione ha generato un
     * evento in sostanza così posso segnale un mancato requisito a livello
     * logico come ad esempio autorizzazioni, dati necessari ma che non ci sono
     * oppure che non ci si è loggati.
     *
     * @return
     */
    public abstract String getErrorCode();
}
