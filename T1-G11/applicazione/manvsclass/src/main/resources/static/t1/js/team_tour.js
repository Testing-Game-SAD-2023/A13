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
                    description: 'Contiene il titolo della pagina e il rimando alla dashboard admin.',
                    side: "bottom",
                    align: 'center'
                }
            },
            {
                element: '.search-container',
                popover: {
                    title: 'Barra di Ricerca',
                    description: 'Usa questa barra per cercare un team specifico e visualizzare i relativi assignment.',
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
            {
                element: '#assignment-section',
                popover: {
                    title: 'Sezione Assignment',
                    description: 'Qui puoi visualizzare e gestire gli assignment relativi ai tuoi team.',
                    side: "top",
                    align: 'center'
                }
            },
            {
                element: '#openModalButtonAssignment',
                popover: {
                    title: 'Aggiungi Assignment',
                    description: 'Clicca qui per aggiungere un nuovo assignment e legarlo ad un team.',
                    side: "left",
                    align: 'center',
                    allowInteraction: true
                },   
            },
            {
                element: '#dropdown-container-assignment',
                popover: {
                    title: 'Filtra gli Assignment',
                    description: 'Filtra i tuoi team per data di scadenza o data di creazione.',
                    side: "left",
                    align: 'center'
                }
            },
            
        ]
    });
    

    document.getElementById("team_tour").addEventListener("click", function() {
        // Logica per il tour o qualsiasi altra funzione che vuoi eseguire
        driverObj.drive();
    });

  
});

