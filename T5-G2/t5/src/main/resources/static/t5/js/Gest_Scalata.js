let selectedScalata = null;       // Oggetto che rappresenta la scalata selezionata
let currentChallengeIndex = 0;    // Indice della sfida corrente nella scalata
let totalScore = 0;               // Punteggio totale accumulato
let nextChallenge = null;         // Prossima sfida
let scalataId=0;

 
 
function handleGameLoss() {
    currentChallengeIndex = getCurrentChallengeIndex();
    scalataName=localStorage.getItem("scalataName");

    if(0==currentChallengeIndex){
        CreaScalata(scalataName);
    }
    flush_localStorage(); // Pulisci i dati salvati

    openModalWithText(
        "Scalata non completata",
        "Peccato! Hai perso.",
        [{ text: "Torna alla Home", href: "/main", class: "btn btn-primary" }]
    );
    
}
 
function handleGameProgress(userScore) {
    
    selectedScalata = JSON.parse(localStorage.getItem("ElencoScalate"));
    currentChallengeIndex = getCurrentChallengeIndex();
    scalataName=localStorage.getItem("scalataName");

    if(0==currentChallengeIndex){
        CreaScalata(scalataName);
    }
    
    totalScore = updateTotalScore(userScore);
 
    if (currentChallengeIndex < selectedScalata.length - 1) {
        proceedToNextChallenge(selectedScalata, currentChallengeIndex, userScore);
    } else {
        completeScalata(totalScore);
    }
}
 
function getCurrentChallengeIndex() {
    return parseInt(localStorage.getItem("currentChallengeIndex"), 10) || 0;
}
 
function updateTotalScore(userScore) {
    totalScore = parseInt(localStorage.getItem("total_score"), 10) || 0;
    totalScore += userScore;
    localStorage.setItem("total_score", totalScore);
    console.log("Total score aggiornato:", totalScore);
    return totalScore;
}
 
function proceedToNextChallenge(selectedScalata, currentChallengeIndex, userScore) {
    nextChallenge = selectedScalata[currentChallengeIndex + 1];
    localStorage.setItem("currentChallengeIndex", currentChallengeIndex + 1);
    localStorage.setItem("underTestClassName", nextChallenge);
    localStorage.removeItem("codeMirrorContent"); // Pulisce il contenuto per il prossimo round
    resetButtons();
 
    openModalWithText(
        "Turno completato",
        `Hai superato una nuova sfida, non mollare!
         Il tuo punteggio attuale è: ${userScore}`,
        [{ text: "Vai al prossimo round", href: `/editor?ClassUT=${nextChallenge}`, class: "btn btn-primary" }]
    );
}
 
function completeScalata(totalScore) {

    scalataId=localStorage.getItem("SCALATAID");
    console.log("ScalataGameId completeScalata: " + scalataId);
 
    closeScalata(scalataId, true, totalScore); // Chiude la scalata
    flush_localStorage(); // Pulisce i dati salvati
 
    openModalWithText(
        "Scalata completata",
        `Congratulazioni! Hai completato la scalata. 
        Il tuo punteggio totale è: ${totalScore}`,
        [{ text: "Vai alla Classifica", href: "/leaderboardScalata", class: "btn btn-primary" }]
    );
}

function closeScalata(scalataId, isWin, finalScore) {


    const data = JSON.stringify({
        //CurrentRound: parseInt(roundId), // Round corrente
        IsFinished: isWin, // Stato di completamento
        FinalScore: finalScore, // Punteggio finale
        ClosedAt: new Date().toISOString() // Data e ora di chiusura
    });
    console.log("[closeScalata] json data: " + data);
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/scalates/' + scalataId, // URL dell'API
            type: 'PUT',
            contentType: "application/json",
            data: data, // Dati della scalata
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error("Errore nell'invio dei dati");
                reject(error);
            }
        });
    });
}


function getProgressColor(percentage) {
    const red = percentage < 50 ? 255 : Math.floor(255 - (percentage * 2 - 100) * 255 / 100);
    const green = percentage > 50 ? 255 : Math.floor((percentage * 2) * 255 / 100);
    return `rgb(${red}, ${green}, 0)`;
}


function ProgressBar(){

 // Calcola la percentuale di progresso
 const selectedScalata = JSON.parse(localStorage.getItem("ElencoScalate"));
 const totalChallenges = selectedScalata.length;
 const currentIndex = parseInt(localStorage.getItem("currentChallengeIndex"), 10) || 0;
 let progressPercentage = ((currentIndex) / totalChallenges) * 100;
 progressPercentage = Math.min(progressPercentage, 100);

 // Determina il colore della barra di progresso in base alla percentuale
 let progressColor=getProgressColor(progressPercentage);

     // Crea il contenitore della barra di progresso
     const progressBarContainer = document.createElement('div');
     progressBarContainer.className = 'progress-bar-container';
     progressBarContainer.style.position = 'relative'; // Aggiunto per il posizionamento assoluto

     // Crea la barra di progresso
     const progressBar = document.createElement('div');
     progressBar.className = 'progress-bar';
     progressBar.style.width = '0%'; // Imposta inizialmente a 0% per l'animazione
     progressBar.style.backgroundColor = progressColor; // Imposta il colore dinamico

     // Aggiungi la barra al contenitore
     progressBarContainer.appendChild(progressBar);

     // Crea e stila il testo della percentuale
     const progressText = document.createElement('span');
     progressText.className = 'progress-text';
     progressText.innerText = `${Math.round(progressPercentage)}%`;
     progressText.style.position = 'absolute';
     progressText.style.top = '50%';
     progressText.style.left = '50%';
     progressText.style.transform = 'translate(-50%, -50%)';
     progressText.style.color = '#2b3035'; 
     progressText.style.fontWeight = 'bold';
     progressText.style.fontSize = '16px';

     // Aggiungi il testo al contenitore
     progressBarContainer.appendChild(progressText);

     // Aggiungi il contenitore al corpo del modal
     document.getElementById('Modal_body').appendChild(progressBarContainer);

     // Anima la barra di progresso
     setTimeout(() => {
         progressBar.style.width = `${progressPercentage}%`;
     }, 100); // Aggiunge un lieve ritardo per animare

}

function CreaScalata(selectedScalata) {
   
        
    $.ajax({
        url: '/api/save-scalata', // URL dell'API
        type: 'POST',
        data: {
            playerID: parseJwt(getCookie("jwt")).userId, // ID del giocatore
            scalataName: selectedScalata // Nome della scalata
        },
        success: function(data) {
            var result = JSON.parse(data);

            console.log("ScalataGameId Gamelogic: " + result.scalataGameId);
         
            // Salva i dati della scalata nel localStorage
            localStorage.setItem("SCALATAID", result.scalataGameId);
            temp=localStorage.getItem("SCALATAID");
            console.log("ScalataGameId Gamelogic: "+ temp);
        },
        error: function(error) {
            console.log(error);
        }
    });
}


