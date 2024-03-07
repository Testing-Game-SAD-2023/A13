
var turno = 0; // numero di turni giocati fino ad ora
var perc_robot = '0'; // percentuale di copertura del robot scelto

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

const parseJwt = (token) => {
  try {
      return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
      return null;
  }
};

var sidebarEditor = CodeMirror.fromTextArea(document.getElementById("sidebar-textarea"), {
  mode: "text/x-java",
  indentWithTabs:true,
  lineWrapping:true,
  theme: "lucario",
  lineNumbers: true,
  smartIndent: true,
  electricChars: true,
  readOnly: true,
  autoCloseBrackets: true,
  matchBrackets: true,
  disableInput:true,
  autofocus: true
});
var consoleArea = CodeMirror.fromTextArea(document.getElementById("console-textarea"), {
  mode: "text/x-java",
  lineWrapping:true,
  theme: "lucario",
  electricChars: true,
  readOnly: true,
  disableInput:true,
  autofocus: true
});
var consoleArea2 = CodeMirror.fromTextArea(document.getElementById("console-textarea2"), {
  mode: "javascript",
  lineWrapping: true,
  theme: "lucario",
  electricChars: true,
  readOnly: true,
  disableInput: true,
  autofocus: true
});
var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
  mode: "text/x-java",
  indentWithTabs: true,
  smartIndent: true,
  lineWrapping: true,
  theme: "lucario",
  lineNumbers: true,
  autoMatchBrackets: true,
  autoCloseBrackets: true,
  autofocus: true,
  foldGutter: true,
  gutters: ['CodeMirror-foldgutter']
});

  editor.on('click', function(cm, event) {
    var gutterWidth = cm.getGutterElement().offsetWidth;
    var gutterClick = event.clientX - gutterWidth;
    if (gutterClick >= (gutterWidth - 10)) {
      var gutterHeight = cm.defaultTextHeight() * cm.lineCount();
      var line = cm.lineAtHeight(event.clientY + cm.getScrollInfo().top, "client");
      var linePos = cm.charCoords({ line: line, ch: 0 }, "local").y;
      var arrowPos = linePos + cm.defaultTextHeight() / 2;
      var arrowSize = cm.defaultCharWidth();
      if (event.clientX >= gutterWidth && event.clientX <= gutterWidth + arrowSize &&
          event.clientY >= arrowPos - arrowSize && event.clientY <= arrowPos + arrowSize) {
        cm.foldCode(line);
      }
    }
  });

  var startReplaceButtons = document.getElementsByClassName("startReplace");
  for (var i = 0; i < startReplaceButtons.length; i++) {
    startReplaceButtons[i].addEventListener("click", function() {
      var replaceTermInput = document.getElementById("replaceTerm");
      var nuovoTesto = replaceTermInput.value;
      if (nuovoTesto) {
        var testoSelezionato = editor.getSelection();
        if (testoSelezionato) {
          var cursor = editor.getCursor();
          var codice = editor.getValue();
          var nuovoCodice = codice.replaceAll(testoSelezionato, nuovoTesto);
          editor.setValue(nuovoCodice);
          editor.setCursor(cursor);
        }
      }
      closeReplaceModal();
    });
  }

  //ricezione di CUT e game info
$(document).ready(function() {
  var idUtente = parseJwt(getCookie("jwt")).userId;
  var idPartita = localStorage.getItem("gameId");
  var idTurno = localStorage.getItem("roundId");
  var nameCUT= localStorage.getItem("classe");
  var robotScelto = localStorage.getItem("robot");
  var difficolta = localStorage.getItem("difficulty");

  if(idUtente == null || idPartita == null || idTurno == null || nameCUT == null || robotScelto == null) window.location.href = "/main";

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
    success: function(response) {
      // Ricezione avvenuta con successo
      console.log(response);     
      // Associa le informazioni ricevute ai campi della finestra modale
      var idUtenteElement = document.createElement("p");
      idUtenteElement.textContent = "UserID: " + idUtente;
      modal2Content.appendChild(idUtenteElement);

      var idPartitaElement = document.createElement("p");
      idPartitaElement.textContent = "GameID: " + idPartita;
      modal2Content.appendChild(idPartitaElement);

      var idTurnoElement = document.createElement("p");
      idTurnoElement.textContent = "Turno: " + idTurno;
      modal2Content.appendChild(idTurnoElement);

      var robotSceltoElement = document.createElement("p");
      robotSceltoElement.textContent = "Robot: " + robotScelto;
      modal2Content.appendChild(robotSceltoElement);

      var difficoltaElement = document.createElement("p");
      difficoltaElement.textContent = "Livello: " + difficolta;
      modal2Content.appendChild(difficoltaElement);
      // Aggiorna il contenuto dell'editor laterale con il contenuto ricevuto
      console.log(JSON.parse(response).class);
      sidebarEditor.setValue(JSON.parse(response).class);
    },    
    error: function() {
      // Gestione dell'errore
      console.log("Errore durante la ricezione del file ClassUnderTest.java");
    }
  });
});

