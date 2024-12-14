
///------ZONA FOLDER E RELATIVE FUNZIONI -----

// Funzione di visualizzazione dei Team con filtro
function folder_view(orderBy = "default") {
    // Simulazione fetch per ottenere i team
    fetch("/visualizzaTeams", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
    .then((response) => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            
            // Verifica se la risposta è in formato JSON
            const contentType = response.headers.get("Content-Type");
            if (contentType && contentType.includes("application/json")) {
                return response.json(); // Se la risposta è JSON, la parsifichiamo come JSON
            } else {
                return response.text(); // Altrimenti, trattiamo la risposta come testo
            }
        })
        .then((data) => {
            // Se la risposta è un messaggio di errore (testo)
        // Rimuovi il messaggio "Non hai ancora effettuato un assignment" se esiste
            const existingNoFolder = document.getElementById("noFolder");
            if (existingNoFolder) {
                existingNoFolder.remove();
            }
            if (typeof data === "string") {
                const folderContainer = document.getElementById("folders");
                // Ottieni il contenitore principale della sezione
                const FolderSection = document.getElementById('team-section');
                const noFolder  = document.createElement("div");
                noFolder.classList = "noFolder";
                noFolder.id="noFolder";
                noFolder.textContent= data;
                // Inserisci il nuovo elemento prima di assignment-folder-container
                FolderSection.insertBefore(noFolder, folderContainer);
                        
                return;
            }

    
            // Ordinamento
            if(Array.isArray(data)){
            if (orderBy === "alphabetical") {
                data.sort((a, b) => a.name.localeCompare(b.name));
                console.log("Dati dopo l'ordinamento alfabetico:", data);
            } else if (orderBy === "date") {
                data.sort((a, b) => new Date(a.creationDate) - new Date(b.creationDate));
            }
            console.log("Dati ordinati team:", data);
    
            // Rendering cartelle
            renderFolders(data);
        }
        })
    
        .catch((error) => {
            console.error("Errore durante la fetch:", error);
            alert(error.message);
        });
    }
function renderFolders(data){
    const folderContainer = document.getElementById("folders");
    folderContainer.innerHTML = ""; // Pulisce i folder precedenti
        
        // Creazione dinamica delle cartelle
     data.forEach((item) => {
            const folder = document.createElement("div");
            folder.className = "folder";
            
            const foldercontent = document.createElement("div");
            foldercontent.classList.add("folder-content");

            const img = document.createElement("img");
            img.src = "/t1/css/Images/icon_team.png";
            img.alt = item.name;

            const span = document.createElement("span");
            span.textContent = item.name;

            const info1 = document.createElement("p");
            // Formattazione della data
            const formattedDate = new Date(item.creationDate).toLocaleDateString('it-IT');
            info1.textContent = `Creato il: ${formattedDate }`;
            info1.classList.add("folder-info");
            info1.id="info-date";

            const info2 = document.createElement("p");
            if (item.numStudenti ===0) {
                info2.textContent = `Totale studenti: -`;
            } else {
                console.log(item.numStudenti);

                info2.textContent = `Totale studenti: ${item.numStudenti}`;
            }
            
            info2.classList.add("folder-info");
            info2.id="info-student";
            const info3 = document.createElement("p");
            info3.textContent=`Codice Team: ${item.idTeam}`;
            info3.id="info-id";
            info3.classList.add("folder-info");
            const deleteButton = document.createElement("button");
            deleteButton.classList.add("delete-button");
            deleteButton.textContent = "Delete";

            foldercontent.appendChild(img);
            foldercontent.appendChild(span);
            folder.appendChild(foldercontent);
            folder.appendChild(info1);
            folder.appendChild(info2);
            folder.appendChild(info3);
            folder.appendChild(deleteButton);
            folderContainer.appendChild(folder);
 
            // Event listener per eliminazione
            deleteButton.addEventListener("click", (event) => {
                event.stopPropagation();
                if (confirm(`Sei sicuro di voler eliminare ${item.name}?`)) {
                    
                    fetch(`/deleteTeam`, {
                        method: "DELETE",
                        headers: { "Content-Type": "text/plain" },
                        body: item.idTeam // Invia la stringa direttamente
                    })
                    .then((response) => {
                        if (response.ok) {
                            console.log(`Cartella ${item.name} eliminata.`);
                        
                            deleteAssignmentsByTeamName(item.name); 
                            
                            view_assignments();
                            folder.remove();
                            folder_view();
                            
                            
                        } else {
                            return response.text().then(errorMessage => {
                                throw new Error(errorMessage);
                            });
                        }
                    })
                    .catch(
                        (error) => console.error("Errore di rete:", error));
                }
            });
            //rimando alla pagina del team specifico
            folder.addEventListener("click", () => {
                const teamId = item.idTeam;  // Prendi l'ID del team
                window.location.href = `/visualizzaTeam/${teamId}`;  // Reindirizza alla pagina di dettagli, passando l'ID
            });
        });
          
    }

    // Event listener per cambiare l'ordinamento
