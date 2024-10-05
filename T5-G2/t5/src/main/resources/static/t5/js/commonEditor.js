//TASTO CERCA
var searchButton = document.getElementById("searchButton");
searchButton.addEventListener("click", function () {
	var searchTerm = document.getElementById("searchTerm").value.trim();
	var resultCount = 0; // Variabile per contare le occorrenze
	var resultSpan = document.getElementById("SearchItems"); // Span dove mostrare il risultato

	if (searchTerm) {
		clearSearchHighlights(); // Rimuovi eventuali evidenziazioni precedenti
		var cursor = editor_utente.getSearchCursor(searchTerm);
		// Evidenzia tutte le occorrenze del termine cercato
		while (cursor.findNext()) {
			editor_utente.markText(cursor.from(), cursor.to(), {
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
		var cursor = editor_utente.getSearchCursor(searchTerm);
		// Sostituisci ogni occorrenza del termine cercato con il nuovo testo
		while (cursor.findNext()) {
			editor_utente.replaceRange(replaceTerm, cursor.from(), cursor.to());
		}
		clearSearchHighlights(); // Rimuovi le evidenziazioni dopo la sostituzione
	}
});
// Funzione per rimuovere tutte le evidenziazioni precedenti
function clearSearchHighlights() {
	var marks = editor_utente.getAllMarks();
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

// TASTO COMPILA
/*
var compileButton = document.getElementById("compileButton");
compileButton.addEventListener("click", function () {
    // Logica da eseguire quando il pulsante viene cliccato
    // Ad esempio, esegui una chiamata AJAX al tuo controller Spring per inviare i dati
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
    formData.append("testingClassCode", editor_robot.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
    formData.append("underTestClassCode", sidebareditor_robot.getValue());

    document.getElementById('loading-editor_robot').style.display = 'block';
    $.ajax({
        url: "/api/sendInfo", //URL del controller Spring
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "text",
        success: function (response) {
            document.getElementById('loading-editor_robot').style.display = 'none';
            // Logica da eseguire in caso di successo
            consoleArea.setValue(response);
            console.log("Richiesta inviata con successo. Risposta del server:", response);
            alert(" (/sendInfo) Visualizza i risultati prodotti dalla compilazione.");
        },
        error: function (xhr, status, error) {
            // Logica da eseguire in caso di errore
            document.getElementById('loading-editor_robot').style.display = 'none';
            alert("Si è verificato un errore!");
            console.log("Errore durante l'invio della richiesta:", error);
        }
    });
});
*/

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
			editor_utente.setValue(fileContent); // Inserisci il contenuto nel CodeMirror
		};
		reader.readAsText(file); // Leggi il file come testo
	}
});