//compilazione verso T7
  function getEditorContent() {
    return editor.getValue(); // Restituisce il contenuto del codice nell'editor
  }
  function getCUTContent() {
    return sidebarEditor.getValue(); // Restituisce il contenuto del codice nella CUT
  }

  //funzione handler del tasto di logout
  var logout = document.getElementById("logout");
  logout.addEventListener("click", function(){
    if(confirm("Sei sicuro di voler effettuare il logout?")){
      $.ajax({
        url: '/logout',
        method: 'GET',
        success: function (data, textStatus, xhr) {
          window.location.href = "/login";
        },
        error: function (xhr, textStatus, error) {
          console.error('Error:', textStatus, error);
        }
      });
      
    }
  });

  //funzione handler del tasto di storico
  var storico = document.getElementById("storico");
  storico.addEventListener("click", function(){
  document.getElementById('loading-editor').style.display = 'block';
    if(turno == 0){
      async function valRob() {
        try {
          let params = {
            testClassId: localStorage.getItem("classe"),
            type: localStorage.getItem("robot"),
            difficulty: localStorage.getItem("difficulty"),
          };
      
          // Utilizzando $.ajax di jQuery:
          let data = await $.ajax({
            url: "/robots",
            method: 'GET',
            data: params,
            dataType: 'json',
          });
      
          perc_robot = data.scores;
        } catch (error) {
          console.error('Error:', error);
        }
      
        document.getElementById('loading-editor').style.display = 'none';
        alert("Non esiste ancora uno storico dei test\nPercentuale di copertura da battere: " + perc_robot);
      }
      

      valRob();
    }
    else{
      document.getElementById('loading-editor').style.display = 'none';
      var dastampare = "Percentuale di copertura da battere: " + perc_robot + "\n";
      var testi = "\n";

      async function fetchTurns(turno) {
        for (var i = 1; i <= turno; i++) {
          let val;
          if (localStorage.getItem("gameId") === "null") {
            val = parseInt(localStorage.getItem("turnId")) - turno + i;
          } else {
            val = parseInt(localStorage.getItem("turnId")) - turno + (i - 1);
          }
      
          try {
            let response = await $.ajax({
              url: "/turns/" + val.toString(),
              method: 'GET',
              dataType: 'json',
            });
      
            if (response.isWinner) {
              dastampare += "Tentativo " + i.toString() + ": Vittoria\n" + "Percentuale di copertura ottenuta: " + response.scores + "\n";
            } else {
              dastampare += "Tentativo " + i.toString() + ": Sconfitta\n" + "Percentuale di copertura ottenuta: " + response.scores + "\n";
            }
            console.log(response.scores);
          } catch (error) {
            console.error('Error:', error);
          }
      
          try {
            let response = await $.ajax({
              url: "/tests/" + val.toString() + '/Test' + localStorage.getItem("classe"),
              method: 'GET',
              dataType: 'text',
            });
      
            testi += "Codice di test sottoposto al tentativo " + i.toString() + ":\n" + response + "\n\n";
          } catch (error) {
            console.error('Error:', error);
          }
        }
      
        consoleArea.setValue(dastampare + testi);
        console.log(dastampare);
      }
      

      fetchTurns(turno);
    }
  });

  // Ottieni il riferimento al pulsante compilando l'ID
  var compileButton = document.getElementById("compileButton");

  // Aggiungi un gestore di eventi al pulsante per l'evento "click"
  compileButton.addEventListener("click", function() {
    // Logica da eseguire quando il pulsante viene cliccato
    // Ad esempio, esegui una chiamata AJAX al tuo controller Spring per inviare i dati
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
    formData.append("testingClassCode", editor.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe")+".java");
    formData.append("underTestClassCode", sidebarEditor.getValue());

  // Esegui la chiamata AJAX al tuo controller Spring
  // Utilizza la libreria jQuery per semplificare la gestione delle richieste AJAX
  document.getElementById('loading-editor').style.display = 'block';
  $.ajax({
    url: "/api/sendInfo", //URL del controller Spring
    type: "POST",
    data: formData,
    processData: false,
    contentType: false,
    dataType: "text",
    success: function(response) {
      document.getElementById('loading-editor').style.display = 'none';
      // Logica da eseguire in caso di successo
      consoleArea.setValue(response);
      console.log("Richiesta inviata con successo. Risposta del server:", response);
    },
    error: function(xhr, status, error) {
      // Logica da eseguire in caso di errore
      document.getElementById('loading-editor').style.display = 'none';
      alert("Si è verificato un errore!");
      console.log("Errore durante l'invio della richiesta:", error);
    }
  });
});

//esecuzione ricevendo XML da T7 e mandando partita a T4
var runButton = document.getElementById("runButton");
runButton.addEventListener("click", function() {
  //   $(document).ready(function() {
  //   $.ajax({
  //     url: "http://localhost:80/getResultXml",
  //     type: "GET",
  //     dataType: "text",
  //     success: function(xmlContent) {
  //       // Utilizza il contenuto del file XML come desideri
  //       console.log(xmlContent);

  //       // Imposta il contenuto della console CodeMirror con i risultati del file XML
  //       consoleArea2.setValue(xmlContent);
  //     },
  //     error: function() {
  //       // Gestisci l'errore, ad esempio mostra un messaggio di errore
  //       console.log("Errore durante il recupero del file XML.");
  //     }
  //   });
  // });
  document.getElementById('loading-editor').style.display = 'block';
  $(document).ready(function() {
    if(localStorage.getItem("gameId") == "null") { //controllo game invece che turn
      document.getElementById('loading-editor').style.display = 'none';
      alert("Impossibile effettuare un nuovo tentativo: Hai già vinto!");
    } else {
      var risp; // variabile per salvare response
      var formData = new FormData();
      formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
      formData.append("testingClassCode", editor.getValue());
      formData.append("underTestClassName", localStorage.getItem("classe")+".java");
      formData.append("underTestClassCode", sidebarEditor.getValue());

      formData.append("turnId",localStorage.getItem("turnId"));
      formData.append("roundId",localStorage.getItem("roundId"));
      formData.append("gameId",localStorage.getItem("gameId"));
      formData.append("testClassId", localStorage.getItem("classe"));
      formData.append("difficulty", localStorage.getItem("difficulty"));
      formData.append("type", localStorage.getItem("robot")); // modificato
      $.ajax({
        url: "/api/run", // con questa verso il task 6, si salva e conclude la partita e si decreta il vincitore
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function(response) {
          console.log(response);

          risp = response;

          perc_robot = response.robotScore.toString();
        
          consoleArea.setValue(response.outCompile);
          highlightCodeCoverage($.parseXML(response.coverage));
          /*consoleArea2.setValue(`Esito Risultati (percentuale di linee coperte)
Il tuo punteggio: ${response.score.toString()}% LOC
Il punteggio del robot: ${response.robotScore.toString()}% LOC`);*/
          document.getElementById('loading-editor').style.display = 'none';
          alert(response.win == true ? "Hai vinto!" : "Hai perso!");
          //localStorage.clear();
          if(response.win == true){
            turno++;  // incremento il numero di turno giocati fino ad ora
            //localStorage.clear();
            localStorage.setItem("gameId",null); // setto gameId null invece che tutto
            //console.log(localStorage.getItem("gameId"));
          }
          else{
            //localStorage.setItem("gameId",(parseInt(localStorage.getItem("gameId"))+1).toString());
            //localStorage.setItem("roundId",(parseInt(localStorage.getItem("roundId"))+1).toString());
            //localStorage.setItem("turnId",(parseInt(localStorage.getItem("turnId"))+1).toString()); // vedere che fare
            //console.log(localStorage.getItem("turnId"));

            $.ajax({
              url:'/api/save-data',
              data: {
                playerId: parseJwt(getCookie("jwt")).userId,
                classe: localStorage.getItem("classe"),
                robot: localStorage.getItem("robot"),
                difficulty: localStorage.getItem("difficulty")
              },
              type:'POST',
              success: function (response) {
                // Gestisci la risposta del server qui
                localStorage.setItem("gameId", response.game_id);
                localStorage.setItem("turnId", response.turn_id);
                localStorage.setItem("roundId", response.round_id);
                console.log(localStorage.getItem("turnId"));
                turno++; // incremento il numero di turno giocati fino ad ora
                //window.location.href = "/editor";
                
              },
              dataType: "json",
              error: function (error) {
                document.getElementById('loading-editor').style.display = 'none';
                console.error('Errore nell invio dei dati');
                alert("Dati non inviati con successo");
                // Gestisci l'errore qui
              }
            });

          }
          var classe = 'VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/' + localStorage.getItem("classe") + 'SourceCode/' + localStorage.getItem("classe") + '.java';

      var test = '/VolumeT8/FolderTreeEvo/Tests/' + localStorage.getItem("turnId");

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
              var terzoElemento = parseInt(secondRowElements[2]*100);
              var terzoElemento1 = parseInt(terzoRowElements[2]*100);
              var terzoElemento2 = parseInt(quartoRowElements[2]*100);
              var terzoElemento3 = parseInt(quintoRowElements[2]*100);
              var terzoElemento4 = parseInt(sestoRowElements[2]*100);
              var terzoElemento5 = parseInt(settimoRowElements[2]*100);
              var terzoElemento6 = parseInt(ottavoRowElements[2]*100);
              var terzoElemento7 = parseInt(nonoRowElements[2]*100);
      
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
        error: function() {
          document.getElementById('loading-editor').style.display = 'none';
          alert("Si è verificato un errore. Assicurati prima che la compilazione vada a buon fine!");
        }
      });
      
      //Da qui fino a FINE è stato aggiunto
      //aggiungere cose per copertura evosuite
      

        //FINE

    }
  });
});
function getJavaClassContent() {
  // Assume che tu abbia un elemento HTML con un ID univoco che contiene il codice della classe Java
  var javaClassElement = document.getElementById("editor");
  if (javaClassElement) {
    return javaClassElement.value; // Restituisce il valore del campo di input o l'HTML interno dell'elemento
  }else {
    return ""; // Restituisce una stringa vuota se l'elemento non è presente o non ha un valore
  }
}

var fileName;
/* 
var saveButton = document.getElementById("saveButton");
saveButton.addEventListener("click", function() {
  $(document).ready(function() {
    var idUtente = "VALORE_ID_UTENTE";
    var idPartita = "VALORE_ID_PARTITA";
    var idTurno = "VALORE_ID_TURNO";
    var nameCUT= "CLASS_UNDER_TEST";
    var robotScelto = "VALORE_ROBOT_SCELTO";
    var difficolta = "VALORE_DIFFICOLTA";
    var playerTestClass=getJavaClassContent();
    // Chiedi all'utente di inserire il nome del file
    if (!fileName) {
      // Primo click: chiedi all'utente di inserire il nome del file
      fileName = prompt("Inserisci il nome del file");
      if (fileName) {
      // Aggiungi l'estensione .java se non è già presente
        if (!fileName.endsWith(".java")) {
        fileName += ".java";
        }
      }
    }
    // Crea un oggetto FormData per inviare i dati come parte di una richiesta multipart/form-data
    var formData = new FormData();
    formData.append("idUtente", idUtente);
    formData.append("idPartita", idPartita);
    formData.append("idTurno", idTurno);
    formData.append("nameCUT", nameCUT);
    formData.append("robotScelto", robotScelto);
    formData.append("difficolta", difficolta);
    formData.append("file",new Blob([playerTestClass],{type:"text/plain"}),fileName);
    // Effettua la richiesta AJAX per inviare i dati e il file XML
    $.ajax({
      url: "http://localhost:80/inviaDatiEFile",
      type: "POST",
      data: formData,
      processData: false,
      contentType: false,
      success: function(response) {
        // Gestisci la risposta dal servizio destinazione
        console.log(response);
      },
      error: function() {
        // Gestisci l'errore
        console.log("Errore durante l'invio dei dati e del file XML.");
      }
    });
  });
});
*/
//risultati JACOCO da T7
var coverageButton = document.getElementById("coverageButton");
  coverageButton.addEventListener("click", processJaCoCoReport);
  function processJaCoCoReport() {
    // Effettua una richiesta al tuo controller Spring per ottenere il file di output di JaCoCo
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
    formData.append("testingClassCode", editor.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe")+".java");
    formData.append("underTestClassCode", sidebarEditor.getValue());
    document.getElementById('loading-editor').style.display = 'block';
    $.ajax({
      url: "/api/getJaCoCoReport",
      type: "POST",
      data: formData,
      processData: false,
      contentType: false,
      dataType: "xml", //potrebbe essere anche giusto leggere da un file xml di report di JaCoCo
      success: function(reportContent) {
        document.getElementById('loading-editor').style.display = 'none';
        console.log(reportContent);
        // Una volta ricevuto il file di output di JaCoCo, elabora il contenuto
        highlightCodeCoverage(reportContent);
      },
      error: function() {
        document.getElementById('loading-editor').style.display = 'none';
        alert("Si è verificato un errore. Assicurati prima che la compilazione vada a buon fine!");
        console.log("Errore durante il recupero del file di output di JaCoCo.");
      }
    });

    //aggiungere cose per copertura evosuite
    var classe = 'VolumeT8/FolderTreeEvo/' + localStorage.getItem("classe") + '/' + localStorage.getItem("classe") + 'SourceCode/' + localStorage.getItem("classe") + '.java'; //sidebarEditor.getValue(); // Assumendo che rappresenti la classe

    var test = '/VolumeT8/FolderTreeEvo/Tests/' + localStorage.getItem("turnId");

    // Definisci il percorso dell'API
    var apiBaseUrl = '/api/';

    // Concatena il percorso della classe al percorso dell'API
    var url = apiBaseUrl + classe + '+' + test + '+/app';

    const javaCode = editor.getValue(); // codice della classe di test

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
                 
        var reader = new FileReader();
    
        reader.onload = function () {
          var csvContent = reader.result;
    
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
              var terzoElemento = parseInt(secondRowElements[2]*100);
              var terzoElemento1 = parseInt(terzoRowElements[2]*100);
              var terzoElemento2 = parseInt(quartoRowElements[2]*100);
              var terzoElemento3 = parseInt(quintoRowElements[2]*100);
              var terzoElemento4 = parseInt(sestoRowElements[2]*100);
              var terzoElemento5 = parseInt(settimoRowElements[2]*100);
              var terzoElemento6 = parseInt(ottavoRowElements[2]*100);
              var terzoElemento7 = parseInt(nonoRowElements[2]*100);
    
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
          }
        };
    
        reader.readAsText(data);
      },
      error: function (xhr, textStatus, error) {
        console.log('Si è verificato un errore:', error);
      }
    });
    

  }
  function highlightCodeCoverage(reportContent) {
    // Analizza il contenuto del file di output di JaCoCo per individuare le righe coperte, non coperte e parzialmente coperte
    // Applica lo stile appropriato alle righe del tuo editor

    var coveredLines = [];
    var uncoveredLines = [];
    var partiallyCoveredLines = [];

    reportContent.querySelectorAll("line").forEach(function(line) {
      if(line.getAttribute("mi") == 0) coveredLines.push(line.getAttribute("nr"));
      else if(line.getAttribute("cb") / (line.getAttribute("mb") + line.getAttribute("cb")) == (2/4)) partiallyCoveredLines.push(line.getAttribute("nr"));
      else uncoveredLines.push(line.getAttribute("nr"));
    });

    coveredLines.forEach(function(lineNumber) {
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
      sidebarEditor.addLineClass(lineNumber - 2, "background", "covered-line");
    });

    uncoveredLines.forEach(function(lineNumber) {
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
      sidebarEditor.addLineClass(lineNumber - 2, "background", "uncovered-line");
    });

    partiallyCoveredLines.forEach(function(lineNumber) {
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
      sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
      sidebarEditor.addLineClass(lineNumber - 2, "background", "partially-covered-line");
    });
  }
  
