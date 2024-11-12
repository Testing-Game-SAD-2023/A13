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

/*
* 		Qui ci sono le chiamate REST dell'editor 
*/
// Funzione per ottenere i dati dal localStorage
function getGameData() {
    let underTestClassName = localStorage.getItem("underTestClassName");
    if (underTestClassName) {
        // La stringa non è né nulla né vuota
        let ClassUT = getParameterByName("ClassUT");
        if(ClassUT === underTestClassName){
            return {
                playerId: 			String(parseJwt(getCookie("jwt")).userId),
                type_robot: 		localStorage.getItem("robot"),
                difficulty: 		localStorage.getItem("difficulty"),
                mode: 				localStorage.getItem("modalita"),
                underTestClassName: localStorage.getItem("underTestClassName"),
            };
        }
    }   
    //modal che ti blocca
    openModalError(
        "Accesso Illegale all'editor ",
        `Non sei passato da Gamemode per settare la tua partita o hai inserito un URL sbagliato.`,
        [{ text: 'Vai alla Home', href: '/main', class: 'btn btn-primary' }]
    );
}

// Funzione per eseguire la richiesta AJAX
async function runGameAction(url, formData, isGameEnd) {
    try {
        formData.append("isGameEnd", isGameEnd);
        const response = await ajaxRequest(url, "POST", formData, false, "json");
        return response;
    } catch (error) {
        console.error("Errore nella richiesta AJAX:", error);
        throw error;
    }
}

// Documento pronto
$(document).ready(function () {
    const data = getGameData();
    startGame(data);
    if (data.mode === "Allenamento") {
        document.getElementById("runButton").disabled = true;
        console.log("Sei in allenamento");
    }
    const savedContent = localStorage.getItem('codeMirrorContent');
	if (!savedContent) {
        //non ho salvato il contenuto dell'editor, quindi lo scarico e imposto il template con i dati dell'utente
        let formattedDate = `${String(currentDate.getDate()).padStart(2, "0")}/${String(currentDate.getMonth() + 1).padStart(2, "0")}/${currentDate.getFullYear()}`;
        let replacements  = {
            TestClasse: `Test${localStorage.getItem("underTestClassName")}`,
            username: jwtData.sub,
            userID: jwtData.userId,
            date: formattedDate,
        };
        SetInitialEditor(replacements);
	}else{
        //Se ho del contenuto salvato 
        editor_utente.setValue(savedContent);
        //document.getElementById('Editor_utente').value = savedContent;
        editor_utente.refresh(); // Ricarica l'editor per applicare le modifiche
    }
    const savedStorico = localStorage.getItem('storico');
    if(savedStorico){
        viewStorico();
    }
});


let isActionInProgress = false; // Flag per indicare se un'azione è attualmente in corso

// Funzione principale per gestire l'azione del gioco
async function handleGameAction(isGameEnd) {
    isActionInProgress = true; // Imposta il flag per bloccare altre azioni
    run_button.disabled = true; // Disabilita il pulsante di esecuzione
    coverage_button.disabled = true; // Disabilita il pulsante di coverage

    // Determina le chiavi per il caricamento e il pulsante in base a isGameEnd
    const loadingKey = isGameEnd ? "loading_run" : "loading_cov";
    const buttonKey = isGameEnd ? "runButton" : "coverageButton";

    toggleLoading(true, loadingKey, buttonKey); // Mostra l'indicatore di caricamento
    setStatus("sending"); // Aggiorna lo stato a "sending"

    const formData = getFormData(); // Recupera i dati del modulo
    const response = await runGameAction("/run", formData, isGameEnd); // Esegue l'azione del gioco
    setStatus("compiling"); // Aggiorna lo stato a "compiling"

    handleResponse(response, formData, isGameEnd, loadingKey, buttonKey); // Gestisce la risposta ricevuta

    isActionInProgress = false; // Reimposta il flag al termine dell'azione
}