document.getElementById("dropdown-container").addEventListener("change", (event) => {
    const orderBy = event.target.value; // Recupera il valore selezionato
    folder_view(orderBy); // Ricarica la vista con il nuovo ordinamento
});


// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => folder_view());


function showLoadingScreen() {
    document.getElementById("loadingScreen").style.display = "flex";
}

function hideLoadingScreen() {
    document.getElementById("loadingScreen").style.display = "none";
}

// --- BARRA DI RICERCA---
function searchTeamByName(name) {
    const folderContainer = document.getElementById("folders");
    const folders = folderContainer.querySelectorAll(".folder");
    let found = false;

    // Svuota il contenitore prima di aggiungere il risultato
    folderContainer.innerHTML = "";

    folders.forEach((folder) => {
        const teamName = folder.querySelector("span").textContent.trim();
        const info_id = folder.querySelector("#info-id").textContent;
        const teamId = info_id.split(":")[1].trim();
        if (teamName.toLowerCase() === name.toLowerCase()) {
            found = true;
           // Clona il folder e aggiungilo al container
            const newFolder = folder.cloneNode(true);
            folderContainer.appendChild(newFolder);

             // Mostra gli assignment relativi a questo team
             displayAssignmentsForTeam(teamName);
            // Aggiungi l'evento click direttamente sul nuovo folder
            newFolder.addEventListener("click", () => {
                window.location.href = `/visualizzaTeam/${teamId}`;  // Reindirizza alla pagina di dettagli, passando l'ID
            });
        }
        
    });
    if (!found) {
        alert("Nessun team trovato con questo nome.");
        folderContainer.innerHTML = "<p>Nessun risultato trovato.</p>";
    }
}


// Event listener per il pulsante di ricerca
document.querySelector(".search-button").addEventListener("click", () => {
    const searchInput = document.querySelector(".search-bar").value.trim();
    if (searchInput) {
        searchTeamByName(searchInput);
    } else {
        alert("Inserisci un nome per cercare.");
    }
});

function resetFolders() {
    folder_view(); // Richiama la funzione per mostrare tutti i team
}


// Event listener per il clic nello spazio vuoto
document.getElementById("folders").addEventListener("click", (event) => {
    // Se l'utente clicca nello spazio vuoto (non su un folder)
    if (event.target.id === "folders") {
        resetFolders();
        resetAssignments();
    }
});




//  ----- ZONA FINESTRA MODALE TEAM -----

// Apertura finestra modale
function openModalTeam() {
    const modalContainer = document.getElementById("modalContainerTeam");
    modalContainer.innerHTML = `
        <div class="modal_team">
            <div class="modal_team-content">
                <span class="close">&times;</span>
                <h2>Crea Nuovo Team</h2>
                <form id="teamForm">
                    <label for="teamName">Nome del Team:</label>
                    <input type="text" id="teamName" name="teamName" required>
    
                    <button type="button" id="createTeamButton">Crea Team</button>
                </form>
            </div>
        </div>
    `;

    const modal = document.querySelector(".modal_team");
    const closeButton = document.querySelector(".close");

    modal.style.display = "block";

    // Chiudi la modale
    closeButton.onclick = () => (modal.style.display = "none");

    window.onclick = (event) => {
        if (event.target == modal) modal.style.display = "none";
    };

    // Aggiungi il listener per il pulsante "Crea Team"
    const createTeamButton = document.getElementById("createTeamButton");
    createTeamButton.addEventListener("click", () => createTeam(modal));
    // Limita i caratteri nel campo di input "Nome del Team" a 15
    const teamNameInput = document.getElementById("teamName");
    teamNameInput.addEventListener("input", (event) => {
        if (event.target.value.length > 15) {
            event.target.value = event.target.value.substring(0, 15); // Troncamento a 15 caratteri
        }
    });
}

