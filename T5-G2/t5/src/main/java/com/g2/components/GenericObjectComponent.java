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

import java.util.HashMap;
import java.util.Map;

/**
 * Componente generico per inserire un oggetto nel modello.
 */
public class GenericObjectComponent {

    // Mappa per memorizzare i dati del modello
    protected Map<String, Object> model = new HashMap<>();

    /**
     * Costruttore per creare un componente con un oggetto e una chiave
     * specificata.
     *
     * @param key    la chiave per inserire l'oggetto nel modello.
     * @param object l'oggetto da inserire nel modello.
     */
    public GenericObjectComponent(String key, Object object) {
        if (key != null && object != null) {
            this.model.put(key, object);
        }
    }

    /**
     * Restituisce il modello con l'oggetto inserito.
     *
     * @return una mappa contenente la chiave e l'oggetto inseriti.
     */
    public Map<String, Object> getModel() {
        return this.model;
    }

    /**
     * Metodo per aggiornare l'oggetto.
     *
     * @param object il nuovo oggetto da inserire nel modello.
     */
    public void setObject(String key, Object object) {
        this.model.put(key, object);
    }
}
