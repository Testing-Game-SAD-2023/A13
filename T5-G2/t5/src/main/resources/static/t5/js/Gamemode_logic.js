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
 
resetGame = false;
// Funzione per ottenere i parametri dall'URL
function getParameterByName(name) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
    const results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
// Funzione per aggiornare la modalità selezionata
function SetMode(setM) {
    if(!setM){
        sanitizedMode = GetMode();
        const elements = document.querySelectorAll(".selectedMode");
        elements.forEach((element) => {
            element.textContent += " " + get_mode_text(sanitizedMode);
        });
    }else{
        localStorage.setItem("modalita", sanitizedMode);
    }
 
    // Rende invisibile il select del robot e difficoltà se la modalità è allenamento
    const selectRobotElement = document.getElementById("robot_selector");
    const selectclassElement = document.getElementById("class_selector"); //new aggiunta
    const selectdifficultyElemenet = document.getElementById("difficulty_selector");
    const selectScalataElement = document.getElementById("scalate_selector");
   
    if (sanitizedMode === "Allenamento") {
        selectRobotElement.classList.add("d-none");        
        selectdifficultyElemenet.classList.add("d-none");
        selectScalataElement.classList.add("d-none");
    } else if (sanitizedMode === "Sfida") {    
        selectScalataElement.classList.add("d-none");
    } else if (sanitizedMode === "Scalata") {       
        selectclassElement.classList.add("d-none");
    }
 
   
}
function GetMode() {
    const mode = getParameterByName("mode");
    if (mode) {
        const sanitizedMode = mode.replace(/[^a-zA-Z0-9\s]/g, " ");
        return sanitizedMode;
    }
    return null;
}
// Funzione per salvare i valori nel localStorage
function saveToLocalStorage() {
    const selectClassValue = document.getElementById("select_class").value;
    const selectRobotValue = document.getElementById("select_robot").value;
    const selectDifficultyValue = document.getElementById("select_diff").value;

    // Salva i valori nel localStorage
    localStorage.setItem("underTestClassName", selectClassValue);
    localStorage.setItem("robot", selectRobotValue);
    localStorage.setItem("difficulty", selectDifficultyValue);
   
 
    localStorage.setItem("modalita", GetMode());
}
 
 
// Funzione per ottenere i dati della partita precedente solo se l'ID utente coincide
async function fetchPreviousGameData() {
    try {
        const response = await fetch("/StartGame", { method: "GET" });
        const data = await response.json();
 
        // Verifica se i dati per l'utente corrente sono presenti
        if (data[userId]) {
            return data[userId]; // Ritorna i dati solo se l'ID coincide
        } else {
            return null; // Se l'ID non corrisponde, ritorna null
        }
    } catch (error) {
        console.error(
            "Errore durante il recupero dei dati della partita precedente:",
            error
        );
        return null;
    }
}
function redirectToMain() {
    window.location.href = "/main";
}
function toggleVisibility(elementId) {
    var element = document.getElementById(elementId);
    if (element) {
        element.classList.toggle("d-none");
    } else {
        console.error("Elemento non trovato con ID:", elementId);
    }
}
function pulisciLocalStorage(chiave) {
    // Controlla se la chiave esiste nel localStorage
    if (localStorage.getItem(chiave)) {
        // Rimuovi la chiave dal localStorage
        localStorage.removeItem(chiave);
        console.log(`Dati associati a "${chiave}" rimossi dal localStorage.`);
    } else {
        console.log(`Nessun dato trovato per la chiave "${chiave}".`);
    }
}
//pulizia local storage
function flush_localStorage() {
    //Pulisco i dati locali
    pulisciLocalStorage("difficulty");
    pulisciLocalStorage("modalita");
    pulisciLocalStorage("robot");
    pulisciLocalStorage("roundId");
    pulisciLocalStorage("turnId");
    pulisciLocalStorage("gameId");
    pulisciLocalStorage("underTestClassName");
    pulisciLocalStorage("username");
    pulisciLocalStorage("storico");
    pulisciLocalStorage("codeMirrorContent");
 
    //Variabili scalata
    
    pulisciLocalStorage("current_round_scalata");
    pulisciLocalStorage("scalata_name");
    pulisciLocalStorage("SelectedScalata");
    pulisciLocalStorage("total_rounds_of_scalata");
    pulisciLocalStorage("SCALATAID");
 
   
}
// Funzione per eseguire la richiesta AJAX
async function runGameActionElimina(url, formData, isGameEnd) {
    try {
        formData.append("isGameEnd", isGameEnd);
        const response = await ajaxRequest(url, "POST", formData, false, "json");
        return response;
    } catch (error) {
        console.error("Errore nella richiesta AJAX:", error);
        throw error;
    }
}
// Codice da eseguire alla creazione della pagina
document.addEventListener("DOMContentLoaded", async function () {
    const mode = localStorage.getItem("modalita");
    const previousGameData = await fetchPreviousGameData();
    SetMode(false);
 
    if (previousGameData !== null) {
        //esiste già una partita su questo player id
        console.log("modalità continua");
        toggleVisibility("scheda_nuovo");
        toggleVisibility("scheda_continua");
 
       
        document.getElementById("gamemode_classeUT").textContent =
            previousGameData.classeUT;
        document.getElementById("gamemode_robot").textContent =
            previousGameData.type_robot;
        document.getElementById("gamemode_difficulty").textContent =
            previousGameData.difficulty;
        document.getElementById("gamemode_modalita").textContent =
        previousGameData.mode;
 
        // Setto tasto continua
        const link = document.getElementById("Continua");
        link.setAttribute("href", "/editor?ClassUT=" + previousGameData.classeUT);
 
    }else{
        flush_localStorage();
        SetMode(true);
    }
});
 
