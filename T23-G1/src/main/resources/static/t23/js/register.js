//Modifica (18/06/2024)
//Bugfix della funzione della registrazione, non coerente con la POST nel controller
//Aggiunto anche il popup di errore nel caso di una compilazione sbagliata

//MODIFICA (05/11/2024) Aggiunto controller reCAPTCHA
let recaptchaToken = null; 

function setRecaptchaToken(token) {
  recaptchaToken = token; // Assegna il token reCAPTCHA alla variabile globale
}

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
    formData.append("g-recaptcha-response", recaptchaToken); // Aggiungi il token di reCAPTCHA

    // Validazione lato client
    if (nome === '') {
      alert("Compila il campo Nome!");
      return;
    }

    if (cognome === '') {
      alert("Compila il campo Cognome!");
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
      alert("Inserisci un'email valida!");
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
      
    for (let pair of formData.entries()) {
      console.log(`${pair[0]}: ${pair[1]}`);
    }

    //MODIFICA (05/11/2024) Aggiunto controllo reCAPTCHA
    if (!recaptchaToken) {
      //controllo reCAPTCHA
      alert("Per favore, completa il reCAPTCHA.");
      return;
    }

    try {
      // Invia la richiesta al server
      const response = await fetch('/register', {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        const errorBody = await response.text();
        console.error('Errore dalla risposta:', errorBody);
        throw new Error(`Errore dalla richiesta: ${response.status} ${response.statusText}`);
      }

      if (!response.redirected) {
        const errorBody = await response.text();
        throw new Error(`Errore: ${errorBody}`);
      }

      // Reindirizza alla pagina di successo
      window.location.href = response.url;
      
    } catch (error) {
      // Pop-up del messaggio di errore da parte della pagina, gestisce anche gli errori del controller
      console.error('Error:', error.message);
      alert(error.message);
    }
  });
});

//FINE MODIFICA
