// Assicurati che il DOM sia caricato prima di eseguire il codice
document.addEventListener("DOMContentLoaded", async () => {
    const friendsContainer = document.getElementById('friendsContainer');

    // Mostra uno stato di caricamento (opzionale)
    toggleLoading(true, 'friendsContainer');

    try {
        // Recupera l'elenco amici tramite una richiesta AJAX
        const friends = await ajaxRequest('/api/getFriends', 'GET');

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
        friendElement.textContent = `${friend.name} - ${friend.status}`;
        container.appendChild(friendElement);
    });
}

