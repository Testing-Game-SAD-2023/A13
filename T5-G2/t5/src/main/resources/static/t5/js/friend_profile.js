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
            const userId = this.getAttribute('data-user-id');
            console.log('Clicked! UserId:', userId); // Verifica il valore quando si clicca

            try {
                const response = await fetch(`/follow/${userId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

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
                console.error('Error:', error);
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
