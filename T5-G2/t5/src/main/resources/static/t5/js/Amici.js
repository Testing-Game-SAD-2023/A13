document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById("friendsContainer");
    const errorMessage = document.getElementById("errorMessage");
    const searchFriendInput = document.getElementById("searchFriendInput");
    const searchFriendMessage = document.getElementById("searchFriendMessage");
    const searchResult = document.getElementById("searchResult");
    const friendName = document.getElementById("friendName");
    const addFriendButton = document.getElementById("addFriendButton");
    const searchFriendButton = document.getElementById("searchFriendButton");

    // Funzione per caricare gli amici
    function loadFriends() {
        friendsContainer.innerHTML = "";
        errorMessage.textContent = "";

        fetch("/getFriendlist", {
            method: "GET",
            credentials: "include",
        })
            .then((response) => {
                if (response.status === 401) {
                    throw new Error("Non sei autorizzato. Effettua il login.");
                }
                if (!response.ok) {
                    throw new Error("Errore durante il recupero della lista amici.");
                }
                return response.json();
            })
            .then((data) => {
                if (data.length === 0) {
                    errorMessage.textContent = "Non hai ancora degli amici. Aggiungine uno!";
                    return;
                }

                data.forEach((friend) => {
                    const friendItem = document.createElement("div");
                    friendItem.className = "friend-item list-group-item d-flex align-items-center";

                    const avatar = document.createElement("img");
                    avatar.src = friend.avatar || "/t5/images/profilo/sampleavatar.jpg";
                    avatar.alt = "Avatar";
                    avatar.className = "friend-avatar";

                    const friendInfo = document.createElement("div");
                    friendInfo.className = "friend-info flex-grow-1";

                    const friendName = document.createElement("p");
                    friendName.className = "friend-name";
                    friendName.textContent = friend.nickname;

                    friendInfo.appendChild(friendName);

                    const deleteButton = document.createElement("button");
                    deleteButton.className = "btn btn-danger btn-sm delete-friend-btn ms-auto";
                    deleteButton.textContent = "Elimina";
                    deleteButton.onclick = () => deleteFriend(friend.friendId);
                    
                    if(friend.friendId) {
                        deleteButton.onclick = () => deleteFriend(friend.friendId);
                    } else {
                        console.error("friendId is missing for this friend");
                    }


                    friendItem.appendChild(avatar);
                    friendItem.appendChild(friendInfo);
                    friendItem.appendChild(deleteButton);

                    friendsContainer.appendChild(friendItem);
                });
            })
            .catch((error) => {
                errorMessage.textContent = error.message;
                errorMessage.className = "text-danger";
            });
    }

    // Funzione per cercare un amico
    function searchFriend() {
        searchFriendMessage.textContent = "";
        searchResult.style.display = "none";

        const friendIdentifier = searchFriendInput.value.trim();
        if (!friendIdentifier) {
            searchFriendMessage.textContent = "Per favore, inserisci un nickname o ID valido.";
            searchFriendMessage.className = "text-danger";
            return;
        }

        fetch(`/searchFriend?identifier=${encodeURIComponent(friendIdentifier)}`, {
            method: "GET",
            credentials: "include",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Amico non trovato.");
                }
                return response.json();
            })
            .then((data) => {
                friendName.textContent = `${data.nickname} (ID: ${data.id})`;
                searchResult.style.display = "block";
                addFriendButton.setAttribute("data-friend-id", data.id);
            })
            .catch((error) => {
                searchFriendMessage.textContent = error.message;
                searchFriendMessage.className = "text-danger";
            });
    }

    // Funzione per aggiungere un amico
    function addFriend() {
        const friendId = addFriendButton.getAttribute("data-friend-id");
        if (!friendId) {
            searchFriendMessage.textContent = "Errore: nessun amico selezionato.";
            searchFriendMessage.className = "text-danger";
            return;
        }

        fetch("/addFriend", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ friendId }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Errore durante l'aggiunta dell'amico.");
                }
                return response.text();
            })
            .then((message) => {
                searchFriendMessage.textContent = message || "Amico aggiunto con successo!";
                searchFriendMessage.className = "text-success";
                loadFriends();
                searchResult.style.display = "none";
            })
            .catch((error) => {
                searchFriendMessage.textContent = error.message;
                searchFriendMessage.className = "text-danger";
            });
    }

    // Funzione per eliminare un amico
    function deleteFriend(friendId) {
    // Assicurati che friendId sia un valore valido
    if (friendId === undefined || friendId === null) {
        console.error("friendId is undefined or null");
        return;
    }

    fetch(`/deleteFriend/${friendId}`, {
        method: "DELETE",
        credentials: "include",
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error("Errore durante l'eliminazione dell'amico.");
        }
        return response.text();
    })
    .then((message) => {
        alert(message || "Amico eliminato con successo!");
        loadFriends(); // Aggiorna la lista degli amici
    })
    .catch((error) => {
        alert("Errore: " + error.message);
    });
    }



    // Aggiungi i listener ai bottoni
    searchFriendButton.addEventListener("click", searchFriend);
    addFriendButton.addEventListener("click", addFriend);

    // Carica gli amici al caricamento della pagina
    loadFriends();
});


