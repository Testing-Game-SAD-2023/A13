/**
 * gamemode_scalata.js
 * Gestisce la selezione e l'avvio delle scalate disponibili
 * 
 * Le card delle scalate sono già renderizzate da Thymeleaf nel DOM.
 * JavaScript si occupa solo dell'interattività (selezione e avvio).
 * 
 * Dipendenze:
 * - UserUtil.js (caricato dal footer): getCookie(), parseJwt()
 * - jQuery
 * - SweetAlert
 */

console.log("🚀 gamemode_scalata.js CARICATO!");

// ------------------------------
// UTILITY FUNCTIONS
// ------------------------------

/**
 * Formatta i secondi in formato MM:SS
 * @param {number} seconds - Secondi da formattare
 * @returns {string} Tempo formattato (es. "04:51")
 */
function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

/**
 * Recupera un parametro dall'URL
 * @param {string} name - Nome del parametro
 * @returns {string|null} Valore del parametro o null
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
 * Recupera la modalità di gioco dall'URL
 * @returns {string} Modalità di gioco (default: "Sfida")
 */
function GetMode() {
    const mode = getParameterByName("mode");
    if (mode) {
        const trimmed = mode.replace(/[^a-zA-Z0-9\s]/g, " ").trim();
        return trimmed;
    }
    return "Sfida"; // Default
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

/**
 * Verifica se esiste una sessione di gioco attiva per la modalità Scalata
 * @param {number} playerId - ID del giocatore
 * @returns {Promise<Object|null>} Dati della sessione esistente o null
 */
async function checkScalataSession(playerId) {
    const mode = "Scalata";
    const url = `/api/gameEngine/session/gamemode/${playerId}?mode=${mode}`;
    
    try {
        const response = await $.ajax({
            url: url,
            type: "GET",
            xhrFields: { withCredentials: true }
        });
        
        console.log("[gamemode_scalata] Sessione esistente trovata:", response);
        return response;
    } catch (error) {
        console.log("[gamemode_scalata] Nessuna sessione attiva");
        return null;
    }
}

// ------------------------------
// GESTIONE SESSIONE E SCHEDE
// ------------------------------

/**
 * Aggiorna il DOM con i dati della sessione esistente
 * Mostra scheda_continua_scalata se c'è una sessione, altrimenti scalate-container
 */
function updateDOMWithPreviousScalataData(sessionData) {
    if (sessionData) {
        console.log("[gamemode_scalata] Scalata in corso, mostro scheda continua");
        
        // Nascondi container scalate
        const scalateContainer = document.getElementById("scalate-container");
        const submitButton = document.getElementById("submit-button");
        
        if (scalateContainer) scalateContainer.classList.add("d-none");
        if (submitButton) submitButton.parentElement.classList.add("d-none");
        
        // Mostra scheda continua
        document.getElementById("scheda_continua_scalata").classList.remove("d-none");
        
        // Popola i dati nella scheda continua
        // ATTENZIONE: Usiamo i nomi dei campi JSON (@JsonProperty) non i nomi Java!
        // class_ut (non classUTName), remainingTime resta uguale perché non ha @JsonProperty custom
        document.getElementById("gamemode_scalata_nome").innerText = sessionData.scalataName || "";
        document.getElementById("gamemode_livello_corrente").innerText = sessionData.currentLevel || 1;
        document.getElementById("gamemode_livelli_totali").innerText = sessionData.totalLevels || "N/A";
        document.getElementById("gamemode_modalita").innerText = "Scalata";
        
        // Formatta e mostra il tempo rimanente
        const remainingTime = sessionData.remainingTime || 0;
        document.getElementById("gamemode_time_limit").innerText = formatTime(remainingTime);
        
        // Imposta il link per riprendere la partita con remainingTime
        // Usa class_ut dal JSON, non classUTName
        const linkRiprendi = document.getElementById("Continua");
        const classUT = sessionData.class_ut || sessionData.classUTName || "";
        
        linkRiprendi.href = `/editor?ClassUT=${classUT}&mode=Scalata&remainingTime=${remainingTime}`;
        
        console.log("[gamemode_scalata] Link riprendi impostato:", linkRiprendi.href);
        console.log("[gamemode_scalata] ClassUT dalla sessione:", classUT);
        console.log("[gamemode_scalata] Remaining time dalla sessione:", remainingTime);
        
    } else {
        console.log("[gamemode_scalata] Nessuna scalata in corso, mostro scheda nuovo");
        
        // Mostra container scalate
        const scalateContainer = document.getElementById("scalate-container");
        const submitButton = document.getElementById("submit-button");
        
        if (scalateContainer) scalateContainer.classList.remove("d-none");
        if (submitButton) submitButton.parentElement.classList.remove("d-none");
        
        // Nascondi scheda continua
        document.getElementById("scheda_continua_scalata").classList.add("d-none");
    }
}

/**
 * Controlla se esiste una sessione attiva per la modalità Scalata
 * @param {number} playerId - ID del giocatore
 * @returns {Promise<Object|null>} Dati della sessione o null se non esiste
 */
async function checkScalataSession(playerId) {
    const mode = "Scalata";
    const url = `/api/gameEngine/session/gamemode/${playerId}?mode=${mode}`;
    
    try {
        const response = await $.ajax({
            url: url,
            type: "GET",
            xhrFields: { withCredentials: true }
        });
        
        console.log("[gamemode_scalata] Sessione esistente trovata:", response);
        return response;
    } catch (error) {
        console.log("[gamemode_scalata] Nessuna sessione attiva");
        return null;
    }
}

/**
 * Elimina la sessione corrente per permettere di avviare una nuova scalata
 */
async function deleteScalataSession() {
    // Recupera il playerId dal token JWT
    const jwtToken = getCookie("jwt");
    const playerId = parseJwt(jwtToken).userId;
    
    const url = `/api/gameEngine/session/gamemode/${playerId}?mode=Scalata`;
    
    try {
        await $.ajax({
            url: url,
            type: "DELETE",
            xhrFields: { withCredentials: true }
        });
        console.log("[gamemode_scalata] Sessione eliminata con successo");
        return true;
    } catch (error) {
        console.error("[gamemode_scalata] Errore nell'eliminazione della sessione:", error);
        return false;
    }
}

// Quando il documento è pronto
$(document).ready(async function() {
    console.log("[gamemode_scalata] ========== INIZIALIZZAZIONE PAGINA ==========");
    
    // 1. CONTROLLA SE ESISTE UNA SESSIONE ATTIVA
    const jwtToken = getCookie("jwt");
    console.log("[gamemode_scalata] JWT Token:", jwtToken ? "presente" : "assente");
    
    const playerId = parseJwt(jwtToken).userId;
    console.log("[gamemode_scalata] Player ID:", playerId);
    
    const existingSession = await checkScalataSession(playerId);
    console.log("[gamemode_scalata] Existing session:", existingSession);
    
    updateDOMWithPreviousScalataData(existingSession);
    
    // 2. INIZIALIZZA LE CARD DELLE SCALATE con event delegation
    // Usa event delegation sul container invece che sulle card direttamente
    // Questo funziona anche se le card sono nascoste o caricate dinamicamente
    $('#scalate-container').on('click', '.scalata-card', function() {
        console.log("=== CLICK RILEVATO SU CARD SCALATA ===");
        console.log("[gamemode_scalata] Click rilevato su card tramite delegation");
        selectScalata($(this));
    });
    
    const scalateCards = $('.scalata-card');
    console.log("[gamemode_scalata] Numero di card trovate:", scalateCards.length);
    console.log("[gamemode_scalata] Card HTML:", scalateCards.html());
    
    // 3. EVENT LISTENER PER IL BOTTONE SUBMIT (nella scheda nuovo)
    $('#submit-button').on('click', async function() {
        if (!selectedScalata) {
            swal({
                title: "Attenzione!",
                text: "Seleziona una scalata prima di procedere",
                icon: "warning",
                button: "OK"
            });
            return;
        }
        
        // Avvia la scalata selezionata
        console.log("[gamemode_scalata] Avvio scalata:", selectedScalata);
        startScalata();
    });
    
    // 4. EVENT LISTENER PER IL BOTTONE "NUOVA SCALATA" (nella scheda continua)
    $('#new_game').on('click', async function() {
        console.log("[gamemode_scalata] Click su 'Nuova partita'");
        
        // Chiedi conferma
        swal({
            title: "Sei sicuro?",
            text: "Vuoi abbandonare la scalata corrente e iniziarne una nuova?",
            icon: "warning",
            buttons: {
                cancel: {
                    text: "Annulla",
                    value: null,
                    visible: true
                },
                confirm: {
                    text: "Sì, abbandona",
                    value: true,
                    className: "btn-danger"
                }
            },
            dangerMode: true
        }).then(async (willDelete) => {
            if (willDelete) {
                console.log("[gamemode_scalata] Confermato abbandono, elimino sessione");
                
                // Elimina la sessione
                const deleted = await deleteScalataSession();
                if (deleted) {
                    console.log("[gamemode_scalata] Sessione eliminata, mostro scheda nuovo");
                    
                    // Nascondi scheda continua
                    document.getElementById("scheda_continua_scalata").classList.add("d-none");
                    
                    // Mostra container scalate
                    const scalateContainer = document.getElementById("scalate-container");
                    const submitButton = document.getElementById("submit-button");
                    
                    if (scalateContainer) scalateContainer.classList.remove("d-none");
                    if (submitButton) submitButton.parentElement.classList.remove("d-none");
                    
                    swal("Sessione eliminata!", "Puoi ora selezionare una nuova scalata", "success");
                } else {
                    console.error("[gamemode_scalata] Errore eliminazione sessione");
                    swal("Errore!", "Impossibile eliminare la sessione", "error");
                }
            }
        });
    });
});

/**
 * Gestisce la selezione di una scalata
 * @param {jQuery} $card - La card jQuery cliccata
 */
function selectScalata($card) {
    console.log("[gamemode_scalata] Card cliccata:", $card);
    
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
            if (mode === "Scalata")
					timer_remainingTime = remainingTime;
                    scalata_name = selectedScalata.name;
                    scalata_currentLevel = 1;
                    scalata_totalLevels = selectedScalata.totalLevels;

            window.location.href = `/editor?ClassUT=${underTestClassName}&mode=${mode}`;
        })
        .catch((error) => {
            console.error("Errore nell'avvio della scalata:", error);
            
            // Estrai il messaggio di errore dettagliato
            let errorMessage = "Impossibile avviare la scalata. Riprova più tardi.";
            
            if (error && error.errors && Array.isArray(error.errors)) {
                // Se ci sono errori di validazione, mostrali tutti
                errorMessage = "Errori di validazione:\n" + error.errors.map(err => `• ${err}`).join('\n');
            } else if (error && error.message) {
                errorMessage = error.message;
            } else if (error && typeof error === 'string') {
                errorMessage = error;
            }
            
            console.error("[gamemode_scalata] Messaggio errore elaborato:", errorMessage);
            
            swal({
                title: "Errore!",
                text: errorMessage,
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

// ------------------------------
// FUNZIONI PER GESTIRE LA SESSIONE CON REDIS
// ------------------------------

// --- Funzione per ottenere il gamemode dalla sessione  ---
// Funzione getGameMode riscritta usando `fetch`
async function getGameMode(playerId, mode) {
	const url = `/api/gameEngine/session/gamemode/${playerId}?mode=${mode}`;
	try {
		const response = await fetch(url, {
			method: "GET", // tipo di richiesta GET
			headers: {
				"Content-Type": "application/json", // Impostazione per la richiesta
			},
		});

		// Se la risposta non è ok, lanciamo un errore
		if (!response.ok) {
			throw new Error(`Errore nella richiesta: ${response.statusText}`);
		}

		// Parsa la risposta come JSON
		const data = await response.json();
		return data; // Restituisce i dati della risposta JSON
	} catch (error) {
		// Gestione dell'errore in caso di problema nella chiamata fetch
		console.error("Errore nella chiamata a getGameMode:", error);
		throw error; // Rilancia l'errore per poterlo gestire più avanti
	}
}

// -- Funzione per aggiornare il gamemode della sessione ---
function putGameMode(
	playerId,
	mode,
	underTestClassName,
	typeRobot,
	difficulty
) {
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `session/gamemode/${playerId}`,
			type: "PUT",
			contentType: "application/json",
			data: JSON.stringify({
				mode: mode,
				playerId: playerId,
				underTestClassName: underTestClassName,
				type_robot: typeRobot,
				difficulty: difficulty,
			}),
			success: function (response) {
				resolve(response);
			},
			error: function (xhr) {
				reject(xhr.responseText);
			},
		});
	});
}