var openButton = document.querySelector(".open-button");
openButton.addEventListener("click", function() {
  var fileInput = document.createElement("input");
  fileInput.type = "file";
  fileInput.accept = ".java";

// Funzione di gestione dell'evento di caricamento del file
fileInput.addEventListener("change", function(e) {
  var file = e.target.files[0];
  var reader = new FileReader();

  reader.onload = function(e) {
    var content = e.target.result;
    editor.setValue(content); // Imposta il contenuto del file nell'editor
  };

  reader.readAsText(file); // Leggi il contenuto del file come testo
});

fileInput.click(); // Simula il click sull'input del file
});

// Cambio Tema
var themeButtons = document.getElementsByClassName('theme-button');
var currentTheme = 'lucario'; // Tema iniziale
for (var i = 0; i < themeButtons.length; i++) {
    themeButtons[i].onclick = function() {
      if (currentTheme === 'lucario') {
        sidebarEditor.setOption('theme', 'elegant');
        consoleArea.setOption('theme', 'elegant');
        consoleArea2.setOption('theme', 'elegant');
        editor.setOption('theme', 'elegant');
        currentTheme = 'elegant';
      } else {
        sidebarEditor.setOption('theme', 'lucario');
        consoleArea.setOption('theme', 'lucario');
        consoleArea2.setOption('theme', 'lucario');
        editor.setOption('theme', 'lucario');
        currentTheme = 'lucario';
      }
    };
  }
