// Funzione per creare l'URL dell'API
function createApiUrl(formData, orderTurno) {
	const className = formData.get("className");
	const underTestClassName = formData.get("underTestClassName");
	const playerId = formData.get("playerId");

	// Costruisce il percorso per la classe
	const classePath = `VolumeT8/FolderTreeEvo/${className}/${className}SourceCode/${underTestClassName}`;

	// Ottiene il percorso del test generato
	const testPath = generaPercorsoTest(orderTurno, formData);

	// Costruisce l'URL dell'API
	const apiUrl = `/api/${classePath}+${testPath}+/app+${playerId}`;

	return apiUrl;
}

// Funzione per generare il percorso del test
function generaPercorsoTest(orderTurno, formData) {
	const modalita = localStorage.getItem("modalita");
	const playerId = formData.get("playerId");
	const gameId = formData.get("gameId");
	const roundId = formData.get("roundId");
	const classeLocal = formData.get("className");

	// Verifica la modalità e costruisce il percorso appropriato
	if (modalita === "Scalata" || modalita === "Sfida") {
		const scalataPart =
			modalita === "Scalata"
				? `/${localStorage.getItem("SelectedScalata")}${localStorage.getItem(
						"scalataId"
				  )}`
				: "";

		return `/VolumeT8/FolderTreeEvo/StudentLogin/Player${playerId}/${modalita}${scalataPart}/${classeLocal}/Game${gameId}/Round${roundId}/Turn${orderTurno}/TestReport`;
	} else {
		console.error("Errore: modalità non trovata");
		window.location.href = "/main";
		return null;
	}
}

// Funzione per estrarre la terza colonna di un CSV
function extractThirdColumn(csvContent) {
	const rows = csvContent.split("\n"); // Divide le righe
	const thirdColumnValues = [];

	// Inizia il ciclo dalla seconda riga (indice 1)
	rows.slice(1).forEach((row) => {
		const cells = row.split(","); // Divide le celle
		if (cells.length >= 3) {
			thirdColumnValues.push(cells[2]); // Aggiunge la terza colonna
		}
	});

	return thirdColumnValues;
}

function getConsoleTextCoverage(data) {
	var valori_csv = extractThirdColumn(data);

	var consoleText = `Esito Risultati (percentuale di linee coperte)
                      Il tuo punteggio: ${valori_csv[0]}% LOC
                      Informazioni aggiuntive di copertura:
                      Il tuo punteggio EvoSuite: ${valori_csv[1]}% Branch
                      Il tuo punteggio EvoSuite: ${valori_csv[2]}% Exception
                      Il tuo punteggio EvoSuite: ${valori_csv[3]}% WeakMutation
                      Il tuo punteggio EvoSuite: ${valori_csv[4]}% Output
                      Il tuo punteggio EvoSuite: ${valori_csv[5]}% Method
                      Il tuo punteggio EvoSuite: ${valori_csv[6]}% MethodNoException
                      Il tuo punteggio EvoSuite: ${valori_csv[7]}% CBranch`;

	// Restituisce il testo generato
	return consoleText;
}

function getConsoleTextRun(data, punteggioJacoco, punteggioRobot) {
	var valori_csv = extractThirdColumn(data);

	var consoleText = `Esito Risultati (percentuale di linee coperte)
                        Il tuo punteggio EvoSuite: ${valori_csv[0]}% LOC
                        Il tuo punteggio Jacoco: ${punteggioJacoco}% LOC
                        Il punteggio del robot: ${punteggioRobot}% LOC
                        Informazioni aggiuntive di copertura:
                        Il tuo punteggio EvoSuite: ${valori_csv[1]}% Branch
                        Il tuo punteggio EvoSuite: ${valori_csv[2]}% Exception
                        Il tuo punteggio EvoSuite: ${valori_csv[3]}% WeakMutation
                        Il tuo punteggio EvoSuite: ${valori_csv[4]}% Output
                        Il tuo punteggio EvoSuite: ${valori_csv[5]}% Method
                        Il tuo punteggio EvoSuite: ${valori_csv[6]}% MethodNoException
                        Il tuo punteggio EvoSuite: ${valori_csv[7]}% CBranch`;

	// Restituisce il testo generato
	return consoleText;
}

// Funzione per avviare il gioco utilizzando ajaxRequest
async function startGame(data) {
    try {
        // Utilizziamo la funzione ajaxRequest per la chiamata POST
        const response = await ajaxRequest("/StartGame", "POST", data, true, "text");

        console.log("Partita iniziata con successo:", response);
    } catch (error) {
        console.error("Errore durante l'avvio della partita:", error);
    }
}

