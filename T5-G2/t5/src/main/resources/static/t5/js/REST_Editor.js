/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/* REST_Editor.js
   Gestione delle chiamate REST per l’editor.
   (Dipende da Util_Editor.js, che deve essere caricato prima.)
*/

async function getGameActionRequestBody() {
    console.log("getGameActionRequestBody");
    console.log(editor_robot.getValue());
    let requestBody = {
        playerId: jwtData.userId,
        mode: GetMode(),
        testingClassCode: editor_utente.getValue(),
        classUTCode: editor_robot.getValue()
    };

    if (GetMode() === "PartitaSingola")
        requestBody["remainingTime"] = timer_remainingTime.toString();

	return requestBody;
}

// Funzione per salvare la sessione quando l'utente lascia la pagina (necessaria per implementare il timer)
async function handleBeforeUnload(e) {
    let leaveGameRequestBody = await getGameActionRequestBody();
    console.log("[POST /leave] Sending request upon leaving");
    navigator.sendBeacon("/api/gameEngine/leave", JSON.stringify(leaveGameRequestBody));
}
window.addEventListener("beforeunload", handleBeforeUnload);

// === FUNZIONE PER LA RICHIESTA AJAX DEL GIOCO ===
async function runGameAction(url, runGameRequestBody) {
    try {
        console.log("runGameRequestBody", runGameRequestBody);
        const response = await ajaxRequest_ForRun(url, "POST", runGameRequestBody, false, "json");
        return response;
    } catch (error) {
        console.error("Errore nella richiesta AJAX:", error);
        throw error;
    }
}

$(document).ready(function () {
    const savedContent = localStorage.getItem('codeMirrorContent');
    if (!savedContent) {
        const currentDate = new Date();
        const formattedDate = `${String(currentDate.getDate()).padStart(2, "0")}/${String(currentDate.getMonth() + 1).padStart(2, "0")}/${currentDate.getFullYear()}`;
        const replacements = {
            TestClasse: `Test${ClassName}`,
            username: jwtData.sub,
            userID: jwtData.userId,
            date: formattedDate,
        };
        SetInitialEditor(replacements);
    } else {
        editor_utente.setValue(savedContent);
        editor_utente.refresh();
    }
    
    if (localStorage.getItem('storico')) {
        viewStorico();
    }
});

let isActionInProgress = false; // Flag per indicare se un'azione è attualmente in corso

// Funzione principale per gestire l'azione del gioco
async function handleGameAction(isGameEnd, compileUponEndTime=false) {
    isActionInProgress = true; // Imposta il flag per bloccare altre azioni
    run_button.disabled = true; // Disabilita il pulsante di esecuzione
    coverage_button.disabled = true; // Disabilita il pulsante di coverage
    // Determina le chiavi per il caricamento e il pulsante in base a isGameEnd
    const loadingKey = isGameEnd ? "loading_run" : "loading_cov";
    const buttonKey = isGameEnd ? "runButton" : "coverageButton";
    // Mostra l'indicatore di caricamento
    toggleLoading(true, loadingKey, buttonKey);
    // Aggiorna lo stato a "sending"
    setStatus("sending");
    // Otteniamo il requestBody (con debug sul codice dell'editor)
    let requestBody = await getGameActionRequestBody()

    if (isGameEnd) {
        try {
            //Esegue la terminazione del gioco

            if (!compileUponEndTime) {
                requestBody["testingClassCode"] = "";
                setStatus("game_end");
                const endResponse = await runGameAction("/api/gameEngine/EndGame", requestBody);

                console.error(endResponse);

                handleGameRun(endResponse.runGameResponse, loadingKey, buttonKey, true);
                handleGameEnd(endResponse);
                toggleLoading(false, loadingKey, buttonKey);
            } else {
                const endResponse = await runGameAction("/api/gameEngine/EndGame", requestBody);

                handleGameRun(endResponse.runGameResponse, loadingKey, buttonKey, true);
                handleGameEnd(endResponse);
                toggleLoading(false, loadingKey, buttonKey);
            }
        } catch (error) {
            console.error("[handleGameAction] Errore durante l'esecuzione:", error);
            handleServerInternalError(error, loadingKey, buttonKey);
        }
    } else {
        try {
            //Esegue l'azione di gioco
            const response = await runGameAction("/api/gameEngine/run", requestBody);
            setStatus("compiling");
            handleGameRun(response, loadingKey, buttonKey, false);
            resetButtons();
        } catch (error) {
            handleServerInternalError(error, loadingKey, buttonKey);
        }
    }
    isActionInProgress = false;
}

