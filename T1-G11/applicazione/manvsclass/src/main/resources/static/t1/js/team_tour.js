var driverObj;

document.addEventListener('DOMContentLoaded', (event) => {
    // Verifica che Driver.js sia caricato correttamente 
    const driver = window.driver.js.driver;
    if (typeof driver === 'undefined') {
        console.error('Driver.js non è stato caricato correttamente.');
        return;
    }

    console.log('Driver.js è caricato correttamente!');
   
    // Inizializzazione di Driver.js
    driverObj = driver({
        showProgress: true,
        popoverClass: 'driverjs-theme',  // Puoi personalizzare il tema qui

        // Definizione dei passi per il tour
        steps: [
            {
                element: '.header',
                popover: {
                    title: 'Header',
                    description: 'Contiene il logo e il titolo della pagina.',
                    side: "bottom",
                    align: 'center'
                }
            },
            {
                element: '#search-container',
                popover: {
                    title: 'Barra di Ricerca',
                    description: 'Usa questa barra per cercare un team specifico.',
                    side: "bottom",
                    align: 'center'
                }
            },
            {
                element: '#team-section',
                popover: {
                    title: 'Sezione Team',
                    description: 'Qui puoi visualizzare e gestire i tuoi team.',
                    side: "top",
                    align: 'center'
                }
            },
            {
                element: '#openModalButtonTeam',
                popover: {
                    title: 'Aggiungi Team',
                    description: 'Clicca qui per aggiungere un nuovo team.',
                    side: "left",
                    align: 'center',
                    allowInteraction: true
                }
            },
            {
                element: '#dropdown-container',
                popover: {
                    title: 'Filtra i Team',
                    description: 'Filtra i tuoi team per nome o data di creazione.',
                    side: "left",
                    align: 'center'
                }
            },
            // Altri passi aggiuntivi se necessario
        ]
    });

    // Mostra la conferma per avviare il tour quando la pagina viene caricata
    window.onload = function() {
        if (confirm('Vuoi partecipare al tour guidato?')) {
            // Avvia il tour se l'utente accetta
            driverObj.drive();
        }
    };

  
});

