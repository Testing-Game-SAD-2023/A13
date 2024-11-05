document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const password = document.getElementById("newPassword").value.trim();
    const confermaPassword = document.getElementById("confirmPassword").value.trim();
    const email = document.getElementById("email").value.trim();
    const token = document.getElementById("token").value.trim();

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

    const formData = new URLSearchParams();
    formData.append("email", email);
    formData.append("token", token);
    formData.append("newPassword", password);
    formData.append("confirmPassword", confermaPassword);

    fetch(form.action, {
      method: form.method,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: formData.toString()
    })
    .then(response => response.text())
    .then(message => {
      window.location.href = window.location.pathname + "?message=" + encodeURIComponent(message);
    })
    .catch(error => console.error('Error:', error));
  });

  // Display the message if present in the query parameters
  const urlParams = new URLSearchParams(window.location.search);
  const message = urlParams.get('message');
  if (message) {
    alert(decodeURIComponent(message));
  }
});