document.addEventListener("DOMContentLoaded", function () {
    // Log di verifica
    console.log("DOM completamente caricato e analizzato");

    // Trova gli elementi necessari per la funzionalità di ricerca amici
    const searchInput = document.querySelector('#friend-search-input');
    const suggestionsContainer = document.querySelector('#friend-suggestions');

    console.log(searchInput);
    console.log(suggestionsContainer);

    if (searchInput && suggestionsContainer) {
        // Regex per validare l'email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        // Gestione input nel campo di ricerca
        searchInput.addEventListener("input", async function () {
            const query = searchInput.value.trim();
            suggestionsContainer.style.display = "none"; //Nascondi subito i suggerimenti

            // Validazione dell'email
            if (!emailRegex.test(query)) {
                console.log("Email non valida per regex");
                return;
            }

            try {
                // Effettua la richiesta al server
                const url = new URL("/getUserByEMail", window.location.origin);
                url.searchParams.append("email", query);

                const response = await fetch(url, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                });

                if (response.ok) {
                    console.log("la GET è andata a buon fine, ora vediamo cosa c'è dentro");
                    const user = await response.json();
                    console.log(user);
                    const profile = user.userProfile;
                    console.log(profile);
                    console.log(user.email);

                    // Svuota eventuali precedenti risultati
                    suggestionsContainer.innerHTML = "";

                    if (profile && user.email) {
                        console.log("Esistono sia profile che user.email");

                        const profileInfo = document.createElement('div');
                        profileInfo.className = 'profile-info';

                        //Creiamo un contenitore per i dettagli dell'utente
                        const userDetails = document.createElement('div');
                        userDetails.className = 'user-details';

                        const userName = document.createElement('span');
                        userName.className = 'name';
                        userName.textContent = `${user.name} ${user.surname}`;

                        const userEmail = document.createElement('span');
                        userEmail.className = 'email';
                        userEmail.textContent = user.email;

                        userDetails.appendChild(userName);
                        userDetails.appendChild(userEmail);

                        // Crea il bottone con le caratteristiche richieste
                        const profileBtn = document.createElement('button');
                        profileBtn.className = 'btn btn-custom btn-sm';  // Le classi CSS per lo stile
                        profileBtn.textContent = "Visualizza Profilo";  // Imposta il testo del bottone
                        profileBtn.onclick = function() {
                            location.href = `/friend/${user.id}`;  // Aggiungi l'evento onclick per redirigere
                        };

                        profileInfo.appendChild(userDetails);
                        // Aggiungi il bottone all'interno di profileInfo
                        profileInfo.appendChild(profileBtn);

                        // Aggiungi gestione click sull'elemento del profilo
                        /*profileInfo.addEventListener('click', function () {
                            alert(`Selezionato: ${user.name}`);

                        });*/

                        suggestionsContainer.appendChild(profileInfo);
                        suggestionsContainer.style.display = "block";

                    } else {
                        // Nessun profilo trovato
                        console.log("Mi dispiace riprova");
                        suggestionsContainer.style.display = "none";
                    }
                } else {
                    console.error("Errore durante la ricerca del profilo:", response.statusText);
                }
            } catch (error) {
                console.error("Errore di rete:", error);
            }
        });
    }

    // Gestione dei tab dei trofei
    const trophyTabs = document.querySelectorAll('#trophyTabs button[data-bs-toggle="tab"]');
    trophyTabs.forEach(tab => {
        tab.addEventListener('shown.bs.tab', function (event) {
            console.log(`Tab attivo: ${event.target.id}`);
        });
    });

    // Gestione delle notifiche
    const notificationsList = document.querySelector('.notifications-list');
    if (notificationsList) {
        const userEmail = document.querySelector('#user-email')?.textContent.trim();

        // Gestione click sui pulsanti delle notifiche
        notificationsList.addEventListener('click', async function (e) {
            const notificationItem = e.target.closest('.notification-item');
            if (!notificationItem) return;

            const notificationId = notificationItem.getAttribute('data-notification-id');
            if (!notificationId) return;

            // Gestione pulsante "Leggi"
            if (e.target.classList.contains('read-btn')) {
                try {
                    const formData = new URLSearchParams();
                    formData.append("email", userEmail);
                    formData.append("id", notificationId);

                    const response = await fetch("/read-notification", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: formData.toString(),
                    });

                    if (response.ok) {
                        const statusBadge = notificationItem.querySelector('.notification-status-badge');
                        statusBadge.textContent = 'Letta';
                        statusBadge.classList.remove('status-unread');
                        statusBadge.classList.add('status-read');
                    } else {
                        const errorMessage = await response.text();
                        alert("Errore nella lettura della notifica: " + errorMessage);
                    }
                } catch (error) {
                    console.error("Errore di rete:", error);
                    alert("Errore di rete. Riprova più tardi.");
                }
            }

            // Gestione pulsante "Elimina"
            if (e.target.classList.contains('delete-btn')) {
                try {
                    const formData = new URLSearchParams();
                    formData.append("email", userEmail);
                    formData.append("id", notificationId);

                    const response = await fetch("/delete-notification", {
                        method: "DELETE",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: formData.toString(),
                    });

                    if (response.ok) {
                        notificationItem.remove();

                        // Se non ci sono notifiche, aggiungi il messaggio "Nessuna notifica disponibile"
                        if (!notificationsList.querySelector('.notification-item')) {
                            const noNotificationsMsg = document.createElement('p');
                            noNotificationsMsg.className = 'no-notifications';
                            noNotificationsMsg.textContent = 'Nessuna notifica disponibile.';
                            notificationsList.parentElement.appendChild(noNotificationsMsg);
                        }
                    } else {
                        const errorMessage = await response.text();
                        alert("Errore nell'eliminazione della notifica: " + errorMessage);
                    }
                } catch (error) {
                    console.error("Errore di rete:", error);
                    alert("Errore di rete. Riprova più tardi.");
                }
            }
        });
    }
});

// Listener per chiudere i suggerimenti quando si clicca fuori
document.addEventListener('click', function(e) {
    if (!searchInput.contains(e.target) && !suggestionsContainer.contains(e.target)) {
        suggestionsContainer.style.display = 'none';
    }
});