// -- Funzione per far partire un game ---
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
				resolve(response);
			},
			error: function (xhr) {
				reject(xhr.responseJSON || xhr.responseText);
			},
		});
	});
}

async function fetchPreviousGameData() {
	const playerId = String(parseJwt(getCookie("jwt")).userId); // recupera il playerId dal JWT
	const gamemode = GetMode(); // recupera la modalità di gioco
	try {
		// Uso di await per ottenere i dati in modo sincrono
		const response = await getGameMode(playerId, gamemode);
		// Verifica se la modalità corrisponde
		if (response && response.mode == gamemode) {
			console.log(
				"[fetchPreviousGameData] Trovato gameobject per la modalità " + gamemode
			);
			return response;
		} else {
			console.log("[fetchPreviousGameData] Modalità non corrispondente.");
			return null;
		}
	} catch (error) {
		// Gestione dell'errore
		console.error("Errore durante il recupero della sessione:", error);
		return null;
	}
}

async function putGameMode() {
	const playerId = String(parseJwt(getCookie("jwt")).userId);
	const currentMode = GetMode();
	createGameMode(
		playerId,
		currentMode,
		underTestClassName,
		typeRobot,
		difficulty
	)
		.then((response) => {
			console.log("Modalità creata con successo:", response);
		})
		.catch((error) => {
			console.error("Modalità non creata Errore:", error);
		});
}

async function deleteModalita(mode) {
	const playerId = String(parseJwt(getCookie("jwt")).userId);
	// const url = `/session/gamemode/${playerId}?mode=${mode}`
	const url = `/api/gameEngine/SurrenderGame/${playerId}?mode=${mode}`;
	try {
		const response = await fetch(url, {
			method: "DELETE",
		});
		const result = await response.text();
		console.log("Modalità eliminata:", result);
	} catch (error) {
		console.error("Errore durante l'eliminazione della modalità:", error);
	}
}