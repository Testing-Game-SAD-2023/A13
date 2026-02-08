document.querySelector('option[value="PartitaSingola"]').textContent = gameMode.PartitaSingola;
document.querySelector('option[value="General"]').textContent = general;

const progressContainer = document.querySelector('.progress_container');
const progressFill = document.querySelector(".progress_fill");
const progress = parseFloat(progressContainer.getAttribute('data-progress'));
progressFill.style.strokeDashoffset = 314 * (1 - progress);

const rankData = [
    {
        name: "Test Initiate",
        image: "/t5/images/ranks/rank1.png",
        description: "Hai scritto i tuoi primi test. Coprono poco, ma sono tuoi. L’AI ti osserva con curiosità."
    },
    {
        name: "Assertion Trainee",
        image: "/t5/images/ranks/rank2.png",
        description: "Inizi a capire cosa verificare davvero. Le asserzioni smettono di essere decorative."
    },
    {
        name: "Coverage Apprentice",
        image: "/t5/images/ranks/rank3.png",
        description: "La copertura non è più un numero a caso. Ogni test aggiunge valore misurabile."
    },
    {
        name: "Logic Examiner",
        image: "/t5/images/ranks/rank4.png",
        description: "Analizzi i flussi come un investigatore. I bug semplici non passano più inosservati."
    },
    {
        name: "Integration Tactician",
        image: "/t5/images/ranks/rank5.png",
        description: "I problemi veri nascono tra i moduli. Tu li affronti prima che l’AI li generi."
    },
    {
        name: "Edge-Case Specialist",
        image: "/t5/images/ranks/rank6.png",
        description: "Cerchi ciò che nessuno prova. Gli input impossibili sono il tuo terreno di caccia."
    },
    {
        name: "Coverage Strategist",
        image: "/t5/images/ranks/rank7.png",
        description: "La coverage è una strategia, non un obiettivo cieco. Ogni riga è una scelta consapevole."
    },
    {
        name: "Automation Challenger",
        image: "/t5/images/ranks/rank8.png",
        description: "I test automatici dell’AI sono veloci. I tuoi sono più intelligenti."
    },
    {
        name: "Reliability Architect",
        image: "/t5/images/ranks/rank9.png",
        description: "Il sistema regge perché tu lo hai stressato prima. L’AI ora gioca in difesa."
    },
    {
        name: "Master of Coverage",
        image: "/t5/images/ranks/rank10.png",
        description: "La copertura è quasi totale. Non stai più inseguendo bug: stai progettando affidabilità."
    }
];

export function updateRankUI() {
    const levelEl = document.querySelector(".progress_value");
    if (!levelEl) return;

    const maxLevel = rankData.length;
    let levelText = levelEl.textContent.trim();
    let level = 1;

    if (levelText.includes("MAX")) {
        level = maxLevel;
    } else if (levelText.includes("Lv.")) {
        level = parseInt(levelText.replace("Lv.", ""), 10) || 1;
    }

    const isMax = level >= maxLevel;
    if (isMax) {
        level = maxLevel;
        levelEl.textContent = "Lv. MAX";
    }

    const rank = rankData[level - 1];

    const rankNameEl = document.getElementById("currentRankName");
    const rankImageEl = document.getElementById("rankImage");
    const bioTextEl = document.getElementById("bio-text");

    if (rankNameEl) {
        rankNameEl.textContent = rank.name;

        rankNameEl.querySelector(".rank-badge-max")?.remove();
        if (isMax) {
            const badge = document.createElement("span");
            badge.className = "rank-badge-max";
            badge.textContent = "MAX";
            rankNameEl.appendChild(badge);
        }
    }

    if (rankImageEl) {
        rankImageEl.src = rank.image;
        rankImageEl.classList.toggle("rank-max", isMax);
    }

    if (bioTextEl) {
        bioTextEl.textContent = rank.description || "...";
    }

    const progressFill = document.getElementById("progressFill");
    if (!progressFill) return;

    const radius = 50;
    const circumference = 2 * Math.PI * radius;

    progressFill.style.strokeDasharray = circumference;

    if (isMax) {

        progressFill.style.strokeDasharray = 0;
        return;
    }


    const expRemainingEl = document.getElementById("expRemaining");
    const expPerLevelEl = document.getElementById("expPerLevel");

    const expRemaining = expRemainingEl
        ? parseFloat(expRemainingEl.textContent) || 0
        : 0;

    const expPerLevel = expPerLevelEl
        ? parseFloat(expPerLevelEl.textContent) || 1000
        : 1000;

    const pct = Math.max(
        0,
        Math.min(1, (expPerLevel - expRemaining) / expPerLevel)
    );

    progressFill.style.strokeDashoffset = circumference - pct * circumference;

}

export function openRankModal() {
    const rankModal = document.getElementById('rankModal');
    rankModal.classList.add("active");
    generateRankList();

}

export function closeRankModal() {
    const rankModal = document.getElementById('rankModal');
    rankModal.classList.remove("active");
}

