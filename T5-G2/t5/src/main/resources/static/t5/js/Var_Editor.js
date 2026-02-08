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
* utility 
*/

function getParameterByName(name) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
    const results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function GetMode() {
    const mode = getParameterByName("mode");
    if (mode) {
        const trimmed = mode.replace(/[^a-zA-Z0-9\s]/g, " ").trim();
        return trimmed;
    }
    return null;
}

function GetClassName(){
    const ClassName = getParameterByName("ClassUT");
    if (ClassName){
        const trimmed = ClassName.replace(/[^a-zA-Z0-9\s]/g, " ").trim();
        return trimmed;
    }
    return null;
}

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

const mode      = GetMode();
const ClassName = GetClassName();

let timer = null; // oggetto timer, da attivare solo per la modalità PartitaSingola
let timer_remainingTime = 0; // tempo restante per terminare una partita PartitaSingola

document.getElementById("Nome_modalita").textContent = get_mode_text(mode);

