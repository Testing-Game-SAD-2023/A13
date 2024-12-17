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
            return response.text().then(errorMessage => {
                throw new Error(errorMessage);
     });
        }
        return response.json();  // Parsea la risposta in formato JSON
    })
    .then(data => {
        // Puoi manipolare il DOM o fare altre operazioni con i dati ricevuti
        document.getElementById("title").textContent = `${data.name}`;
    })
    .catch(error => {
        console.error('Errore:', error);
        alert(error.message);
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

    // Event listener per cambiare l'ordinamento
    document.getElementById("dropdown-container-assignment").addEventListener("change", (event) => {
        const orderBy = event.target.value; // Recupera il valore selezionato
        populateTableAssignment(orderBy); // Ricarica la vista con il nuovo ordinamento
    });
    
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
            return response.text().then(errorMessage => {
                throw new Error(errorMessage);
            });
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
        alert(error.message);
        addEmptyRow(tableContainer); // Aggiungi la riga vuota in caso di errore
    });
}

let allAssignments = [];  // Memorizziamo tutti gli assignment di un team


function populateTableAssignment(orderBy = "default") {
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
                if (orderBy === "end") {
                    data.sort((a, b) => new Date(a.dataScadenza) - new Date(b.dataScadenza));
                }else if( orderBy==="creation"){
                    data.sort((a, b) => new Date(b.dataCreazione) - new Date(a.dataCreazione));
                }
                allAssignments = data;  // Memorizziamo tutti gli assignment
                clearTable(tableContainer);
                console.log("Dati ordinati:", data);
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
        <div class="col-5">-</div>
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
    const assignment = "-"; //relativo a quando prima o poi verranno implementata la logica relativa gli assignment
    row.innerHTML = `
        <div class="col-1">${student.id}</div>
        <div class="col-2">${name}</div>
        <div class="col-3">${surname}</div>
        <div class="col-4">${email}</div>
        <div class="col-5">${assignment}</div> 
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
        <div class="col-5"></div>
        <div class="col-delete">
            <button class="delete-button">Delete</button>
        </div>
    `;
     // Aggiungi l'event listener per la visualizzazione dei dettagli quando la riga viene cliccata
     row.addEventListener("click", () => {
        viewAssignmentDetails(row);
    });
    // Aggiungi la riga al contenitore
    container.appendChild(row);
}

