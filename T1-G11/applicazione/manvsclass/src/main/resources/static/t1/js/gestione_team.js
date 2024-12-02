toggle = document.querySelectorAll(".toggle")[0];
nav = document.querySelectorAll("nav")[0];
toggle_open_text = 'Menu';
toggle_close_text = 'Close';

toggle.addEventListener('click', function() {
    nav.classList.toggle('open');

    if (nav.classList.contains('open')) {
        toggle.innerHTML = toggle_close_text;
    } else {
        toggle.innerHTML = toggle_open_text;
    }
}, false);

setTimeout(function(){
    nav.classList.toggle('open');
}, 800);

function returnToHome() {
    window.location.href = "/home_admin";
}



    function showSection(sectionId) {
        // Nasconde tutte le sezioni
        document.querySelectorAll('.section').forEach(function(section) {
            section.classList.remove('active');
        });

        // Mostra la sezione selezionata
        document.getElementById(sectionId).classList.add('active');
    }

    // Mostra la prima sezione di default
    document.addEventListener('DOMContentLoaded', function() {
        showSection('add-team');
    });


// function aggiungiTeam() {
//     fetch('/aggiungi_team', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ teamName: "Nuovo Team", description: "Descrizione del team" })
//     })
//     .then(response => {
//         console.log('Response:', response);
//         if(response.status == 200) {
//             response.text().then(okMessage => {
//                 alert("Team aggiunto con successo.");
//             });
//         } else {
//             response.text().then(errorMessage => {
//                 alert(errorMessage);
//             });
//         }
//     })
//     .catch((error) => {
//         console.error('Error:', error);
//         alert("Si è verificato un errore durante l'aggiunta del team.");
//     });
//}
/* 
function visualizzaTeam() {
    fetch('/visualizza_team', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(response => {
        console.log('Response:', response);
        if(response.status == 200) {
            response.json().then(data => {
                console.log("Dati del team:", data);
                alert("Team caricati con successo.");
            });
        } else {
            response.text().then(errorMessage => {
                alert(errorMessage);
            });
        }
    })
    .catch((error) => {
        console.error('Error:', error);
        alert("Si è verificato un errore durante il caricamento dei team.");
    });
}
 */