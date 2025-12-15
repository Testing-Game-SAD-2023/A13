const links = {
    navDashboardAdmin: VIEWS.DASHBOARD_ADMIN,
    navOpponentsMain: VIEWS.OPPONENTS_MAIN,
    navDashboardAdmin2: VIEWS.DASHBOARD_ADMIN,
    navOpponentsUpload: VIEWS.OPPONENTS_UPLOAD,
    linkSortByDate: VIEWS.OPPONENTS_SORT_BY_DATE,
    linkSortByName: VIEWS.OPPONENTS_SORT_BY_NAME,
    linkFilterDifficultyEasy: VIEWS.OPPONENTS_FILTER_DIFFICULTY_EASY,
    linkFilterDifficultyMedium: VIEWS.OPPONENTS_FILTER_DIFFICULTY_MEDIUM,
    linkFilterDifficultyHard: VIEWS.OPPONENTS_FILTER_DIFFICULTY_HARD,
    navGuidelinesUpload: VIEWS.GUIDELINES_UPLOAD,
};

assignUrls(links);

function showStyledMessage(message, title) {

    const finalTitle = title ? title : translations.titles.alert || "Avviso";

    $('#genericModalTitle').text(finalTitle);
    $('#genericModalBody').text(message);

    $('#genericMessageModal').modal('show');
}

const searchForm = document.getElementById("searchForm");

if (searchForm) {
    searchForm.addEventListener("search", event => {
        event.preventDefault();

        const searchValue = document.getElementById('searchInput').value.trim();
        const url = new URL(VIEWS.OPPONENTS_MAIN, window.location.origin);

        if (searchValue) url.searchParams.set("search", searchValue);

        const currentParams = new URLSearchParams(window.location.search);
        if (currentParams.has("sortBy")) url.searchParams.set("sortBy", currentParams.get("sortBy"));
        if (currentParams.has("filterByDifficulty")) url.searchParams.set("filterByDifficulty", currentParams.get("filterByDifficulty"));

        window.location.href = url.toString();
    });
}

async function downloadClassUT(classUTName) {
    try {
        const blob = await callDownloadClassUT(classUTName);
        if (!blob) throw new Error("Blob non valido o download fallito");

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = classUTName + ".java";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(link.href);
    } catch (err) {
        console.error("Errore nel download:", err);

        showStyledMessage(translations.errors.download || "Si è verificato un errore durante il download.");
    }
}

async function deleteClassUT(classUTName) {
    await callDeleteClassUT(classUTName);
}