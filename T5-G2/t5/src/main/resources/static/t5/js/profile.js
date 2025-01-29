/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
                const url = new URL("/user_by_email", window.location.origin);
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

    //Funzione per la ricerca amici in tempo reale
    function setupTabSearch() {
        const searchInputs = document.querySelectorAll('.tab-search');

        searchInputs.forEach(searchInput => {
            searchInput.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();
                const targetTab = this.getAttribute('data-search-target');
                const container = document.querySelector(`#${targetTab}-content .friends-list`);
                const friendItems = container.querySelectorAll('.friend-item');

                friendItems.forEach(item => {
                    const name = item.querySelector('h5').textContent.toLowerCase();
                    const email = item.querySelector('p').textContent.toLowerCase();

                    if (name.includes(searchTerm) || email.includes(searchTerm)) {
                        item.classList.remove('hidden');
                        // Evidenzia il testo che corrisponde alla ricerca
                        highlightText(item, searchTerm);
                    } else {
                        item.classList.add('hidden');
                    }
                });
            });
        });
    }

    // Funzione per evidenziare il testo cercato
    function highlightText(item, searchTerm) {
        if (searchTerm === '') {
            // Ripristina il testo originale se la ricerca è vuota
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
    const style = document.createElement('style');
    style.textContent = `
        .highlight {
            background-color: rgba(0, 123, 255, 0.2);
            padding: 0 2px;
            border-radius: 3px;
        }
    `;
    document.head.appendChild(style);

    // Inizializza la ricerca
    setupTabSearch();

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
                    formData.append("id notifica", notificationId);
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

            // Gestione pulsante "Elimina"
            if (e.target.classList.contains('delete-btn')) {
                try {
                    const formData = new URLSearchParams();
                    formData.append("email", userEmail);
                    formData.append("idnotifica", notificationId);
                    const response = await fetch("/remove_notification", {
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