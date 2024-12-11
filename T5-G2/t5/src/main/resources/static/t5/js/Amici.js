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

    //by GabMan 11/12
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
                    return; // Solo per interrompere qui
                }
    
                friendsContainer.innerHTML = ""; // Ripulisce il contenitore
                data.forEach(friend => {
                    const friendItem = document.createElement("div");
                    friendItem.className = "friend-item list-group-item d-flex align-items-center";
    
                    const avatar = document.createElement("img");
                    avatar.src = friend.avatar || "/t5/images/profilo/sampleavatar.jpg";
                    avatar.alt = "Avatar";
                    avatar.className = "friend-avatar";
    
                    const friendInfo = document.createElement("div");
                    friendInfo.className = "friend-info flex-grow-1";
                    friendInfo.textContent = friend.nickname;
    
                    const friendId = document.createElement("p");
                    friendId.className = "friend-id";
                    friendId.textContent = `ID: ${friend.friendId}`;
    
                    const deleteButton = document.createElement("button");
                    deleteButton.className = "btn btn-danger btn-sm ms-auto";
                    deleteButton.textContent = "Elimina";
                    deleteButton.addEventListener("click", () => removeFriend(friend.friendId));
    
                    friendInfo.append(friendId);
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
    //GabMan 11/12
    function addFriend() {
        const friendId = addFriendButton.dataset.friendId;
    
        if (!friendId) {
            searchFriendMessage.textContent = "Errore: nessun amico selezionato.";
            return;
        }
    
        fetch(`/addFriend?friendId=${encodeURIComponent(friendId)}`, {
            method: "POST",
            credentials: "include",
        })
            .then(response => {
                if (!response.ok) throw new Error("Errore durante l'aggiunta dell'amico.");
                return response.text();
            })
            .then(message => {
                alert(message);
                loadFriends();
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
