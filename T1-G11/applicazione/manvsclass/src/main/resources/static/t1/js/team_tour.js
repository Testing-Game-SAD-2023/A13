
 
 document.addEventListener('DOMContentLoaded', () => {
    var driverObj;
    console.log(window.driver);  // Questo dovrebbe restituire una funzione se driver.js è caricato correttamente.

    // Configura Driver.js
    driverObj = driver({
        showProgress: true,
        popoverClass: 'driverjs-theme',
        steps: [
            // Sezione Team
            {
                element: '.header',
                popover: {
                    title: 'Header',
                    description: 'Contiene il logo e il titolo della pagina.',
                    side: 'bottom',
                    align: 'center',
                },
            },
            {
                element: '#search-container',
                popover: {
                    title: 'Barra di Ricerca',
                    description: 'Usa questa barra per cercare un team specifico.',
                    side: 'bottom',
                    align: 'center',
                },
            },
            {
                element: '#team-section',
                popover: {
                    title: 'Sezione Team',
                    description: 'Qui puoi visualizzare e gestire i tuoi team.',
                    side: 'top',
                    align: 'center',
                },
            },
            {
                element: '#openModalButtonTeam',
                popover: {
                    title: 'Aggiungi Team',
                    description: 'Clicca qui per aggiungere un nuovo team.',
                    side: 'left',
                    align: 'center',
                    allowInteraction: true,
                },
            },
            {
                element: '#dropdown-container',
                popover: {
                    title: 'Filtra i Team',
                    description: 'Filtra i tuoi team per nome o data di creazione.',
                    side: 'left',
                    align: 'center',
                },
            },

            // Sezione Assignment
            {
                element: '#assignment-section',
                popover: {
                    title: 'Sezione Assignment',
                    description: 'In questa sezione puoi gestire i tuoi assignment.',
                    side: 'top',
                    align: 'center',
                },
            },
            {
                element: '#openModalButtonAssignment',
                popover: {
                    title: 'Aggiungi Assignment',
                    description: 'Clicca qui per aggiungere un nuovo assignment.',
                    side: 'left',
                    align: 'center',
                    allowInteraction: true,
                },
            },
            {
                element: '#dropdown-container-assignment',
                popover: {
                    title: 'Filtra gli Assignment',
                    description: 'Filtra i tuoi assignment per data di creazione.',
                    side: 'left',
                    align: 'center',
                },
            },
        ],
    });

    // Mostra il modale al caricamento della pagina
    const tourModal = document.getElementById('tourModal');
    const startTourModal = document.getElementById('startTourModal');
    const closeModal = document.getElementById('closeModal');

    tourModal.style.display = 'flex';

    startTourModal.addEventListener('click', () => {
        tourModal.style.display = 'none';
        driverObj.drive();
    });

    closeModal.addEventListener('click', () => {
        tourModal.style.display = 'none';
    });

    // Pulsante per riavviare il tour
    const startTourButton = document.getElementById('startTourModal');
    startTourButton.addEventListener('click', () => {
        driverObj.drive();
    });
});
