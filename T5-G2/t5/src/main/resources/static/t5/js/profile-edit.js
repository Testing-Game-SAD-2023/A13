document.addEventListener("DOMContentLoaded", function () {
    const profilePictures = document.querySelectorAll(".profile-picture");
    const bioInput = document.getElementById("bio-input");
    const saveButton = document.getElementById("save-button");
    const userData = document.getElementById("player-data");

    if (!userData) return; // sicurezza

    const userEmail = userData.dataset.email;

    // Dati correnti
    const currentBio = userData.dataset.currentBio;
    const currentImage = userData.dataset.currentImage;

    let selectedImage = currentImage;

    // Selezione immagini
    profilePictures.forEach((img) => {
        img.addEventListener("click", function () {
            profilePictures.forEach((img) => img.classList.remove("selected"));
            this.classList.add("selected");
            selectedImage = this.getAttribute("src")
                .split("/")
                .pop()
                .replace(/-\w{32}\.png$/, ".png");
        });
    });

    // Salvataggio profilo
    if (saveButton && userData && bioInput) {
        const profileSuccess = document.getElementById("profileSuccess");
        const profileError = document.getElementById("profileError");

        saveButton.addEventListener("click", async function () {
            const newBio = bioInput.value.trim();
            const bioToSend = newBio || currentBio;
            const imageToSend = selectedImage || currentImage;

            // reset messaggi
            profileSuccess?.classList.add("d-none");
            profileError?.classList.add("d-none");
            saveButton.disabled = true;

            try {
                const formData = new URLSearchParams();
                formData.append("email", userEmail);
                formData.append("bio", bioToSend);
                formData.append("profilePicturePath", imageToSend);

                const response = await fetch("/api/gameEngine/update_profile", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: formData.toString(),
                });

               if (response.ok) {
                   profileSuccess.classList.remove("d-none");
                   setTimeout(() => window.location.href = "/profile", 800);
               } else {
                   const errorTextRaw = await response.text();
                   console.log("RAW ERROR:", JSON.stringify(errorTextRaw)); // utile per debug
                   const errorText = errorTextRaw.trim().replace(/^["']|["']$/g, ''); // rimuove virgolette e spazi

                   console.error("Errore update_profile:", response.status, errorText);

                   if (errorText.includes("BIO_TOO_LONG")) {
                       profileError.textContent = "Errore nella modifica: biografia troppo lunga.";
                   } else {
                       profileError.textContent = "Errore durante l'aggiornamento del profilo.";
                   }
                   profileError.classList.remove("d-none");

               }
            } catch (error) {
                console.error("Errore di rete:", error);
                profileError.textContent =
                    "Errore di rete durante l'aggiornamento del profilo. Controlla la connessione o riprova.";
                profileError.classList.remove("d-none");
            } finally {
                saveButton.disabled = false;
            }
        });
    }
});
 