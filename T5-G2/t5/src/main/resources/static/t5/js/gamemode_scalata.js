/**
 * gamemode_scalata.js
 * Gestisce la selezione e l'avvio delle scalate disponibili
 *
 * Le card delle scalate sono già renderizzate da Thymeleaf nel DOM.
 * JavaScript si occupa solo dell'interattività (selezione e avvio).
 *
 * Dipendenze:
 * - jQuery
 * - SweetAlert
 */

// ------------------------------
// UTILITY FUNCTIONS (da common_utils.js - duplicate per evitare problemi di caricamento)
// ------------------------------

/**
 * Recupera un cookie per nome
 * @param {string} name - Nome del cookie
 * @returns {string|null} Valore del cookie o null
 */
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

/**
 * Decodifica un JWT e ritorna il payload
 * @param {string} token - Token JWT
 * @returns {Object} Payload decodificato
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
    } catch (error) {
        console.error("[gamemode_scalata] Errore nel parsing del JWT:", error);
        return null;
    }
}

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
                withCredentials: true,
            },
            success: function (response) {
                console.log("[gamemode_scalata] StartGame success:", response);
                resolve(response);
            },
            error: function (xhr) {
                console.error("[gamemode_scalata] StartGame error:", xhr);
                reject(xhr.responseJSON || xhr.responseText);
            },
        });
    });
}

// ------------------------------
// LOGICA SCALATA
// ------------------------------

// Variabile globale per tenere traccia della scalata selezionata
let selectedScalata = null;

// Quando il documento è pronto
$(document).ready(function() {
    console.log("[gamemode_scalata] Inizializzazione pagina");

    // Verifica se ci sono scalate nel DOM
    const scalateCards = $('.scalata-card');

    if (scalateCards.length === 0) {
        console.warn("[gamemode_scalata] Nessuna scalata disponibile nel DOM");
        return;
    }

    console.log(`[gamemode_scalata] Trovate ${scalateCards.length} scalate nel DOM`);

    // Aggiungi event listener a tutte le card
    scalateCards.on('click', function() {
        selectScalata($(this));
    });

    // Event listener per il bottone submit
    $('#submit-button').on('click', function() {
        if (selectedScalata) {
            console.log("[gamemode_scalata] Avvio scalata:", selectedScalata);
            startScalata();
        } else {
            swal({
                title: "Attenzione!",
                text: "Seleziona una scalata prima di procedere",
                icon: "warning",
                button: "OK"
            });
        }
    });
});

/**
 * Gestisce la selezione di una scalata
 * @param {jQuery} $card - La card jQuery cliccata
 */
function selectScalata($card) {
    // Rimuovi la classe selected da tutte le card
    $('.scalata-card').removeClass('selected');

    // Aggiungi la classe selected alla card cliccata
    $card.addClass('selected');

    // Leggi i dati dalla card tramite data attributes
    const scalataName = $card.data('name');
    const totalLevels = parseInt($card.data('levels'));

    // Salva la scalata selezionata
    selectedScalata = {
        name: scalataName,
        totalLevels: totalLevels
    };

    console.log("[gamemode_scalata] Scalata selezionata:", selectedScalata);
}

/**
 * Avvia la scalata selezionata
 * 1. Recupera il primo livello da T1
 * 2. Prepara i dati per StartGame
 * 3. Effettua la chiamata POST
 */
async function startScalata() {
    if (!selectedScalata) {
        console.error("[gamemode_scalata] Nessuna scalata selezionata");
        return;
    }

    const playerId = String(parseJwt(getCookie("jwt")).userId);
    const mode = "Scalata";

    console.log("[gamemode_scalata] Avvio scalata:", selectedScalata);

    // 1. CHIAMATA T1 PER OTTENERE IL PRIMO LIVELLO
    const firstLevel = await fetchCurrentLevel(selectedScalata.name, 1);

    if (!firstLevel) {
        console.error("Impossibile recuperare il primo livello della scalata");
        swal({
            title: "Errore!",
            text: "Impossibile recuperare i dati del primo livello. Riprova più tardi.",
            icon: "error",
            button: "OK"
        });
        return;
    }

    console.log("[gamemode_scalata] Dati primo livello ricevuti da T1:", firstLevel);

    // Verifica se c'è un errore nella risposta
    if (firstLevel.error) {
        console.error("[gamemode_scalata] Errore dal backend:", firstLevel.error);
        swal({
            title: "Errore!",
            text: firstLevel.error,
            icon: "error",
            button: "OK"
        });
        return;
    }

    // 2. MAPPA I CAMPI DA T1 AL FORMATO RICHIESTO
    // T1 ritorna: { idLevel, scalataName, className, tempoMax, opponentName }
    // StartScalataRequestDTO richiede: { underTestClassName, typeRobot, difficulty, remainingTime }

    const underTestClassName = firstLevel.className;  // ← Corretto!
    const typeRobot = "EvoSuite";  // ← Default, T1 non lo fornisce ancora
    const difficulty = "EASY";    // ← Default, T1 non lo fornisce ancora
    const remainingTime = parseInt(firstLevel.tempoMax) || 300;  // ← Assicura sia int

    // Validazione campi obbligatori
    if (!underTestClassName) {
        console.error("[gamemode_scalata] className mancante nei dati del livello");
        swal({
            title: "Errore!",
            text: "Dati del livello incompleti: classe di test mancante",
            icon: "error",
            button: "OK"
        });
        return;
    }

    // 3. PREPARA I DATI PER StartGame
    let requestData = {
        playerId: playerId,
        mode: mode,
        scalataName: selectedScalata.name,
        currentLevel: 1,  // Inizia sempre dal livello 1
        totalLevels: selectedScalata.totalLevels,
        underTestClassName: underTestClassName,
        typeRobot: typeRobot,
        difficulty: difficulty,
        remainingTime: remainingTime
    };

    console.log("[gamemode_scalata] RequestData preparato per StartGame:", JSON.stringify(requestData, null, 2));

    // 4. CHIAMATA AJAX (stessa di PartitaSingola)
    console.log("[gamemode_scalata] Invio richiesta StartGame:", requestData);

    startGameRequest(requestData)
        .then((response) => {
            console.log("[gamemode_scalata] StartGame response:", response);

            // 5. REDIRECT ALL'EDITOR
            window.location.href = `/editor?ClassUT=${underTestClassName}&mode=${mode}`;
        })
        .catch((error) => {
            console.error("Errore nell'avvio della scalata:", error);
            swal({
                title: "Errore!",
                text: "Impossibile avviare la scalata. " + (error.message || "Riprova più tardi."),
                icon: "error",
                button: "OK"
            });
        });
}

/**
 * Recupera i dati di un livello specifico della scalata da T1
 * @param {string} scalataName - Nome della scalata
 * @param {number} currentLevel - Numero del livello corrente da recuperare
 * @returns {Promise<Object>} Dati del livello richiesto (classUT, robot, difficulty, tempoMax, etc.)
 */
async function fetchCurrentLevel(scalataName, currentLevel) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/scalata/${scalataName}/level/${currentLevel}`,
            type: "GET",
            xhrFields: {
                withCredentials: true
            },
            success: function (response) {
                console.log(`[gamemode_scalata] Livello ${currentLevel} recuperato:`, response);
                resolve(response);
            },
            error: function (xhr) {
                console.error(`[gamemode_scalata] Errore recupero livello ${currentLevel}:`, xhr);
                reject(xhr.responseJSON || xhr.responseText);
            }
        });
    });
}
