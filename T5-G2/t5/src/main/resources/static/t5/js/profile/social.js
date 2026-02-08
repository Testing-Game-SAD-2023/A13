let socialLoaded = false;
let searchTimeout = null;
window.currentFollowing = [];
window.currentFollowers = [];
window.lastSearchResults = [];
window.lastSearchTerm = "";

export function loadSocialData() {
    if (socialLoaded) return;

    loadFollowing();
    loadFollowers();

    socialLoaded = true;
}

export async function loadAllUsers(searchTerm) {
    window.lastSearchTerm = searchTerm;
    const response = await fetch(`/profile/social/allUsers?searchTerm=${encodeURIComponent(searchTerm)}`);
    const data = await response.json();
    window.lastSearchResults = data;
    renderSearchResults(data);
}

export async function loadFollowing() {
    const response = await fetch("/profile/social/following/" + userId);
    const data = await response.json();
    window.currentFollowing = data;
    renderFollowing(data);
}

export async function loadFollowers() {
    const response = await fetch("/profile/social/followers/" + userId);
    const data = await response.json();
    window.currentFollowers = data;
    renderFollowers(data, window.currentFollowing);
}

export async function toggleFollow(targetUserId) {
    const profileId = document.getElementById("userProfileId").value;
    const formData = new URLSearchParams();
    formData.append("profileId", profileId);
    formData.append("targetUserId", targetUserId);

    const response = await fetch('/profile/toggle_follow', { method: "POST", body: formData });
    if (!response.ok) throw new Error("Toggle failed");

    await loadFollowing();
    await loadFollowers();

    if (window.lastSearchResults.length > 0) renderSearchResults(window.lastSearchResults);
}

export function renderFollowing(users) {
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
        <button class="btn btn-outline-danger btn-sm follow-btn"
                data-user-id="${user.id}">
            Smetti di seguire
        </button>
    `;

        li.querySelector(".follow-btn")
            .addEventListener("click", () => toggleFollow(user.id));

        ul.appendChild(li);
    });

}

export function renderFollowers(users, followingUsers = []) {
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

        li.innerHTML = `
        <div class="d-flex align-items-center gap-3">
            <img src="${user?.profilePicturePath || '/t5/images/profileImages/default.png'}"
                 class="social-avatar">

            <div class="social-info">
                <strong>${user?.nickname || "Utente"}</strong><br>
                <span class="text-muted small">${user.name} ${user.surname}</span>
            </div>
        </div>

        <button class="btn ${btnClass} btn-sm follow-btn" ${alreadyFollowing ? "disabled" : ""}>
            ${btnText}
        </button>
    `;

        const followBtn = li.querySelector(".follow-btn");
        if (!alreadyFollowing) {
            followBtn.addEventListener("click", () => toggleFollow(user.id));
        }

        ul.appendChild(li);
    });

}

export function renderSearchResults(users) {
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

        <button 
            class="btn btn-sm ${alreadyFollowing ? 'btn-outline-danger' : 'btn-outline-primary'} follow-btn"
            data-user-id="${user.id}"
            data-context="search">
            ${alreadyFollowing ? 'Smetti di seguire' : 'Segui'}
        </button>
    `;

        li.querySelector(".follow-btn")
            .addEventListener("click", () => toggleFollow(user.id));

        ul.appendChild(li);

    });

}

export function showFollowing() {
    resetTabs();
    document.getElementById("followingList").classList.remove("d-none");
    document.getElementById("btnFollowing").classList.add("active");
}

export function showFollowers() {
    resetTabs();
    document.getElementById("followersList").classList.remove("d-none");
    document.getElementById("btnFollowers").classList.add("active");
}

export function showSearch() {
    resetTabs();
    document.getElementById("searchSection").classList.remove("d-none");
    document.getElementById("btnSearch").classList.add("active");
}

export function resetTabs() {
    document.getElementById("followingList").classList.add("d-none");
    document.getElementById("followersList").classList.add("d-none");
    document.getElementById("searchSection").classList.add("d-none");

    document.getElementById("btnFollowing").classList.remove("active");
    document.getElementById("btnFollowers").classList.remove("active");
    document.getElementById("btnSearch").classList.remove("active");
}

export function handleUserSearchInput(e) {
    const term = e.target.value.trim();
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        loadAllUsers(term);
    }, 300);
}