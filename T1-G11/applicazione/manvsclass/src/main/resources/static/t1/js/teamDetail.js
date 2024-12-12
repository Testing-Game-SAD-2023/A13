function getTeamCodeFromPath() {
    // Ottieni il percorso completo dalla URL
    const path = window.location.pathname;

    // Estrai il codice del team (l'ultima parte del percorso dopo l'ultimo '/')
    const pathParts = path.split('/');
    
    // Restituisci l'ultimo elemento, che dovrebbe essere il codice del team
    const teamCode = pathParts[pathParts.length - 1];
    console.log(teamCode);
    // Restituisci il codice del team
    return teamCode;
}

//Funzione per la visualizzazione del nome del team
document.addEventListener("DOMContentLoaded", () => nameTeams());

function nameTeams() {
    //Prelievo del codice del team
    const idTeam=getTeamCodeFromPath();
    const url = `/cercaTeam/${idTeam}`;//url della rotta per il prelievo dei dati del team

    // 
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Errore nel recupero dei dati del team');
        }
        return response.json();  // Parsea la risposta in formato JSON
    })
    .then(data => {
        // Puoi manipolare il DOM o fare altre operazioni con i dati ricevuti
        document.getElementById("title").textContent = `${data.name}`;
    })
    .catch(error => {
        console.error('Errore:', error);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    populateTableBasedOnToggle(); // Popola la tabella al caricamento della pagina
});


// Funzione per aggiornare l'header della tabella in base al toggle
document.getElementById("toggleSwitch").addEventListener("change", populateTableBasedOnToggle);

// Funzione per aggiornare l'header della tabella in base al toggle
function populateTableBasedOnToggle() {
    const toggleSwitch = document.getElementById("toggleSwitch");
    const studentsContainer = document.getElementById("studentsContainer");
    const assignmentsContainer = document.getElementById("assignmentsContainer");
    const studentsHeader = document.querySelector("#studentsContainer .table-header");
    const assignmentsHeader = document.querySelector("#assignmentsContainer .table-header");
    const toggleText = document.getElementById("toggleText");
  

    // Controlla lo stato del toggle (checked o no)
    if (toggleSwitch.checked) {
        // Se il toggle è su "Assignments"
        studentsContainer.classList.remove("active"); // Nasconde la tabella degli studenti
        assignmentsContainer.classList.add("active"); // Mostra la tabella degli assignment

        studentsHeader.style.display = "none"; // Nasconde l'header degli studenti
        assignmentsHeader.style.display = "flex"; // Mostra l'header degli assignment

        // Aggiorna il testo del toggle
        toggleText.textContent = "Assignments";
      

        populateTableAssignment(); // Popola la tabella degli assignment
    } else {
        // Se il toggle è su "Student"
        studentsContainer.classList.add("active"); // Mostra la tabella degli studenti
        assignmentsContainer.classList.remove("active"); // Nasconde la tabella degli assignment

        studentsHeader.style.display = "flex"; // Mostra l'header degli studenti
        assignmentsHeader.style.display = "none"; // Nasconde l'header degli assignment

        // Aggiorna il testo del toggle
        toggleText.textContent = "Student";
        

        populateTable(); // Popola la tabella degli studenti
    }
}


