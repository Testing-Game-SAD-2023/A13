const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

document.addEventListener("DOMContentLoaded", function () {
  // Aggiungi un listener per i tab
  const trophyTabs = document.querySelectorAll('#trophyTabs button[data-bs-toggle="tab"]');
  trophyTabs.forEach(tab => {
      tab.addEventListener('shown.bs.tab', function (event) {
          console.log(`Tab attivo: ${event.target.id}`); // Log del tab attivo
      });
  });
});