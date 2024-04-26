var turno = 0;                // numero di turni giocati fino ad ora
var orderTurno = 0;
var perc_robot = '0';         // percentuale di copertura del robot scelto
var gameScore = 0;
var locGiocatore = 0;

// (MODIFICA 23/04/2024) Get actual date
var currentDate = new Date();

//Riceeiving the game info from the server
$(document).ready(function () {
  var idUtente = parseJwt(getCookie("jwt")).userId;     
  var idPartita = localStorage.getItem("gameId");       // set by <task x> at the start of the game
  var idTurno = localStorage.getItem("turnId");
  var nameCUT = localStorage.getItem("classe");
  var robotScelto = localStorage.getItem("robot");
  var difficolta = localStorage.getItem("difficulty");

  // (MODIFICA 23/04/2024) Define test class name
  var testClassName = "Test" + nameCUT;

  //Redirect to /main page if some parameters are missing
  if (idUtente == null || idPartita == null || idTurno == null || nameCUT == null || robotScelto == null || difficolta == null) window.location.href = "/main";

  $.ajax({
    url: "/api/receiveClassUnderTest",
    type: "GET",
    data: {
      idUtente: idUtente,
      idPartita: idPartita,
      idTurno: idTurno,
      nomeCUT: nameCUT,
      robotScelto: robotScelto,
      difficolta: difficolta
    },
    dataType: "text",
    success: function (response) {

      // Ricezione avvenuta con successo
      // console.log(response);
      console.log(nameCUT+"SourceCode \n\n\n"+JSON.parse(response).class);
      sidebarEditor.setValue(JSON.parse(response).class);

      //MODIFICA (23/04/2024)  
      //Format date to dd/mm/yyyy
      var formattedDate = currentDate.getDate() + "/" + (currentDate.getMonth() + 1) + "/" + currentDate.getFullYear();
      console.log("formattedDate: "+formattedDate);

      //Obtain the content of the textarea
      var textareaContent = document.getElementById("editor").value;
      
      //Substitute "TestClasse" with the name of the CUT already received
      var newContent = textareaContent.replace("TestClasse", testClassName);

      //Substitute "username" with the username of the player
      newContent = newContent.replace("username", parseJwt(getCookie("jwt")).sub);

      //Substitute "userID" with the ID of the player
      newContent = newContent.replace("userID", parseJwt(getCookie("jwt")).userId);

      //Substitute "date" with the current date
      newContent = newContent.replace("date", formattedDate);

      console.log("newContent \n\n"+newContent);

      //Set the new content to the textarea
      document.getElementById("editor").value = newContent;
      editor.setValue(newContent);
      //FINE MODIFICA (23/04/2024)

      alert("Classe: "+nameCUT+".java ricevuta con successo");
    },
    error: function () {

      // Gestione dell'errore
      console.log("Errore durante la ricezione del file "+ nameCUT+".java");
    }
  });
});

//TASTO STORICO
var storico = document.getElementById("storico");
storico.addEventListener("click", function () {

  document.getElementById('loading-editor').style.display = 'block';
  if (orderTurno == 0) {

    alert("Non esiste ancora uno storico dei test");
    document.getElementById('loading-editor').style.display = 'none';

  } else if (localStorage.getItem("gameId") === "null") {

    alert("Impossibile accedere allo storico. La partita è terminata");
    document.getElementById('loading-editor').style.display = 'none';

  } else {

    document.getElementById('loading-editor').style.display = 'none';
    var dastampare = "";
    async function fetchTurns(turno) {
      for (var i = orderTurno; i >= 1; i--) {

        try {
          let response = await $.ajax({
            url: "/turns/" + (parseInt(localStorage.getItem("turnId")) - i + 1).toString(),
            method: 'GET',
            dataType: 'json',
          });

          dastampare += "Turno " + Math.abs(i - orderTurno - 1).toString() + "\n" + "Percentuale di copertura ottenuta: " + response.scores + "\n";
          console.log("response.scores: "+response.scores);

        } catch (error) {
          console.error('Error:', error);
        }

        try {
          let response = await $.ajax({

            //TODO: Change the path to the correct one
            //e.g. /tests/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game109/Round109/Turn1/TestReport/TestCalcolatrice.java
            //url: "/tests/Game" + localStorage.getItem("gameId") + '/Round' + localStorage.getItem("roundId") + '/Turn' + Math.abs(i - orderTurno - 1).toString() + '/' + localStorage.getItem("classe"),
            url: "/tests/VolumeT8/FolderTreeEvo/" + localStorage.getItem("classe") + "/StudentLogin/Player" + parseJwt(getCookie("jwt")).userId + "/Game" + localStorage.getItem("gameId") + "/Round" + localStorage.getItem("roundId") + "/Turn" + Math.abs(i - orderTurno - 1).toString() + "/TestReport/" + "Test" + localStorage.getItem("classe") + ".java",
            method: 'GET',
            dataType: 'text',
          });

          dastampare += "Codice di test sottoposto al tentativo " + Math.abs(i - orderTurno - 1).toString() + ":\n" + response + "\n\n";

        } catch (error) {
          console.error('Error:', error);
        }
        // Printing separator
        dastampare += "-----------------------------------------------------------------------------\n\n";
      }
      consoleArea.setValue(dastampare);
      console.log(dastampare);
    }
    fetchTurns(orderTurno);
  }
});

