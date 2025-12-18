/*
 * Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 * All rights reserved.
 */

// === FUNZIONI DI UTILITY INTERNE ===

/**
 * Funzione che gestisce il cambio lingua:
 * 1. Salva nel LocalStorage (per il frontend JS)
 * 2. Salva nel Cookie (per il backend Java)
 * 3. Ricarica la pagina con ?lang=xx (per Thymeleaf)
 */
function changeLanguage(lang) {
    console.log("Cambio lingua in corso:", lang);

    // 1. Salva nel LocalStorage
    localStorage.setItem('language', lang);

    // 2. Salva nel Cookie (scadenza 30 giorni)
    document.cookie = "language=" + lang + "; path=/; max-age=" + (60*60*24*30);

    // 3. Ricarica forzata aggiungendo il parametro GET per Spring Boot
    const url = new URL(window.location.href);
    url.searchParams.set("lang", lang);
    window.location.href = url.toString();
}

// === INIZIALIZZAZIONE AL CARICAMENTO DELLA PAGINA ===
document.addEventListener("DOMContentLoaded", () => {

    // --- 1. GESTIONE REDIRECT MENU ---

    const usersBtn = document.getElementById("dashboard_users_redirect_btn");
    if (usersBtn) {
        usersBtn.addEventListener("click", () => {
            // Assicurati che VIEWS sia definito nel file endpoints.js incluso prima di questo script
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.USERS_TABLE;
        });
    }

    const adminsBtn = document.getElementById("dashboard_admins_redirect_btn");
    if (adminsBtn) {
        adminsBtn.addEventListener("click", () => {
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.ADMINS_TABLE;
        });
    }

    // --- 2. GESTIONE LOGOUT ---

    const logoutBtn = document.getElementById("logout_admin_btn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", async () => {
            // Assicurati che callLogoutAdmin sia definito in api.js
            if (typeof callLogoutAdmin === 'function') {
                await callLogoutAdmin();
            } else {
                console.error("Funzione callLogoutAdmin non trovata.");
            }
        });
    }

    // --- 3. GESTIONE BOTTONI DASHBOARD ---

    const btnScalata = document.querySelector(".button-scalata");
    if (btnScalata) {
        btnScalata.addEventListener("click", () => {
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.SCALATA_MAIN;
        });
    }

    const btnTeam = document.querySelector(".button-team");
    if (btnTeam) {
        btnTeam.addEventListener("click", () => {
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.TEAMS_MAIN;
        });
    }

    const btnClass = document.querySelector(".button-class");
    if (btnClass) {
        btnClass.addEventListener("click", function () {
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.OPPONENTS_MAIN;
        });
    }

    const btnHints = document.querySelector(".button-hints");
    if (btnHints) {
        btnHints.addEventListener("click", function () {
            if (typeof VIEWS !== 'undefined') window.location.href = VIEWS.HINTS_MAIN;
        });
    }

    // --- 4. GESTIONE EMAIL ADMIN ---

    const emailDisplay = document.getElementById("email-display");
    if (emailDisplay) {
        // getCookie deve essere definito in cookie_util.js
        if (typeof getCookie === 'function') {
            const cookie = getCookie("jwt");
            if (cookie) {
                try {
                    // parseJwt deve essere definito globalmente o qui
                    const parseJwt = (token) => {
                        try {
                            return JSON.parse(atob(token.split('.')[1]));
                        } catch (e) {
                            return null;
                        }
                    };

                    const jwtData = parseJwt(cookie);
                    if (jwtData && jwtData.sub) {
                        emailDisplay.textContent = jwtData.sub;
                    }
                } catch (e) {
                    console.error("Errore parsing JWT:", e);
                }
            }
        }
    }

    // --- 5. GESTIONE SIDEBAR RESPONSIVE ---

    const sidebar = document.querySelector(".sidebar");
    const sidebarToggle = document.querySelector(".sidebar-toggle");

    if (sidebar && sidebarToggle) {
        function handleResize() {
            if (window.innerWidth <= 768) {
                sidebar.style.display = "none";
                sidebarToggle.style.display = "block";
                sidebar.classList.remove("open");
            } else {
                sidebar.style.display = "block";
                sidebarToggle.style.display = "none";
            }
        }

        sidebarToggle.addEventListener("click", function () {
            sidebar.classList.toggle("open");
            if (sidebar.classList.contains("open")) {
                sidebar.style.display = "block";
            } else {
                sidebar.style.display = "none";
            }
        });

        window.addEventListener("resize", handleResize);
        // Esegui subito al caricamento
        handleResize();
    }

    // --- 6. GESTIONE CAMBIO LINGUA (FIXED) ---

    // Seleziona tutti i tag <a> con classe "language_select"
    const langButtons = document.querySelectorAll('.language_select');

    if (langButtons.length > 0) {
        langButtons.forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault(); // Evita scroll in alto o reload standard

                // Legge l'attributo data-lang (es. data-lang="it")
                const lang = btn.getAttribute('data-lang');

                if (lang) {
                    changeLanguage(lang);
                } else {
                    console.error("Attributo data-lang mancante sul pulsante cliccato.");
                }
            });
        });
    } else {
        console.warn("Nessun elemento con classe .language_select trovato.");
    }

});