// Gestisce la risposta dal server
function handleResponse(response, formData, isGameEnd, loadingKey, buttonKey) {
    const { robotScore, userScore, outCompile, 
            coverage, gameId, roundId,
            coverageDetails} = response;
    // Aggiorna i dati del modulo con gameId e roundId
    formData.append("gameId", gameId);
    formData.append("roundId", roundId);
    console_utente.setValue(outCompile); // Mostra l'output della compilazione nella console utente
    parseMavenOutput(outCompile); // Analizza l'output di Maven
    if (!coverage) { // Se non c'è copertura, gestisce l'errore di compilazione
        setStatus("error");
        handleCompileError(loadingKey, buttonKey); // Gestisce l'errore
        return;
    }
    // Se la copertura è disponibile, la processa
    processCoverage(coverage, formData, robotScore, userScore, isGameEnd, loadingKey, buttonKey, coverageDetails);
}

// Processa la copertura del codice e aggiorna i dati di gioco
async function processCoverage(coverage, formData, robotScore, userScore, isGameEnd, loadingKey, buttonKey, coverageDetails) {
    highlightCodeCoverage($.parseXML(coverage), editor_robot); // Evidenzia la copertura del codice nell'editor
    orderTurno++; // Incrementa l'ordine del turno
    const csvContent = await fetchCoverageReport(formData); // Recupera il report di coverage
    setStatus("loading"); // Aggiorna lo stato a "loading"
    const valori_csv = extractThirdColumn(csvContent); // Estrae i valori dalla terza colonna del CSV
    updateStorico(orderTurno, userScore, valori_csv[0]); // Aggiorna lo storico del gioco
    setStatus(isGameEnd ? "game_end" : "turn_end"); // Imposta lo stato di fine gioco o fine turno
    toggleLoading(false, loadingKey, buttonKey); // Nasconde l'indicatore di caricamento
    displayUserPoints(isGameEnd, valori_csv, robotScore, userScore, coverageDetails); // Mostra i punti dell'utente
    if (isGameEnd) { // Se il gioco è finito
        handleEndGame(userScore); // Gestisce la fine del gioco
    } else {
        resetButtons(); // Reimposta i pulsanti
    }
}

// Mostra i punti dell'utente nella console
function displayUserPoints(isGameEnd, valori_csv, robotScore, userScore, coverageDetails) {
    const displayUserPoints = isGameEnd 
        ? getConsoleTextRun(valori_csv, coverageDetails, robotScore, userScore) // Testo per la fine del gioco
        : getConsoleTextCoverage(valori_csv, userScore, coverageDetails); // Testo per la copertura

    console_robot.setValue(displayUserPoints); // Aggiorna la console del robot con i punti
}

// Gestisce gli errori di compilazione
function handleCompileError(loadingKey, buttonKey) {
    console_robot.setValue(getConsoleTextError()); // Mostra l'errore nella console del robot
    toggleLoading(false, loadingKey, buttonKey); // Nasconde l'indicatore di caricamento
    resetButtons(); // Reimposta i pulsanti
}

// Recupera il report di coverage da T8
async function fetchCoverageReport(formData) {
    const url = createApiUrl(formData, orderTurno); // Crea l'URL dell'API
    return await ajaxRequest(url, "POST", formData.get("testingClassCode"), false, "text"); // Esegue la richiesta AJAX
}

// Gestisce la fine del gioco, mostra un messaggio e pulisce i dati
function handleEndGame(userScore) {
    openModalWithText(
        status_game_end,
        `${score_partita_text} ${userScore} pt.`, // Mostra il punteggio dell'utente
        [{ text: vai_home, href: '/main', class: 'btn btn-primary' }] // Pulsante per tornare alla home
    );
    flush_localStorage(); // Pulisce i dati salvati nel localStorage
}

// Reimposta i pulsanti per consentire nuove azioni
function resetButtons() {
    run_button.disabled = (localStorage.getItem("modalita") === "Allenamento"); // Abilita/disabilita in base alla modalità
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
});

// Pulsante "Run/Submit"
document.getElementById("runButton").addEventListener("click", () => handleGameAction(true));
// Pulsante "Coverage"
document.getElementById("coverageButton").addEventListener("click", () => handleGameAction(false));