function handleServerInternalError(error, loadingKey, buttonKey) {
    console.error("[handleGameAction] Errore durante l'esecuzione:", error);
    const errorObject = JSON.parse(error.responseText);

    try {
        switch (error.status) {
            case 429:
                console_robot.setValue(errorMessages["429"]);
                break;
            case 504:
                console_robot.setValue(errorMessages["504"]);
                break;
            default:
                const formattedError = JSON.stringify(errorObject, null, 4);
                console_robot.setValue(formattedError);
                break;
        }
    } catch (e) {
        // Se non è un JSON valido stampo l'errore raw
        console_robot.setValue(error.responseText);
    }

    toggleLoading(false, loadingKey, buttonKey); // Nasconde l'indicatore di caricamento
    resetButtons(); // Reimposta i pulsanti
}


function handleGameEnd(response) {
    let {userScore, robotScore, isWinner, expGained, achievementsUnlocked} = response;
    console.log("achievementsUnlocked", achievementsUnlocked);

    generateEndGameMessage(userScore, robotScore, isWinner, expGained, achievementsUnlocked); // Gestisce la fine del gioco

    // Disattivo il timer
    if (GetMode() === "PartitaSingola")
        stopTimer();

    // Disattivo la chiamata a POST /leave all'uscita dalla pagina
    window.removeEventListener("beforeunload", handleBeforeUnload);
}

function handleGameRun(response, loadingKey, buttonKey, isGameEnd) {
    const {
            userCoverageDetails, robotCoverageDetails,
            canWin, unlockedAchievements,
            userScore, robotScore,
        } = response;

    console.log("game run", response);

    const userOutputCompile = userCoverageDetails.compileOutput;
    const userCoverage_ForHighlight = userCoverageDetails.xml_coverage;
    const robotCoverage_ForHighlight = robotCoverageDetails.xml_coverage;

    console.log("userOutputCompile", userOutputCompile);
    console.log("userCoverage_ForHighlight", userCoverageDetails.xml_coverage);
    console.log("robotCoverage_ForHighlight", robotCoverageDetails.xml_coverage);

    console_utente.setValue(userOutputCompile); // Mostra l'output della compilazione nella console utente
    parseMavenOutput(userOutputCompile); // Analizza l'output di Maven
    if (!userCoverage_ForHighlight) { // Se non c'è copertura, gestisce l'errore di compilazione
        setStatus("error");
        //Gestione degli errori 
        handleCompileError(loadingKey, buttonKey);
        return;
    }

    // Se la copertura è disponibile, la processa
    processCoverage(
        userCoverage_ForHighlight, robotCoverage_ForHighlight,
        userCoverageDetails, robotCoverageDetails,
        userScore, robotScore,
        canWin, unlockedAchievements,
        loadingKey, buttonKey, isGameEnd
    );
}

// Processa la copertura del codice e aggiorna i dati di gioco
async function processCoverage(userCoverage_ForHighlight, robotCoverage_ForHighlight, userCoverageDetails, robotCoverageDetails, userScore, robotScore, canWin, unlockedAchievements, loadingKey, buttonKey, isGameEnd) {
    highlightCodeCoverage($.parseXML(userCoverage_ForHighlight), $.parseXML(robotCoverage_ForHighlight), editor_robot); // Evidenzia la copertura del codice nell'editor
    orderTurno++; // Incrementa l'ordine del turno

    setStatus("loading"); // Aggiorna lo stato a "loading"
    updateStorico(orderTurno, userScore, userCoverageDetails.evosuite_line); // Aggiorna lo storico del gioco

    setStatus("turn_end"); // Imposta lo stato di fine gioco o fine turno
    toggleLoading(false, loadingKey, buttonKey); // Nasconde l'indicatore di caricamento
    displayUserPoints(userCoverageDetails, robotCoverageDetails, canWin, userScore, robotScore, isGameEnd); // Mostra i punti dell'utente
    if (!isGameEnd && unlockedAchievements.length !== 0)
        handleUnlockedAchievements(unlockedAchievements);
}

