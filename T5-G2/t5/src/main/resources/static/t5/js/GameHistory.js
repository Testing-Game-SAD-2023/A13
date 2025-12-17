document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("game-history-container");
    const pagination = document.getElementById("game-history-pagination");
    const GAMES_PER_PAGE = 3;
    let currentPage = 1;
    let allGames = [];
 
    const i18n = window.GAMES_i18n || {
        win_label: "VITTORIA",
        loss_label: "SCONFITTA",
        no_games_message: "Nessuna partita trovata",
        error_loading_games: "Errore nel caricamento delle partite"
    };
 
    if (!container || !pagination) return;
 
    function fetchGameHistory(playerId) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: `/api/gamerepo/games/player/${playerId}`,
                type: "GET",
                dataType: "json",
                success: response => {
                    if (Array.isArray(response)) resolve(response);
                    else reject("Risposta non valida dall'API");
                },
                error: (xhr, status, error) => reject(error)
            });
        });
    }
 
    function formatDate(isoDate) {
        const date = new Date(isoDate);
        const day = ("0" + date.getDate()).slice(-2);
        const month = ("0" + (date.getMonth() + 1)).slice(-2);
        const year = date.getFullYear();
        const hours = ("0" + date.getHours()).slice(-2);
        const minutes = ("0" + date.getMinutes()).slice(-2);
        return `${day}/${month}/${year} ${hours}:${minutes}`;
    }
 
    function renderGamesPage(page = 1) {
        container.innerHTML = "";
        currentPage = page;
 
        const start = (page - 1) * GAMES_PER_PAGE;
        const end = start + GAMES_PER_PAGE;
        const gamesToShow = allGames.slice(start, end);
 
        if (gamesToShow.length === 0) {
            container.innerHTML = `<p class="text-center text-secondary">${i18n.no_games_message}</p>`;
            return;
        }
 
        gamesToShow.forEach(game => {
            let resultText = '<span class="badge text-bg-secondary">N/A</span>';
            let scoreText = '';
 
            if (game.playerResults && Object.keys(game.playerResults).length > 0) {
                const firstUserId = Object.keys(game.playerResults)[0];
                const result = game.playerResults[firstUserId];
 
                if (result) {
                    resultText = result.winner
                        ? `<span class="badge text-bg-success">${i18n.win_label}</span>`
                        : `<span class="badge text-bg-danger">${i18n.loss_label}</span>`;
                        scoreText = `${i18n.coverage_label} ${result.score || 0}%`;
                    }
            }
 
            const html = `
                <a class="list-group-item list-group-item-action">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h5 class="mb-1">${game.rounds?.[0]?.classUT || "N/A"}</h5>
                            <p class="mb-0">${game.gameMode || "N/A"} - ${game.rounds?.[0]?.type || "N/A"} - ${game.rounds?.[0]?.difficulty || "N/A"}</p>
                            <small>${formatDate(game.closedAt)}</small>
                        </div>
                        <div class="text-center p-3">
                            <h4 class="mb-0 fw-semibold">${resultText}</h4>
                            <small>${scoreText}</small>
                        </div>
                    </div>
                </a>
            `;
            container.insertAdjacentHTML("beforeend", html);
        });
 
        renderPaginationControls();
    }
 
    function renderPaginationControls() {
        pagination.innerHTML = "";
        const pageCount = Math.ceil(allGames.length / GAMES_PER_PAGE);
 
        for (let i = 1; i <= pageCount; i++) {
            const li = document.createElement("li");
            li.classList.add("page-item");
            if (i === currentPage) li.classList.add("active");
 
            const link = document.createElement("a");
            link.classList.add("page-link");
            link.href = "#";
            link.innerText = i;
 
            link.addEventListener("click", e => {
                e.preventDefault();
                renderGamesPage(i);
            });
 
            li.appendChild(link);
            pagination.appendChild(li);
        }
    }
 
    // --- CARICAMENTO IMMEDIATO DEL PRIMO TAB ---
    const userId = document.body.dataset.userid;
    if (userId) {
        fetchGameHistory(userId)
            .then(games => {
                allGames = games.sort((a, b) => new Date(b.closedAt) - new Date(a.closedAt));
                renderGamesPage(1);
            })
            .catch(err => {
                container.innerHTML = `<div class="text-center p-3 text-danger">${i18n.error_loading_games}</div>`;
                console.error(err);
            });
    }
});