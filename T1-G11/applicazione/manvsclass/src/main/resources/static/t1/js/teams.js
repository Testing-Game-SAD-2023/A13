// Visualizzazione delle cartelle
function folder_view() {
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
            const folderContainer = document.getElementById("folders");
            folderContainer.innerHTML = ""; // Pulisce eventuali contenuti precedenti

            if (data.length === 0) {
                window.alert("Non ci sono classi");
                return;
            }

            // Creazione dinamica delle cartelle
            data.forEach((item) => {
                const folder = document.createElement("div");
                folder.className = "folder";

                const img = document.createElement("img");
                img.src = "/t1/css/Images/cartella_gialla.png";
                img.alt = item.teamName;

                const span = document.createElement("span");
                span.textContent = item.teamName;

                const deleteButton = document.createElement("button");
                deleteButton.classList.add("delete-button");
                deleteButton.textContent = "Delete";

                folder.appendChild(img);
                folder.appendChild(span);
                folder.appendChild(deleteButton);
                folderContainer.appendChild(folder);

                // Event listener per eliminazione
                deleteButton.addEventListener("click", (event) => {
                    event.stopPropagation();
                    if (confirm(`Sei sicuro di voler eliminare ${item.teamId}?`)) {
                        folder.remove();
                        fetch(`/deleteTeam`, {
                            method: "DELETE",
                            headers: { "Content-Type": "text/plain" },
                            body: item.teamId // Invia la stringa direttamente
                        })
                            .then((response) => {
                                if (response.ok) {
                                    console.log(`Cartella ${item.teamId} eliminata.`);
                                } else {
                                    console.error("Errore durante l'eliminazione.");
                                }
                            })
                            .catch((error) => console.error("Errore di rete:", error));
                    }
                });
            });
        })
        .catch((error) => {
            console.error("Errore durante la fetch:", error);
        });
}

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
                    
                    <label for="teamCode">Codice Team:</label>
                    <input type="text" id="teamCode" name="teamCode" required>
                    
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
}

function createTeam(modal) {
    // Ottieni i dati dagli input
    const teamName = document.getElementById("teamName").value.trim();
    const teamCode = document.getElementById("teamCode").value.trim();
    window.alert(teamCode)
    // Validazione dei dati
    if (!teamName || !teamCode) {
        alert("Entrambi i campi sono obbligatori!");
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
            idTeam: teamCode
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

// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", folder_view);