function populateTable() {
    const tableContainer = document.querySelector("#studentsContainer .responsive-table");
    const idTeam = getTeamCodeFromPath();
    clearTable(tableContainer);
    const rows = tableContainer.querySelectorAll('.table-row');
    rows.forEach(row => row.style.display = "none"); // Nascondi le righe prima di caricare nuovi dati

    fetch(`/ottieniStudentiTeam/${idTeam}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        // Verifica se la risposta è vuota o non valida
        if (!response.ok) {
            throw new Error('Errore nella risposta del server');
        }
         // Controlla il tipo di contenuto della risposta
         const contentType = response.headers.get('Content-Type');
        
         if (contentType && contentType.includes('application/json')) {
             // Se la risposta è JSON, parsificalo
             return response.json();
         } else {
             // Se la risposta è un testo (ad esempio, un messaggio di errore o una risposta vuota), restituiscilo come stringa
             return response.text();
         }
    })
    .then(data => {
        // Assicurati che data.body sia un array prima di chiamare forEach
        if (Array.isArray(data.body)) {
            if (data.body.length > 0) {
                data.body.forEach(student => addRow(tableContainer, student)); // Aggiungi i dati alla tabella
            } else {
                addEmptyRow(tableContainer);  // Aggiungi la riga vuota se il corpo è vuoto
            }
         } else if (typeof data === 'string') {
                // Se la risposta è una stringa (ad esempio, un messaggio del tipo "Nessun assignment trovato")
                console.log('Risposta testuale ricevuta: ', data);
                addEmptyRow(tableContainer); // Aggiungi una riga vuota in caso di risposta vuota o messaggio di errore} 
         }else {
            console.error("Errore: la risposta del server non contiene un array valido di studenti.");
            addEmptyRow(tableContainer); // Aggiungi la riga vuota in caso di errore
        }
    })
    .catch(error => {
        console.error("Errore:", error);
        addEmptyRow(tableContainer); // Aggiungi la riga vuota in caso di errore
    });
}
function populateTableAssignment() {
    const tableContainer = document.querySelector("#assignmentsContainer .responsive-table"); // Assicurati che qui venga selezionato il giusto container per gli assignment
    const idTeam = getTeamCodeFromPath();
    clearTable(tableContainer);
    const rows = tableContainer.querySelectorAll('.table-row');
    rows.forEach(row => row.style.display = "none"); // Nascondi le righe prima di caricare nuovi dati

    fetch(`/visualizzaTeamAssignments/${idTeam}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        // Verifica se la risposta è vuota o non valida
        if (!response.ok) {
            return response.text(); // Non solleviamo un errore, ma restituiamo la risposta come testo
        }

        // Controlla il tipo di contenuto della risposta
        const contentType = response.headers.get('Content-Type');
        
        if (contentType && contentType.includes('application/json')) {
            // Se la risposta è JSON, parsificalo
            return response.json();
        } else {
            // Se la risposta è un testo (ad esempio, un messaggio di errore o una risposta vuota), restituiscilo come stringa
            return response.text();
        }
    })
    .then(data => {
        // Gestiamo la risposta basata sul tipo di dato ricevuto
        if (Array.isArray(data)) {
            if (data.length > 0) {
                data.forEach(assignment => addRowAssignment(tableContainer, assignment)); // Aggiungi gli assignment
            } else {
                addEmptyRow(tableContainer);  // Aggiungi una riga vuota se non ci sono assignment
            }
        } else if (typeof data === 'string') {
            // Se la risposta è una stringa (ad esempio, un messaggio del tipo "Nessun assignment trovato")
            console.log('Risposta testuale ricevuta: ', data);
            addEmptyRow(tableContainer); // Aggiungi una riga vuota in caso di risposta vuota o messaggio di errore
        } else {
            console.error("Errore: la risposta non è né un JSON valido né una stringa.");
            addEmptyRow(tableContainer); // Aggiungi la riga vuota in caso di errore
        }
    })
    .catch(error => {
        console.error("Errore:", error);
        addEmptyRow(tableContainer); // Aggiungi la riga vuota in caso di errore
    });
}

// Funzione per aggiungere una riga vuota
function addEmptyRow(container) {
    // Verifica se esiste già una riga vuota nel container specificato
    const existingEmptyRow = container.querySelector("#empty-row");
    
    if (existingEmptyRow) {
        return; // Se esiste, non aggiungere una nuova riga vuota
    }

    // Crea una nuova riga vuota
    const emptyRow = document.createElement("li");
    emptyRow.classList.add("table-row");
    emptyRow.id = "empty-row";

    // Aggiungi il contenuto della riga vuota
    emptyRow.innerHTML = `
        <div class="col-1">-</div>
        <div class="col-2">-</div>
        <div class="col-3">-</div>
        <div class="col-4">-</div>
        <div class="col-delete"></div>
    `;

    // Aggiungi la riga vuota al container
    container.appendChild(emptyRow);
}

