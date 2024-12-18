let robotFiles = {};
function updateDropdowns() {
    const dropdowns = document.querySelectorAll('#dropdownContainer select');

    // Trova i valori selezionati
    const selectedValues = Array.from(dropdowns)
        .map(dropdown => dropdown.value)
        .filter(value => value); // Filtra solo i valori non vuoti

    // Trova tutte le opzioni disponibili in base al primo dropdown
    const allOptions = Array.from(dropdowns[0].querySelectorAll('option'))
        .map(option => option.value)
        .filter(value => value); // Esclude il valore vuoto

    // Aggiorna le opzioni nei menu a tendina
    dropdowns.forEach(dropdown => {
        const options = dropdown.querySelectorAll('option');
        options.forEach(option => {
            if (selectedValues.includes(option.value) && option.value !== dropdown.value) {
                option.style.display = 'none'; // Nasconde le opzioni già selezionate
            } else {
                option.style.display = ''; // Mostra le opzioni disponibili
            }
        });
    });

    // Nascondi il pulsante "+" se tutti i robot sono stati selezionati
    const addButtonContainer = document.getElementById('addButtonContainer');
    if (selectedValues.length === allOptions.length) {
        addButtonContainer.style.display = 'none';
    } else {
        addButtonContainer.style.display = 'block';
    }
}

// Aggiunge un nuovo menu a tendina con input file
function addDropdown() {
    const container = document.getElementById('dropdownContainer');
    
    // Clona il primo gruppo di dropdown
    const firstDropdownGroup = container.querySelector('.dropdown-group');
    const newDropdownGroup = firstDropdownGroup.cloneNode(true);
    
    
    // Ripulisci i valori selezionati nel nuovo gruppo
    const newDropdown = newDropdownGroup.querySelector('select');


    newDropdown.value = ""; // Resetta la selezione
    newDropdown.setAttribute("onchange", "updateDropdowns()"); // Aggiungi evento onchange
    
    // Svuota l'input file nel nuovo gruppo
    const fileInput = newDropdownGroup.querySelector('.test-input');
    fileInput.value = "";

    /*const dropdownCount = container.querySelectorAll('.dropdown-group').length + 1;
    const robotName = `robot${dropdownCount}`;
    console.log(robotName);
    fileInput.setAttribute("onchange", `handleFileChange(event, '${robotName}')`); // Aggiungi evento onchange*/
    newDropdown.addEventListener('change', () => {
        const robotName = newDropdown.value; // Assegna robotName al valore selezionato
        console.log("Nome del robot selezionato:", robotName);
        fileInput.setAttribute("onchange", `handleFileChange(event, '${robotName}')`); // Aggiungi evento onchange per file input
    });

    // Rimuovi il messaggio di testo nel nuovo gruppo
    const firstMessage = newDropdownGroup.querySelector('.first-message');
    if (firstMessage) {
        firstMessage.remove();
    }

    // Aggiungi il nuovo gruppo di dropdown al contenitore
    container.appendChild(newDropdownGroup);
    
     // Sposta il pulsante "+" alla fine del contenitore
    const addButtonContainer = document.getElementById('addButtonContainer');
    container.appendChild(addButtonContainer);
    // Aggiorna le opzioni disponibili in tutti i dropdown
    updateDropdowns();
}

// Funzione per caricare i robot dal server tramite API
function loadRobots() {
    fetch('/listofrobots')  // Chiamata GET per ottenere la lista dei robot
    .then(response => response.json())
    .then(robots => {
        console.log(robots); 
        // Ottieni il dropdown per i robot
        const robotDropdown = document.getElementById('robotDropdown');
        // Pulisci le opzioni esistenti (tranne la prima)
        robotDropdown.innerHTML = '<option value="">Select a robot</option>';
        // Aggiungi ogni robot come opzione nel menu a tendina
        robots.forEach(robot => {
            const option = document.createElement('option');
            option.value = robot;
            option.textContent = robot;
            robotDropdown.appendChild(option);
        });
    })
    .catch(error => {
        console.error('Error loading robots:', error);
    });
}

// Chiamare la funzione per caricare i robot quando la pagina è pronta
//window.onload = loadRobots;
window.onload = function() {
    // Carica i robot nel primo dropdown
    loadRobots();

    // Configura l'evento onchange per il primo dropdown
    const firstDropdown = document.querySelector('#robotDropdown');
    firstDropdown.addEventListener('change', () => {
        const robotName = firstDropdown.value; // Usa il valore selezionato come robotName
        console.log("Nome del primo robot selezionato:", robotName);

        // Configura l'input file per il primo robot
        const firstFileInput = document.querySelector('.dropdown-group .test-input');
        if (firstFileInput) {
            firstFileInput.setAttribute("onchange", `handleFileChange(event, '${robotName}')`);
        }
    });
};

                    
function goBack() {
    window.history.back();
}

function uploadTest(event) {
    const name = document.getElementById('className').value;
    const date = document.getElementById('date').value;
    const difficulty = document.getElementById('difficulty').value;
    const description = document.getElementById('description').value;
    const category = [
        document.getElementById('category1').value,
        document.getElementById('category2').value,
        document.getElementById('category3').value
    ];

    const classInput = document.getElementById('fileInput');
    const file = classInput.files[0];

    // Crea FormData per la classe
    const formData = new FormData();
    formData.append('file', file);
    formData.append('model', JSON.stringify({
        name: name,
        date: date,
        difficulty: difficulty,
        description: description,
        category: category
    }));

    // Aggiungi i file test di ciascun robot dalla mappa robotFiles
    for (let robotName in robotFiles) {
        const testFile = robotFiles[robotName];
        formData.append(robotName, testFile);
    }

    console.log('Dati in FormData:');
    formData.forEach((value, key) => {
        console.log(`${key}:`, value);
    });
                        
    // Carica il tutto
    document.getElementById('loadingOverlay').style.display = 'block';
    $.ajax({
        url: '/uploadTest',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            console.log('Success:', data);
            $('#loadingOverlay').hide();
            $('#successModal').modal('show');
        },
        /*error: function(error) {
            console.error('Error:', error);
            $('#loadingOverlay').hide();
            alert('Si è verificato un errore durante la richiesta. Classe già presente nel sistema."');
        }*/
        error: function(error) {
    console.error('Error:', error);
    $('#loadingOverlay').hide();

    // Prova a estrarre il messaggio d'errore dinamico
    let errorMessage = 'Si è verificato un errore durante la richiesta.';
    if (error.responseJSON && error.responseJSON.message) {
        // Caso in cui il server risponde con JSON
        errorMessage = error.responseJSON.message;
    } 

    // Mostra il messaggio d'errore dinamico
    alert(errorMessage);
}
    });
}

// Funzione per gestire il cambiamento del file caricato per ciascun robot
function handleFileChange(event, robotName) {
    const fileInput = event.target;
    const file = fileInput.files[0];
    
    // Aggiungi il file alla mappa robotFiles
    if (file) {
        robotFiles[robotName] = file;
    }
    console.log("Aggiungo il file alla lista '${robotName}'")
}

function goBack() {
    window.history.back();    //MODIFICA (05/11/2024) tasto Go Back
}

function redirectToClass() {
    window.location.href = '/class';
}
