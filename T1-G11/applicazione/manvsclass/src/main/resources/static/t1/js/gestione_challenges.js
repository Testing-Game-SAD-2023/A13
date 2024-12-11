// Recupera tutte le challenges per un team
async function loadChallenges() {
    const response = await fetch('/challenges/team/team123', {
        method: 'GET',
        credentials: 'include'
    });
    const challenges = await response.json();
    const challengeListBody = document.getElementById('challengeListBody');
    challengeListBody.innerHTML = '';

    challenges.forEach(challenge => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${challenge.challengeName}</td>
            <td>${challenge.description}</td>
            <td>${challenge.teamId}</td>
            <td>${challenge.startDate}</td>
            <td>${challenge.endDate}</td>
            <td>${challenge.status}</td>
        `;
        challengeListBody.appendChild(row);
    });
}

// Aggiunge una nuova challenge
async function createChallenge(event) {
    event.preventDefault();
    const formData = new FormData(document.getElementById('challengeForm'));
    const challenge = Object.fromEntries(formData);

    const response = await fetch('/challenges', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(challenge)
    });

    if (response.ok) {
        alert('Challenge creata con successo!');
        loadChallenges();
    } else {
        alert('Errore nella creazione della challenge.');
    }
}

// Collega eventi
document.getElementById('challengeForm').addEventListener('submit', createChallenge);

// Carica le challenges al caricamento della pagina
loadChallenges();
