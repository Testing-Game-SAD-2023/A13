/*
 * Copyright (c) 2025 Stefano Marano
 * Gestione Upload Suggerimenti
 * Versione: Multi-lingua Support (Reads Cookies)
 */

document.addEventListener("DOMContentLoaded", () => {
    const uploadForm = document.getElementById('uploadHintsForm');

    if (uploadForm) {
        uploadForm.addEventListener('submit', handleUploadSubmit);
    }
});

async function handleUploadSubmit(event) {
    event.preventDefault();

    const fileInput = document.getElementById('hintsFile');
    const imagesInput = document.getElementById('hintImages');
    const submitBtn = event.target.querySelector('button[type="submit"]');

    if (!fileInput.files.length) {
        showFeedbackModal("Attenzione", "Seleziona il file JSON!", true);
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    if (imagesInput.files.length > 0) {
        for (let i = 0; i < imagesInput.files.length; i++) {
            formData.append('images', imagesInput.files[i]);
        }
    }

    const originalBtnText = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = `<i class="fa fa-spinner fa-spin"></i> Loading...`;

    try {
        const token = getCookie("jwtToken");

        // --- MODIFICA FONDAMENTALE PER LA LINGUA ---
        // 1. Cerchiamo il cookie 'lang', se non c'è cerchiamo 'language', altrimenti default 'en'
        // Nota: getCookie deve essere definita nel tuo cookie_util.js
        const userLang = getCookie("lang") || getCookie("language") || "en";

        const response = await fetch('/hints/upload', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                // 2. FORZIAMO L'HEADER CON LA LINGUA DEL COOKIE
                'Accept-Language': userLang
            },
            body: formData
        });

        if (response.ok) {
            // Successo (Il messaggio arriverà in Inglese dal backend)
            // Nota: Se il backend manda solo la stringa, la usiamo.
            // Se vuoi puoi anche gestire il titolo del modale in base alla lingua qui,
            // ma il messaggio del server sarà già tradotto.
            const successMsg = await response.text();
            showFeedbackModal("Success", successMsg || "Hints uploaded successfully!", false);

            document.getElementById('uploadHintsForm').reset();
        } else {
            // === LOGICA PARSING ERRORE ===
            let errorMessage = "Error during upload.";

            try {
                const errorJson = await response.json();

                if (errorJson.message) {
                    errorMessage = errorJson.message;
                } else if (errorJson.error) {
                    errorMessage = errorJson.error;
                }
            } catch (e) {
                const rawText = await response.text();
                if (rawText) errorMessage = rawText;
            }

            // Pulizia prefissi (es. "400 ...")
            errorMessage = errorMessage.replace(/^\d{3}\s+/, '');

            showFeedbackModal("Error", errorMessage, true);
        }

    } catch (error) {
        console.error("Network Error:", error);
        showFeedbackModal("Communication Error", "Unable to contact the server.", true);
    } finally {
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalBtnText;
    }
}

// === FUNZIONE HELPER PER IL MODALE ===
function showFeedbackModal(title, message, isError = false) {
    const modalTitle = document.getElementById('successModalLabel');
    const modalBody = document.getElementById('successMessageContent');

    if (modalTitle && modalBody) {
        modalTitle.innerHTML = isError ? `<i class="fa fa-exclamation-triangle"></i> ${title}` : `<i class="fa fa-check-circle"></i> ${title}`;
        modalBody.innerHTML = message;

        modalTitle.classList.remove('text-danger', 'text-success');

        if (isError) {
            modalTitle.classList.add('text-danger');
        } else {
            modalTitle.classList.add('text-success');
        }

        $('#successModal').modal('show');
    } else {
        alert(title + ": " + message);
    }
}