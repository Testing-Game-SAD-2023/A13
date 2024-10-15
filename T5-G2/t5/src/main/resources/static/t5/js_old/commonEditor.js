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
    indentWithTabs: true,
    lineWrapping: true,
    theme: "material-darker",
    lineNumbers: true,
    smartIndent: true,
    electricChars: true,
    readOnly: true,
    autoCloseBrackets: true,
    matchBrackets: true,
    disableInput: true,
    autofocus: true
});
var consoleArea = CodeMirror.fromTextArea(document.getElementById("console-textarea"), {
    mode: "text/x-java",
    lineWrapping: true,
    theme: "material-darker",
    electricChars: true,
    readOnly: true,
    disableInput: true,
    autofocus: true
});
var consoleArea2 = CodeMirror.fromTextArea(document.getElementById("console-textarea2"), {
    mode: "bash",
    lineWrapping: true,
    theme: "material-darker",
    electricChars: true,
    readOnly: true,
    disableInput: true,
    autofocus: true
});
var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
    mode: "bash",
    indentWithTabs: true,
    smartIndent: true,
    lineWrapping: true,
    theme: "material-darker",
    lineNumbers: true,
    autoMatchBrackets: true,
    autoCloseBrackets: true,
    autofocus: true,
    foldGutter: true,
    gutters: ['CodeMirror-foldgutter']
});

