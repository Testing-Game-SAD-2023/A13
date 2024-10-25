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

const getCookie = (name) => {
	const value = `; ${document.cookie}`;
	const parts = value.split(`; ${name}=`);
	if (parts.length === 2) return parts.pop().split(";").shift();
	return null;
};

const parseJwt = (token) => {
	try {
		return JSON.parse(atob(token.split(".")[1]));
	} catch (e) {
		return null;
	}
};

//Variabili varie
var turno = 0; // numero di turni giocati fino ad ora
var current_round_scalata = 0; // round corrente
var total_rounds_scalata = 0; // numero totale di rounds
var selectedScalata = "";
var iGameover = false; // flag per indicare se il giocatore ha vinto o perso
var orderTurno = 0;
var perc_robot = "0"; // percentuale di copertura del robot scelto
var userScore = 0;
var locGiocatore = 0;
var currentDate = new Date();

//chiamata a /StartGame
const data = {
	playerId: String(parseJwt(getCookie("jwt")).userId),
	type_robot: localStorage.getItem("robot"),
	difficulty: localStorage.getItem("difficulty"),
	mode: localStorage.getItem("modalita"),
	underTestClassName: localStorage.getItem("underTestClassName"),
};

$(document).ready(function () {
	startGame(data);

	if(localStorage.getItem("modalita") === "Allenamento"){
		const runButton = document.getElementById("runButton");
		runButton.disabled = true;
		console.log("Sei in allenamento");
	}

	// Format date to dd/mm/yyyy
	const formattedDate = `${String(currentDate.getDate()).padStart(
		2,
		"0"
	)}/${String(currentDate.getMonth() + 1).padStart(
		2,
		"0"
	)}/${currentDate.getFullYear()}`;
	console.log("formattedDate:", formattedDate);

	// Obtain the content of the textarea
	const textareaContent = document.getElementById("Editor_utente").value;

	// Get player details
	const jwtData = parseJwt(getCookie("jwt"));
	const username = jwtData.sub;
	const userId = jwtData.userId;

	// Create a function to handle multiple replacements
	const replacePlaceholders = (content, replacements) => {
		for (const [placeholder, value] of Object.entries(replacements)) {
			content = content.replace(new RegExp(placeholder, "g"), value);
		}
		return content;
	};

	className = localStorage.getItem("underTestClassName");
	var testClassName = "Test" + className;

	// Define replacements
	const replacements = {
		TestClasse: testClassName,
		username: username,
		userID: userId,
		date: formattedDate,
	};

	viewStorico();
	// Perform replacements
	const newContent = replacePlaceholders(textareaContent, replacements);
	console.log("newContent \n\n", newContent);

	// Set the new content to the textarea
	document.getElementById("Editor_utente").value = newContent;
	editor_utente.setValue(newContent);
});

current_round_scalata = localStorage.getItem("current_round_scalata");
total_rounds_scalata = localStorage.getItem("total_rounds_of_scalata");

