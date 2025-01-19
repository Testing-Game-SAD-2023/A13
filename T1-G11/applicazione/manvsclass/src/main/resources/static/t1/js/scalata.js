var totalClasses = 0;                          //stores the total number of classes
var rounds = 0;                                //stores the number of rounds selected by the user
var username = "";                              //stores the username of the user from the JWT token
var selectedClasses = [];                      //stores the classes selected by the user

// var colorClasses = ["footer-color-1", "footer-color-2", "footer-color-3",   
//                     "footer-color-4","footer-color-5",  "footer-color-6",
//                     "footer-color-7", "footer-color-8", "footer-color-9"
// ];

var difficultyColors = {
  "Beginner" : "footer-color-1",
  "Intermediate" : "footer-color-3",
  "Advanced" : "footer-color-5"
};

var images = ["/t1/css/Images/logo_card.jpg","/t1/css/Images/logo_card_1.jpg",
              "/t1/css/Images/logo_card_2.jpg","/t1/css/Images/logo_card_3.jpg",
              "/t1/css/Images/logo_card_4.jpg", "/t1/css/Images/logo_card_5.jpg",
              "/t1/css/Images/logo_card_6.jpg", "/t1/css/Images/logo_card_7.jpg",
              "/t1/css/Images/logo_card_8.jpg", "/t1/css/Images/logo_card_9.jpg"
];
/*Retrieve all of the classes already uploaded
url: /home
type: GET
xhrFields: withCredentials: true
*/
$.ajax({
    url: "/home",
    type: "GET",
    xhrFields: {                               // Necessary to send cookies with the request
        withCredentials: true
        },
    success: function (data) {

        /* the data object returned by the AJAX call is
        an array of objects and each object represents a class
        not a single object.
        To get the total number of classes, we can use the 'length'
        property of the data array.
        */
        console.log("Fetching the classes from the server...\n");
        totalClasses = data.length;
        console.log("Total classes uploaded: "+ totalClasses+"\n");

        /* set the attribute 'max' of element with the id 'rounds'
        to the total number of classes.
        */
        document.getElementById("rounds").max = totalClasses;

        data.forEach(function(classUT) {
        console.log(classUT.name+"\n");

        //Handling the data
        console.log("Displaying the classes...\n");
        displayClasses(data);
        
      }
      );
       
    },
    error: function (error) {
        console.log("Error",error);
    }
  });

//Function to dispaly the retrieved classed into the HTML page
function displayClasses(classes) {
  console.log("displayClasses chiamata");
  const classUTList = $("#classUTList");

  /*This line of code is used to select an HTML
  element by its id (classUTList) infact the symbol
  '#' is used to select an id and the symbol '$' is
  a shorthand for the jQuery function.
  */

  // Clear the container before creating new cards
  classUTList.empty();
  console.log("Cleaning the container before creating new cards...\n");

  $.each(classes, function(index, classUT) {
      
      //Create a new card for each class
      const card = $('<div>').addClass('col-md-3 card border-primary mb-3 mr-5').attr('data-difficulty', classUT.difficulty.toLowerCase());
      // const img = $('<img>').addClass('card-img-top').attr('src', '/t1/css/Images/logo_card.jpg');

      //Generate a random image for the card
      var image = images[Math.floor(Math.random() * images.length)];
      const img = $('<img>').addClass('card-img-top').attr('src', image);

      const cardBody = $('<div>').addClass('card-body');
      const cardTitle = $('<h5>').addClass('card-title text-center').text(classUT.name);
      const cardText = $('<p>').addClass('card-text').text(classUT.description);

      // Create a new footer for the difficulty
      const difficulty = $('<div>').addClass('card-footer text-muted text-center').text('Difficulty: ' + getStars(classUT.difficulty));

            
      let coverage = 'Coperture: \n';
      if (Array.isArray(classUT.robotList) 
          && classUT.robotList.length > 0 
          && Array.isArray(classUT.coverage) 
          && classUT.coverage.length > 0
          && Array.isArray(classUT.robotDifficulty)) 
      {
      for (let i = 0; i < classUT.robotList.length; i++) {

        coverage += classUT.robotList[i] + "\r\n";

        for (let j = 0; j < classUT.robotDifficulty.length; j++) {

          coverage += classUT.robotDifficulty[j] + ": " + classUT.coverage[3*i+j]+ "\r\n";

        }
      }
      } else {
          coverage += 'N/A';
      }
      const coverageElement = $('<p>').addClass('card-text').text(coverage);

      //Generate the color class for the difficulty
      var colorClass = difficultyColors[classUT.difficulty];
      difficulty.addClass(colorClass);

      //Append the card title to the card body to the card
      card.append(img);
      cardBody.append(cardTitle);
      cardBody.append(cardText);
      cardBody.append(coverageElement);
      cardBody.append(difficulty);
      card.append(cardBody);

      //Append the card to the container
      classUTList.append(card);
 
  });

}
//Get the number of 'stars' associated to the difficulty of the class
function getStars(difficulty) {
  switch (difficulty) {
      case 'Beginner':
          return '★';
      case 'Intermediate':
          return '★★';
      case 'Advanced':
          return '★★★';
      default:
          return '';
  }
}

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

  //Modify the content of the <a> element in the navbar
  $('.navbar-brand').text('Ciao, ' + username + '!');
}