function createTeam(modal) {
    // Ottieni i dati dagli input
    const teamName = document.getElementById("teamName").value.trim();
    // Validazione dei dati
    if (!teamName) {
        alert("Campo sono obbligatorio!");
        return;
    }

    // Verifica lunghezza del nome del team
    if (teamName.length < 3 || teamName.length > 30) {
        alert("Il nome del team deve essere tra 3 e 30 caratteri.");
        return;
    }
    showLoadingScreen();  
    // Effettua la richiesta al backend
    fetch("/creaTeam", { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include', // Include i cookie (JWT) se necessario
        body: JSON.stringify({
            name: teamName,
        })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.text().then(errorMessage => {
                throw new Error(errorMessage || "Errore nella creazione del team.");
            });
        }
    })
    .then(data => {
        // Successo
        alert("Team creato con successo!");
        console.log("Risultato della creazione:", data);

        // Chiudi la modale
        if (modal) {
            modal.style.display = "none";
        }
        if (typeof folder_view === 'function') {
            folder_view();
        }
    })
    .catch(error => {
        // Gestione errori
        console.error("Errore durante la creazione del team:", error);
        alert(`Errore: ${error.message}`);
    }).finally(() => {
        // Nascondi la schermata di caricamento sempre, alla fine della richiesta
        hideLoadingScreen();
    });
}


document.getElementById('viewAllLink-Team').addEventListener('click', function() {
    // Ottieni tutte le cartelle (team)
    var folders = document.querySelectorAll('.folder-container .folder');
    
    // Se il testo è "View All", mostra tutti i team
    if (this.textContent === 'View All') {
      folders.forEach(function(folder, index) {
        if (index >= 3) {
          folder.style.display = 'flex'; // Mostra tutti i team oltre il terzo
        }
      });
      
      this.textContent = 'View Less'; // Cambia il testo in "View Less"
    } 
    // Se il testo è "View Less", nasconde i team dopo il terzo
    else if (this.textContent === 'View Less') {
      folders.forEach(function(folder, index) {
        if (index >= 3) {
          folder.style.display = 'none'; // Nasconde i team oltre il terzo
        }
      });
      
      this.textContent = 'View All'; // Cambia il testo in "View All"
    }
  });


// Event listener per il bottone modale
document.getElementById("openModalButtonTeam").addEventListener("click", openModalTeam);


// ---- ZONA ASSIGNMENT E RELATIVE FUNZIONI ----

