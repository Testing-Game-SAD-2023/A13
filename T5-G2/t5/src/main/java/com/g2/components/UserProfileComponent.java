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
package com.g2.components;

import com.g2.interfaces.ServiceManager;
import com.g2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Componente che recupera i dati del profilo utente dal servizio e li inserisce
 * nel modello della pagina.
 */
public class UserProfileComponent extends GenericObjectComponent {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileComponent.class);
    private final ServiceManager serviceManager;
    private final boolean isFriendProfile;
    private final Long userID;
    private final Long friendID;

    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param modelKey       la chiave con cui inserire i dati nel modello.
     * @param userId         l'ID dell'utente di cui recuperare il profilo.
     * @param serviceName    il nome del servizio da cui recuperare i dati del
     *                       profilo.
     * @param action         l'azione da eseguire per ottenere il profilo.
     */
    public UserProfileComponent(ServiceManager serviceManager,
                                Boolean isFriendProfile,
                                Long userID
    ) {
        super(null, null);  // Il costruttore della superclasse è invocato senza parametri
        this.serviceManager = serviceManager;
        this.isFriendProfile = isFriendProfile;
        this.userID = userID;
        this.friendID = null;
    }

    public UserProfileComponent(ServiceManager serviceManager,
                                Boolean isFriendProfile,
                                Long userID,
                                Long friendID
    ) {
        super(null, null);  // Il costruttore della superclasse è invocato senza parametri
        this.serviceManager = serviceManager;
        this.isFriendProfile = isFriendProfile;
        this.userID = userID;
        this.friendID = friendID;
    }

    /**
     * Esegue la logica per recuperare il profilo utente dal servizio e
     * inserirlo nel modello.
     *
     * @return una mappa con i dati del profilo utente.
     */
    @Override
    public Map<String, Object> getModel() {
        try {
            // Inserisce i dati del profilo utente nel modello con la chiave specificata
            User user = serviceManager.handleRequest("T23", "GetUser", User.class, this.userID);
            if (this.isFriendProfile) {
                User friendUser = (User) serviceManager.handleRequest("T23", "GetUser", this.friendID);
                this.model.put("user", friendUser);
                this.model.put("viewID", user.getUserProfile().getId());
            } else {
                // profilo privato dell'utente 
                this.model.put("user", user);
                this.model.put("viewID", null);
            }
            this.model.put("isFriendProfile", isFriendProfile);
            return this.model;
        } catch (Exception e) {
            // Gestione delle eccezioni, ad esempio log dell'errore
            logger.error("[UserProfileComponent]Errore durante il recupero del profilo utente: ", e);
            return Collections.emptyMap();
        }
    }
}