function filterClassesByDifficulty() {
  $('#difficultyFilter').on('change', function() {
      var filter = $(this).val().toLowerCase();
      var hasVisibleCards = false;

      $('.card').each(function() {
          var difficulty = $(this).attr('data-difficulty');

          if (filter === "" || difficulty === filter) {
            $(this).show();
            hasVisibleCards = true;
          } else {
            $(this).hide();
        }
      });

      if (!hasVisibleCards && filter !== "") {
        swal("Attenzione!", "Non ci sono classi con la difficoltà assegnata", "warning");
    }
  });
}
function updateTotalClasses() {
  $.ajax({
    url: '/home',
    type: 'GET',
    success: function(data) {
      
      totalClasses = data.length;
      document.getElementById('totalClassesText').textContent = " *Attualmente nel sistema sono state caricate: " + totalClasses + " classi";
    },
    error: function(error) {
      console.log("Error", error);
    }
  });
}
function updateTotalScalate() {
  $.ajax({
    url: '/scalate_list',
    type: 'GET',
    success: function(data) {
      
      totalScalate = data.length;
      document.getElementById('totalScalateText').textContent = " *Attualmente nel sistema sono state caricate: " + totalScalate + " Scalate";
    },
    error: function(error) {
      console.log("Error", error);
    }
  });

}

// Define a function to handle changes on the rounds input
function handleRoundsChange() {

  //Get the addButton element
  var addButton = document.getElementById('addRoundButton');

  // Add an event listener for the click event
  addButton.addEventListener('click', function() {

    console.log("addButton clicked.");

    // Get the rounds input element
    var roundsInput = document.getElementById('rounds');

    //Read the input value
    rounds = parseInt(roundsInput.value);
    console.log("handleRoundsChange() rounds: " + rounds);

    //Check if the value is greater than or equal to 2
    if (rounds >= 2) {

      swal("Rounds info", "La tua 'Scalata' sarà costituita da  "+ rounds + " rounds, puoi procedere con la scelta delle classi.", "info");

      // Call selectClasses with the value of the rounds input
      selectClasses(rounds);

      // Remove the 'disabled' class from the 'classUTList' div
      $('#classUTList').removeClass('disabled');
      addButton.disabled = true; // Disabilita il tasto "Aggiungi"
    }
    else {

      swal("Errore!","La modalità 'Scalata' prevede un numero minimo di rounds pari a 2, imposta un valore corretto","error");

    }

  });

}

