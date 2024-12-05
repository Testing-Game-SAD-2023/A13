// Gestione delle sezioni della pagina
function showSection(sectionId) {
    // Nascondi tutte le sezioni
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    // Mostra solo la sezione selezionata
    document.getElementById(sectionId).classList.add('active');
}

// Funzione per tornare alla home page dell'amministratore

// Quando il DOM è completamente caricato
document.addEventListener('DOMContentLoaded', function () {
    // Mostra la sezione "Lista Team" di default
    showSection('list-team');

    // Aggiungi eventuali altre logiche di inizializzazione qui
});


// Gestisci il modulo per creare un team
document.getElementById('teamForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const teamData = {
        teamName: document.getElementById('teamName').value,
        description: document.getElementById('description').value,
        leaderId: document.getElementById('leaderId').value,
        member: document.getElementById('member').value.split(','),
        creationDate: document.getElementById('creationDate').value
    };

    fetch('/team', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(teamData)
    })
        .then(response => {
            if (response.ok) {
                alert('Team creato con successo!');
                document.getElementById('teamForm').reset();
            } else {
                alert('Errore durante la creazione del team.');
            }
        })
        .catch(error => console.error('Errore:', error));
});


