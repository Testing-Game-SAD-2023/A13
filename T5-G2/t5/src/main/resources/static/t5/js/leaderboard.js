document.addEventListener("DOMContentLoaded", function () {
    // Configurazione paginazione (numero di righe per pagina)
    const rowsPerPage = 3;

    // Riferimenti agli elementi DOM
    const tableBody = document.querySelector("#leaderboardTable tbody");
    const pagination = document.getElementById("pagination");
    const selector = document.getElementById("scoreTypeSelector");

    const SCORE_CONFIG = {
        exp: { label: i18nExperience },
        wins: { label: i18nWins }
        // Aggiungere nuove metriche QUI
    };

    /**
     * Aggiorna la classifica in base al tipo di punteggio selezionato (esperienza o vittorie).
     * Riordina le righe, calcola i rank con dense ranking e aggiorna il badge del giocatore.
     * @param {string} scoreType - Tipo di punteggio: "exp" (esperienza) o "wins" (vittorie)
     */
    function updateLeaderboard(scoreType) {
        const rows = Array.from(tableBody.querySelectorAll("tr"));

        // Aggiorna il contenuto delle celle dei punteggi
        rows.forEach(row => {
            const scoreCell = row.querySelector(".player-score");
            scoreCell.textContent = scoreCell.dataset[scoreType];
        });

        // Ordina le righe in ordine decrescente per punteggio
        rows.sort((a, b) => {
            const scoreA = parseInt(a.querySelector(".player-score").dataset[scoreType]);
            const scoreB = parseInt(b.querySelector(".player-score").dataset[scoreType]);
            return scoreB - scoreA;
        });

        // Svuota la tabella per reinserire le righe ordinate
        tableBody.innerHTML = "";
        // Inizializza i valori per ordinare la tabella
        let playerRank = null;
        let playerScore = null;
        let currentRank = 1;
        let lastScore = null;

        // Ciclo per reinserire le righe ordinate e calcolare il rank
        rows.forEach((row, index) => {
            const score = parseInt(row.querySelector(".player-score").dataset[scoreType]);
            // Incrementa il rank solo se il punteggio è diverso dal precedente (dense ranking)
            if (index > 0 && score !== lastScore) {
                currentRank++;
            }
            lastScore = score;
            // Assegna il rank alla cella della posizione
            row.querySelector(".rank").textContent = currentRank;

            // Evidenzia la riga del giocatore corrente
            row.classList.remove("highlight-row");
            if (row.dataset.email === playerEmail) {
                row.classList.add("highlight-row");
                playerRank = currentRank;
                playerScore = score;
            }
            tableBody.appendChild(row);
        });

        // Aggiorna il badge del giocatore con posizione e punteggio correnti
        if (playerRank !== null) {
            document.getElementById("playerPositionBadge").textContent = "# " + playerRank;
            document.getElementById("playerScoreBadge").textContent =
                SCORE_CONFIG[scoreType].label + ": " + playerScore;
        }

        // Ricrea la paginazione e mostra la prima pagina
        setupPagination();
        showPage(1);
    }

    /**
     * Mostra una specifica pagina della tabella nascondendo tutte le altre righe.
     * @param {number} page - Numero della pagina da visualizzare (1-based)
     */
    function showPage(page) {
        const rows = tableBody.querySelectorAll("tr");
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, i) => {
            row.style.display = (i >= start && i < end) ? "" : "none";
        });
    }

    /**
     * Crea i controlli di paginazione in base al numero totale di righe.
     * Genera un pulsante per ogni pagina e gestisce lo stato attivo.
     */
    function setupPagination() {
        const rows = tableBody.querySelectorAll("tr");
        const pageCount = Math.ceil(rows.length / rowsPerPage);

        pagination.innerHTML = "";

        // Ciclo di generazione dei pulsanti 1, 2, 3...
        // Assegna a ciascuno l'evento click per cambiare l'UI e visualizzare la pagina con showPage(i).
        for (let i = 1; i <= pageCount; i++) {
            const li = document.createElement("li");
            li.classList.add("page-item");
            if (i === 1) li.classList.add("active");

            const link = document.createElement("a");
            link.classList.add("page-link");
            link.href = "#";
            link.innerText = i;

            // Gestisce il click sul pulsante di paginazione
            link.addEventListener("click", function (e) {
                e.preventDefault();
                document.querySelectorAll(".page-item").forEach(p => p.classList.remove("active"));
                li.classList.add("active");
                showPage(i);
            });

            li.appendChild(link);
            pagination.appendChild(li);
        }
    }

    // Inizializza la classifica con il punteggio "esperienza"
    updateLeaderboard("exp");

    // Listener per il cambio del tipo di punteggio (esperienza/vittorie)
    selector.addEventListener("change", function () {
        updateLeaderboard(this.value);
    });
});