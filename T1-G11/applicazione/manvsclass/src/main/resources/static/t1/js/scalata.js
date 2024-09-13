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

      //Generate the color class for the difficulty
      var colorClass = difficultyColors[classUT.difficulty];
      difficulty.addClass(colorClass);

      //Append the card title to the card body to the card
      card.append(img);
      cardBody.append(cardTitle);
      cardBody.append(cardText);
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

    }
    else {

      swal("Errore!","La modalità 'Scalata' prevede un numero minimo di rounds pari a 2, imposta un valore corretto","error");

    }

  });

}

function selectClasses(rounds) {

  console.log("selectClasses called");

  // Clear the selectedClasses array
  selectedClasses.length = 0;

  // Get the container with the id 'classUTList'
  var container = document.getElementById('classUTList');

  // Get all div elements (cards) within the container
  // var cards = container.getElementsByTagName('div');

  // Get all div elements (cards) with the specific class within the container
  var cards = container.getElementsByClassName('col-md-3 card border-primary mb-3 mr-5');

  // Add a click event listener to each card
  for (var i = 0; i < cards.length; i++) {

    // console.log("Add EventListener to card" + i);

    cards[i].addEventListener('click', function() {

      //Check if the user has already selected the maximum number of classes
      if (selectedClasses.length >= rounds && !this.classList.contains('selected')) {

        swal("Attenzione!", "Hai già selezionato il numero massimo di classi.", "warning");
        return;
      }

      // Toggle the 'selected' class on the clicked card
      this.classList.toggle('selected');

      // Check if the class is already selected
      var index = selectedClasses.indexOf(this);

      if (index > -1) {

        // The class is already selected, remove it from the array
        selectedClasses.splice(index,1);
      }
      else {

        // The class is not selected, add it to the array
        selectedClasses.push(this);
      }

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

    //Check if the number of selected classes is equal to the number of rounds
    if (selectedClasses.length !== rounds) {

      swal("Errore!", "Seleziona un numero di classi pari al numero di rounds definiti in precedenza prima di procedere.", "error");
      return;
    }

    // Create a string that contains tha names of the selected classes
    var summary = "";
    for (var i = 0; i < selectedClasses.length; i++) {

      // Get the element with the class 'card-title' within the selected class
      var titleElement = selectedClasses[i].querySelector('.card-title');

      // Get the text content of the title element
      var title = titleElement.textContent;

      summary += "ROUND[" + (i +1) + "]: " + title + "\n";
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
        // location.reload();
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

// Call the functions after the page has been loaded
$(document).ready(function() {
  displayScalate();
  updateTotalClasses();
  updateTotalScalate();
  filterClassesByDifficulty();
  updateNavbar();
  handleRoundsChange();
  getSummary();
  handleConfirm();
});


