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

// ------------------------------
// UTILITY FUNCTIONS
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