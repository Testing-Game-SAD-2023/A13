// Gestione cambio lingua
function changeLanguage(lang) {
    fetch("/api/gameEngine/changeLanguage?lang=" + lang, {
        method: "POST",
    })
        .then((response) => {
            if (response.ok) {
                console.log("Lingua cambiata con successo.");
                location.reload(); // Ricarica la pagina per applicare il cambio lingua
            } else {
                console.error("Errore nel cambio lingua:", response.statusText);
            }
        })
        .catch((error) => {
            console.error("Errore nella richiesta:", error);
        });
}

// Aggiorna icona e testo del pulsante dropdown in base al cookie
function updateLanguageIcon() {
    const lang = getCookie("lang");
    const languageItems = document.querySelectorAll(".language_select");

    languageItems.forEach((item) => {
        const langData = item.getAttribute("data-lang");
        if (langData === lang) {
            const svgIcon = item.querySelector("svg").outerHTML;
            const icon = document.getElementById("dropdownIcon");
            icon.innerHTML = svgIcon;

            const langText = item.querySelector("h6").textContent;
            icon.setAttribute("aria-label", langText); // Etichetta per accessibilità
        }
    });
}

//  MODIFICA: Event Delegation per intercettare i click sui selettori lingua
document.addEventListener("click", (event) => {
    const target = event.target.closest(".language_select");
    if (!target) return;

    const lang = target.getAttribute("data-lang");
    changeLanguage(lang);
    updateLanguageIcon();
});

// Imposta cookie lingua di default se non presente e aggiorna icona
document.addEventListener("DOMContentLoaded", function () {
    const langCookie = getCookie("lang");
    if (!langCookie) {
        setCookie("lang", "it", 7 * 24 * 60 * 60); // Default: italiano
        console.log("Cookie 'lang' impostato su 'it'");
    } else {
        console.log("Cookie 'lang' esistente: " + langCookie);
    }
    updateLanguageIcon();
});
