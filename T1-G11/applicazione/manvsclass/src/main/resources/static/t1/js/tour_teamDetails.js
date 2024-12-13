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
                    description: 'Contiene il titolo della pagina e il rimando alla pagina di gestione dei team.',
                    side: "bottom",
                    align: 'center'
                }
            },
            {
                element: '.title-container',
                popover: {
                    title: 'Titolo e Modifica',
                    description: 'Qui puoi vedere il titolo della pagina e modificarlo cliccando sulla matita, inserendo un nuovo nome per il tuo Team.',
                    side: "bottom",
                    align: 'center'
                }
            },
          
            {
                element: '#studentsContainer',
                popover: {
                    title: 'Tabella Studenti',
                    description: 'Questa tabella mostra gli studenti con i loro dettagli, Passando su ogni studente comparirà il button Elimina per rimuoverlo dal Team!.',
                    side: "top",
                    align: 'center'
                }
            },
            {
                element: '#openModalButton',
                popover: {
                    title: 'Aggiungi Studente',
                    description: 'Clicca qui per aggiungere un nuovo studente, come avviene nella pagina precedente!.',
                    side: "left",
                    align: 'center',
                    allowInteraction: true
                }
            },

            {
                element: '.switch',
                popover: {
                    title: 'Toggle Switch',
                    description: 'Usa questo switch per passare tra "Assignments" e "Student". Forza premilo così vediamo gli Assignments!',
                    side: "top",
                    align: 'center',
                    allowInteraction: true
                }
            },
            {
                element: '#assignmentsContainer',
                popover: {
                    title: 'Tabella Assignment',
                    description: 'Questa sezione mostra gli assignment, con la possibilità di filtrarli, inoltre cliccando su ogni assignment vedrai la sua descrizione!.',
                    side: "top",
                    align: 'center'
                }
            },
            {
                element: '#dropdown-container-assignment',
                popover: {
                    title: 'Filtra gli Assignment',
                    description: 'Filtra i tuoi team per data di scadenza (mostrandoti i primi a scadere!) o data di creazione (mostrandoti gli ultimi creati!).',
                    side: "left",
                    align: 'center'
                }
            },
           
            {
                element: '#openModalButtonAssignment',
                popover: {
                    title: 'Aggiungi Assignment',
                    description: 'Clicca qui per aggiungere un nuovo assignment, come avviene nella pagina precedente!.',
                    side: "left",
                    align: 'center',
                    allowInteraction: true
                }   
            },
            
        ]
    });
    
    // Aggiungi un evento di click per avviare il tour
    document.getElementById("team_tour").addEventListener("click", function() {
        // Logica per avviare il tour
        driverObj.drive();
    });
});
