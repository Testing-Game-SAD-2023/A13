
///------ZONA FOLDER E RELATIVE FUNZIONI -----

// Funzione di visualizzazione delle cartelle con filtro
function folder_view(orderBy = "default") {
    // Simulazione fetch per ottenere i team
    fetch("/visualizzaTeams", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            if (data.length === 0) {
                window.alert("Non ci sono classi");
                return;
            }
            console.log("Dati originali ricevuti:", data);
    
            // Ordinamento
            if (orderBy === "alphabetical") {
                data.sort((a, b) => a.name.localeCompare(b.name));
                console.log("Dati dopo l'ordinamento alfabetico:", data);
            } else if (orderBy === "date") {
                data.sort((a, b) => new Date(a.creationDate) - new Date(b.creationDate));
            }
            console.log("Dati ordinati:", data);
    
            // Rendering cartelle
            renderFolders(data);
        })
        .catch((error) => {
            console.error("Errore durante la fetch:", error);
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
            if(!item.numeroStudenti){
                info2.textContent = `Totale studenti: -`;
            }else{
                info2.textContent = `Totale studenti: ${item.numeroStudenti}`;
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
                    folder.remove();
                    fetch(`/deleteTeam`, {
                        method: "DELETE",
                        headers: { "Content-Type": "text/plain" },
                        body: item.idTeam // Invia la stringa direttamente
                    })
                    .then((response) => {
                        if (response.ok) {
                            console.log(`Cartella ${item.teamName} eliminata.`);
                        } else {
                            console.error("Errore durante l'eliminazione.");
                        }
                    })
                    .catch((error) => console.error("Errore di rete:", error));
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


function resetFolders() {
    folder_view(); // Richiama la funzione per mostrare tutti i team
}
// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => folder_view());



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


function resetFolders() {
    folder_view(); // Richiama la funzione per mostrare tutti i team
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

// Event listener per il clic nello spazio vuoto
document.getElementById("folders").addEventListener("click", (event) => {
    // Se l'utente clicca nello spazio vuoto (non su un folder)
    if (event.target.id === "folders") {
        resetFolders();
    }
});




//  ----- ZONA FINESTRA MODALE -----

// Apertura finestra modale
function openModal() {
    const modalContainer = document.getElementById("modalContainer");
    modalContainer.innerHTML = `
        <div class="modal">
            <div class="modal-content">
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

    const modal = document.querySelector(".modal");
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
    });
}



// Event listener per il bottone modale
document.getElementById("openModalButton").addEventListener("click", openModal);