function addRow(container, student) {
    const row = document.createElement("li");
    row.classList.add("table-row");
     // Controlla se la riga con lo stesso ID esiste già nella tabella
     const existingRow = container.querySelector(`.table-row[data-id="${student.id}"]`);
     if (existingRow) return; // Se esiste già, non aggiungere una nuova riga


    // Controlla se i campi sono vuoti, altrimenti metti un trattino "-"
    const name = student.name || "-";
    const surname = student.surname || "-";
    const email = student.email || "-";

    row.innerHTML = `
        <div class="col-1">${student.id}</div>
        <div class="col-2">${name}</div>
        <div class="col-3">${surname}</div>
        <div class="col-4">${email}</div>
        <div class="col-delete">
            <button class="delete-button">Delete</button>
        </div>
    `;

    container.appendChild(row);
}

// Funzione di utilità per formattare le date
function formatDate(dateString) {
    const date = new Date(dateString);
    if (isNaN(date)) {  // Verifica se la data è valida
        return "-";
    }
    // Restituisci la data formattata come giorno/mese/anno
    return date.toLocaleDateString('it-IT');
}


function addRowAssignment(container, assignment) {
    const row = document.createElement("li");
    row.classList.add("table-row");

    // Verifica e assegna i valori dei campi dell'assegnamento
    const idAssignment = assignment.idAssignment || "-";
    const titolo = assignment.titolo || "-";

   // Usa la funzione di utilità per formattare le date
   const dataCreazione = formatDate(assignment.dataCreazione);
   const dataScadenza = formatDate(assignment.dataScadenza);

    // Aggiungi il contenuto alla riga
    row.innerHTML = `
        <div class="col-1">${idAssignment}</div>
        <div class="col-2">${titolo}</div>
        <div class="col-3">${dataCreazione}</div>
        <div class="col-4">${dataScadenza}</div>
        <div class="col-delete">
            <button class="delete-button">Delete</button>
        </div>
    `;

    // Aggiungi la riga al contenitore
    container.appendChild(row);
}



