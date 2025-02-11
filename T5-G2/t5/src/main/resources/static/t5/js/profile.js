document.addEventListener("DOMContentLoaded", function () {
    // Log di verifica
    console.log("DOM completamente caricato e analizzato");
    // Inizializza la funzionalità di ricerca amici
    initFriendSearch();
    // Inizializza la ricerca per i tab
    setupTabSearch();
    // Inizializza la gestione notifiche
    initNotifications();
    // Gestione dei tab dei trofei
    initTrophyTabs();
    // Aggiungi stile per il testo evidenziato
    addHighlightStyle();
});

function initFriendSearch() {
    const searchInput = document.querySelector('#friend-search-input');
    const suggestionsContainer = document.querySelector('#friend-suggestions');
    
    let debounceTimeout;

    if (searchInput && suggestionsContainer) {
        searchInput.addEventListener("input", function () {
            const query = searchInput.value.trim();
            suggestionsContainer.style.display = "none"; // Nascondi subito i suggerimenti

            if (!isValidEmail(query)) {
                console.log("Email non valida per regex");
                return;
            }

            // Cancella il timeout precedente se l'utente sta ancora digitando
            clearTimeout(debounceTimeout);

            // Aggiungi un nuovo timeout per la ricerca
            debounceTimeout = setTimeout(async function () {
                try {
                    if (email) {
                        const user = await fetchUserByEmail(query);
                        if (user) {
                            displayUserSuggestions(user, suggestionsContainer);
                        } 
                    } else {
                        suggestionsContainer.style.display = "none";
                    }
                } catch (error) {
                    console.error("Errore di rete:", error);
                }
            }, 500); // Timeout di 500ms dopo l'ultimo carattere digitato
        });
    }
}

// Funzione per validare l'email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Funzione per recuperare l'utente tramite email
async function fetchUserByEmail(email) {
    const url = new URL("/user_by_email", window.location.origin);
    url.searchParams.append("email", email);

    const response = await fetch(url, { method: "GET", headers: { "Content-Type": "application/x-www-form-urlencoded" } });

    if (response.ok) {
        const user = await response.json();
        return user;
    } else {
        console.error("Errore durante la ricerca del profilo:", response.statusText);
        return null;
    }
}

// Funzione per visualizzare i suggerimenti degli utenti
function displayUserSuggestions(user, suggestionsContainer) {
    const profile = user.userProfile;
    
    suggestionsContainer.innerHTML = "";
    if (profile && user.email) {
        const profileInfo = createProfileInfo(user);
        suggestionsContainer.appendChild(profileInfo);
        suggestionsContainer.style.display = "block";
    }
}

// Crea e restituisce il contenitore per i dettagli del profilo
function createProfileInfo(user) {
    const profileInfo = document.createElement('div');
    profileInfo.className = 'profile-info';

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

    const profileBtn = createProfileButton(user);
    profileInfo.appendChild(userDetails);
    profileInfo.appendChild(profileBtn);

    return profileInfo;
}

// Crea il bottone per visualizzare il profilo
function createProfileButton(user) {
    const profileBtn = document.createElement('button');
    profileBtn.className = 'btn btn-custom btn-sm';
    profileBtn.textContent = "Visualizza Profilo";
    profileBtn.onclick = function () {
        location.href = `/friend/${user.id}`;
    };
    return profileBtn;
}

// Funzione per la ricerca amici nei tab
function setupTabSearch() {
    const searchInputs = document.querySelectorAll('.tab-search');
    searchInputs.forEach(searchInput => {
        searchInput.addEventListener('input', function () {
            handleTabSearch(this);
        });
    });
}

// Gestisce la ricerca all'interno di un tab
function handleTabSearch(searchInput) {
    const searchTerm = searchInput.value.toLowerCase();
    const targetTab = searchInput.getAttribute('data-search-target');
    const container = document.querySelector(`#${targetTab}-content .friends-list`);
    const friendItems = container.querySelectorAll('.friend-item');

    friendItems.forEach(item => {
        const name = item.querySelector('h5').textContent.toLowerCase();
        const email = item.querySelector('p').textContent.toLowerCase();

        if (name.includes(searchTerm) || email.includes(searchTerm)) {
            item.classList.remove('hidden');
            highlightText(item, searchTerm);
        } else {
            item.classList.add('hidden');
        }
    });
}

// Funzione per evidenziare il testo
function highlightText(item, searchTerm) {
    if (searchTerm === '') {
        item.querySelector('h5').innerHTML = item.querySelector('h5').textContent;
        item.querySelector('p').innerHTML = item.querySelector('p').textContent;
        return;
    }

    const nameElement = item.querySelector('h5');
    const emailElement = item.querySelector('p');

    const highlightMatch = (text, term) => {
        const regex = new RegExp(`(${term})`, 'gi');
        return text.replace(regex, '<span class="highlight">$1</span>');
    };

    nameElement.innerHTML = highlightMatch(nameElement.textContent, searchTerm);
    emailElement.innerHTML = highlightMatch(emailElement.textContent, searchTerm);
}

// Aggiungi stile per il testo evidenziato
function addHighlightStyle() {
    const style = document.createElement('style');
    style.textContent = `
        .highlight {
            background-color: rgba(0, 123, 255, 0.2);
            padding: 0 2px;
            border-radius: 3px;
        }
    `;
    document.head.appendChild(style);
}

// Funzione per gestire i tab dei trofei
function initTrophyTabs() {
    const trophyTabs = document.querySelectorAll('#trophyTabs button[data-bs-toggle="tab"]');
    trophyTabs.forEach(tab => {
        tab.addEventListener('shown.bs.tab', function (event) {
            console.log(`Tab attivo: ${event.target.id}`);
        });
    });
}

// Funzione per inizializzare la gestione delle notifiche
function initNotifications() {
    const notificationsList = document.querySelector('.notifications-list');
    if (notificationsList) {
        const userEmail = document.querySelector('#user-email')?.textContent.trim();
        notificationsList.addEventListener('click', async function (e) {
            const notificationItem = e.target.closest('.notification-item');
            if (!notificationItem) return;

            const notificationId = notificationItem.getAttribute('data-notification-id');
            if (!notificationId) return;

            // Gestione pulsante "Leggi"
            if (e.target.classList.contains('read-btn')) {
                await handleMarkAsRead(notificationItem, userEmail, notificationId);
            }

            // Gestione pulsante "Elimina"
            if (e.target.classList.contains('delete-btn')) {
                await handleDeleteNotification(notificationItem, userEmail, notificationId);
            }
        });
    }
}

// Funzione per segnare la notifica come letta
async function handleMarkAsRead(notificationItem, userEmail, notificationId) {
    try {
        const formData = new URLSearchParams();
        formData.append("email", userEmail);
        formData.append("notificationID", notificationId);
        const response = await fetch("/update_notification", {
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

// Funzione per eliminare la notifica
async function handleDeleteNotification(notificationItem, userEmail, notificationId) {
    try {
        const formData = new URLSearchParams();
        formData.append("email", userEmail);
        formData.append("notificationID", notificationId);
        const response = await fetch("/remove_notification", {
            method: "DELETE",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData.toString(),
        });

        if (response.ok) {
            notificationItem.remove();
            if (!document.querySelector('.notification-item')) {
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
