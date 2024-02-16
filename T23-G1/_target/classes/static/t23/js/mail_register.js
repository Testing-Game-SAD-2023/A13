const form = document.querySelector("form");

form.addEventListener("submit", (event) => {
    event.preventDefault();
    
    const username = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    /*const appleButton = document.querySelector('.apple-login-button');
    const googleButton = document.querySelector('.google-login-button');
    */

    if (username === '') {
        alert('Compila il campo Username!');
        return;
      }
  
      if (password === '') {
        alert('Compila il campo Password!');
        return;
      }

  //form.reset();
  form.submit();

    });