// Funzione per aprire la modale con la barra di ricerca
// Funzione per aprire la modale con la barra di ricerca
function openModal() {
    // Recupera il contenitore della modale e inserisce il contenuto HTML della finestra modale.
    const modalContainer = document.getElementById("modalContainer");
    modalContainer.innerHTML = `
        <div class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Aggiungi Studente</h2>
                <input type="text" id="search-email" placeholder="Cerca studente per email..." />
                <p id="selectedCount">Selezionati: 0</p>
                <ul id="searchResults"></ul>
                <button id="searchButton">Cerca</button>
                <button id="addButton" disabled>Aggiungi</button>
            </div>
        </div>
    `;

    // Recupera i riferimenti agli elementi HTML della modale
    const searchEmailInput = document.getElementById("search-email"); // Campo input per la ricerca
    const searchResults = document.getElementById("searchResults"); // Contenitore per i risultati della ricerca
    const addButton = document.getElementById("addButton"); // Pulsante "Aggiungi", inizialmente disabilitato
    const selectedCount = document.getElementById("selectedCount"); // Elemento per mostrare il numero di selezionati

    // Array per tenere traccia degli studenti selezionati (ID)
    let selectedStudents = [];

    // Funzione per cercare studenti in base all'email inserita
    function searchStudents() {
        // Recupera e normalizza l'email dall'input
        const emailQuery = searchEmailInput.value.toLowerCase().trim();

        // Abilita il pulsante "Aggiungi" solo dopo aver avviato una ricerca
        addButton.disabled = false;

        // Se il campo di ricerca è vuoto, mostra un messaggio di errore
        if (!emailQuery) {
            searchResults.innerHTML = '<li>Ricerca almeno uno studente!</li>';
            return;
        }

        // Effettua una richiesta al server per cercare uno studente con l'email fornita
        fetch(`/studentByEmail/${emailQuery}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                // Verifica se la risposta è valida
                if (!response.ok) {
                    throw new Error('Errore durante la ricerca degli studenti');
                }
                // Converte la risposta in formato JSON
                return response.json();
            })
            .then(filteredStudent => {
                // Se lo studente esiste, mostra i suoi dati
                if (filteredStudent) {
                    // Controlla se lo studente è già stato selezionato
                    const isSelected = selectedStudents.includes(filteredStudent.id);

                    // Crea l'elenco dei risultati con il checkbox
                    searchResults.innerHTML = `
                        <li>
                            <input 
                                type="checkbox" 
                                data-id="${filteredStudent.id}" 
                                data-name="${filteredStudent.name}" 
                                data-surname="${filteredStudent.surname}" 
                                ${isSelected ? 'checked' : ''} 
                            />
                            ${filteredStudent.name} ${filteredStudent.surname}
                        </li>
                    `;
                } else {
                    // Mostra un messaggio se nessuno studente è stato trovato
                    searchResults.innerHTML = '<li>Nessuno studente trovato.</li>';
                }

                // Aggiunge un listener di evento a tutti i checkbox
                document.querySelectorAll('#searchResults input[type="checkbox"]').forEach(checkbox => {
                    checkbox.addEventListener('change', (e) => {
                        // Recupera l'ID dello studente dal checkbox selezionato
                        const studentId = e.target.dataset.id;

                        // Aggiunge o rimuove l'ID dello studente dall'array dei selezionati
                        if (e.target.checked) {
                            if (!selectedStudents.includes(studentId)) {
                                selectedStudents.push(studentId);
                            }
                        } else {
                            selectedStudents = selectedStudents.filter(id => id !== studentId);
                        }

                        // Aggiorna il contatore e lo stato del pulsante "Aggiungi"
                        selectedCount.textContent = `Selezionati: ${selectedStudents.length}`;
                        addButton.disabled = selectedStudents.length === 0;
                    });
                });
            })
            .catch(error => {
                // Gestisce eventuali errori durante la ricerca
                console.error('Errore nella fetch:', error);
                searchResults.innerHTML = '<li>Si è verificato un errore. Riprova.</li>';
            });
    }
    // Ascoltatore per il pulsante di ricerca
    document.getElementById("searchButton").addEventListener("click", searchStudents);

    // Funzione per aggiungere gli studenti selezionati alla tabella
    function addStudentsToTeam() {
            console.log(selectedStudents);
            const idTeam = getTeamCodeFromPath();
            if (!selectedStudents || selectedStudents.length === 0) {
                return;
            }
            // Serializza l'array selectedStudents in formato JSON
            const requestBody = JSON.stringify(selectedStudents);  // Converti in JSON
            fetch(`/aggiungiStudenti/${idTeam}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: requestBody, // Invia solo gli ID degli studenti selezionati
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Errore durante l\'aggiunta degli studenti');
                    }
                    return response.json(); // Processa la risposta come JSON
                })
                .then(data => {
                    // Notifica il successo
                    alert('Studenti aggiunti con successo al team!');
                    console.log('Risposta del server:', data);
                    const tableContainer = document.querySelector("#studentsContainer .responsive-table");
                    clearTable(tableContainer);
                    populateTable();
                    
                })
                .catch(error => {
                    console.error('Errore nella fetch:', error);
                    alert('Si è verificato un errore durante l\'aggiunta degli studenti. Riprova.');
                });
                resetModal();
                
                
                modal.style.display= "none";
        
    }

    // Ascoltatore per il pulsante "Aggiungi"
    addButton.addEventListener("click", addStudentsToTeam);
    
    // Funzione di chiusura della modale
    const closeButton = document.querySelector(".close");
    const modal = document.querySelector(".modal");
    closeButton.onclick = () => modal.style.display = "none";

    window.onclick = (event) => {
        if (event.target == modal) modal.style.display = "none";
    };

    // Riapri la modale
    modal.style.display = "block";
}

// Funzione per pulire la modale dopo l'aggiunta
function resetModal() {
    // Resetta il campo di ricerca
    document.getElementById("search-email").value = "";
    
    // Pulisce i risultati e il contatore
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("selectedCount").textContent = "Selezionati: 0";
    
    // Disabilita il bottone "Aggiungi"
    document.getElementById("addButton").disabled = true;

}


