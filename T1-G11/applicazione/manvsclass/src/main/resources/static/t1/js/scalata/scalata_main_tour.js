var driverObj;
var fullTourSteps; // Variabile globale per memorizzare l'array completo dei passi

// Funzione per ottenere il valore di un parametro dall'URL
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// Funzione per avviare il tour
function startTour() {
    // Controllo di sicurezza
    if (!fullTourSteps) {
        console.error("I passi del tour non sono stati definiti.");
        return;
    }

    // Eseguiamo anche un controllo sulla presenza di scalate qui,
    // per non bloccare il tour sui passi condizionali mancanti
    const hasScalate = document.querySelector('.scalate') !== null;
    let finalSteps;

    // Passi originali (dal primo al sesto) - USIAMO LA VARIABILE GLOBALE
    const universalSteps = fullTourSteps.slice(0, 6);

    if (hasScalate) {
        // Se ci sono scalate, usiamo tutti i passi
        finalSteps = fullTourSteps;
    } else {
        // Se non ci sono scalate, usiamo solo i passi universali + un messaggio finale
        finalSteps = universalSteps.concat([
            {
                element: '.container',
                popover: {
                    title: 'Attenzione: Nessuna Scalata',
                    description: 'Al momento non ci sono scalate da visualizzare. Usa il bottone "Crea Scalata" per iniziare a configurare una nuova sfida.',
                    side: "bottom",
                    align: 'center'
                }
            }
        ]);
    }

    // Creiamo e avviamo un nuovo driverObj con i passi corretti
    const conditionalDriver = window.driver.js.driver({
        showProgress: true,
        popoverClass: 'driverjs-theme',
        steps: finalSteps
    });

    conditionalDriver.drive();
}

document.addEventListener('DOMContentLoaded', (event) => {
    // Tour of features
    const driver = window.driver.js.driver;

    // 1. Definiamo i passi completi e li memorizziamo
    fullTourSteps = [
        {
            element: 'body',
            popover: {
                title: 'Panoramica Scalate',
                description: 'Questa è la pagina principale dove puoi visualizzare, gestire e creare tutte le "Scalate" configurate.',
                side: "center",
                align: 'center'
            }
        },
        {
            element: '.navbar',
            popover: {
                title: 'Barra degli strumenti',
                description: 'Qui in alto trovi tutti i controlli rapidi per la navigazione e la gestione delle scalate.',
                side: "bottom",
                align: 'center'
            }
        },
        {
            element: 'a.btn-outline-secondary:has(i.bi-arrow-left)',
            popover: {
                title: 'Torna al Cruscotto',
                description: 'Clicca qui per tornare rapidamente alla dashboard di amministrazione.',
                side: "right",
                align: 'center'
            },
            allowInteraction: true
        },
        {
            element: 'a.btn-outline-success',
            popover: {
                title: 'Crea una Nuova Scalata',
                description: 'Usa questo bottone per accedere alla pagina di configurazione e creare una nuova sfida per i giocatori.',
                side: "bottom",
                align: 'center'
            },
            allowInteraction: true
        },
        {
            element: '.dropdown.me-2',
            popover: {
                title: 'Ordina la lista',
                description: 'Organizza le scalate per nome o data di creazione per facilitare la ricerca.',
                side: "left",
                align: 'end'
            },
            allowInteraction: true
        },
        {
            element: 'form.input-group',
            popover: {
                title: 'Cerca Scalata',
                description: 'Usa la barra di ricerca per trovare rapidamente una scalata specifica tramite il suo nome.',
                side: "left",
                align: 'center'
            },
            allowInteraction: true
        },
        // Passi condizionali (dal 7 in poi)
        {
            element: '.scalate:first-child',
            popover: {
                title: 'Visualizzazione Scalata',
                description: 'Ciascun blocco rappresenta una scalata. La sezione riassuntiva mostra nome, creatore, data e numero di livelli.',
                side: "left",
                align: 'start'
            },
            allowInteraction: false
        },
        {
            element: '.scalate:first-child summary',
            popover: {
                title: 'Dettagli Espandibili',
                description: 'Clicca su questo riquadro per espandere e visualizzare la descrizione e tutti i livelli che compongono la scalata.',
                side: "left",
                align: 'start'
            },
            allowInteraction: true
        },
        {
            element: '.scalate:first-child .delete-button',
            popover: {
                title: 'Elimina Scalata',
                description: 'Se la scalata non è più necessaria, puoi eliminarla permanentemente cliccando questo bottone.',
                side: "left",
                align: 'center'
            },
            allowInteraction: true
        },
        {
            element: '.scalate:first-child .py-1.border:first-child',
            popover: {
                title: 'Dettagli Livello',
                description: 'Questa riga mostra i dettagli di un singolo livello: difficoltà, tipo di avversario, classe da testare e tempo massimo concesso.',
                side: "right",
                align: 'center'
            },
            allowInteraction: false
        }
    ];

    // 2. Inizializziamo driverObj usando la variabile
    driverObj = driver({
        showProgress: true,
        popoverClass: 'driverjs-theme',
        steps: fullTourSteps
    });

    // NUOVA LOGICA: Avvia il tour solo se il parametro 'tour=true' è nell'URL
    const tourRequestedFromDashboard = getUrlParameter('tour') === 'true';

    if (tourRequestedFromDashboard) {
        // Usiamo window.onload per eseguire il confirm dopo che il DOM e i media sono caricati
        window.onload = function() {
            if (confirm('Vuoi partecipare al tour guidato della pagina?')) {
                startTour();
            }
        };
    }

    // Listener sul pulsante per riavviare il tour (bi-info-circle)
    const tourButton = document.querySelector('.navbar-brand .bi-info-circle');

    if (tourButton) {
        tourButton.style.cursor = 'pointer';
        tourButton.addEventListener('click', function(event) {
            event.preventDefault();
            startTour();
        });
    }
});