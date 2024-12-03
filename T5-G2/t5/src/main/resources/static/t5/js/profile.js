document.addEventListener("DOMContentLoaded", () => {
    const editBioButton = document.getElementById("editBioButton");
    const saveBioButton = document.getElementById("saveBioButton");
    const bioDisplaySection = document.getElementById("bioDisplaySection");
    const bioEditSection = document.getElementById("bioEditSection");
    const bioText = document.getElementById("bioText");
    const cancelBioButton = document.getElementById("cancelBioButton");
    const biographyInput = document.getElementById("biography");

    // Funzione per caricare la biografia dal database
    const loadBiography = async () => {
        try {
            const response = await fetch('/getBiography');
            if (response.ok) {
                const data = await response.json();
                bioText.textContent = data.biography || 'La tua biografia...';
            } else {
                console.error('Errore nel caricamento della biografia.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    // Funzione per caricare le informazioni dell'utente
    const loadUserInfo = async () => {
        try {
            const response = await fetch('/getUserInfo'); // Endpoint per ottenere info utente
            if (response.ok) {
                const data = await response.json();

                // Aggiorna gli elementi HTML con i dati utente
                document.getElementById('userFullName').textContent = `${data.name} ${data.surname}`;
                document.getElementById('userNickname').textContent = data.nickname;
            } else {
                console.error('Errore nel caricamento delle informazioni utente.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    // Carica le informazioni al caricamento della pagina
    loadBiography();
    loadUserInfo();

    const testAchievementsAPI = async () => {
        try {
            // Chiamata all'endpoint del backend
            const response = await fetch('/api/getAchievements'); // Assicurati che l'URL sia corretto
            if (response.ok) {
                const achievements = await response.json(); // Converte la risposta in JSON
    
                // Mostra i dati nella console del browser per debug
                console.log('Achievements:', achievements);
    
                // Mostra gli achievement sulla pagina (opzionale)
                const achievementList = document.getElementById('achievementList');
                if (achievementList) {
                    achievementList.innerHTML = ''; // Pulisci eventuali dati precedenti
                    achievements.forEach(achievement => {
                        const achievementItem = document.createElement('div');
                        achievementItem.classList.add('achievement-item');
                        achievementItem.innerHTML = `
                            <h3>${achievement.name}</h3>
                            <p>${achievement.description}</p>
                            <p>Progress Required: ${achievement.progressRequired}</p>
                        `;
                        achievementList.appendChild(achievementItem);
                    });
                }
            } else {
                console.error('Errore nel recupero degli achievement:', response.status);
            }
        } catch (error) {
            console.error('Errore di connessione:', error);
        }
    };
    
    // Chiamare questa funzione al caricamento della sezione "Trofei"
    document.getElementById('btnTrophies').addEventListener('click', testAchievementsAPI);
    

    // Passa alla modalità modifica
    editBioButton.addEventListener("click", () => {
        biographyInput.value = bioText.textContent.trim();
        bioDisplaySection.style.display = "none";
        bioEditSection.style.display = "block";
    });

    // Salva la biografia
    saveBioButton.addEventListener("click", async () => {
        const bio = biographyInput.value;

        try {
            const response = await fetch("/updateBiography", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({ biography: bio })
            });

            if (response.ok) {
                bioText.textContent = bio;
                bioEditSection.style.display = "none";
                bioDisplaySection.style.display = "block";

                const saveMessage = document.getElementById("saveMessage");
                saveMessage.style.display = "block";
                setTimeout(() => {
                    saveMessage.style.display = "none";
                }, 3000);
            } else {
                const error = await response.text();
                alert("Errore nel salvataggio: " + error);
            }
        } catch (error) {
            alert("Errore nella connessione al server.");
        }
    });

    // Gestisci il pulsante "Annulla"
    cancelBioButton.addEventListener("click", () => {
        bioEditSection.style.display = "none";
        bioDisplaySection.style.display = "block";
    });

    // Gestione dei pulsanti attivi
    const buttons = {
        profile: document.getElementById("btnProfile"),
        trophies: document.getElementById("btnTrophies"),
        stats: document.getElementById("btnStats"),
        friends: document.getElementById("btnFriends"),
        leaderboard: document.getElementById("btnLeaderboard"),
    };

    const activateButton = (sectionId) => {
        Object.values(buttons).forEach(button => button.classList.remove("btn-active"));
        if (buttons[sectionId]) {
            buttons[sectionId].classList.add("btn-active");
        } else {
            console.error(`Pulsante con ID '${sectionId}' non trovato.`);
        }
    };

    const showSection = (sectionId) => {
        document.querySelectorAll('.section').forEach(section => section.classList.remove('active'));
        const selectedSection = document.getElementById(sectionId);
        if (selectedSection) {
            selectedSection.classList.add('active');
        } else {
            console.error(`Sezione con ID '${sectionId}' non trovata.`);
        }
    };

    Object.keys(buttons).forEach(sectionId => {
        buttons[sectionId]?.addEventListener("click", () => {
            activateButton(sectionId);
            showSection(sectionId);
        });
    });

    activateButton("profile");
    showSection("profile");
});
