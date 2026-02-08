/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   You may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/* UTIL_Editor.js
   Funzioni di utilità per l'editor.
*/

// === TESTI ASCII ===
const you_win = `
__     ______  _    _  __          _______ _   _ 
\\ \\   / / __ \\| |  | | \\ \\        / /_   _| \\ | |
 \\ \\_/ / |  | | |  | |  \\ \\  /\\  / /  | | |  \\| |
  \\   /| |  | | |  | |   \\ \\/  \\/ /   | | | . \` |
   | | | |__| | |__| |    \\  /\\  /   _| |_| |\\  |
   |_|  \\____/ \\____/      \\/  \\/   |_____|_| \\_|
`;

const you_lose = `
__     ______  _    _   _      ____   _____ ______ 
\\ \\   / / __ \\| |  | | | |    / __ \\ / ____|  ____|
 \\ \\_/ / |  | | |  | | | |   | |  | | (___ | |__   
  \\   /| |  | | |  | | | |   | |  | |\\___ \\|  __|  
   | | | |__| | |__| | | |___| |__| |____) | |____ 
   |_|  \\____/ \\____/  |______\\____/|_____/|______|
`;

const error = `
______ _____  _____   ____   _____  
|  ____|  __ \\|  __ \\ / __ \\ / ____| 
| |__  | |__) | |__) | |  | | (___   
|  __| |  _  /|  _  /| |  | |\\___ \\  
| |____| | \\ \\| | \\ \\| |__| |____) | 
|______|_|  \\_\\_|  \\_\\\\____/|_____/  
`;

// === FUNZIONI DI FORMATTAMENTO TESTO ===
function wrapAtSpaces(str, maxLineLength = 62) {
    let result = '';
    let i = 0;

    while (i < str.length) {
        let end = i + maxLineLength;
        if (end >= str.length) {
            result += '    ' + str.slice(i);
            break;
        }

        let spaceIndex = str.lastIndexOf(' ', end);
        if (spaceIndex <= i) {
            spaceIndex = str.indexOf(' ', end);
            if (spaceIndex === -1) {
                result += '    ' + str.slice(i);
                break;
            }
        }

        result += '    ' + str.slice(i, spaceIndex) + '\n';
        i = spaceIndex + 1;
    }

    return result;
}

function roundToTwoDecimals(value) {
    return Math.round(value * 100) / 100;
}

function centerText(text, width) {
    const totalPadding = width - text.length;
    const paddingLeft = Math.floor(totalPadding / 2);
    const paddingRight = totalPadding - paddingLeft;
    return ' '.repeat(paddingLeft) + text + ' '.repeat(paddingRight);
}

function formatJacocoRow(label, user, robot) {
    const userPct = roundToTwoDecimals(user.covered / (user.covered + user.missed) * 100);
    const robotPct = roundToTwoDecimals(robot.covered / (robot.covered + robot.missed) * 100);
    const userTotal = user.covered + user.missed;
    const robotTotal = robot.covered + robot.missed;

    const userStr = `${userPct.toFixed(2)}% (${user.covered}/${userTotal} LOC)`;
    const robotStr = `${robotPct.toFixed(2)}% (${robot.covered}/${robotTotal} LOC)`;

    return `    ${label.padEnd(14)} | ${userStr.padStart(21)} | ${robotStr.padStart(21)}`;
}

function formatEvoRow(label, user, robot) {
    let userPct = 0, robotPct = 0;
    const robotTotal = robot.covered + robot.missed;
    const userTotal = user.covered + user.missed;

    if (robotTotal > 0) {
        userPct = userTotal > 0 ? roundToTwoDecimals((user.covered / userTotal) * 100) : 0;
        robotPct = roundToTwoDecimals((robot.covered / robotTotal) * 100);
    }

    const userStr = `${userPct.toFixed(2)}%`;
    const robotStr = `${robotPct.toFixed(2)}%`;

    return `    ${label.padEnd(14)} | ${userStr.padStart(21)} | ${robotStr.padStart(21)}`;
}

