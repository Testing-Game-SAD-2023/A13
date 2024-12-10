$(document).ready(function() {
    // Esegui la logica per il confetti basata su rankByPoints
    let section1 = document.getElementById('section-1');
    let rankByPoints = section1.getAttribute('data-default');
    if (rankByPoints == 1) {
        confetti({
            particleCount: 100,
            spread: 70,
            origin: { y: 0.6 }
        });
    }

    // Inizializza la tabella DataTable
    $('#studentTable').DataTable({
        "pageLength": 10,
        "bLengthChange": false,
        "pagingType": "simple",
        "ordering": false,
        "autoWidth": false,
        "language": {
            "info": "",
            "infoEmpty": "",
            "infoFiltered": ""
        }
    });
});

// Rileva il toggle e aggiorna gli elementi
const toggleInput = document.getElementById('toggle');
const elements = document.querySelectorAll('[data-default][data-checked]'); // Seleziona gli elementi con data-default e data-checked

// Aggiungi un event listener per il cambiamento del toggle
toggleInput.addEventListener('change', () => {
    // Cicla attraverso tutti gli elementi selezionati
    elements.forEach(element => {
        // Leggi i dati associati ad ogni elemento
        let rankByPoints = element.getAttribute('data-default');
        let rankByGames = element.getAttribute('data-checked');

        if (toggleInput.checked) {
            // Quando il toggle è attivo, usa il valore di data-checked
            element.textContent = rankByGames;

            // Esegui il confetti se il valore di rankByGames è 1
            if (rankByGames == 1) {
                confetti({
                    particleCount: 100,
                    spread: 70,
                    origin: { y: 0.6 }
                });
            }
        } else {
            // Quando il toggle non è attivo, usa il valore di data-default
            element.textContent = rankByPoints;
        }
    });
});
