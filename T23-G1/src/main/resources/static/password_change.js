const form = document.querySelector("form");

form.addEventListener("submit", (event) => {
  event.preventDefault();

  const password = document.getElementById("password").value.trim();
  const confermaPassword = document.getElementById("check_password").value.trim();


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

  const datiUtente = {
   //nome: nome,
    //cognome: cognome,
   // email: email,
    password: password,
  };

 // console.log(datiUtente);
  //form.reset();
  form.submit();
});