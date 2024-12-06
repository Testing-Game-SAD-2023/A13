// Gestione delle sezioni della pagina
function showSection(sectionId) {
    // Nascondi tutte le sezioni
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    // Mostra solo la sezione selezionata
    document.getElementById(sectionId).classList.add('active');
}
// Quando il DOM è completamente caricato
document.addEventListener('DOMContentLoaded', function () {
    // Mostra la sezione "Lista Team" di default
    showSection('list-team');
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
});


    // Gestisci il modulo per creare un team
    const teamForm = document.getElementById('teamForm');
    teamForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const teamData = {
            teamName: document.getElementById('teamName').value,
            description: document.getElementById('description').value,
            leaderId: document.getElementById('leaderId').value,
            member: Array.from(memberSelect.selectedOptions).map(option => option.value), // Ottieni gli ID dei membri selezionati
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