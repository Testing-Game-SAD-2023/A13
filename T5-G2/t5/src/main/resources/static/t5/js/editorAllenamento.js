var perc_robot = '0'; // percentuale di copertura del robot scelto
var testCounter = 0

var currentDate = new Date();

//ricezione di CUT
$(document).ready(function () {
    var idUtente = parseJwt(getCookie("jwt")).userId;
    var nameCUT = localStorage.getItem("classe");
    var robotScelto = localStorage.getItem("robot");
    var difficolta = localStorage.getItem("difficulty");
    var testClassName = "Test" + nameCUT;

    if (idUtente == null || nameCUT == null || robotScelto == null) window.location.href = "/main";

    $.ajax({
        url: "/api/receiveClassUnderTest",
        type: "GET",
        data: {
            idUtente: idUtente,
            nomeCUT: nameCUT,
            robotScelto: robotScelto,
            difficolta: difficolta
        },
        dataType: "text",
        success: function (response) {
           
            console.log(nameCUT+"SourceCode \n\n"+JSON.parse(response).class);
            sidebarEditor.setValue(JSON.parse(response).class);

            //Obtain the content of the textarea
            var textareaContent = document.getElementById("editor").value;
      
            //Substitute "TestClasse" with the name of the CUT already received
            var newContent = textareaContent.replace("TestClasse", testClassName);

            //Set the new content to the textarea
            document.getElementById("editor").value = newContent;
            editor.setValue(newContent);
            
            alert("Classe "+nameCUT+ " ricevuta con successo");
        },
        error: function () {
            // Gestione dell'errore
            console.log("Errore durante la ricezione del file: "+nameCUT+".java");
        }
    });
});

// TASTO STORICO
var storico = document.getElementById("storico");
storico.addEventListener("click", function () {
    document.getElementById('loading-editor').style.display = 'block';

    if (testCounter == 0) {
        alert("Non esiste ancora uno storico dei test");
        document.getElementById('loading-editor').style.display = 'none';
    } else {
        document.getElementById('loading-editor').style.display = 'none';
        var dastampare = "";
        async function fetchTurns(turno) {
            for (var i = turno; i >= 1; i--) {

                try {
                    let response = await $.ajax({
                        url: "/tests/VolumeT8/FolderTreeEvo/" + localStorage.getItem("classe") + "/StudentLogin/Player" + parseJwt(getCookie("jwt")).userId + "/Allenamento/Test" + Math.abs(i - turno - 1) + "/TestReport/" + "Test" + localStorage.getItem("classe") + ".java",
                        method: 'GET',
                        dataType: 'text',
                    });

                    dastampare += "Codice di test sottoposto al tentativo " + Math.abs(i - turno - 1).toString() + ":\n" + response + "\n\n";

                } catch (error) {
                    console.error('Error:', error);
                }

                // Printing separator
                dastampare += "-----------------------------------------------------------------------------\n\n";
            }

            consoleArea.setValue(dastampare);
            console.log(dastampare);
        }

        fetchTurns(testCounter);
    }

});