//Modificare la funzione selectClasses per memorizzare le selezioni di robot e difficoltà
function selectClasses(rounds) {
  console.log("selectClasses called");
  selectedClasses.length = 0;
  // Get the container with the id 'classUTList'
  var container = document.getElementById('classUTList');

  // Get all div elements (cards) within the container
  var cards = container.getElementsByClassName('col-md-3 card border-primary mb-3 mr-5');
  // Add a click event listener to each card
  for (var i = 0; i < cards.length; i++) {
    cards[i].addEventListener('click', function() {
      // Prevent selecting more than the allowed number of classes
      if (selectedClasses.length >= rounds && !this.classList.contains('selected')) {
        swal("Attenzione!", "Hai già selezionato il numero massimo di classi.", "warning");
        return;
      }
      var className = $(this).find('.card-title').text();
      var robot = $(this).find('.robot-select').val();
      var difficulty = $(this).find('.difficulty-select').val();
            
      // Controllo per impedire il clic sulla carta se entrambi i campi non sono stati selezionati
      if (robot === '' || difficulty === '') {
        swal("Errore!", "Seleziona sia il robot che la difficoltà prima di selezionare la classe.", "error");
        return;
      }

      // Toggle the 'selected' class on the clicked card
      this.classList.toggle('selected');

      // Find the class data from the card's title and dropdowns
      var className = $(this).find('.card-title').text();
      var robot = $(this).find('.robot-select').val();
      var difficulty = $(this).find('.difficulty-select').val();
  
      // Find if the class is already selected
      var index = selectedClasses.findIndex(function(selectedClass) {
        return selectedClass.className === className;
      });

      if (index > -1) {
        // If it's already selected, remove it from the array
        selectedClasses.splice(index, 1);
        //riabilita i menu a tendina quando la card viene deselezionata
        $(this).find('.robot-select, .difficulty-select').prop('disabled', false);

    
      } else {
        // If not selected, add it to the array with the robot and difficulty
        selectedClasses.push({
          className: className,
          robot: robot,
          difficulty: difficulty
        });
        //disabilita i menu a tendina quando la card viene selezionata
        $(this).find('.robot-select, .difficulty-select').prop('disabled', true);

      }
    });
    // Aggiungi i gestori di eventi per i menu a tendina per impedire la propagazione dell'evento di clic
    $(cards[i]).find('.robot-select, .difficulty-select').on('click', function(event) {
    event.stopPropagation();
    });
  }
}

function getSummary() {
  // Get the "Riepilogo" button
  var summaryButton = document.getElementById('summaryButton');

  // Add a click event listener to the "Riepilogo" button
  summaryButton.addEventListener('click', function() {

    // Check if both rounds and selectedClasses are zero
    if (rounds === 0 && selectedClasses.length === 0) {
      swal("Errore!", "Definisci il numero di rounds prima di procedere.", "error");
      return;
    }

    // Check if the number of selected classes is equal to the number of rounds
    if (selectedClasses.length !== rounds) {
      swal("Errore!", "Seleziona un numero di classi pari al numero di rounds definiti in precedenza prima di procedere.", "error");
      return;
    }

    // Create a string that contains the names of the selected classes, robots, and difficulty
    var summary = "";
    for (var i = 0; i < selectedClasses.length; i++) {
      var className = selectedClasses[i].className;
      var robot = selectedClasses[i].robot;
      var difficulty = selectedClasses[i].difficulty;

      summary += "ROUND[" + (i + 1) + "]: " + className + " - Robot: " + robot + " - Difficulty: " + difficulty + "\n";
    }

    // Show the summary in a pop-up
    swal("Riepilogo 'Scalata'", "La tua 'Scalata' è costituita da: " + rounds + " rounds così ripartiti:\n" + summary, "info");
  });
}

