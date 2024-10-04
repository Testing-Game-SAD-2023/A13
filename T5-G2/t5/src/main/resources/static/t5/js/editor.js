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

	// Perform replacements
	const newContent = replacePlaceholders(textareaContent, replacements);
	console.log("newContent \n\n", newContent);

	// Set the new content to the textarea
	document.getElementById("Editor_utente").value = newContent;
	editor_utente.setValue(newContent);
});

current_round_scalata = localStorage.getItem("current_round_scalata");
total_rounds_scalata = localStorage.getItem("total_rounds_of_scalata");

//Funzione per aggiungere un elemento allo storico
function addStorico(score, covValue) {
	var list = document.getElementById("storico_list"); // Seleziona la lista

	    // Verifica se esiste un placeholder e lo rimuove al primo inserimento
	var placeholder = document.getElementById("placeholder-item");
	if (placeholder) {
		placeholder.remove();
	}

	// Crea un nuovo elemento <li> con la struttura specificata
	var newItem = document.createElement("li");
	newItem.className =
		"list-group-item d-flex justify-content-between align-items-start";

	// Imposta il contenuto HTML del nuovo elemento
	newItem.innerHTML = `
		<div class="ms-2 me-auto">
			<div class="fw-bold">Punteggio </div>
			<small>%COV: ${covValue}</small>
		</div>
		<span class="badge text-bg-primary rounded-pill">${score}</span>
	`;

	// Aggiunge il nuovo elemento alla lista
	list.appendChild(newItem);
}

// Elemento del pulsante "Play/Submit"
const runButton = document.getElementById("runButton");
// Funzione principale per la gestione del click del pulsante
runButton.addEventListener("click", async function () {
	toggleLoading(true, "loading_run", "runButton");

	try {
		const formData = getFormData();
		formData.append("isGameEnd", false);
		for (let [key, value] of formData.entries()) {
			console.log(key, value);
		}

		// Prima richiesta AJAX per eseguire il test
		const response = await ajaxRequest("/run", "POST", formData, false, "json");
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
		addStorico(userScore, valori_csv[0]);
		displayRobotPoints = getConsoleTextRun(csvContent, 0, robotScore, userScore)
		console_robot.setValue(displayRobotPoints);

		console.log("punteggio robot: "  + robotScore);
		console.log("punteggio utente: " + userScore);

		if (localStorage.getItem("modalita") === "Scalata") {
			console.log("Game mode is 'Scalata'");
			controlloScalata(
				GameOver,
				current_round_scalata,
				total_rounds_scalata,
				displayRobotPoints
			);
		} else {
			console.log("Game mode is 'Sfida'");
		}
	} catch (error) {
		swal(error.message);
	} finally {
		toggleLoading(false, "loading_run", "runButton");
	}
});

// TASTO RUN (COVERAGE)
var coverageButton = document.getElementById("coverageButton");
coverageButton.addEventListener("click", async function () {
	const formData = getFormData();
	toggleLoading(true, "loading_cov", "coverageButton");

	try {
		urlJacoco = "http://remoteccc-app-1:1234/compile-and-codecoverage";
		// Richiesta per ottenere il report di JaCoCo
		const reportContent = await ajaxRequest(
			urlJacoco,
			"POST",
			formData,
			false,
			"xml"
		);
		console.log("(POST /getJaCoCoReport)", reportContent);
		highlightCodeCoverage(reportContent);

		orderTurno++;
		const url = createApiUrl(formData, orderTurno);

		// Richiesta per inviare il codice di test
		const csvContent = await ajaxRequest(url, "POST", formData.get("testingClassCode"), false, "text");
		console_utente.setValue(getConsoleTextCoverage(csvContent));

		const turnId = localStorage.getItem("turnId");
		await updateOrCreateTurn(turnId, locGiocatore, orderTurno);
	} catch (error) {
		alert(
			"Si è verificato un errore. Assicurati prima che la compilazione vada a buon fine!"
		);
		console.error(
			"Errore durante il recupero del file di output di JaCoCo o la gestione del turno:",
			error
		);
	} finally {
		toggleLoading(true, "loading_cov", "coverageButton");
	}
});

Gameoverdow.onbeforeunload = function () {
	if (localStorage.getItem("modalita") !== "Scalata") {
		localStorage.setItem("gameId", null);
		localStorage.setItem("turnId", null);
		localStorage.setItem("classe", null);
		localStorage.setItem("robot", null);
		localStorage.setItem("difficulty", null);
	}
};

//codice custom per l'integrabilità con thymeleaf
var robot = "[[${robot}]]";
var username = "[[${username}]]";
var gameIDJ = "[[${gameIDj}]]";
