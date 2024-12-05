document.addEventListener('DOMContentLoaded', function () {
    // Mostra la sezione "Lista Team" di default
    showSection('list-team');
});


// Funzione per tornare alla home page dell'amministratore
function returnToHome() {
    window.location.href = "/home_admin";
}

// Mostra la sezione di default
showSection('add-team');

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


