package com.g2.Interfaces;
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
            throw new IllegalArgumentException("[HANDLEREQUEST] Numero di parametri errato: atteso " 
                    + parameterTypes.length + ", ricevuto " + params.length);
        }
        for (int i = 0; i < params.length; i++) {
            if (!parameterTypes[i].isInstance(params[i])) {
                throw new IllegalArgumentException("[HANDLEREQUEST] Parametro " + (i + 1) + " non è del tipo corretto: atteso " 
                        + parameterTypes[i].getSimpleName() + ", ricevuto " 
                        + (params[i] == null ? "null" : params[i].getClass().getSimpleName()));
            }
        }
    }
}