// TASTO RUN ALLENAMENTO
var runButton = document.getElementById("allenamentoButton");
runButton.addEventListener("click", function () {

    // Mostra il messaggio di caricamento
    $('#loading-editor').css('display', 'block');

    //var risp; // variabile per salvare response
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java"); //e.g. TestCalcolatrice.java
    formData.append("testingClassCode", editor.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe") + ".java");        //e.g. Calcolatrice.java
    formData.append("underTestClassCode", sidebarEditor.getValue());
    formData.append("className", localStorage.getItem("classe"));                           //e.g. Calcolatrice

    formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
    formData.append("testClassId", localStorage.getItem("classe"));
    formData.append("difficulty", localStorage.getItem("difficulty"));
    formData.append("type", localStorage.getItem("robot")); // modificato

    $.ajax({
        url: "/api/allenamento",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function (response) {

            console.log("(/api/allenamento) response: "+response);
            //risp = response;

            perc_robot = response.robotScore.toString();

            consoleArea.setValue(response.outCompile);
            highlightCodeCoverage($.parseXML(response.coverage));
            document.getElementById('loading-editor').style.display = 'none';

            testCounter++;

            //Under test class path example: VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java
            var classe = 'VolumeT8/FolderTreeEvo/' + formData.get("className") +
                          '/' + formData.get("className") + 'SourceCode' +
                          '/' + formData.get("underTestClassName");
            
            //Test path training example: /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Allenamento/Test1/TestReport
            var test = '/VolumeT8/FolderTreeEvo/' + formData.get("className") +
                        '/StudentLogin' +
                        '/Player' + formData.get("playerId") +
                        '/Allenamento' +
                        '/Test' + testCounter +
                        '/TestReport';

            // Definisci il percorso dell'API
            var apiBaseUrl = '/api/';

            // Concatena il percorso della classe al percorso dell'API
            var url = apiBaseUrl + classe + '+' + test + '+/app' + '+' + formData.get("playerId");
            console.log("(/allenamento) URL post on: " + url);

            const javaCode = editor.getValue();                                 // codice della classe di test
            document.getElementById('loading-result').style.display = 'block';
            $.ajax({
                url: url,
                type: 'POST',
                data: javaCode,
                timeout: 3000000,
                processData: false,
                contentType: false,
                success: function (data, textStatus, xhr) {

                    var csvContent = data;
                    console.log("(/allenamento) csvContent: \n"+csvContent);

                    // Dividi il contenuto CSV in righe separate
                    var lines = csvContent.split('\n');
                    if (lines.length > 1) {

                        // Dividi la seconda riga in elementi separati da virgole
                        var secondRowElements = lines[1].split(',');
                        var terzoRowElements = lines[2].split(',');
                        var quartoRowElements = lines[3].split(',');
                        var quintoRowElements = lines[4].split(',');
                        var sestoRowElements = lines[5].split(',');
                        var settimoRowElements = lines[6].split(',');
                        var ottavoRowElements = lines[7].split(',');
                        var nonoRowElements = lines[8].split(',');

                        if (secondRowElements.length > 2) {
                            // Estrai il terzo elemento (indice 2) della seconda riga
                            var terzoElemento = parseInt(secondRowElements[2] * 100);
                            var terzoElemento1 = parseInt(terzoRowElements[2] * 100);
                            var terzoElemento2 = parseInt(quartoRowElements[2] * 100);
                            var terzoElemento3 = parseInt(quintoRowElements[2] * 100);
                            var terzoElemento4 = parseInt(sestoRowElements[2] * 100);
                            var terzoElemento5 = parseInt(settimoRowElements[2] * 100);
                            var terzoElemento6 = parseInt(ottavoRowElements[2] * 100);
                            var terzoElemento7 = parseInt(nonoRowElements[2] * 100);

                            document.getElementById('loading-result').style.display = 'none';
                            console.log('Terzo elemento della seconda riga:', terzoElemento);
                            consoleArea2.setValue(`Esito Risultati (percentuale di linee coperte)
                Il tuo punteggio EvoSuite: ${terzoElemento}% LOC
                Il tuo punteggio Jacoco: ${response.score.toString()}% LOC
                Il punteggio del robot: ${response.robotScore.toString()}% LOC
                Informazioni aggiuntive di copertura:
                Il tuo punteggio EvoSuite: ${terzoElemento1}% Branch
                Il tuo punteggio EvoSuite: ${terzoElemento2}% Exception
                Il tuo punteggio EvoSuite: ${terzoElemento3}% WeakMutation
                Il tuo punteggio EvoSuite: ${terzoElemento4}% Output
                Il tuo punteggio EvoSuite: ${terzoElemento5}% Method
                Il tuo punteggio EvoSuite: ${terzoElemento6}% MethodNoException
                Il tuo punteggio EvoSuite: ${terzoElemento7}% CBranch`);
                        }
                    }
                },
                error: function (xhr, textStatus, error) {
                    document.getElementById('loading-editor').style.display = 'none';
                    console.log('Si è verificato un errore:', error);
                }
            });

        },
        error: function () {
            document.getElementById('loading-editor').style.display = 'none';
            alert("Si è verificato un errore. Assicurati prima che la compilazione vada a buon fine!");
        }
    });


});

// Funzione per rimuovere la cartella Allenamento
// function removeAllenamentoFolders() {
//     var url = "/remove-allenamento";

//     $.ajax({
//         url: url,
//         type: 'GET',
//         timeout: 30000,
//         success: function (data, textStatus, xhr) {
//             console.log("Cartelle di allenamento rimosse con successo");
//         },
//         error: function (xhr, textStatus, errorThrown) {
//             console.error("Errore durante la rimozione delle cartelle di allenamento:", errorThrown);
//         }
//     });
// }

// Aggiungi un gestore per l'evento di chiusura della pagina
window.addEventListener('beforeunload', function (e) {
    let className = localStorage.getItem("classe");
    let userId = parseJwt(getCookie("jwt")).userId;
    removeAllenamentoFolders(className, userId);
});


