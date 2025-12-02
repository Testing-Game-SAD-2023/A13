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
 * Funzioni di utilità condivise tra Gamemode_logic.js e gamemode_scalata.js
 */

// ------------------------------
// COOKIE & JWT
// ------------------------------

/**
 * Recupera il valore di un cookie dato il suo nome
 * @param {string} name - Nome del cookie
 * @returns {string|null} Valore del cookie o null se non trovato
 */
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

/**
 * Decodifica un JWT e restituisce il payload
 * @param {string} token - Token JWT da decodificare
 * @returns {Object|null} Payload del JWT o null in caso di errore
 */
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("[common_utils] Errore nel parsing del JWT:", e);
        return null;
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
// URL PARAMETERS
// ------------------------------

/**
 * Recupera il valore di un parametro dall'URL
 * @param {string} name - Nome del parametro
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
 * Recupera la modalità di gioco corrente dall'URL
 * @returns {string} Modalità di gioco (Sfida, Allenamento, PartitaSingola, Scalata)
 */
function GetMode() {
    const mode = getParameterByName("mode");
    if (mode) {
        const trimmed = mode.replace(/[^a-zA-Z0-9\s]/g, " ").trim();
        return trimmed;
    }
    return "Sfida"; // Default
}

// ------------------------------
// FORMATTING
// ------------------------------

/**
 * Formatta il tempo in secondi in formato MM:SS
 * @param {number} seconds - Secondi da formattare
 * @returns {string} Tempo formattato (es. "02:30")
 */
function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

// ------------------------------
// UI HELPERS
// ------------------------------

/**
 * Toggle della visibilità di un elemento
 * @param {string} elementId - ID dell'elemento da toggleare
 */
function toggleVisibility(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.classList.toggle("d-none");
    }
}

console.log("[common_utils] Utilities loaded successfully");
