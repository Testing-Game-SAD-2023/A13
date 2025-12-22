// Browser -> Nginx (/api...) -> rewrite -> API Gateway -> T9 (/api/profiles/...)
// Quindi dal browser dobbiamo chiamare /api/api/profiles/...
const API_BASE = "/api/api/profiles";

// ✅ Friend page è servita da T5...
// Il UI Gateway inoltra verso T5 le rotte /friend/{id} e /follow/{id}.
// /social/** non viene proxata: per questo usiamo /follow/{id} come alias.
const FOLLOW_BASE = "/follow";

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

// evita crash se un elemento non esiste nel DOM
function setText(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = value;
}

async function loadProfile(friendId) {
  const profile = await apiFetch(`${API_BASE}/${friendId}`);

  setText("nicknameText", profile.nickname ?? "");
  setText("nameText", `${profile.name ?? ""} ${profile.surname ?? ""}`.trim());
  setText("bioText", profile.bio ?? "");

  const avatarImg = document.getElementById("avatarImg");
  if (avatarImg) {
    const avatarFile = profile.profilePicturePath || "default.png";
    avatarImg.src = `/t5/images/profileImages/${avatarFile}`;
  }

  // ✅ ID coerenti col template
  setText("matchesPlayed", String(profile.matchesPlayed ?? 0));
  setText("matchesWon", String(profile.matchesWon ?? 0));

  // compat: lastMatch o lastMatchAt
  const last = profile.lastMatch ?? profile.lastMatchAt ?? "-";
  setText("lastMatch", String(last || "-"));

  if (profile.themeColor) document.documentElement.style.setProperty("--profile-bg", profile.themeColor);
  if (profile.accentColor) document.documentElement.style.setProperty("--profile-accent", profile.accentColor);
}

async function loadSocial(friendId) {
  // default safe
  setText("followersCount", "0");
  setText("followingCount", "0");

  const btn = document.getElementById("followBtn");
  if (btn) btn.style.display = "none";

  // GET /follow/{id} -> { followers, following, isFollowing }
  let data = null;
  try {
    data = await apiFetch(`${FOLLOW_BASE}/${encodeURIComponent(friendId)}`);
  } catch (e) {
    console.warn("Failed to load social (follow status/counts)", e);
    return;
  }

  const followers = data?.followers ?? data?.followersCount ?? 0;
  const following = data?.following ?? data?.followingCount ?? 0;
  setText("followersCount", String(followers));
  setText("followingCount", String(following));

  // isFollowing: null => non loggato (niente bottone)
  if (!btn) return;
  if (data?.isFollowing === null || typeof data?.isFollowing === "undefined") {
    btn.style.display = "none";
    return;
  }

  let isFollowing = !!data.isFollowing;

  // setup bottone follow
  btn.style.display = "";
  btn.textContent = isFollowing ? "Non seguire" : "Segui";
  btn.classList.toggle("btn-danger", isFollowing);
  btn.classList.toggle("btn-success", !isFollowing);

  btn.onclick = async () => {
    btn.disabled = true;
    try {
      // POST /follow/{id} (toggle)
      const res = await apiFetch(`${FOLLOW_BASE}/${encodeURIComponent(friendId)}`, {
        method: "POST",
        // body opzionale: mantenuto per compatibilità
        body: JSON.stringify({ targetUserId: Number(friendId) }),
      });

      const followingNow = !!res.following;
      isFollowing = followingNow;

      btn.textContent = followingNow ? "Non seguire" : "Segui";
      btn.classList.toggle("btn-danger", followingNow);
      btn.classList.toggle("btn-success", !followingNow);

      // aggiorna counts: se il backend li ha già ritornati, usali; altrimenti ricarica
      if (typeof res.followers !== "undefined") setText("followersCount", String(res.followers));
      if (typeof res.followingCount !== "undefined") setText("followingCount", String(res.followingCount));
      else {
        try {
          const refreshed = await apiFetch(`${FOLLOW_BASE}/${encodeURIComponent(friendId)}`);
          const f1 = refreshed?.followers ?? refreshed?.followersCount ?? 0;
          const f2 = refreshed?.following ?? refreshed?.followingCount ?? 0;
          setText("followersCount", String(f1));
          setText("followingCount", String(f2));
        } catch (e2) {
          console.warn("Failed to refresh social counts", e2);
        }
      }
    } catch (e) {
      console.error("Toggle follow failed", e);
      if (e.status === 401) {
        alert("Sessione scaduta. Fai login di nuovo.");
        window.location.href = "/login";
      } else {
        alert("Errore nel seguire/non seguire");
      }
    } finally {
      btn.disabled = false;
    }
  };
}

document.addEventListener("DOMContentLoaded", async () => {
  const friendId =
    document.body?.dataset?.friendId ||
    window.__FRIEND_ID__ ||
    new URLSearchParams(location.search).get("id") ||
    (location.pathname.includes("/friend/") ? location.pathname.split("/friend/")[1] : null);

  if (!friendId) return;

  // profilo: se fallisce, ha senso fermarsi
  try {
    await loadProfile(friendId);
  } catch (e) {
    console.error("Friend profile load failed (profile)", e);
    alert("Errore nel caricamento profilo");
    return;
  }

  // social: se fallisce, non deve bloccare la pagina
  try {
    await loadSocial(friendId);
  } catch (e) {
    console.warn("Friend profile load failed (social)", e);
  }
});
