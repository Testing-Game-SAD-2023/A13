// Selezione immagine e gestione eventi
document.addEventListener("DOMContentLoaded", function () {
    const profilePictures = document.querySelectorAll(".profile-picture");
    const bioInput = document.getElementById("bio-input");
    const saveButton = document.getElementById("save-button");
    const userEmail = "{{user.email}}"; // Inserisce l'email direttamente dal server
    let selectedImage = null;

    // Gestione della selezione delle immagini
    profilePictures.forEach((img) => {
        img.addEventListener("click", function () {
            // Rimuovi la classe "selected" da tutte le immagini
            profilePictures.forEach((img) => img.classList.remove("selected"));
            // Aggiungi la classe "selected" all'immagine cliccata
            this.classList.add("selected");
            selectedImage = this.getAttribute("src");
        });
    });

    // Gestione del salvataggio
    if (saveButton) {
        saveButton.addEventListener("click", async function () {
            const newBio = bioInput.value.trim();

            if (!newBio || !selectedImage) {
                alert("Devi selezionare un'immagine e inserire una bio valida.");
                return;
            }

            try {
                // Prepara i dati nel formato x-www-form-urlencoded
                const formData = new URLSearchParams();
                formData.append("email", userEmail);
                formData.append("bio", newBio);
                formData.append("profilePicturePath", selectedImage);

                // Effettua la chiamata POST
                const response = await fetch("/edit_profile", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: formData.toString(),
                });

                if (response.ok) {
                    alert("Profilo aggiornato con successo!");
                    location.reload();
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