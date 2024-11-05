// const form = document.querySelector("form");

// form.addEventListener("submit", (event) => {
//   event.preventDefault();

//   const nome = document.getElementById("name").value.trim();
//   const cognome = document.getElementById("surname").value.trim();
//   const email = document.getElementById("email").value.trim();
//   const password = document.getElementById("password").value.trim();
//   const confermaPassword = document.getElementById("check_password").value.trim();
//   const corsoDiStudi = document.getElementById("studies").value;

//   if (nome === '') {

//     //alert("Compila il campo Nome!");
//     //return;

//     //MODIFICA (03/02/2024)
//     nome.style.border = "1px solid red";
//     name_error.style.display = "block";
//     nome.focus();
//     return false;
//   }

//   if (cognome === '') {
//     alert("Compila il campo Cognome!");
//     return;
//   }

//   if (email === '') {
//     alert("Compila il campo Email!");
//     return;
//   }

//   if (password === '') {
//     alert("Compila il campo Password!");
//     return;
//   }

//   if (confermaPassword === '') {
//     alert("Compila il campo Conferma Password!");
//     return;
//   }

//   if (password !== confermaPassword) {
//     alert("Le password non corrispondono!");
//     return;
//   }

//   const datiUtente = {
//     nome: nome,
//     cognome: cognome,
//     email: email,
//     password: password,
//     corsoDiStudi: corsoDiStudi
//   };

//   console.log(datiUtente);
//   //form.reset();
//   form.submit();
// });

//MODIFICA (03/02/2024)

const form = document.querySelector("form");

form.addEventListener("submit", async (event) => {
  event.preventDefault();

  //Form data extraction logic...
  const nome = document.getElementById("name").value.trim();
  const cognome = document.getElementById("surname").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();
  const confermaPassword = document.getElementById("check_password").value.trim();
  const corsoDiStudi = document.getElementById("studies").value;

  const datiUtente = {
    name: nome,
    surname: cognome,
    email: email,
    password: password,
    check_password: confermaPassword,
    studies: corsoDiStudi,
  };

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
    
    // Use fetch to send form data to the server
    const response = await fetch('/your-register-endpoint', {
      method: 'POST', // or 'PUT' or 'PATCH' depending on your server logic
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datiUtente),
    });

    // Check if the response status is OK (2xx)
    if (!response.ok) {
      // If not OK, handle the error
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    // If the response is OK, you can handle the success
    const responseData = await response.json();
    console.log('Received data:', responseData);
  } catch (error) {
    // Handle the error
    console.error('Error:', error.message);
  }
});

//FINE MODIFICA