function handleConfirm() {

  console.log("handleConfirm called!");

  // Get the "Conferma" button
  var confirmButton = document.getElementById('confirmButton');

  // Add a click event listener to the "Conferma" button
  confirmButton.addEventListener('click', function() {

    // Get the "Scalata" name
    var scalataName = document.getElementById('form-name').value;

     // Define the regex for the name validation
    var nameRegex = /^[a-zA-Z0-9_]{3,20}$/;

    //Check if the user has assigned a name
    if (!scalataName) {
      swal("Errore!", "Inserisci un nome per la tua 'Scalata' prima di procedere.", "error");
      return;
    }
    if (!nameRegex.test(scalataName)) {
      swal("Errore!", "Il nome della 'Scalata' deve contenere da 3 a 20 caratteri alfanumerici separati da un underscore (_) e.g. 'Prova_scalata'", "error");
      return;

    }
    
    // Check if both rounds and selectedClasses are zero
    if (rounds === 0 && selectedClasses.length === 0) {
      swal("Errore!", "Definisci il numero di rounds prima di procedere.", "error");
      return;
    }

    //Check if the number of selected classes is equal to the number of rounds
    if (selectedClasses.length !== rounds) {

      swal("Errore!", "Seleziona un numero di classi pari al numero di rounds definiti in precedenza prima di procedere.", "error");
      return;
    }

    // Show a confirmation pop-up
    if (confirm('Vuoi procedere con l\'operazione?')) {

      // If the user confirms, perform the POST request
      checkName(scalataName)
        .then(() => {

          // If the Promise resolved, call submitScalataData
          submitScalataData();
        })
        .catch((error) => {

          // If the Promise rejected, handle the error
          console.error('Error:', error);
        });
    }
    else {
      // If the user cancels, do nothing
    }
  });
}

function submitScalataData() {

  console.log("submitScalataData called");

  // Get the Data
  const username = parseJwt(getCookie("jwt")).sub;
  const scalataName = document.getElementById('form-name').value;
  const scalataDescription = document.getElementById('FormControlTextareaDescription').value;
  const numberOfRounds = rounds;
  const selectedClassesData = selectedClasses.map(function(classElement){

    return classElement.querySelector('.card-title').textContent;

  });

  // Create a data object
  const data = {

    username: username,
    scalataName: scalataName,
    scalataDescription: scalataDescription,
    numberOfRounds: numberOfRounds,
    selectedClasses: selectedClassesData
  };

  // Send the data to the server

  fetch('/configureScalata', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })
  .then(response => {

    console.log('Response:', response);

    if(response.status == 200) {
        
      response.text().then(okMessage => {
        
        swal("Operazione completata!", "La tua 'Scalata' è stata configurata con successo.", "success");
        
        //Reload the page
         //location.reload();
      })
    }
    else {

      response.text().then(errorMessage => {

        swal("Errore!", errorMessage, "error");
      })
    }
  })
  .catch((error) => {

    window.location.href = "/loginAdmin";
    console.error('Error:', error);

    //Aggiungi qui il codice per gestire gli errori
  });

}

function checkName(name) {

  console.log("checkName called");

  // Return a new Promise
  return new Promise((resolve, reject) => {

    // Make a GET request to the endpoint /retrieve_scalata/{scalataName}
    fetch(`/retrieve_scalata/${name}`)
    .then(response => {

      // If the response is ok, it means a Scalata with the same name exists
      if (response.ok) {
      swal("Errore","Esiste già una 'Scalata' con quel nome, scegline un altro.","error");
      reject(new Error("La 'Scalata' con il nome specificato: "+ name +  " esiste già!"));
      }
      else {

        resolve();
      }

    })
    .catch(error => {

      // Handle any errors
      console.error('Error:', error);
      reject(error);
    });

  });

}

