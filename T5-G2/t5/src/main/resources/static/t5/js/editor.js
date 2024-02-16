var turno = 0; // numero di turni giocati fino ad ora
var orderTurno = 0;
var perc_robot = '0'; // percentuale di copertura del robot scelto
var gameScore = 0;

//ricezione di CUT e game info

$(document).ready(function () {
  var idUtente = parseJwt(getCookie("jwt")).userId;
  var idPartita = localStorage.getItem("gameId");
  var idTurno = localStorage.getItem("turnId");
  var nameCUT = localStorage.getItem("classe");
  var robotScelto = localStorage.getItem("robot");
  var difficolta = localStorage.getItem("difficulty");

  if (idUtente == null || idPartita == null || idTurno == null || nameCUT == null || robotScelto == null) window.location.href = "/main";

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
      console.log(response);
      console.log(JSON.parse(response).class);
      sidebarEditor.setValue(JSON.parse(response).class);
    },
    error: function () {
      // Gestione dell'errore
      console.log("Errore durante la ricezione del file ClassUnderTest.java");
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
          console.log(response.scores);

        } catch (error) {
          console.error('Error:', error);
        }

        try {
          let response = await $.ajax({
            url: "/tests/Game" + localStorage.getItem("gameId") + '/Round' + localStorage.getItem("roundId") + '/Turn' + Math.abs(i - orderTurno - 1).toString() + '/' + localStorage.getItem("classe"),
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
    if (localStorage.getItem("gameId") == "null") { //controllo game invece che turn
      document.getElementById('loading-editor').style.display = 'none';
      alert("Impossibile effettuare un nuovo tentativo: La partita è già terminata.");
    } else {
      var risp; // variabile per salvare response
      var formData = new FormData();
      formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
      formData.append("testingClassCode", editor.getValue());
      formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
      formData.append("underTestClassCode", sidebarEditor.getValue());

      formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
      formData.append("turnId", localStorage.getItem("turnId"));
      formData.append("roundId", localStorage.getItem("roundId"));
      formData.append("gameId", localStorage.getItem("gameId"));
      formData.append("testClassId", localStorage.getItem("classe"));
      formData.append("difficulty", localStorage.getItem("difficulty"));
      formData.append("type", localStorage.getItem("robot")); // modificato
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

          risp = response;

          perc_robot = response.robotScore.toString();
          gameScore = response.gameScore.toString();

          consoleArea.setValue(response.outCompile);
          highlightCodeCoverage($.parseXML(response.coverage));
          document.getElementById('loading-editor').style.display = 'none';

          if (response.win == true) {
            alert("Hai vinto! Punteggio: " + gameScore);
            turno++;  // incremento il numero di turno giocati fino ad ora
          }
          else {
            alert("Hai perso! Punteggio: " + gameScore);
            turno++;
          }

          orderTurno++;

          var classe = 'VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/' + localStorage.getItem("classe") + 'SourceCode/' + localStorage.getItem("classe") + '.java';

          var test = '/VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/StudentLogin/Game' + localStorage.getItem("gameId") + '/Round' + localStorage.getItem("roundId") + '/Turn' + orderTurno + '/TestReport';

          localStorage.setItem("gameId", null);

          // Definisci il percorso dell'API
          var apiBaseUrl = '/api/';

          // Concatena il percorso della classe al percorso dell'API
          var url = apiBaseUrl + classe + '+' + test + '+/app';

          const javaCode = editor.getValue(); // codice della classe di test
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
                Il tuo punteggio Jacoco: ${risp.score.toString()}% LOC
                Il punteggio del robot: ${risp.robotScore.toString()}% LOC
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

    }
  });
});

// TASTO RUN

var coverageButton = document.getElementById("coverageButton");
coverageButton.addEventListener("click", processJaCoCoReport);
function processJaCoCoReport() {
  // Effettua una richiesta al tuo controller Spring per ottenere il file di output di JaCoCo
  var formData = new FormData();
  formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
  formData.append("testingClassCode", editor.getValue());
  formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
  formData.append("underTestClassCode", sidebarEditor.getValue());
  document.getElementById('loading-editor').style.display = 'block';
  $.ajax({
    url: "/api/getJaCoCoReport",
    type: "POST",
    data: formData,
    processData: false,
    contentType: false,
    dataType: "xml", //potrebbe essere anche giusto leggere da un file xml di report di JaCoCo
    success: function (reportContent) {
      document.getElementById('loading-editor').style.display = 'none';
      console.log(reportContent);
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

  //aggiungere cose per copertura evosuite
  var classe = 'VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/' + localStorage.getItem("classe") + 'SourceCode/' + localStorage.getItem("classe") + '.java'; //sidebarEditor.getValue(); // Assumendo che rappresenti la classe

  var test = '/VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/StudentLogin/Game' + localStorage.getItem("gameId") + '/Round' + localStorage.getItem("roundId") + '/Turn' + orderTurno + '/TestReport';

  // Definisci il percorso dell'API
  var apiBaseUrl = '/api/';

  // Concatena il percorso della classe al percorso dell'API
  var url = apiBaseUrl + classe + '+' + test + '+/app';

  const javaCode = editor.getValue(); // codice della classe di test

  locGiocatore = 0;

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
          console.log('Terzo elemento della seconda riga:', terzoElemento);
          consoleArea.setValue(`Esito Risultati (percentuale di linee coperte)
    Il tuo punteggio: ${terzoElemento}% LOC
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
      console.log("turnId: " + localStorage.getItem("turnId"));
      console.log("roundId: " + localStorage.getItem("roundId"));

      // Se è il primo turno giocato allora effettuiamo una put

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

        console.log(orderTurno);

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
            "closedAt": new Date().toISOString()  // Get current date and time in ISO 8601 format
          }),
          dataType: "json",
          success: function (response) {
            console.log("Success:", response);
            localStorage.setItem("turnId", response[0].id.toString());
            localStorage.setItem("orderTurno", response[0].order.toString());
            turno++; // incremento il numero di turno giocati fino ad ora
            // window.location.href = "/editor";
          },
          error: function (error) {
            console.error('Error:', error);
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

// GAME INFO

function openInfoModal() {
  // Apri il modale
  var infoModal = document.getElementById("infoModal");
  infoModal.style.display = "block";

  // Aggiorna il contenuto del modale
  var modal2Content = document.querySelector("#infoModal .modal2-content");
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

    var idPartitaElement = document.createElement("p");
    idPartitaElement.textContent = "GameID: " + gameIDj;
    modal2Content.appendChild(idPartitaElement);

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

//codice custom per l'integrabilità con thymeleaf
var robot = "[[${robot}]]";
var username = "[[${username}]]";
var gameIDJ = "[[${gameIDj}]]";