// Mostra i punti dell'utente nella console
function displayUserPoints(userCoverageDetails, robotCoverageDetails, canWin, userScore, robotScore, isGameEnd) {
    const displayUserPoints = getConsoleTextRun(userCoverageDetails, robotCoverageDetails, canWin, userScore, robotScore, isGameEnd)
    console_robot.setValue(displayUserPoints); // Aggiorna la console del robot con i punti
}

// Gestisce gli errori di compilazione
function handleCompileError(loadingKey, buttonKey) {
    console_robot.setValue(getConsoleTextError()); // Mostra l'errore nella console del robot
    toggleLoading(false, loadingKey, buttonKey); // Nasconde l'indicatore di caricamento
    resetButtons(); // Reimposta i pulsanti
}

// Gestisce la fine del gioco, mostra un messaggio e pulisce i dati
function generateEndGameMessage(userScore, robotScore, isWinner, expGained, achievementsUnlocked) {
    let resultMessage = isWinner ? gameEndData.game_win : gameEndData.game_lose;
    let expMessage = "";
    let achievementsMessage = ""
    let rememberSaveMessage = gameEndData.game_remember_save;

    if (isWinner) {
        if (expGained === 0) {
            expMessage = gameEndData.game_exp.zero;
        } else if (expGained === 1) {
            expMessage = `${gameEndData.game_exp.base} ${gameEndData.game_exp.one}`;
        } else {
            expMessage = `${gameEndData.game_exp.base} ${expGained} ${gameEndData.game_exp.multi}`;
        }

        if (achievementsUnlocked.length > 0) {
            achievementsMessage = "\n\n" + `${unlockedNewAchievementMessage.descr}\n${achievementsUnlocked.map(a => ` - ${achievementData[a]?.name || a}\n`).join("")}`;
            achievementsMessage = achievementsMessage.slice(0, -1);
        }
    } else {
        expMessage = gameEndData.game_retry;
    }

    openModalWithText(
        gameEndData.game_end,
        `${gameEndData.game_score}: ${userScore} pt.\n${resultMessage}\n${expMessage}${achievementsMessage}\n\n${rememberSaveMessage}`,
        [{ tagName: "button", text: `${modalButtonText.close}`, data_bs_dismiss: "modal", class: 'btn btn-primary' }]
    );
}

function handleUnlockedAchievements(unlockedAchievements) {
    openModalWithText(
        unlockedNewAchievementMessage.title,
        `${unlockedNewAchievementMessage.descr}\n${unlockedAchievements.map(a => ` - ${achievementData[a]?.name || a}\n`).join("")}`,
        [{ tagName: "button", text: `${modalButtonText.close}`, data_bs_dismiss: "modal", class: 'btn btn-primary' }]
    );
}


// Reimposta i pulsanti per consentire nuove azioni
function resetButtons() {
    run_button.disabled = (mode === "Allenamento"); // Abilita/disabilita in base alla modalità
    coverage_button.disabled = false; // Abilita il pulsante di coverage
}

/*
*   Se premo il tasto go back quando è in atto un caricamento 
*/
window.addEventListener('beforeunload', (event) => {
    if (isActionInProgress) {
        // Ottieni il link di destinazione. Puoi usare `event.target` per prendere il link dell'evento.
        // Se l'utente sta cercando di navigare tramite un link, usa `document.activeElement.href` se è un link.
        let targetUrl = '';
        // Verifica se l'evento proviene da un link cliccato
        if (document.activeElement && document.activeElement.tagName === 'A') {
            targetUrl = document.activeElement.href;
        }
        // Previeni il comportamento predefinito del browser
        event.preventDefault();
        // Il messaggio predefinito non può essere personalizzato, ma il modal può apparire
        return ''; // Restituisce una stringa vuota per attivare il messaggio predefinito
    }
    // Pulisco il localstorage dal test scritto dall'utente 
    localStorage.removeItem('codeMirrorContent');
    localStorage.removeItem('storico');
});

// Pulsante "Run/Submit"
document.getElementById("runButton").addEventListener("click", () => handleGameAction(true));
// Pulsante "Coverage"
document.getElementById("coverageButton").addEventListener("click", () => handleGameAction(false));