//Funzione per visualizzare le classi
// Funzione per visualizzare le classi
function displayClasses(classes) {
  const classUTList = $("#classUTList");

  // Svuota il contenitore prima di creare nuove card
  classUTList.empty();
  console.log("Cleaning the container before creating new cards...\n");

  $.each(classes, function (index, classUT) {
    // Crea una nuova card per ogni classe
    const card = $('<div>')
      .addClass('col-md-3 card border-primary mb-3 mr-5')
      .attr('data-difficulty', classUT.difficulty.toLowerCase());

    // Genera un'immagine casuale per la card
    var image = images[Math.floor(Math.random() * images.length)];
    const img = $('<img>').addClass('card-img-top').attr('src', image);

    const cardBody = $('<div>').addClass('card-body');
    const cardTitle = $('<h5>')
      .addClass('card-title text-center')
      .text(classUT.name);
    const cardText = $('<p>').addClass('card-text').text(classUT.description);

    // Crea un footer per visualizzare la difficoltà
    const difficulty = $('<div>')
      .addClass('card-footer text-muted text-center')
      .text('Difficulty: ' + getStars(classUT.difficulty));

    let coverage = 'Coperture: \n';
    if (
      Array.isArray(classUT.robotList) &&
      classUT.robotList.length > 0 &&
      Array.isArray(classUT.coverage) &&
      classUT.coverage.length > 0 &&
      Array.isArray(classUT.robotDifficulty)
    ) {
      for (let i = 0; i < classUT.robotList.length; i++) {
        coverage += classUT.robotList[i] + "\r\n";
        for (let j = 0; j < classUT.robotDifficulty.length; j++) {
          coverage +=
            classUT.robotDifficulty[j] + ": " + classUT.coverage[3 * i + j] + "\r\n";
        }
      }
    } else {
      coverage += 'N/A';
    }
    const coverageElement = $('<p>').addClass('card-text').text(coverage);

    // Crea i dropdown per robot e difficoltà
    const robotSelect = $('<select>').addClass('form-control robot-select');
    robotSelect.append($('<option>').val('').text('')); // Aggiungi una riga vuota
    classUT.robotList.forEach(function (robot) {
      robotSelect.append($('<option>').text(robot).val(robot));
    });

    const difficultySelect = $('<select>').addClass('form-control difficulty-select');
    difficultySelect.append($('<option>').val('').text('Select difficulty')); // Aggiungi una riga vuota

    // Funzione per aggiornare il menu delle difficoltà in base al robot selezionato
    function updateDifficultySelect(selectedRobot) {
      difficultySelect.empty(); // Svuota il menu delle difficoltà
      difficultySelect.append($('<option>').val('').text('Select difficulty')); // Aggiungi una riga vuota

      const robotIndex = classUT.robotList.indexOf(selectedRobot);
      if (robotIndex !== -1) {
        // Aggiungi le difficoltà valide per il robot selezionato
        classUT.robotDifficulty.forEach(function (difficulty, index) {
          const coverageValue = classUT.coverage[3 * robotIndex + index];
          if (coverageValue != null) {
            difficultySelect.append($('<option>').text(difficulty).val(difficulty));
          }
        });
      }
    }

    // Aggiungi un listener per l'evento di cambiamento sul menu del robot
    robotSelect.on("change", function () {
      const selectedRobot = $(this).val();
      console.log("Robot selezionato:", selectedRobot);
      updateDifficultySelect(selectedRobot);
    });

    // Aggiungi il contenuto alla card
    cardBody.append(cardTitle);
    cardBody.append(cardText);
    cardBody.append(coverageElement);
    cardBody.append(difficulty);
    cardBody.append('Select Robot: ', robotSelect);
    cardBody.append('Select Difficulty: ', difficultySelect);

    // Aggiungi la card al contenitore
    card.append(img);
    card.append(cardBody);
    classUTList.append(card);
  });
}


