$(document).ready(function() {
    // Leggi il valore di data-default di section1
    let section1 = document.getElementById('section-1');
    let rankByPoints = section1.getAttribute('data-default');

    // Se rankByPoints è 1, lancia i confetti
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

    // Inizializza entrambe le tabelle con DataTable (se necessario)
    $('#pointsTable').DataTable({
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

    $('#gamesTable').DataTable({
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
    }).destroy(); // Distruggi per partire nascosto

    // Imposta visibilità iniziale
    $('#pointsTable').show();
    $('#gamesTable').hide();
});

// Rileva il toggle e aggiorna il contenuto
const toggleInput = document.getElementById('toggle');
const elements = document.querySelectorAll('[data-default][data-checked]');

// Aggiungi un event listener per il cambiamento del toggle
toggleInput.addEventListener('change', () => {
    // Controlla lo stato del toggle e aggiorna section1
    let section1 = document.getElementById('section-1');
    if (toggleInput.checked) {
        // Usa data-checked quando il toggle è attivo
        let rankByGames = section1.getAttribute('data-checked');
        section1.textContent = rankByGames;

        // Lancia i confetti se rankByGames è 1
        if (rankByGames == 1) {
            confetti({
                particleCount: 100,
                spread: 70,
                origin: { y: 0.6 }
            });
        }
    } else {
        // Usa data-default quando il toggle è inattivo
        let rankByPoints = section1.getAttribute('data-default');
        section1.textContent = rankByPoints;

        // Lancia i confetti se rankByPoints è 1
        if (rankByPoints == 1) {
            confetti({
                particleCount: 100,
                spread: 70,
                origin: { y: 0.6 }
            });
        }
    }

    // Aggiorna i valori degli altri elementi
    elements.forEach(element => {
        if (toggleInput.checked) {
            element.textContent = element.getAttribute('data-checked');
        } else {
            element.textContent = element.getAttribute('data-default');
        }
    });

    const pointsTable = $('#pointsTable');
    const gamesTable = $('#gamesTable');

    // Alterna la visualizzazione e reinizializza DataTable
    if (pointsTable.is(':visible')) {
        pointsTable.DataTable().destroy(); // Distruggi DataTable per pointsTable
        pointsTable.hide(); // Nascondi la tabella
        gamesTable.show(); // Mostra la tabella
        gamesTable.DataTable({
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
    } else {
        gamesTable.DataTable().destroy(); // Distruggi DataTable per gamesTable
        gamesTable.hide(); // Nascondi la tabella
        pointsTable.show(); // Mostra la tabella
        pointsTable.DataTable({
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
    }

    console.log('Toggle cambiato: testi aggiornati.');
});
