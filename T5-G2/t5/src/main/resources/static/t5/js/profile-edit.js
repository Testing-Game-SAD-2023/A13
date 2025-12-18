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

    const profilePictures = document.querySelectorAll(".profile-picture");
    const bioInput = document.getElementById("bio-input");
    const saveButton = document.getElementById("save-button");
    
    // ============================== MODIFICATO (player al posto di user)
    // L'HTML usa id="player-data", quindi il JS deve leggere questo id. Prima cercavi "user-data", che NON ESISTEVA → userData era null → errore in console
    const userData = document.getElementById("player-data"); // modifica
    
    // ============================== MODIFICATO (player al posto di user)
    // L'HTML usa un pulsante con id="player-email" (non user-email). Ora il JS legge correttamente l'email dal testo del pulsante.
    const userEmailElement = document.getElementById("player-email");
    
    // ============================== MODIFICATO: input hidden email per UserUtil.js
    const emailInput = document.getElementById("email");

    // ============================== MODIFICATO: div notifications per notification.js
    const notificationsDiv = document.getElementById("notifications");
    

    // Ottieni i dati dell'utente
    const currentBio = userData.dataset.currentBio;
    const currentImage = userData.dataset.currentImage;
    const userEmail = userEmailElement ? userEmailElement.textContent.trim() : null;


    // Inizializza le variabili con i valori correnti 

    // ============================== MODIFICATO
    // Prima selectedImage era null, quindi se l'utente non cliccava nessuna immagine il profilo veniva salvato con "null".
    // Ora invece parte dall'immagine corrente salvata nel DB (currentImage).
    let selectedImage = currentImage; 

    // ============================== AGGIUNTA: Recupera l'input nickname dall'HTML
    const nicknameInput = document.getElementById("nickname-input");


    // Gestione della selezione delle immagini
    profilePictures.forEach((img) => {
        img.addEventListener("click", function () {
            profilePictures.forEach((img) => img.classList.remove("selected"));
            this.classList.add("selected");
            selectedImage = this.getAttribute("src").split("/").pop().replace(/-\w{32}\.png$/, ".png");
        });
    });

    // Gestione del salvataggio
    if (saveButton) {
        saveButton.addEventListener("click", async function () {
            const newBio = bioInput.value.trim();


            // ============================== AGGIUNTA: Legge il valore del nickname dall'input
            const nicknameValue = nicknameInput ? nicknameInput.value.trim() : ""; 


            // Usa i valori correnti se non sono stati modificati
            const bioToSend = newBio || currentBio;
            const imageToSend = selectedImage || currentImage;

            try {
                const formData = new URLSearchParams();
                formData.append("email", userEmail);
                formData.append("bio", bioToSend);
                formData.append("profilePicturePath", imageToSend);

                formData.append("nickname", nicknameValue); //AGGIUNTO


                // ============================== MODIFICA (aggiunta /api/gameEngine per comunicazione da front-end)
                const response = await fetch("/api/gameEngine/update_profile", {  
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: formData.toString(),
                });


                if (response.ok) {
                    alert("Profilo aggiornato con successo!");
                    // Torna alla pagina precedente e ricaricala
                    window.location.href = document.referrer;
                    // Se document.referrer non dovesse funzionare, usa questo come fallback
                    setTimeout(() => {
                        window.location.href = "/profile";
                    }, 100);
                } else {
                    const errorMessage = await response.text();
                    alert("Errore nell'aggiornamento del profilo: " + errorMessage);
                }
            } catch (error) {
                console.error("Errore di rete:", error);
                alert("Errore di rete. Riprova più tardi.");
            }
        });
    }
});

