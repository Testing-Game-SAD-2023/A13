//Gabman 03/12

document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById('friendsContainer');

    function loadFriends() {
        const friendsContainer = document.getElementById("friendsContainer");
        const errorMessage = document.getElementById("errorMessage");
    
        // Clear previous content
        friendsContainer.innerHTML = "";
        errorMessage.textContent = "";
    
        // Fetch friends list from the server
        fetch("/getFriendlist", {
            method: "GET",
            credentials: "include", // Include cookies in the request
        })
            .then(response => {
                if (response.status === 401) {
                    throw new Error("Non sei autorizzato. Effettua il login.");
                }
                if (response.status === 404) {
                    throw new Error("Nessun amico trovato.");
                }
                if (!response.ok) {
                    throw new Error("Errore durante il recupero della lista amici.");
                }
                return response.json();
            })
            .then(data => {
                // Populate the friend list in the HTML
                data.forEach(friend => {
                    const friendItem = document.createElement("div");
                    friendItem.className = "list-group-item";
                    friendItem.textContent = `Nome: ${friend.name}, Email: ${friend.email}`;
                    friendsContainer.appendChild(friendItem);
                });
            })
            .catch(error => {
                // Display error message
                errorMessage.textContent = error.message;
            });
    }

    // Funzione per aggiungere un amico
    function addFriend() {
        const friendIdInput = document.getElementById("friendIdInput");
        const addFriendMessage = document.getElementById("addFriendMessage");
    
        // Clear previous message
        addFriendMessage.textContent = "";
    
        const friendId = friendIdInput.value.trim();
        if (!friendId) {
            addFriendMessage.textContent = "Per favore, inserisci un nickname valido.";
            addFriendMessage.className = "text-danger";
            return;
        }
    
        // Send request to add a friend
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
                friendIdInput.value = ""; // Clear input
                loadFriends(); // Refresh the friend list
            })
            .catch(error => {
                addFriendMessage.textContent = error.message;
                addFriendMessage.className = "text-danger";
            });
    }
    

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
