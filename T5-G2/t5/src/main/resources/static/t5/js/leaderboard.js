// Inizializza DataTables con paginazione per 10 righe
$(document).ready(function() {
    $('#studentTable').DataTable({
        "pageLength": 10,  // Imposta il numero di righe per pagina
        "bLengthChange": false,
        "pagingType": "simple", // Abilita la paginazione
        "ordering": false,
        "autoWidth": false,
        "language": {
            "info": "",  // Rimuove la scritta "Showing x of x entries"
            "infoEmpty": "",  // Rimuove la scritta "Showing 0 to 0 of 0 entries" quando la tabella è vuota
            "infoFiltered": ""  // Rimuove "filtered from x total entries"
        }
    });
});

const toggleInput = document.getElementById('toggle');
const elements = document.querySelectorAll('[data-default][data-checked]'); // Seleziona tutti gli elementi con data-default e data-checked

// Aggiungi un event listener per il cambiamento del toggle
toggleInput.addEventListener('change', () => {
    // Cicla attraverso tutti gli elementi selezionati
    elements.forEach(element => {
        if (toggleInput.checked) {
            // Quando il toggle è attivo, imposta il testo a partire da data-checked
            element.textContent = element.getAttribute('data-checked');
        } else {
            // Quando il toggle non è attivo, ripristina il testo a partire da data-default
            element.textContent = element.getAttribute('data-default');
        }
    });

    console.log('Toggle cambiato: testi aggiornati.');
});