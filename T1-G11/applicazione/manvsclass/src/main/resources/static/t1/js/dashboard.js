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

// Imposto l'url del redirect alla tabella degli utenti/giocatori registrati
document.getElementById("dashboard_users_redirect_btn").addEventListener("click", () => {
  window.location.href=VIEWS.USERS_TABLE;
});

// Imposto l'url del redirect alla tabella degli amministratori registrati
document.getElementById("dashboard_admins_redirect_btn").addEventListener("click", () => {
  window.location.href=VIEWS.ADMINS_TABLE;
});

// Aggiungo la chiamata di logout
document.getElementById("logout_admin_btn").addEventListener("click", async () => {
  await callLogoutAdmin();
});

// Imposto l'url del redirect alla pagina della modalità scalata
document.querySelector(".button-scalata").addEventListener("click", () => {
    window.location.href = VIEWS.SCALATA_MAIN + "?tour=true";
});

// Imposto l'url del redirect alla pagina dei team
document.querySelector(".button-team").addEventListener("click", () => {
  window.location.href=VIEWS.TEAMS_MAIN;
});

// Imposto l'url del redirect alla pagina di upload di classi di test e avversari
document.querySelector(".button-class").addEventListener("click", function () {
  window.location.href = VIEWS.OPPONENTS_MAIN;
});

// Inizializzo al caricamento della pagina la mail dell'amministratore loggato
document.addEventListener("DOMContentLoaded", () => {
  const jwtData = parseJwt(getCookie("jwt"));
  const email = jwtData.sub;

  // Mostra la mail nel DOM
  console.log(document.getElementById("email-display"));
  document.getElementById("email-display").textContent = email;
});

document.addEventListener("DOMContentLoaded", function () {
  const sidebar = document.querySelector(".sidebar");
  const sidebarToggle = document.querySelector(".sidebar-toggle");

  // Funzione per nascondere/mostrare la sidebar in base alla larghezza dello schermo
  function handleResize() {
    if (window.innerWidth <= 768) {
      sidebar.style.display = "none"; // Nascondi la sidebar su schermi piccoli
      sidebarToggle.style.display = "block"; // Mostra la freccia
    } else {
      sidebar.style.display = "block"; // Mostra la sidebar su schermi grandi
      sidebarToggle.style.display = "none"; // Nascondi la freccia
    }
  }

  // Funzione per aprire/chiudere la sidebar e ruotare la freccia
  sidebarToggle.addEventListener("click", function () {
    sidebar.classList.toggle("open"); // Alterna la classe 'open' sulla sidebar
    if (sidebar.classList.contains("open")) {
      sidebar.style.display = "block"; // Mostra la barra laterale
    } else {
      sidebar.style.display = "none"; // Nascondi la barra laterale
    }
  });

  // Ascolta gli eventi di resize per aggiornare la visibilità della barra laterale
  window.addEventListener("resize", handleResize);

  // Esegui una volta all'inizio per applicare la visibilità corretta
  handleResize();
});






// Funzione per invitare un nuovo amministratore, attualmente l'invito non è supportato e non più in uso.
// Lasciata per legacy in caso si decida di supportarlo nuovamente
function Invita() {
  fetch("/invite_admins", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      console.log("Response:", response);
      if (response.status == 200) {
        response.text().then((okMessage) => {
          alert(
            "Verrai reindirizzato alla pagina di invito di nuovi amministratori."
          );
        });

        window.location.href = "/invite_admins";
      } else {
        response.text().then((errorMessage) => {
          alert(errorMessage);
        });
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      //Aggiungi qui il codice per gestire gli errori
    });
}


