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

// ------------------------------
// FUNZIONI DI UTILITÀ
// ------------------------------
function getParameterByName(name) {
	const url = window.location.href;
	name = name.replace(/[\[\]]/g, "\\$&");
	const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
	const results = regex.exec(url);
	if (!results) return null;
	if (!results[2]) return "";
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function GetMode() {
	const mode = getParameterByName("mode");
	if (mode) {
		const trimmed = mode.replace(/[^a-zA-Z0-9\s]/g, " ").trim();
		return trimmed;
	}
	return "Sfida"; // Default in forma capitalizzata
}

function SetMode(setM) {
	const currentMode = GetMode();
	if (!setM) {
		const elements = document.querySelectorAll(".selectedMode");
		elements.forEach((element) => {
			element.textContent += " " + gameMode[currentMode];
		});
	}
	const selectRobotElement = document.getElementById("robot_selector");
	const selectDifficultyElement = document.getElementById(
		"difficulty_selector"
	);
	if (currentMode === "Allenamento") {
		selectRobotElement.classList.add("d-none");
		selectDifficultyElement.classList.add("d-none");
	} else {
		selectRobotElement.classList.remove("d-none");
		selectDifficultyElement.classList.remove("d-none");
	}
}

// Rendo visibile la selezione del timer se la partita è di tipo PartitaSingola
document.addEventListener("DOMContentLoaded", function () {
	let timeLimitField = document.getElementById("time_limit_selector");
	timeLimitField.style.display = (GetMode() === "PartitaSingola") ? "block" : "none";
});

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

// ------------------------------
// FUNZIONI PER GESTIRE IL GIOCO
// ------------------------------
async function startGame() {
	const playerId = String(parseJwt(getCookie("jwt")).userId);
	const mode = GetMode();
	const underTestClassName = document.getElementById("select_class").value;
	let typeRobot = "";
	let difficulty = "";
	let remainingTime = 0;

	if (mode === "Sfida" || mode === "PartitaSingola") {
		typeRobot = document.getElementById("select_robot").value;
		difficulty = document.getElementById("select_diff").value;
		if (!underTestClassName || !typeRobot || !difficulty) {
			console.error("Parametri mancanti per la modalità Sfida.");
			return;
		}
		if (mode === "PartitaSingola") {
			remainingTime = document.getElementById("select_time_limit").value * 60; // Converto il timer da minuti a secondi
		}
	} else if (mode === "Allenamento") {
		typeRobot = "NONE";
		difficulty = "EASY";
		if (!underTestClassName) {
			console.error("Parametri mancanti per la modalità Allenamento.");
			return;
		}
	}

	try {
		let requestData = {
			playerId: playerId,
			typeRobot: typeRobot,
			difficulty: difficulty,
			mode: mode,
			underTestClassName: underTestClassName,
		};

		if (mode === "PartitaSingola") {
			requestData["remainingTime"] = remainingTime;
		}

		console.log("requestData: ", requestData);

		startGameRequest(requestData)
			.then((response) => {
				if (mode === "PartitaSingola")
					timer_remainingTime = remainingTime;

				window.location.href = `/editor?ClassUT=${underTestClassName}&mode=${mode}`;
			})
			.catch((error) => {
				console.error("Errore nell'avvio della partita:", error);
			});
	} catch (error) {
		console.error("Errore durante l'avvio della partita:", error);
	}
}

// Aggiungere un flush del localStorage se necessario

// ------------------------------
// EVENTI DELL'INTERFACCIA
// ------------------------------

let restart_game = false;

//Carica partita precedente o nuova partita
function updateDOMWithPreviousGameData(previousGameObject) {
	if (previousGameObject) {
		console.log("Partita già in corso, riprendo la sessione.");
		document.getElementById("scheda_nuovo").classList.add("d-none");
		document.getElementById("scheda_continua").classList.remove("d-none");
		document.getElementById("gamemode_classeUT").innerText =
			previousGameObject.class_ut || "";
		document.getElementById("gamemode_robot").innerText =
			previousGameObject.type_robot || "";
		document.getElementById("gamemode_difficulty").innerText =
			difficulty[previousGameObject.difficulty] || "";
		document.getElementById("gamemode_modalita").innerText =
			gameMode[previousGameObject.mode] || "";

		if (previousGameObject.mode === "PartitaSingola")
			document.getElementById("gamemode_time_limit").innerText =
				formatTime(previousGameObject.remainingTime) || formatTime(0);
		else
			document.getElementById("gamemode_time_limit").innerText = "Nessun tempo limite"

		const link = document.getElementById("Continua");

		if (previousGameObject.mode === "PartitaSingola")
			link.href = `/editor?ClassUT=${previousGameObject.class_ut}&mode=${previousGameObject.mode}&remainingTime=${previousGameObject.remainingTime}`;
		else
			link.href = `/editor?ClassUT=${previousGameObject.class_ut}&mode=${previousGameObject.mode}`;

	} else {
		console.log(
			"Nessuna partita in corso, pronta per avviare una nuova partita."
		);
		document.getElementById("scheda_nuovo").classList.remove("d-none");
		document.getElementById("scheda_continua").classList.add("d-none");
	}
}

//Evento
document.addEventListener("DOMContentLoaded", async function () {
	SetMode(false);
	let previousGameObject = null;
	try {
		previousGameObject = await fetchPreviousGameData();
		console.log("Oggetto partita precedente:", previousGameObject);
	} catch (error) {
		console.error("Errore durante il recupero dei dati del gioco:", error);
	}
	updateDOMWithPreviousGameData(previousGameObject);
});

document
	.getElementById("new_game")
	.addEventListener("click", async function () {
		toggleVisibility("scheda_nuovo");
		toggleVisibility("scheda_continua");
		toggleVisibility("alert_nuova");
		restart_game = true;
	});

document
	.getElementById("link_editor")
	.addEventListener("click", async function () {
		if(restart_game){
			await deleteModalita(GetMode());
		}
		await startGame();
		restart_game = false;
	});

// Aggiorna lo stato del pulsante in base ai selettori del form
function updateButtonState() {
	const submitButton = document.getElementById("link_editor");
	const mode = GetMode();
	const allSelected =
		document.getElementById("select_class").value &&
		document.getElementById("select_robot").value &&
		document.getElementById("select_diff").value;
	const classSelected = document.getElementById("select_class").value;
	submitButton.classList.toggle(
		"disabled",
		mode !== "Allenamento" ? !allSelected : !classSelected
	);
}

document
	.getElementById("select_class")
	.addEventListener("change", updateButtonState);
document
	.getElementById("select_robot")
	.addEventListener("change", updateButtonState);
document
	.getElementById("select_diff")
	.addEventListener("change", updateButtonState);

function toggleVisibility(elementId) {
	const element = document.getElementById(elementId);
	if (element) {
		element.classList.toggle("d-none");
	} else {
		console.error("Elemento non trovato con ID:", elementId);
	}
}

// Filtro i robot disponibili in base alla classeUT scelta
document.addEventListener("DOMContentLoaded", function () {
	const selectClass = document.getElementById("select_class");
	const selectRobot = document.getElementById("select_robot");
	const selectDifficulty = document.getElementById("select_diff");

	selectClass.addEventListener("change", function () {
		const selectedClass = selectClass.value;

		selectRobot.disabled = true;
		selectDifficulty.disabled = true;

		// Rimuovo i robot disponibili associati alla precedente classeUT scelta
		Array.from(selectRobot.options)
			.slice(1) // Mantengo la prima opzione ("Seleziona un'opzione")
			.forEach((option) => option.remove());

		// Rimuovo le difficoltà disponibili associate alla precedente classeUT scelta e al precedente robot scelto
		Array.from(selectDifficulty.options)
			.slice(1) // Mantengo la prima opzione ("Seleziona un'opzione")
			.forEach((option) => option.remove());

		selectRobot.innerHTML =
			document.getElementById("select_robot").children[0].outerHTML;
		selectRobot.disabled = true;

		if (selectedClass) {
			const filteredRobots = availableRobots
				.filter((robot) => robot.classUT === selectedClass)
				.reduce((unique, robot) => {
					if (!unique.includes(robot.opponentType)) {
						unique.push(robot.opponentType);
					}
					return unique;
				}, []);

			filteredRobots.forEach((robot) => {
				const option = document.createElement("option");
				option.value = robot;
				option.textContent = robot;
				selectRobot.appendChild(option);
			});

			selectRobot.disabled = filteredRobots.length === 0;
		}
	});
});

// Filtro le difficoltà disponibili in base alla classeUT e al robot scelti
document.addEventListener("DOMContentLoaded", function () {
	const selectClass = document.getElementById("select_class");
	const selectRobot = document.getElementById("select_robot");
	const selectDifficulty = document.getElementById("select_diff");

	selectRobot.addEventListener("change", function () {
		const selectedClass = selectClass.value;
		const selectedRobot = selectRobot.value;

		selectDifficulty.disabled = true;

		// Rimuovo le difficoltà disponibili associate alla precedente classeUT scelta e al precedente robot scelto
		Array.from(selectDifficulty.options)
			.slice(1) // Mantengo la prima opzione ("Seleziona un'opzione")
			.forEach((option) => option.remove());

		selectDifficulty.innerHTML =
			document.getElementById("select_robot").children[0].outerHTML;
		selectDifficulty.disabled = true;

		if (selectedClass && selectedRobot) {
			const filteredDifficulties = availableRobots
				.filter(
					(robot) =>
						robot.classUT === selectedClass &&
						robot.opponentType === selectedRobot
				)
				.map((robot) => robot.opponentDifficulty);

			const difficultyOptions =
				document.getElementById("difficulty_options").children;

			for (let option of difficultyOptions) {
				if (filteredDifficulties.includes(option.value)) {
					selectDifficulty.appendChild(option.cloneNode(true));
				}
			}

			selectDifficulty.disabled = filteredDifficulties.length === 0;
		}
	});
});
