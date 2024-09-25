// Funzione per creare l'URL dell'API
function createApiUrl(formData, orderTurno) {
    const className = formData.get("className");
    const underTestClassName = formData.get("underTestClassName");
    const playerId = formData.get("playerId");

    // Costruisce il percorso per la classe
    const classePath = `VolumeT8/FolderTreeEvo/${className}/${className}SourceCode/${underTestClassName}`;

    // Ottiene il percorso del test generato
    const testPath = generaPercorsoTest(orderTurno);

    // Costruisce l'URL dell'API
    const apiUrl = `/api/${classePath}+${testPath}+/app+${playerId}`;

    return apiUrl;
}

// Funzione per generare il percorso del test
function generaPercorsoTest(orderTurno) {
    const modalita = localStorage.getItem("modalita");
    const playerId = localStorage.getItem("playerId");
    const gameId = localStorage.getItem("gameId");
    const roundId = localStorage.getItem("roundId");
    const classeLocal = localStorage.getItem("classe");

    // Verifica la modalità e costruisce il percorso appropriato
    if (modalita === "Scalata" || modalita === "Sfida") {
        const scalataPart = modalita === "Scalata" 
            ? `/${localStorage.getItem('SelectedScalata')}${localStorage.getItem("scalataId")}` 
            : '';

        return `/VolumeT8/FolderTreeEvo/StudentLogin/Player${playerId}/${modalita}${scalataPart}/${classeLocal}/Game${gameId}/Round${roundId}/Turn${orderTurno}/TestReport`;
    } else {
        console.error("Errore: modalità non trovata");
        window.location.href = "/main";
        return null;
    }
}

// Funzione per estrarre la terza colonna di un CSV
function extractThirdColumn(csvContent) {
    const rows = csvContent.split("\n");  // Divide le righe
    const thirdColumnValues = [];

    rows.forEach((row) => {
        const cells = row.split(",");  // Divide le celle
        if (cells.length >= 3) {
            thirdColumnValues.push(cells[2]);  // Aggiunge la terza colonna
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
                        Il tuo punteggio EvoSuite: ${valori_csv[7]}% CBranch`

    // Restituisce il testo generato
    return consoleText;
}