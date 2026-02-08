/*
 * profile.js
 * Gestione livello, esperienza, progress bar e rank utente
 */


//costanti utili
let isEditingBio = false;
let allGames = [];
let currentPage = 0;
let socialLoaded = false;
let searchTimeout = null;
const PAGE_SIZE = 3;
window.currentFollowing = [];
window.currentFollowers = [];
window.lastSearchResults = [];
window.lastSearchTerm = "";

document.querySelector('option[value="PartitaSingola"]').textContent = gameMode.PartitaSingola;
document.querySelector('option[value="General"]').textContent = general;

const progressContainer = document.querySelector('.progress_container');
const progressFill = document.querySelector(".progress_fill");
const progress = parseFloat(progressContainer.getAttribute('data-progress'));
progressFill.style.strokeDashoffset = 314 * (1 - progress);

const availableAvatars = [
    "default.png",
    "men-1.png",
    "men-2.png",
    "men-3.png",
    "men-4.png",
    "women-1.png",
    "women-2.png",
    "women-3.png",
    "women-4.png"
];

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

const dom = {
    bioText: document.getElementById("bioText"),
    bioInput: document.getElementById("bioInput"),
    saveProfileBtn: document.getElementById("saveProfileBtn"),
    profileName: document.getElementById("profileName"),
    nicknameInput: document.getElementById("nicknameInput"),
    editNicknameBtn: document.getElementById("editNicknameBtn"),
    profileImage: document.getElementById("profileImage"),
    rankModal: document.getElementById("rankModal"),
};

// PARTE FISSA //

function selectAvatar(filename) {
    const avatarImg = dom.profileImage;

    if (avatarImg) {
        avatarImg.src = `/t5/images/profileImages/${filename}`;
    }

    const hiddenInput = document.getElementById("selectedAvatarInput");
    if (hiddenInput) {
        hiddenInput.value = filename;
    }

}

function openAvatarModal() {
    const modal = document.getElementById("avatarPickerModal");
    const list = document.getElementById("avatarList");

    list.innerHTML = "";

    availableAvatars.forEach(img => {
        const element = document.createElement("img");
        element.src = `/t5/images/profileImages/${img}`;
        element.classList.add("avatar-choice");

        element.addEventListener("click", () => selectAvatar(img));

        list.appendChild(element);
    });

    modal.classList.add("active");

}

function closeAvatarModal() {
    const modal = document.getElementById("avatarPickerModal");
    modal.classList.remove("active");
}

function closeBioEdit(saveChanges) {
    const bioText = dom.bioText;
    const bioInput = dom.bioInput;
    const saveBtn = dom.saveProfileBtn;

    if (saveChanges) {
        bioText.innerText = bioInput.value.trim();
    }

    bioInput.style.display = "none";
    bioText.style.display = "block";

    saveBtn.disabled = false;
    isEditingBio = false;
}

async function saveProfile() {
    if (isEditingBio) {
        alert("Chiudi prima la modifica della bio");
        return;
    }

    const bio = dom.bioText.innerText.trim();
    const selectedAvatarPath = dom.profileImage.src;
    const nickname = dom.nicknameInput.innerText.trim();
    const email = document.getElementById('userEmail').value;

    try {
        const formData = new URLSearchParams();
        formData.append("bio", bio);
        formData.append("avatar", selectedAvatarPath || '');
        formData.append("nickname", nickname);
        formData.append("email", email);

        const response = await fetch(`/profile/save`, {
            method: "POST",
            body: formData
        });

        if (response.ok) {
            alert("Profilo salvato correttamente!");
        } else {
            alert("Errore nel salvataggio del profilo.");
        }
    } catch (err) {
        console.error(err);
        alert("Errore di connessione al server.");
    }
}

function startBioEdit() {
    const bioText = dom.bioText;
    const bioInput = dom.bioInput;
    const saveBtn = dom.saveProfileBtn;

    bioInput.value = bioText.innerText.trim();

    bioText.style.display = "none";
    bioInput.style.display = "block";
    bioInput.focus();

    saveBtn.disabled = true;
    isEditingBio = true;
}

function handleBioInputKeydown(e) {
    if (e.key === "Enter") {
        e.preventDefault(); // evita newline
        closeBioEdit(true);
    }
}

