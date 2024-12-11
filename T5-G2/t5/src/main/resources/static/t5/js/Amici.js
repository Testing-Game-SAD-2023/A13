document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById("friendsContainer");
    const errorMessage = document.getElementById("errorMessage");
    const searchFriendInput = document.getElementById("searchFriendInput");
    const searchFriendMessage = document.getElementById("searchFriendMessage");
    const searchResult = document.getElementById("searchResult");
    const friendName = document.getElementById("friendName");
    const addFriendButton = document.getElementById("addFriendButton");
    const searchFriendButton = document.getElementById("searchFriendButton");

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
                    deleteButton.addEventListener("click", () => deleteFriendById(friend.friendId)); // Usa friendId

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
                addFriendButton.dataset.friendId = data.id;
            })
            .catch(error => {
                searchFriendMessage.textContent = error.message;
            });
    }

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
                if (!response.ok) throw new Error("Amico già nella lista");
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

    function deleteFriendById(friendId) { // Aggiornata per utilizzare friendId
        fetch(`/deleteFriendById?friendId=${encodeURIComponent(friendId)}`, {
            method: "DELETE",
            credentials: "include",
        })
            .then((response) => {
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error("Amicizia non trovata.");
                    } else if (response.status === 401) {
                        throw new Error("Non autorizzato. Accedi di nuovo.");
                    }
                    throw new Error("Errore durante l'eliminazione dell'amico.");
                }
                return response.text();
            })
            .then((message) => {
                console.log(message);
                loadFriends();
            })
            .catch((error) => {
                console.error("Errore:", error.message);
            });
    }
    

    searchFriendButton.addEventListener("click", searchFriend);
    addFriendButton.addEventListener("click", addFriend);

    loadFriends();
});
