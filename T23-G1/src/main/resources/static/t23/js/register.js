//Modifica (18/06/2024)
//Bugfix della funzione della registrazione, non coerente con la POST nel controller
//Aggiunto anche il popup di errore nel caso di una compilazione sbagliata


document.addEventListener('DOMContentLoaded', (event) => {
  const form = document.querySelector("form");
  if (form) {
    console.log("Form trovato.");
    console.log(form);
  }
  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    //Costruzione del form per l'invio dei dati tramite la POST request.
    const nome = document.getElementById("name").value.trim();
    const cognome = document.getElementById("surname").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const confermaPassword = document.getElementById("check_password").value.trim();
    const corsoDiStudi = document.getElementById("studies").value;

    const formData = new FormData();
    formData.append("name", nome);
    formData.append("surname", cognome);
    formData.append("email", email);
    formData.append("password", password);
    formData.append("check_password", confermaPassword);
    formData.append("studies", corsoDiStudi);

    try {

      if (nome === '') {
        alert("Compila il campo Nome!");
        return;
      }
    
      if (cognome === '') {
        alert("Compila il campo Cognome!");
        return;
      }
    
      if (email === '') {
        alert("Compila il campo Email!");
        return;
      }
    
      if (password === '') {
        alert("Compila il campo Password!");
        return;
      }
    
      if (confermaPassword === '') {
        alert("Compila il campo Conferma Password!");
        return;
      }
    
      if (password !== confermaPassword) {
        alert("Le password non corrispondono!");
        return;
      }
      
      console.log(toString(formData));

      // fatch della richiesta
      const response = await fetch('/register', {
        method: 'POST',
        body: formData,
      });

      // Controllo dello status code di reindirizzamento (301)
      if (!response.redirected) {
        // Se non non è 301 lancia un errore
        const errorBody = await response.text();
        throw new Error(`Errore: ${errorBody}`);
      }

      // Se la response è di reindirizzo cerca l'url nell'header della response
      console.log('Received data:', response.body);
      window.location.href = response.url;
    } catch (error) {
      // Pop-up del messaggio di errore da parte della pagina, gestisce anche gli errori del controller
      console.error('Error:', error.message);
      alert(error.message);
    }
  });
});

//FINE MODIFICA
