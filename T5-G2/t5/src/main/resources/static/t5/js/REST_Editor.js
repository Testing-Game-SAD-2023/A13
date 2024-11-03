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

let isActionInProgress = false; // Flag per indicare se un'azione è in corso

// Gestione del click del pulsante
async function handleGameAction(isGameEnd) {

    isActionInProgress = true; // Imposta il flag a true

    //disabilito i tasti durante l'azione 
    run_button.disabled = true;
    coverage_button.disabled = true;
    
    toggleLoading(true, isGameEnd ? "loading_run" : "loading_cov", isGameEnd ? "runButton" : "coverageButton");
    setStatus("sending");
	//pulisco console utente prima di eseguire
    const formData = getFormData();
    const response = await runGameAction("/run", formData, isGameEnd);
    setStatus("compiling");
    const { robotScore, userScore, outCompile, coverage, GameOver, gameId, roundId } = response;
    // Aggiornamento dei dati del form
    formData.append("gameId", gameId);
    formData.append("roundId", roundId);
    console_utente.setValue(outCompile);
    parseMavenOutput(outCompile);
    if (!coverage) {
		//Errore di compilazione
        setStatus("error");
        console_robot.setValue(getConsoleTextError());
        isActionInProgress = false; // Imposta il flag a false in caso di errore
        return;
    }
    highlightCodeCoverage($.parseXML(coverage), editor_robot);
    orderTurno++;
    const url = createApiUrl(formData, orderTurno);
    setStatus("loading");
	// Chiamo T8 per recuperare il report di coverage
    const csvContent = await ajaxRequest(url, "POST", formData.get("testingClassCode"), false, "text");
    const valori_csv = extractThirdColumn(csvContent);
    
	// Aggiorno lo storico della partita 
    updateStorico(orderTurno, userScore, valori_csv[0]);

    setStatus(isGameEnd ? "game_end" : "turn_end");
	toggleLoading(false, isGameEnd ? "loading_run" : "loading_cov", isGameEnd ? "runButton" : "coverageButton");
	// Determina il valore di displayUserPoints in base a isGameEnd
	const displayUserPoints = isGameEnd 
	? getConsoleTextRun(valori_csv, 0, robotScore, userScore) 
	: getConsoleTextCoverage(valori_csv, userScore);
	// Imposta il valore nel console_robot
	console_robot.setValue(displayUserPoints);
    if (isGameEnd) {
        openModalWithText(
            status_game_end ,
            `${score_partita_text} ${userScore} pt.`,
            [{ text: vai_home, href: '/main', class: 'btn btn-primary' }]
        );
        flush_localStorage();
        //La partita è finita quindi non resetto i tasti 
    }else{
        run_button.disabled = (localStorage.getItem("modalita") === "Allenamento");
        coverage_button.disabled = false;
    }
    isActionInProgress = false; // Imposta il flag a false al termine dell'azione
}

// Gestione dell'evento beforeunload
window.addEventListener('beforeunload', (event) => {
    if (isActionInProgress) {
        const confirmationMessage = 'Stai per aggiornare la pagina durante un caricamento. La seguente azione causerà inconsistenza nei dati e vanificherà i tuoi sforzi nella sfida attuale. Vuoi continuare?';
        event.returnValue = confirmationMessage; // Firefox
        return confirmationMessage; // Standard
    }
});
// Pulsante "Run/Submit"
document.getElementById("runButton").addEventListener("click", () => handleGameAction(true));
// Pulsante "Coverage"
document.getElementById("coverageButton").addEventListener("click", () => handleGameAction(false));