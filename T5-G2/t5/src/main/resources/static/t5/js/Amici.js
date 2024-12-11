document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById("friendsContainer");
    const errorMessage = document.getElementById("errorMessage");
    const searchFriendInput = document.getElementById("searchFriendInput");
    const searchFriendMessage = document.getElementById("searchFriendMessage");
    const searchResult = document.getElementById("searchResult");
    const friendName = document.getElementById("friendName");
    const addFriendButton = document.getElementById("addFriendButton");
    const searchFriendButton = document.getElementById("searchFriendButton");

    // Aggiungi un controllo per vedere se addFriendButton esiste
    if (!addFriendButton) {
        console.error("addFriendButton non trovato!");
        return;
    }

    function loadFriends() {
        friendsContainer.innerHTML = "";
        errorMessage.textContent = "";

        fetch("/getFriendlist", {
            method: "GET",
            credentials: "include",
        })
            .then(response => {
                if (!response.ok) throw new Error("Errore durante il recupero della lista amici.");
                return response.json();
            })
            .then(data => {
                console.log("Dati ricevuti dalla lista amici:", data);
                if (data.length === 0) {
                    errorMessage.textContent = "Non hai ancora degli amici. Aggiungine uno!";
                    return;
                }

                data.forEach(friend => {
                    const friendItem = document.createElement("div");
                    friendItem.className = "friend-item list-group-item d-flex align-items-center";

                    const avatar = document.createElement("img");
                    avatar.src = friend.avatar || "/t5/images/profilo/sampleavatar.jpg";
                    avatar.alt = "Avatar";
                    avatar.className = "friend-avatar";

                    const friendInfo = document.createElement("div");
                    friendInfo.className = "friend-info flex-grow-1";
                    friendInfo.textContent = `${friend.nickname} (ID: ${friend.friendId})`;

                    const deleteButton = document.createElement("button");
                    deleteButton.className = "btn btn-danger btn-sm ms-auto";
                    deleteButton.textContent = "Elimina";
                    deleteButton.addEventListener("click", () => removeFriend(friend.friendId));

                    friendItem.append(avatar, friendInfo, deleteButton);
                    friendsContainer.appendChild(friendItem);
                });
            })
            .catch(error => {
                errorMessage.textContent = error.message;
            });
    }

    function searchFriend() {
        const friendIdentifier = searchFriendInput.value.trim();
        if (!friendIdentifier) {
            searchFriendMessage.textContent = "Inserisci un nickname o ID valido.";
            return;
        }

        fetch(`/searchFriend?identifier=${encodeURIComponent(friendIdentifier)}`, {
            method: "GET",
            credentials: "include",
        })
            .then(response => {
                if (!response.ok) throw new Error("Amico non trovato.");
                return response.json();
            })
            .then(data => {
                friendName.textContent = `${data.nickname} (ID: ${data.id})`;
                searchResult.style.display = "block";

                // Aggiungi un controllo per verificare che `data.id` esista
                if (data && data.id) {
                    addFriendButton.dataset.friendId = data.id; // Imposta il friendId
                    console.log("ID dell'amico trovato:", data.id); // Aggiungi un log per il debug
                } else {
                    searchFriendMessage.textContent = "Errore: l'ID dell'amico non è valido.";
                }
            })
            .catch(error => {
                searchFriendMessage.textContent = error.message;
            });
    }

    function addFriend() {
        const friendId = addFriendButton.dataset.friendId;
    
        // Controlla se friendId è stato correttamente impostato
        if (!friendId) {
            searchFriendMessage.textContent = "Errore: nessun amico selezionato.";
            return;
        }
    
        // Verifica se l'amico è già nella lista chiamando loadFriends
        fetch("/getFriendlist", {
            method: "GET",
            credentials: "include",
        })
            .then(response => {
                if (!response.ok) throw new Error("Errore durante il controllo della lista amici.");
                return response.json();
            })
            .then(friendList => {
                // Controlla se l'amico è già nella lista
                const isAlreadyFriend = friendList.some(friend => friend.friendId === friendId);
                if (isAlreadyFriend) {
                    searchFriendMessage.textContent = "Siete già Amici!";
                    return;
                }
    
                // Se l'amico non è nella lista, procedi con l'aggiunta
                return fetch(`/addFriend?friendId=${encodeURIComponent(friendId)}`, {
                    method: "POST",
                    credentials: "include",
                });
            })
            .then(response => {
                if (response && !response.ok) throw new Error("Errore durante l'aggiunta dell'amico.");
                return response?.text();
            })
            .then(message => {
                if (message) alert(message);
                loadFriends(); // Ricarica la lista degli amici
                searchResult.style.display = "none";
            })
            .catch(error => {
                searchFriendMessage.textContent = error.message;
            });
    }
    

    function removeFriend(friendId) {
        if (!friendId) {
            alert("Errore: ID amico non valido.");
            return;
        }
    
        fetch(`/removeFriend?friendId=${encodeURIComponent(friendId)}`, {
            method: "POST", // Cambiato in POST
            credentials: "include",
        })
            .then(response => {
                if (!response.ok) throw new Error("Errore durante la rimozione dell'amico.");
                return response.text();
            })
            .then(message => {
                alert(message); 
                loadFriends(); 
            })
            .catch(error => {
                alert(error.message); // Mostra un messaggio di errore
            });
    }
    
    
    

    searchFriendButton.addEventListener("click", searchFriend);
    addFriendButton.addEventListener("click", addFriend);

    loadFriends();
});
