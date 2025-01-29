// ---REFACTORING TOTALE 5/12/2024---
//Function per la disconnessione 
function Disconnessione() {

  fetch('/logout_admin', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      
      response.text().then(okMessage => {
        alert("Logout avvenuto con successo, a breve verrai indirizzato nella pagina di login");
        localStorage.removeItem('usernameAdmin');

      })

      window.location.href = "/loginAdmin";
    }
    else {
      response.text().then(errorMessage => {
        alert(errorMessage);
      })
    }
  })
  .catch((error) => {
    console.error('Error:', error);
    //Aggiungi qui il codice per gestire gli errori
  });
}

//Function invita 
function Invita() {

  fetch('/invite_admins', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      
      response.text().then(okMessage => {
        alert("Verrai reindirizzato alla pagina di invito di nuovi amministratori.");
      })

      window.location.href = "/invite_admins";
    }
    else {
      response.text().then(errorMessage => {
        alert(errorMessage);
      })
    }
  })
  .catch((error) => {
    console.error('Error:', error);
    //Aggiungi qui il codice per gestire gli errori
  });
}

//MODIFICA (17/04/2024) : Redirect alla pagina per configurare la nuova modalità di gioco
document.querySelector('.button-scalata').addEventListener('click', Scalata);
function Scalata() {

  fetch('/scalata', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      
      response.text().then(okMessage => {
        alert("Verrai reindirizzato alla pagina di configurazione della nuova modalità di gioco");
      })

      window.location.href = "/scalata";
    }
    else {
      response.text().then(errorMessage => {
        alert(errorMessage);
      })
    }
  })
  .catch((error) => {
    console.error('Error:', error);
    //Aggiungi qui il codice per gestire gli errori
  });
}

//Modifica 05/12/2024: Creazione chiamate Teams per reindirizzare alla view
document.querySelector('.button-team').addEventListener('click', Teams);

function Teams() {
  fetch('/teams', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      response.text().then(okMessage => {
        alert("Verrai reindirizzato alla pagina dei Teams.");
      })
      
      window.location.href = "/teams";  // Reindirizza alla pagina dei team
    }
    else {
      response.text().then(errorMessage => {
        alert(errorMessage);
      })
    }
  })
  .catch((error) => {
    console.error('Error:', error);
    //Aggiungi qui il codice per gestire gli errori
  });
}

//Funzione get admin per la visualizzazione del nome utente 
function getAdmin() {
  // Controlla se il nome dell'utente è già memorizzato nel localStorage
  const username = localStorage.getItem('usernameAdmin');
  
  if (username) {
      // Se il nome utente è trovato nel localStorage, lo mostri direttamente
      document.getElementById("username-display").textContent = `${username}`;
  } else {
      // Altrimenti, fai una richiesta per ottenere il nome
      fetch("/usernameAdmin", {
          method: 'GET',
          headers: {
              'Content-Type': 'text/plain'
          },
      })
      .then((response) => {
          if (!response.ok) {
              throw new Error(`Errore HTTP! Status: ${response.status}`);
          }
          return response.text(); // Aspettati una risposta di tipo testo
      })
      .then((data) => {
          // Memorizza il nome utente nel localStorage
          localStorage.setItem('usernameAdmin', data);

          // Mostra il nome utente nel DOM
          document.getElementById("username-display").textContent = `${data}`;
      })
      .catch((error) => {
          console.error("Errore nella fetch:", error);
      });
  }
}

// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => getAdmin());


// Modifica per il bottone Achievement
document.querySelector('.button-achievement').addEventListener('click', function() {
  window.location.href = "/achievements";  // Reindirizza alla pagina degli achievement
});

// Modifica per il bottone Classes
document.querySelector('.button-class').addEventListener('click', function() {
  window.location.href = "/class";  // Reindirizza alla pagina delle classi
});

document.addEventListener("DOMContentLoaded", function () {
  const sidebar = document.querySelector('.sidebar');
  const sidebarToggle = document.querySelector('.sidebar-toggle');

  // Funzione per nascondere/mostrare la sidebar in base alla larghezza dello schermo
  function handleResize() {
      if (window.innerWidth <= 768) {
          sidebar.style.display = 'none';  // Nascondi la sidebar su schermi piccoli
          sidebarToggle.style.display = 'block';  // Mostra la freccia
      } else {
          sidebar.style.display = 'block';  // Mostra la sidebar su schermi grandi
          sidebarToggle.style.display = 'none';  // Nascondi la freccia
      }
  }

  // Funzione per aprire/chiudere la sidebar e ruotare la freccia
  sidebarToggle.addEventListener('click', function() {
      sidebar.classList.toggle('open');  // Alterna la classe 'open' sulla sidebar
      if (sidebar.classList.contains('open')) {
          sidebar.style.display = 'block';  // Mostra la barra laterale
      } else {
          sidebar.style.display = 'none';  // Nascondi la barra laterale
      }
  });

  // Ascolta gli eventi di resize per aggiornare la visibilità della barra laterale
  window.addEventListener('resize', handleResize);

  // Esegui una volta all'inizio per applicare la visibilità corretta
  handleResize();
});


// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => getAdmin());