// Ascoltatore per il pulsante "Add Student"
document.getElementById("openModalButton").addEventListener("click", function () {
    const toggleText = document.getElementById("toggleText").textContent.trim().toLowerCase(); // Normalizza il testo

    // Verifica se il testo del toggle è "students"
    if (toggleText === "student") {
        // Apri la modale degli studenti
        openModal();
    } else {
        // Non aprire la modale degli studenti
        alert("Devi essere nella sezione Students per aggiungere uno studente.");
    }
});

// Ascoltatore per il pulsante "Add Assignment"
document.getElementById("openModalButtonAssignment").addEventListener("click", function () {
    const toggleText = document.getElementById("toggleText").textContent.trim().toLowerCase(); // Normalizza il testo

    // Verifica se il testo del toggle è "assignments"
    if (toggleText === "assignments") {
        // Apri la modale degli assignment
        openModalAssignment();
    } else {
        // Non aprire la modale degli assignment
        alert("Devi essere nella sezione Assignments per creare un Assignment.");
    }
});


// Apertura finestra modale Assignment
function openModalAssignment() {
    const modalContainer = document.getElementById("modalContainerAssignment");
    const nomeTeam = document.getElementById("title").textContent;

    // HTML della modale
    modalContainer.innerHTML = `
        <div class="modal_assignment">
            <div class="modal-content">
                <span class="close_assignment">&times;</span>
                <h2>Crea Nuovo Assignment</h2>
                <form id="AssignmentForm">
                    <!-- Nome del Team -->
                    <label for="teamName">Nome del Team: ${nomeTeam}</label>

                    <!-- Titolo dell'assegnamento -->
                    <label for="gameSelector">Scegli Classe di test da sottoporre:</label>
                    <select id="gameSelector" name="gameSelector" required>
                        <option value="">Caricamento giochi...</option>
                    </select>

                    <!-- Data di scadenza -->
                    <label for="deadline">Data di Scadenza:</label>
                    <input type="date" id="deadline" name="deadline" required>

                    <!-- Descrizione -->
                    <label for="description">Descrizione (max 500 caratteri):</label>
                    <textarea id="description" name="description" maxlength="500" placeholder="Inserisci una descrizione..."></textarea>

                    <!-- Pulsante per creare l'assignment -->
                    <button type="button" id="createAssignmentButton">Crea Assignment</button>
                </form>
            </div>
        </div>
    `;

    // Mostra la modale
    const modal = document.querySelector(".modal_assignment");
    const closeButton = document.querySelector(".close_assignment");

    modal.style.display = "block";

    // Popola il menu a tendina con una fetch dal database
    populateGameSelector();

    // Chiudi la modale al clic su "x" o fuori dalla modale
    closeButton.onclick = () => (modal.style.display = "none");
    window.onclick = (event) => {
        if (event.target === modal) modal.style.display = "none";
    };

    // Aggiungi listener al pulsante "Crea Assignment"
    const createAssignmentButton = document.getElementById("createAssignmentButton");
    createAssignmentButton.addEventListener("click", () => {
        const teamName = nomeTeam;
        const selectedGame = document.getElementById("gameSelector").value;
        const deadline = document.getElementById("deadline").value;
        const description = document.getElementById("description").value;
        console.log(deadline);
        if (!teamName || !selectedGame) {
            alert("Inserisci il nome del team e seleziona un gioco prima di procedere!");
            return;
        }
        
        // Cerca l'ID del team in base al nome
        const teamId = getTeamCodeFromPath();
        if (!teamId) {
            alert("Team non trovato!");
            return;
        }
         // Creiamo una data con l'orario di default 00:00:00, se necessario
        const formattedDate = new Date(deadline + "T00:00:00");  // Assicurati che la data venga considerata come inizio giornata


        // Invio della fetch per creare l'assignment
        fetch(`/creaAssignment/${teamId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: 'include', // Include i cookie (JWT) se necessario
            body: JSON.stringify({
                titolo: selectedGame,  // Usa il gioco selezionato come titolo
                descrizione: description,  // Usa la descrizione inserita
                dataScadenza: formattedDate.toISOString() // Usa la data di scadenza selezionata
            })
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error("Errore durante la creazione dell'Assignment");
            }
            return response.text();
        })
        .then((result) => {
            // La risposta è una stringa che contiene il messaggio
            if (result === "Assignment creato con successo e associato al Team.") {
                alert(result); // Mostriamo il messaggio di successo
                console.log("Risultato della creazione:", result);
                modal.style.display = "none";  // Chiudi la modale dopo la creazione
                const tableContainer = document.querySelector("#assignmentsContainer .responsive-table");
                clearTable(tableContainer);
                populateTableAssignment();
            } else {
                // Gestione di eventuali messaggi di errore
                alert("Errore durante la creazione dell'Assignment. Riprova.");
            }
        })
        .catch((error) => {
            console.error("Errore durante la creazione dell'Assignment:", error);
            alert("Errore durante la creazione dell'Assignment. Riprova.");
        });
    });
}

// Funzione per popolare il menu a tendina dei giochi
function populateGameSelector() {
    const gameSelector = document.getElementById("gameSelector");

    fetch("/elencoNomiClassiUT", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error("Errore nel recupero dei giochi");
        }
        return response.json();
    })
    .then((games) => {
        gameSelector.innerHTML = `<option value="">Seleziona un gioco...</option>`;
        games.forEach((game) => {
            const option = document.createElement("option");
            option.textContent = game; // Poiché 'game' è già una stringa, la usiamo direttamente
            gameSelector.appendChild(option);
        });
    })
    .catch((error) => {
        console.error("Errore nel caricamento dei giochi:", error);
        gameSelector.innerHTML = `<option value="">Errore nel caricamento dei giochi</option>`;
    });
}

document.getElementById("openModalButton").addEventListener("click", () => {
    console.log("Pulsante 'Add Student' premuto. Stato toggle:", toggleSwitch.checked);
});

document.getElementById("openModalButtonAssignment").addEventListener("click", () => {
    console.log("Pulsante 'Add Assignment' premuto. Stato toggle:", toggleSwitch.checked);
});




// Funzione per aprire la modale
document.getElementById("editIcon").addEventListener("click", function() {
    // Crea la modale dinamicamente
    const modalContainer = document.createElement("div");
    modalContainer.classList.add("modal");
    modalContainer.id = "modalContainer";

    const modalContent = document.createElement("div");
    modalContent.classList.add("modal-content");

    // Aggiungi la parte di contenuto della modale
    const closeButton = document.createElement("span");
    closeButton.classList.add("close");
    closeButton.innerHTML = "&times;";  // Simbolo della 'X' per chiudere la modale

    const title = document.createElement("h2");
    title.innerText = "Modify Team Name";

    const form = document.createElement("form");
    form.id = "teamNameForm";

    const label = document.createElement("label");
    label.setAttribute("for", "newTeamName");
    label.innerText = "New Team Name:";

    const input = document.createElement("input");
    input.type = "text";
    input.id = "newTeamName";
    input.name = "newTeamName";
    input.placeholder = "Enter new team name";

    const submitButton = document.createElement("button");
    submitButton.id="button-team";
    submitButton.type = "submit";
    submitButton.innerText = "Save";

    // Aggiungi gli elementi alla modale
    form.appendChild(label);
    form.appendChild(input);
    form.appendChild(submitButton);

    modalContent.appendChild(closeButton);
    modalContent.appendChild(title);
    modalContent.appendChild(form);

    modalContainer.appendChild(modalContent);

    // Aggiungi la modale al body
    document.body.appendChild(modalContainer);

    // Mostra la modale
    modalContainer.style.display = "block";

    // Funzione per chiudere la modale
    closeButton.addEventListener("click", function() {
        document.body.removeChild(modalContainer);
    });

    // Funzione per salvare il nuovo nome del team
    form.addEventListener("submit", function(event) {
        event.preventDefault(); // Impedisce il refresh della pagina
        const teamId = getTeamCodeFromPath();
        const newName = input.value;
        if (newName) {
            
            // Crea l'oggetto da inviare al server
            const requestData = {
                idTeam: teamId, // Sostituisci con l'ID del team (deve essere disponibile nello scope)
                newName: newName
            };
        
            // Effettua la richiesta PUT
            fetch('/modificaNomeTeam', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestData)  // Converte l'oggetto in JSON, // Converte l'oggetto in JSON
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Errore HTTP: ${response.status}`);
                    }
                    return response.json(); // Elabora la risposta
                })
                .then(data => {
                    document.getElementById("title").innerText =newName;
                })
                .catch(error => {
                    console.error('Errore durante la modifica del nome del team:', error);
                    alert('Si è verificato un errore durante la modifica del nome del team. Riprova.');
                });
        
            // Chiude la modale
            document.body.removeChild(modalContainer);
        }
        
    });
});


