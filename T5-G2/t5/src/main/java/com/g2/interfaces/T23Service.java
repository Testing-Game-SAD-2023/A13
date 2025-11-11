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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.model.NotificationResponse;
import com.g2.model.User;
import com.g2.model.dto.PlayerDTO;
import com.g2.model.dto.GameProgressDTO;
import com.g2.model.dto.PlayerProgressDTO;
import com.g2.model.dto.UpdateGameProgressDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class T23Service extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(T23Service.class);
    private static final String EMAIL_FIELD = "email"; // Dichiarato come variabile per rimuovere l'issue di SonarQube per le troppe ripetizioni di "email"
    /*
     * Per il test locale senza container docker usare:
     * BASE_URL = "http://127.0.0.1:8090"
     */
    private static final String BASE_URL = "http://api_gateway-controller:8090";
    private static final String SERVICE_PREFIX = "userService";
    private final ObjectMapper mapper = new ObjectMapper();

    /*
     * La dimensione del costruttore è stata ridotta dividendolo in sotto-metodi per essere compliant con SonarQube
     */
    @SuppressWarnings("unchecked")
    public T23Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL + "/" + SERVICE_PREFIX);

        // Registrazione delle azioni
        registerAction("GetAuthenticated", new ServiceActionDefinition(
                params -> getAuthenticated((String) params[0]),
                String.class
        ));

        registerNotificationActions();
        registerGetUserActions();
        registerUserProfileActions();
        registerPlayerStatusActions();
        registerPlayerActions();
    }

    /*
     * Di seguito sono riportati i metodi in cui è stato scomposto il costruttore per ridurne la dimensione e risolvere l'issue di SonarQube
     */
    private void registerNotificationActions() {
        registerAction("NewNotification", new ServiceActionDefinition(
                params -> newNotification((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class
        ));

        registerAction("getNotifications", new ServiceActionDefinition(
                params -> getNotifications((String) params[0], (Integer) params[1], (Integer) params[2]),
                String.class, Integer.class, Integer.class
        ));

        registerAction("updateNotification", new ServiceActionDefinition(
                params -> updateNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("deleteNotification", new ServiceActionDefinition(
                params -> deleteNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("clearNotifications", new ServiceActionDefinition(
                params -> clearNotifications((String) params[0]),
                String.class
        ));
    }

    private void registerGetUserActions() {
        registerAction("GetUsers", new ServiceActionDefinition(
                params -> getUsers() //metodo senza parametri
        ));

        registerAction("GetUser", new ServiceActionDefinition(
                params -> getUser((Long) params[0]),
                Long.class
        ));

        registerAction("GetUsersByList", new ServiceActionDefinition(
                params -> getUserByList((List<String>) params[0]),
                List.class
        ));

        registerAction("GetUserByEmail", new ServiceActionDefinition(
                params -> getUserByEmail((String) params[0]),
                String.class
        ));
    }

    private void registerUserProfileActions() {
        registerAction("UpdateProfile", new ServiceActionDefinition(
                params -> updateProfile((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class
        ));

        registerAction("followUser", new ServiceActionDefinition(
                params -> followUser((Integer) params[0], (Integer) params[1]),
                Integer.class, Integer.class
        ));

        registerAction("getFollowers", new ServiceActionDefinition(
                params -> getFollowers((String) params[0]),
                String.class
        ));

        registerAction("getFollowing", new ServiceActionDefinition(
                params -> getFollowing((String) params[0]),
                String.class
        ));
    }

    private void registerPlayerStatusActions() {
        registerAction("createPlayerProgressAgainstOpponent", new ServiceActionDefinition(
                params -> createPlayerProgressAgainstOpponent((long) params[0], (GameMode) params[1], (String) params[2], (String) params[3], (OpponentDifficulty) params[4]),
                Long.class, GameMode.class, String.class, String.class, OpponentDifficulty.class
        ));

        registerAction("getPlayerProgressAgainstOpponent", new ServiceActionDefinition(
                params -> getPlayerProgressAgainstOpponent((long) params[0], (GameMode) params[1], (String) params[2], (String) params[3], (OpponentDifficulty) params[4]),
                Long.class, GameMode.class, String.class, String.class, OpponentDifficulty.class
        ));

        registerAction("updatePlayerProgressAgainstOpponent", new ServiceActionDefinition(
                params -> updatePlayerProgressAgainstOpponent((long) params[0], (GameMode) params[1], (String) params[2],
                        (String) params[3], (OpponentDifficulty) params[4], (boolean) params[5], (Set<String>) params[6]),
                Long.class, GameMode.class, String.class, String.class, OpponentDifficulty.class, Boolean.class, Set.class
        ));

        registerAction("getPlayerProgressAgainstAllOpponent", new ServiceActionDefinition(
                params -> getPlayerProgressAgainstAllOpponent((long) params[0]),
                Long.class
        ));

        registerAction("incrementUserExp", new ServiceActionDefinition(
                params -> incrementUserExp((long) params[0], (int) params[1]),
                Long.class, Integer.class
        ));

        registerAction("updateGlobalAchievements", new ServiceActionDefinition(
                params -> updateGlobalAchievements((long) params[0], (Set<String>) params[1]), Long.class, Set.class
        ));
    }

    private void registerPlayerActions(){
        registerAction("getAllPlayers", new ServiceActionDefinition(
            params -> getAllPlayers()
        ));
    }

    private List<PlayerDTO> getAllPlayers() {
        final String endpoint = "/players";
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<PlayerDTO>>(){});
    }

    private GameProgressDTO createPlayerProgressAgainstOpponent(long playerId, GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty) {
        final String endpoint = "/players/%s/progression/against".formatted(playerId);

        JSONObject requestBody = new JSONObject();
        requestBody.put("classUT", classUT);
        requestBody.put("gameMode", gameMode.name());
        requestBody.put("type", type);
        requestBody.put("difficulty", difficulty.name());
        return callRestPost(endpoint, requestBody, null, null, GameProgressDTO.class);
    }

    private GameProgressDTO getPlayerProgressAgainstOpponent(long playerId, GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty) {
        final String endpoint = "/players/%s/progression/against/%s/%s/%s/%s".formatted(playerId, gameMode.name(), classUT, type, difficulty.name());
        return callRestGET(endpoint, null, GameProgressDTO.class);
    }

    private GameProgressDTO updatePlayerProgressAgainstOpponent(long playerId, GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty, boolean isWinner, Set<String> unlockedAchievements) {
        final String endpoint = "/players/%s/progression/against/%s/%s/%s/%s".formatted(playerId, gameMode.name(), classUT, type, difficulty.name());

        UpdateGameProgressDTO dto = new UpdateGameProgressDTO(isWinner, unlockedAchievements);
        JSONObject requestBody;
        try {
            requestBody = new JSONObject(mapper.writeValueAsString(dto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.info("REQUEST BODY MAPPER: {}", requestBody);
        return callRestPut(endpoint, requestBody, null, null, GameProgressDTO.class);
    }

    private PlayerProgressDTO getPlayerProgressAgainstAllOpponent(long playerId) {
        final String endpoint = "/players/%s/progression".formatted(playerId);
        return callRestGET(endpoint, null, PlayerProgressDTO.class);
    }

    private int incrementUserExp(long playerId, int expGained) {
        final String endpoint = "/players/%s/progression/experience".formatted(playerId);
        JSONObject requestBody = new JSONObject();
        requestBody.put("experiencePoints", expGained);
        return callRestPut(endpoint, requestBody, null, null, Integer.class);
    }

    private Set<String> updateGlobalAchievements(long playerId, Set<String> achievements) {
        final String endpoint = "/players/%s/progression/achievements/global".formatted(playerId);

        JSONObject requestBody = new JSONObject();
        requestBody.put("unlockedAchievements", achievements);
        return (Set<String>) callRestPut(endpoint, requestBody, null, null, Set.class);
    }

    // Metodo per l'autenticazione
    private JwtValidationResponseDTO getAuthenticated(String jwt) {
        final String endpoint = "/auth/validateToken";
        // Verifica se il JWT è valido prima di fare la richiesta
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException("[GETAUTHENTICATED] Errore, token nullo o vuoto");
        }
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("jwt", jwt);
        // Chiamata POST utilizzando il metodo della classe base
        return callRestPost(endpoint, formData, null, JwtValidationResponseDTO.class);
    }

    // Metodo per ottenere la lista degli utenti
    private List<User> getUsers() {
        final String endpoint = "/players/students_list";
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<User>>() {
        });
    }

    private User getUser(long userId) {
        final String endpoint = "/players/students_list/" + userId;
        return callRestGET(endpoint, null, User.class);
    }

    //Do una lista di ID e mi ritorna una lista di User
    // Implementata a mano perchè un po' strana è una POST che ottiene dati come una GET
    private List<User> getUserByList(List<String> idsStudenti) {
        final String endpoint = "/players/getStudentiTeam";
        // Crea un oggetto HttpEntity con i dati che vogliamo inviare (la lista degli ID)
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(idsStudenti);
        // Esegui la chiamata POST all'endpoint
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                BASE_URL + "/" + SERVICE_PREFIX + endpoint, // URL dell'endpoint
                HttpMethod.POST, // Tipo di richiesta POST
                requestEntity, // Corpo della richiesta (lista di studenti)
                new ParameterizedTypeReference<List<User>>() {
                } // Tipo di risposta che ci aspettiamo
        );

        // Gestisci la risposta
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) responseEntity.getBody();
            return users;
        } else {
            return null;
        }
    }

    // Metodo per modificare il profilo di un utente
    private Boolean updateProfile(String userEmail, String bio, String imagePath) {
        final String endpoint = "/profile/update_profile";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(EMAIL_FIELD, userEmail);
        map.add("bio", bio);
        map.add("profilePicturePath", imagePath);
        return callRestPost(endpoint, map, null, Boolean.class);
    }

    private User getUserByEmail(String userEmail) {
        final String endpoint = "/profile/user_by_email";
        Map<String, String> queryParams = Map.of(EMAIL_FIELD, userEmail);
        return callRestGET(endpoint, queryParams, User.class);
    }

    // Metodo per la creazione di una notifica
    private String newNotification(String userEmail, String title, String message) {
        final String endpoint = "/notification/new_notification";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(EMAIL_FIELD, userEmail);
        map.add("title", title);
        map.add("message", message);
        return callRestPost(endpoint, map, null, String.class);
    }

    public NotificationResponse getNotifications(String userEmail, int page, int size) {
        final String endpoint = "/notification/notifications";
        // Creazione dei parametri di query, inclusi email, pagina e dimensione
        Map<String, String> queryParams = Map.of(
                EMAIL_FIELD, userEmail,
                "page", String.valueOf(page),
                "size", String.valueOf(size)
        );

        ResponseEntity<NotificationResponse> response = restTemplate.exchange(
                buildUri(endpoint, queryParams),
                HttpMethod.GET,
                null, // Puoi aggiungere intestazioni, se necessario
                NotificationResponse.class
        );

        if (response == null) {
            return new NotificationResponse();
        } else {
            return response.getBody();
        }
    }

    public String updateNotification(String userEmail, String notificationID) {
        final String endpoint = "/notification/update_notification";
        // Imposta i dati del form
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(EMAIL_FIELD, userEmail);
        formData.add("id notifica", notificationID);
        // Effettua una chiamata POST per aggiornare lo stato della notifica
        return callRestPost(endpoint, formData, null, String.class);
    }

    // Metodo per eliminare una singola notifica
    public String deleteNotification(String userEmail, String notificationID) {
        final String endpoint = "/notification/delete_notification";
        Map<String, String> queryParams = Map.of(
                EMAIL_FIELD, userEmail,
                "idnotifica", notificationID
        );
        return callRestDelete(endpoint, queryParams);
    }

    // Metodo per eliminare tutte le notifiche
    public String clearNotifications(String userEmail) {
        final String endpoint = "/notification/clear_notifications";
        Map<String, String> queryParams = Map.of(EMAIL_FIELD, userEmail);
        return callRestDelete(endpoint, queryParams);
    }

    /*
     *   Metodo per follow/unfollow di un utente
     *   il targetUserId + chi viene seguito
     *   il authUserId è chi segue
     */
    public String followUser(Integer targetUserId, Integer authUserId) {
        final String endpoint = "/profile/toggle_follow";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("targetUserId", String.valueOf(targetUserId));
        map.add("authUserId", String.valueOf(authUserId));
        return callRestPost(endpoint, map, null, String.class);
    }

    public List<User> getFollowers(String userId) {
        final String endpoint = "/profile/followers";
        Map<String, String> queryParams = Map.of("userId", userId);
        return callRestGET(endpoint, queryParams, new ParameterizedTypeReference<List<User>>() {
        });
    }

    public List<User> getFollowing(String userId) {
        final String endpoint = "/profile/following";
        Map<String, String> queryParams = Map.of("userId", userId);
        return callRestGET(endpoint, queryParams, new ParameterizedTypeReference<List<User>>() {
        });
    }
}
