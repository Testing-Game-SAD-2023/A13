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

function initializeEditorResizing(
	container,
	editor,
	divider,
	section1,
	section2,
	closeButton
) {
	function setEditorSize() {
		const availableHeight = container.clientHeight;
		editor.setSize(null, availableHeight + "px");
	}

	function updateIcon(iconElement, isMinimized) {
		const icon1Class = "bi-arrow-bar-down";
		const icon2Class = "bi-arrow-bar-up";

		if (isMinimized) {
			iconElement.classList.add(icon2Class);
			iconElement.classList.remove(icon1Class);
		} else {
			iconElement.classList.add(icon1Class);
			iconElement.classList.remove(icon2Class);
		}
	}

	function enableResizing() {
		let isDragging = false;
		let lastHeightSection1 = null;
		let lastHeightSection2 = section2.offsetHeight; // Inizializza con l'altezza attuale
		const minHeightThreshold = 50; // Soglia per impostare section2 a 0
		const maxHeightThreshold = container.clientHeight - 50; // Soglia per espandere section2 al massimo

		divider.addEventListener("mousedown", () => {
			isDragging = true;
			document.body.style.cursor = "n-resize";
			container.classList.add("no-select");
		});

		document.addEventListener("mousemove", (e) => {
			if (!isDragging) return;
			const containerRect = container.getBoundingClientRect();
			const offsetY = e.clientY - containerRect.top;
			const newHeightSection2 = containerRect.height - offsetY - divider.offsetHeight;
			if (newHeightSection2 < minHeightThreshold) {
				// Imposta section2 a 0 se l'altezza è sotto la soglia minima
				section2.style.height = '0';
				section1.style.height = `${containerRect.height}px`;
				updateIcon(closeButton.querySelector("i"), true);
			} else if (newHeightSection2 > maxHeightThreshold) {
				// Espande section2 al massimo se supera la soglia massima
				section2.style.height = `${containerRect.height}px`;
				section1.style.height = '0';
				updateIcon(closeButton.querySelector("i"), false);
			} else {
				section1.style.height = `${offsetY}px`;
				section2.style.height = `${newHeightSection2}px`;
				updateIcon(closeButton.querySelector("i"), false);
			}
		});

		document.addEventListener("mouseup", () => {
			if (isDragging) {
				isDragging = false;
				document.body.style.cursor = "default";
				container.classList.remove("no-select");
				// Salva l'ultima altezza di section2 solo se non è minimizzata
				if (section2.offsetHeight > 0) {
					lastHeightSection2 = section2.offsetHeight;
				}
			}
		});

		closeButton.addEventListener("click", () => {
			const isMinimized = section2.offsetHeight === 0;
			if (isMinimized) {
				// Ripristina l'ultima altezza salvata
				section2.style.height = `${lastHeightSection2}px`;
				section1.style.height = `${container.clientHeight - lastHeightSection2}px`;
			} else {
				// Minimizza section2
				lastHeightSection2 = section2.offsetHeight; // Salva l'altezza corrente
				section2.style.height = '0';
				section1.style.height = `${container.clientHeight}px`;
			}

			// Aggiorna l'icona in base allo stato
			updateIcon(
				closeButton.querySelector("i"),
				!isMinimized
			);
		});
	}

	window.addEventListener("resize", setEditorSize);
	enableResizing();
	setEditorSize();
}

// Chiama la funzione per inizializzare il ridimensionamento degli editor
initializeEditorResizing(
	container_user,
	editor_utente,
	divider_Console,
	section_editor,
	section_console,
	close_console_utente
);
initializeEditorResizing(
	container_robot,
	editor_robot,
	divider_result,
	section_UT,
	section_result,
	close_console_result
);

// Aggiungi gli event listener ai bottoni
document.querySelectorAll(".toggleButton").forEach((button) => {
	button.addEventListener("click", function () {
		const icon1Class = this.getAttribute("data-icon1");
		const icon2Class = this.getAttribute("data-icon2");
		const iconElement = this.querySelector("i"); // Seleziona l'elemento <i> all'interno del bottone
		toggleIcons(icon1Class, icon2Class, iconElement);
	});
});
/*
 *   Qui è gestito l'handler del tema
 */
const chk = document.getElementById("chk_theme");
function handleThemeToggle() {
	const htmlElement = document.getElementById("html-root");
	// Ottieni tutti gli editor e console
	const editors = [editor_utente, console_utente, editor_robot, console_robot];

	// Definisci i temi come variabili
	const lightTheme = "mdn-like"; // Tema chiaro
	const darkTheme = "material-darker"; // Tema scuro

	// Funzione per applicare il tema e gestire l'opacità
	const applyTheme = () => {
		// Imposta un fade out
		editors.forEach((editor) => {
			editor.getWrapperElement().style.transition = "opacity 0.6s ease";
			editor.getWrapperElement().style.opacity = 0; // Fai svanire l'editor
		});

		// Cambiamo il tema visivamente
		setTimeout(() => {
			if (chk.checked) {
				// Tema chiaro
				htmlElement.setAttribute("data-bs-theme", "light");
				editors.forEach((editor) => {
					editor.setOption("theme", lightTheme);
					editor.setOption("mode", "text/x-java"); // Imposta il linguaggio Java
				});
			} else {
				// Tema scuro
				htmlElement.setAttribute("data-bs-theme", "dark");
				editors.forEach((editor) => {
					editor.setOption("theme", darkTheme);
					editor.setOption("mode", "text/x-java"); // Imposta il linguaggio Java
				});
			}

			// Dopo che il tema è cambiato, riporta l'opacità a 1 per il fade in
			editors.forEach((editor) => {
				editor.getWrapperElement().style.opacity = 1; // Fai riapparire l'editor
			});
		}, 500); // Tempo per il fade out prima di cambiare il tema
	};

	// Inizializza il tema al caricamento della pagina
	const initializeTheme = () => {
		if (chk.checked) {
			htmlElement.setAttribute("data-bs-theme", "light");
		} else {
			htmlElement.setAttribute("data-bs-theme", "dark");
		}
		applyTheme(); // Applica il tema iniziale
	};

	// Gestisci l'evento di change del toggle
	chk.addEventListener("change", applyTheme);

	// Inizializza il tema al caricamento della pagina
	document.addEventListener("DOMContentLoaded", initializeTheme);
}
// Chiama la funzione per gestire il toggle
handleThemeToggle();

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
