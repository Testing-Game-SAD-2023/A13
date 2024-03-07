const passwordForm = document.querySelector("#password-form");
const emailSentMsg = document.querySelector("#email-sent-msg");

passwordForm.addEventListener("submit", (event) => {
  event.preventDefault();
  const email = passwordForm.email.value;
  console.log(`Invio email a ${email}...`);
  // Qui si potrebbe aggiungere del codice per inviare l'email con il link per reimpostare la password
  passwordForm.reset();
  emailSentMsg.classList.remove('hidden');
  passwordForm.submit();
});
