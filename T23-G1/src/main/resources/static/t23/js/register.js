const form = document.querySelector("form");

form.addEventListener("submit", (event) => {
  event.preventDefault();

  const nome = document.getElementById("name").value.trim();
  const cognome = document.getElementById("surname").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();
  const confermaPassword = document.getElementById("check_password").value.trim();
  const corsoDiStudi = document.getElementById("studies").value;

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

  const datiUtente = {
    nome: nome,
    cognome: cognome,
    email: email,
    password: password,
    corsoDiStudi: corsoDiStudi
  };

  console.log(datiUtente);
  //form.reset();
  form.submit();
});