// Elemento del pulsante "Play/Submit"
var runButton = document.getElementById("runButton");
// Funzione principale per la gestione del click del pulsante
runButton.addEventListener("click", async function () {
	toggleLoading(true, "loading_run", "runButton");
	try {
		console.log("AVVIATA SUBMIT");
		setStatus("Avvio submit");
		const formData = getFormData();
		//qui l'utente fa submit quindi decide di chiudere il gioco
		formData.append("isGameEnd", true);
		for (let [key, value] of formData.entries()) {
			console.log(key, value);
		}

		// Prima richiesta AJAX per eseguire il test
		const response = await ajaxRequest("/run", "POST", formData, false, "json");
		setStatus("Playing turn");
		console.log(response);
		const {
			robotScore,
			userScore,
			outCompile,
			coverage,
			GameOver,
			gameId,
			roundId,
		} = response;
		formData.append("gameId", gameId);
		formData.append("roundId", roundId);

		setStatus("Getting Results");

		console_utente.setValue(outCompile);
		parseMavenOutput(outCompile)

		if (coverage == null || coverage === "") {
			displayUserPoints = getConsoleTextError();
			console_robot.setValue(displayUserPoints);
		} else {
			setStatus(false, "Game end");
			highlightCodeCoverage($.parseXML(coverage));

			orderTurno++;

			const url = createApiUrl(formData, orderTurno);
			console.log("URL post on: " + url);

			// Seconda richiesta AJAX per inviare il codice di test
			const csvContent = await ajaxRequest(
				url,
				"POST",
				formData.get("testingClassCode"),
				false,
				"text"
			);
			var valori_csv = extractThirdColumn(csvContent);
			addStorico(orderTurno,userScore, valori_csv[0]);
			viewStorico();
			displayRobotPoints = getConsoleTextRun(
				csvContent,
				0,
				robotScore,
				userScore
			);
			console_robot.setValue(displayRobotPoints);

			console.log("punteggio robot: " + robotScore);
			console.log("punteggio utente: " + userScore);

			openModalWithText(
				'Partita Terminata !', 
				'Hai terminato la tua partita con un punteggio: ' + userScore + "pt.",
				[
					{ text: 'Vai alla Home', href: '/main', class: 'btn btn-primary' }
				]
			);
			//Pulisco i dati locali 
			flush_localStorage();
		}
	} catch (error) {
		setStatus(false, "Error");
		getConsoleTextError();
		openModalWithText('Errore!',error.message);
	} finally {
		toggleLoading(false, "loading_run", "runButton");
		var coverageButton = document.getElementById("coverageButton");
		coverageButton.disabled = true;
		runButton.disabled = true;
	}
});

// TASTO RUN (COVERAGE)
var coverageButton = document.getElementById("coverageButton");
coverageButton.addEventListener("click", async function () {
	const formData = getFormData();
	toggleLoading(true, "loading_cov", "coverageButton");
	try {
		console.log("AVVIATA COVERAGE");
		setStatus("Avvio Coverage");
		const formData = getFormData();
		formData.append("isGameEnd", false);
		for (let [key, value] of formData.entries()) {
			console.log(key, value);
		}
		// Prima richiesta AJAX per eseguire il test
		const response = await ajaxRequest("/run", "POST", formData, false, "json");
		setStatus("Getting Results");
		console.log(response);
		const {
			robotScore,
			userScore,
			outCompile,
			coverage,
			GameOver,
			gameId,
			roundId,
		} = response;
		formData.append("gameId", gameId);
		formData.append("roundId", roundId);
		console_utente.setValue(outCompile);
		parseMavenOutput(outCompile);
		if (coverage == null || coverage === "") {
			// Errori di compilazione
			displayUserPoints = getConsoleTextError();
			console_robot.setValue(displayUserPoints);
		} else {
			setStatus(false, "Turn ended");
			highlightCodeCoverage($.parseXML(coverage));
			orderTurno++;
			const url = createApiUrl(formData, orderTurno);
			console.log("URL post on: " + url);
			// Seconda richiesta AJAX per inviare il codice di test
			const csvContent = await ajaxRequest(
				url,
				"POST",
				formData.get("testingClassCode"),
				false,
				"text"
			);
			var valori_csv = extractThirdColumn(csvContent);
			addStorico(orderTurno, userScore, valori_csv[0]);
			viewStorico();
			displayUserPoints = getConsoleTextCoverage(csvContent, userScore);
			console_robot.setValue(displayUserPoints);
		}
	} catch (error) {
		error_message = 'Errore durante il recupero del file di output di JaCoCo o la gestione del turno:' + error; 
		openModalWithText('Errore!',error_message);
		console.error(error_message);
		getConsoleTextError();
		setStatus(false, "Error");
	} finally {
		toggleLoading(false, "loading_cov", "coverageButton");
	}
});