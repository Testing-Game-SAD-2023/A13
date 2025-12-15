/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/**
 * common_utils.js
 * Funzioni di utilità condivise SOLO per gamemode_scalata.js e Gamemode_logic.js
 * 
 * NOTA IMPORTANTE: 
 * - getCookie, parseJwt: definiti in UserUtil.js (caricato dal footer) - NON ridefinire
 * - getParameterByName, GetMode, formatTime, toggleVisibility: DEVONO stare QUI perché:
 *   > Var_Editor.js NON viene caricato nelle pagine gamemode
 *   > Util_Editor.js viene caricato DOPO Gamemode_logic.js (troppo tardi)
 */

// ------------------------------
// URL E PARAMETRI
// ------------------------------

/**
 * Ottieni un parametro dall'URL corrente
 * @param {string} name - Nome del parametro da cercare
 * @returns {string|null} Valore del parametro o null se non trovato
 */
function getParameterByName(name) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
    const results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Ottieni la modalità di gioco dall'URL
 * @returns {string|null} Modalità (Sfida, Allenamento, PartitaSingola, Scalata) o null
 */
function GetMode() {
    const mode = getParameterByName("mode");
    if (mode) {
        const cleanedMode = mode.replace(/[^\w]/g, "").trim();
        return cleanedMode;
    }
    return null;
}

// ------------------------------
// FORMATTAZIONE E UI
// ------------------------------

/**
 * Formatta i secondi in formato MM:SS
 * @param {number} seconds - Secondi da formattare
 * @returns {string} Tempo formattato (MM:SS)
 */
function formatTime(seconds) {
	let hrs = Math.floor(seconds / 3600);
	let mins = Math.floor((seconds % 3600) / 60);
	let secs = seconds % 60;
	return (
		String(hrs).padStart(2, '0') + ":" +
		String(mins).padStart(2, '0') + ":" +
		String(secs).padStart(2, '0')
	);
}

/**
 * Toggle visibilità di un elemento tramite classe Bootstrap d-none
 * @param {string} elementId - ID dell'elemento da mostrare/nascondere
 */
function toggleVisibility(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.classList.toggle('d-none');
    }
}

// ------------------------------
// API REQUESTS
// ------------------------------

/**
 * Avvia una nuova partita tramite l'API StartGame
 * @param {Object} requestData - Dati della richiesta (playerId, mode, etc.)
 * @returns {Promise} Promise con la response dell'API
 */
function startGameRequest(requestData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/api/gameEngine/StartGame",
            type: "POST",
            contentType: "application/json",
            data: requestData ? JSON.stringify(requestData) : null,
            xhrFields: {
                withCredentials: true, // Invia automaticamente i cookie (JWT)
            },
            success: function (response) {
                console.log("[common_utils] StartGame success:", response);
                resolve(response);
            },
            error: function (xhr) {
                console.error("[common_utils] StartGame error:", xhr);
                reject(xhr.responseJSON || xhr.responseText);
            },
        });
    });
}

// ------------------------------
// SESSION MANAGEMENT
// ------------------------------

/**
 * Recupera il gamemode dalla sessione Redis tramite API
 * @param {string} playerId - ID del giocatore
 * @param {string} mode - Modalità di gioco
 * @returns {Promise<Object>} Dati della sessione
 */
async function getGameMode(playerId, mode) {
    const url = `/api/gameEngine/session/gamemode/${playerId}?mode=${mode}`;
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error(`Errore nella richiesta: ${response.statusText}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("[common_utils] Errore nella chiamata a getGameMode:", error);
        throw error;
    }
}

/**
 * Recupera i dati di una partita precedente dalla sessione
 * @returns {Promise<Object|null>} Dati della partita o null
 */
async function fetchPreviousGameData() {
    const playerId = String(parseJwt(getCookie("jwt")).userId);
    const gamemode = GetMode();
    try {
        const response = await getGameMode(playerId, gamemode);
        if (response && response.mode == gamemode) {
            console.log(
                "[common_utils] Trovato gameobject per la modalità " + gamemode
            );
            return response;
        } else {
            console.log("[common_utils] Modalità non corrispondente.");
            return null;
        }
    } catch (error) {
        console.error("[common_utils] Errore durante il recupero della sessione:", error);
        return null;
    }
}

/**
 * Elimina una modalità dalla sessione (surrender game)
 * @param {string} mode - Modalità di gioco da eliminare
 */
async function deleteModalita(mode) {
    const playerId = String(parseJwt(getCookie("jwt")).userId);
    const url = `/api/gameEngine/SurrenderGame/${playerId}?mode=${mode}`;
    try {
        const response = await fetch(url, {
            method: "DELETE",
        });
        const result = await response.text();
        console.log("[common_utils] Modalità eliminata:", result);
    } catch (error) {
        console.error("[common_utils] Errore durante l'eliminazione della modalità:", error);
    }
}

console.log("[common_utils] Utilities loaded successfully");