function startNicknameEdit() {
    const profileName = dom.profileName;
    const editNicknameBtn = dom.editNicknameBtn;
    const nicknameInput = dom.nicknameInput;
    const saveProfileBtn = dom.saveProfileBtn;

    profileName.style.display = 'none';
    editNicknameBtn.style.display = 'none';

    nicknameInput.style.display = 'block';
    nicknameInput.focus();

    saveProfileBtn.disabled = true;
}

function handleNicknameInputKeypress(e) {
    if (e.key === 'Enter') {
        e.preventDefault();

        const profileName = dom.profileName;
        const editNicknameBtn = dom.editNicknameBtn;
        const nicknameInput = dom.nicknameInput;
        const saveProfileBtn = dom.saveProfileBtn;

        const newNickname = nicknameInput.value.trim();
        if (newNickname !== '') {
            profileName.textContent = newNickname;

            profileName.style.display = 'block';
            editNicknameBtn.style.display = 'inline-block';
            nicknameInput.style.display = 'none';

            saveProfileBtn.disabled = false;
        }
    }
}

//Funzione per cambiare vista secondaria
function switchMainView(view) {
    document.querySelectorAll(".view-section").forEach(v => {
        v.classList.remove("active");
        v.style.display = "none";
    });

    document.querySelectorAll(".main-tab-btn").forEach(b => {
        b.classList.remove("active");
    });

    document.getElementById(`view-${view}`).classList.add("active");
    document.getElementById(`view-${view}`).style.display = "block";

    event.currentTarget.classList.add("active");

    if (view === "social") {
        loadSocialData();
    }
    else if (view === "matches") {
        fetchGameHistory(userId);
    }
    else if (view === "stats") {
        updateRankUI();
    }
}


//     SOCIAL     //
function loadSocialData() {
    if (socialLoaded) return;

    loadFollowing();
    loadFollowers();

    socialLoaded = true;
}

async function loadFollowing() {
    const response = await fetch("/profile/social/following/" + userId);
    const data = await response.json();

    window.currentFollowing = data;
    renderFollowing(data);
}

async function loadFollowers() {
    const response = await fetch("/profile/social/followers/" + userId);
    const data = await response.json();

    window.currentFollowers = data;
    renderFollowers(data, window.currentFollowing);
}

function renderFollowing(users) {
    const ul = document.getElementById("followingList");
    const count = document.getElementById("followingCount");

    ul.innerHTML = "";
    count.textContent = users ? users.length : 0;

    if (!users || users.length === 0) {
        ul.innerHTML = `<li class="text-muted">Non segui ancora nessuno</li>`;
        return;
    }

    users.forEach(user => {
        const li = document.createElement("li");
        li.className = "social-item d-flex align-items-center justify-content-between";

        li.innerHTML = `
            <div class="d-flex align-items-center gap-3">
                <img src="${user?.profilePicturePath || 't5/images/profileImages/default.png'}"
                     class="social-avatar">

                <div class="social-info">
                    <strong>${user.nickname}</strong><br>
                    <span class="text-muted small">${user.name} ${user.surname}</span>
                </div>
            </div>

            <button class="btn btn-outline-danger btn-sm"
                    onclick="toggleFollow(${user.id},'following')">
                Smetti di seguire
            </button>
        `;

        ul.appendChild(li);
    });
}

function renderFollowers(users, followingUsers = []) {
    const ul = document.getElementById("followersList");
    const count = document.getElementById("followersCount");

    ul.innerHTML = "";
    count.textContent = users ? users.length : 0;

    if (!users || users.length === 0) {
        ul.innerHTML = `<li class="text-muted">Nessun follower</li>`;
        return;
    }

    users.forEach(user => {
        const li = document.createElement("li");
        li.className = "social-item d-flex align-items-center justify-content-between";

        const alreadyFollowing = followingUsers.some(f => f.id === user.id);
        const btnText = alreadyFollowing ? "Già seguito" : "Segui";
        const btnClass = alreadyFollowing ? "btn-secondary" : "btn-primary";
        const btnDisabled = alreadyFollowing ? "disabled" : "";

        li.innerHTML = `
            <div class="d-flex align-items-center gap-3">
                <img src="${user?.profilePicturePath || 't5/images/profileImages/default.png'}"
                     class="social-avatar">

                <div class="social-info">
                    <strong>${user?.nickname || "Utente"}</strong><br>
                    <span class="text-muted small">${user.name} ${user.surname}</span>
                </div>
            </div>

            <button class="btn ${btnClass} btn-sm" ${btnDisabled}
                    onclick="toggleFollow(${user.id},'followers')">
                ${btnText}
            </button>
        `;

        ul.appendChild(li);
    });
}