document.getElementById("new_game").addEventListener("click", function () {
    toggleVisibility("scheda_nuovo");
    toggleVisibility("scheda_continua");
    toggleVisibility("alert_nuova");
    resetGame = true;
});
 
document.getElementById("link_editor").addEventListener("click", async function () {
    const mode = GetMode(); // Ottiene la modalità corrente
    if (resetGame) {
        const formData = new FormData();
        formData.append("playerId", String(parseJwt(getCookie("jwt")).userId));
        formData.append("eliminaGame", true);
        const response = runGameActionElimina("/run", formData, true);
        flush_localStorage();
    }
 
    saveToLocalStorage();
    // Aggiorna il link
   
    if(mode=="Sfida"||mode=="Allenamento"){
       
        const link = document.getElementById("link_editor");
        const selectClassValue = document.getElementById("select_class").value;        
        link.setAttribute("href", "/editor?ClassUT=" + selectClassValue);
 
    }else if(mode=="Scalata"){
 
       
            const link = document.getElementById("link_editor");
            const selectedScalata = document.getElementById("select_scalate").value;
       
            console.log("Scalata SELEZIONATA: ", selectedScalata);
 
            const scalataNameMatch = selectedScalata.match(/scalataName=([^,]+)/);
 
            if (scalataNameMatch && scalataNameMatch[1]) {
                const scalataName = scalataNameMatch[1].trim(); // Ottieni il valore di scalataName
                console.log(scalataName); // Output: test
                localStorage.setItem("scalataName",scalataName);              
            }
           
   
       
            // Estrai la lista di classi dalla stringa selectedScalata
            const selectedClassesMatch = selectedScalata.match(/selectedClasses=\[([^\]]+)\]/);
 
            if (selectedClassesMatch && selectedClassesMatch[1]) {
                const selectedClasses = selectedClassesMatch[1].split(",").map(item => item.trim());
                const firstClass = selectedClasses[0]; // Prendi la prima classe
 
                       
 
               
                localStorage.setItem("ElencoScalate", JSON.stringify(selectedClasses));
                localStorage.setItem("length_scalata", selectedClasses.length);
                localStorage.setItem("currentChallengeIndex", 0);  // Round iniziale
                localStorage.setItem("total_score",0);
 
                // Imposta la prima classe nel localStorage e aggiorna il link
                localStorage.setItem("underTestClassName", firstClass);
                link.setAttribute("href", "/editor?ClassUT=" + firstClass);
                console.log("Prima classe selezionata: ", firstClass);
            } else {
                console.error("Errore nell'estrazione delle classi dalla scalata selezionata.");
            }
       
       
        }  
   
   
});
 
//Tasto submit disabilitato fin quando non ho selezionato dei parametri
// Funzione generica per verificare le select e abilitare/disabilitare il pulsante
function updateButtonState() {
    const submitButton = document.getElementById("link_editor");
    const mode = GetMode(); // Ottiene la modalità corrente
 
    // Elementi selezionati
    const classSelected = !!document.getElementById("select_class").value;
    const robotSelected = !!document.getElementById("select_robot").value;
    const diffSelected = !!document.getElementById("select_diff").value;
    const scalateSelected = !!document.getElementById("select_scalate").value;
 
    // Logica di abilitazione del pulsante in base alla modalità
    let enableButton = false;
    if (mode === "Allenamento") {
        enableButton = classSelected; // Controlla solo Classe
    } else if (mode === "Sfida") {
        enableButton = classSelected && diffSelected && robotSelected; // Controlla Classe, Difficoltà, Robot
    } else if (mode === "Scalata") {
        enableButton = scalateSelected && robotSelected && diffSelected; // Controlla Scalata e Robot
    }
 
    // Aggiorna lo stato del pulsante
    submitButton.classList.toggle("disabled", !enableButton);
}
 
 
// Aggiungi un evento change a ciascun select
document
    .getElementById("select_class")
    .addEventListener("change", updateButtonState);
document
    .getElementById("select_robot")
    .addEventListener("change", updateButtonState);
document
    .getElementById("select_diff")
    .addEventListener("change", updateButtonState);
document
    .getElementById("select_scalate")
    .addEventListener("change", updateButtonState);
    