function view_assignments(orderBy = "default") {
    // Effettua una fetch per ottenere gli assignment
    fetch("/visualizzaAssignments", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
    .then((response) => {
        if (!response.ok) {
            return response.text().then(errorMessage => {
                throw new Error(errorMessage);
            });
        }
        
        // Verifica se la risposta è in formato JSON
        const contentType = response.headers.get("Content-Type");
        if (contentType && contentType.includes("application/json")) {
            return response.json(); // Se la risposta è JSON, la parsifichiamo come JSON
        } else {
            return response.text(); // Altrimenti, trattiamo la risposta come testo
        }
    })
    .then((data) => {
        // Se la risposta è un messaggio di errore (testo)
        // Rimuovi il messaggio "Non hai ancora effettuato un assignment" se esiste
        const existingNoAssignment = document.getElementById("noAssignment");
        if (existingNoAssignment) {
            existingNoAssignment.remove();
        }
        if (typeof data === "string") {
            const folderContainer = document.getElementById("assignment-folder-container");
            // Ottieni il contenitore principale della sezione
            const assignmentSection = document.getElementById('assignment-section');
            const noAssignment  = document.createElement("div");
            noAssignment.classList = "noAssignment";
            noAssignment.id="noAssignment";
            noAssignment.textContent= data;
            // Inserisci il nuovo elemento prima di assignment-folder-container
            assignmentSection.insertBefore(noAssignment, folderContainer);
                    
            return;
        }

        // Se la risposta è un oggetto JSON (i dati degli assignment)
        if (Array.isArray(data)) {
            // Ordinamento
            console.log("Valore di orderBy:", orderBy);
            if (orderBy === "end") {
                data.sort((a, b) => new Date(a.dataScadenza) - new Date(b.dataScadenza));

            }else if( orderBy==="creation"){
                data.sort((a, b) => new Date(a.dataCreazione) - new Date(b.dataCreazione));
            }
            console.log("Dati ordinati:", data);

            // Rendering cartelle per gli assignment
            renderAssignmentFolders(data);
        }
    })
    .catch((error) => {
        console.error("Errore durante la fetch:", error);
        alert(error.message);
    });
}


function renderAssignmentFolders(data) {
    const folderContainer = document.getElementById("assignment-folder-container");
    folderContainer.innerHTML = ""; // Pulisce i folder precedenti
    
    // Creazione dinamica delle cartelle per gli assignment
    data.forEach((item) => {
        const folder = document.createElement("div");
        folder.className = "assignment-folder";

        const folderContent = document.createElement("div");
        folderContent.classList.add("assignment-folder-content");

        const img = document.createElement("img");
        img.src = "/t1/css/Images/pencil.png";
        img.alt = item.name;

        const span = document.createElement("span");
        span.textContent = item.titolo;

        const info1 = document.createElement("p");
        // Formattazione della data
        const formattedDateCreazione = new Date(item.dataCreazione).toLocaleDateString('it-IT');
        info1.textContent = `Data Creazione: ${formattedDateCreazione}`;
        info1.classList.add("assignment-folder-info");
        info1.id = "info-date";

        // Formattazione della data
        const info2 = document.createElement("p");
        const formattedDateScadenza = new Date(item.dataScadenza).toLocaleDateString('it-IT');
        info2.textContent = `Data Scadenza: ${formattedDateScadenza}`;
        info2.classList.add("assignment-folder-info");
        info2.id = "info-date";

        const info3 = document.createElement("p");
        info3.textContent = `Classe Assegnata: `;
        info3.classList.add("assignment-folder-info");
        info3.id = "info-class";
        const info4 = document.createElement("p");
        info4.textContent=`${item.nomeTeam}`;
        info4.classList.add("assignment-folder-info");
        info4.id = "info-class";

        const deleteButton = document.createElement("button");
        deleteButton.classList.add("delete-button");
        deleteButton.textContent = "Delete";

        folderContent.appendChild(img);
        folderContent.appendChild(span);
        folder.appendChild(folderContent);
        folder.appendChild(info1);
        folder.appendChild(info3);
        folder.appendChild(info4);
        folder.appendChild(info2);
        folder.appendChild(deleteButton);
        folderContainer.appendChild(folder);

        // Event listener per eliminazione
        deleteButton.addEventListener("click", (event) => {
            event.stopPropagation();
            if (confirm(`Sei sicuro di voler eliminare l'assignment "${item.titolo}"?`)) {
                folder.remove();
                fetch(`/deleteAssignment/${item.idAssignment}`, {
                    method: "DELETE",
                    headers: { "Content-Type": "application/json" }
                })
                    .then((response) => {
                        if (response.ok) {
                            console.log(`Assignment "${item.name}" eliminato.`);
                            view_assignments();
                        } else {
                            return response.text().then(errorMessage => {
                                throw new Error(errorMessage);
                            });
                        }
                    })
                    .catch((error)=> {  
                        console.error("Errore di rete:", error)
                        alert(error.message);
                    });
            }
        });
            folder.addEventListener("click", () => {
                openAssignmentInfoWindow(item);
            });
    });
}

    // Event listener per cambiare l'ordinamento
    document.getElementById("dropdown-container-assignment").addEventListener("change", (event) => {
        const orderBy = event.target.value; // Recupera il valore selezionato
        view_assignments(orderBy); // Ricarica la vista con il nuovo ordinamento
    });

    // Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => view_assignments());


// Event listener per "View All" / "View Less"
document.getElementById('viewAllLink-Assignment').addEventListener('click', function() {
    // Ottieni tutte le cartelle (assignment) all'interno del container con la classe "assignment-container"
    var folders = document.querySelectorAll('.assignment-container .assignment-folder');
    
    // Se il testo è "View All", mostra tutti gli assignment
    if (this.textContent === 'View All') {
        folders.forEach(function(folder, index) {
            if (index >= 3) {
                folder.style.display = 'flex'; // Mostra tutti gli assignment oltre il terzo
            }
        });
        
        this.textContent = 'View Less'; // Cambia il testo in "View Less"
    } 
    // Se il testo è "View Less", nasconde gli assignment dopo il terzo
    else if (this.textContent === 'View Less') {
        folders.forEach(function(folder, index) {
            if (index >= 3) {
                folder.style.display = 'none'; // Nasconde gli assignment oltre il terzo
            }
        });
        
        this.textContent = 'View All'; // Cambia il testo in "View All"
    }
});

function deleteAssignmentsByTeamName(teamName) {
    const folderContainer = document.getElementById("assignment-folder-container");
    const folders = folderContainer.querySelectorAll(".assignment-folder"); // Seleziona tutti i folder

    folders.forEach((folder, index) => {
        const infoClassElement = folder.querySelector("#info-class"); // Seleziona l'elemento con id info-class

        // Debug: log per verificare la presenza di info-class
        if (!infoClassElement) {
            console.error(`Elemento con ID 'info-class' non trovato in folder ${index + 1}`);
            return; // Salta questo folder se manca
        }

        const teamNameInFolder = infoClassElement.textContent.trim().replace("Classe Assegnata: ", "");

        // Debug: log per verificare i valori confrontati
        console.log(`Folder ${index + 1}: Nome del team in info-class = "${teamNameInFolder}"`);

        if (teamNameInFolder.toLowerCase() === teamName.toLowerCase()) {
            console.log(`Rimuovendo folder ${index + 1} per il team "${teamName}"`);
            folder.remove(); // Rimuovi il folder
        }
    });
}



// Funzione che cerca l'ID del team in base al nome
function getTeamIdByName(teamName) {
    const folderContainer = document.getElementById("folders");
    const folders = folderContainer.querySelectorAll(".folder");
    let teamId = null;

    folders.forEach((folder) => {
        const folderName = folder.querySelector("span").textContent.trim();
        if (folderName.toLowerCase() === teamName.toLowerCase()) {
            const info_id = folder.querySelector("#info-id").textContent;
            teamId = info_id.split(":")[1].trim(); // Ottieni l'ID del team
        }
    });

    return teamId; // Restituisce null se il team non viene trovato
}

// Apertura finestra modale Assignment
function openModalAssignment() {
    const modalContainer = document.getElementById("modalContainerAssignment");

    // HTML della modale
    modalContainer.innerHTML = `
        <div class="modal_assignment">
            <div class="modal_assignment-content">
                <span class="close_modal_assignment">&times;</span>
                <h2>Crea Nuovo Assignment</h2>
                <form id="AssignmentForm">
                    <!-- Nome del Team -->
                    <label for="teamName">Nome del Team:</label>
                    <input type="text" id="teamName" name="teamName" placeholder="Inserisci il nome del team..." required>

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
    const closeButton = document.querySelector(".close_modal_assignment");

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
        const teamName = document.getElementById("teamName").value.trim();
        const selectedGame = document.getElementById("gameSelector").value;
        const deadline = document.getElementById("deadline").value;
        const description = document.getElementById("description").value;
        console.log(deadline);
        if (!teamName || !selectedGame) {
            alert("Inserisci il nome del team e seleziona un gioco prima di procedere!");
            return;
        }
        
        // Cerca l'ID del team in base al nome
        const teamId = getTeamIdByName(teamName);
        if (!teamId) {
            alert("Team non trovato!");
            return;
        }
         // Creiamo una data con l'orario di default 00:00:00, se necessario
        const formattedDate = new Date(deadline + "T00:00:00");  // Assicurati che la data venga considerata come inizio giornata

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
                console.log("Risultato della creazione:", result);
                modal.style.display = "none";  // Chiudi la modale dopo la creazione
                view_assignments();
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
    }
    )}

// Funzione per popolare il menu a tendina dei giochi
function populateGameSelector() {
    const gameSelector = document.getElementById("gameSelector");

    fetch("/elencoNomiClassiUT", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
    .then((response) => {
        if (!response.ok) {
            return response.text().then(errorMessage => {
                throw new Error(errorMessage);
            });
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
        alert(error.message);
        gameSelector.innerHTML = `<option value="">Errore nel caricamento dei giochi</option>`;
    });
}


// --- RICERCA ASSIGNMENT PER NOME TEAM ---
function displayAssignmentsForTeam(teamName) {
    const folderContainer = document.getElementById("assignment-folder-container");
    const folders = folderContainer.querySelectorAll(".assignment-folder");
    let found = false;

    // Svuota il contenitore prima di aggiungere il risultato
    folderContainer.innerHTML = "";

    // Aggiungi log di debug
    console.log("Cercando il team:", teamName);

    folders.forEach((folder, index) => {
        const infoClassElement = folder.querySelector("#info-class"); // Seleziona l'elemento con id info-class
        
        if (infoClassElement) {
             // Estrai solo il nome del team, rimuovendo il prefisso "Classe Assegnata: "
            const teamNameInFolder = infoClassElement.textContent.trim().replace("Classe Assegnata: ", "");
            console.log(`Folder ${index + 1}: Nome del team in info-class = "${teamNameInFolder}"`);

            // Confronta il nome del team con quello cercato
            if (teamNameInFolder.toLowerCase() === teamName.toLowerCase()) {
                found = true;
                console.log(`Trovato un assignment per ${teamNameInFolder}`);

                // Clona il folder e aggiungilo al container
                const newFolder = folder.cloneNode(true);
                newFolder.style.display = 'flex'; // Mostra tutti gli assignment
                folderContainer.appendChild(newFolder);
            }
        } 
    });

    // Se non è stato trovato nessun team, visualizza un messaggio
    if (!found) {
        folderContainer.innerHTML = "<p>Nessun risultato trovato per questo team.</p>";
    }
}
function resetAssignments() {
    view_assignments(); // Richiama la funzione per mostrare tutti i team
}
// Event listener per il clic nello spazio vuoto
document.getElementById("assignment-folder-container").addEventListener("click", (event) => {
    // Se l'utente clicca nello spazio vuoto (non su un folder)
    if (event.target.id === "assignment-folder-container") {
        resetAssignments();
        resetFolders();
    }
});


document.getElementById("openModalButtonAssignment").addEventListener("click", openModalAssignment);


function openAssignmentInfoWindow(assignment) {
    // Controlla se esiste già una modale aperta
    const existingModal = document.getElementById("assignmentInfoModal");
    if (existingModal) {
        existingModal.remove(); // Rimuovi la modale precedente
    }

    // Crea il container per la finestra di visualizzazione
    const modal = document.createElement("div");
    modal.classList.add("modal_assignment_info");
    modal.id = "assignmentInfoModal";

    // Crea il contenuto della finestra modale
    const modalContent = document.createElement("div");
    modalContent.classList.add("modal_assignment_info_content");

    // Bottone di chiusura
    const closeButton = document.createElement("span");
    closeButton.classList.add("close_modal_assignment");
    closeButton.innerHTML = "&times;";
    closeButton.onclick = () => {
        modal.style.display = "none"; // Nasconde la finestra
        modal.remove(); // Rimuove la finestra dal DOM
    };

    // Titolo
    const title = document.createElement("h2");
    title.textContent = assignment.titolo;

    // Descrizione
    const description = document.createElement("p");
    description.textContent = " Descrizione: " + assignment.descrizione || "Nessuna descrizione fornita.";

    // Data di creazione
    const creationDate = document.createElement("p");
    creationDate.textContent = `Data Creazione: ${new Date(assignment.dataCreazione).toLocaleDateString('it-IT')}`;

    // Data di scadenza
    const deadline = document.createElement("p");
    deadline.textContent = `Data Scadenza: ${new Date(assignment.dataScadenza).toLocaleDateString('it-IT')}`;

    // Classe assegnata
    const team = document.createElement("p");
    team.textContent = `Classe Assegnata: ${assignment.nomeTeam}`;

    // Aggiungi gli elementi al contenuto della modale
    modalContent.appendChild(closeButton);
    modalContent.appendChild(title);
    modalContent.appendChild(description);
    modalContent.appendChild(creationDate);
    modalContent.appendChild(deadline);
    modalContent.appendChild(team);

    // Aggiungi il contenuto alla modale
    modal.appendChild(modalContent);

    // Aggiungi la modale al body
    document.body.appendChild(modal);

    // Mostra la finestra
    modal.style.display = "block";
}




