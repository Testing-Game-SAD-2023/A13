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

/*
function initializeEditorResizing(container, editor, divider, section1, section2, closeButton) {
    function setEditorSize(container, editor) {
        // Altezza del container
        const availableHeight = container.clientHeight; 
        // Imposta l'altezza dell'editor in base all'altezza del container disponibile
        editor.setSize(null, availableHeight + "px");
    }

    function enableResizing(container, divider, section1, section2, closeButton) {
        let isDragging = false;
        let lastHeightSection1 = null;
        let lastHeightSection2 = null;

        // Aggiungi event listener per il mouse down (quando si inizia a trascinare)
        divider.addEventListener("mousedown", function () {
            isDragging = true;
            document.body.style.cursor = "n-resize"; // Mostra la mano chiusa durante il trascinamento
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

            // Aggiorna le ultime altezze
            lastHeightSection1 = section1.style.height;
            lastHeightSection2 = section2.style.height;
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
        closeButton.addEventListener("click", function () {
            const section2Height = getComputedStyle(section2).height; // Ottiene l'altezza calcolata di section2

            if (lastHeightSection1 === null || lastHeightSection2 === null) {
                lastHeightSection1 = getComputedStyle(section1).height;
                lastHeightSection2 = getComputedStyle(section2).height;
            }

            // Alterna tra le altezze salvate e 0
            if (section2Height !== "0px") {
                // Imposta l'altezza della seconda sezione a 0
                section2.style.height = "0";
                // La prima sezione occupa tutta l'altezza del contenitore
                section1.style.height = `${container.clientHeight}px`;
            } else {
                // Ripristina l'ultima altezza fissata dall'utente
                section2.style.height = lastHeightSection2;
                section1.style.height = lastHeightSection1;
            }
        });
    }

    // Attiva il ridimensionamento degli editor al caricamento della finestra
    window.addEventListener("resize", function () {
        setEditorSize(container, editor); 
    });

    // Inizializza il ridimensionamento 
    enableResizing(
        container,
        divider,
        section1,
        section2,
        closeButton
    );
    // Imposta inizialmente la dimensione degli editor
    setEditorSize(container, editor); 
}
// Chiama la funzione per inizializzare il ridimensionamento degli editor
initializeEditorResizing(container_user, editor_utente, divider_Console, section_editor, section_console, close_console_utente);
initializeEditorResizing(container_robot, editor_robot, divider_result, section_UT, section_result, close_console_result);


function toggleIcons(icon1Class, icon2Class, iconElement) {
	// Controlla quale icona è attualmente presente e toggla
	if (iconElement.classList.contains(icon1Class)) {
		iconElement.classList.remove(icon1Class); // Rimuove l'icona 1
		iconElement.classList.add(icon2Class); // Aggiunge l'icona 2
	} else {
		iconElement.classList.add(icon1Class); // Rimuove l'icona 2
		iconElement.classList.remove(icon2Class); // Aggiunge l'icona 1
	}
}

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

	function updateIcon(iconElement, isMinimized, icon1Class, icon2Class) {
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
		let lastHeightSection2 = null;

		divider.addEventListener("mousedown", () => {
			isDragging = true;
			document.body.style.cursor = "n-resize";
			container.classList.add("no-select");
		});

        container.addEventListener("mousemove", (e) => {
            if (!isDragging) return;
            const containerRect = container.getBoundingClientRect();
            const offsetY = e.clientY - containerRect.top;
            section1.style.height = `${offsetY}px`;
            section2.style.height = `${containerRect.height - offsetY - divider.offsetHeight}px`;
        });

        closeButton.addEventListener("click", () => {
            const isMinimized = section2.offsetHeight === 0;
            section2.style.height = isMinimized ? '200px' : '0'; // Imposta l'altezza desiderata
            section1.style.height = isMinimized ? `${container.clientHeight - 200}px` : `${container.clientHeight}px`; // Cambia l'altezza di section1
            const iconElement = closeButton.querySelector("i");
            updateIcon(iconElement, !isMinimized, closeButton.getAttribute("data-icon1"), closeButton.getAttribute("data-icon2"));
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