// TASTO PLAY/SUBMIT
var runButton = document.getElementById("runButton");
runButton.addEventListener("click", function () {

  document.getElementById('loading-editor').style.display = 'block';
  $(document).ready(function () {

    //Check if the game is over
    if (localStorage.getItem("gameId") == "null") {                     
      document.getElementById('loading-editor').style.display = 'none';
      alert("Impossibile effettuare un nuovo tentativo: La partita è già terminata.");

    } else {

      // var risp;                                                                                    // saving the response
      var formData = new FormData();
      formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");     // e.g TestCalcolatrice.java
      formData.append("testingClassCode", editor.getValue());
      formData.append("underTestClassName", localStorage.getItem("classe") + ".java");            // e.g Calcolatrice.java
      formData.append("underTestClassCode", sidebarEditor.getValue());

      formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
      formData.append("turnId", localStorage.getItem("turnId"));
      formData.append("roundId", localStorage.getItem("roundId"));
      formData.append("gameId", localStorage.getItem("gameId"));
      formData.append("difficulty", localStorage.getItem("difficulty"));
      formData.append("type", localStorage.getItem("robot"));                                     // modificato
      formData.append("order", orderTurno);
      formData.append("username", localStorage.getItem("username"));

      $.ajax({
        url: "/api/run", // con questa verso il task 6, si salva e conclude la partita e si decreta il vincitore
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function (response) {

          console.log(response);
          // risp = response;                            

          perc_robot = response.robotScore.toString();
          gameScore = response.gameScore.toString();

          consoleArea.setValue(response.outCompile);
          highlightCodeCoverage($.parseXML(response.coverage));
          document.getElementById('loading-editor').style.display = 'none';

          if (response.win == true) {

            alert("Hai vinto! Punteggio: " + gameScore);
            turno++;                                      // Increment the number of turns played so far
          }
          else {

            alert("Hai perso! Punteggio: " + gameScore);
            turno++;
          }
          orderTurno++;

          // e.g VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java
          var classe = 'VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + 
                        '/' + localStorage.getItem("classe") + 'SourceCode' +
                        '/' + localStorage.getItem("classe") + '.java';

          //  TODO:  This path is used also from task t8, so if change, check also t8
          // /VolumeT8/FolderTreeEvo/StudentLogin/Player1/Game1/Round1/Turn1/TestReport
          var test = '/VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + 
                      '/StudentLogin'+
                      '/Player' + formData.get("playerId") +
                      '/Game' + localStorage.getItem("gameId") + 
                      '/Round' + localStorage.getItem("roundId") + 
                      '/Turn' + orderTurno + 
                      '/TestReport';

          // added to window.onbeforeunload to remove it only when the window is closed
          // localStorage.setItem("gameId", null); 

          // Definisci il percorso dell'API per il task T8 (prova_esecuzione_parametri4.js)
          var apiBaseUrl = '/api/';

          // Concatena il percorso della classe al percorso dell'API
          // example /api/VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java+/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Game39/Round39/Turn1/TestReport+/app+playerId
          var url = apiBaseUrl + classe + '+' + test + '+/app' + '+' + formData.get("playerId");
          console.log('URL post on: '+url);

          const javaCode = editor.getValue();                               // Code of the test class
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
              console.log(csvContent);

              if (csvContent) {

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
    }
  });
});

// TASTO RUN (COVERAGE)
var coverageButton = document.getElementById("coverageButton");
coverageButton.addEventListener("click", processJaCoCoReport);

function processJaCoCoReport() {

  // Effettua una richiesta al tuo controller Spring per ottenere il file di output di JaCoCo
  var formData = new FormData();
  formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");     // e.g TestCalcolatrice.java
  formData.append("testingClassCode", editor.getValue());
  formData.append("underTestClassName", localStorage.getItem("classe") + ".java");            // e.g Calcolatrice.java
  formData.append("underTestClassCode", sidebarEditor.getValue());
  formData.append("className", localStorage.getItem("classe"));                             // e.g Calcolatrice

  formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
  formData.append("turnId", localStorage.getItem("turnId"));
  formData.append("roundId", localStorage.getItem("roundId"));
  formData.append("gameId", localStorage.getItem("gameId"));


  document.getElementById('loading-editor').style.display = 'block';
  $.ajax({
    url: "/api/getJaCoCoReport",
    type: "POST",
    data: formData,
    processData: false,
    contentType: false,
    dataType: "xml",                                                  //potrebbe essere anche giusto leggere da un file xml di report di JaCoCo
    success: function (reportContent) {

      document.getElementById('loading-editor').style.display = 'none';
      console.log("(POST /getJaCoCoReport)"+ reportContent);
      // Una volta ricevuto il file di output di JaCoCo, elabora il contenuto
      highlightCodeCoverage(reportContent);
      
    },
    error: function () {

      document.getElementById('loading-editor').style.display = 'none';
      alert("Si è verificato un errore. Assicurati prima che la compilazione vada a buon fine!");
      console.log("Errore durante il recupero del file di output di JaCoCo.");
    }
  });
  orderTurno++;

  // e.g VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java
  var classe = 'VolumeT8/FolderTreeEvo/' + formData.get("className") + 
                '/' + formData.get("className") + 'SourceCode' +
                '/' + formData.get("underTestClassName");
  
  // TODO: This path is used also from task t8, so if change, check also t8
  // e.g. /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game1/Round1/Turn1/TestReport
  var test = '/VolumeT8/FolderTreeEvo/' + formData.get("className") + 
              '/StudentLogin'+
              '/Player' + formData.get("playerId") +
              '/Game' + formData.get("gameId") + 
              '/Round' + formData.get("roundId") + 
              '/Turn' + orderTurno + 
              '/TestReport';

  // Definisci il percorso dell'API
  var apiBaseUrl = '/api/';

  // Concatena il percorso della classe al percorso dell'API
  // e.g /api/VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java+/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game39/Round39/Turn1/TestReport+/app
  var url = apiBaseUrl + classe + '+' + test + '+/app' + '+' + formData.get("playerId");

  const javaCode = editor.getValue();                       // code of the test class
  // locGiocatore = 0;

  $.ajax({
    url: url,
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain'
    },
    data: javaCode,
    timeout: 300000,
    processData: false,
    success: function (data, textStatus, xhr) {

      var csvContent = data;
      console.log(csvContent);

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

          locGiocatore = terzoElemento;
          console.log('Terzo elemento della seconda riga:', locGiocatore);

          consoleArea.setValue(`Esito Risultati (percentuale di linee coperte)
    Il tuo punteggio: ${locGiocatore}% LOC
    Informazioni aggiuntive di copertura:
    Il tuo punteggio EvoSuite: ${terzoElemento1}% Branch
    Il tuo punteggio EvoSuite: ${terzoElemento2}% Exception
    Il tuo punteggio EvoSuite: ${terzoElemento3}% WeakMutation
    Il tuo punteggio EvoSuite: ${terzoElemento4}% Output
    Il tuo punteggio EvoSuite: ${terzoElemento5}% Method
    Il tuo punteggio EvoSuite: ${terzoElemento6}% MethodNoException
    Il tuo punteggio EvoSuite: ${terzoElemento7}% CBranch`);
        }
      };

      console.log("Player: " + [parseJwt(getCookie("jwt")).userId]);
      console.log("turnId: " + orderTurno);
      console.log("roundId: " + localStorage.getItem("roundId"));

      // If is the first turn, we need to make a POST request
      if (orderTurno == 1) {

        // ID del turno da aggiornare
        var turnId = localStorage.getItem("turnId");

        // Dati da inviare per l'aggiornamento del turno
        var updateData = {
          scores: locGiocatore.toString(),
          startedAt: new Date().toISOString(),
          closedAt: new Date().toISOString()
        };

        // Effettua la richiesta PUT
        $.ajax({
          url: "/turns/" + turnId,
          type: 'PUT',
          contentType: "application/json",
          data: JSON.stringify(updateData),
          dataType: "json",
          success: function (response) {

            console.log("Turno aggiornato con successo:", response);
            // Esegui azioni aggiuntive dopo l'aggiornamento del turno
          },
          error: function (error) {

            console.error('Errore durante l\'aggiornamento del turno:', error);
            // Gestisci l'errore di aggiornamento del turno
          }
        });
        // altrimenti effettuiamo una post
      } else {

        console.log("orderTurno"+orderTurno);

        $.ajax({
          url: "/turns",
          type: 'POST',
          contentType: "application/json",
          data: JSON.stringify({

            "players": [String(parseJwt(getCookie("jwt")).userId)],
            "order": orderTurno,
            "roundId": parseInt(localStorage.getItem("roundId")),
            "scores": locGiocatore.toString(),
            "startedAt": new Date().toISOString(),
            "closedAt": new Date().toISOString()                    // Get current date and time in ISO 8601 format
          }),
          dataType: "json",
          success: function (response) {

            console.log("(POST /turns) Success:", response);
            localStorage.setItem("turnId", response[0].id.toString());
            turno++;                                              // incremento il numero di turno giocati fino ad ora
            // window.location.href = "/editor";
          },
          error: function (error) {

            console.error('(POST /turns) Error:', error);
            alert("Dati non inviati con successo. Riprova");
          }
        });
      }
    },
    error: function (xhr, textStatus, error) {

      console.log('Si è verificato un errore:', error);
    }
  });
}

// GAME INFO BUTTON
function openInfoModal() {

  // Open the modal
  var infoModal = document.getElementById("infoModal");

  /* Set the display property of the modal to "block" to make it visible
  if it was previously hidden
  */
  infoModal.style.display = "block";

  //Get a reference to the modal2-content element
  var modal2Content = document.querySelector("#infoModal .modal2-content");

  //Retrieve the gameID from the local storage
  var gameIDj = localStorage.getItem("gameId");

  // Seleziona tutti gli elementi figli tranne il primo (che è il pulsante per chiudere il modale)
  var childrenToRemove = Array.from(modal2Content.children).slice(1);

  // Rimuovi tutti gli elementi figli eccetto il primo
  childrenToRemove.forEach(child => {
    modal2Content.removeChild(child);
  });

  // Aggiungi il titolo
  var titleElement = document.createElement("h2");
  titleElement.classList.add("modal2-title");
  titleElement.textContent = "GAME INFO";
  modal2Content.appendChild(titleElement);

  if (gameIDj != "null") {

    // Aggiungi i campi aggiornati
    var idUtenteElement = document.createElement("p");
    var usernamej = parseJwt(getCookie("jwt")).userId;
    idUtenteElement.textContent = "UserID: " + usernamej;
    modal2Content.appendChild(idUtenteElement);
    
    var usernameElement = document.createElement("p");
    var username = parseJwt(getCookie("jwt")).sub.toString();
    usernameElement.textContent = "Username: " + username;
    modal2Content.appendChild(usernameElement);

    var idPartitaElement = document.createElement("p");
    idPartitaElement.textContent = "GameID: " + gameIDj;
    modal2Content.appendChild(idPartitaElement);

    // (MODIFICA 23/04/2024): Add the name of the CUT
    var CUTName = document.createElement("p");
    CUTName.textContent = "Class Under Test: " + localStorage.getItem("classe");
    modal2Content.appendChild(CUTName);

    var idTurnoElement = document.createElement("p");
    var turnoIDj = orderTurno + 1;
    idTurnoElement.textContent = "Turno: " + turnoIDj;
    modal2Content.appendChild(idTurnoElement);

    var robotSceltoElement = document.createElement("p");
    var robotj = localStorage.getItem("robot");
    robotSceltoElement.textContent = "Robot:" + robotj;
    modal2Content.appendChild(robotSceltoElement);

    var difficoltaElement = document.createElement("p");
    difficoltaElement.textContent = "Livello:" + localStorage.getItem("difficulty");
    modal2Content.appendChild(difficoltaElement);
  }
}
function closeInfoModal() {
  var infoModal = document.getElementById("infoModal");
  infoModal.style.display = "none";
}

window.onbeforeunload = function() {
  localStorage.setItem("gameId", null);
  localStorage.setItem("turnId", null);
  localStorage.setItem("classe", null);
  localStorage.setItem("robot", null);
  localStorage.setItem("difficulty", null);
}

//codice custom per l'integrabilità con thymeleaf
var robot = "[[${robot}]]";
var username = "[[${username}]]";
var gameIDJ = "[[${gameIDj}]]";