// Funzione principale per la gestione dello storico dei turni
async function fetchTurns() {
	let output = "";

	for (let i = orderTurno; i >= 1; i--) {
		const turnId = (
			parseInt(localStorage.getItem("turnId")) -
			i +
			1
		).toString();
		output += await fetchTurnData(turnId, i);

		const testPath = generaPercorsoTest(orderTurno);
		output += await fetchTestCode(testPath, i);

		// Separatore tra i turni
		output +=
			"-----------------------------------------------------------------------------\n\n";
	}

	// Impostare il risultato nella console
	consoleArea.setValue(output);
	console.log(output);
}

// Funzione per ottenere i dati di un turno
async function fetchTurnData(turnId, i) {
	try {
        const url = `/turns/${turnId}`;
        
        // Utilizziamo la funzione ajaxRequest per eseguire una chiamata GET
        const response = await ajaxRequest(url, "GET", null, false, "json");

        return `Turno ${Math.abs(i - orderTurno - 1)}\nPercentuale di copertura ottenuta: ${response.scores}\n`;
    } catch (error) {
        console.error("Error fetching turn data:", error);
        return "";
    }
}

// Funzione per ottenere il codice di test di un turno utilizzando ajaxRequest
async function fetchTestCode(testPath, i) {
    try {
        // Utilizziamo la funzione ajaxRequest per eseguire una chiamata GET
        const response = await ajaxRequest(testPath, "GET", null, false, "text");

        return `Codice di test sottoposto al tentativo ${Math.abs(
            i - orderTurno - 1
        )}:\n${response}\n\n`;
    } catch (error) {
        console.error("Error fetching test code:", error);
        return "";
    }
}

function toggleLoading(showSpinner, divId, buttonId) {
    const divElement = document.getElementById(divId);
	const button =  document.getElementById(buttonId);

    if (!divElement) {
        console.error(`Elemento con ID "${divId}" non trovato.`);
        return;
    }

    const spinner = divElement.querySelector('.spinner-border');
    const statusText = divElement.querySelector('[role="status"]');
    const icon = divElement.querySelector('i');

    if (showSpinner) {
        spinner.style.display = 'inline-block'; // Mostra lo spinner
        statusText.style.display = 'inline';    // Mostra il testo "Loading..."
        icon.style.display = 'none';             // Nascondi l'icona
		button.disabled = true;
    } else {
        spinner.style.display = 'none';          // Nascondi lo spinner
        statusText.style.display = 'none';       // Nascondi il testo "Loading..."
        icon.style.display = 'inline-block';     // Mostra l'icona
		button.disabled = false;
    }
}

// Funzione per mostrare un messaggio di alert e nascondere il caricamento
function showAlert(message) {
	alert(message);
	toggleLoading(false);
}

function highlightCodeCoverage(reportContent) {
    // Analizza il contenuto del file di output di JaCoCo per individuare le righe coperte, non coperte e parzialmente coperte
    // Applica lo stile appropriato alle righe del tuo editor

    var coveredLines = [];
    var uncoveredLines = [];
    var partiallyCoveredLines = [];

    reportContent.querySelectorAll("line").forEach(function (line) {
        if (line.getAttribute("mi") == 0) coveredLines.push(line.getAttribute("nr"));
        else if (line.getAttribute("cb") / (line.getAttribute("mb") + line.getAttribute("cb")) == (2 / 4)) partiallyCoveredLines.push(line.getAttribute("nr"));
        else uncoveredLines.push(line.getAttribute("nr"));
    });

    coveredLines.forEach(function (lineNumber) {
        editor_robot.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        editor_robot.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        editor_robot.addLineClass(lineNumber - 2, "background", "covered-line");
    });

    uncoveredLines.forEach(function (lineNumber) {
        editor_robot.removeLineClass(lineNumber - 2, "background", "covered-line");
        editor_robot.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        editor_robot.addLineClass(lineNumber - 2, "background", "uncovered-line");
    });

    partiallyCoveredLines.forEach(function (lineNumber) {
        editor_robot.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        editor_robot.removeLineClass(lineNumber - 2, "background", "covered-line");
        editor_robot.addLineClass(lineNumber - 2, "background", "partially-covered-line");
    });
}

// Funzione per ottenere i dati del form da inviare
function getFormData() {
    const formData = new FormData();
    const className = localStorage.getItem("underTestClassName");
    
    //formData.append("testingClassName", `Test${className}.java`);
    formData.append("testingClassCode", editor_utente.getValue());
    formData.append("underTestClassName", `${className}.java`);
    formData.append("underTestClassCode", editor_robot.getValue());
    formData.append("className", className);
    formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
    formData.append("turnId", localStorage.getItem("turnId"));
    formData.append("difficulty", localStorage.getItem("difficulty"));
    formData.append("type", localStorage.getItem("robot"));
    formData.append("order", orderTurno);
    formData.append("username", localStorage.getItem("username"));
    formData.append("testClassId", className);

    return formData;
}

