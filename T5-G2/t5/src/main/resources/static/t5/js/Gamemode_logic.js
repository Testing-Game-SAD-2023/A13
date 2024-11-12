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

resetGame = false;
// Funzione per ottenere i parametri dall'URL
function getParameterByName(name) {
	const url = window.location.href;
	name = name.replace(/[\[\]]/g, "\\$&");
	const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
	const results = regex.exec(url);
	if (!results) return null;
	if (!results[2]) return "";
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}
// Funzione per aggiornare la modalità selezionata
function SetMode(setM) {
	if(!setM){
		sanitizedMode = GetMode();
		const elements = document.querySelectorAll(".selectedMode");
		elements.forEach((element) => {
			element.textContent += " " + get_mode_text(sanitizedMode);
		});
	}else{
		localStorage.setItem("modalita", sanitizedMode);
	}

	// Rende invisibile il select del robot e difficoltà se la modalità è allenamento
	const selectRobotElement = document.getElementById("robot_selector");
	const selectdifficultyElemenet = document.getElementById("difficulty_selector");
	if (sanitizedMode === "Allenamento") {
		selectRobotElement.classList.add("d-none");
		selectdifficultyElemenet.classList.add("d-none");
	} else {
		selectRobotElement.classList.remove("d-none");
		selectdifficultyElemenet.classList.remove("d-none");
	}
}
function GetMode() {
	const mode = getParameterByName("mode");
	if (mode) {
		const sanitizedMode = mode.replace(/[^a-zA-Z0-9\s]/g, " ");
		return sanitizedMode;
	}
	return null;
}
// Funzione per salvare i valori nel localStorage
function saveToLocalStorage() {
	const selectClassValue = document.getElementById("select_class").value;
	const selectRobotValue = document.getElementById("select_robot").value;
	const selectDifficultyValue = document.getElementById("select_diff").value;
	// Salva i valori nel localStorage
	localStorage.setItem("underTestClassName", selectClassValue);
	localStorage.setItem("robot", selectRobotValue);
	localStorage.setItem("difficulty", selectDifficultyValue);
	localStorage.setItem("modalita", GetMode());
}


// Funzione per ottenere i dati della partita precedente solo se l'ID utente coincide
async function fetchPreviousGameData() {
	try {
		const response = await fetch("/StartGame", { method: "GET" });
		const data = await response.json();

		// Verifica se i dati per l'utente corrente sono presenti
		if (data[userId]) {
			return data[userId]; // Ritorna i dati solo se l'ID coincide
		} else {
			return null; // Se l'ID non corrisponde, ritorna null
		}
	} catch (error) {
		console.error(
			"Errore durante il recupero dei dati della partita precedente:",
			error
		);
		return null;
	}
}
function redirectToMain() {
	window.location.href = "/main";
}
function toggleVisibility(elementId) {
	var element = document.getElementById(elementId);
	if (element) {
		element.classList.toggle("d-none");
	} else {
		console.error("Elemento non trovato con ID:", elementId);
	}
}
function pulisciLocalStorage(chiave) {
	// Controlla se la chiave esiste nel localStorage
	if (localStorage.getItem(chiave)) {
		// Rimuovi la chiave dal localStorage
		localStorage.removeItem(chiave);
		console.log(`Dati associati a "${chiave}" rimossi dal localStorage.`);
	} else {
		console.log(`Nessun dato trovato per la chiave "${chiave}".`);
	}
}
//pulizia local storage
function flush_localStorage() {
	//Pulisco i dati locali
	pulisciLocalStorage("difficulty");
	pulisciLocalStorage("modalita");
	pulisciLocalStorage("robot");
	pulisciLocalStorage("roundId");
	pulisciLocalStorage("turnId");
	pulisciLocalStorage("gameId");
	pulisciLocalStorage("underTestClassName");
	pulisciLocalStorage("username");
	pulisciLocalStorage("storico");
	pulisciLocalStorage("codeMirrorContent");
}
// Funzione per eseguire la richiesta AJAX
async function runGameActionElimina(url, formData, isGameEnd) {
    try {
        formData.append("isGameEnd", isGameEnd);
        const response = await ajaxRequest(url, "POST", formData, false, "json");
        return response;
    } catch (error) {
        console.error("Errore nella richiesta AJAX:", error);
        throw error;
    }
}
// Codice da eseguire alla creazione della pagina
document.addEventListener("DOMContentLoaded", async function () {
	const previousGameData = await fetchPreviousGameData();
	SetMode(false);
	if (previousGameData !== null) {
		//esiste già una partita su questo player id
		console.log("modalità continua");
		toggleVisibility("scheda_nuovo");
		toggleVisibility("scheda_continua");
		document.getElementById("gamemode_classeUT").textContent =
			previousGameData.classeUT;
		document.getElementById("gamemode_robot").textContent =
			previousGameData.type_robot;
		document.getElementById("gamemode_difficulty").textContent =
			previousGameData.difficulty;
		document.getElementById("gamemode_modalita").textContent =
			previousGameData.mode;

		// Setto tasto continua
		const link = document.getElementById("Continua");
		link.setAttribute("href", "/editor?ClassUT=" + previousGameData.classeUT);
	}else{
		flush_localStorage();
		SetMode(true);
	}
});

document.getElementById("new_game").addEventListener("click", function () {
	toggleVisibility("scheda_nuovo");
	toggleVisibility("scheda_continua");
	toggleVisibility("alert_nuova");
	resetGame = true;
});

document.getElementById("link_editor").addEventListener("click", async function () {
	const selectClassValue = document.getElementById("select_class").value;
	if (resetGame) {
		const formData = new FormData();
		formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
		formData.append("eliminaGame", true);
		const response = runGameActionElimina("/run", formData, true);
		flush_localStorage();
	}
	saveToLocalStorage();
	// Aggiorna il link
	const link = document.getElementById("link_editor");
	link.setAttribute("href", "/editor?ClassUT=" + selectClassValue);
});

//Tasto submit disabilitato fin quando non ho selezionato dei parametri
// Funzione generica per verificare le select e abilitare/disabilitare il pulsante
function updateButtonState() {
	const submitButton = document.getElementById("link_editor");
	const mode = GetMode();
	const allSelected = document.getElementById("select_class").value 
						&& document.getElementById("select_robot").value 
						&& document.getElementById("select_diff").value;
			
	const classSelected = document.getElementById("select_class").value;
	// Se mi trovo in allenamento mi interessa controllare solo la classe, altrimenti devo controllare tutto 
	submitButton.classList.toggle("disabled", mode !== "Allenamento" ? !allSelected : !classSelected);
}

// Aggiungi un evento change a ciascun select
document
	.getElementById("select_class")
	.addEventListener("change", updateButtonState);
document
	.getElementById("select_robot")
	.addEventListener("change", updateButtonState);
document
	.getElementById("select_diff")
	.addEventListener("change", updateButtonState);
