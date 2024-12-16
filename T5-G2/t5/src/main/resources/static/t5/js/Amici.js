document.addEventListener("DOMContentLoaded", () => {
    const friendsContainer = document.getElementById("friendsContainer");
    const errorMessage = document.getElementById("errorMessage");
    const searchFriendInput = document.getElementById("searchFriendInput");
    const searchFriendMessage = document.getElementById("searchFriendMessage");
    const searchResult = document.getElementById("searchResult");
    const friendName = document.getElementById("friendName");
    const addFriendButton = document.getElementById("addFriendButton");
    const searchFriendButton = document.getElementById("searchFriendButton");


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
                    return; 
                }
    
                friendsContainer.innerHTML = ""; 
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
                if (!response.ok) throw new Error("Nessun utente trovato");
                return response.json();
            })
            .then(data => {
                friendName.textContent = `${data.nickname} (ID: ${data.id})`;
                searchResult.style.display = "block";

                // Aggiungi un controllo per verificare che `data.id` esista
                if (data && data.id) {
                    addFriendButton.dataset.friendId = data.id; // Imposta il friendId
                    console.log("E'stato trovato un utente", data.id); // Aggiungi un log per il debug
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
                if (!response.ok) throw new Error("Siete già Amici!");
                return response.text();
            })
            .then(message => {
                alert(message);
                confetti({
                    particleCount: 200, // Più particelle
                    spread: 150, // Spargimento più ampio
                    angle: 90, // Direzione verso l'alto
                    origin: { y: 0.6 },
                    colors: ["#ff69b4", "#00ffff", "#4b0082", "#ff0000"]
                });
                

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
                createRainWithClouds();
                loadFriends(); 
            })
            .catch(error => {
                alert(error.message); // Mostra un messaggio di errore
            });
    }
    function createRainWithClouds() {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        document.body.appendChild(canvas);
    
        // Configurazione del canvas
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
        canvas.style.position = 'fixed';
        canvas.style.top = 0;
        canvas.style.left = 0;
        canvas.style.pointerEvents = 'none';
    
        const raindrops = [];
        const cloudY = 50; // Altezza delle nuvole
        const cloudColor = 'rgba(200, 200, 200, 0.8)'; // Colore delle nuvole
    
        // Genera gocce di pioggia
        for (let i = 0; i < 100; i++) {
            raindrops.push({
                x: Math.random() * canvas.width,
                y: Math.random() * canvas.height * 0.5 - 50,
                length: Math.random() * 20 + 10,
                speed: Math.random() * 3 + 2,
                opacity: Math.random() * 0.5 + 0.5,
            });
        }
    
        function drawClouds() {
            ctx.fillStyle = cloudColor;
            ctx.beginPath();
            ctx.arc(150, cloudY, 60, 0, Math.PI * 2); // Nuvola sinistra
            ctx.arc(200, cloudY, 70, 0, Math.PI * 2); // Nuvola centrale
            ctx.arc(270, cloudY, 60, 0, Math.PI * 2); // Nuvola destra
            ctx.closePath();
            ctx.fill();
        }
    
        function drawRain() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
    
            // Disegna le nuvole
            drawClouds();
    
            // Disegna le gocce di pioggia
            raindrops.forEach(drop => {
                ctx.beginPath();
                ctx.strokeStyle = `rgba(0, 191, 255, ${drop.opacity})`; // Colore azzurro
                ctx.lineWidth = 2;
                ctx.moveTo(drop.x, drop.y);
                ctx.lineTo(drop.x, drop.y + drop.length);
                ctx.stroke();
            });
    
            // Aggiorna posizione delle gocce
            raindrops.forEach(drop => {
                drop.y += drop.speed;
                if (drop.y > canvas.height) {
                    drop.y = -drop.length;
                    drop.x = Math.random() * canvas.width;
                }
            });
        }
    
        let animationFrame;
        const startTime = Date.now();
    
        function animate() {
            const elapsed = Date.now() - startTime;
            if (elapsed > 2000) {
                cancelAnimationFrame(animationFrame);
                canvas.remove(); // Rimuove il canvas dopo 2 secondi
            } else {
                drawRain();
                animationFrame = requestAnimationFrame(animate);
            }
        }
    
        animate();
    }
    
    

    searchFriendButton.addEventListener("click", searchFriend);
    addFriendButton.addEventListener("click", addFriend);

    loadFriends();
});
