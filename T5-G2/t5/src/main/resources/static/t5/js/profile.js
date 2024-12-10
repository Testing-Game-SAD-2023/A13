
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

const populateForm = () => {
  document.getElementById('nameInput').value = userUpdate.name || '';
  document.getElementById('surnameInput').value = userUpdate.surname || '';
  document.getElementById('emailInput').value = userUpdate.email || '';
  document.getElementById('studiesInput').value = userUpdate.studies || '';
  document.getElementById('biographyInput').value = userUpdate.biography || '';
};


const updateUserObject = () => {
  const fields = {
      name: 'nameInput',
      surname: 'surnameInput',
      email: 'emailInput',
      studies: 'studiesInput',
      password: 'passwordInput',
      biography: 'biographyInput'
  };

  Object.keys(fields).forEach(key => {
      const inputValue = document.getElementById(fields[key]).value;
      userUpdate[key] = inputValue === '' ? userUpdate[key] : inputValue;
  });
};


document.addEventListener('DOMContentLoaded', () => {
  // Mostra solo gli achievement completati all'avvio
  populateForm();
  viewCompletedAchievements();
});

function viewCompletedAchievements() {
  const achievements = document.querySelectorAll('.achievement-container');
  achievements.forEach(achievement => {
      const isCompleted = achievement.getAttribute('data-completed') === 'true';
      achievement.style.display = isCompleted ? '' : 'none';
  });
};

// Funzione per mostrare tutti gli achievement
function viewAllAchievements() {
  const achievements = document.querySelectorAll('.achievement-container');
  achievements.forEach(achievement => {
      achievement.style.display = '';
  });
};

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
};

const saveChanges = () => {
  updateUserObject(); // Aggiorna l'oggetto con i dati del modulo

  const oldPassword = $('#passwordoldInput').val();

  if(!oldPassword){
    alert("Non è stata inserita l'attuale password per l'autenticazione.");
    return;
  }

  const jwt = getCookie('jwt');

  if (!jwt) {
      alert("Utente non autenticato. Effettua il login.");
      return;
  }


  $.ajax({
    url: '/profile/modifyUser?old_psw=' + encodeURIComponent(oldPassword), // Parametro di query
    type: 'PUT',
    contentType: 'application/json',
    data: JSON.stringify(userUpdate), // Corpo JSON
    xhrFields: {
        withCredentials: true // Include i cookie nella richiesta
    },
    success: function(response) {
        alert("Modifiche salvate con successo!");
        location.reload();
    },
    error: function(xhr) {
        const errorMessage = xhr.responseText || "Errore sconosciuto.";
        alert("Errore durante il salvataggio: " + errorMessage);
    }
});
};


const search = () => {
  const jwt = getCookie('jwt');

  if (!jwt) {
      alert("Utente non autenticato. Effettua il login.");
      return;
  }

  const keySearch = $('#key_search').val();

  if (!keySearch) {
      alert("Inserisci una chiave di ricerca.");
      return;
  }

  if(keySearch === user.id || keySearch === user.email){
    alert("Non puoi cercare te stesso!");
    return;
  }

  $.ajax({
      url: '/profile/searchPlayer?key_search=' + encodeURIComponent(keySearch),
      type: 'GET',
      xhrFields: {
          withCredentials: true 
      },
      success: function(response) {
        if (response) {
          renderSearchResults(response); // Passa direttamente il singolo giocatore
      } else {
          renderSearchResults(null); // Nessun giocatore trovato
      }
  },
      error: function(xhr) {
          const errorMessage = xhr.responseText || "Errore sconosciuto.";
          alert("Errore durante la ricerca: " + errorMessage);
      }
  });
};

