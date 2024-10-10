var driverObj;

document.addEventListener('DOMContentLoaded', (event) => {

    // Tour of features
    const driver = window.driver.js.driver;

    //Call the constructor
    driverObj = driver({

        showProgress: true,
        popoverClass: 'driverjs-theme',

        //Define the steps of the tour
        steps: [
            { 
                element: '#user-button',
                popover: {
                    title: 'Game Info',
                    description: 'Cliccando questo bottone, potrai visualizzare le informazioni relative al gioco, come, ad esempio: il tuo ID ed username, la classe da testare, il robot da sfidare e molto altro ancora.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#theme-button',
                popover: {
                    title: 'Theme Button',
                    description: 'Preferisci il tema chiaro oppure quello scuro? Sta a te decidere!',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#open-button',
                popover: {
                    title: 'Open File Button',
                    description: 'Hai già scritto un test e vuoi caricarlo nella schermata di editing? Niente paura, cliccando questo bottone potrai caricare un file di testo.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#saveAs-button',
                popover: {
                    title: 'Save As Button',
                    description: 'Salva i tuoi tests con un semplice click.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#undo-button',
                popover: {
                    title: 'Undo Button',
                    description: 'Vuoi annullare l\'ultima azione? Clicca qui!',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#redo-button',
                popover: {
                    title: 'Redo Button',
                    description: 'Hai annullato un\'azione per sbaglio? Nessun problema, clicca qui per ripristinarla!',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#find-button',
                popover: {
                    title: 'Find Button',
                    description: 'Cerca una parola o una frase all\'interno del tuo codice.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#replace-btn',
                popover: {
                    title: 'Replace Button',
                    description: 'Sostituisci una parola o una frase all\'interno del tuo codice.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#storico',
                popover: {
                    title: 'Storico Button',
                    description: 'Visualizza la cronologia dei risultati ottenutim ma ricorda: non potrai visualizzare i risultati se non hai ancora eseguito il test!',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#compileButton',
                popover: {
                    title: 'Compile Button',
                    description: 'Hai finito di scrivere il tuo test? Clicca qui per compilare il codice! Durante ciascuna partita, potrai cliccare questo bottone tutte le volte che vorrai compilare nuovo codice.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#coverageButton',
                popover: {
                    title: 'Coverage Button',
                    description: 'Hai compilato il codice? Clicca qui per visualizzare i risultati di copertura. Durante ciascuna partita, potrai cliccare questo bottone quante volte vorrai per controllare i risultati di copertura ottenuti.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#runButton',
                popover: {
                    title: 'Run Button',
                    description: 'Sei curioso di scoprire chi ha vinto la sfida, se tu oppire il robot? Cliccando questo bottone terminerà la partita e verrà decretato il vincitore.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#logout',
                popover: {
                    title: 'Logout Button',
                    description: 'Hai finito di giocare? Clicca qui per tornare alla schermata di login.',
                    side: "center",
                    align: 'center' 
                }
            },

        ]

    });

    window.onload = function() {
        if (confirm('Vuoi partecipare al tour guidato?')) {

        // Start the tour if the user accepts
        driverObj.drive();
        }
    };

});