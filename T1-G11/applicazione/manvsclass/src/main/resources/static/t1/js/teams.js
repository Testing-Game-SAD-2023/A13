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
                img.alt = item.className;

                const span = document.createElement("span");
                span.textContent = item.className;

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
                    if (confirm(`Sei sicuro di voler eliminare ${item.className}?`)) {
                        folder.remove();
                        fetch(`url_api/elimina/${item.id}`, {
                            method: "DELETE",
                            headers: { "Content-Type": "application/json" }
                        })
                            .then((response) => {
                                if (response.ok) {
                                    console.log(`Cartella ${item.className} eliminata.`);
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
                    
                    <label for="studentSearch">Cerca Studenti:</label>
                    <div class="search-container">
                        <input type="text" id="studentSearch" placeholder="Nome o Cognome">
                        <button type="button" id="searchButton">Cerca</button>
                    </div>
                    <div class="search-results" id="searchResults"></div>
                    <div id="selectedStudents"></div>
                    <button type="submit">Crea Team</button>
                </form>
            </div>
        </div>
    `;

    const modal = document.querySelector(".modal");
    const closeButton = document.querySelector(".close");

    modal.style.display = "block";

    closeButton.onclick = () => (modal.style.display = "none");

    window.onclick = (event) => {
        if (event.target == modal) modal.style.display = "none";
    };

    setupSearch(); // Configura la ricerca
}

// Configurazione della ricerca
function setupSearch() {
    const searchInput = document.getElementById("studentSearch");
    const searchButton = document.getElementById("searchButton");
    const resultsContainer = document.getElementById("searchResults");
    const selectedStudentsContainer = document.createElement("div");
    selectedStudentsContainer.id = "selectedStudents";
    selectedStudentsContainer.style.marginTop = "10px";
    resultsContainer.parentElement.appendChild(selectedStudentsContainer);

    const selectedStudents = new Set(); // Set per evitare duplicati

    const performSearch = () => {
        const query = searchInput.value.trim();
        resultsContainer.innerHTML = "";
        if (query.length === 0) {
            resultsContainer.style.display = "none";
            return;
        }

        // Simula una fetch al database
        fetch(`url_api/students?search=${query}`)
            .then((response) => response.json())
            .then((data) => {
                resultsContainer.innerHTML = "";
                resultsContainer.style.display = data.length > 0 ? "block" : "none";

                if (data.length === 0) {
                    resultsContainer.innerHTML = `<div>Nessun risultato trovato</div>`;
                    return;
                }

                data.forEach((student) => {
                    const result = document.createElement("div");
                    result.className = "search-result";
                    result.innerHTML = `
                        <input type="checkbox" id="student_${student.id}" value="${student.id}">
                        <label for="student_${student.id}">${student.firstName} ${student.lastName}</label>
                    `;

                    // Aggiungi evento al checkbox per selezionare lo studente
                    const checkbox = result.querySelector("input[type='checkbox']");
                    checkbox.addEventListener("change", () => {
                        if (checkbox.checked) {
                            selectedStudents.add(student.id);

                            const selected = document.createElement("div");
                            selected.className = "selected-student";
                            selected.textContent = `${student.firstName} ${student.lastName}`;
                            selected.dataset.studentId = student.id;

                            // Pulsante per rimuovere uno studente selezionato
                            const removeButton = document.createElement("button");
                            removeButton.textContent = "Rimuovi";
                            removeButton.className = "remove-student";
                            removeButton.addEventListener("click", () => {
                                selectedStudents.delete(student.id);
                                selected.remove();
                                checkbox.checked = false; // Deseleziona il checkbox
                            });

                            selected.appendChild(removeButton);
                            selectedStudentsContainer.appendChild(selected);
                        } else {
                            // Rimuovi lo studente dal set se il checkbox viene deselezionato
                            selectedStudents.delete(student.id);

                            const toRemove = [...selectedStudentsContainer.children].find(
                                (el) => el.dataset.studentId == student.id
                            );
                            if (toRemove) toRemove.remove();
                        }
                    });

                    resultsContainer.appendChild(result);
                });
            })
            .catch((error) => {
                console.error("Errore durante la ricerca:", error);
                resultsContainer.innerHTML = `<div>Errore durante la ricerca</div>`;
            });
    };

    // Assegna l'evento di ricerca al pulsante
    searchButton.addEventListener("click", performSearch);
}


// Event listener per il bottone modale
document.getElementById("openModalButton").addEventListener("click", openModal);

// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", folder_view);
