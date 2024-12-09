document.addEventListener("DOMContentLoaded", () => {
    const editBioButton = document.getElementById("editBioButton");
    const saveBioButton = document.getElementById("saveBioButton");
    const bioDisplaySection = document.getElementById("bioDisplaySection");
    const bioEditSection = document.getElementById("bioEditSection");
    const modifyAvatarButton = document.getElementById('modifyAvatarButton');
    const saveAvatarButton = document.getElementById("saveAvatarButton");
    const bioText = document.getElementById("bioText");
    const cancelBioButton = document.getElementById("cancelBioButton");
    const biographyInput = document.getElementById("biography");
    const avatarSelection = document.getElementById('avatarSelection');
    const currentProfilePicture = document.getElementById('currentProfilePicture');
    //GabMan08/12 - Modifica info profilo
    const editInfoButton = document.getElementById("editInfoButton");
    const editInfoForm = document.getElementById("editInfoForm");
    const cancelEditInfoButton = document.getElementById("cancelEditInfoButton");

     // Aggiungi questo per il pulsante "Modifica Avatar" cami
    let selectedAvatar = null;
    
   
    

    // Mostra e nascondi la sezione di selezione dell'avatar
    if (modifyAvatarButton) {
        modifyAvatarButton.addEventListener('click', () => {
            avatarSelection.style.display = avatarSelection.style.display === 'none' ? 'block' : 'none';
        });
    }
    
    // Funzione per selezionare un avatar
    const selectAvatar = (path) => {
        selectedAvatar = path;
    
        // Rimuovi la selezione da tutte le immagini
        document.querySelectorAll('.avatar-option').forEach(img => {
            img.classList.remove('selected');
        });
    
        // Aggiungi la classe "selected" all'immagine cliccata
        const selectedImg = document.querySelector(`img[src='${path}']`);
        if (selectedImg) {
            selectedImg.classList.add('selected');
        }
    
        // Cambia l'immagine di profilo corrente
        if (currentProfilePicture) {
            currentProfilePicture.src = path;
        }
    
        // Aggiorna il valore del campo nascosto
        const hiddenInput = document.getElementById('selectedAvatar');
        if (hiddenInput) {
            hiddenInput.value = path; // Sincronizza con il campo nascosto
        } else {
            console.error("Il campo nascosto selectedAvatar non è stato trovato.");
        }
    };

    // Salva l'avatar selezionato
    const saveAvatar = async () => {
    if (!selectedAvatar) {
        alert('Seleziona un avatar prima di salvare.');
        return;
    }

    console.log('Avatar selezionato:', selectedAvatar); // Debug

    try {
        const response = await fetch('/updateAvatar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `avatar=${encodeURIComponent(selectedAvatar)}` // Dati in formato URL encoded
        });

        if (response.ok) {
            alert('Avatar salvato con successo!');

            // Nascondi la finestra degli avatar
            const avatarSelection = document.getElementById('avatarSelection');
            if (avatarSelection) {
                avatarSelection.style.display = 'none';
            }
        } else {
            const error = await response.text();
            alert(`Errore nel salvataggio dell'avatar: ${error}`);
        }
    } catch (error) {
        console.error('Errore nella connessione al server:', error);
        alert('Errore nella connessione al server.');
    }
    };

    
    // Gestisci il pulsante "Salva Avatar"
    // Intercetta il submit del form per evitare il ricaricamento della pagina
    const avatarForm = document.querySelector('form[action="/updateAvatar"]');
    if (avatarForm) {
    avatarForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Interrompe il comportamento predefinito del form
        await saveAvatar(); // Chiama la funzione per salvare l'avatar
    });
    }

    
    // Gestisci la selezione dell'avatar
    const avatarImages = document.querySelectorAll('.avatar-option');
    avatarImages.forEach(img => {
        img.addEventListener('click', () => {
            const avatarPath = img.src; // Ottieni il percorso dell'immagine
            selectAvatar(avatarPath);
        });
    });

    // Funzione per caricare l'avatar al caricamento della pagina
    const loadAvatar = async () => {
    try {
        const response = await fetch('/getAvatar', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const avatarPath = await response.text(); // Ottieni l'avatar dal backend
            if (currentProfilePicture) {
                currentProfilePicture.src = avatarPath; // Aggiorna l'immagine di profilo
            }
        } else {
            console.error('Errore nel caricamento dell\'avatar:', await response.text());
        }
    } catch (error) {
        console.error('Errore durante la connessione al backend:', error);
    }
    };

    // Carica l'avatar al caricamento della pagina
    window.addEventListener('load', loadAvatar);


    
   


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

                // Popola i campi del form per la modifica delle informazioni utente
                document.getElementById("newName").value = data.name || ""; // Valore attuale o stringa vuota
                document.getElementById("newSurname").value = data.surname || ""; // Valore attuale o stringa vuota
                document.getElementById("newNickname").value = data.nickname || ""; // Valore attuale o stringa vuota

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

    //GabMan 08/12 Gestione del form per la modifica delle informazioni utente
    // Mostra il form e nasconde il pulsante "Modifica Info Personali"
    editInfoButton.addEventListener("click", () => {
        editInfoForm.style.display = "block";
        editInfoButton.style.display = "none";
    });

    // Nasconde il form e ripristina il pulsante "Modifica Info Personali"
    cancelEditInfoButton.addEventListener("click", () => {
        editInfoForm.style.display = "none";
        editInfoButton.style.display = "inline-block";
    });

    // GabMan 8/12  Gestisce il salvataggio del form
    editInfoForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // Previene il comportamento predefinito del form

        const name = document.getElementById("newName").value;
        const surname = document.getElementById("newSurname").value;
        const nickname = document.getElementById("newNickname").value;

        try {
            const response = await fetch('/updateUserInfo', {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({ name, surname, nickname }) // Invia i dati
            });

            if (response.ok) {
                alert("Informazioni aggiornate con successo!");

                // Aggiorna la visualizzazione nel profilo
                document.getElementById("userFullName").textContent = `${name} ${surname}`;
                document.getElementById("userNickname").textContent = `@${nickname}`;

                // Nascondi il form
                editInfoForm.style.display = "none";
                editInfoButton.style.display = "inline-block";
            } else {
                const error = await response.text();
                alert("Errore nel salvataggio delle informazioni: " + error);
            }
        } catch (error) {
            alert("Errore nella connessione al server.");
        }
    });

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

