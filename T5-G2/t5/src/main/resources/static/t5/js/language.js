// Gestione tasto lingua navbar
function changeLanguage(lang) {
    fetch("/api/gameEngine/changeLanguage?lang=" + lang, {
        method: "POST",
    })
        .then((response) => {
            if (response.ok) {
                console.log("Lingua cambiata con successo.");
                // Qui puoi aggiornare la UI o ricaricare la pagina se necessario
                location.reload(); // Ricarica la pagina per applicare il cambiamento di lingua
            } else {
                console.error("Errore nel cambio lingua:", response.statusText);
            }
        })
        .catch((error) => {
            console.error("Errore nella richiesta:", error);
        });
}

// Funzione per aggiornare l'icona e il testo del pulsante dropdown in base al cookie
function updateLanguageIcon() {
    const lang = getCookie("lang");
    const languageItems = document.querySelectorAll(".language_select");

    // Cerca l'elemento corrispondente alla lingua del cookie
    languageItems.forEach((item) => {
        const langData = item.getAttribute("data-lang");
        if (langData === lang) {
            // Ottieni l'icona SVG dell'elemento corrispondente
            const svgIcon = item.querySelector("svg").outerHTML;

            // Cambia l'icona del pulsante dropdown con l'icona trovata
            const icon = document.getElementById("dropdownIcon");
            icon.innerHTML = svgIcon;

            // Aggiorna il testo del pulsante con la lingua selezionata
            const langText = item.querySelector("h6").textContent;
            icon.setAttribute("aria-label", langText); // Imposta un'etichetta aria per l'accessibilità
        }
    });
}

// Aggiungi evento click per tutti gli elementi della lingua
document.querySelectorAll(".language_select").forEach((item) => {
    item.addEventListener("click", (event) => {
        const lang = event.currentTarget.getAttribute("data-lang"); // Ottieni il valore della lingua
        changeLanguage(lang); // Chiama la funzione per cambiare lingua
        updateLanguageIcon();
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const langCookie = getCookie("lang");
    if (!langCookie) {
        // Se il cookie non esiste, creane uno di default
        setCookie("lang", "it", 7 * 24 * 60 * 60); // Imposta il cookie per 7 giorni
        console.log("Cookie 'lang' impostato su 'it'");
    } else {
        console.log("Cookie 'lang' esistente: " + langCookie);
    }
    updateLanguageIcon();
});