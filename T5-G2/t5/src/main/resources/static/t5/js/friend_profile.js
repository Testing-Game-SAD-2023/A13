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

                if (response.ok) {
                    const data = await response.json();
                    // Aggiorna il bottone in base alla risposta
                    if (data.following) {
                        this.textContent = 'Unfollow';
                        this.classList.remove('btn-primary');
                        this.classList.add('btn-danger');
                    } else {
                        this.textContent = 'Follow';
                        this.classList.remove('btn-danger');
                        this.classList.add('btn-primary');
                    }
                }
            } catch (error) {
                console.error('Error:', error);
            }
        });
    }
});