export function generateRankList() {

    const progressValueEl = document.querySelector(".progress_value");
    let currentLevel = 1;
    if (progressValueEl) {
        const text = progressValueEl.textContent.trim();
        if (text === "Lv. MAX") {
            currentLevel = 10;
        } else {
            currentLevel = parseInt(text.replace("Lv. ", ""), 10);
        }
    }

    const list = document.getElementById("fullRankList");
    if (!list) return;
    list.innerHTML = "";

    rankData.forEach((r, idx) => {
        const li = document.createElement("li");
        li.className = "rank-list-item clickable-hover-sound";

        let icon = "";
        if (idx + 1 < currentLevel) {
            li.classList.add("past");
            icon = `<i class="bi bi-check"></i>`;
        } else if (idx + 1 === currentLevel) {

            li.classList.add("current");
            icon = `<i class="bi bi-map-marker-alt"></i>`;
            setTimeout(() => li.scrollIntoView({ block: "center" }), 100);
        } else {
            li.classList.add("locked");
            icon = `<i class="bi bi-lock"></i>`;
        }

        li.innerHTML = `
            <div class="rank-list-left">
                <img src="${r.image}" alt="${r.name}" class="list-rank-img" onerror="this.style.display='none'">
                <div class="rank-list-info">
                    <span class="rank-list-combined">${r.name}</span>
                </div>
            </div>
            <div class="rank-list-status">${icon}</div>
        `;

        list.appendChild(li);
    });
}

export function groupBy(array, key) {
    return array.reduce((result, item) => {
        (result[item[key]] = result[item[key]] || []).push(item);
        return result;
    }, {});
}

export function renderGeneralAchievements(containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    if (!general_achievements || general_achievements.length === 0) {
        container.innerHTML = `
            <div class="text-center mt-3 fw-bold">
                ${no_achievement_message}
            </div>`;
        return;
    }

    const achievementsContainer = document.createElement("div");
    achievementsContainer.classList.add("achievements-container");

    general_achievements.forEach(ach => {
        const achDiv = document.createElement("div");
        achDiv.classList.add("achievement-item");
        achDiv.innerHTML = `
            <img src="/images/achievements/${ach}.png"
                 alt="${achievementData[ach]?.name || ach}">
            <div class="achievement-info">
                <h6>${achievementData[ach]?.name || ach}</h6>
                <p>${achievementData[ach]?.descr || ""}</p>
            </div>
        `;
        achievementsContainer.appendChild(achDiv);
    });

    container.appendChild(achievementsContainer);
}


export function renderGameModeAchievements(containerId, selectedMode) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    let hasAchievements = false;

    if (!gamemode_achievements || gamemode_achievements.length === 0) {
        container.innerHTML = `
            <div class="text-center mt-3 fw-bold">
                ${no_achievement_message}
            </div>`;
        return;
    }

    const filtered = gamemode_achievements.filter(
        a => a.gameMode === selectedMode
    );

    const groupedByClassUT = groupBy(filtered, "classUT");

    for (let classUT in groupedByClassUT) {
        const classUTDiv = document.createElement("div");
        classUTDiv.innerHTML = `<div class="class-ut">${classUT}</div>`;

        const groupedByRobot = groupBy(groupedByClassUT[classUT], "type");

        for (let robotType in groupedByRobot) {
            const robotDiv = document.createElement("div");
            const groupedByDifficulty = groupBy(groupedByRobot[robotType], "difficulty");

            for (let difficulty in groupedByDifficulty) {
                const difficultyText =
                    difficulty === "EASY" ? difficultyTranslation.easy :
                        difficulty === "MEDIUM" ? difficultyTranslation.medium :
                            difficultyTranslation.hard;

                const difficultyTitle = document.createElement("div");
                difficultyTitle.classList.add("robot-difficulty");
                difficultyTitle.textContent = `${robotType} - ${difficultyText}`;

                const achievementsContainer = document.createElement("div");
                achievementsContainer.classList.add("achievements-container");

                groupedByDifficulty[difficulty].forEach(entry => {
                    entry.achievements?.forEach(ach => {
                        const achDiv = document.createElement("div");
                        achDiv.classList.add("achievement-item");
                        achDiv.innerHTML = `
                            <img src="/images/achievements/${ach}.png"
                                 alt="${achievementData[ach]?.name || ach}">
                            <div class="achievement-info">
                                <h6>${achievementData[ach]?.name || ach}</h6>
                                <p>${achievementData[ach]?.descr || ""}</p>
                            </div>
                        `;
                        achievementsContainer.appendChild(achDiv);
                        hasAchievements = true;
                    });
                });

                if (achievementsContainer.children.length > 0) {
                    robotDiv.appendChild(difficultyTitle);
                    robotDiv.appendChild(achievementsContainer);
                }
            }

            classUTDiv.appendChild(robotDiv);
        }

        if (classUTDiv.children.length > 1) {
            container.appendChild(classUTDiv);
        }
    }

    if (!hasAchievements) {
        container.innerHTML = `
            <div class="text-center mt-3 fw-bold">
                ${no_achievement_message}
            </div>`;
    }
}

export function handleGameModeChange(e) {
    const modalEl = document.getElementById("achievementsModal");
    const modal = new bootstrap.Modal(modalEl);
    const containerId = "achievements-modal-container";
    const title = document.getElementById("achievementsModalTitle");

    if (this.value === "General") {
        title.textContent = "Obiettivi Generici";
        renderGeneralAchievements(containerId);
    } else {
        title.textContent = `Obiettivi – ${this.value}`;
        renderGameModeAchievements(containerId, this.value);
    }

    modal.show();
}

export function handleAchievementsModalHidden() {
    document.activeElement?.blur();
}