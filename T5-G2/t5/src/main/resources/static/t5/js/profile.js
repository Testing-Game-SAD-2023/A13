/*
 *   Copyright (c) 2025 Stefano Marano
 *   All rights reserved.
 *   Licensed under the Apache License, Version 2.0
 */

document.addEventListener("DOMContentLoaded", function () {
    console.log("profile.js loaded");

    // ==========================
    //  EDIT PROFILE (/edit_profile)
    // ==========================
    const profilePictures = document.querySelectorAll(".profile-picture");
    const bioInput = document.getElementById("bio-input");
    const saveButton = document.getElementById("save-button");
    const userData = document.getElementById("player-data");

    if (userData && saveButton && bioInput) {
        const userEmail = userData.dataset.email;
        const currentBio = userData.dataset.currentBio;
        const currentImage = userData.dataset.currentImage;
        let selectedImage = currentImage;

        profilePictures.forEach((img) => {
            img.addEventListener("click", function () {
                profilePictures.forEach((i) => i.classList.remove("selected"));
                this.classList.add("selected");
                selectedImage = this.getAttribute("src")
                    .split("/")
                    .pop()
                    .replace(/-\w{32}\.png$/, ".png");
            });
        });

        saveButton.addEventListener("click", async function () {
            const newBio = bioInput.value.trim();
            const bioToSend = newBio || currentBio;
            const imageToSend = selectedImage || currentImage;

            try {
                const formData = new URLSearchParams();
                formData.append("email", userEmail);
                formData.append("bio", bioToSend);
                formData.append("profilePicturePath", imageToSend);

                const response = await fetch("/api/gameEngine/update_profile", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: formData.toString(),
                });

                if (response.ok) {
                    alert("Profilo aggiornato!");
                    setTimeout(() => (window.location.href = "/profile"), 100);
                } else {
                    const errorMessage = await response.text();
                    alert("Errore: " + errorMessage);
                }
            } catch (error) {
                console.error("Errore:", error);
                alert("Errore di rete.");
            }
        });
    }
// ==========================
//  FOLLOWERS / FOLLOWING + SEARCH
// ==========================
const profileIdSpan = document.getElementById("userProfileID");
const profileId = profileIdSpan ? profileIdSpan.textContent.trim() : null;

const followersBlock = document.getElementById("followers_block");
const followingBlock = document.getElementById("following_block");
const followersCountSpan = document.getElementById("followersCount");
const followingCountSpan = document.getElementById("followingCount");

const messagesBlock = document.getElementById("messages_block");
const messagesTab = document.getElementById("optionMessages");

const followersRadio = document.getElementById("option_Followers");
const followingRadio = document.getElementById("option_Following");
const searchInput = document.getElementById("text-srh_social");

const ownProfileFlag = document.getElementById("ownProfileFlag");
const isOwnProfile = ownProfileFlag && ownProfileFlag.textContent.trim() === "true";

if (profileId && followersCountSpan && followingCountSpan) {

    async function fetchAndUpdateUserBlock(endpoint, block, countSpan) {

        // Se NON è il tuo profilo, non mostrare la parte dinamica sotto
        if (!isOwnProfile || !block) {
            try {
                const res = await fetch(`${endpoint}?userId=${profileId}`);
                if (!res.ok) throw new Error(await res.text());
                const users = await res.json();

                const count = Array.isArray(users) ? users.length : 0;
                if (countSpan) countSpan.textContent = count;
            } catch (e) {
                console.error("Errore followers/following:", e);
                if (countSpan) countSpan.textContent = "0";
            }
            return;
        }

        // --- QUI sotto è il comportamento "vecchio" SOLO per il tuo profilo ---
        block.innerHTML = `
            <div class="text-center my-3">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Caricamento...</span>
                </div>
            </div>
        `;
        try {
            const res = await fetch(`${endpoint}?userId=${profileId}`);
            if (!res.ok) throw new Error(await res.text());
            const users = await res.json();

            console.log("Risposta", endpoint, users);

            block.innerHTML = "";
            const count = Array.isArray(users) ? users.length : 0;
            if (countSpan) countSpan.textContent = count;

            if (!Array.isArray(users) || users.length === 0) {
                block.innerHTML = `<p class="text-muted my-3">Nessun risultato trovato.</p>`;
                return;
            }

            users.forEach((u) => {
                const col = document.createElement("div");
                col.className = "col mb-4";
                col.innerHTML = `
                    <div class="card">
                        <div class="card-body p-4 d-flex align-items-center gap-3">
                            <img src="/t5/images/profileImages/${u.profilePicturePath}"
                                 alt="twbs" width="32" height="32"
                                 class="rounded-circle flex-shrink-0">
                            <div class="text-start">
                                <h5 class="fw-semibold mb-0">${u.name} ${u.surname}</h5>
                                <span class="fs-5 d-flex align-items-center">${u.email}</span>
                            </div>
                            <a class="btn btn-primary py-1 px-2 ms-auto" href="/friend/${u.userId}">
                              Vai al profilo
                            </a>
                        </div>
                    </div>
                `;
                block.appendChild(col);
            });
        } catch (e) {
            console.error("Errore followers/following:", e);
            block.innerHTML = `<p class="text-danger my-3">Errore nel caricamento.</p>`;
        }
    }

    // carica subito dati
    fetchAndUpdateUserBlock("/followers", followersBlock, followersCountSpan);
    fetchAndUpdateUserBlock("/following", followingBlock, followingCountSpan);

    // Se è il TUO profilo, mantieni toggle e ricerca
    if (isOwnProfile && followersRadio && followingRadio) {
        function updateVisibility() {
            if (followersRadio.checked) {
                followersBlock.style.display = "flex";
                followingBlock.style.display = "none";
                if (messagesBlock) messagesBlock.style.display = "none";
            } else if (followingRadio.checked) {
                followersBlock.style.display = "none";
                followingBlock.style.display = "flex";
                if (messagesBlock) messagesBlock.style.display = "none";
            }
        }
        followersRadio.addEventListener("change", updateVisibility);
        followingRadio.addEventListener("change", updateVisibility);
        updateVisibility();
    }

    if (isOwnProfile && searchInput) {
        searchInput.addEventListener("keyup", function () {
            const searchText = this.value.toLowerCase();
            const activeBlock =
                followersRadio && followersRadio.checked ? followersBlock : followingBlock;

            activeBlock.querySelectorAll(".col.mb-4").forEach((card) => {
                const name = card.querySelector("h5")?.textContent.toLowerCase() || "";
                const email = card.querySelector("span")?.textContent.toLowerCase() || "";
                card.style.display =
                    name.includes(searchText) || email.includes(searchText) ? "" : "none";
            });
        });
    }
}

// ==========================
//  MESSAGGI (Inbox / Outbox)
// ==========================

//const messagesBlock = document.getElementById("messages_block");
//const messagesTab = document.getElementById("optionMessages");

if (messagesBlock && messagesTab) {
    async function loadMessages(mode) {
        const endpoint = mode === "outbox" ? "/messages/outbox" : "/messages/inbox";

        // spinner
        messagesBlock.innerHTML = `<div class="text-center my-3">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Caricamento...</span>
            </div>
        </div>`;

        try {
            const res = await fetch(endpoint);
            if (!res.ok) throw new Error(await res.text());
            const list = await res.json();

            // header con Inbox/Outbox + New chat
            messagesBlock.innerHTML = `
                <div class="d-flex justify-content-between mb-3">
                    <div class="btn-group" role="group">
                        <button type="button"
                                class="btn btn-outline-light"
                                id="btnInbox">
                            Inbox
                        </button>
                        <button type="button"
                                class="btn btn-outline-light"
                                id="btnOutbox">
                            Outbox
                        </button>
                    </div>
                    <button type="button"
                            class="btn btn-outline-primary"
                            id="openNewChatBtn"
                            data-bs-toggle="modal"
                            data-bs-target="#newChatModal">
                        <i class="bi bi-chat-dots"></i> New chat
                    </button>
                </div>
            `;

            if (!Array.isArray(list) || list.length === 0) {
                const empty = document.createElement("div");
                empty.className = "text-muted my-3";
                empty.innerHTML = `<i class="bi bi-inbox me-2"></i>No messages found`;
                messagesBlock.appendChild(empty);
            } else {
                list.forEach((m) => {
                    const col = document.createElement("div");
                    col.className = "col mb-3";
                    col.innerHTML = `
                        <div class="card h-100 p-3 text-start shadow-sm">
                            <div class="d-flex justify-content-between">
                                <h5 class="fw-bold mb-1">
                                    ${mode === "outbox"
                                        ? `To: ${m.receiverEmail ?? "sconosciuto"}`
                                        : `Mittente: ${m.senderEmail ?? "sconosciuto"}`}
                                </h5>
                                <button type="button"
                                        class="btn btn-sm btn-outline-danger delete-msg-btn"
                                        data-id="${m.id}">
                                    Delete
                                </button>
                            </div>
                            <p class="mb-2">${m.content ?? ""}</p>
                            <small class="text-muted">
                                ${m.timestamp
                                    ? new Date(m.timestamp).toLocaleString("it-IT")
                                    : "Ora sconosciuta"}
                            </small>
                        </div>`;
                    messagesBlock.appendChild(col);
                });
            }

            // Delete → /messages/delete?id=...
            messagesBlock.querySelectorAll(".delete-msg-btn").forEach((btn) => {
                btn.addEventListener("click", async () => {
                    const id = btn.dataset.id;
                    if (!confirm("Vuoi cancellare questo messaggio?")) return;

                    try {
                        const params = new URLSearchParams({ id });
                        const resDel = await fetch(`/messages/delete?${params.toString()}`, {
                            method: "POST",
                        });

                        if (resDel.ok || resDel.status === 204) {
                            loadMessages(mode); // ricarica inbox o outbox corrente
                        } else {
                            alert("Errore cancellazione messaggio");
                        }
                    } catch (e) {
                        console.error(e);
                        alert("Errore di rete durante la cancellazione");
                    }
                });
            });

            // toggle bottoni Inbox/Outbox
            const btnInbox = document.getElementById("btnInbox");
            const btnOutbox = document.getElementById("btnOutbox");
            btnInbox.classList.toggle("active", mode === "inbox");
            btnOutbox.classList.toggle("active", mode === "outbox");

            btnInbox.onclick = () => loadMessages("inbox");
            btnOutbox.onclick = () => loadMessages("outbox");
        } catch (e) {
            console.error("Messages error:", e);
            messagesBlock.innerHTML = `
                <p class="text-danger my-3">Errore caricamento messaggi.</p>`;
        }
    }

    messagesTab.addEventListener("change", function () {
        if (this.checked) {
            const fb = document.getElementById("followers_block");
            const fg = document.getElementById("following_block");
            if (fb) fb.style.display = "none";
            if (fg) fg.style.display = "none";

            messagesBlock.style.display = "block";
            loadMessages("inbox"); // default
        }
    });
}

// ==========================
//  NUOVA CHAT
// ==========================
    const newChatModal = document.getElementById("newChatModal");
    const chatSource = document.getElementById("chatSource");
    const chatUserSelect = document.getElementById("chatUserSelect");
    const chatMessage = document.getElementById("chatMessage");
    const chatError = document.getElementById("chatError");
    const chatSuccess = document.getElementById("chatSuccess");
    const sendChatBtn = document.getElementById("sendChatBtn");
    const viewId = profileId;   // il mittente è lo stesso del profilo



    if (newChatModal && chatUserSelect && profileId) {
        async function loadUsers(source) {
            console.log("loadUsers chiamato con:", source);
            chatUserSelect.innerHTML = "<option value=''>Caricamento...</option>";
            try {
                const res = await fetch(`/${source}?userId=${profileId}`);
                console.log("HTTP", res.status, res.url);
                if (!res.ok) throw new Error("Errore caricamento " + res.status);
                const users = await res.json();

                chatUserSelect.innerHTML = "<option value=''>Seleziona utente...</option>";
                if (!Array.isArray(users) || users.length === 0) {
                    chatUserSelect.innerHTML = "<option value=''>Nessun utente</option>";
                    return;
                }
                users.forEach((u) => {
                    const opt = document.createElement("option");
                    opt.value = u.userId;
                    opt.textContent = `${u.name} ${u.surname} (${u.email})`;
                    chatUserSelect.appendChild(opt);
                });
            } catch (e) {
                console.error("ERRORE loadUsers:", e);
                chatUserSelect.innerHTML = "<option value=''>Errore caricamento</option>";
            }
        }

        newChatModal.addEventListener("shown.bs.modal", () => {
            chatMessage.value = "";
            chatError?.classList.add("d-none");
            chatSuccess?.classList.add("d-none");
            loadUsers(chatSource.value);
        });

        chatSource?.addEventListener("change", () => loadUsers(chatSource.value));

        sendChatBtn?.addEventListener("click", async () => {
            const receiverId = chatUserSelect.value;
            const content = chatMessage.value.trim();

            if (!receiverId || !content) {
                chatError.textContent = "Seleziona utente e messaggio.";
                chatError.classList.remove("d-none");
                return;
            }
            if (!viewId) {
                chatError.textContent = "Errore: viewId mancante.";
                chatError.classList.remove("d-none");
                return;
            }

            sendChatBtn.disabled = true;
            try {
                const res = await fetch("/messages/new", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ senderId: viewId, receiverId, content }),
                });
                if (res.ok) {
                    chatSuccess.classList.remove("d-none");
                    chatMessage.value = "";
                } else throw new Error("Errore invio");
            } catch (e) {
                console.error(e);
                chatError.textContent = "Errore nell'invio del messaggio: messaggio troppo lungo.";
                chatError.classList.remove("d-none");
            } finally {
                sendChatBtn.disabled = false;
            }
        });
    }

