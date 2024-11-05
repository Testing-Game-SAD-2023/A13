//variabili per la selezione della classe e del robot
var classe = null;
var robot = null;
var difficulty = null;
//variabili per il login
var user = null;
var password = null;
var classe = null;

// Variabile per tenere traccia del bottone precedentemente selezionato
var bottonePrecedente1 = null;
// Variabile per tenere traccia del bottone precedentemente selezionato
var bottonePrecedente2 = null;

var selectedElement = null;

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

document.addEventListener("DOMContentLoaded", (e) => {
  document.getElementById("usernameField").innerText = parseJwt(getCookie("jwt")).sub;
});



function selectGameMode(mode) {
  console.log("Modalità selezionata:", mode);
  localStorage.setItem("modalita", mode);

  // Verifica la modalità selezionata

  if (mode === "Sfida" || mode === "Allenamento") {
    
    // Chiedi conferma all'utente
    let userConfirmation = confirm("Hai selezionato la modalità " + mode + ". Vuoi continuare?");
    if (userConfirmation) {

      //Redirect
      window.location.href = "/gamemode";
    }
    else{
      //Do nothing
    }
  } else if (mode === "Multiplayer") {

    // Avvisa l'utente che questa modalità non è ancora disponibile
    swal("Attenzione","La modalità " + mode + " non è ancora disponibile. Arriverà presto!","info");
  }
  else if (mode === "LeaderboardScalata") {

    // Chiedi conferma all'utente
    let userConfirmation = confirm("Sei sicuro di voler visualizzare la classifica delle Scalate?");
    if (userConfirmation) {

      //Redirect
      window.location.href = "/leaderboardScalata";
    }
    else{
      //Do nothing
    }

  }
  //MODIFICA (20/05): Redirect alla pagina di selezione delle "Scalate"
  else if (mode === "Scalata") {

    // Chiedi conferma all'utente
    confirm("Hai selezionato la modalità " + mode + ". Vuoi continuare?");
    if (confirm) {

      /* Controllare, prima di re-indirizzare l'utente alla pagina /gamemode_scalata,
      se siano presenti delle "Scalate" disponibili per la selezione, altrimenti 
      mostrare a video un messaggio d'errore.
      Per riuscire nell'intento eseguire una GET all'endpoint /scalate_list del task T1.
      */
      $.ajax({
        url: '/scalate_list',
        type: 'GET',
        timeout: 30000,
        success: function (data, textStatus, xhr) {

          if (data.length > 0) {

            console.log("Nel sistema sono presenti" + data.lenght + " 'Scalate' disponibili.");
            window.location.href = "/gamemode_scalata";

          } else {

            console.log("Nessuna Scalata disponibile, mostrare messaggio d'errore");
            swal("Attenzione!","Nessuna 'Scalata' disponibile al momento. Ti preghiamo di selezionare un'altra modalità di gioco.","error");
          }
        },
        error: function (xhr, textStatus, errorThrown) {
          console.error("Errore durante il recupero delle Scalate:", errorThrown);
          swal("Errore","Errore durante il recupero delle Scalate","error");
        }
      });
    }
    else {
      //Do nothing
    }
  }
}

function Handlebuttonclass(id, button) {
  $(document).ready(function () {
    classe = id;
    console.log('Hai cliccato sul bottone delle classi con id: ' + classe);
    document.querySelectorAll("span.levels:not(.hidden)").forEach((el) => el.classList.add("hidden"));
    if (document.getElementById("levels-" + button.id).classList.contains("hidden")) document.getElementById("levels-" + button.id).classList.remove("hidden");
    // Se il bottone precedentemente selezionato è diverso da null
    // allora rimuoviamo la classe highlighted
    if (bottonePrecedente1 != null) {
      bottonePrecedente1.classList.remove("highlighted");
    }
    if (button.classList.contains("highlighted")) {
      button.classList.remove("highlighted");
    } else {
      button.classList.add("highlighted");
    }

    bottonePrecedente1 = button;
  });
}

function Handlebuttonrobot(id, button, rob, size) { //modificato
  $(document).ready(function () {
    robot = rob;
    if (robot == "evosuite") { // aggiunto
      difficulty = parseInt(id) - parseInt(size) / 2; // devo prendere l'id attuale meno la metà della grandezza totale del vettore di robot
      difficulty = difficulty.toString();
    }
    else { // aggiunto
      difficulty = id;
    }
    //difficulty = id;
    console.log('Hai cliccato sul bottone del robot con id: ' + robot);

    // Se il bottone precedentemente selezionato è diverso da null
    // allora rimuoviamo la classe highlighted
    if (bottonePrecedente2 != null) {
      bottonePrecedente2.classList.remove("highlighted");
    }

    if (button.classList.contains("highlighted")) {
      button.classList.remove("highlighted");
    } else {
      button.classList.add("highlighted");
    }
    bottonePrecedente2 = button;

  });
}

function redirectToPagereport() {
  console.log(classe);
  console.log(robot);
  console.log(difficulty);
  if (classe && robot && difficulty) {

    localStorage.setItem("classe", classe);
    localStorage.setItem("robot", robot);
    localStorage.setItem("difficulty", difficulty);
    window.location.href = "/report";
  }
  else {
    alert("Seleziona una classe e un robot");
    console.log("Seleziona una classe e un robot");
  }

}

function redirectToPagemain() {
  window.location.href = "/main";
}

