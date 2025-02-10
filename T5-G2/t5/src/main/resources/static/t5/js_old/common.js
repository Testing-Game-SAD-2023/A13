// Funzione per ottenere una classe specifica da un array JSON delle scalate
function getScalataClasse(roundId, scalateJsonArray) {
    return JSON.parse(scalateJsonArray)[roundId];
}

// Funzione per creare un nuovo gioco
async function createGame(robot, classe, difficulty, scalataId, username, gamemode) {
    console.log("[createGame] robot: ", robot, " classe: ", classe, " difficulty: ", difficulty, " scalataId: ", scalataId, " username: ", username, " gamemode: ", gamemode);
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/api/save-data',
            data: {
                playerId: parseJwt(getCookie("jwt")).userId, // Estrae l'ID del giocatore dal token JWT
                classe: classe, // Classe selezionata
                robot: robot, // Robot selezionato
                difficulty: difficulty, // Difficoltà del gioco
                selectedScalata: scalataId, // ID della scalata selezionata
                gamemode: gamemode, // Modalità di gioco
                username: parseJwt(getCookie("jwt")).sub // Estrae il nome utente dal token JWT
            },
            type: 'POST',
            traditional: true,
            success: function (response) {
                console.log("[createGame] Dati salvati con successo:", response);
                // Salva gli ID del gioco, turno e round nel localStorage
                localStorage.setItem("gameId", response.game_id);
                localStorage.setItem("turnId", response.turn_id);
                localStorage.setItem("roundId", response.round_id);
                resolve(response);
            },
            dataType: "json",
            error: function (error) {
                console.error("[createGame] Errore durante l'invio dei dati: ", error);
                reject({
                    source: "createGame",
                    message: error.responseText || "Errore sconosciuto durante la creazione del gioco."
                });
            }
        });
    });
}

// Funzione per incrementare il round della scalata
async function incrementScalataRound(scalataId, roundId) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/scalates/' + scalataId, // URL dell'API
            type: 'PUT',
            contentType: "application/json",
            data: JSON.stringify({
                CurrentRound: roundId // Aggiorna il round corrente
            }),
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error("Errore nell'invio dei dati");
                reject(error);
            }
        });
    });
}

// Funzione per chiudere una scalata
async function closeScalata(scalataId, isWin, finalScore, roundId) {
    localStorage.removeItem("scalataId");
    const data = JSON.stringify({
        CurrentRound: parseInt(roundId), // Round corrente
        IsFinished: isWin, // Stato di completamento
        FinalScore: finalScore, // Punteggio finale
        ClosedAt: new Date().toISOString() // Data e ora di chiusura
    });
    console.log("[closeScalata] json data: " + data);
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/scalates/' + scalataId, // URL dell'API
            type: 'PUT',
            contentType: "application/json",
            data: data, // Dati della scalata
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error("Errore nell'invio dei dati");
                reject(error);
            }
        });
    }); 
}

// TODO: Funzione backend da implementare per calcolare il punteggio finale
async function calculateFinalScore(scalataId) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/api/calculateScalataFinalScore', // Endpoint per il calcolo del punteggio
            data: {
                scalataId: scalataId // ID della scalata
            },
            type: 'GET',
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error("Errore nell'invio dei dati");
                reject(error);
            }
        });
    });
}

// Funzione per recuperare i dati di una scalata
async function retrieveScalata(selectedScalata) {
    return new Promise((resolve, reject) => {
        console.log("Inizio recupero Scalata: ", selectedScalata); // Log di debug
        $.ajax({
            url: `/retrieve_scalata/${selectedScalata}`, // URL dell'API
            type: 'GET',
            success: (data) => {
                console.log("Dati ricevuti da retrieveScalata: ", data); // Log di debug
                // Salva i dati della scalata nel localStorage
                localStorage.setItem("SelectedScalata", selectedScalata);
                localStorage.setItem("total_rounds_of_scalata", data[0].numberOfRounds);
                localStorage.setItem("scalata_classes", JSON.stringify(data[0].selectedClasses));
                localStorage.setItem("classe", data[0].selectedClasses[0]);
                resolve(data);
            },
            error: (error) => {
                console.error("Errore in retrieveScalata: ", error); // Log di debug
                reject({ source: "retrieveScalata", message: error.responseText || "Errore sconosciuto" });
            }
        });
    });
}

// Funzione per creare una nuova scalata
function CreaScalata(selectedScalata, selectRobotValue,selectDifficultyValue) {
    $.ajax({
        url: '/api/save-scalata', // URL dell'API
        type: 'POST',
        data: {
            playerID: parseJwt(getCookie("jwt")).userId, // ID del giocatore
            scalataName: selectedScalata // Nome della scalata
        },
        success: function(data) {
            var result = JSON.parse(data);

            console.log("ScalataGameId: " + result.scalataGameId);

            // Salva i dati della scalata nel localStorage
            localStorage.setItem("modalita", "Scalata");
            localStorage.setItem("scalataId", result.scalataGameId);
            localStorage.setItem("difficulty", selectDifficultyValue);
            localStorage.setItem("robot", selectRobotValue);
            localStorage.setItem("current_round_scalata", 1);
            localStorage.setItem("scalata_name", selectedScalata);

            // Recupera i dati della scalata e crea il gioco
            retrieveScalata(selectedScalata)
                .then(data => {
                    return createGame(selectRobotValue, data[0].selectedClasses[0], selectDifficultyValue, result.scalataGameId, username, "Scalata");
                })
                .then(() => {
                    console.log(data);
                    window.location.href = "editor_old";
                })
                .catch((error) => {
                    console.log("error: " + error);
                    swal("Errore!", "Si è verificato un errore durante la creazione del gioco. Riprovare.", "error");
                });
        },
        error: function(error) {
            console.log(error);
        }
    });
}