function deleteStudent(event) {
    // Verifica se il toggle text è "Students"
    const toggleText = document.getElementById("toggleText").textContent.trim().toLowerCase();
    if (toggleText !== "student") {
        alert("Devi essere nella sezione Students per eliminare uno studente.");
        return;
    }

    // Trova il pulsante cliccato
    const button = event.target;
    const idTeam = getTeamCodeFromPath();

    // Risali alla riga (<li> in questo caso)
    const row = button.closest(".table-row");

    // Ottieni l'ID dello studente dalla riga
    const studentId = row.querySelector(".col-1").textContent.trim();
    if (!studentId) {
        console.error("ID studente non trovato.");
        return;
    }

    // Effettua una richiesta DELETE per rimuovere lo studente
    fetch(`/rimuoviStudenteTeam/${idTeam}`, { // Modifica l'URL con il tuo endpoint backend
        method: "PUT",
        headers: {
            'Content-Type': "text/plain",
        },
        body: studentId,
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Errore durante l'eliminazione dello studente con ID: ${studentId}`);
            }
            console.log(`Studente con ID ${studentId} eliminato con successo.`);
            // Rimuovi la riga dal DOM
            const tableContainer = document.querySelector(".responsive-table");
            clearTable(tableContainer);
            populateTable();
        })
        .catch(error => {
            console.error("Errore:", error);
        });
}

