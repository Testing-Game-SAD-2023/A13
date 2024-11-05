var sidebarEditor = CodeMirror.fromTextArea(document.getElementById("sidebar-textarea"), {
    mode: "text/x-java",
    indentWithTabs:true,
    lineWrapping:true,
    theme: "lucario",
    lineNumbers: true,
    smartIndent: true,
    electricChars: true,
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
    disableInput:true,
    autofocus: true
  });
  var consoleArea2 = CodeMirror.fromTextArea(document.getElementById("console-textarea2"), {
    mode: "javascript",
    lineWrapping: true,
    theme: "lucario",
    electricChars: true,
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
    var idUtente = "VALORE_ID_UTENTE";
    var idPartita = "VALORE_ID_PARTITA";
    var idTurno = "VALORE_ID_TURNO";
    var nameCUT= "CLASS_UNDER_TEST";
    var robotScelto = "VALORE_ROBOT_SCELTO";
    var difficolta = "VALORE_DIFFICOLTA";

    $.ajax({
      url: "/receiveClassUnderTest",
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
    // Ottieni il riferimento al pulsante compilando l'ID
    var compileButton = document.getElementById("compileButton");

    // Aggiungi un gestore di eventi al pulsante per l'evento "click"
    compileButton.addEventListener("click", function() {
      // Logica da eseguire quando il pulsante viene cliccato
      // Ad esempio, esegui una chiamata AJAX al tuo controller Spring per inviare i dati
      var requestData = {
        testingClassName: "TestClass.java",
        testingClassCode: getEditorContent(), // Assumi che tu abbia una funzione "getEditorContent" per ottenere il contenuto dell'editor
        underTestClassName: nameCUT,
        underTestClassCode: getCUTContent()
      };

    // Esegui la chiamata AJAX al tuo controller Spring
    // Utilizza la libreria jQuery per semplificare la gestione delle richieste AJAX
    $.ajax({
      url: "/sendInfo", //URL del controller Spring
      type: "POST",
      data: JSON.stringify(requestData),
      contentType: "application/json",
      success: function(response) {
        // Logica da eseguire in caso di successo
        console.log("Richiesta inviata con successo. Risposta del server:", response);
      },
      error: function(xhr, status, error) {
        // Logica da eseguire in caso di errore
        console.log("Errore durante l'invio della richiesta:", error);
      }
    });
  });

  //esecuzione ricevendo XML da T7 e mandando partita a T4
  var runButton = document.getElementById("runButton");
  runButton.addEventListener("click", function() {
      $(document).ready(function() {
      $.ajax({
        url: "/getResultXml",
        type: "GET",
        dataType: "text",
        success: function(xmlContent) {
          // Utilizza il contenuto del file XML come desideri
          console.log(xmlContent);

          // Imposta il contenuto della console CodeMirror con i risultati del file XML
          consoleArea2.setValue(xmlContent);
        },
        error: function() {
          // Gestisci l'errore, ad esempio mostra un messaggio di errore
          console.log("Errore durante il recupero del file XML.");
        }
      });
    });

    $(document).ready(function() {
      $.ajax({
        url: "/getResultRobot",
        type: "GET",
        dataType: "text",
        success: function(xmlContent) {
          // Utilizza il contenuto del file XML come desideri
          console.log(xmlContent);

          // Imposta il contenuto della console CodeMirror con i risultati del file XML
          consoleArea2.append(xmlContent);
        },
        error: function() {
          // Gestisci l'errore, ad esempio mostra un messaggio di errore
          console.log("Errore durante il recupero del file XML.");
        }
      });
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
        url: "/inviaDatiEFile",
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

  //risultati JACOCO da T7
  var coverageButton = document.getElementById("coverageButton");
    coverageButton.addEventListener("click", processJaCoCoReport);
    function processJaCoCoReport() {
      // Effettua una richiesta al tuo controller Spring per ottenere il file di output di JaCoCo
      $.ajax({
        url: "/getJaCoCoReport",
        type: "GET",
        dataType: "html", //potrebbe essere anche giusto leggere da un file xml di report di JaCoCo
        success: function(reportContent) {
          // Una volta ricevuto il file di output di JaCoCo, elabora il contenuto
          highlightCodeCoverage(reportContent);
        },
        error: function() {
          console.log("Errore durante il recupero del file di output di JaCoCo.");
        }
      });
    }
    function highlightCodeCoverage(reportContent) {
      // Analizza il contenuto del file di output di JaCoCo per individuare le righe coperte, non coperte e parzialmente coperte
      // Applica lo stile appropriato alle righe del tuo editor

      var coveredLines = [/* array di numeri di riga coperti */];
      var uncoveredLines = [/* array di numeri di riga non coperti */];
      var partiallyCoveredLines = [/* array di numeri di riga parzialmente coperti */];

      coveredLines.forEach(function(lineNumber) {
        editor.addLineClass(lineNumber - 1, "background", "covered-line");
      });

      uncoveredLines.forEach(function(lineNumber) {
        editor.addLineClass(lineNumber - 1, "background", "uncovered-line");
      });

      partiallyCoveredLines.forEach(function(lineNumber) {
        editor.addLineClass(lineNumber - 1, "background", "partially-covered-line");
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
  idUtenteElement.textContent = "UserID: 'Nome Utente'";
  modal2Content.appendChild(idUtenteElement);

  var idPartitaElement = document.createElement("p");
  idPartitaElement.textContent = "GameID: 'Identificativo Partita'";
  modal2Content.appendChild(idPartitaElement);

  var idTurnoElement = document.createElement("p");
  idTurnoElement.textContent = "Turno: '#Turno'";
  modal2Content.appendChild(idTurnoElement);

  var robotSceltoElement = document.createElement("p");
  robotSceltoElement.textContent = "Robot: 'Evosuite o Randoop'";
  modal2Content.appendChild(robotSceltoElement);

  var difficoltaElement = document.createElement("p");
  difficoltaElement.textContent = "Livello: 'Livello del match'";
  modal2Content.appendChild(difficoltaElement);
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