function redirectToPageGamemode() {
  window.location.href = "/gamemode";
}

// function redirectToPageeditor() {

//   gamemode = localStorage.getItem("modalita");

//   if (gamemode === "Allenamento") {
//     let className = localStorage.getItem("classe");
//     let userId = parseJwt(getCookie("jwt")).userId;
//     removeAllenamentoFolders(className, userId);
//     alert("GET /remove-allenamento, cartelle di allenamento rimosse con successo, a breve verrao re-indirizzato all'editorAllenamento");
//     window.location.href = "/editorAllenamento";
//     // $.ajax({
//     //   url: '/remove-allenamento',
//     //   type: 'GET',
//     //   timeout: 30000,
//     //   success: function (data, textStatus, xhr) {
//     //     console.log("Cartelle di allenamento rimosse con successo");
//     //     alert("GET /remove-allenamento, cartelle di allenamento rimosse con successo, a breve verrao re-indirizzato all'editorAllenamento");
//     //     window.location.href = "/editorAllenamento";
//     //   },
//     //   error: function (xhr, textStatus, errorThrown) {
//     //     console.error("Errore durante la rimozione delle cartelle di allenamento:", errorThrown);
//     //   }
//     // // });

//   } else {
//     $.ajax({
//       url: '/api/save-data',
//       data: {
//         playerId: parseJwt(getCookie("jwt")).userId,
//         classe: classe,
//         robot: robot,
//         difficulty: difficulty,
//         username: localStorage.getItem("username")
//       },
//       type: 'POST',
//       traditional: true,
//       success: function (response) {
//         localStorage.setItem("gameId", response.game_id);
//         localStorage.setItem("turnId", response.turn_id);
//         localStorage.setItem("roundId", response.round_id);
//         localStorage.setItem("orderTurno", "1");
//         window.location.href = "/editor";
//       },
//       dataType: "json",
//       error: function (error) {
//         console.log("Error details:", error);
//         console.log("USERNAME : ", localStorage.getItem("username"));
//         console.error('Errore nell invio dei dati');
//         alert("Dati non inviati con successo. Riprovare");
//       }

//     })
//   }
// }
function redirectToPageeditor() {

  console.log("reidrectToPageeditor() called.");
  let gamemode = localStorage.getItem("modalita");

  switch (gamemode) {

    case "Allenamento":
      let className = localStorage.getItem("classe");
      let userId = parseJwt(getCookie("jwt")).userId;
      removeAllenamentoFolders(className, userId);
      // swal("Successo!", "(GET /remove-allenamento) Cartelle di allenamento rimosse con successo, a breve verrai re-indirizzato all'editorAllenamento", "success");
      alert("(GET /remove-allenamento) Cartelle di allenamento rimosse con successo, a breve verrai re-indirizzato all'editorAllenamento.");
      window.location.href = "/editorAllenamento";
      break;

    case "Sfida":
      $.ajax({
        url: '/api/save-data',
        data: {
          playerId: parseJwt(getCookie("jwt")).userId,
          classe: classe,
          robot: robot,
          difficulty: difficulty,
          username: localStorage.getItem("username"),
          gamemode: "Sfida"
        },
        type: 'POST',
        traditional: true,
        success: function (response) {
          localStorage.setItem("gameId", response.game_id);
          localStorage.setItem("turnId", response.turn_id);
          localStorage.setItem("roundId", response.round_id);
          localStorage.setItem("orderTurno", "1");
          window.location.href = "/editor";
        },
        dataType: "json",
        error: function (error) {
          console.log("Error details:", error);
          console.log("USERNAME : ", localStorage.getItem("username"));
          console.error('Errore nell\' invio dei dati');
          // swal("Errore", "Dati non inviati con successo. Riprovare.", "error");
          alert("Dati non inviati con successo. Riprovare.");
        }
      });
      break;
    case "Scalata":
      console.log("Salvataggio dei dati per la Scalata");
      //TODO:Continuare da qui 
  }
}

// Funzione per gestire il click sul bottone di download
function downloadFile() {
  fileId = classe;
  if (fileId) {
    const downloadUrl = '/api/downloadFile/' + fileId;

    fetch(downloadUrl, {
      method: 'GET',
    })
      .then(function (response) {
        if (response.ok) {
          return response.blob();
        } else {
          throw new Error('Errore nella risposta del server');
        }
      })
      .then(function (blob) {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = "class.java";
        link.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(function (error) {
        console.error('Errore nel download del file', error);
      });
  } else {
    console.log('Nessun file selezionato');
  }
}


function redirectToLogin() {
  if (confirm("Sei sicuro di voler effettuare il logout?")) {
    const jwt = getCookie('jwt');
    console.log("jwt token: ");
    console.log(jwt);
    fetch(`/logout?authToken=${encodeURIComponent(jwt)}`, {
      method: 'POST',
      //body: JSON.stringify({ authToken: jwt }),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Richiesta logout non andata a buon fine');
        }
        else {
          console.log("stai per essere reindirizzato alla pagina di login");
          alert("Logout avvenuto con successo, a breve verrai re-indirizzato alla pagina di login");
          window.location.href = "/login";
        }
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }
}

function redirectToUserInfo() {
  console.log("Redirect to UserInfo");
  window.location.href = "/profile";
}

function saveLoginData() {
  var username = parseJwt(getCookie("jwt")).sub;
  username = username.toString();
  localStorage.setItem("username", username);
  console.log("username :", username);
}
