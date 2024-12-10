document.addEventListener("DOMContentLoaded", function () {
    const profilePictures = document.querySelectorAll(".profile-picture");
    const bioInput = document.getElementById("bio-input");
    const saveButton = document.getElementById("save-button");
    const userData = document.getElementById("user-data");
    const userEmailElement = document.getElementById("user-email"); // Inserisce l'email direttamente dal server
    const userEmail = userEmailElement ? userEmailElement.textContent.trim() : null;

    // Ottieni i dati dell'utente
    const currentBio = userData.dataset.currentBio;
    const currentImage = userData.dataset.currentImage;

    // Inizializza le variabili con i valori correnti
    let selectedImage = null;

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

            // Usa i valori correnti se non sono stati modificati
            const bioToSend = newBio || currentBio;
            const imageToSend = selectedImage || currentImage;

            try {
                const formData = new URLSearchParams();
                formData.append("email", userEmail);
                formData.append("bio", bioToSend);
                formData.append("profilePicturePath", imageToSend);

                const response = await fetch("/update-profile", {
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