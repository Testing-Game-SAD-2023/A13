// Browser -> Nginx (/api...) -> rewrite -> API Gateway -> T9 (/api/profiles/...)
// Quindi dal browser dobbiamo chiamare /api/api/profiles/...
const API_BASE = "/api/api/profiles";

// UI Gateway inoltra /t5 -> T5, quindi usiamo un endpoint alias in T5
const SOCIAL_BASE = "/t5/social";



const avatarFiles = [
  "default.png",
  "men-1.png",
  "men-2.png",
  "men-3.png",
  "men-4.png",
  "women-1.png",
  "women-2.png",
  "women-3.png",
  "women-4.png",
];

function setCssVars(themeColor, accentColor) {
  if (themeColor) document.documentElement.style.setProperty("--profile-bg", themeColor);
  if (accentColor) document.documentElement.style.setProperty("--profile-accent", accentColor);
}

function showSavedOk() {
  const el = document.getElementById("savedOk");
  if (!el) return;
  el.style.display = "block";
  clearTimeout(showSavedOk._t);
  showSavedOk._t = setTimeout(() => (el.style.display = "none"), 1800);
}

function showNicknameError(msg) {
  const el = document.getElementById("nicknameError");
  if (!el) return;
  el.textContent = msg;
  el.style.display = msg ? "block" : "none";
}

function renderVisibilityBadge(isPublic) {
  const badge = document.getElementById("visibilityBadge");
  if (!badge) return;
  if (isPublic) {
    badge.textContent = "Public";
    badge.className = "badge text-bg-success";
  } else {
    badge.textContent = "Private";
    badge.className = "badge text-bg-dark";
  }
}