function showFollowing() {
    resetTabs();
    document.getElementById("followingList").classList.remove("d-none");
    document.getElementById("btnFollowing").classList.add("active");
}

function showFollowers() {
    resetTabs();
    document.getElementById("followersList").classList.remove("d-none");
    document.getElementById("btnFollowers").classList.add("active");
}

function showSearch() {
    resetTabs();
    document.getElementById("searchSection").classList.remove("d-none");
    document.getElementById("btnSearch").classList.add("active");
}

async function loadAllUsers(searchTerm) {
    window.lastSearchTerm = searchTerm;

    const response = await fetch(
        `/profile/social/allUsers?searchTerm=${encodeURIComponent(searchTerm)}`
    );

    const data = await response.json();
    window.lastSearchResults = data;

    renderSearchResults(data);
}

function renderSearchResults(users) {
    const ul = document.getElementById("searchResults");
    const profileId = Number(document.getElementById("userProfileId").value);
    const followingUsers = window.currentFollowing || [];

    ul.innerHTML = "";

    if (!users || users.length === 0) {
        ul.innerHTML = `<li class="text-muted">Nessun utente trovato</li>`;
        return;
    }

    const filteredUsers = users.filter(user => user.id !== profileId);

    if (filteredUsers.length === 0) {
        ul.innerHTML = `<li class="text-muted">Nessun utente trovato</li>`;
        return;
    }

    filteredUsers.forEach(user => {
        const alreadyFollowing = followingUsers.some(f => f.id === user.id);

        const li = document.createElement("li");
        li.className = "social-item d-flex align-items-center justify-content-between";

        li.innerHTML = `
            <div class="d-flex align-items-center gap-3">
                <img src="${user.profilePicturePath || '/t5/images/profileImages/default.png'}"
                     class="social-avatar">
                <div class="social-info">
                    <strong>${user.nickname}</strong><br>
                    <span class="text-muted small">${user.name} ${user.surname}</span>
                </div>
            </div>

            <button class="btn btn-sm ${alreadyFollowing ? 'btn-outline-danger' : 'btn-outline-primary'}"
                    onclick="toggleFollow(${user.id}, 'search')">
                ${alreadyFollowing ? 'Smetti di seguire' : 'Segui'}
            </button>
        `;

        ul.appendChild(li);
    });
}

function resetTabs() {
    document.getElementById("followingList").classList.add("d-none");
    document.getElementById("followersList").classList.add("d-none");
    document.getElementById("searchSection").classList.add("d-none");

    document.getElementById("btnFollowing").classList.remove("active");
    document.getElementById("btnFollowers").classList.remove("active");
    document.getElementById("btnSearch").classList.remove("active");
}

/**
 * targetUserId: id dell'utente su cui si clicca
 */
async function toggleFollow(targetUserId) {
    const profileId = document.getElementById("userProfileId").value;

    try {
        const formData = new URLSearchParams();
        formData.append("profileId", profileId);
        formData.append("targetUserId", targetUserId);

        const response = await fetch('/profile/toggle_follow', {
            method: "POST",
            body: formData
        });

        if (!response.ok) throw new Error("Toggle failed");

        await loadFollowing();
        await loadFollowers();

        if (window.lastSearchResults.length > 0) {
            renderSearchResults(window.lastSearchResults);
        }

    } catch (err) {
        console.error(err);
        alert("Errore durante l'operazione di follow.");
    }
}

function handleUserSearchInput(e) {
    const term = e.target.value.trim();
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        loadAllUsers(term);
    }, 300);
}

//   MATCHES   //
async function fetchGameHistory(playerId) {
    try {
        const response = await fetch(`/profile/game-history/${playerId}`);
        if (!response.ok) throw new Error(`Errore caricamento history: ${response.statusText}`);

        const data = await response.json();
        if (Array.isArray(data)) {
            allGames = data.sort((a, b) => new Date(b.closedAt) - new Date(a.closedAt));
            renderCurrentPage();
        }
    } catch (error) {
        console.error(error);
    }
}

