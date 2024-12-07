// Funzione per caricare gli amici
function loadFriends() {
    const friendsContainer = document.getElementById("friendsContainer");
    const errorMessage = document.getElementById("errorMessage");

    // Pulisce il contenuto precedente
    friendsContainer.innerHTML = "";
    errorMessage.textContent = "";

    // Recupera la lista amici dal server
    fetch("/getFriendlist", {
        method: "GET",
        credentials: "include", // Include i cookie nella richiesta
    })
        .then(response => {
            if (response.status === 401) {
                throw new Error("Non sei autorizzato. Effettua il login.");
            }
            if (response.status === 404) {
                throw new Error("Non hai ancora degli amici. Aggiungine uno!");
            }
            if (!response.ok) {
                throw new Error("Errore durante il recupero della lista amici.");
            }
            return response.json();
        })
        .then(data => {
            // Controlla se la lista è vuota
            if (data.length === 0) {
                errorMessage.textContent = "Non hai ancora degli amici. Aggiungine uno!";
                return;
            }

            // Popola la lista degli amici
            data.forEach(friend => {
                // Crea il contenitore per ogni amico
                const friendItem = document.createElement("div");
                friendItem.className = "friend-item";

                // Avatar dell'amico
                const avatar = document.createElement("img");
                avatar.src = friend.avatar || '/t5/images/profilo/sampleavatar.jpg '; // Usa il campo 'avatar' restituito dal backend
                avatar.alt = "Avatar";
                avatar.className = "friend-avatar";

                // Informazioni sull'amico
                const friendInfo = document.createElement("div");
                friendInfo.className = "friend-info";

                const friendName = document.createElement("p");
                friendName.className = "friend-name";
                friendName.textContent = friend.nickname; // Usa il campo 'username' restituito dal backend

                friendInfo.appendChild(friendName);

                // Pulsante per eliminare l'amico
                const deleteButton = document.createElement("button");
                deleteButton.className = "btn btn-danger btn-sm delete-friend-btn";
                deleteButton.textContent = "Elimina";
                deleteButton.onclick = () => deleteFriend(friend.friendId); // Funzione per eliminare l'amico

                // Assembla il contenitore
                friendItem.appendChild(avatar);
                friendItem.appendChild(friendInfo);
                friendItem.appendChild(deleteButton);

                // Aggiunge l'elemento alla lista
                friendsContainer.appendChild(friendItem);
            });
        })
        .catch(error => {
            // Mostra messaggio di errore
            errorMessage.textContent = error.message;
            errorMessage.className = "text-danger";
        });
    }


// Funzione per aggiungere un amico
function addFriend() {
    const friendIdInput = document.getElementById("friendIdInput");
    const addFriendMessage = document.getElementById("addFriendMessage");

    // Pulisce il messaggio precedente
    addFriendMessage.textContent = "";

    const friendId = friendIdInput.value.trim();
    if (!friendId) {
        addFriendMessage.textContent = "Per favore, inserisci un nickname valido.";
        addFriendMessage.className = "text-danger";
        return;
    }

    // Invia la richiesta per aggiungere un amico
    fetch("/addFriend", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ friendId }),
    })
        .then(response => {
            if (response.status === 401) {
                throw new Error("Non sei autorizzato. Effettua il login.");
            }
            if (!response.ok) {
                throw new Error("Errore durante l'aggiunta dell'amico.");
            }
            return response.json();
        })
        .then(data => {
            addFriendMessage.textContent = "Amico aggiunto con successo!";
            addFriendMessage.className = "text-success";
            friendIdInput.value = ""; // Pulisce il campo di input
            loadFriends(); // Ricarica la lista degli amici
        })
        .catch(error => {
            addFriendMessage.textContent = error.message;
            addFriendMessage.className = "text-danger";
        });
}

// Funzione per eliminare un amico
function deleteFriend(friendId) {
    fetch(`/deleteFriend/${friendId}`, {
        method: "DELETE",
        credentials: "include",
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Errore durante l'eliminazione dell'amico.");
            }
            return response.json();
        })
        .then(() => {
            alert("Amico eliminato con successo!");
            loadFriends(); // Aggiorna la lista degli amici
        })
        .catch(error => {
            alert("Errore: " + error.message);
        });
}

// Carica la lista amici al caricamento della pagina
document.addEventListener("DOMContentLoaded", loadFriends);
