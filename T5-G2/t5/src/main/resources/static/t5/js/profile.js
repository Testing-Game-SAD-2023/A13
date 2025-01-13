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
    const editInfoButton = document.getElementById("editInfoButton");
    const editInfoForm = document.getElementById("editInfoForm");
    const cancelEditInfoButton = document.getElementById("cancelEditInfoButton");
    const uploadButton = document.getElementById("uploadProfilePictureButton"); // Pulsante "+"
    const uploadForm = document.getElementById("uploadProfilePictureForm"); // Form di upload
    const uploadSubmitButton = document.getElementById("uploadProfilePictureSubmitButton"); // Pulsante "Upload"
    const cancelAvatarButton = document.getElementById('cancelAvatarButton');
    

     // Aggiungi questo per il pulsante "Modifica Avatar" cami
    let selectedAvatar = null;
    
   
    

    // Mostra e nascondi la sezione di selezione dell'avatar
    if (modifyAvatarButton) {
        modifyAvatarButton.addEventListener('click', () => {
            avatarSelection.style.display = avatarSelection.style.display === 'none' ? 'block' : 'none';
        });
    }

    
    if (cancelAvatarButton) {
        cancelAvatarButton.addEventListener('click', () => {
            avatarSelection.style.display = 'none';
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

    // Funzione per caricare l'avatar o l'immagine del profilo
    const loadAvatarOrImage = async () => {
    try {
        const response = await fetch('/getImage');
        const data = await response.json();

        if (response.ok && data.imageUrl) {
            currentProfilePicture.src = data.imageUrl;
        } else {
            currentProfilePicture.src = '/t5/images/sampleavatar.jpg'; // Fallback
        }
    } catch (error) {
        console.error("Errore durante il caricamento dell'immagine:", error);
        currentProfilePicture.src = '/t5/images/sampleavatar.jpg'; // Fallback in caso di errore
    }
    };
    window.addEventListener('load', loadAvatarOrImage);

    

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

    
    
    // form per il salvataggio dell'avatar
    const avatarForm = document.querySelector('form[action="/updateAvatar"]');
    if (avatarForm) {
    avatarForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Interrompo il comportamento predefinito del form
        await saveAvatar(); 
    });
    }

    
    //selezione dell'avatar
    const avatarImages = document.querySelectorAll('.avatar-option');
    avatarImages.forEach(img => {
        img.addEventListener('click', () => {
            const avatarPath = img.src; // percorso dell'immagine
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
            const avatarPath = await response.text(); // avatar dal backend
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
            const response = await fetch('/getBiography');
            if (response.ok) {
                const data = await response.json();
                bioText.textContent = data.biography || 'La tua biografia...';
            } else {
                console.error('Errore nel caricamento della biografia.');
            }

    }
      
   
    

    
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
                document.getElementById('userNickname').textContent = `@${data.nickname}`;

                // header che porta info in tutte le sezioni
                document.getElementById('headerFullName').textContent = `${data.name} ${data.surname}`;
                document.getElementById('headerNickname').textContent = `@${data.nickname}`;

            } else {
                console.error('Errore nel caricamento delle informazioni utente.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };

    //User Id GabMan 10/12
    const displayUserId = async () => {
        try {
            // Chiamata all'endpoint per ottenere l'userId
            const response = await fetch('/getUserId');
            if (response.ok) {
                const data = await response.json();
                const userId = data.userId;
    
                // Mostra l'userId in un elemento HTML
                const userIdElement = document.getElementById('userIdDisplay');
                if (userIdElement) {
                    userIdElement.textContent = `User ID: ${userId}`;
                } else {
                    console.error("Elemento HTML per visualizzare l'userId non trovato.");
                }
            } else {
                console.error('Errore nel recupero dell\'userId.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
        }
    };
    
    // Esegui la funzione quando la pagina viene caricata
    window.onload = displayUserId;
    

    // Carica le informazioni al caricamento della pagina
    loadBiography();
    loadUserInfo();

    //GabMan 08/12 Gestione del form per la modifica delle informazioni utente
    
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


    

    // Passa alla modalità modifica
    editBioButton.addEventListener("click", () => {
        biographyInput.value = bioText.textContent.trim();
        bioDisplaySection.style.display = "none";
        bioEditSection.style.display = "block";
    });

    // Salva la biografia
    saveBioButton.addEventListener("click", async () => {
        const bio = biographyInput.value;
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
    
            // Mostra o nascondi l'header utente
            const userHeader = document.getElementById('userHeader');
            if (sectionId === "profile") {
                userHeader.classList.add("d-none"); // Nascondi nel profilo
            } else {
                userHeader.classList.remove("d-none"); // Mostra in altre sezioni
            }
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

    
    //by GabMan 12/12 FORM di Upload Immagine
    if (uploadButton && uploadForm) {
        uploadButton.addEventListener("click", () => {
            uploadForm.style.display = uploadForm.style.display === "none" ? "block" : "none";
        });
    }

    const loadImage = async () => {
        try {
            const response = await fetch('/getImage');
            console.log("Risposta fetch /getImage:", response); // DEBUG
            if (response.ok) {
                const data = await response.json();
                console.log("Data ricevuta da /getImage:", data); // DEBUG
                if (data.imageUrl) {
                    document.getElementById("currentProfilePicture").src = data.imageUrl;
                } else {
                    console.warn("Nessuna immagine trovata.");
                }
            } else {
                console.error("Errore nel caricamento dell'immagine:", await response.text());
            }
        } catch (error) {
            console.error("Errore durante la connessione al backend:", error);
        }
    };
    
    // Carica l'avatar al caricamento della pagina
    window.addEventListener('load', loadImage);
    
    
       
    // by cami 14/12 Funzione per il caricamento dell'immagine (chiamata dal pulsante "Upload")
    const uploadProfilePicture = async () => {
        const fileInput = document.getElementById("profilePictureUploadInput");
    
        if (!fileInput.files.length) {
            alert("Seleziona un file da caricare.");
            return;
        }
    
        const file = fileInput.files[0];
        const reader = new FileReader();
    
        reader.onload = async () => {
            const base64Image = reader.result.split(",")[1]; // Rimuove il prefisso "data:image/*;base64,"
    
            try {
                const response = await fetch('/updateProfilePicture', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ profilePicture: base64Image }),
                });
    
                if (response.ok) {
                    alert("Immagine caricata con successo!");
                    loadImage(); // Aggiorna l'immagine

                    if (uploadForm) {
                        uploadForm.style.display = "none";
                    }
                    
                    // Nascondi l'intera finestra di selezione avatar
                    if (avatarSelection) {
                        avatarSelection.style.display = "none";
                    }
                } else {
                    const error = await response.text();
                    alert(`Errore durante il caricamento: ${error}`);
                }
            } catch (error) {
                console.error("Errore durante il caricamento:", error);
                alert("Si è verificato un errore durante il caricamento.");
            }
        };
    
        reader.readAsDataURL(file);
    };
    

    
    // Collega la funzione al clic del pulsante "Upload"
    if (uploadSubmitButton) {
        uploadSubmitButton.addEventListener("click", async () => {
            const loadingMessage = document.getElementById("loadingMessage");
    
            // Verifica che un file sia stato selezionato
            if (document.getElementById("profilePictureUploadInput").files.length === 0) {
                alert("Nessun file selezionato.");
                return;
            }
    
            // Mostra il messaggio di caricamento
            if (loadingMessage) {
                loadingMessage.style.display = "block";
            }

            await loadImage();
    
            try {
                // Chiama la funzione per eseguire l'upload
                await uploadProfilePicture();
            } catch (error) {
                console.error("Errore durante l'upload:", error);
            } finally {
                // Nascondi il messaggio di caricamento dopo il completamento
                if (loadingMessage) {
                    loadingMessage.style.display = "none";
                }
            }
        });
    }
    document.addEventListener("DOMContentLoaded", async function () {
        try {
            const response = await fetch('/api/getAchievements');
            const achievements = await response.json();
            console.log("Achievements caricati:", achievements); // Aggiungi questo log
    
            achievements.forEach(achievement => {
                console.log("Aggiornamento achievement:", achievement); // Aggiungi questo log
                const achievementElement = document.getElementById(`achievement-${achievement.ID}`);
                if (achievementElement) {
                    const progressBar = achievementElement.querySelector('.progress-bar');
                    const progressPercent = (achievement.Progress / achievement.ProgressRequired) * 100;
    
                    progressBar.style.width = `${progressPercent}%`;
                    progressBar.innerText = `${achievement.Progress}/${achievement.ProgressRequired}`;
                    progressBar.setAttribute('aria-valuenow', achievement.Progress);
                    progressBar.setAttribute('aria-valuemax', achievement.ProgressRequired);
                } else {
                    console.warn(`Elemento HTML non trovato per ID: achievement-${achievement.ID}`);
                }
            });
        } catch (error) {
            console.error("Errore durante il caricamento degli achievement:", error);
        }
    });
        // Mostra Trofei Sbloccati
    window.showCompleted = function () {
        document.getElementById("completed-trophies").classList.add("visible");
        document.getElementById("completed-trophies").classList.remove("hidden");
        document.getElementById("in-progress-trophies").classList.add("hidden");
        document.getElementById("in-progress-trophies").classList.remove("visible");

        // Cambia lo stile dei bottoni
        document.getElementById("btnCompleted").classList.add("btn-primary");
        document.getElementById("btnCompleted").classList.remove("btn-secondary");
        document.getElementById("btnInProgress").classList.add("btn-secondary");
        document.getElementById("btnInProgress").classList.remove("btn-primary");
          
        confetti({
            particleCount: 150,
            spread: 120,
            origin: { y: 0.6 }, // Altezza di lancio
            colors: ['#ff0', '#f00', '#0f0', '#00f', '#f0f']
        });
        
        
    };

    //byGabman
    const changeLanguage = async (lang) => {
        try {
            const response = await fetch('/changeLanguage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ lang })
            });

            if (response.ok) {
                alert(`Lingua cambiata con successo a: ${lang === 'it' ? 'Italiano' : 'English'}`);
                location.reload(); // Ricarica la pagina per applicare la nuova lingua
            } else {
                alert('Errore durante il cambio della lingua.');
            }
        } catch (error) {
            console.error('Errore nella connessione al server:', error);
            alert('Errore durante il cambio della lingua.');
        }
    };

    // Event listener per il cambio lingua
    document.getElementById("btnItalian").addEventListener("click", () => {
        changeLanguage('it');
    });

    document.getElementById("btnEnglish").addEventListener("click", () => {
        changeLanguage('en');
    });

    // Mostra Trofei in Progresso
    window.showInProgress = function () {
        document.getElementById("in-progress-trophies").classList.add("visible");
        document.getElementById("in-progress-trophies").classList.remove("hidden");
        document.getElementById("completed-trophies").classList.add("hidden");
        document.getElementById("completed-trophies").classList.remove("visible");

        // Cambia lo stile dei bottoni
        document.getElementById("btnInProgress").classList.add("btn-primary");
        document.getElementById("btnInProgress").classList.remove("btn-secondary");
        document.getElementById("btnCompleted").classList.add("btn-secondary");
        document.getElementById("btnCompleted").classList.remove("btn-primary");
    };

    // Inizializza la visibilità delle sezioni al caricamento della pagina
    document.addEventListener("DOMContentLoaded", () => {
        document.getElementById("completed-trophies").classList.add("hidden");
        document.getElementById("in-progress-trophies").classList.add("visible");
    });

   

    
});

