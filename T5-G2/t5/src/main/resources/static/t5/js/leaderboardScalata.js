var username = "";
var leaderboardData = {};
var scalateData = new Set();
var playerName = "";
var playerSurname = "";
var playersGetTracker = [];

const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
};

const parseJwt = (token) => {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
};


document.addEventListener('DOMContentLoaded', () => {
    username = parseJwt(getCookie("jwt")).sub; // Sostituisci con il nome del partecipante che sta guardando la classifica
    scalateData.clear();
    getScalate().then((response) => {
        for (let i = 0; i < response["data"].length; i++) {
            if(response["data"][i]["isFinished"] === true){
                scalateData.add(response["data"][i]["scalataName"]);

                if (!(response["data"][i]["scalataName"] in leaderboardData)) {
                    leaderboardData[response["data"][i]["scalataName"]] = [];
                }
                playersGetTracker.push(getUsername(response["data"][i]["playerID"]));
                getUsername(response["data"][i]["playerID"]).then((responseGetUsername) => {
                    console.log("responseGetUsername: ", responseGetUsername);
                    if (responseGetUsername["email"] === username) {
                        console.log("responseGetUsername: checking player is equal to our player");
                        playerName = responseGetUsername["name"];
                        playerSurname = responseGetUsername["surname"];
                    }
                    leaderboardData[response["data"][i]["scalataName"]].push({
                        nome: responseGetUsername["name"] + " " + responseGetUsername["surname"],
                        punteggio: response["data"][i]["finalScore"],
                        tempo: getTimePassedInMillis(response["data"][i]["createdAt"], response["data"][i]["closedAt"]),
                    });
                    playersGetTracker.pop();
                }).catch((error) => {
                    console.log("Error on get username details:", error);
                    playersGetTracker.pop();
                }).finally(() => {
                    if (playersGetTracker.length === 0) {
                        updateScalateList();
                        const scalataSelector = document.getElementById('scalataSelector');
                    
                        scalataSelector.addEventListener('change', (event) => {
                            const selectedScalata = event.target.value;
                            updateLeaderboard(selectedScalata);
                        });
                        // Imposta la classifica iniziale alla prima partita
                        updateLeaderboard(scalataSelector.value);
                    }
                });
            }
        }
        updateScalateList();
        const scalataSelector = document.getElementById('scalataSelector');
    
        scalataSelector.addEventListener('change', (event) => {
            const selectedScalata = event.target.value;
            updateLeaderboard(selectedScalata);
        });
        // Imposta la classifica iniziale alla prima partita
        updateLeaderboard(scalataSelector.value);
    });
});


async function getScalate() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/scalates',
            type: 'GET',
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error('Errore nel recupero dei dati');
                reject(error);
            }
        })
    })
}

async function getUsername(playerId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/students_list/' + playerId,
            type: 'GET',
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error('Errore nel recupero dei dati');
                reject(error);
            }
        })
    })
}


function getTimePassedInString(timeDifferenceInMillis) {
    // Converting milliseconds to a more readable format
    const millisecondsInSecond = 1000;
    const secondsInMinute = 60;
    const minutesInHour = 60;

    const diffInSeconds = Math.floor(timeDifferenceInMillis / millisecondsInSecond);
    const diffInMinutes = Math.floor(diffInSeconds / secondsInMinute);
    const diffInHours = Math.floor(diffInMinutes / minutesInHour);

    const remainingMinutes = diffInMinutes % minutesInHour;
    const remainingSeconds = diffInSeconds % secondsInMinute;
    return `${diffInHours}h ${remainingMinutes}m ${remainingSeconds}s`
}

function getTimePassedInMillis(createdAt, closedAt) {
    const createdAtDate = new Date(createdAt);
    const closedAtDate = new Date(closedAt);

    // Calculating the difference in milliseconds
    const timeDifference = closedAtDate - createdAtDate;
    return timeDifference;
}


function updateLeaderboard(scalata) {
    const data = leaderboardData[scalata];
    const leaderboardTable = document.getElementById('leaderboardTable');
    const participantRow = document.getElementById('participantRow');
    let rows = '';
    //TODO: sort data by punteggio and time
    data.sort((a, b) => {
        // Compare primary value (score)
        if (a.punteggio > b.punteggio) {
            return -1;
        } else if (a.punteggio < b.punteggio) {
            return 1;
        } else {
            // If scores are equal, compare secondary value (age)
            if (a.tempo < b.tempo) {
            return -1;
            } else if (a.tempo > b.tempo) {
            return 1;
            } else {
            return 0;
            }
        }
    });
    console.log("updateLeaderboard: username " + username);
    console.log("updateLeaderboard: scalata " + scalata);
    console.log("updateLeaderboard " + data);
    console.log("updateLeaderboard: player " + playerName + " " + playerSurname);
    data.forEach((entry, index) => {
        const highlightClass = entry.nome === playerName + " " + playerSurname ? 'highlight' : '';
        rows += `<tr class="${highlightClass}">
                    <td>${index+1}</td>
                    <td>${entry.nome}</td>
                    <td>${entry.punteggio}</td>
                    <td>${getTimePassedInString(entry.tempo)}</td>
                 </tr>`;
    });

    leaderboardTable.innerHTML = `
        <tr>
            <th>Posizione</th>
            <th>Nome</th>
            <th>Punteggio</th>
            <th>Tempo</th>
        </tr>
        ${rows}
    `;

    const playerIndex = data.findIndex(entry => entry.nome === playerName + " " + playerSurname);
    const playerData = data[playerIndex];
    if (playerData) {
        participantRow.innerHTML = `
            <tr>
                <td>${playerIndex+1}</td>
                <td>${playerData.nome}</td>
                <td>${playerData.punteggio}</td>
                <td>${getTimePassedInString(playerData.tempo)}</td>
            </tr>
        `;
    }
}

function updateScalateList() {
    // Get the <select> element by its ID
    const selectElement = document.getElementById('scalataSelector');

    // Clear all existing options in the <select> element
    selectElement.innerHTML = '';

    // Loop through the Set and add options to the <select> element
    scalateData.forEach(optionValue => {
        // Create a new <option> element
        const optionElement = document.createElement('option');
        // Set the value and text content of the <option>
        optionElement.value = optionValue;
        optionElement.textContent = optionValue;
        // Append the <option> to the <select> element
        selectElement.appendChild(optionElement);
    });
}
