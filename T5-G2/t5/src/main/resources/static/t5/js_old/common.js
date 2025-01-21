//EDIT: Oltre al parse della classe per ogni round, viene effettuato il parse della classe per ogni robot e difficoltà
function getScalataClasse(roundId, scalateJsonArray) {
    return JSON.parse(scalateJsonArray)[roundId];
}
function getScalataRobot(roundId, scalateJsonArray) {
    return JSON.parse(scalateJsonArray)[roundId];
}
function getScalataDifficulty(roundId, scalateJsonArray) {
    const difficulty =  JSON.parse(scalateJsonArray)[roundId];
    return getDifficulty(JSON.parse(scalateJsonArray)[roundId]);
}
function getDifficulty(difficulty) {
    switch (difficulty) {
        case 'Beginner':
            return 1;
        case 'Intermediate':
            return 2;
        case 'Advanced':
            return 3;
        default:
            return '';
    }
  }

//chiamata non più in uso
async function createGame(robot, classe, difficulty, scalataId, username, gamemode) {
    
    console.log("[createGame] robot: ", robot, " classe: ", classe, " difficulty: ", difficulty, " scalataId: ", scalataId, " username: ", username, "gamemode: ", gamemode);
     // salvo il nome della classe come underTestClassName
    return new Promise((resolve, reject) => { 
        $.ajax({ 
            /* Chiamata vecchia
            url: '/api/save-data',
            data: {
            playerId: parseJwt(getCookie("jwt")).userId,
            classe: classe,
            robot: robot,
            difficulty: difficulty,
            selectedScalata: scalataId,
            gamemode: gamemode,
            username: parseJwt(getCookie("jwt")).sub
            },
            */
           /*Chiamata nuova */
           
           url: '/StartGame',
            data: {
                playerId: parseJwt(getCookie("jwt")).userId,
                type_robot: robot,
                difficulty: difficulty,
                mode:"Sfida", //Inseriamo Sfida perché le scalate non sono state implementate
                underTestClassName: classe,
                //scalata_
            },
           
            type: 'POST',
            traditional: true,
            success: function (response) {
                //la risposta non fornisce ID e non fa chiamate a database 
                if (!response || $.isEmptyObject(response)) {
                    response = {data: 'SampleData'};
                }
                //localStorage.setItem("gameId", response.game_id);
                //localStorage.setItem("turnId", response.turn_id);
                //localStorage.setItem("roundId", response.round_id); 
                resolve(response);
                
            },
            //dataType: "json",
            error: function (error) {
                console.log("Error details:", error);
                console.log("USERNAME : ", localStorage.getItem("username"));
                console.error('Errore nell\' invio dei dati');
                reject(error);
                // swal("Errore", "Dati non inviati con successo. Riprovare.", "error");
            }
        })
    })
}


async function incrementScalataRound(scalataId, roundId) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/scalates/'+scalataId,
            type: 'PUT',
            contentType: "application/json",
            data: JSON.stringify({
                CurrentRound: roundId
            }),
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error('Errore nell\' invio dei dati');
                reject(error);
            }
        })
    })
}


async function closeScalata(scalataId, isWin, finalScore, roundId) {
    data = JSON.stringify({
        CurrentRound: parseInt(roundId),
        IsFinished: isWin,
        FinalScore: finalScore,
        ClosedAt: new Date().toISOString()  
    });
    console.log("[closeScalata] json data: "+data);
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/scalates/'+scalataId,
            type: 'PUT',
            contentType: "application/json",
            data: data,
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error('Errore nell\' invio dei dati');
                reject(error);
            }
        })
    })
}

//TODO: implement in backend t6
async function calculateFinalScore(scalataId) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: '/api/calculateScalataFinalScore',
            data: {
                scalataId: scalataId
            },
            type: 'GET',
            dataType: "json",
            success: function (response) {
                resolve(response);
            },
            error: function (error) {
                console.log("Error details:", error);
                console.error('Errore nell\' invio dei dati');
                reject(error);
            }
        })
    })
}