/// ==========================
//  FOLLOW / UNFOLLOW (PROFILO AMICO)
// ==========================
    const followButton = document.getElementById("followButton");
    const followingIdSpan = document.getElementById("userProfileID");
    const followerIdSpan = document.getElementById("viewID");

    if (followButton && followingIdSpan && followerIdSpan) {
        const followingID = followingIdSpan.textContent.trim();  // profilo visitato
        const followerID = followerIdSpan.textContent.trim();    // chi guarda

        async function updateFollowButton() {
            try {
                const response = await fetch(
                    `/isFollowing?followerId=${followerID}&followingId=${followingID}`
                );
                if (!response.ok) throw new Error("Errore nel recupero dello stato di follow");

                const isFollowing = await response.json();

                if (isFollowing) {
                    followButton.classList.remove("btn-info");
                    followButton.classList.add("btn-danger");
                    followButton.textContent = "Unfollow";
                } else {
                    followButton.classList.remove("btn-danger");
                    followButton.classList.add("btn-info");
                    followButton.textContent = "Follow";
                }
            } catch (error) {
                console.error("Errore durante il caricamento dello stato di follow:", error);
            }
        }

        updateFollowButton();

        followButton.addEventListener("click", async () => {
            followButton.disabled = true;

            try {
                const params = new URLSearchParams({
                    followerId: followerID,
                    followingId: followingID,
                });

                const response = await fetch(`/toggle_follow?${params.toString()}`, {
                    method: "POST",
                });

                if (!response.ok) throw new Error("Errore durante il follow/unfollow");

                const isFollowing = await response.json();

                followButton.classList.toggle("btn-info", !isFollowing);
                followButton.classList.toggle("btn-danger", isFollowing);
                followButton.textContent = isFollowing ? "Unfollow" : "Follow";

                const followStateChangeEvent = {
                    followerId: followerID,
                    followingId: followingID,
                    isFollowing: isFollowing,
                    timestamp: new Date().getTime()
                };
                localStorage.setItem("followStateChanged", JSON.stringify(followStateChangeEvent));
            } catch (error) {
                console.error("Errore follow/unfollow nel profilo:", error);
            } finally {
                followButton.disabled = false;
            }
        });
    }



});
