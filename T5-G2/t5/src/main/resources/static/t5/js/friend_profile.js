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

//Estrai utente da token jwt
function getAuthUserId(jwt) {
    try {
        // Estrai il payload del JWT (la seconda parte separata da ".")
        const base64Url = jwt.split('.')[1];
        // Decodifica Base64 (gestisce correttamente il padding mancante)
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        // Converte la stringa JSON in oggetto
        const payload = JSON.parse(jsonPayload);
        // Estrai userId e convertilo in numero
        return parseInt(payload.userId, 10);
    } catch (error) {
        console.error('Errore durante la decodifica del JWT:', error);
        return null;
    }
}


document.addEventListener("DOMContentLoaded", function () {
    // Gestione dei tab dei trofei
    const trophyTabs = document.querySelectorAll('#trophyTabs button[data-bs-toggle="tab"]');
    trophyTabs.forEach(tab => {
        tab.addEventListener('shown.bs.tab', function (event) {
            console.log(`Tab attivo: ${event.target.id}`);
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const followButton = document.getElementById('followButton');
    if (followButton) {
        followButton.addEventListener('click', async function() {
            const FriendUserId = this.getAttribute('data-user-id'); 
            const userId = parseInt(parseJwt(getCookie("jwt")).userId);
            console.log('Clicked! UserId:', FriendUserId);
            // Creazione del Form Data per la richiesta
            const formData = new URLSearchParams();
            formData.append("targetUserId", FriendUserId);
            formData.append("authUserId", userId);
            try {
                const response = await fetch(`/add-follow`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: formData.toString()
                });
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                // Cambia il bottone in base al suo stato corrente
                if (this.textContent === 'Follow') {
                    this.textContent = 'Unfollow';
                    this.classList.remove('btn-primary');
                    this.classList.add('btn-danger');
                } else {
                    this.textContent = 'Follow';
                    this.classList.remove('btn-danger');
                    this.classList.add('btn-primary');
                }
            } catch (error) {
                console.error('Errore nella richiesta:', error);
            }
        });
    }
    // Funzione per la ricerca amici in tempo reale
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

    document.addEventListener("DOMContentLoaded", function() {
        // Aggiungi stile per il testo evidenziato
        const style = document.createElement('style');
        style.textContent = `
            .highlight {
                background-color: rgba(0, 123, 255, 0.2);
                padding: 0 2px;
                border-radius: 3px;
            }
            .hidden {
                display: none;
            }
        `;
        document.head.appendChild(style);
        // Inizializza la ricerca
        setupTabSearch();
    });

});
