
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
  
  document.addEventListener('DOMContentLoaded', () => {
    // Mostra solo gli achievement completati all'avvio
    
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
  
  let currentRobot = 'None'; // Filtro iniziale per Robot
  let currentGameMode = 'All'; // Filtro iniziale per GameMode
  function filterStatistics(robotType, gameMode) {
    // Aggiorna i filtri correnti
    if (robotType !== undefined) currentRobot = robotType;
    if (gameMode !== undefined) currentGameMode = gameMode;
  
    // Recupera tutte le statistiche
    const statisticsRows = document.querySelectorAll('.statistic-row');
  
    // Filtra in base al tipo di robot e modalità di gioco
    statisticsRows.forEach(row => {
        const robot = row.getAttribute('data-robot');
        const gamemode = row.getAttribute('data-gamemode');
  
        const robotMatch = (currentRobot === 'None' || robot === currentRobot);
        const gameModeMatch = (currentGameMode === 'All' || gamemode === currentGameMode);
  
        // Mostra o nascondi la riga in base ai filtri
        if (robotMatch && gameModeMatch) {
            row.style.display = ''; // Mostra la riga
        } else {
            row.style.display = 'none'; // Nascondi la riga
        }
    });
  };
  
  function clearFilter() {
    currentRobot = 'None';
    currentGameMode = 'All';
    filterStatistics(currentRobot, currentGameMode);
  }
  

  function unfollowPlayer() {
    const jwt = getCookie('jwt');

    if (!jwt) {
        alert("Utente non autenticato. Effettua il login.");
        return;
    }

    $.ajax({
        url: `/profile/rmFollow?userID_1=${user.id}&userID_2=${playerId}`,
        type: 'POST',
        xhrFields: {
            withCredentials: true // Include i cookie nella richiesta
        },
        success: function() {
            alert('Unfollow eseguito con successo.');

            // Aggiorna manualmente il modello utente
            user.following = user.following.filter(f => f.id !== playerId); // Rimuovi dalla lista

            // Reindirizza alla route profile
            window.location.href = '/profile';
        },
        error: function(xhr) {
            const errorMessage = xhr.responseText || "Errore sconosciuto.";
            alert("Errore durante l'operazione: " + errorMessage);
        }
    });
};
