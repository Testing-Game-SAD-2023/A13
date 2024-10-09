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