window.addEventListener('resize', function() {
if (window.innerWidth < 800 || window.innerHeight < 600) {
  window.resizeTo(Math.max(800, window.innerWidth), Math.max(600, window.innerHeight));
}
}); 
document.querySelector('.undo-button').addEventListener('click', function () {
        editor.undo();
});
document.querySelector('.redo-button').addEventListener('click', function () {
        editor.redo();
});
function showName(nome) {
  document.getElementById("nome-pulsante").innerHTML = nome;
}
function hideName() {
  document.getElementById("nome-pulsante").innerHTML = "";
}
function runCoverage(){
  var code=editor.getValue();
  var jacoco=new Jacoco();
  jacoco.setOptions({
    language:"java",
    sourceCode:code
  });
  var report=jacoco.run();
  console.log(report);
}

function openSearchModal() {
  var searchModal = document.getElementById("searchModal");
  searchModal.style.display = "block";
}
function openReplaceModal() {
var replaceModal = document.getElementById("replaceModal");
replaceModal.style.display = "block";
}
function closeReplaceModal() {
  var replaceModal = document.getElementById("replaceModal");
  replaceModal.style.display = "none";
}
function closeSearchModal() {
  var searchModal = document.getElementById("searchModal");
  searchModal.style.display = "none";
}
function cercaParola() {
  var searchTerm = document.getElementById("searchTerm").value;
  var marks = editor.getAllMarks();
      // Rimuovi tutti i segnalibri precedenti
      for (var i = 0; i < marks.length; i++) {
        marks[i].clear();
      }
      
      var cursor = editor.getSearchCursor(searchTerm, null, true);

      // Trova tutte le occorrenze non case sensitive
      while (cursor.findNext()) {
        var startPos = cursor.from();
        var endPos = cursor.to();
        
        // Evidenzia l'occorrenza
        editor.markText(startPos, endPos, { className: "highlight" });
      }
      
      // Scrolla alla posizione della prima occorrenza
      if (editor.getAllMarks().length > 0) {
        editor.scrollIntoView(editor.getAllMarks()[0].find());
      }
      
      // Rimuovi i segnalibri quando l'editor viene modificato
      editor.on("change", function() {
        marks = editor.getAllMarks();
        for (var i = 0; i < marks.length; i++) {
          marks[i].clear();
        }
    });
  closeSearchModal(); // Chiudi la finestra modale dopo la ricerca
}
function saveCode() {
  var code = document.getElementById("editor").value;
  var fileName = prompt("Inserisci il nome del file:", "MyFile.java");
  if (fileName != null && fileName != "") {
    var blob = new Blob([code], {type: "text/plain;charset=utf-8"});
    saveAs(blob, fileName);
  }
}
function saveAs(blob, fileName) {
  var link = document.createElement("a");
  link.download = fileName;
  link.href = URL.createObjectURL(blob);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
function sostituisciTesto(editor) {
  var testoSelezionato = editor.getSelection();
  if (testoSelezionato) {
    var replaceTermInput = document.getElementById("replaceTerm");
    var nuovoTesto = replaceTermInput.value;
    if (nuovoTesto) {
      var cursor = editor.getCursor();
      var codice = editor.getValue();
      var nuovoCodice = codice.replaceAll(testoSelezionato, nuovoTesto);
      editor.setValue(nuovoCodice);
      editor.setCursor(cursor);
    }
  }
}
function autocomplete(cm, options) {
    var startTag = options.startTag || ".";
    var suggestionList = options.suggestionList || [];

    var currentPrefix = "";
    var dropdown = null;

    cm.on("inputRead", function(cm, change) {
        if (change.text.length && change.text[0] === startTag) {
            currentPrefix = "";
            showSuggestions(cm, currentPrefix);
        }
    });

    cm.on("keyup", function(cm, event) {
        var inputValue = cm.getValue();
        var cursor = cm.getCursor();
        var line = cm.getLine(cursor.line);
        var endPos = cursor.ch;
        var startPos = line.lastIndexOf(startTag, endPos - 1);
        if (startPos !== -1) {
            currentPrefix = line.slice(startPos + 1, endPos).toLowerCase();
            showSuggestions(cm, currentPrefix);
        } else {
            closeAllLists();
        }
    });

    function showSuggestions(cm, prefix) {
        var suggestions = suggestionList.filter(function(item) {
            return item.toLowerCase().indexOf(prefix) === 0;
        });

        closeAllLists();

        if (suggestions.length === 0) {
            return;
        }

        dropdown = document.createElement("div");
        dropdown.className = "autocomplete-items";
        dropdown.style.position = "absolute";
        dropdown.style.zIndex = 9999; // Imposta il valore dello z-index per farlo apparire in primo piano
        cm.addWidget(cm.getCursor(), dropdown);

        for (var i = 0; i < suggestions.length; i++) {
            var option = document.createElement("div");
            option.textContent = suggestions[i];
            option.className = "autocomplete-item";
            option.addEventListener("click", function(e) {
                cm.replaceRange(this.textContent.slice(currentPrefix.length), cm.getCursor());
                closeAllLists();
                cm.focus();
            });
            dropdown.appendChild(option);
        }
    }

    function closeAllLists() {
        if (dropdown) {
            dropdown.parentNode.removeChild(dropdown);
            dropdown = null;
        }
    }

    document.addEventListener("click", function(e) {
        closeAllLists();
    });
}
var suggestionList = ["ArrayList", "LinkedList","HashMap","HashSet", "String","Integer","Boolean","Double","Float","Character","Byte",  "Short","Long","Array","List","Set","Map","Queue",  "Stack","TreeSet","TreeMap","PriorityQueue", "Comparator", "Comparable", "Iterator", "Enumeration", "AbstractList", "AbstractSet", "AbstractMap", "AbstractQueue", "AbstractSequentialList", "LinkedListNode", "LinkedListIterator", "LinkedListSpliterator", "LinkedListDescendingIterator", "LinkedListDescendingSpliterator","ArrayListNode", "ArrayListIterator", "ArrayListSpliterator","ArrayListReverseIterator","ArrayListReverseSpliterator","HashSetNode", "HashSetIterator", "HashSetSpliterator", "HashMapEntry","HashMapNode","HashMapIterator", "HashMapSpliterator", "HashMapKeyIterator", "HashMapKeySpliterator", "HashMapValueIterator", "HashMapValueSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNode","TreeSetIterator","TreeSetSpliterator", "TreeSetDescendingIterator","TreeSetDescendingSpliterator","TreeMapEntry", "TreeMapNode",  "TreeMapIterator",  "TreeMapSpliterator",  "TreeMapKeyIterator",  "TreeMapKeySpliterator", "TreeMapValueIterator","TreeMapValueSpliterator", "TreeMapEntryIterator","TreeMapEntrySpliterator",  "PriorityQueueNode", "PriorityQueueIterator","PriorityQueueSpliterator","AbstractCollection", "AbstractQueueIterator","AbstractQueueSpliterator", "AbstractQueueDescendingIterator", "AbstractQueueDescendingSpliterator", "AbstractDeque","LinkedListDeque","LinkedListDequeNode", "LinkedListDequeIterator", "LinkedListDequeSpliterator", "LinkedListDequeDescendingIterator", "LinkedListDequeDescendingSpliterator", "ArrayDeque", "ArrayDequeNode", "ArrayDequeIterator", "ArrayDequeSpliterator", "ArrayDequeDescendingIterator", "ArrayDequeDescendingSpliterator", "StackNode", "StackIterator", "StackSpliterator",   "StackDescendingIterator", "StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator","ArrayListListIterator","ArrayListListSpliterator", "LinkedListListIterator", "LinkedListListSpliterator", "AbstractSetIterator", "AbstractSetSpliterator","HashSetSetIterator","HashSetSetSpliterator","TreeSetSetIterator", "TreeSetSetSpliterator", "AbstractMapIterator", "AbstractMapSpliterator", "HashMapMapIterator", "HashMapMapSpliterator", "TreeMapMapIterator", "TreeMapMapSpliterator","AbstractSequentialListIterator", "AbstractSequentialListSpliterator","LinkedListSequentialListIterator","LinkedListSequentialListSpliterator", "ArrayListSequentialListIterator", "ArrayListSequentialListSpliterator",  "LinkedListNodeIterator", "LinkedListNodeSpliterator", "ArrayListNodeIterator", "ArrayListNodeSpliterator","HashSetNodeIterator", "HashSetNodeSpliterator","HashMapEntryIterator", "HashMapEntrySpliterator","HashSetDescendingIterator","HashSetDescendingSpliterator","TreeSetNodeIterator","TreeSetNodeSpliterator","TreeMapEntryIterator","TreeMapEntrySpliterator", "PriorityQueueNodeIterator", "PriorityQueueNodeSpliterator","AbstractCollectionIterator","AbstractCollectionSpliterator","AbstractQueueIterator","AbstractQueueSpliterator","AbstractQueueDescendingIterator","AbstractQueueDescendingSpliterator","AbstractDequeIterator","AbstractDequeSpliterator","LinkedListDequeIterator","LinkedListDequeSpliterator","LinkedListDequeDescendingIterator","LinkedListDequeDescendingSpliterator","ArrayDequeIterator","ArrayDequeSpliterator","ArrayDequeDescendingIterator","ArrayDequeDescendingSpliterator","StackNodeIterator","StackNodeSpliterator","StackDescendingIterator","StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator","ArrayListListIterator","ArrayListListSpliterator","LinkedListListIterator", "LinkedListListSpliterator","AbstractSetIterator","AbstractSetSpliterator","HashSetSetIterator","HashSetSetSpliterator","TreeSetSetIterator","TreeSetSetSpliterator","AbstractMapIterator","AbstractMapSpliterator","HashMapMapIterator","HashMapMapSpliterator","TreeMapMapIterator","TreeMapMapSpliterator", "AbstractSequentialListIterator","AbstractSequentialListSpliterator","LinkedListSequentialListIterator","LinkedListSequentialListSpliterator","ArrayListSequentialListIterator","ArrayListSequentialListSpliterator","LinkedListNodeIterator","LinkedListNodeSpliterator","ArrayListNodeIterator","ArrayListNodeSpliterator","HashSetNodeIterator","HashSetNodeSpliterator","HashMapEntryIterator","HashMapEntrySpliterator","HashSetDescendingIterator","HashSetDescendingSpliterator","TreeSetNodeIterator","TreeSetNodeSpliterator","TreeMapEntryIterator","TreeMapEntrySpliterator","PriorityQueueNodeIterator" ];
autocomplete(editor, { startTag: ".", suggestionList: suggestionList });
function openInfoModal() {
  var infoModal=document.getElementById("infoModal");
  infoModal.style.display="block";
}
function closeInfoModal(){
  var infoModal=document.getElementById("infoModal");
  infoModal.style.display="none";
}
// Aggiungi i campi e il titolo iniziali alla finestra modale
var modal2Content=document.querySelector("#infoModal .modal2-content");
// Aggiungi il titolo
var titleElement = document.createElement("h2");
titleElement.classList.add("modal2-title");
titleElement.textContent = "GAME INFO";
modal2Content.insertBefore(titleElement, modal2Content.firstChild);
// Aggiungi i campi
var idUtenteElement = document.createElement("p");
var usernamej = parseJwt(getCookie("jwt")).userId;
idUtenteElement.textContent = "UserID: "+usernamej;
modal2Content.appendChild(idUtenteElement);

var idPartitaElement = document.createElement("p");
var gameIDj = localStorage.getItem("gameId");
idPartitaElement.textContent = "GameID: "+gameIDj;
modal2Content.appendChild(idPartitaElement);

var idTurnoElement = document.createElement("p");
var turnoIDj = localStorage.getItem("roundId");
idTurnoElement.textContent = "Turno: " + turnoIDj;
modal2Content.appendChild(idTurnoElement);

var robotSceltoElement = document.createElement("p");
var robotj= localStorage.getItem("robot");
robotSceltoElement.textContent = "Robot:" +robotj;
modal2Content.appendChild(robotSceltoElement);

// var difficoltaElement = document.createElement("p");
// difficoltaElement.textContent = "Livello: 1";
// modal2Content.appendChild(difficoltaElement);

//codice custom per l'integrabilità con thymeleaf
  var robot = "[[${robot}]]";
  var username = "[[${username}]]";
  var gameIDJ = "[[${gameIDj}]]";

