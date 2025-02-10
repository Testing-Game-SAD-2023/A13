
var username = "";                              //stores the username of the player from the JWT token
var selectedScalata = "";                       //stores the "Scalata" selecetd by the user

$.ajax({
    url: '/scalate_list',
    type: 'GET',
    success: function(data) {

        // Get the container id
        var scalate_container = document.getElementById('scalate-container');

        if (!scalate_container) {
            console.error('Elemento con id: "scalate-container" non trovato');
            return;
        }

        var scalate_list = document.createElement('ul');

        //Iterate trough the list of "Scalate"
        data.forEach( function(item) {

            var listItem = document.createElement('li');
            listItem.style.display = 'flex';
            listItem.style.alignItems = 'center';

            var button = document.createElement('button');
            button.textContent = item.scalataName;

            // Add Bootstrap button class
            button.classList.add('btn', 'btn-lg');

            var badge = document.createElement('span');
            badge.classList.add('badge', 'badge-info'); 
            badge.textContent = item.numberOfRounds;

            listItem.appendChild(button);
            listItem.appendChild(badge);

            button.onclick = function() {

                // Remove the 'active' class from all buttons
                var buttons = document.querySelectorAll('button');
                buttons.forEach(function(btn) {
                    btn.classList.remove('active');
                });

                // Add the 'active' class to the clicked button
                this.classList.add('active');

                selectedScalata = item.scalataName;
                console.log("Scalata selezionata: " + selectedScalata);
            };

            scalate_list.appendChild(listItem);

            tippy_configuration(button, item);
        });
        scalate_container.appendChild(scalate_list);

    },
    error: function(error) {

        swal("Errore", "Si è verificato un errore nel caricamento della lista delle 'Scalate'.", "error");
    }

});

/* Retrieve a cookie from the browser's document object,
it takes a 'name' parameter, which is the name of the
cookie you want to retrieve.
Cookies are stored as follows: "{name}={value}; {name}={value}; ..."
In order to retrieve specific cookie value, we just need
to get string that is after "; {name}=" and before next ";",
before we do any processing, we prepend the cookies string with "; ",
so that every cookie name, including the first one, is enclosed with "; " and "=":
*/
const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
  }
  
  /* This function is used to parse a JSON Web Token (JWT), it
  takes a 'token' parameter, which is the JWT that we want to parse.
  */
  const parseJwt = (token) => {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
  };

function updateNavbar() {
    //Get the username from cookie
    username = parseJwt(getCookie("jwt")).sub;
    localStorage.setItem("username", username);
    //Modify the content of the <a> element in the navbar
    $('.navbar-brand').text('Ciao, ' + username + '!');
}

function confirmLogout() {
    document.getElementById('nav-logout').addEventListener('click', function(event) {
        event.preventDefault();
        swal({
            title: "Sei sicuro?",
            text: "Sei sicuro di voler effettuare il Logout?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then((willLogout) => {
            if (willLogout) {
                window.location.href = this.href;
            }
        });
    });
}

function tippy_configuration(button, item) {
    // Add a tooltip to the button with Tippy.js
    tippy(button, {
        content: item.scalataDescription,
        placement: 'left',
        arrow: true,
        animation: 'scale-extreme',
        theme: 'translucent'
        });
}

function pressedSubmit() {
    // Get the 'Submit' button
    var submitButton = document.getElementById('submit-button');

    // Add a click event listener to the "Submit" button
    submitButton.addEventListener('click', function() {
        // Check if the user has selected a "Scalata"
        if (selectedScalata === "") {
            swal("Errore", "Devi selezionare una 'Scalata' dall'elenco per poter proseguire.", "error");
            return;
        }
        else {
            // Confirm the user's choice
            var confirm_choice = confirm("Hai selezionato la 'Scalata': " + selectedScalata + ".\nSei sicuro di voler proseguire?");
            if (confirm_choice) {
                // Confirmed. Handle the POST request
                console.log("Confirmed choice.");
                $.ajax({
                    url: '/api/save-scalata',
                    type: 'POST',
                    data: {
                        playerID: parseJwt(getCookie("jwt")).userId,
                        scalataName: selectedScalata
                    },
                    success: function(data) {
                        var result = JSON.parse(data);

                        console.log("ScalataGameId: " + result.scalataGameId);
                        localStorage.setItem("scalataId", result.scalataGameId);
                        localStorage.setItem("difficulty", 1);
                        localStorage.setItem("robot", "evosuite");
                        localStorage.setItem("current_round_scalata", 1);
                        localStorage.setItem("scalata_name", selectedScalata);

                        retrieveScalata(selectedScalata)
                            .then( data => {return createGame("evosuite", data[0].selectedClasses[0], 1, result.scalataGameId, username, "Scalata")})
                            .then( data => {
                                return swal("Successo!", "La tua scelta è stata confermata, a breve verrai reindirizzato all'arena di gioco.", "success");
                            })
                            .then( () => {
                                console.log(data);
                                window.location.href = "editor_old";
                            })
                            .catch((error) => {
                                console.log("error: "+ error);
                                swal("Errore!", "Si è verificato un errore durante la creazione del gioco. Riprovare.", "error");
                            })
                    },
                    error: function(error) {
                        console.log(error);
                    }
                });
            }
            else {
                //Do nothing
            }
        }
    });
}


async function retrieveScalata(scalata) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: `/retrieve_scalata/${scalata}`,
            type: 'GET',
            success: (data) => {
                localStorage.setItem("SelectedScalata", selectedScalata);
                localStorage.setItem("total_rounds_of_scalata", data[0].numberOfRounds);
                localStorage.setItem("scalata_classes", JSON.stringify(data[0].selectedClasses));
                localStorage.setItem("classe", data[0].selectedClasses[0]);
                resolve(data);
            },
            error: (error) => {
                reject(error);
            }
        })
    })
}


// async function createGame(robot, classe, difficulty, scalataId) {
//     return new Promise((resolve, reject) => { 
//         $.ajax({
//             url: '/api/save-data',
//             data: {
//             playerId: parseJwt(getCookie("jwt")).userId,
//             classe: classe,
//             robot: robot,
//             difficulty: difficulty,
//             selectedScalata: scalataId,
//             username: username
//             },
//             type: 'POST',
//             traditional: true,
//             success: function (response) {
//                 localStorage.setItem("gameId", response.game_id);
//                 localStorage.setItem("turnId", response.turn_id);
//                 localStorage.setItem("roundId", response.round_id);
//                 resolve(response);
//             },
//             dataType: "json",
//             error: function (error) {
//                 console.log("Error details:", error);
//                 console.log("USERNAME : ", localStorage.getItem("username"));
//                 console.error('Errore nell\' invio dei dati');
//                 reject(error);
//                 // swal("Errore", "Dati non inviati con successo. Riprovare.", "error");
//             }
//         })
//     })
// }

// Call the functions after the page has been loaded
$(document).ready(function() {
    localStorage.setItem("modalita", "Scalata");
    updateNavbar();
    confirmLogout();
    pressedSubmit();
});