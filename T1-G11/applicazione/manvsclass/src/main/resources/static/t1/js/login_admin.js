//MODIFICA (11/02/2024) : Controllo form login */
function Logi() {

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
  
    const data = {
      username: username,
      password: password
    };
  
    fetch('/loginAdmin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      console.log('Response:', response);
      if(response.status == 200) {
        
        response.text().then(okMessage => {
          alert(okMessage);
        })
  
        window.location.href = "/home_adm";
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