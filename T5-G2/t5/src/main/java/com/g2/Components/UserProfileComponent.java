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
package com.g2.Components;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.AchievementProgress;
import com.g2.Model.Notification;
import com.g2.Model.Statistic;
import com.g2.Model.StatisticProgress;
import com.g2.Model.User;
import com.g2.Service.AchievementService;
import com.g2.Service.UserProfileService;

/**
 * Componente che recupera i dati del profilo utente dal servizio e li inserisce
 * nel modello della pagina.
 */
public class UserProfileComponent extends GenericObjectComponent {

    private final ServiceManager serviceManager;
    private final AchievementService achievementService;
    private final User user;
    private final String playerID;
    private final Boolean IsFriendProfile;
    /**
     * Costruttore per il componente.
     *
     * @param serviceManager il ServiceManager per gestire la richiesta.
     * @param modelKey la chiave con cui inserire i dati nel modello.
     * @param userId l'ID dell'utente di cui recuperare il profilo.
     * @param serviceName il nome del servizio da cui recuperare i dati del
     * profilo.
     * @param action l'azione da eseguire per ottenere il profilo.
     */
    public UserProfileComponent(ServiceManager serviceManager,
            User user,
            String playerID,
            AchievementService achievementService,
            Boolean IsFriendProfile
    ) {
        super(null, null);  // Il costruttore della superclasse è invocato senza parametri
        this.serviceManager = serviceManager;
        this.user = user;
        this.playerID = playerID;
        this.achievementService = achievementService;
        this.IsFriendProfile = IsFriendProfile;
    }

    /**
     * Esegue la logica per recuperare il profilo utente dal servizio e
     * inserirlo nel modello.
     *
     * @return una mappa con i dati del profilo utente.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getModel() {
        try {
            // Inserisce i dati del profilo utente nel modello con la chiave specificata
            this.Model.put("user", this.user);
            int playerID_int = Integer.parseInt(playerID);
            // Ottieni follower e following
            List<User> followersList = (List<User>) serviceManager.handleRequest("T23", "getFollowers", this.playerID);
            List<User> followingList = (List<User>) serviceManager.handleRequest("T23", "getFollowing", this.playerID);
            this.Model.put("followersList", followersList);
            this.Model.put("followingList", followingList);
            //Ottengo lista di Achievement
            //Qui probabilmente ci sono delle inefficienze, andare a vedere achievementService
            List<AchievementProgress> achievementProgresses = achievementService.getProgressesByPlayer(playerID_int);
            List<StatisticProgress> statisticProgresses = achievementService.getStatisticsByPlayer(playerID_int);
            Map<String, Statistic> IdToStatistic = achievementService.GetIdToStatistic();
            //Inserisco nel modello
            this.Model.put("unlockedAchievements", achievementService.getUnlockedAchievementProgress(achievementProgresses));
            this.Model.put("lockedAchievements", achievementService.getLockedAchievementProgress(achievementProgresses));
            this.Model.put("statisticProgresses", statisticProgresses);
            this.Model.put("IdToStatistic", IdToStatistic);
            //Se è il profilo dell'utente carico delle info, sennò è un profilo amico e quindi carico altro
            if (!IsFriendProfile) {
                //Ottengo la lista delle notifiche 
                List<Notification> notifications = (List<Notification>) serviceManager.handleRequest("T23", "getNotifications", user.getEmail());
                this.Model.put("notifications", notifications);
            }
            return this.Model;
        } catch (Exception e) {
            // Gestione delle eccezioni, ad esempio log dell'errore
            System.err.println("[UserProfileComponent]Errore durante il recupero del profilo utente: " + e.getMessage());
            return null;
        }
    }

    public String getUserId() {
        return playerID;
    }
}
