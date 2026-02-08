let allGames = [];
let currentPage = 0;
const PAGE_SIZE = 3;

export async function fetchGameHistory(playerId) {
    const response = await fetch(`/profile/game-history/${playerId}`);
    const data = await response.json();
    if (Array.isArray(data)) {
        allGames = data.sort((a,b) => new Date(b.closedAt) - new Date(a.closedAt));
        renderCurrentPage();
    }
}

export function goToNextMatchesPage() {
    if ((currentPage + 1) * PAGE_SIZE < allGames.length) {
        currentPage++;
        renderCurrentPage();
    }
}

export function goToPrevMatchesPage() {
    if (currentPage > 0) {
        currentPage--;
        renderCurrentPage();
    }
}

export function renderCurrentPage() {
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

export function updateButtons() {
    document.getElementById("prevMatchesBtn").disabled = currentPage === 0;
    document.getElementById("nextMatchesBtn").disabled =
        (currentPage + 1) * PAGE_SIZE >= allGames.length;
}