editor.on('click', function (cm, event) {
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

            const divider_Console = document.getElementById("divider_Console");
			const divider_result = document.getElementById("divider_result");
			const section_editor = document.getElementById("section_editor");
			const section_console = document.getElementById("section_console");
			const section_UT = document.getElementById("section_UT");
			const section_result = document.getElementById("section_result");
			const container_user = document.getElementById("card_user");
			const container_robot = document.getElementById("card_robot");

			function enableResizing(container, divider, section1, section2, close_button) {
				let isDragging = false;

				// Aggiungi event listener per il mouse down (quando si inizia a trascinare)
				divider.addEventListener("mousedown", function (e) {
					isDragging = true;
					document.body.style.cursor = "grabbing"; // Mostra la mano chiusa durante il trascinamento
					container.classList.add("no-select"); // Disabilita la selezione del testo
				});

				// Event listener per il movimento del mouse
				container.addEventListener("mousemove", function (e) {
					if (!isDragging) return; // Se non si sta trascinando, non fare nulla

					// Calcola la nuova altezza della prima sezione
					const containerRect = container.getBoundingClientRect();
					const offsetY = e.clientY - containerRect.top;

					// Imposta la nuova altezza per la prima sezione
					section1.style.height = `${offsetY}px`;

					// Imposta l'altezza per la seconda sezione
					section2.style.height = `${containerRect.height - offsetY - 10}px`;
				});

				// Event listener per il rilascio del mouse (fine del trascinamento)
				document.addEventListener("mouseup", function () {
					if (isDragging) {
						isDragging = false;
						document.body.style.cursor = "default"; // Ripristina il cursore predefinito
						container.classList.remove("no-select"); // Riabilita la selezione del testo
					}
				});
			
				// Funzione per chiudere totalmente la sezione
				close_button.addEventListener("click", function () {
					const iconElement = close_button.querySelector('i');
					if(section2.style.height != "0"){
						section2.style.height = "0"; // Imposta l'altezza della seconda sezione a 0
						section1.style.height = `${container.clientHeight}px`; // Opzionale: imposta la prima sezione per occupare tutta l'altezza
					}
				});
			}

			// Imposta l'altezza del container all'altezza del viewport in modo dinamico
			function setEditorSize() {
				const availableHeightUser = container_user.clientHeight; // Altezza del container utente
				const availableHeightRobot = container_robot.clientHeigh; // Altezza del container robot

				// Imposta l'altezza degli editor in base all'altezza del container disponibile
				editor.setSize(null, availableHeightUser + "px");
				sidebarEditor.setSize(null, availableHeightRobot + "px");
			}

			window.addEventListener("resize", function () {
				setEditorSize(); // Mantieni l'altezza degli editor sincronizzata con l'altezza del container
			});

			enableResizing(
				container_user,
				divider_Console,
				section_editor,
				section_console,
				close_console_utente
			);
			enableResizing(
				container_robot,
				divider_result,
				section_UT,
				section_result, 
				close_console_result
			);

			// Funzione per cambiare il tema
			function toggleTheme() {
				const htmlElement = document.getElementById("html-root");
				const checkbox = document.getElementById("themeToggle");

				// Ottieni tutti gli editor e console
				const editors = [
					editor,
					consoleArea,
					sidebarEditor,
					consoleArea2,
				];

				// Imposta un fade out
				editors.forEach((editor) => {
					editor.getWrapperElement().style.transition = "opacity 0.6s ease";
					editor.getWrapperElement().style.opacity = 0; // Fai svanire l'editor
				});

				// Cambiamo il tema visivamente
				setTimeout(() => {
					if (checkbox.checked) {
						// Tema chiaro
						htmlElement.setAttribute("data-bs-theme", "light");
						editors.forEach((editor) => {
							editor.setOption("theme", "eclipse");
							editor.setOption("mode", "text/x-java"); // Imposta il linguaggio Java
						});
					} else {
						// Tema scuro
						htmlElement.setAttribute("data-bs-theme", "dark");
						editors.forEach((editor) => {
							editor.setOption("theme", "material-darker");
							editor.setOption("mode", "text/x-java"); // Imposta il linguaggio Java
						});
					}

					// Dopo che il tema è cambiato, riporta l'opacità a 1 per il fade in
					editors.forEach((editor) => {
						editor.getWrapperElement().style.transition = "opacity 0.6s ease";
						editor.getWrapperElement().style.opacity = 1; // Fai riapparire l'editor
					});
				}, 500); // Tempo per il fade out prima di cambiare il tema
			}
			toggleTheme();
			// Assegna l'evento al cambio di stato del checkbox
			document
				.getElementById("themeToggle")
				.addEventListener("change", toggleTheme);

			//funzione menu
			function showTabPane(paneId) {
				// Nascondi tutte le tab pane attive
				var tabPanes = document.querySelectorAll(".tab-pane");
				tabPanes.forEach(function (tabPane) {
					tabPane.classList.remove("show", "active");
				});

				// Mostra solo la tab pane richiesta
				var activePane = document.getElementById(paneId);
				activePane.classList.add("show", "active");
			}

			// Gestione dell'evento keydown per aprire e chiudere il dropdown
			document.addEventListener("keydown", function (event) {
				if (event.ctrlKey && event.key === "d") {
					// Combinazione di tasti Ctrl + D
					event.preventDefault(); // Evita comportamenti predefiniti (ad es. segnalibri nel browser)

					// Ottieni l'elemento del bottone del dropdown
					var dropdownButton = document.getElementById("utente_menu");

					// Crea o recupera l'istanza del dropdown di Bootstrap
					var dropdownInstance = bootstrap.Dropdown.getInstance(dropdownButton);
					if (!dropdownInstance) {
						dropdownInstance = new bootstrap.Dropdown(dropdownButton);
					}

					// Verifica lo stato corrente e alterna tra aprire e chiudere
					if (dropdownButton.getAttribute("aria-expanded") === "true") {
						dropdownInstance.hide(); // Chiudi il dropdown se è aperto
					} else {
						dropdownInstance.show(); // Apri il dropdown se è chiuso
					}
				}
			});

//compilazione verso T7
function getEditorContent() {
    return editor.getValue(); // Restituisce il contenuto del codice nell'editor
}
function getCUTContent() {
    return sidebarEditor.getValue(); // Restituisce il contenuto del codice nella CUT
}

//REDO E UNDO
document.querySelector("#undo-button").addEventListener("click", function () {
	editor.undo();
});
document.querySelector("#redo-button").addEventListener("click", function () {
	editor.redo();
});


// TASTO COMPILA

var compileButton = document.getElementById("compileButton");
compileButton.addEventListener("click", function () {
    // Logica da eseguire quando il pulsante viene cliccato
    // Ad esempio, esegui una chiamata AJAX al tuo controller Spring per inviare i dati
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
    formData.append("testingClassCode", editor.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
    formData.append("underTestClassCode", sidebarEditor.getValue());

    //document.getElementById('loading-editor').style.display = 'block';
    $.ajax({
        url: "/api/sendInfo", //URL del controller Spring
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "text",
        success: function (response) {
            //document.getElementById('loading-editor').style.display = 'none';
            // Logica da eseguire in caso di successo
            consoleArea.setValue(response);
            console.log("Richiesta inviata con successo. Risposta del server:", response);
            alert(" (/sendInfo) Visualizza i risultati prodotti dalla compilazione.");
        },
        error: function (xhr, status, error) {
            // Logica da eseguire in caso di errore
            //document.getElementById('loading-editor').style.display = 'none';
            alert("Si è verificato un errore!");
            console.log("Errore durante l'invio della richiesta:", error);
        }
    });
});

function getJavaClassContent() {
    // Assume che tu abbia un elemento HTML con un ID univoco che contiene il codice della classe Java
    var javaClassElement = document.getElementById("editor");
    if (javaClassElement) {
        return javaClassElement.value; // Restituisce il valore del campo di input o l'HTML interno dell'elemento
    } else {
        return ""; // Restituisce una stringa vuota se l'elemento non è presente o non ha un valore
    }
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
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "covered-line");
    });

    uncoveredLines.forEach(function (lineNumber) {
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "uncovered-line");
    });

    partiallyCoveredLines.forEach(function (lineNumber) {
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "partially-covered-line");
    });
}


