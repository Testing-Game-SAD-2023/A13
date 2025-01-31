
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

            //EDIT: info per sapere se è predefinita o personalizzabile
            var infoButton = document.createElement('button');
            infoButton.textContent = "Info";
            infoButton.classList.add('btn', 'btn-info', 'btn-sm', 'ml-2');
            infoButton.setAttribute('data-scalata', item.scalataName);

            infoButton.onclick = function() {
                //robot nella scalata
                var robots = item.selectedClasses.map(function(classItem) {
                    return classItem.robot;
                });

                //solo nomi unici
                var uniqueRobots = [...new Set(robots)];

                //quindi se scalata è randoop allora in uniquerobot ci sarà un solo elemento randoop, ecc..
                var isPredefinitaRandoop = uniqueRobots.length === 1 && uniqueRobots[0] === "Randoop";
                var isPredefinitaEvosuite = uniqueRobots.length === 1 && uniqueRobots[0] === "Evosuite";
                var isPersonalizzabile = uniqueRobots.includes("Randoop") && uniqueRobots.includes("Evosuite");

                var message = "";

                if (isPersonalizzabile) {
                    message = "Questa scalata è personalizzabile.";
                } else if (isPredefinitaRandoop) {
                    message = "Questa scalata è predefinita Randoop.";
                } else if (isPredefinitaEvosuite) {
                    message = "Questa scalata è predefinita Evosuite.";
                }
                
                swal("Info Scalata", message, "info");
            };

            listItem.appendChild(infoButton);

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

function returnToMenu() {
    document.getElementById('nav-go-back"').addEventListener('click', function(event) {
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
                window.location.href = "/main";
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
                retrieveScalata(selectedScalata)
                .then(() => {
                    const classUT = localStorage.getItem("ClassUT");
                    if (classUT) {
                        window.location.href = `editor?ClassUT=${classUT}`;
                    } else {
                        console.error("ClassUT not found in localStorage");
                    }
                })
                .catch((error) => {
                    console.error("Error retrieving Scalata:", error);
                });
            }
            else {
                swal("Errore!", "Si è verificato un errore imprevisto", "error");//Do nothing
            }
        }
    });
}

function retrieveScalata(scalata) {
    return new Promise((resolve, reject) => { 
        $.ajax({
            url: `/retrieve_scalata/${scalata}`,
            type: 'GET',
            success: (data) => {
                // EDIT: Estrarre i nomi delle classi dalla lista di oggetti
                const classNames = data[0].selectedClasses.map(cls => cls.className);
                const robots = data[0].selectedClasses.map(rbt => rbt.robot);
                const difficulties = data[0].selectedClasses.map(dft => dft.difficulty)

                // Salvare le informazioni necessarie in localStorage
                localStorage.setItem("SelectedScalata", scalata);
                localStorage.setItem("total_rounds_of_scalata", data[0].numberOfRounds);
                //EDIT: Vengono processate le classi, i robot e le difficoltà della scalata
                localStorage.setItem("scalata_classes", JSON.stringify(classNames));
                localStorage.setItem("scalata_robots", JSON.stringify(robots));
                localStorage.setItem("scalata_difficulties", JSON.stringify(difficulties));
                localStorage.setItem("ClassUT", classNames[0]); // Salva il nome della prima classe
                localStorage.setItem("underTestClassName", classNames[0]);
                localStorage.setItem("difficulty", getDifficulty(data[0].selectedClasses[0].difficulty));
                localStorage.setItem("robot", data[0].selectedClasses[0].robot);
                localStorage.setItem("current_round_scalata", 1);
                localStorage.setItem("scalata_score", 0);
                localStorage.setItem("modalita", "Scalata");
                localStorage.setItem("scalata_name", selectedScalata);
                
                console.log(classNames[0]);
                console.log(getDifficulty(data[0].selectedClasses[0].difficulty));
                console.log(data[0].selectedClasses[0].robot);
                
                
                resolve(data);
            },

            error: (error) => {
                reject(error);
            }
        })
    })
}

function flush_localStorage(){
	//Pulisco i dati locali 
	pulisciLocalStorage("difficulty");
	pulisciLocalStorage("robot");
	pulisciLocalStorage("roundId");
	pulisciLocalStorage("turnId");
	pulisciLocalStorage("underTestClassName");
	pulisciLocalStorage("ClassUT");
	pulisciLocalStorage("username");
	pulisciLocalStorage("storico");
	pulisciLocalStorage("codeMirrorContent");
	if(localStorage.getItem("modalita") == "Scalata"){

		pulisciLocalStorage("scalataId");
		pulisciLocalStorage("scalata_name");
		pulisciLocalStorage("scalata_classes");
		pulisciLocalStorage("scalata_robots");
		pulisciLocalStorage("scalata_difficulties");
		pulisciLocalStorage("current_round_scalata");
		pulisciLocalStorage("total_rounds_of_scalata");
		pulisciLocalStorage("scalata_score");
		
	}
	pulisciLocalStorage("modalita");
}

$(document).ready(function() {
    localStorage.setItem("modalita", "Scalata");
    updateNavbar();
    confirmLogout();
    pressedSubmit();
});