function deleteAssignment(event) {
    // Verifica se il toggle text è "Assignments"
    const toggleText = document.getElementById("toggleText").textContent.trim().toLowerCase();
    if (toggleText !== "assignments") {
        alert("Devi essere nella sezione Assignments per eliminare un assignment.");
        return;
    }

    // Trova il pulsante cliccato
    const button = event.target;

    // Risali alla riga (<li> in questo caso)
    const row = button.closest(".table-row");

    // Ottieni l'ID dell'assignment dalla riga
    const assignmentId = row.querySelector(".col-1").textContent.trim();
    if (!assignmentId) {
        console.error("ID assignment non trovato.");
        return;
    }

    // Effettua una richiesta DELETE per rimuovere l'assignment
    fetch(`/deleteAssignment/${assignmentId}`, { // Modifica l'URL con il tuo endpoint backend
        method: "DELETE",
        headers: {
            'Content-Type': "application/json",
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Errore durante l'eliminazione dell'assignment con ID: ${assignmentId}`);
            }
            console.log(`Assignment con ID ${assignmentId} eliminato con successo.`);
            // Rimuovi la riga dal DOM
            const tableContainer = document.querySelector("#assignmentsContainer .responsive-table");
            clearTable(tableContainer);
            populateTableAssignment();
        })
        .catch(error => {
            console.error("Errore:", error);
        });
}





// Aggiungi un event listener ai pulsanti "Delete"
document.querySelector("#studentsContainer .responsive-table").addEventListener("click", event => {
    if (event.target.classList.contains("delete-button")) {
        deleteStudent(event);
    }
});

// Aggiungi un event listener ai pulsanti "Delete"
document.querySelector("#assignmentsContainer .responsive-table").addEventListener("click", event => {
    if (event.target.classList.contains("delete-button")) {
        deleteAssignment(event);
    }
});
function clearTable(container) {
    // Mantieni solo la prima riga (intestazioni della tabella)
    const headerRow = container.querySelector(".table-header");
    container.innerHTML = ''; // Pulisce tutto il contenuto
    if (headerRow) {
        container.appendChild(headerRow); // Ripristina la riga delle intestazioni
    }
}

document.getElementById("backArrow").addEventListener("click", () => {
    // Reindirizza alla pagina desiderata
    window.location.href = "/teams"; 
});