function renderCurrentPage() {
    const container = document.getElementById("matchListArea");
    container.innerHTML = "";

    if (!allGames || allGames.length === 0) {
        container.innerHTML = "<li class='text-muted'>Nessuna partita</li>";
        return;
    }

    const start = currentPage * PAGE_SIZE;
    const end = start + PAGE_SIZE;
    const pageGames = allGames.slice(start, end);

    pageGames.forEach(game => {

        const badge = game.winner
            ? `<span class="badge bg-success">VITTORIA</span>`
            : `<span class="badge bg-danger">SCONFITTA</span>`;

        const achievementsText =
            game.achievements && game.achievements.length > 0
                ? game.achievements
                    .map(ach => achievementData[ach]?.name || ach)
                    .join(", ")
                : "Nessun achievement";

        const li = document.createElement("li");
        li.className = "match-item";
        li.innerHTML = `
    <div class="match-content">
        <strong>${game.type}</strong>

        <div class="match-meta">
            <span><strong>classUT:</strong> ${game.classUT}</span>
            <span>${game.gameMode}</span>
            <span>• ${game.difficulty}</span>
        </div>

        <div class="match-achievements">
            ${achievementsText}
        </div>
        <div class="match-badge">
            ${badge}
        </div>
    </div>
`;

        container.appendChild(li);
    });

    updateButtons();
}

function goToNextMatchesPage() {
    if ((currentPage + 1) * PAGE_SIZE < allGames.length) {
        currentPage++;
        renderCurrentPage();
    }
}

function goToPrevMatchesPage() {
    if (currentPage > 0) {
        currentPage--;
        renderCurrentPage();
    }
}

function updateButtons() {
    document.getElementById("prevMatchesBtn").disabled = currentPage === 0;
    document.getElementById("nextMatchesBtn").disabled =
        (currentPage + 1) * PAGE_SIZE >= allGames.length;
}

//   STATS   //
function updateRankUI() {
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

function openRankModal() {
    const rankModal = dom.rankModal;
    rankModal.classList.add("active");
    generateRankList();

}

function closeRankModal() {
    const rankModal = dom.rankModal;
    rankModal.classList.remove("active");
}

function generateRankList() {

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

function groupBy(array, key) {
    return array.reduce((result, item) => {
        (result[item[key]] = result[item[key]] || []).push(item);
        return result;
    }, {});
}

function renderGeneralAchievements(containerId) {
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


function renderGameModeAchievements(containerId, selectedMode) {
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

function handleGameModeChange(e) {
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

function handleAchievementsModalHidden() {
    document.activeElement?.blur();
}

document.addEventListener("DOMContentLoaded", () => {

    updateRankUI();

    document.getElementById("profileImage").src ||= "/t5/images/profileImages/default.png";

    document.getElementById("rankIconContainer")?.addEventListener("click", openRankModal);

    document.getElementById("rankModal")?.addEventListener("click", closeRankModal);

    document.getElementById("editAvatarBtn")?.addEventListener("click", openAvatarModal);

    document.getElementById("avatarPickerModal")?.addEventListener("click", closeAvatarModal);

    document.querySelector("#avatarPickerModal .avatar-modal-content")?.addEventListener("click", (e) => e.stopPropagation());

    document.getElementById('saveProfileBtn')?.addEventListener('click', saveProfile);

    document.getElementById("editBioBtn")?.addEventListener("click", startBioEdit);

    document.getElementById("bioInput")?.addEventListener("keydown", handleBioInputKeydown);

    document.getElementById('editNicknameBtn')?.addEventListener('click', startNicknameEdit);

    document.getElementById('nicknameInput')?.addEventListener('keypress', handleNicknameInputKeypress);

    document.getElementById('nextMatchesBtn')?.addEventListener('click', goToNextMatchesPage);

    document.getElementById('prevMatchesBtn')?.addEventListener('click', goToPrevMatchesPage);

    document.getElementById("game-mode-select")?.addEventListener("change", handleGameModeChange);

    document.getElementById("achievementsModal")?.addEventListener("hidden.bs.modal", handleAchievementsModalHidden);

    document.getElementById("userSearchInput")?.addEventListener("input", handleUserSearchInput);

});
