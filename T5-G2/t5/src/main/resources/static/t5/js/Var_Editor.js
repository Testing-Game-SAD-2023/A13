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

/*
*       IN QUESTO FILE METTO TUTTE LE VARIABILI UTILIZZATE DAL EDITOR 
*/

/*
*       VARIABILI DI CODEMIRROR
*/
var editor_utente = CodeMirror.fromTextArea(
    document.getElementById("Editor_utente"),
    {
        lineNumbers: true, // Mostra i numeri di riga
        mode: "java", // Specifica la modalità del linguaggio (ad es. javascript, html, css)
        theme: "material-darker", // Tema opzionale, puoi rimuoverlo o cambiarlo
        tabSize: 4, // Dimensione del tab
        matchBrackets: true,
        autoCloseBrackets: true, // Chiude automaticamente parentesi e virgolette
        foldGutter: true, // Abilita il gutter per il folding
        lineWrapping: true, // Abilita il wrapping delle righe
    }
);
var editor_robot = CodeMirror.fromTextArea(
    document.getElementById("Editor_Robot"),
    {
        lineNumbers: true, // Mostra i numeri di riga
        mode: "java", // Specifica la modalità del linguaggio (ad es. javascript, html, css)
        theme: "material-darker", // Tema opzionale, puoi rimuoverlo o cambiarlo
        tabSize: 4, // Dimensione del tab
        readOnly: true,
    }
);
var console_utente = CodeMirror.fromTextArea(
    document.getElementById("Console_utente"),
    {
        lineNumbers: false, // Mostra i numeri di riga
        mode: "shell", // Specifica la modalità del linguaggio (ad es. javascript, html, css)
        theme: "material-darker", // Tema opzionale, puoi rimuoverlo o cambiarlo
        tabSize: 4, // Dimensione del tab
        readOnly: true,
        autoCloseBrackets: true, // Chiude automaticamente parentesi e virgolette
    }
);
var console_robot = CodeMirror.fromTextArea(
    document.getElementById("Console_Robot"),
    {
        lineNumbers: false, // Mostra i numeri di riga
        mode: "shell", // Specifica la modalità del linguaggio (ad es. javascript, html, css)
        theme: "material-darker", // Tema opzionale, puoi rimuoverlo o cambiarlo
        tabSize: 4, // Dimensione del tab
        readOnly: true,
        autoCloseBrackets: true, // Chiude automaticamente parentesi e virgolette
    }
);

/*
*   ZONE DEL EDITOR 
*/
const divider_Console       = document.getElementById("divider_Console");
const divider_result        = document.getElementById("divider_result");
const section_editor        = document.getElementById("section_editor");
const section_console       = document.getElementById("section_console");
const section_UT            = document.getElementById("section_UT");
const section_result        = document.getElementById("section_result");
const container_user        = document.getElementById("card_user");
const container_robot       = document.getElementById("card_robot");
const close_console_result  = document.getElementById("close_console_result");
const close_console_utente  = document.getElementById("close_console_utente");

const run_button = document.getElementById("runButton");
const coverage_button = document.getElementById("coverageButton");

/*
*   MESSAGGI E TESTO 
*/

const mode = document.getElementById("Nome_modalita").textContent = get_mode_text(localStorage.getItem("modalita"));
// const mode_sfida             = /*[[${welcomeMessage}]]*/
// const mode_allenamento       = /*[[${welcomeMessage}]]*/
// const mode_scalata           = /*[[${welcomeMessage}]]*/

//  const status_sending:       "Sending Test..."	
//  const status_loading: 	    "Loading Results..." 	
//  const status_compiling:     "Compiling..."  
//  const status_ready: 		"Ready" 		
//  const status_error: 		"Error" 		
//  const status_turn_end:      "Turn Ended" 	
//  const status_game_end:      "Game Ended" 


//  const status_button_coverage gioca 
//  const status_button_run      submit ?? 
//  const var_info_ClasseUT      "ClasseUT: "
//  const var_info_difficulty
//  const var_info_robot 

//  Variabili di gioco 

var turno = 0; // numero di turni giocati fino ad ora
var current_round_scalata = 0; // round corrente
var total_rounds_scalata = 0; // numero totale di rounds
var iGameover = false; // flag per indicare se il giocatore ha vinto o perso
var orderTurno = 0;
var perc_robot = "0"; // percentuale di copertura del robot scelto
var userScore = 0;
var locGiocatore = 0;
var currentDate = new Date();