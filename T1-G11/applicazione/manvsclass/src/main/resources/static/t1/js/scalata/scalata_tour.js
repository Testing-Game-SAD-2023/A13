var driverObj;

document.addEventListener('DOMContentLoaded', (event) => {
    //Tour of features
    const driver = window.driver.js.driver;

    //Call the constructor
    // const driverObj = driver({
    driverObj = driver({

        showProgress: true,
        popoverClass: 'driverjs-theme',

        //Define the steps of the tour
        steps: [
            { 
                element: '#my-jumbotron',
                popover: {
                    title: 'Modalità "Scalata"',
                    description: 'In questa nuova e stimolante modalità di gioco avrai la possibilità di essere tu a decidere con quante e quali classi i giocatori si dovranno cimentare nella scrittura di test in JUnit e ricorda: solamente il giocatore capace di superare tutte le sfida potrà essere decretato come vincitore. ',
                    side: "center",
                    align: 'center' 
                }
            },
            {

                element: '#navbar',
                popover: {
                    title: 'Barra di navigazione',
                    description: 'È la prima volta su questa pagina? Non preoccuparti, qui potrai trovare tutte le informazioni di cui hai bisogno per iniziare a configurare la tua scalata!',
                    side: "bottom",
                    align: 'center' 
                }
            },
            {
                element: '#navbar-button',
                popover: {
                    title: 'Cliccami!',
                    description: 'Con questo bottone potrai visualizzare un utile menù a tendina, vieni, ti faccio vedere.',
                    side: "bottom",
                    align: 'start' 
                },
                allowInteraction: true
            },
            {
                element: '#nav-home',
                popover: {
                    title: 'Hai configurato tutto?',
                    description: 'Cone questo bottone potrai comodamente ritornare al cruscotto degli amministratori, provami!',
                    side: "left",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#nav-info',
                popover: {
                    title: 'Ti senti sperduto?',
                    description: 'Le informazioni non sono mai troppe, con questo bottone potrai visualizzare un breve riepilogo per poter iniziare a configurare!',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#nav-add',
                popover: {
                    title: 'Se ne possono aggiungere di più?',
                    description: 'Pensi di voler aggiungere qualche nuova classe e ma non hai voglia di ritornare alla homepage? Questo è il posto giusto per farlo!',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#nav-tour',
                popover: {
                    title: 'Tutto chiaro?',
                    description: 'Torna qui tutte le volte che avrai bisogno di ripetere il tour.',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#form-name',
                popover: {
                    title: 'Dai un nome alla tua "Scalata"',
                    description: 'Assegnale un nome esplicativo, in maniera tale che gli sfidanti sappiano contro cosa dovranno gareggiare!',
                    side: "left",
                    align: 'start' 
                },
                allowInteraction: false
            },
            {
                element: '#FormControlTextareaDescription',
                popover: {
                    title: 'Te la senti di fornire una descrizione?',
                    description: 'Se vuoi fornire ulteriori informazioni riguardo la tua "Scalata", questo è il posto giusto per farlo, sii sintetico e non rivelare informazioni preziose.',
                    side: "left",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#rounds',
                popover: {
                    title: 'Di quanti round sarà formata la tua "Scalata"? (1)',
                    description: 'Ricorda che una partita in questa modalità sarà formata da un certo numero di rounds, ciascuno corrispondente ad una particolare classe da testare.',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#addRoundButton',
                popover: {
                    title: 'Di quanti round sarà formata la tua "Scalata"? (2)',
                    description: '...e non dimenticare di cliccare questo bottone una volta aver definito il numero di rounds desiderati.',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#difficultyFilter',
                popover: {
                    title: 'Non trovi quello che stai cercando?',
                    description: 'Non so se hai notato, ma ciascuna classe è contraddistina da un nome univoco, eventualmente una descrizione ed una difficoltà: "Beginner", "Intermediate" ed infine "Advanced"; grazie al filtro potrai cercare la classe che fa al caso tuo!',
                    side: "left",
                    align: 'start' 
                },
                allowInteraction: false
            },
            {
                element: '#summarymButton',
                popover: {
                    title: 'Ti serve un riepilogo?',
                    description: 'Cliccando questo bottone potrai avere una panoramica di come è stata configurata la "Scalata".',
                    side: "right",
                    align: 'center' 
                },
                allowInteraction: false
            },
            {
                element: '#confirmButton',
                popover: {
                    title: 'Siamo proprio giunti alla fine',
                    description: 'Controlla di aver compilato tutti i campi e di aver selezionato correttamente l\'ordine ed il numero delle tue classi.',
                    side: "left",
                    align: 'center' 
                },
                allowInteraction: false
            }
        ]
    });

    // driverObj.drive();
    // Show a confirmation message to the user when the page loads
    window.onload = function() {
        if (confirm('Vuoi partecipare al tour guidato?')) {

        // Start the tour if the user accepts
        driverObj.drive();
    }
  };

document.getElementById('nav-tour').addEventListener('click', function(event) {
event.preventDefault(); 
startTour();
});


});
//Repeat the tour
function startTour() {
driverObj.drive();
}