function showName(nome) {
    document.getElementById("nome-pulsante").innerHTML = nome;
}
function hideName() {
    document.getElementById("nome-pulsante").innerHTML = "";
}
function runCoverage() {
    var code = editor.getValue();
    var jacoco = new Jacoco();
    jacoco.setOptions({
        language: "java",
        sourceCode: code
    });
    var report = jacoco.run();
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
    editor.on("change", function () {
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
        var blob = new Blob([code], { type: "text/plain;charset=utf-8" });
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

    cm.on("inputRead", function (cm, change) {
        if (change.text.length && change.text[0] === startTag) {
            currentPrefix = "";
            showSuggestions(cm, currentPrefix);
        }
    });

    cm.on("keyup", function (cm, event) {
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
        var suggestions = suggestionList.filter(function (item) {
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
            option.addEventListener("click", function (e) {
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

    document.addEventListener("click", function (e) {
        closeAllLists();
    });
}

var suggestionList = ["ArrayList", "LinkedList", "HashMap", "HashSet", "String", "Integer", "Boolean", "Double", "Float", "Character", "Byte", "Short", "Long", "Array", "List", "Set", "Map", "Queue", "Stack", "TreeSet", "TreeMap", "PriorityQueue", "Comparator", "Comparable", "Iterator", "Enumeration", "AbstractList", "AbstractSet", "AbstractMap", "AbstractQueue", "AbstractSequentialList", "LinkedListNode", "LinkedListIterator", "LinkedListSpliterator", "LinkedListDescendingIterator", "LinkedListDescendingSpliterator", "ArrayListNode", "ArrayListIterator", "ArrayListSpliterator", "ArrayListReverseIterator", "ArrayListReverseSpliterator", "HashSetNode", "HashSetIterator", "HashSetSpliterator", "HashMapEntry", "HashMapNode", "HashMapIterator", "HashMapSpliterator", "HashMapKeyIterator", "HashMapKeySpliterator", "HashMapValueIterator", "HashMapValueSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNode", "TreeSetIterator", "TreeSetSpliterator", "TreeSetDescendingIterator", "TreeSetDescendingSpliterator", "TreeMapEntry", "TreeMapNode", "TreeMapIterator", "TreeMapSpliterator", "TreeMapKeyIterator", "TreeMapKeySpliterator", "TreeMapValueIterator", "TreeMapValueSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNode", "PriorityQueueIterator", "PriorityQueueSpliterator", "AbstractCollection", "AbstractQueueIterator", "AbstractQueueSpliterator", "AbstractQueueDescendingIterator", "AbstractQueueDescendingSpliterator", "AbstractDeque", "LinkedListDeque", "LinkedListDequeNode", "LinkedListDequeIterator", "LinkedListDequeSpliterator", "LinkedListDequeDescendingIterator", "LinkedListDequeDescendingSpliterator", "ArrayDeque", "ArrayDequeNode", "ArrayDequeIterator", "ArrayDequeSpliterator", "ArrayDequeDescendingIterator", "ArrayDequeDescendingSpliterator", "StackNode", "StackIterator", "StackSpliterator", "StackDescendingIterator", "StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator", "ArrayListListIterator", "ArrayListListSpliterator", "LinkedListListIterator", "LinkedListListSpliterator", "AbstractSetIterator", "AbstractSetSpliterator", "HashSetSetIterator", "HashSetSetSpliterator", "TreeSetSetIterator", "TreeSetSetSpliterator", "AbstractMapIterator", "AbstractMapSpliterator", "HashMapMapIterator", "HashMapMapSpliterator", "TreeMapMapIterator", "TreeMapMapSpliterator", "AbstractSequentialListIterator", "AbstractSequentialListSpliterator", "LinkedListSequentialListIterator", "LinkedListSequentialListSpliterator", "ArrayListSequentialListIterator", "ArrayListSequentialListSpliterator", "LinkedListNodeIterator", "LinkedListNodeSpliterator", "ArrayListNodeIterator", "ArrayListNodeSpliterator", "HashSetNodeIterator", "HashSetNodeSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNodeIterator", "TreeSetNodeSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNodeIterator", "PriorityQueueNodeSpliterator", "AbstractCollectionIterator", "AbstractCollectionSpliterator", "AbstractQueueIterator", "AbstractQueueSpliterator", "AbstractQueueDescendingIterator", "AbstractQueueDescendingSpliterator", "AbstractDequeIterator", "AbstractDequeSpliterator", "LinkedListDequeIterator", "LinkedListDequeSpliterator", "LinkedListDequeDescendingIterator", "LinkedListDequeDescendingSpliterator", "ArrayDequeIterator", "ArrayDequeSpliterator", "ArrayDequeDescendingIterator", "ArrayDequeDescendingSpliterator", "StackNodeIterator", "StackNodeSpliterator", "StackDescendingIterator", "StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator", "ArrayListListIterator", "ArrayListListSpliterator", "LinkedListListIterator", "LinkedListListSpliterator", "AbstractSetIterator", "AbstractSetSpliterator", "HashSetSetIterator", "HashSetSetSpliterator", "TreeSetSetIterator", "TreeSetSetSpliterator", "AbstractMapIterator", "AbstractMapSpliterator", "HashMapMapIterator", "HashMapMapSpliterator", "TreeMapMapIterator", "TreeMapMapSpliterator", "AbstractSequentialListIterator", "AbstractSequentialListSpliterator", "LinkedListSequentialListIterator", "LinkedListSequentialListSpliterator", "ArrayListSequentialListIterator", "ArrayListSequentialListSpliterator", "LinkedListNodeIterator", "LinkedListNodeSpliterator", "ArrayListNodeIterator", "ArrayListNodeSpliterator", "HashSetNodeIterator", "HashSetNodeSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNodeIterator", "TreeSetNodeSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNodeIterator"];
autocomplete(editor, { startTag: ".", suggestionList: suggestionList });


//TASTO CERCA
var searchButton = document.getElementById("searchButton");
searchButton.addEventListener("click", function () {
	var searchTerm = document.getElementById("searchTerm").value.trim();
	var resultCount = 0; // Variabile per contare le occorrenze
	var resultSpan = document.getElementById("SearchItems"); // Span dove mostrare il risultato

	if (searchTerm) {
		clearSearchHighlights(); // Rimuovi eventuali evidenziazioni precedenti
		var cursor = editor.getSearchCursor(searchTerm);
		// Evidenzia tutte le occorrenze del termine cercato
		while (cursor.findNext()) {
			editor.markText(cursor.from(), cursor.to(), {
				className: "highlight",
			});
			resultCount++;
		}
		// Aggiorna lo span con il numero di occorrenze trovate
		resultSpan.textContent = resultCount;
		resultSpan.style.display = "inline"; // Mostra lo span
	} else {
		// Nessun termine inserito, mostra un messaggio di errore e rendi visibile lo span
		resultSpan.style.display = "inline"; // Mostra lo span
	}
});

// Funzione per sostituire il termine cercato con il nuovo termine
var startReplaceButton = document.getElementById("startReplace");
startReplaceButton.addEventListener("click", function () {
	var searchTerm = document.getElementById("searchTerm").value.trim();
	var replaceTerm = document.getElementById("replaceTerm").value.trim();

	if (searchTerm && replaceTerm) {
		var cursor = editor.getSearchCursor(searchTerm);
		// Sostituisci ogni occorrenza del termine cercato con il nuovo testo
		while (cursor.findNext()) {
			editor.replaceRange(replaceTerm, cursor.from(), cursor.to());
		}
		clearSearchHighlights(); // Rimuovi le evidenziazioni dopo la sostituzione
	}
});
// Funzione per rimuovere tutte le evidenziazioni precedenti
function clearSearchHighlights() {
	var marks = editor.getAllMarks();
	marks.forEach(function (mark) {
		mark.clear();
	});
	var resultSpan = document.getElementById("SearchItems");
	resultSpan.style.display = "none"; // Nasconde lo span
}
// Aggiungi evento input per rimuovere le vecchie evidenziazioni quando si inserisce un nuovo termine di ricerca
document
	.getElementById("searchTerm")
	.addEventListener("input", clearSearchHighlights);


// INPUT FILE
var fileInput = document.getElementById("fileInput");
// Aggiungi un event listener per l'input file
fileInput.addEventListener("change", function (event) {
	var file = event.target.files[0]; // Prendi il primo file selezionato
	if (file) {
		var reader = new FileReader();
		// Leggi il contenuto del file
		reader.onload = function (e) {
			var fileContent = e.target.result; // Contenuto del file
			editor.setValue(fileContent); // Inserisci il contenuto nel CodeMirror
		};
		reader.readAsText(file); // Leggi il file come testo
	}
});

// DOWNLOAD FILE
var DownloadButton = document.getElementById("DownloadButton");
// Aggiungi un event listener per il pulsante di salvataggio
DownloadButton.addEventListener("click", function () {
	var fileContent = editor.getValue(); // Ottieni il contenuto dall'editor
	var blob = new Blob([fileContent], { type: "text/plain" }); // Crea un oggetto Blob con il contenuto
	// Crea un link temporaneo per il download del file
	var link = document.createElement("a");
	link.href = URL.createObjectURL(blob);
	link.download = "codice.java"; // Nome del file salvato
	// Simula un clic sul link per avviare il download
	link.click();
	// Rilascia l'oggetto URL per evitare perdite di memoria
	URL.revokeObjectURL(link.href);
});


//AGGIUNTA A STORICO
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

//TASTO INFO
document.addEventListener("DOMContentLoaded", function () {
	// Seleziona il bottone popover
	var popoverButton = document.getElementById("popover_info");
	var messaggio =
		"Classe UT: "  + 	localStorage.getItem("underTestClassName") + "<br>" +
		"Difficoltà: " + 	localStorage.getItem("difficulty") + "<br>" +
		"Robot: " 	   +	localStorage.getItem("robot");
	
	var popover = new bootstrap.Popover(popoverButton, {
		content: messaggio,  	  // Usa il contenuto dal localStorage
		trigger: 'click',         // Mostra il popover al passaggio del mouse (puoi cambiare con 'click' o 'focus')
		html: true                // Se vuoi abilitare contenuti HTML nel popover
	});
});
