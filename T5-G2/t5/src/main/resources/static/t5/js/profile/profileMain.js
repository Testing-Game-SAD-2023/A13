
import * as social from './social.js';
import * as matches from './matches.js';
import * as stats from './stats.js';
import * as fixed from './profileFixed.js';

document.addEventListener("DOMContentLoaded", () => {

    stats.updateRankUI();

    document.querySelectorAll(".main-tab-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            switchMainView(btn.dataset.view, btn);
        });
    });

    document.getElementById("profileImage").src ||= "/t5/images/profileImages/default.png";

    document.getElementById("editAvatarBtn")?.addEventListener("click", fixed.openAvatarModal);

    document.getElementById("avatarPickerModal")?.addEventListener("click", fixed.closeAvatarModal);

    document.querySelector("#avatarPickerModal .avatar-modal-content")?.addEventListener("click", (e) => e.stopPropagation());

    document.getElementById('saveProfileBtn')?.addEventListener('click', fixed.saveProfile);

    document.getElementById("editBioBtn")?.addEventListener("click", fixed.startBioEdit);

    document.getElementById("bioInput")?.addEventListener("keydown", fixed.handleBioInputKeydown);

    document.getElementById('editNicknameBtn')?.addEventListener('click', fixed.startNicknameEdit);

    document.getElementById('nicknameInput')?.addEventListener('keypress', fixed.handleNicknameInputKeypress);

    document.getElementById('nextMatchesBtn')?.addEventListener('click', matches.goToNextMatchesPage);

    document.getElementById('prevMatchesBtn')?.addEventListener('click', matches.goToPrevMatchesPage);

    document.getElementById("game-mode-select")?.addEventListener("change", stats.handleGameModeChange);

    document.getElementById("achievementsModal")?.addEventListener("hidden.bs.modal", stats.handleAchievementsModalHidden);

    document.getElementById("rankIconContainer")?.addEventListener("click", stats.openRankModal);

    document.getElementById("rankModal")?.addEventListener("click", stats.closeRankModal);

    document.getElementById("userSearchInput")?.addEventListener("input", social.handleUserSearchInput);

    document.getElementById("btnFollowing")?.addEventListener("click", social.showFollowing);

    document.getElementById("btnFollowers")?.addEventListener("click", social.showFollowers);

    document.getElementById("btnSearch")?.addEventListener("click", social.showSearch);

});

function switchMainView(view, btn) {
    document.querySelectorAll(".view-section").forEach(v => {
        v.classList.remove("active");
        v.style.display = "none";
    });

    document.querySelectorAll(".main-tab-btn").forEach(b =>
        b.classList.remove("active")
    );

    const viewEl = document.getElementById(`view-${view}`);
    viewEl.classList.add("active");
    viewEl.style.display = "block";

    btn.classList.add("active");

    if (view === "social") social.loadSocialData();
    else if (view === "matches") matches.fetchGameHistory(userId);
    else if (view === "stats") stats.updateRankUI();
}

