//Creato by GabMan 30/11

document.addEventListener("DOMContentLoaded", async () => {
    const friendsContainer = document.getElementById('friendsContainer');
    const friendIdInput = document.getElementById('friendIdInput');
    const addFriendButton = document.getElementById('addFriendButton');

    // Mostra uno stato di caricamento (opzionale)
    toggleLoading(true, 'friendsContainer');

    try {
        // Recupera l'elenco amici tramite una richiesta AJAX
        const friends = await ajaxRequest('/getFriends', 'GET');

        // Nascondi lo stato di caricamento
        toggleLoading(false, 'friendsContainer');

        // Popola il contenitore con gli amici ricevuti
        populateFriendsList(friendsContainer, friends);
    } catch (error) {
        // Nascondi lo stato di caricamento
        toggleLoading(false, 'friendsContainer');

        // Mostra un modal di errore se qualcosa va storto
        openModalError('Errore', 'Non è stato possibile caricare la lista degli amici.');
    }

    // Aggiunge un listener per il pulsante "Aggiungi Amico"
    addFriendButton.addEventListener('click', async () => {
        const friendId = friendIdInput.value.trim();

        if (!friendId) {
            alert('Inserisci un ID valido.');
            return;
        }

        try {
            const response = await ajaxRequest('/addFriend', 'POST', { friendId });

            if (response) {
                alert('Amico aggiunto con successo!');
                friendIdInput.value = ''; // Pulisce il campo di input
                location.reload(); // Aggiorna la lista amici
            } else {
                alert('Errore durante l\'aggiunta dell\'amico.');
            }
        } catch (error) {
            console.error('Errore durante l\'aggiunta dell\'amico:', error);
            alert('Errore di connessione al server.');
        }
    });
});

// Funzione per popolare la lista degli amici
function populateFriendsList(container, friends) {
    container.innerHTML = ''; // Svuota il contenitore

    if (!friends || friends.length === 0) {
        container.innerHTML = '<p>Nessun amico trovato.</p>';
        return;
    }

    // Aggiungi ogni amico al contenitore
    friends.forEach(friend => {
        const friendElement = document.createElement('div');
        friendElement.className = 'friend-item';
        friendElement.innerHTML = `
            <span>${friend.name} - ${friend.status}</span>
            <button class="btn btn-danger btn-sm ml-2" onclick="removeFriend('${friend.id}')">Rimuovi</button>
        `;
        container.appendChild(friendElement);
    });
}

// Funzione per rimuovere un amico
async function removeFriend(friendId) {
    if (!confirm('Sei sicuro di voler rimuovere questo amico?')) return;

    try {
        const response = await ajaxRequest('/removeFriend', 'POST', { friendId });

        if (response) {
            alert('Amico rimosso con successo!');
            location.reload(); // Aggiorna la lista amici
        } else {
            alert('Errore durante la rimozione dell\'amico.');
        }
    } catch (error) {
        console.error('Errore durante la rimozione dell\'amico:', error);
        alert('Errore di connessione al server.');
    }
}
