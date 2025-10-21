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

package com.g2.interfaces;

import java.util.function.Function;

//Questa classe rappresenterà un'azione con la funzione da eseguire e le specifiche sui parametri.
public class ServiceActionDefinition {

    private final Function<Object[], Object> function;
    private final Class<?>[] parameterTypes;

    public ServiceActionDefinition(Function<Object[], Object> function, Class<?>... parameterTypes) {
        this.function = function;
        this.parameterTypes = parameterTypes != null ? parameterTypes : new Class<?>[0];
    }

    public Object execute(Object[] params) {
        // Se params è nullo, assegniamo un array vuoto per la validazione
        if (params == null) {
            params = new Object[0];
        }
        validateParameters(params);
        return function.apply(params);
    }

    private void validateParameters(Object[] params) {
        if (params.length != parameterTypes.length) {
            throw new MissingParametersException("[ServiceActionDefinition] Numero di parametri errato: atteso "
                    + parameterTypes.length + ", ricevuto " + params.length);
        }
        for (int i = 0; i < params.length; i++) {
            if (!parameterTypes[i].isInstance(params[i])) {
                throw new InvalidParameterTypeException("[ServiceActionDefinition] Parametro " + (i + 1) + " non è del tipo corretto: atteso "
                        + parameterTypes[i].getSimpleName() + ", ricevuto "
                        + (params[i] == null ? "null" : params[i].getClass().getSimpleName()));
            }
        }
    }

    //Eccezioni specifiche del ServiceAction 
    public class MissingParametersException extends RuntimeException {
        public MissingParametersException(String message) {
            super(message);
        }
    }

    public class InvalidParameterTypeException extends RuntimeException {
        public InvalidParameterTypeException(String message) {
            super(message);
        }
    }
}