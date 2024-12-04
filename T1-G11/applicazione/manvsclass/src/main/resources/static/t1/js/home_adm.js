toggle = document.querySelectorAll(".toggle")[0];
nav = document.querySelectorAll("nav")[0];
toggle_open_text = 'Menu';
toggle_close_text = 'Close';

toggle.addEventListener('click', function() {
	nav.classList.toggle('open');
	
  if (nav.classList.contains('open')) {
    toggle.innerHTML = toggle_close_text;
  } else {
    toggle.innerHTML = toggle_open_text;
  }
}, false);

setTimeout(function(){
	nav.classList.toggle('open');	
}, 800);

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

//Modifica 29/11/2024: Creazione chiamate Teams per reindirizzare alla view
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

//Modifica 29/11/2024: creazione funzione Assignments per reinderizzare alla view.
function Assignments() {
  fetch('/assignments', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      
      response.text().then(okMessage => {
        alert("Verrai reindirizzato alla pagina degli assignments.");
      })

      window.location.href = "/assignments";  // Reindirizza alla pagina degli assignments
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

function getAdmin(){
    // Esegui una richiesta fetch all'endpoint /usernameAdmin
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
    // Puoi mostrare il dato nel DOM, ad esempio:
    document.getElementById("username-display").textContent = `Username: ${data}`;
  })
  .catch((error) => {
    console.error("Errore nella fetch:", error);
  });



}
// Inizializzazione al caricamento della pagina
document.addEventListener("DOMContentLoaded", () => getAdmin());

