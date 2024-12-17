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
const teamListBody = document.getElementById('teamListBody');
const selectMembro = document.getElementById('member');
const selectedStudentsList = document.getElementById('selectedStudents');
const clearButton = document.getElementById('clearSelectedStudents');
const addMembroSelect = document.getElementById('member-add');
const teamSelect = document.getElementById('team'); // Select della sezione "Dettagli Team"
const teamMembro = document.getElementById('team-add'); // Select della sezione "Dettagli Team"
const teamDetailsContainer = document.createElement('div'); // Contenitore per i dettagli del team
const teamForm = document.getElementById('teamForm');
const removeTeamSelect = document.getElementById('team-remove-member');
const removeMembroSelect = document.getElementById('memberToremove');
const teamDelete = document.getElementById('team-delete');

document.getElementById('dettagli-team').appendChild(teamDetailsContainer); // Aggiunge il contenitore nella sezione
let datiTeam = []; // Variabile per salvare i dati dei team
//ottiene la data
function getCurrentDate() {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // I mesi partono da 0
    const dd = String(today.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`; // Ritorna la data nel formato "yyyy-mm-dd"
}

function fetchTeams(teamSelect) {
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
function listHtml(){
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
}

function filterTeams() {
    const searchInput = document.getElementById('teamSearch').value.toLowerCase(); // Testo inserito dall'utente
    const rows = document.querySelectorAll('#teamListBody tr'); // Tutte le righe della tabella

    rows.forEach(row => {
        const teamName = row.querySelector('td:first-child').textContent.toLowerCase(); // Prima colonna: Nome Team
        if (teamName.includes(searchInput)) {
            row.style.display = ''; // Mostra la riga se il testo corrisponde
        } else {
            row.style.display = 'none'; // Nasconde la riga se non corrisponde
        }
    });
}




// Funzione per aggiornare la lista degli studenti selezionati
function toggleSelectedStudentsList() {
    // Ottieni la lista corrente degli studenti selezionati
    const selectedEmails = Array.from(selectedStudentsList.children).map(li => li.dataset.value);

    // Aggiungi o rimuovi gli studenti selezionati
    Array.from(selectMembro.selectedOptions).forEach(option => {
        const email = option.value; // Usa l'email come identificatore
        const isAlreadySelected = selectedEmails.includes(email);

        if (isAlreadySelected) {
            // Se l'email è già presente, rimuovila dalla lista
            const existingStudent = Array.from(selectedStudentsList.children).find(li => li.dataset.value === email);
            if (existingStudent) {
                selectedStudentsList.removeChild(existingStudent);
            }
        } else {
            // Se l'email non è presente, aggiungila alla lista
            const li = document.createElement('li');
            li.textContent = option.textContent; // Mostra il testo visibile (es. nome completo)
            li.dataset.value = email; // Salva SOLO l'email nel dataset
            selectedStudentsList.appendChild(li);
        }
    });
}


function studentiLista(studentsSelect) {
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
            // Salva l'elenco completo degli studenti per la ricerca
            const allStudents = data;

            // Popola la select con gli studenti
            updateStudentList(studentsSelect, allStudents);

            // Assegna il filtro alla label di ricerca
            const searchInput = document.getElementById('searchStudents');
            searchInput.addEventListener('input', () => {
                const searchQuery = searchInput.value.toLowerCase();
                const filteredStudents = allStudents.filter(student =>
                    `${student.nome} ${student.surname} ${student.email}`.toLowerCase().includes(searchQuery)
                );
                updateStudentList(studentsSelect, filteredStudents);
            });
        })
        .catch(error => {
            console.error('Errore:', error);
        });
}

function updateStudentList(studentsSelect, students) {
    studentsSelect.innerHTML = ''; // Svuota la lista attuale
    students.forEach(student => {
        const option = document.createElement('option');
        option.value = student.email; // Usa l'email come valore
        option.textContent = `${student.name} ${student.surname} <${student.email}>`; // Mostra nome e email
        studentsSelect.appendChild(option);
    });
}


selectMembro.addEventListener('change', () => {
        toggleSelectedStudentsList();
});
// Funzione per pulire la lista degli studenti selezionati
clearButton.addEventListener('click', () => {
        // Deseleziona tutti gli studenti nella select
        Array.from(selectMembro.options).forEach(option => {
            option.selected = false;
        });

        // Svuota la lista degli studenti selezionati
        selectedStudentsList.innerHTML = '';
});



// Quando il DOM è completamente caricato
teamForm.addEventListener('submit', function (event) {

        event.preventDefault();
        const teamData = {
            teamName: document.getElementById('teamName').value,
            description: document.getElementById('description').value,
            leaderId: document.getElementById('leaderId').value,
            member: Array.from(selectedStudentsList.children).map(li => li.dataset.value), // Ottieni gli ID dei membri selezionati
            creationDate: getCurrentDate()
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
                    location.reload();
                } else if (response.status === 409) {
                    alert('Errore: Il team esiste già. Inserisci un nome diverso.');
                } else {
                    alert('Errore durante la creazione del team.');
                }
            })
            .catch(error => console.error('Errore:', error));
        
});

teamSelect.addEventListener('change', () => {
        const selectedTeam = teamSelect.value; // Ottieni il valore del team selezionato
        if (selectedTeam) {
            displayTeamDetails(selectedTeam); // Mostra i dettagli del team selezionato
        } else {
            teamDetailsContainer.innerHTML = ''; // Pulisce i dettagli se nessun team è selezionato
        }
});

document.getElementById('add-member-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const teamName = document.getElementById('team-add').value;
    const memberId = document.getElementById('member-add').value;

    if (!teamName || !memberId) {
        alert('Seleziona un team e uno studente!');
        return;
    }

    fetch('/team_add_member', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        },
        body: JSON.stringify({ teamName, memberId })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(message => {
        alert("Membro aggiunto al team! Ricarico...");
        location.reload();
    })
    .catch(error => {
        console.error('Errore durante l\'aggiunta del membro:', error);
        alert('Errore durante l\'aggiunta del membro: ' + error.message);
    });
});

document.getElementById('remove-member-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const teamName = document.getElementById('team-remove-member').value;
    const memberId = document.getElementById('memberToremove').value;

    if (!teamName || !memberId) {
        alert('Seleziona un team e uno studente!');
        return;
    }

    fetch('/team_remove_member', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        },
        body: JSON.stringify({ teamName, memberId })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(message => {
        alert("Membro rimosso dal team! Ricarico...");
        location.reload();
    })
    .catch(error => {
        console.error('Errore durante la rimozione del membro:', error);
        alert('Errore durante la rimozione del membro: ' + error.message);
    });
});

document.getElementById('delete-team-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const teamName = document.getElementById('team-delete').value;

    if (!teamName) {
        alert('Seleziona un team!');
        return;
    }

    fetch('/team_delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        },
        body: JSON.stringify({ teamName })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(message => {
        alert("Team Cancellato! Ricarico...");
        location.reload();
    })
    .catch(error => {
        console.error('Errore durante la rimozione del team:', error);
        alert('Errore durante la rimozione del team: ' + error.message);
    });
});




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
    listHtml();
    filterTeams();      
    fetchTeams(teamSelect);
    fetchTeams(teamMembro);
    studentiLista(addMembroSelect);
    studentiLista(selectMembro);
    studentiLista(removeMembroSelect);
    fetchTeams(removeTeamSelect);
    fetchTeams(teamDelete);
});