const renderSearchResults = (player) => {
  const resultsContainer = document.getElementById('searchResults');
  resultsContainer.innerHTML = ''; // Svuota i risultati precedenti

  if (!player) {
      resultsContainer.innerHTML = '<p>Nessun giocatore trovato.</p>';
      return;
  }

  // Controlla se l'utente corrente segue il giocatore
  const isFollowing = isUserFollowing(player);
  const followButtonText = isFollowing ? 'Unfollow' : 'Follow';
  const buttonClass = isFollowing ? 'btn-danger' : 'btn-primary';

  const playerDiv = document.createElement('div');
  playerDiv.classList.add('d-flex', 'align-items-center', 'mb-2');

  playerDiv.innerHTML = `
      <div class="flex-grow-1">
          <strong>${player.name} ${player.surname}</strong>
      </div>
      <button 
          class="btn ${buttonClass} btn-sm"
          onclick="toggleFollow(${player.id}, ${isFollowing})"
      >
          ${followButtonText}
      </button>
  `;

  resultsContainer.appendChild(playerDiv);
};

// Funzione per determinare se l'utente sta già seguendo un altro utente
const isUserFollowing = (player) => {
  return user.following.some(followedPlayer => followedPlayer.id === player.id);
};

const toggleFollow = (playerId, isFollowing) => {
  const jwt = getCookie('jwt');

  if (!jwt) {
      alert("Utente non autenticato. Effettua il login.");
      return;
  }

  const action = isFollowing ? 'rmFollow' : 'addFollow';
  $.ajax({
      url: `/profile/${action}?userID_1=${user.id}&userID_2=${playerId}`,
      type: 'POST',
      xhrFields: {
          withCredentials: true // Include i cookie nella richiesta
      },
      success: function() {
          alert(`${isFollowing ? 'Unfollow' : 'Follow'} eseguito con successo.`);

          // Aggiorna manualmente il modello utente
          if (isFollowing) {
              user.following = user.following.filter(f => f.id !== playerId); // Rimuovi dalla lista
          } else {
              user.following.push({ id: playerId }); // Aggiungi alla lista
          }

          search();
      },
      error: function(xhr) {
          const errorMessage = xhr.responseText || "Errore sconosciuto.";
          alert("Errore durante l'operazione: " + errorMessage);
      }
  });
};

$('#searchPlayerModal').on('hidden.bs.modal', function () {
  location.reload(); // Ricarica l'intera pagina
});

let currentRobot = 'None'; // Filtro iniziale per Robot
let currentGameMode = 'All'; // Filtro iniziale per GameMode

// Aggiorna il filtro per Robot e il testo del bottone
function selectRobotFilter(robotType) {
    currentRobot = robotType;
    document.getElementById('robotFilterDropdown').innerText = robotType; // Aggiorna il testo del bottone
    filterStatistics(); // Applica il filtro
}

// Aggiorna il filtro per GameMode e il testo del bottone
function selectGameModeFilter(gameMode) {
    currentGameMode = gameMode;
    document.getElementById('gameModeFilterDropdown').innerText = gameMode; // Aggiorna il testo del bottone
    filterStatistics(); // Applica il filtro
}

// Applica i filtri alle statistiche
function filterStatistics() {
    const statisticsRows = document.querySelectorAll('.statistic-row');

    statisticsRows.forEach(row => {
        const robot = row.getAttribute('data-robot');
        const gamemode = row.getAttribute('data-gamemode');

        const robotMatch = 
            (currentRobot === 'None' && robot === 'None') || 
            (currentRobot !== 'None' && robot === currentRobot);

        const gameModeMatch = 
            (currentGameMode === 'All') || 
            (gamemode === currentGameMode);

        if (robotMatch && gameModeMatch) {
            row.style.display = ''; // Mostra la riga
        } else {
            row.style.display = 'none'; // Nascondi la riga
        }
    });
}

// Resetta i filtri e il testo dei bottoni
function clearFilter() {
    currentRobot = 'None';
    currentGameMode = 'All';

    document.getElementById('robotFilterDropdown').innerText = 'Robot'; // Ripristina il testo del bottone
    document.getElementById('gameModeFilterDropdown').innerText = 'GameMode'; // Ripristina il testo del bottone

    const statisticsRows = document.querySelectorAll('.statistic-row');
    statisticsRows.forEach(row => {
        row.style.display = ''; // Mostra tutte le righe
    });
}
