
// Variabili globali che puoi popolare da Thymeleaf in profile.html
let ACH_userExp = 0;
let ACH_expPerLevel = 0;
let ACH_startingLevel = 0;
let ACH_maxLevel = 0;

let ACH_gamemode = [];
let ACH_general = [];

// ------------------------------------------------
// AGGANCIO DEL TAB (Bootstrap shown.bs.tab)
// ------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    const achievementTab = document.getElementById("pills-achievements-tab");
    if (!achievementTab) return;

    achievementTab.addEventListener("shown.bs.tab", async () => {
        const userId = document.body.dataset.userid;

        const response = await fetch(`/Achievement/${userId}`);

        if (response.status === 401) {
            window.location.href = "/login";
            return;
        }

        const data = await response.json();

        mapAchievementData(data);
        initAchievements();
    });
});

// ------------------------------------------------
// INIZIALIZZAZIONE PRINCIPALE
// ------------------------------------------------
function initAchievements() {
    console.log("Achievements inizializzati");

    loadTopSection();
    animateProgressBar();

    document.getElementById("achievement-mode").addEventListener("change", function () {
        if (this.value === "General") renderGeneralAchievements();
        else renderGameModeAchievements(this.value);
    });

    renderGameModeAchievements("PartitaSingola");
}

// ------------------------------------------------
// CARICA TESTI E NUMERI UTENTE
// ------------------------------------------------
function loadTopSection() {
    document.getElementById("user-exp").textContent = ACH_userExp;
    document.getElementById("total-exp-label").textContent = ACH_labels.total_exp;

    document.getElementById("exp-next").textContent =
        ACH_expPerLevel - (ACH_userExp % ACH_expPerLevel);

    document.getElementById("exp-next-label").textContent = ACH_labels.exp_next;

    document.getElementById("achievement-title").textContent = ACH_labels.list;

    document.querySelector('option[value="PartitaSingola"]').textContent = ACH_labels.single;
    document.querySelector('option[value="General"]').textContent = ACH_labels.general;

    const lvl = Math.floor(ACH_userExp / ACH_expPerLevel) + ACH_startingLevel;
    document.getElementById("progress-value").innerText =
        lvl >= ACH_maxLevel ? "Lv. MAX" : "Lv. " + lvl;
}

// ------------------------------------------------
// PROGRESS BAR
// ------------------------------------------------
function animateProgressBar() {
    let progressFill = document.querySelector(".progress_fill");
    let progress = (ACH_userExp % ACH_expPerLevel) / ACH_expPerLevel;
    let offset = 314 * (1 - progress);
    progressFill.style.strokeDashoffset = offset;
}

// ------------------------------------------------
// RENDER GENERAL
// ------------------------------------------------
function renderGeneralAchievements() {
    let container = document.getElementById("achievements-container");
    container.innerHTML = "";

    if (!ACH_general || ACH_general.length === 0) {
        container.innerHTML = `<div class="text-center mt-3 fw-bold">${ACH_labels.no_achievement}</div>`;
        return;
    }

    let achievementsContainer = document.createElement("div");
    achievementsContainer.classList.add("achievements-container");

    ACH_general.forEach(ach => {
        achievementsContainer.appendChild(createAchievementBox(ach));
    });

    container.appendChild(achievementsContainer);
}

// ------------------------------------------------
// RENDER PER MODALITÀ
// ------------------------------------------------
function renderGameModeAchievements(mode) {
    let container = document.getElementById("achievements-container");
    container.innerHTML = "";

    let filtered = ACH_gamemode.filter(a => a.gameMode === mode);
    if (filtered.length === 0) {
        container.innerHTML = `<div class="text-center mt-3 fw-bold">${ACH_labels.no_achievement}</div>`;
        return;
    }

    let groupClass = groupBy(filtered, "classUT");

    for (let classUT in groupClass) {
        let classDiv = document.createElement("div");
        classDiv.innerHTML = `<div class="class-ut">${classUT}</div>`;

        let groupType = groupBy(groupClass[classUT], "type");

        for (let type in groupType) {
            let typeDiv = document.createElement("div");

            let groupDiff = groupBy(groupType[type], "difficulty");

            for (let diff in groupDiff) {
                let diffTitle = document.createElement("div");
                diffTitle.classList.add("robot-difficulty");
                diffTitle.innerText = `${type} - ${ACH_labels.diff[diff.toLowerCase()]}`;

                let achievementsContainer = document.createElement("div");
                achievementsContainer.classList.add("achievements-container");

                groupDiff[diff].forEach(a => {
                    if (a.achievements) {
                        a.achievements.forEach(ach => {
                            achievementsContainer.appendChild(createAchievementBox(ach));
                        });
                    }
                });

                if (achievementsContainer.children.length > 0) {
                    typeDiv.appendChild(diffTitle);
                    typeDiv.appendChild(achievementsContainer);
                }
            }

            classDiv.appendChild(typeDiv);
        }

        container.appendChild(classDiv);
    }
}

// ------------------------------------------------
// UTILITY
// ------------------------------------------------
function groupBy(list, key) {
    return list.reduce((acc, item) => {
        (acc[item[key]] = acc[item[key]] || []).push(item);
        return acc;
    }, {});
}

function createAchievementBox(ach) {
    let div = document.createElement("div");
    div.classList.add("achievement-item");

    let name = ACH_data[ach]?.name || ach;
    let descr = ACH_data[ach]?.descr || "";

    div.innerHTML = `
        <img src="/images/achievements/${ach}.png" alt="${name}">
        <div class="achievement-info">
            <h6>${name}</h6>
            <p>${descr}</p>
        </div>
    `;
    return div;
}

function mapAchievementData(data) {
    ACH_userExp = data.experiencePoints;

    ACH_gamemode = data.gameProgressesDTO || [];
    ACH_general = data.globalAchievements || [];

    ACH_expPerLevel = data.expPerLevel || ACH_expPerLevel;
    ACH_startingLevel = data.startingLevel || ACH_startingLevel;
    ACH_maxLevel = data.maxLevel || ACH_maxLevel;
}

 