function displayScalate() {

  const scalateList = $("#scalateContainer");

  // Clear the container before creating new cards
  scalateList.empty();
  
  /* Get to the endpoint /scalate_list in order to retrieve
  all the 'Scalate' already configured by the user and stored
  */ 
  $.get("/scalate_list", function(data) {

    $.each(data, function(index, scalata) {

      // Create a badge
      var badge = $('<span class="badge badge-warning badge-style"></span>');
      badge.text(scalata.scalataName);

      // Aggiungi un gestore di click al badge
      badge.click(function() {

      console.log("Clicked on the badge with name: " + scalata.scalataName);
      var confirmDelete = confirm("Vuoi cancellare la 'Scalata': " + scalata.scalataName + "?" );
      if (confirmDelete) {

        /* If the user clicked "OK", delete the "Scalata" with the specified name
        by making a DELETE request to the endpoint /delete_scalata/{scalataName}
        */

        // Get the name of the "Scalata" to delete
        const scalataName = scalata.scalataName;

        // Create a data object
        const data = {
          scalataName: scalataName
        };

        //Send the data to the server

        fetch(`/delete_scalata/${scalataName}`, {
          method: 'DELETE',
          header: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })
        .then(response => {

          console.log("Response:", response);

          if(response.status == 200) {

            response.text().then(okMessage => {
            swal("Operazione completata!", "La 'Scalata' è stata cancellata con successo.", "success");
            badge.remove();
            })

          } else {

            response.text().then(errorMessage => {
            swal("Errore!", errorMessage, "error");
            })
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });

      }else {
        //Do nothing
      }
      });

      // Aggiungi il badge al contenitore
      $("#scalateContainer").append(badge);

    });

  });

}

//Modificare submitScalataData per includere robot e difficoltà
function submitScalataData() {
  console.log("submitScalataData called");

  // Get the Data
  const username = parseJwt(getCookie("jwt")).sub;
  const scalataName = document.getElementById('form-name').value;
  const scalataDescription = document.getElementById('FormControlTextareaDescription').value;
  const numberOfRounds = rounds;
  
  // Prepare selected classes data with robot and difficulty
  const selectedClassesData = selectedClasses.map(function(classElement) {
    if (classElement.robot && classElement.difficulty) {
      return {
        className: classElement.className,
        robot: classElement.robot,
        difficulty: classElement.difficulty
      };
    }
    return null;
  }).filter(function(item) {
    return item !== null; // Remove any null values if there are any
  });

  // Create a data object
  const data = {
    username: username,
    scalataName: scalataName,
    scalataDescription: scalataDescription,
    numberOfRounds: numberOfRounds,
    selectedClasses: selectedClassesData
  };

  // Send the data to the server
  fetch('/configureScalata', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })
  .then(response => {
    console.log('Response:', response);
    if(response.status == 200) {
      document.getElementById('addRoundButton').disabled = false; // Riabilita il tasto "Aggiungi"
      response.text().then(okMessage => {
        swal("Operazione completata!", "La tua 'Scalata' è stata configurata con successo.", "success")
          .then(() => {
            // Reload della pagina dopo che il popup è stato chiuso
            location.reload();
          });
      });
     
    } else {
      response.text().then(errorMessage => {
        swal("Errore!", errorMessage, "error");
      });
    }
  })
  .catch((error) => {
    window.location.href = "/loginAdmin";
    console.error('Error:', error);
  });
}

// Call the functions after the page has been loaded
$(document).ready(function() {
  // Le altre funzioni che vengono chiamate quando la pagina è pronta
  displayScalate();
  displayClasses();
  selectClasses();
  updateTotalClasses();
  updateTotalScalate();
  filterClassesByDifficulty();
  updateNavbar();
  handleRoundsChange();
  getSummary();
  handleConfirm();

  // Aggiungi il codice per gestire il cambio nei select (robot e difficulty)
  $(document).on('change', '.robot-select, .difficulty-select', function() {
    // Trova il genitore (card) della selezione cambiata
    var parentCard = $(this).closest('.card');
    
    // Ottieni i nuovi valori di robot e difficulty
    var robot = parentCard.find('.robot-select').val();
    var difficulty = parentCard.find('.difficulty-select').val();
    var className = parentCard.find('.card-title').text();

    // Trova l'indice della classe selezionata nell'array selectedClasses
    var classIndex = selectedClasses.findIndex(function(item) {
      return item.className === className;
    });

    // Se la classe è trovata, aggiorna i valori di robot e difficulty
    if (classIndex > -1) {
      selectedClasses[classIndex].robot = robot;
      selectedClasses[classIndex].difficulty = difficulty;
    }
  });
});
if ("scrollRestoration" in history) {
  history.scrollRestoration = "manual";  // Impedisce al browser di ripristinare la posizione
}

window.addEventListener('load', function() {
  window.scrollTo(0, 0);
});