// DOWNLOAD FILE
var DownloadButton = document.getElementById("DownloadButton");
// Aggiungi un event listener per il pulsante di salvataggio
DownloadButton.addEventListener("click", function () {
	var fileContent = editor_utente.getValue(); // Ottieni il contenuto dall'editor
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

//REDO E UNDO
document.querySelector("#undo-button").addEventListener("click", function () {
	editor_utente.undo();
});
document.querySelector("#redo-button").addEventListener("click", function () {
	editor_utente.redo();
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


function openModalWithText(text_title, text_content) {
	document.getElementById('Modal_title').innerText = text_title;
	// Imposta il testo nel corpo del modal
	document.getElementById('Modal_body').innerText = text_content;
	// Ottieni il modal
	var modal = new bootstrap.Modal(document.getElementById('Modal'));
	// Mostra il modal
	modal.show();
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
				cm.replaceRange(
					this.textContent.slice(currentPrefix.length),
					cm.getCursor()
				);
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
var suggestionList = [
	"ArrayList",
	"LinkedList",
	"HashMap",
	"HashSet",
	"String",
	"Integer",
	"Boolean",
	"Double",
	"Float",
	"Character",
	"Byte",
	"Short",
	"Long",
	"Array",
	"List",
	"Set",
	"Map",
	"Queue",
	"Stack",
	"TreeSet",
	"TreeMap",
	"PriorityQueue",
	"Comparator",
	"Comparable",
	"Iterator",
	"Enumeration",
	"AbstractList",
	"AbstractSet",
	"AbstractMap",
	"AbstractQueue",
	"AbstractSequentialList",
	"LinkedListNode",
	"LinkedListIterator",
	"LinkedListSpliterator",
	"LinkedListDescendingIterator",
	"LinkedListDescendingSpliterator",
	"ArrayListNode",
	"ArrayListIterator",
	"ArrayListSpliterator",
	"ArrayListReverseIterator",
	"ArrayListReverseSpliterator",
	"HashSetNode",
	"HashSetIterator",
	"HashSetSpliterator",
	"HashMapEntry",
	"HashMapNode",
	"HashMapIterator",
	"HashMapSpliterator",
	"HashMapKeyIterator",
	"HashMapKeySpliterator",
	"HashMapValueIterator",
	"HashMapValueSpliterator",
	"HashMapEntryIterator",
	"HashMapEntrySpliterator",
	"HashSetDescendingIterator",
	"HashSetDescendingSpliterator",
	"TreeSetNode",
	"TreeSetIterator",
	"TreeSetSpliterator",
	"TreeSetDescendingIterator",
	"TreeSetDescendingSpliterator",
	"TreeMapEntry",
	"TreeMapNode",
	"TreeMapIterator",
	"TreeMapSpliterator",
	"TreeMapKeyIterator",
	"TreeMapKeySpliterator",
	"TreeMapValueIterator",
	"TreeMapValueSpliterator",
	"TreeMapEntryIterator",
	"TreeMapEntrySpliterator",
	"PriorityQueueNode",
	"PriorityQueueIterator",
	"PriorityQueueSpliterator",
	"AbstractCollection",
	"AbstractQueueIterator",
	"AbstractQueueSpliterator",
	"AbstractQueueDescendingIterator",
	"AbstractQueueDescendingSpliterator",
	"AbstractDeque",
	"LinkedListDeque",
	"LinkedListDequeNode",
	"LinkedListDequeIterator",
	"LinkedListDequeSpliterator",
	"LinkedListDequeDescendingIterator",
	"LinkedListDequeDescendingSpliterator",
	"ArrayDeque",
	"ArrayDequeNode",
	"ArrayDequeIterator",
	"ArrayDequeSpliterator",
	"ArrayDequeDescendingIterator",
	"ArrayDequeDescendingSpliterator",
	"StackNode",
	"StackIterator",
	"StackSpliterator",
	"StackDescendingIterator",
	"StackDescendingSpliterator",
	"AbstractListIterator",
	"AbstractListSpliterator",
	"ArrayListListIterator",
	"ArrayListListSpliterator",
	"LinkedListListIterator",
	"LinkedListListSpliterator",
	"AbstractSetIterator",
	"AbstractSetSpliterator",
	"HashSetSetIterator",
	"HashSetSetSpliterator",
	"TreeSetSetIterator",
	"TreeSetSetSpliterator",
	"AbstractMapIterator",
	"AbstractMapSpliterator",
	"HashMapMapIterator",
	"HashMapMapSpliterator",
	"TreeMapMapIterator",
	"TreeMapMapSpliterator",
	"AbstractSequentialListIterator",
	"AbstractSequentialListSpliterator",
	"LinkedListSequentialListIterator",
	"LinkedListSequentialListSpliterator",
	"ArrayListSequentialListIterator",
	"ArrayListSequentialListSpliterator",
	"LinkedListNodeIterator",
	"LinkedListNodeSpliterator",
	"ArrayListNodeIterator",
	"ArrayListNodeSpliterator",
	"HashSetNodeIterator",
	"HashSetNodeSpliterator",
	"HashMapEntryIterator",
	"HashMapEntrySpliterator",
	"HashSetDescendingIterator",
	"HashSetDescendingSpliterator",
	"TreeSetNodeIterator",
	"TreeSetNodeSpliterator",
	"TreeMapEntryIterator",
	"TreeMapEntrySpliterator",
	"PriorityQueueNodeIterator",
	"PriorityQueueNodeSpliterator",
	"AbstractCollectionIterator",
	"AbstractCollectionSpliterator",
	"AbstractQueueIterator",
	"AbstractQueueSpliterator",
	"AbstractQueueDescendingIterator",
	"AbstractQueueDescendingSpliterator",
	"AbstractDequeIterator",
	"AbstractDequeSpliterator",
	"LinkedListDequeIterator",
	"LinkedListDequeSpliterator",
	"LinkedListDequeDescendingIterator",
	"LinkedListDequeDescendingSpliterator",
	"ArrayDequeIterator",
	"ArrayDequeSpliterator",
	"ArrayDequeDescendingIterator",
	"ArrayDequeDescendingSpliterator",
	"StackNodeIterator",
	"StackNodeSpliterator",
	"StackDescendingIterator",
	"StackDescendingSpliterator",
	"AbstractListIterator",
	"AbstractListSpliterator",
	"ArrayListListIterator",
	"ArrayListListSpliterator",
	"LinkedListListIterator",
	"LinkedListListSpliterator",
	"AbstractSetIterator",
	"AbstractSetSpliterator",
	"HashSetSetIterator",
	"HashSetSetSpliterator",
	"TreeSetSetIterator",
	"TreeSetSetSpliterator",
	"AbstractMapIterator",
	"AbstractMapSpliterator",
	"HashMapMapIterator",
	"HashMapMapSpliterator",
	"TreeMapMapIterator",
	"TreeMapMapSpliterator",
	"AbstractSequentialListIterator",
	"AbstractSequentialListSpliterator",
	"LinkedListSequentialListIterator",
	"LinkedListSequentialListSpliterator",
	"ArrayListSequentialListIterator",
	"ArrayListSequentialListSpliterator",
	"LinkedListNodeIterator",
	"LinkedListNodeSpliterator",
	"ArrayListNodeIterator",
	"ArrayListNodeSpliterator",
	"HashSetNodeIterator",
	"HashSetNodeSpliterator",
	"HashMapEntryIterator",
	"HashMapEntrySpliterator",
	"HashSetDescendingIterator",
	"HashSetDescendingSpliterator",
	"TreeSetNodeIterator",
	"TreeSetNodeSpliterator",
	"TreeMapEntryIterator",
	"TreeMapEntrySpliterator",
	"PriorityQueueNodeIterator",
];
autocomplete(editor_robot, { startTag: ".", suggestionList: suggestionList });