// === FUNZIONI PER GENERARE IL TESTO DELLA CONSOLE ===
function getConsoleTextRun(userCoverageDetails, robotCoverageDetails, canWin, userScore, robotScore, isGameEnd) {
    const userInstructionCov = roundToTwoDecimals(userCoverageDetails.jacoco_instruction.covered / (userCoverageDetails.jacoco_instruction.covered + userCoverageDetails.jacoco_instruction.missed) * 100);
    const robotInstructionCov = roundToTwoDecimals(robotCoverageDetails.jacoco_instruction.covered / (robotCoverageDetails.jacoco_instruction.covered + robotCoverageDetails.jacoco_instruction.missed) * 100);

    let result =
        wrapAtSpaces(terminalMessages.points_descr) + "\n\n\n" +
        '    ' + '═'.repeat(62) + '\n' +
        `    ${terminalMessages.your_instruction_coverage}: ${userInstructionCov}% LOC\n` +
        `    ${terminalMessages.robot_instruction_coverage}: ${robotInstructionCov}% LOC\n` +
        '    ' + '═'.repeat(62) + '\n\n\n' +
        wrapAtSpaces(canWin ? terminalMessages.you_can_win : terminalMessages.you_cant_win) + '\n\n\n\n';

    const header =
        `    ${centerText(terminalMessages.metrics, 14)} |` +
        ` ${centerText(terminalMessages.you, 21)} |` +
        ` ${centerText(terminalMessages.robot, 21)}\n` +
        `    ${'─'.repeat(14)} | ${'─'.repeat(21)} | ${'─'.repeat(21)}\n`;

    // JaCoCo table
    result +=
        '    ' + '─'.repeat(62) + '\n' +
        `    ${centerText(terminalMessages.jacoco_table_title, 62)}\n` +
        '    ' + '─'.repeat(62) + '\n' +
        header +
        formatJacocoRow('Instruction', userCoverageDetails.jacoco_instruction, robotCoverageDetails.jacoco_instruction) + '\n' +
        formatJacocoRow('Line', userCoverageDetails.jacoco_line, robotCoverageDetails.jacoco_line) + '\n' +
        formatJacocoRow('Branch', userCoverageDetails.jacoco_branch, robotCoverageDetails.jacoco_branch) + '\n\n\n\n';

    // EvoSuite table
    if (isGameEnd) {
        result +=
            '    ' + '─'.repeat(62) + '\n' +
            `    ${centerText(terminalMessages.evosuite_table_title, 62)}\n` +
            '    ' + '─'.repeat(62) + '\n' +
            header +
            formatEvoRow('Line', userCoverageDetails.evosuite_line, robotCoverageDetails.evosuite_line) + '\n' +
            formatEvoRow('Branch', userCoverageDetails.evosuite_branch, robotCoverageDetails.evosuite_branch) + '\n' +
            formatEvoRow('Exception', userCoverageDetails.evosuite_exception, robotCoverageDetails.evosuite_exception) + '\n' +
            formatEvoRow('WeakMutation', userCoverageDetails.evosuite_weak_mutation, robotCoverageDetails.evosuite_weak_mutation) + '\n' +
            formatEvoRow('CBranch', userCoverageDetails.evosuite_cbranch, robotCoverageDetails.evosuite_cbranch) + '\n\n';
    }

    return result;
}

function getConsoleTextError() {
    return `===================================================================== \n` +
        error + "\n" +
        `============================== ${terminalMessages.results} =============================== \n` +
        terminalMessages.compilation_error;
}

// === FUNZIONI AJAX ===
async function ajaxRequest_ForRun(url, method = "POST", data = null) {
    try {
        const options = {
            url: url,
            type: method,
            processData: false,
            contentType: "application/json",
            data: JSON.stringify(data)
        };

        const response = await $.ajax(options);
        return response;
    } catch (error) {
        console.error("Si è verificato un errore:", error);
        throw error;
    }
}

