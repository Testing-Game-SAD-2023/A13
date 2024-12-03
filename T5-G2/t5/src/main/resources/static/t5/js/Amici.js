//Gabman 03/12

document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById('friendsContainer');

    // Funzione per caricare la lista amici
    const loadFriends = async () => {
        try {
            const response = await fetch('/getFriendlist'); // Endpoint backend
            if (response.ok) {
                const friends = await response.json();
                friendsContainer.innerHTML = ''; // Svuota contenitore

                friends.forEach(friend => {
                    const friendItem = document.createElement('div');
                    friendItem.className = 'list-group-item';

                    friendItem.innerHTML = `
                        <img src="${friend.profilePicture || '/default-avatar.jpg'}" alt="Immagine amico" class="friend-avatar">
                        <span class="friend-nickname flex-grow-1">${friend.nickname}</span>
                        <button class="btn btn-danger btn-sm" onclick="removeFriend(${friend.id})">Elimina amico</button>
                    `;

                    friendsContainer.appendChild(friendItem);
                });
            } else {
                console.error('Errore nel caricamento degli amici.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    // Funzione per aggiungere un amico
    window.addFriend = async () => {
        const friendId = document.getElementById('friendIdInput').value;
        if (!friendId) {
            alert('Inserisci un nickname valido!');
            return;
        }

        try {
            const response = await fetch('/addFriend', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({ friendId })
            });

            if (response.ok) {
                alert('Amico aggiunto con successo!');
                loadFriends(); // Ricarica lista amici
            } else {
                alert('Errore nell\'aggiunta dell\'amico.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    // Funzione per rimuovere un amico
    window.removeFriend = async (friendId) => {
        try {
            const response = await fetch(`/removeFriend/${friendId}`, { method: 'DELETE' });
            if (response.ok) {
                alert('Amico rimosso con successo!');
                loadFriends(); // Ricarica lista amici
            } else {
                alert('Errore nella rimozione dell\'amico.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    // Carica la lista amici al caricamento della pagina
    loadFriends();
});