async function apiFetch(url, options = {}) {
  const resp = await fetch(url, {
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const text = await resp.text();
  let body = null;
  try {
    body = text ? JSON.parse(text) : null;
  } catch {
    body = text;
  }

  if (!resp.ok) {
    const err = new Error("API error");
    err.status = resp.status;
    err.body = body;
    throw err;
  }

  return body;
}

function renderAvatarPicker(currentFile) {
  const container = document.getElementById("avatarPicker");
  if (!container) return;

  container.innerHTML = "";
  avatarFiles.forEach((file) => {
    const img = document.createElement("img");
    img.src = `/t5/images/profileImages/${file}`;
    img.alt = file;
    if (file === currentFile) img.classList.add("selected");

    img.addEventListener("click", async () => {
      try {
        const updated = await apiFetch(`${API_BASE}/me/avatar`, {
          method: "PUT",
          body: JSON.stringify({ profilePicturePath: file }),
        });

        document.querySelectorAll("#avatarPicker img").forEach((i) => i.classList.remove("selected"));
        img.classList.add("selected");

        const avatarImg = document.getElementById("avatarImg");
        if (avatarImg) avatarImg.src = `/t5/images/profileImages/${updated.profilePicturePath || file}`;
        showSavedOk();
      } catch (e) {
        console.error(e);
        alert("Failed to update avatar");
      }
    });

    container.appendChild(img);
  });
}

function normalizeColor(value, fallback) {
  if (typeof value !== "string") return fallback;
  const v = value.trim();
  if (/^#[0-9a-fA-F]{6}$/.test(v)) return v;
  return fallback;
}

function formatLastMatch(value) {
  if (!value) return "-";
  try {
    const dt = new Date(value);
    if (!Number.isNaN(dt.getTime())) {
      return dt.toLocaleDateString(undefined, { year: "numeric", month: "short", day: "2-digit" });
    }
  } catch {
    // ignore
  }
  return String(value).split("T")[0] || "-";
}

async function loadSocialCounts() {
  const followersEl = document.getElementById("followersCount");
  const followingEl = document.getElementById("followingCount");
  if (!followersEl || !followingEl) return;

  try {
    const counts = await apiFetch(`${SOCIAL_BASE}/me/counts`, { method: "GET" });

    const followers = counts?.followersCount ?? counts?.followers ?? 0;
    const following = counts?.followingCount ?? counts?.following ?? 0;

    followersEl.textContent = String(followers);
    followingEl.textContent = String(following);
  } catch (e) {
    console.warn("Failed to load social counts", e);
    followersEl.textContent = "0";
    followingEl.textContent = "0";
  }
}

async function loadProfile() {
  showNicknameError("");

  const profile = await apiFetch(`${API_BASE}/me`);

  document.getElementById("nicknameText").textContent = profile.nickname ?? "";
  document.getElementById("nameText").textContent = `${profile.name ?? ""} ${profile.surname ?? ""}`.trim();
  document.getElementById("nicknameInput").value = profile.nickname ?? "";
  document.getElementById("bioInput").value = profile.bio ?? "";

  const avatarFile = profile.profilePicturePath || "default.png";
  document.getElementById("avatarImg").src = `/t5/images/profileImages/${avatarFile}`;
  renderAvatarPicker(avatarFile);

  const played = profile.matchesPlayed ?? 0;
  const won = profile.matchesWon ?? 0;
  const last = profile.lastMatchAt ?? null;

  const mpEl = document.getElementById("matchesPlayed");
  const mwEl = document.getElementById("matchesWon");
  const lmEl = document.getElementById("lastMatch");
  if (mpEl) mpEl.textContent = String(played);
  if (mwEl) mwEl.textContent = String(won);
  if (lmEl) lmEl.textContent = formatLastMatch(last);

  const theme = profile.themeColor || "#0b1b2b";
  const accent = profile.accentColor || "#198754";
  const pub = profile.publicProfile !== false;

  document.getElementById("themeColor").value = normalizeColor(theme, "#0b1b2b");
  document.getElementById("accentColor").value = normalizeColor(accent, "#198754");

  const publicSwitch = document.getElementById("publicSwitch");
  if (publicSwitch) publicSwitch.checked = pub;

  setCssVars(theme, accent);
  renderVisibilityBadge(pub);

  await loadSocialCounts();
}

function bindHandlers() {
  const refreshBtn = document.getElementById("refreshBtn");
  if (refreshBtn) {
    refreshBtn.addEventListener("click", async () => {
      try {
        await loadProfile();
      } catch (e) {
        console.error(e);
        alert("Failed to refresh profile");
      }
    });
  }

  const saveNicknameBtn = document.getElementById("saveNicknameBtn");
  if (saveNicknameBtn) {
    saveNicknameBtn.addEventListener("click", async () => {
      const nickname = document.getElementById("nicknameInput").value.trim();
      try {
        const updated = await apiFetch(`${API_BASE}/me/nickname`, {
          method: "PUT",
          body: JSON.stringify({ nickname }),
        });

        document.getElementById("nicknameText").textContent = updated.nickname ?? nickname;
        showSavedOk();
        showNicknameError("");
      } catch (e) {
        console.error(e);
        if (e.status === 409) {
          showNicknameError("Nickname già in uso. Scegline un altro.");
        } else {
          showNicknameError("Errore nel salvataggio del nickname.");
        }
      }
    });
  }

  const saveBioBtn = document.getElementById("saveBioBtn");
  if (saveBioBtn) {
    saveBioBtn.addEventListener("click", async () => {
      const bio = document.getElementById("bioInput").value;
      try {
        const updated = await apiFetch(`${API_BASE}/me/bio`, {
          method: "PUT",
          body: JSON.stringify({ bio }),
        });
        document.getElementById("bioInput").value = updated.bio ?? bio;
        showSavedOk();
      } catch (e) {
        console.error(e);
        alert("Failed to save bio");
      }
    });
  }

  const generateBioBtn = document.getElementById("generateBioBtn");
  if (generateBioBtn) {
    generateBioBtn.addEventListener("click", async () => {
      const btn = document.getElementById("generateBioBtn");
      const oldHtml = btn.innerHTML;
      btn.disabled = true;
      btn.innerHTML =
        '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Generating...';

      try {
        const updated = await apiFetch(`${API_BASE}/me/generate-bio`, { method: "POST" });
        document.getElementById("bioInput").value = updated.bio ?? "";
        showSavedOk();
      } catch (e) {
        console.error(e);
        alert("Failed to generate bio");
      } finally {
        btn.disabled = false;
        btn.innerHTML = oldHtml;
      }
    });
  }

  const savePrefsBtn = document.getElementById("savePrefsBtn");
  if (savePrefsBtn) {
    savePrefsBtn.addEventListener("click", async () => {
      const themeColor = document.getElementById("themeColor").value;
      const accentColor = document.getElementById("accentColor").value;
      const publicProfile = document.getElementById("publicSwitch").checked;

      try {
        const updated = await apiFetch(`${API_BASE}/me/preferences`, {
          method: "PUT",
          body: JSON.stringify({ themeColor, accentColor, publicProfile }),
        });

        setCssVars(updated.themeColor || themeColor, updated.accentColor || accentColor);
        renderVisibilityBadge(updated.publicProfile !== false);
        showSavedOk();
      } catch (e) {
        console.error(e);
        alert("Failed to save preferences");
      }
    });
  }

  const themeColorEl = document.getElementById("themeColor");
  const accentColorEl = document.getElementById("accentColor");
  if (themeColorEl) themeColorEl.addEventListener("input", (e) => setCssVars(e.target.value, null));
  if (accentColorEl) accentColorEl.addEventListener("input", (e) => setCssVars(null, e.target.value));
}

document.addEventListener("DOMContentLoaded", async () => {
  bindHandlers();

  try {
    await loadProfile();
  } catch (e) {
    console.error(e);
    if (e.status === 401) {
      alert("Non autenticato. Fai login di nuovo.");
      window.location.href = "/login";
    } else {
      alert("Failed to load profile");
    }
  }
});
