
const challengeListBody = document.getElementById('challengeListBody');
const challengeForm = document.getElementById('challengeForm');
const teamSelect = document.getElementById('teamSelect'); // Select della sezione "Dettagli Team"
const challenge_del = document.getElementById('challengeSelect');
const challenge_det = document.getElementById('challengeSelectDetails');

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

function controlloStato(){
    const currentDate = new Date(); // Data attuale

    // Converte la data inserita in formato stringa in un oggetto Date
    const startDateInput = new Date(document.getElementById('startDate').value);

    // Determina lo stato del challenge
    let status;
    if (startDateInput > currentDate) {
        status = 'in attesa'; // La data di inizio è futura
    } else if (startDateInput.toDateString() === currentDate.toDateString()) {
        status = 'in corso'; // La data di inizio è uguale a quella attuale
    }
    return status;
}

challengeForm.addEventListener('submit', function (event) {

    event.preventDefault();
    const currentDate=dataFormattata();
    const challengeData = {
        challengeName: document.getElementById('challengeName').value,
        description: document.getElementById('description').value,
        teamId: document.getElementById('teamSelect').value,
        creatorId: document.getElementById('creatorId').value, // Assumendo che ci sia un campo per l'ID del creatore
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        status: controlloStato(),
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

function dataFormattata(){
    const today = new Date();
    // Formatta la data in modo che sia compatibile con l'input di tipo date
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // I mesi partono da 0
    const dd = String(today.getDate()).padStart(2, '0');
    return formattedToday = `${yyyy}-${mm}-${dd}`;
}

function controlData() {
    const formattedToday=dataFormattata();

    // Imposta la data minima per startDate
    const startDateInput = document.getElementById('startDate');
    startDateInput.setAttribute('min', formattedToday);

    // Imposta la data minima per endDate in base alla startDate
    const endDateInput = document.getElementById('endDate');
    endDateInput.setAttribute('min', formattedToday); // Imposta la data minima iniziale

    // Aggiorna dinamicamente il min di endDate quando viene selezionata una startDate
    startDateInput.addEventListener('input', () => {
        const selectedStartDate = startDateInput.value;
        if (selectedStartDate) {
            endDateInput.setAttribute('min', selectedStartDate); // Aggiorna min per endDate
        }
    });
}


document.getElementById('delete-challenge-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const challengeName = document.getElementById('challengeSelect').value;

    if (!challengeName) {
        alert('Seleziona una challenge!');
        return;
    }

    fetch('/challenges_ChallengesByName', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${document.cookie.split('jwt=')[1]}`
        },
        body: JSON.stringify({ challengeName })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(message => {
        alert("Challenge cancellata! Ricarico...");
        location.reload();
    })
    .catch(error => {
        console.error('Errore durante la rimozione della challenge:', error);
        alert('Errore durante la rimozione della challenge: ' + error.message);
    });
});


function populateChallengeSelect(selectId) {
    fetch('/challenges_view', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            return response.json();
        })
        .then(challenges => {
            // Trova la select con l'ID passato come parametro
            if (!selectId) {
                console.error(`Elemento con ID '${selectId}' non trovato.`);
                return;
            }
            selectId.innerHTML = ''; // Resetta le opzioni esistenti
            challenges.forEach(challenge => {
                const option = document.createElement('option');
                option.value = challenge.challengeName; // Usa il nome della challenge come valore
                option.textContent = challenge.challengeName; // Usa il nome della challenge come etichetta
                selectId.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Errore durante il recupero delle challenge:', error);
            alert('Errore durante il caricamento delle challenge.');
        });
}

    function getVictoryConditionType() {
    const victoryConditionTypeSelect = document.getElementById("victoryConditionType");

    // Chiamata API per ottenere i valori dell'enum
    fetch('/victoryConditionTypes') // Endpoint creato nel Controller
        .then(response => {
            if (!response.ok) {
                throw new Error("Errore nella risposta del server");
            }
            return response.json(); // Converte la risposta in JSON
        })
        .then(data => {
            // Popola dinamicamente la select con i valori ricevuti
            data.forEach(type => {
                const option = document.createElement("option");
                option.value = type;        // Imposta il valore dell'opzione
                option.textContent = type;  // Imposta il testo visibile
                victoryConditionTypeSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Errore nel caricamento delle opzioni:", error);
        });
};


document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('nav a').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            const targetId = link.getAttribute('href').replace('#', '');
            showSection(targetId);
        });
    });

    controlData();
    showSection('list-challenges')
    listHtml();
    fetchTeams(teamSelect);
    populateChallengeSelect(challenge_det);
    populateChallengeSelect(challenge_del);
    getVictoryConditionType();
});
