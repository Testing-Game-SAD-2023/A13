/*const passwordForm = document.querySelector("#password-form");
const emailSentMsg = document.querySelector("#email-sent-msg");

passwordForm.addEventListener("submit", (event) => {
  event.preventDefault();
  const email = passwordForm.email.value;
  console.log(`Invio email a ${email}...`);
  // Qui si potrebbe aggiungere del codice per inviare l'email con il link per reimpostare la password
  passwordForm.reset();
  emailSentMsg.classList.remove('hidden');
  passwordForm.submit();
});*/

document.addEventListener("DOMContentLoaded", () => {
  function submitForm(event) {
      event.preventDefault(); // Prevent the default form submission
      console.log('form')
      const form = event.target;
      const formData = new FormData(form);

      fetch(form.action, {
          method: form.method,
          body: formData
      })
      .then(response => response.text())
      .then(message => {
          // Reload the page with a query parameter indicating the result
          window.location.href = window.location.pathname + "?message=" + encodeURIComponent(message);
      })
      .catch(error => console.error('Error:', error));
  }

  window.onload = function() {
      const form = document.getElementById('password-form');
      form.addEventListener('submit', submitForm);

      // Display the message if present in the query parameters
      const urlParams = new URLSearchParams(window.location.search);
      const message = urlParams.get('message');
      if (message) {
          alert(decodeURIComponent(message));
      }
  };

});