// Funzione per mostrare messaggi di vittoria o sconfitta
function showGameResult(isWin, gameScore) {
    if (isWin) {
        swal("Complimenti!", `Hai vinto! Ecco il tuo punteggio: ${gameScore}`, "success");
    } else {
        swal("Peccato!", `Hai perso! Ecco il tuo punteggio: ${gameScore}`, "error");
    }
}

async function ajaxRequest(url, method = "POST", data = null, isJson = true, dataType = "json") {
    try {
        const options = {
            url: url,
            type: method,
            dataType: dataType,
            processData: isJson, // Set to true to encode data properly
            contentType: isJson ? "application/x-www-form-urlencoded; charset=UTF-8" : false,
            data: isJson && data ? $.param(data) : data, // Convert data to URL-encoded string
        };

        const response = await $.ajax(options);
        return response;
    } catch (error) {
        console.error("Si è verificato un errore:", error);
        throw error;
    }
}

function controlloScalata(iswin, current_round_scalata, total_rounds_scalata, displayRobotPoints) {
	// Check if the player has won the round
	if (isWin) {
		/*The player has won the round, check if the player has 
        completed the Scalata (current_round_scalata == total_rounds_scalata)
        */
		if (current_round_scalata == total_rounds_scalata) {
			// alert("Hai completato la scalata!");
			calculateFinalScore(localStorage.getItem("scalataId"))
				.then((data) => {
					console.log("calculateFinalScore response: ", data.finalScore);
					closeScalata(
						localStorage.getItem("scalataId"),
						true,
						data.finalScore,
						current_round_scalata
					).then((data) => {
						swal(
							"Complimenti!",
							`Hai completato la scalata!\n${displayRobotPoints}\n A breve verrai reindirizzato alla classifica.`,
							"success"
						).then((value) => {
							window.location.href = "/leaderboardScalata";
						});
					});
				})
				.catch((error) => {
					console.log("Error:", error);
					swal(
						"Errore!",
						"Si è verificato un errore durante il recupero dei dati. Riprovare.",
						"error"
					);
				});
		} else {
			//The player has completed the round, not the Scalata
			swal(
				"Complimenti!",
				`Hai completato il round ${current_round_scalata}/${total_rounds_scalata}!\n${displayRobotPoints}`,
				"success"
			).then((value) => {
				current_round_scalata++;
				localStorage.setItem("current_round_scalata", current_round_scalata);
				classe = getScalataClasse(
					current_round_scalata - 1,
					localStorage.getItem("scalata_classes")
				);
				localStorage.setItem("classe", classe);
				console.log(
					"[editor.js] classes in scalata: " +
						localStorage.getItem("scalata_classes") +
						"\n\
                      selected class: " +
						classe
				);
				incrementScalataRound(
					localStorage.getItem("scalataId"),
					current_round_scalata
				)
					.then((data) => {
						console.log(
							"[editor.js] Creating new game for next round in scalata with parameters: \
                Robot: evosuite\n\
                Classe: " +
								classe +
								"\n\
                Difficulty: 1\n\
                ScalataId: " +
								localStorage.getItem("scalataId") +
								"\n\
                Username: " +
								localStorage.getItem("username") +
								"."
						);
						createGame(
							"evosuite",
							classe,
							1,
							localStorage.getItem("scalataId"),
							localStorage.getItem("username")
						).then((data) => {
							console.log(data);
							window.location.href = "/editor";
						});
					})
					.catch((error) => {
						console.log("Error:", error);
						swal(
							"Errore!",
							"Si è verificato un errore durante il recupero dei dati. Riprovare.",
							"error"
						);
					});
			});
		}
	} else {
		//The player has lost the round
		closeScalata(
			localStorage.getItem("scalataId"),
			false,
			0,
			current_round_scalata
		)
			.then((data) => {
				console.log("Close Scalata response: ", data);
				swal(
					"Peccato!",
					`Hai perso al round ${current_round_scalata}/${total_rounds_scalata} della scalata, la prossima volta andrà meglio!\n${displayRobotPoints}`,
					"error"
				).then((value) => {
					window.location.href = "/main";
				});
			})
			.catch((error) => {
				console.log("Error:", error);
				swal(
					"Errore!",
					"Si è verificato un errore durante il recupero dei dati. Riprovare.",
					"error"
				);
			});
	}
}
