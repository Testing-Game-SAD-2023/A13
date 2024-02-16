const confermaLink = document.getElementById('conferma-link');
const confermatoMsg = document.getElementById('confermato-msg');

confermaLink.addEventListener('click', function(event) {
  event.preventDefault();
  confermatoMsg.classList.remove('hidden');
});