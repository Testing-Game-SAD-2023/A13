function Registrazione_Invito() {
  
    const invitationToken = document.getElementById('invitationToken').value;
    const nome = document.getElementById('nome').value;
    const cognome = document.getElementById('cognome').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
   
    const data = {
  
      invitationToken: invitationToken,
      nome: nome,
      cognome: cognome,
      username: username,
      password: password,
      email: email
    };
  
    //MODIFICA (11/02/2024) : Gestione feedback form registrazione
     fetch('/login_with_invitation', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      console.log('Response:', response);
      if(response.status == 200) {
        window.location.href = "/loginAdmin";
      }
      else {
        response.text().then(errorMessage => {
          alert(errorMessage);                  // pop-up che mostra il messaggio di errore restituito dal controller
        });
      }
    })
    .catch((error) => {
     window.location.href = "/registraAdmin";
     console.error('Error:', error);
    //Aggiungi qui il codice per gestire gli errori
    });
  }