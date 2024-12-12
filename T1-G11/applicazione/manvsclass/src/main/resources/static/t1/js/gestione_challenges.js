
const challengeListBody = document.getElementById('challengeListBody');
const challengeForm = document.getElementById('challengeForm');
const teamSelect = document.getElementById('teamSelect'); // Select della sezione "Dettagli Team"
let datiTeam = []; // Variabile per salvare i dati dei team

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



// Recupera tutte le challenges per un team mostrando la listaHTML
function listHtml(){
    fetch('/challenge_view', {
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
                throw new Error('Errore nel recupero delle challenge.');
            }
        }
        return response.text();
    })
    .then(html => {
        // Inserisci l'HTML direttamente nella tabella
        challengeListBody.innerHTML = html;
    })
    .catch(error => {
        console.error('Errore:', error);
        alert('Si è verificato un errore durante il caricamento delle challenge.');
    });
}


challengeForm.addEventListener('submit', function (event) {

    event.preventDefault();
    const challengeData = {
        challengeName: document.getElementById('challengeName').value,
        description: document.getElementById('description').value,
        teamId: document.getElementById('teamSelect').value,
        creatorId: document.getElementById('creatorId').value, // Assumendo che ci sia un campo per l'ID del creatore
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        status: document.getElementById('status').value,
        victoryConditionType: document.getElementById('victoryConditionType').value,
        victoryCondition: document.getElementById('victoryCondition').value
    };


    fetch('/challenge_create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        },
        body: JSON.stringify(challengeData)
    })
        .then(response => {
            if (response.ok) {
                alert('Challenge creata con successo!');
                challengeForm.reset();
                location.reload();
            } else if (response.status === 409) {
                alert('Errore: La challenge esiste già. Inserisci un nome diverso.');
            } else {
                alert('Errore durante la creazione della challenge.');
            }
        })
        .catch(error => console.error('Errore:', error));
    
});


// Funzione per recuperare i team e popolare la select
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




document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('nav a').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            const targetId = link.getAttribute('href').replace('#', '');
            showSection(targetId);
        });
    });


    showSection('list-challenges')
    listHtml();
    fetchTeams(teamSelect);
});
