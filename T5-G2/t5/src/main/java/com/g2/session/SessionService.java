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

package com.g2.session;

import com.g2.game.gameMode.GameLogic;
import com.g2.session.exception.GameModeAlreadyExistException;
import com.g2.session.exception.GameModeDontExistException;
import com.g2.session.exception.SessionAlredyExist;
import com.g2.session.exception.SessionDoesntExistException;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class SessionService {

    // TTL di default per tutte le operazioni relative alla sessione (in secondi)
    public static final long DEFAULT_SESSION_TTL = 10800L; // 3 ore
    /*
     * Prefisso key della sessione
     */
    private static final String KEY_PREFIX = "User_session";
    /*
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private final RedisTemplate<String, Sessione> redisTemplate;

    @Autowired
    public SessionService(RedisTemplate<String, Sessione> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static boolean handleNetworkError(String caller, Exception e) {
        if (e instanceof RedisConnectionException) {
            logger.error("{} - Errore di connessione a Redis: {}", caller, e.getMessage(), e);
        } else if (e instanceof RedisCommandTimeoutException || e instanceof TimeoutException) {
            logger.error("{} - Timeout durante l'operazione su Redis: {}", caller, e.getMessage(), e);
        } else if (e instanceof DataAccessException) {
            logger.error("{} - Errore generico di accesso a RedisTemplate: {}", caller, e.getMessage(), e);
        } else if (e instanceof IllegalArgumentException) {
            logger.error("{} - Argomento non valido passato al metodo: {}", caller, e.getMessage(), e);
        } else if (e instanceof RedisException) {
            logger.error("{} - Errore generico di Lettuce Redis: {}", caller, e.getMessage(), e);
        } else {
            logger.error("{} - Errore sconosciuto: {}", caller, e.getMessage(), e);
        }
        return false;
    }

    /**
     * Crea la chiave della sessione usando playerId e un timestamp. Il formato
     * della chiave sarà: "prefix:playerId:".
     */
    private String buildCompositeKey(Long userId) {
        return KEY_PREFIX + ":" + userId + ":";
    }

    public String createSession(Long playerId) throws SessionAlredyExist {
        return createSession(playerId, Optional.empty());
    }

    public String createSession(Long playerId, Optional<Long> ttlSeconds) throws SessionAlredyExist {
        Long ttl = ttlSeconds.filter(ttlSec -> ttlSec > 0).orElse(DEFAULT_SESSION_TTL);
        logger.info("createSession - Creazione sessione per il giocatore {} con TTL: {}", playerId, ttl);
        String sessionKey = buildCompositeKey(playerId);
        Sessione session = new Sessione(playerId, sessionKey);
        try {
            boolean success = redisTemplate.opsForValue().setIfAbsent(sessionKey, session, ttl, TimeUnit.SECONDS);
            if (!success) {
                /*
                 * Già esiste una sessione, ti fornisco la key
                 */
                logger.error("createSession - Creazione della sessione fallita per il giocatore {}", playerId);
                return sessionKey;
            }
        } catch (Exception e) {
            handleNetworkError("createSession", e);
        }
        logger.info("createSession - Sessione creata con successo per il giocatore {} con sessionKey: {}", playerId, sessionKey);
        return sessionKey;
    }

    public Sessione getSession(Long playerId) {
        String sessionKey = buildCompositeKey(playerId);
        logger.info("getSession - Recupero della sessione per sessionKey: {}", sessionKey);
        try {
            Sessione sessione = redisTemplate.opsForValue().get(sessionKey);
            if (sessione == null) {
                logger.error("getSession - Sessione non trovata per sessionKey: {}", sessionKey);
                throw new SessionDoesntExistException("Sessione non trovata per la sessionKey: " + sessionKey);
            }
            logger.info("getSession - Sessione recuperata con successo per sessionKey: {}", sessionKey);
            return sessione;
        } catch (Exception e) {
            handleNetworkError("createSession", e);
            throw new SessionDoesntExistException("Errore durante il recupero della sessione per il giocatore %s: %s".formatted(playerId, e.getMessage()));
        }
    }

    /**
     * Cerca una sessione esistente per il player.
     */
    public boolean doesSessionExistForPlayer(Long playerId) {
        String key = KEY_PREFIX + ":" + playerId + ":";
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            return handleNetworkError("doesSessionExistForPlayer", e);
        }
    }

    public boolean renewSessionTTL(String sessionKey, long ttlSeconds) {
        logger.info("renewSessionTTL - Rinnovo del TTL per sessionKey: {} con ttlSeconds: {}", sessionKey, ttlSeconds);
        boolean success;
        try {
            success = redisTemplate.expire(sessionKey, ttlSeconds, TimeUnit.SECONDS);
            if (!success) {
                logger.warn("renewSessionTTL - Rinnovo del TTL fallito per sessionKey: {}", sessionKey);
            } else {
                logger.info("renewSessionTTL - TTL rinnovato con successo per sessionKey: {}", sessionKey);
            }
            return success;
        } catch (Exception e) {
            return handleNetworkError("doesSessionExistForPlayer", e);
        }
    }

    public boolean deleteSession(Long playerId) {
        boolean SessionExist = doesSessionExistForPlayer(playerId);
        if (!SessionExist) {
            throw new SessionDoesntExistException("Sessione non esiste per il playerId: " + playerId);
        }
        String sessionKey = buildCompositeKey(playerId);
        logger.info("deleteSession - Eliminazione della sessione per sessionKey: {}", sessionKey);
        try {
            return redisTemplate.delete(sessionKey);
        } catch (Exception e) {
            return handleNetworkError("deleteSession", e);
        }
    }

    public boolean updateSession(Long playerId, Sessione updatedSession) {
        return updateSession(playerId, updatedSession, Optional.empty());
    }

    public boolean updateSession(Long playerId, Sessione updatedSession, Optional<Long> ttlSeconds) {
        long ttl = ttlSeconds.filter(ttlSec -> ttlSec > 0).orElse(DEFAULT_SESSION_TTL);
        logger.info("updateSession - Aggiornamento della sessione per playerId: {} con TTL: {}", playerId, ttl);

        if (updatedSession == null) {
            logger.error("updateSession - La sessione aggiornata non può essere null");
            throw new IllegalArgumentException("La sessione aggiornata non può essere null");
        }

        try {
            String sessionKey = buildCompositeKey(playerId);
            redisTemplate.opsForValue().set(sessionKey, updatedSession, ttl, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return handleNetworkError("updateSession", e);
        }
    }

    public List<Sessione> getAllSessions() {
        logger.info("getAllSessions - Recupero di tutte le sessioni da Redis");
        Set<String> keys = redisTemplate.keys("*");
        return (keys == null || keys.isEmpty()) ? Collections.emptyList() : redisTemplate.opsForValue().multiGet(keys);
    }

    public GameLogic getGameMode(Long playerId, GameMode gameMode) {
        Sessione sessione = getSession(playerId);
        if (sessione.hasModalita(gameMode)) {
            return sessione.getGame(gameMode);
        } else {
            throw new GameModeDontExistException("Non esiste alcuna partita avviata per la modalità %s per il player %d".
                    formatted(gameMode, playerId));
        }
    }

    public boolean setGameMode(Long playerId, GameLogic game) {
        return setGameMode(playerId, game, Optional.empty());
    }

    public boolean setGameMode(Long playerId, GameLogic game, Optional<Long> ttlSeconds) {
        logger.info("SetGameMode - Aggiunta del game mode: {} per il player: {}", game.getGameMode(), playerId);
        Sessione session = getSession(playerId);
        if (session.hasModalita(game.getGameMode())) {
            //Già esiste
            throw new GameModeAlreadyExistException("Esiste già una partita avviata per la modalità %s per il player %d".
                    formatted(game.getGameMode(), playerId));
        } else {
            session.addModalita(game.getGameMode(), game);
            return updateSession(playerId, session, ttlSeconds);
        }
    }

    public boolean removeGameMode(Long playerId, GameMode mode) {
        return removeGameMode(playerId, mode, Optional.empty());
    }

    public boolean removeGameMode(Long playerId, GameMode mode, Optional<Long> ttlSeconds) {
        logger.info("removeGameMode - Rimozione del game mode: {} per il player: {}", mode, playerId);

        try {
            Sessione session = getSession(playerId);
            session.removeModalita(mode);
            return updateSession(playerId, session, ttlSeconds);
        } catch (Exception e) {
            logger.error("[destroyGame] Errore eliminando la sessione del game mode {} : {}", mode, e.getMessage());
            return false;
        }
    }

    public boolean updateGameMode(Long playerId, GameLogic game) {
        return updateGameMode(playerId, game, Optional.empty());
    }

    public boolean updateGameMode(Long playerId, GameLogic game, Optional<Long> ttlSeconds) {
        logger.info("updateGameMode - Aggiornamento del game mode: {} per il player: {}", game.getGameMode(), playerId);
        Sessione session = getSession(playerId);
        if (session.hasModalita(game.getGameMode())) {
            session.addModalita(game.getGameMode(), game);
            return updateSession(playerId, session, ttlSeconds);
        } else {
            throw new GameModeDontExistException("Non esiste modalità" + game.getGameMode());
        }
    }

    public interface SessionCall {

        void execute() throws Exception;
    }
}
