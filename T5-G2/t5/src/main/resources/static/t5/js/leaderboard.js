document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.querySelector("#leaderboardTable tbody");
    const pagination = document.getElementById("pagination");
    const selector = document.getElementById("scoreTypeSelector");
    const rowsPerPage = window.rowsPerPage || 3;
    const playerEmail = window.playerEmail || "";
 
    if (!tableBody || !pagination) return;
 
    const SCORE_CONFIG = {
        exp: window.i18nExperience || "Esperienza",
        wins: window.i18nWins || "Vittorie"
    };
 
    let leaderboardLoaded = false;
 
    // Funzione principale di fetch dati e popolamento
    async function loadLeaderboard(userId) {
        try {
            const response = await fetch(`/leaderboard/${userId}`);
            if (response.status === 401) {
                window.location.href = "/login";
                return;
            }
            const data = await response.json();
            populateLeaderboard(data.leaderboard || []);
        } catch (err) {
            console.error("Errore fetch leaderboard:", err);
        }
    }
 
    function populateLeaderboard(players) {
        tableBody.innerHTML = "";
 
        players.forEach(player => {
            const row = document.createElement("tr");
            row.dataset.email = player.email;
            row.innerHTML = `
                <td class="rank"></td>
                <td>${player.name}</td>
                <td>${player.surname}</td>
                <td>${player.email}</td>
                <td class="player-score" data-exp="${player.exp}" data-wins="${player.wins}">${player.exp}</td>
            `;
            tableBody.appendChild(row);
        });
 
        updateLeaderboard(window.defaultScoreType || "exp");
    }
 
    function updateLeaderboard(scoreType) {
        const rows = Array.from(tableBody.querySelectorAll("tr"));
        rows.forEach(row => {
            const scoreCell = row.querySelector(".player-score");
            if (scoreCell) scoreCell.textContent = scoreCell.dataset[scoreType];
        });
 
        rows.sort((a, b) => {
            const scoreA = parseInt(a.querySelector(".player-score").dataset[scoreType] || 0);
            const scoreB = parseInt(b.querySelector(".player-score").dataset[scoreType] || 0);
            return scoreB - scoreA;
        });
 
        tableBody.innerHTML = "";
        let playerRank = null, playerScore = null, currentRank = 1, lastScore = null;
 
        rows.forEach((row, index) => {
            const score = parseInt(row.querySelector(".player-score").dataset[scoreType] || 0);
            if (index > 0 && score !== lastScore) currentRank++;
            lastScore = score;
 
            const rankCell = row.querySelector(".rank");
            if (rankCell) rankCell.textContent = currentRank;
 
            row.classList.remove("highlight-row");
            if (row.dataset.email === playerEmail) {
                row.classList.add("highlight-row");
                playerRank = currentRank;
                playerScore = score;
            }
 
            tableBody.appendChild(row);
        });
 
        setupPagination();
        showPage(1);
    }
 
    function showPage(page) {
        const rows = tableBody.querySelectorAll("tr");
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        rows.forEach((row, i) => row.style.display = (i >= start && i < end) ? "" : "none");
    }
 
    function setupPagination() {
        const rows = tableBody.querySelectorAll("tr");
        const pageCount = Math.ceil(rows.length / rowsPerPage);
        pagination.innerHTML = "";
 
        for (let i = 1; i <= pageCount; i++) {
            const li = document.createElement("li");
            li.classList.add("page-item");
            if (i === 1) li.classList.add("active");
 
            const link = document.createElement("a");
            link.classList.add("page-link");
            link.href = "#";
            link.innerText = i;
 
            link.addEventListener("click", e => {
                e.preventDefault();
                document.querySelectorAll(".page-item").forEach(p => p.classList.remove("active"));
                li.classList.add("active");
                showPage(i);
            });
 
            li.appendChild(link);
            pagination.appendChild(li);
        }
    }
 
    // Event listener per cambio tipo punteggio
    if (selector) {
        selector.addEventListener("change", function () {
            updateLeaderboard(this.value);
        });
    }
 
    // Caricamento dati al click sul tab Leaderboard
    const leaderboardTab = document.getElementById("pills-leaderboard-tab");
    if (leaderboardTab) {
        leaderboardTab.addEventListener("shown.bs.tab", () => {
            if (leaderboardLoaded) return;
            leaderboardLoaded = true;
            const userId = document.body.dataset.userid;
            loadLeaderboard(userId);
        });
    }
 
    // Esponi globalmente la funzione
    window.updateLeaderboard = updateLeaderboard;
});
 