document.addEventListener('DOMContentLoaded', function () {
    // Mostra la sezione "Lista Team" di default
    showSection('list-team');
});

// Funzione per mostrare una specifica sezione
function showSection(sectionId) {
    // Nasconde tutte le sezioni
    document.querySelectorAll('.section').forEach(function (section) {
        section.classList.remove('active');
    });

    // Mostra la sezione selezionata
    document.getElementById(sectionId).classList.add('active');

    // Aggiorna la lista dei team quando si accede alla sezione "Lista Team"
    if (sectionId === 'list-team') {
        fetchTeams();
    }
}

// Funzione per tornare alla home page dell'amministratore
function returnToHome() {
    window.location.href = "/home_admin";
}

// Funzione per creare un nuovo team
function createTeam(event) {
    event.preventDefault(); // Previeni il comportamento predefinito del modulo

    // Preleva i dati dal modulo
    const teamData = {
        teamName: document.getElementById('teamName').value,
        description: document.getElementById('description').value,
        leaderId: document.getElementById('leaderId').value,
        member: document.getElementById('member').value.split(','),
        creationDate: document.getElementById('creationDate').value
    };

    // Chiamata POST per creare un nuovo team
    fetch('/team/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(teamData)
    })
        .then(response => {
            if (response.ok) {
                // Team creato con successo
                return response.text().then(message => {
                    showSuccessMessage(message);
                    document.getElementById('teamForm').reset(); // Resetta il modulo
                });
            } else {
                // Errore durante la creazione
                return response.text().then(message => {
                    showErrorMessage(message);
                });
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showErrorMessage('Errore durante la creazione del team. Riprova più tardi.');
        });
}

// Funzione per caricare dinamicamente la lista dei team
function fetchTeams() {
    fetch('/team/view', {
        method: 'GET',
        headers: {
            'Content-Type': 'text/html'
        }
    })
        .then(response => response.text())
        .then(html => {
            // Aggiorna il corpo della tabella con il contenuto ricevuto dal backend
            document.getElementById('teamListBody').innerHTML = html;
        })
        .catch(error => {
            console.error('Errore durante il caricamento della lista:', error);
            document.getElementById('teamListBody').innerHTML = `
                <tr>
                    <td colspan="5" style="color: red; text-align: center;">Errore durante il caricamento dei team.</td>
                </tr>
            `;
        });
}

// Funzione per mostrare un messaggio di errore
function showErrorMessage(message) {
    const alertMessage = document.getElementById('alertMessage');
    alertMessage.innerText = message;
    alertMessage.style.display = 'block';
    alertMessage.style.color = 'red';

    const successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'none'; // Nasconde eventuali messaggi di successo
}

// Funzione per mostrare un messaggio di successo
function showSuccessMessage(message) {
    const successMessage = document.getElementById('successMessage');
    successMessage.innerText = message;
    successMessage.style.display = 'block';
    successMessage.style.color = 'green';
    const alertMessage = document.getElementById('alertMessage');
    alertMessage.style.display = 'none'; // Nasconde eventuali messaggi di errore
}
