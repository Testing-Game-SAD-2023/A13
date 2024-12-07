// Gestione delle sezioni della pagina
function showSection(sectionId) {
    // Nascondi tutte le sezioni
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
    } else {
        console.error(`Sezione con ID '${sectionId}' non trovata.`);
    }

}
// Quando il DOM è completamente caricato
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('nav a').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            const targetId = link.getAttribute('href').replace('#', '');
            showSection(targetId);
        });
    });

    // Imposta la sezione di default (ad esempio, "lista team")
    showSection('list-team');

    const teamListBody = document.getElementById('teamListBody');

    // Effettua la richiesta GET per ottenere il contenuto HTML della tabella
        fetch('/teams_view', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        }
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    alert('Accesso non autorizzato. Verifica il tuo JWT.');
                } else {
                    throw new Error('Errore nel recupero dei team.');
                }
            }
            return response.text();
        })
        .then(html => {
            // Inserisci l'HTML direttamente nella tabella
            teamListBody.innerHTML = html;
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante il caricamento dei team.');
        });

    const memberSelect = document.getElementById('member');
    const selectedStudentsList = document.getElementById('selectedStudents');
    const clearButton = document.getElementById('clearSelectedStudents');

    // Popola la select dei membri del team
    fetch('/students_list', { // Modifica della route
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}` // Estrae il JWT dal cookie
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore nel recupero degli studenti.');
            }
            return response.json();
        })
        .then(data => {
            // Popola la select con gli studenti non assegnati
            data.forEach(student => {
                const option = document.createElement('option');
                option.value = student.email; // Usa l'email come valore
                option.textContent = student.email; // Mostra l'email
                memberSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Errore:', error);
        });

    // Aggiorna la lista degli studenti selezionati
    memberSelect.addEventListener('change', () => {
        toggleSelectedStudentsList();
    });

    // Funzione per aggiornare la lista degli studenti selezionati
    function toggleSelectedStudentsList() {
        // Ottieni la lista corrente degli studenti selezionati
        const selectedEmails = Array.from(selectedStudentsList.children);

        // Aggiungi o rimuovi gli studenti selezionati
        Array.from(memberSelect.selectedOptions).forEach(option => {
            // Cerca se l'email è già presente nella lista
            const existingStudent = selectedEmails.find(li => li.textContent === option.textContent);

            if (existingStudent) {
                // Se presente, rimuovila
                selectedStudentsList.removeChild(existingStudent);
            } else {
                // Se non presente, aggiungila
                const li = document.createElement('li');
                li.textContent = option.textContent; // Mostra email dello studente
                selectedStudentsList.appendChild(li);
            }
        });
    }

    // Funzione per pulire la lista degli studenti selezionati
    clearButton.addEventListener('click', () => {
        // Deseleziona tutti gli studenti nella select
        Array.from(memberSelect.options).forEach(option => {
            option.selected = false;
        });

        // Svuota la lista degli studenti selezionati
        selectedStudentsList.innerHTML = '';
    });



    // Gestisci il modulo per creare un team
    const teamForm = document.getElementById('teamForm');
    teamForm.addEventListener('submit', function (event) {

        event.preventDefault();
        const teamData = {
            teamName: document.getElementById('teamName').value,
            description: document.getElementById('description').value,
            leaderId: document.getElementById('leaderId').value,
            member: Array.from(selectedStudentsList.children).map(li => li.textContent), // Ottieni gli ID dei membri selezionati
            creationDate: document.getElementById('creationDate').value
        };


        fetch('/team_create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
            },
            body: JSON.stringify(teamData)
        })
            .then(response => {
                if (response.ok) {
                    alert('Team creato con successo!');
                    teamForm.reset();
                    selectedStudentsList.innerHTML = ''; // Resetta la lista degli studenti selezionati
                } else {
                    alert('Errore durante la creazione del team.');
                }
            })
            .catch(error => console.error('Errore:', error));
    });

    const teamSelect = document.getElementById('team'); // Select della sezione "Dettagli Team"
    const teamDetailsContainer = document.createElement('div'); // Contenitore per i dettagli del team
    document.getElementById('dettagli-team').appendChild(teamDetailsContainer); // Aggiunge il contenitore nella sezione
    let datiTeam = []; // Variabile per salvare i dati dei team
    // Funzione per recuperare i team e popolare la select
    function fetchTeams() {
        fetch('/team_view', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}` // Estrae il JWT dal cookie
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Errore nel recupero della lista dei team.');
                }
                return response.json();
            })
            .then(data => {
                datiTeam = data; // Salva i dati dei team
                teamSelect.innerHTML = '<option value="">Seleziona un team</option>'; // Pulisce la select e aggiunge un'opzione iniziale
                data.forEach(team => {
                    const option = document.createElement('option');
                    option.value = team.teamName; // Usa il nome del team come valore
                    option.textContent = team.teamName; // Mostra il nome del team
                    teamSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Errore:', error);
                alert('Si è verificato un errore durante il caricamento della lista dei team.');
            });
    }

    // Funzione per mostrare i dettagli del team selezionato
    function displayTeamDetails(teamName) {
        const team = datiTeam.find(t => t.teamName === teamName); // Cerca il team selezionato nei dati salvati
        if (team) {
            // Mostra i dettagli del team
            teamDetailsContainer.innerHTML = `
                <h3>Dettagli del Team</h3>
                <p><strong>Nome:</strong> ${team.teamName}</p>
                <p><strong>Descrizione:</strong> ${team.description}</p>
                <p><strong>ID Leader:</strong> ${team.leaderId}</p>
                <p><strong>Membri:</strong> ${team.member.join(', ')}</p>
                <p><strong>Data di Creazione:</strong> ${team.creationDate}</p>
            `;
        } else {
            alert('Team non trovato.');
            teamDetailsContainer.innerHTML = ''; // Pulisce i dettagli in caso di errore
        }
    }

    // Richiama la funzione per popolare la select al caricamento della pagina
    fetchTeams();

    // Evento per il cambio di selezione del team
    teamSelect.addEventListener('change', () => {
        const selectedTeam = teamSelect.value; // Ottieni il valore del team selezionato
        if (selectedTeam) {
            displayTeamDetails(selectedTeam); // Mostra i dettagli del team selezionato
        } else {
            teamDetailsContainer.innerHTML = ''; // Pulisce i dettagli se nessun team è selezionato
        }
    });
});
