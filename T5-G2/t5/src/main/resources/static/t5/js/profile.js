
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

//TODO: questo è solo per DEMO, i dati del profilo devono essere contenuti in un model



document.addEventListener('DOMContentLoaded', (e) => {
  // Mostra solo gli achievement completati all'avvio
  viewCompletedAchievements();
  const toggleButton = document.getElementById('toggle-button');
    toggleButton.addEventListener('click', toggleAchievements);
});


   function viewCompletedAchievements() {
  const achievements = document.querySelectorAll('.achievement-container');
  achievements.forEach(achievement => {
      const isCompleted = achievement.getAttribute('data-completed') === 'true';
      achievement.style.display = isCompleted ? '' : 'none';
  });
}

// Funzione per mostrare tutti gli achievement
function viewAllAchievements() {
  const achievements = document.querySelectorAll('.achievement-container');
  achievements.forEach(achievement => {
      achievement.style.display = '';
  });
}
function toggleAchievements() {
  const button = document.getElementById('toggle-button');
  const showAll = button.textContent === 'Visualizza tutti gli achievements';

  if (showAll) {
      viewAllAchievements();
      button.textContent = 'Visualizza solo achievements completati';
  } else {
      viewCompletedAchievements();
      button.textContent = 'Visualizza tutti gli achievements';
  }
}

