// Funzione per ottenere un valore da un cookie dato il suo nome
const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
};

// Funzione per decodificare un token JWT
const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

// Funzione per mostrare una specifica sezione
// Modifica (28/11) by GabMan 'Aggiunta della funzione showSection'
function showSection(sectionId) {
  // Rimuovi la classe "active" da tutte le sezioni
  document.querySelectorAll('.section').forEach(section => section.classList.remove('active'));
  // Aggiungi la classe "active" solo alla sezione selezionata
  document.getElementById(sectionId).classList.add('active');
}

// Event Listener per eseguire il caricamento dei dati utente al caricamento della pagina
// Modifica (28/11) by GabMan 'Recupero dati profilo'
document.addEventListener("DOMContentLoaded", (e) => {
  var userId = document.getElementById("user_id").innerHTML; // Recupera l'ID utente dal DOM
  console.log(userId);

  var userAPIString = '/students_list/{0}'.replace('{0}', userId);

  // Esecuzione della richiesta AJAX per ottenere i dati utente
  $.ajax({
    url: userAPIString,
    type: 'GET',
    timeout: 30000,
    success: function (data, textStatus, xhr) {
      // Aggiorna i campi del profilo con i dati ricevuti
      document.getElementById("usernameField").innerText = data.email + " - " + data.name + " " + data.surname;
      document.getElementById("studiesField").innerText = data.studies;
    },
    error: function (xhr, textStatus, errorThrown) {
      console.error("Errore durante il recupero dei dati utente:", errorThrown);
      swal("Errore", "Errore durante il recupero dei dati utente", "error");
    }
  });

  // Modifica (28/11) by GabMan 'Aggiunta funzione per salvare la biografia'
  const saveButton = document.getElementById("saveBioButton");

  if (saveButton) {
    saveButton.addEventListener("click", async () => {
      // Recupera il valore della textarea della biografia
      const bio = document.getElementById("biography").value;

      try {
        // Esegue una richiesta POST per aggiornare la biografia
        const response = await fetch("/updateBiography", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body:new URLSearchParams({ biography: bio }),
        });

        if (response.ok) {
          // Notifica di successo
          swal("Successo", "Biografia salvata con successo!", "success");
        } else {
          // Notifica di errore
          const error = await response.text();
          swal("Errore", "Errore nel salvataggio: " + error, "error");
        }
      } catch (error) {
        // Notifica di errore di connessione
        swal("Errore", "Errore nella connessione al server.", "error");
      }
    });
  }
});