function openModal() {
    // Recupera il contenitore della modale e inserisce il contenuto HTML della finestra modale
    const modalContainer = document.getElementById("modalContainer");
    modalContainer.innerHTML = `
        <div class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Aggiungi Studente</h2>
                <input type="text" id="search-name" placeholder="Cerca studente per nome..." />
                <input type="text" id="search-surname" placeholder="Cerca studente per cognome..." />
                <input type="text" id="search-email" placeholder="Cerca studente per email..." />
                <p id="selectedCount">Selezionati: 0</p>
                <div id="selectedStudents">
                <ul id="searchResults"></ul>    
                    <h3>Studenti Selezionati:</h3>
                    <ul id="selectedList"></ul>
                    <p id="viewAllMessage" style="display: none; color: #555;">...Per visionarli tutti premi View All</p>
                     <button id="viewAllButton">View All</button>
                </div>
                <button id="searchButton">Cerca</button>
                <button id="addButton" disabled>Aggiungi</button>
            </div>
        </div>

        <!-- Modale per "View All" -->
        <div id="viewAllModal" class="modal" style="display: none;">
            <div class="modal-content">
                <span id="viewAllClose" class="close">&times;</span>
                <h2>Elenco Completo Studenti Selezionati</h2>
                <ul id="fullSelectedList"></ul>
            </div>
        </div>

    `;

    const searchNameInput = document.getElementById("search-name");
    const searchSurnameInput = document.getElementById("search-surname");
    const searchEmailInput = document.getElementById("search-email");
    const searchResults = document.getElementById("searchResults");
    const selectedList = document.getElementById("selectedList");
    const selectedCount = document.getElementById("selectedCount");
    const viewAllMessage = document.getElementById("viewAllMessage");
    const viewAllButton = document.getElementById("viewAllButton");
    const addButton = document.getElementById("addButton");

    let selectedStudents = [];

    // Funzione per aggiornare la lista visibile degli studenti selezionati
    function updateSelectedList() {
        selectedList.innerHTML = '';
        const maxVisible = 5;
        fullSelectedList.innerHTML = selectedStudents.map(student => `<li>${student.name} ${student.surname} </li>`).join('');
        // Mostra solo i primi 5 studenti
        selectedStudents.slice(0, maxVisible).forEach(student => {
            const li = document.createElement('li');
            li.textContent = `${student.name} ${student.surname}`;
            selectedList.appendChild(li);
        });

        // Mostra il messaggio se ci sono più di 5 studenti
        if (selectedStudents.length > maxVisible) {
            viewAllMessage.style.display = 'block';
        } else {
            viewAllMessage.style.display = 'none';
        }
    }

    // Funzione per cercare studenti
    function searchStudents() {
        const nameQuery = searchNameInput.value.toLowerCase().trim();
        const surnameQuery = searchSurnameInput.value.toLowerCase().trim();
        const emailQuery = searchEmailInput.value.toLowerCase().trim();

        // Reset campi di input
        searchNameInput.value = '';
        searchSurnameInput.value = '';
        searchEmailInput.value = '';

        if (!nameQuery && !surnameQuery && !emailQuery) {
            searchResults.innerHTML = '<li>Ricerca almeno uno studente!</li>';
            return;
        }

        fetch(`/searchStudents`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: nameQuery, surname: surnameQuery, email: emailQuery })
        })
            .then(response => {
                if (!response.ok){
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
             });
                }
                return response.json();
            })
            .then(filteredStudents => {
                if (filteredStudents.length > 0) {
                    searchResults.innerHTML = filteredStudents.map(student => {
                        const isSelected = selectedStudents.some(s => s.id === student.id);
                        return `
                            <li>
                                <input 
                                    type="checkbox" 
                                    data-id="${student.id}" 
                                    data-name="${student.name}" 
                                    data-surname="${student.surname}" 
                                    ${isSelected ? 'checked' : ''} 
                                />
                                ${student.name} ${student.surname} (${student.email})
                            </li>
                        `;
                    }).join('');
                } else {
                    searchResults.innerHTML = '<li>Nessuno studente trovato.</li>';
                }

                // Aggiunge un listener ai checkbox
                document.querySelectorAll('#searchResults input[type="checkbox"]').forEach(checkbox => {
                    checkbox.addEventListener('change', (e) => {
                        const studentId = e.target.dataset.id;
                        const studentName = e.target.dataset.name;
                        const studentSurname = e.target.dataset.surname;

                        if (e.target.checked) {
                            if (!selectedStudents.some(s => s.id === studentId)) {
                                selectedStudents.push({ id: studentId, name: studentName, surname: studentSurname});
                            }
                        } else {
                            selectedStudents = selectedStudents.filter(s => s.id !== studentId);
                        }

                        selectedCount.textContent = `Selezionati: ${selectedStudents.length}`;
                        updateSelectedList();
                        addButton.disabled = selectedStudents.length === 0;
                    });
                });
            })
            .catch(error => {
                console.error('Errore nella fetch:', error);
                searchResults.innerHTML = '<li>Si è verificato un errore. Riprova.</li>';
            });
    }

    document.getElementById("searchButton").addEventListener("click", searchStudents);

    // Funzione per aggiungere gli studenti al team
    function addStudentsToTeam() {
        console.log(selectedStudents);
        const idTeam = getTeamCodeFromPath();
        if (!selectedStudents || selectedStudents.length === 0) {
            return;
        }
        const requestBody = JSON.stringify(selectedStudents.map(s => s.id));
        console.log(requestBody);
        showLoadingScreen();   
        fetch(`/aggiungiStudenti/${idTeam}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: requestBody,
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
             });
                }
                return response.json();
            })
            .then(data => {
                alert('Studenti aggiunti con successo al team!');
                console.log('Risposta del server:', data);
                const tableContainer = document.querySelector("#studentsContainer .responsive-table");
                clearTable(tableContainer);
                populateTable();
            })
            .catch(error => {
                console.error('Errore nella fetch:', error);
                alert('Si è verificato un errore durante l\'aggiunta degli studenti. Riprova.');
            }).finally(() => {
                // Nascondi la schermata di caricamento sempre, alla fine della richiesta
                hideLoadingScreen();
            });
        resetModal();
        modal.style.display = "none";
    }

    addButton.addEventListener("click", addStudentsToTeam);
    viewAllButton.addEventListener('click', () => {
        viewAllModal.style.display = 'block';
    });

    viewAllClose.addEventListener('click', () => {
        viewAllModal.style.display = 'none';
    });
    // Funzione per chiudere la modale
    const closeButton = document.querySelector(".close");
    const modal = document.querySelector(".modal");
    closeButton.onclick = () => modal.style.display = "none";

    window.onclick = (event) => {
        if (event.target == modal) modal.style.display = "none";
        if (event.target === viewAllModal) viewAllModal.style.display = "none";
    };

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

        // Mostra la schermata di caricamento prima di inviare la richiesta
        showLoadingScreen();    
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
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
         });
            }
            return response.text();
        })
        .then((result) => {
            // La risposta è una stringa che contiene il messaggio
            if (result === "Assignment creato con successo e associato al Team.") {
                alert(result); // Mostriamo il messaggio di successo
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
                alert(error.message);
        }).finally(() => {
            // Nascondi la schermata di caricamento sempre, alla fine della richiesta
            hideLoadingScreen();
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
                        // Leggi il messaggio di errore dal server
                    return response.text().then(errorMessage => {
                       throw new Error(errorMessage);
            });
                    }
                    return response.json(); // Elabora la risposta
                })
                .then(data => {
                    document.getElementById("title").innerText =newName;
                })
                .catch(error => {
                    console.error('Errore durante la modifica del nome del team:', error);
                    alert(error.message); // Mostra il messaggio di errore specifico del server
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
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
         });
            }
            console.log(`Studente con ID ${studentId} eliminato con successo.`);
            // Rimuovi la riga dal DOM
            const tableContainer = document.querySelector(".responsive-table");
            clearTable(tableContainer);
            populateTable();
        })
        .catch(error => {
            console.error("Errore:", error);
            alert(error.message);
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
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
         });
            }
            console.log(`Assignment con ID ${assignmentId} eliminato con successo.`);
            // Rimuovi la riga dal DOM
            const tableContainer = document.querySelector("#assignmentsContainer .responsive-table");
            clearTable(tableContainer);
            populateTableAssignment();
        })
        .catch(error => {
            console.error("Errore:", error);
            alert(error.message);
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

function showLoadingScreen() {
    document.getElementById("loadingScreen").style.display = "flex";
}

function hideLoadingScreen() {
    document.getElementById("loadingScreen").style.display = "none";
}
// Funzione per visualizzare i dettagli di un assignment quando viene cliccato
function viewAssignmentDetails(row) {
    // Modifica il selettore in base alla struttura effettiva della tua tabella
    const assignmentId = row.querySelector(".col-1").textContent.trim();
    console.log(allAssignments);
    console.log(assignmentId);

    // Cerca l'assignment corrispondente nell'array allAssignments
    const assignment = allAssignments.find(assign => assign.idAssignment === assignmentId);
    if (assignment) {
        openModalWithDescription(assignment);
    } else {
        alert("Assignment non trovato.");
    }
}

// Funzione per aprire la modale con la descrizione dell'assignment
function openModalWithDescription(assignment) {
    const modalInfo = document.getElementById("modalContainerAssignmentInfo");
    const modalContainer = document.createElement("div");
    modalContainer.classList.add("modal_assignment_info");
    modalContainer.style.display="flex";
    modalContainer.innerHTML = `
        <div class="modal-content-info-assignment">
            <span class="close-modal-info-assignment">&times;</span>
            <h2>Titolo dell'Assignment:</h2>
            <h2> ${assignment.titolo}</h2>
            <p><strong>Descrizione:</strong></p>
            <p><strong></strong> ${assignment.descrizione}</p>
        </div>
    `;
    modalInfo.appendChild(modalContainer);

    // Aggiungi il comportamento di chiusura della modale
    const closeButton = modalContainer.querySelector(".close-modal-info-assignment");
    closeButton.addEventListener("click", () => modalContainer.remove());
}