// === GESTIONE SCALATA ===
function controlloScalata(iswin, current_round_scalata, total_rounds_scalata, displayRobotPoints) {
    if (iswin) {
        if (current_round_scalata == total_rounds_scalata) {
            calculateFinalScore(localStorage.getItem("scalataId"))
                .then(data => {
                    closeScalata(localStorage.getItem("scalataId"), true, data.finalScore, current_round_scalata)
                        .then(() => {
                            swal(
                                "Complimenti!",
                                `Hai completato la scalata!\n${displayRobotPoints}\nA breve verrai reindirizzato alla classifica.`,
                                "success"
                            ).then(() => window.location.href = "/leaderboardScalata");
                        });
                }).catch(error => {
                    console.error(error);
                    swal("Errore!", "Si è verificato un errore durante il recupero dei dati. Riprovare.", "error");
                });
        } else {
            swal(
                "Complimenti!",
                `Hai completato il round ${current_round_scalata}/${total_rounds_scalata}!\n${displayRobotPoints}`,
                "success"
            ).then(() => {
                current_round_scalata++;
                localStorage.setItem("current_round_scalata", current_round_scalata);
                let classe = getScalataClasse(current_round_scalata - 1, localStorage.getItem("scalata_classes"));
                localStorage.setItem("classe", classe);
                incrementScalataRound(localStorage.getItem("scalataId"), current_round_scalata)
                    .then(() => {
                        createGame("evosuite", classe, 1, localStorage.getItem("scalataId"), localStorage.getItem("username"))
                            .then(() => window.location.href = "/editor");
                    });
            });
        }
    } else {
        closeScalata(localStorage.getItem("scalataId"), false, 0, current_round_scalata)
            .then(() => swal(
                "Peccato!",
                `Hai perso al round ${current_round_scalata}/${total_rounds_scalata} della scalata, la prossima volta andrà meglio!\n${displayRobotPoints}`,
                "error"
            ).then(() => window.location.href = "/main"));
    }
}

// === TIMER ===
window.addEventListener("load", () => {
    if (GetMode() === "PartitaSingola") startTimer();
});

function startTimer() {
    timer = setInterval(() => {
        document.getElementById('timerDisplay').innerHTML = formatTime(timer_remainingTime);
        if (timer_remainingTime <= 0) {
            clearInterval(timer);
            onTimerEnd();
        } else timer_remainingTime--;
    }, 1000);
}

function stopTimer() {
    if (timer !== null) clearInterval(timer);
}

function formatTime(seconds) {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${String(hrs).padStart(2,'0')}:${String(mins).padStart(2,'0')}:${String(secs).padStart(2,'0')}`;
}

function onTimerEnd() {
    isActionInProgress = true;
    run_button.disabled = true;
    coverage_button.disabled = true;
    openModalWithText(
        "Oh no! Il tempo è scaduto!",
        "Vuoi consegnare il codice corrente o mantenere l'ultimo compilato?",
        [
            { tagName: "button", text: "Consegna", class: 'btn btn-primary', data_bs_dismiss: "modal", onclick: () => handleGameAction(true, true) },
            { tagName: "button", text: "Mantieni", class: 'btn btn-primary', data_bs_dismiss: "modal", onclick: () => handleGameAction(true, false) }
        ]
    );
}

// === FUNZIONI UTILITY ===
document.addEventListener('DOMContentLoaded', () => {
    if (previousGameObject && previousGameObject.testingClassCode) editor_utente.setValue(previousGameObject.testingClassCode);
    if (previousGameObject && GetMode() === "PartitaSingola" && previousGameObject.remainingTime)
        timer_remainingTime = previousGameObject.remainingTime;
});

function replaceText(text, replacements) {
    return text.replace(/\b(TestClasse|username|userID|date)\b/g, match => replacements[match] || match);
}

function SetInitialEditor(replacements) {
    const text = editor_utente.getValue();
    const newContent = replaceText(text, replacements);
    editor_utente.setValue(newContent);
}

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
    return mode ? mode.replace(/[^\w]/g, "").trim() : null;
}

// === FUNZIONI MODAL ===
function openModalWithText(title, content, buttons = []) {
    document.getElementById('Modal_title').innerText = title;
    document.getElementById('Modal_body').innerText = content;
    const modalFooter = document.getElementById('Modal_footer');
    modalFooter.innerHTML = '';
    buttons.forEach(button => {
        const btn = document.createElement(button.tagName);
        btn.innerText = button.text;
        btn.className = button.class || 'btn btn-primary';
        if (button.data_bs_dismiss) btn.setAttribute('data-bs-dismiss', button.data_bs_dismiss);
        if (button.onclick) btn.addEventListener("click", button.onclick);
        modalFooter.appendChild(btn);
    });
    new bootstrap.Modal(document.getElementById('Modal')).show();
}

function objectToFormData(obj, formData = new FormData(), parentKey = '') {
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            const fullKey = parentKey ? `${parentKey}[${key}]` : key;
            if (typeof obj[key] === 'object' && obj[key] !== null && !(obj[key] instanceof File)) {
                objectToFormData(obj[key], formData, fullKey);
            } else {
                formData.append(fullKey, obj[key]);
            }
        }
    }
    return formData;
}
