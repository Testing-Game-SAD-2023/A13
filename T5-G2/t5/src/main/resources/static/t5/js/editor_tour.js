/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
                element: '#runButton',
                popover: {
                    title: 'Run Button',
                    description: 'Sei curioso di scoprire chi ha vinto la sfida, se tu oppire il robot? Cliccando questo bottone terminerà la partita e verrà decretato il vincitore.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#searchButton',
                popover: {
                    title: 'Search Button',
                    description: 'Cerca una parola o una frase all\'interno del tuo codice.',
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
                element: '#startReplace',
                popover: {
                    title: 'Replace Button',
                    description: 'Sostituisci una parola o una frase all\'interno del tuo codice.',
                    side: "center",
                    align: 'center'  
                }
            },
            { 
                element: '#fileInput',
                popover: {
                    title: 'Open File Button',
                    description: 'Hai già scritto un test e vuoi caricarlo nella schermata di editing? Niente paura, cliccando questo bottone potrai caricare un file di testo.',
                    side: "center",
                    align: 'center' 
                }
            },
            { 
                element: '#DownloadButton',
                popover: {
                    title: 'Download Button',
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
                element: '#themeToggle',
                popover: {
                    title: 'Theme Toggle',
                    description: 'Preferisci il tema chiaro oppure quello scuro? Sta a te decidere!',
                    side: "center",
                    align: 'center' 
                }
            },
        ]

    });

    window.onload = function() {
        if (!localStorage.getItem('tourCompleted') && confirm('Vuoi partecipare al tour guidato?')) {
            // Start the tour if the user accepts
            driverObj.drive();
            localStorage.setItem('tourCompleted', true); // Salva che il tour è stato completato
        }
    };

});