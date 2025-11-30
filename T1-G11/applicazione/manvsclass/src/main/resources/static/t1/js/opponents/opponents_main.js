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
};

assignUrls(links);

document.getElementById("searchForm").addEventListener("search", event => {
    event.preventDefault();

    const searchValue = document.getElementById('searchInput').value.trim();
    const url = new URL(VIEWS.OPPONENTS_MAIN, window.location.origin);

    if (searchValue) url.searchParams.set("search", searchValue);

    const currentParams = new URLSearchParams(window.location.search);
    if (currentParams.has("sortBy")) url.searchParams.set("sortBy", currentParams.get("sortBy"));
    if (currentParams.has("filterByDifficulty")) url.searchParams.set("filterByDifficulty", currentParams.get("filterByDifficulty"));

    window.location.href = url.toString();
});

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
        alert("Si è verificato un errore durante il download.");
    }
}

async function deleteClassUT(classUTName) {
    await callDeleteClassUT(classUTName);
}

async function fetchAndDisplaySuggestions(className) {
    try {
        const suggestionsList = await callGetSuggestions(className);
        if (!suggestionsList || suggestionsList.length === 0) {
            const listElement = document.getElementById(`suggestions-list_${className}`);
            if (listElement) listElement.innerHTML = '';
            return;
        }

        let listElement = document.getElementById(`suggestions-list_${className}`);
        let detailsContainer = document.getElementById(`suggestions-container_${className}`);

        if (!detailsContainer) {
            const classDetailsElement = document.querySelector(`label[for='uploadInput_${className}']`).closest('details');
            if (!classDetailsElement) return;

            const template = document.getElementById('suggestions-container_TEMPLATE');
            if (!template) return;

            detailsContainer = template.cloneNode(true);
            detailsContainer.id = `suggestions-container_${className}`;
            listElement = detailsContainer.querySelector('ul');
            listElement.id = `suggestions-list_${className}`;
            classDetailsElement.appendChild(detailsContainer);
        }

        listElement.innerHTML = '';

        suggestionsList.forEach(suggestion => {
            const li = document.createElement('li');
            li.className = 'd-flex justify-content-between align-items-start mb-1 border-bottom pb-2';

            const divContent = document.createElement('div');
            divContent.className = 'w-100';

            const headerRow = document.createElement('div');
            headerRow.className = 'd-flex align-items-center mb-1';

            const titleSpan = document.createElement('span');
            titleSpan.textContent = suggestion.title;
            titleSpan.className = 'font-weight-bold mr-2';

            const levelSpan = document.createElement('span');
            levelSpan.textContent = suggestion.level;
            let badgeClass = 'badge-secondary';
            if (suggestion.level === 'LOW') badgeClass = 'badge-success';
            if (suggestion.level === 'MEDIUM') badgeClass = 'badge-warning';
            if (suggestion.level === 'HIGH') badgeClass = 'badge-danger';
            levelSpan.className = `badge ${badgeClass} mr-2`;

            const dateSmall = document.createElement('small');
            dateSmall.textContent = suggestion.date || '';
            dateSmall.className = 'text-muted';

            headerRow.appendChild(titleSpan);
            headerRow.appendChild(levelSpan);
            headerRow.appendChild(dateSmall);

            const hintDiv = document.createElement('div');
            hintDiv.textContent = suggestion.hint;
            hintDiv.className = 'text-dark mb-1';

            divContent.appendChild(headerRow);
            divContent.appendChild(hintDiv);

            if (suggestion.image) {
                const imgContainer = document.createElement('div');
                imgContainer.className = 'mt-2';
                const img = document.createElement('img');
                img.src = suggestion.image.startsWith('data:image') ? suggestion.image : `data:image/png;base64,${suggestion.image}`;
                img.className = 'img-fluid rounded border';
                img.style.maxHeight = '200px';
                img.style.maxWidth = '100%';
                img.alt = 'Immagine suggerimento';
                imgContainer.appendChild(img);
                divContent.appendChild(imgContainer);
            }

            li.appendChild(divContent);

            const deleteButton = document.createElement('button');
            deleteButton.type = 'button';
            deleteButton.className = 'btn btn-sm btn-outline-danger ml-3 mt-1';
            deleteButton.title = 'Elimina suggerimento';
            deleteButton.onclick = () => deleteSuggestion(className, suggestion.title);

            const trashIcon = document.createElement('i');
            trashIcon.className = 'fa fa-trash';
            deleteButton.appendChild(trashIcon);

            li.appendChild(deleteButton);
            listElement.appendChild(li);
        });

        detailsContainer.style.display = 'block';
        detailsContainer.open = true;

    } catch (error) {
        console.error(`Errore durante il recupero suggerimenti per ${className}:`, error);
        alert("Errore nel recuperare i suggerimenti aggiornati. Ricarica la pagina.");
    }
}

async function deleteSuggestion(className, suggestionTitle) {
    try {
        await callDeleteSuggestion(className, suggestionTitle);
        await fetchAndDisplaySuggestions(className);
    } catch (error) {
        console.error("Errore cancellazione suggerimento:", error);
        alert("Impossibile eliminare il suggerimento.");
    }
}

function handleUpload(inputElement, expectedClassName) {
    const file = inputElement.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = async (event) => {
        try {
            const fileContent = event.target.result;
            const jsonData = JSON.parse(fileContent);

            const isValidSuggestion = s => (
                typeof s === 'object' && s !== null &&

                typeof s.title === 'string' && s.title.trim().length > 0 &&

                typeof s.hint === 'string' && s.hint.trim().length > 0 &&

                (s.image === undefined || s.image === null || typeof s.image === 'string') &&

                typeof s.level === 'string'
            );

            if (!Array.isArray(jsonData)) {
                throw new Error("Formato non valido: Il file deve essere una lista JSON pura [...].");
            }

            if (!jsonData.every(isValidSuggestion)) {
                throw new Error("Dati non validi: 'title' e 'hint' sono obbligatori e non possono essere vuoti o contenere solo spazi.");
            }

            const suggestionsPayload = jsonData;

            await callUploadSuggestions(expectedClassName, suggestionsPayload);


            await fetchAndDisplaySuggestions(expectedClassName);
            await fetchAndDisplayGuidelines();

            alert(`Suggerimenti per ${expectedClassName} caricati con successo!`);
        } catch (e) {
            console.error("Errore upload suggerimento:", e);
            alert(`Errore: ${e.message}`);
        } finally {
            inputElement.value = null;
        }
    };
    reader.onerror = () => alert("Impossibile leggere il file.");
    reader.readAsText(file);
}

async function fetchAndDisplayGuidelines() {
    const listContainer = document.getElementById('global-guidelines-list');

    const detailsContainer = document.getElementById('global-guidelines-container');

    if (!listContainer) return;

    try {
        const guidelines = await callGetGuidelines();
        listContainer.innerHTML = '';

        if (!guidelines || guidelines.length === 0) {
            listContainer.innerHTML = '<li class="list-group-item bg-transparent text-muted border-0 pl-0">Nessuna linea guida presente.</li>';
            return;
        }

        if (detailsContainer) {
            detailsContainer.open = true;
        }

        guidelines.forEach(guide => {

            const li = document.createElement('li');
            li.className = 'd-flex justify-content-between align-items-start mb-1 border-bottom pb-2';

            const divContent = document.createElement('div');
            divContent.className = 'w-100';

            const headerRow = document.createElement('div');
            headerRow.className = 'd-flex align-items-center mb-1';

            const titleSpan = document.createElement('span');
            titleSpan.textContent = guide.title;
            titleSpan.className = 'font-weight-bold mr-2 text-info';
            headerRow.appendChild(titleSpan);

            if (guide.date) {
                const dateSmall = document.createElement('small');
                dateSmall.textContent = guide.date;
                dateSmall.className = 'text-muted';
                headerRow.appendChild(dateSmall);
            }

            const hintDiv = document.createElement('div');
            hintDiv.textContent = guide.hint || "";
            hintDiv.className = 'text-dark mb-1 small';

            divContent.appendChild(headerRow);
            divContent.appendChild(hintDiv);

            const imageContent = guide.image || guide.base64Image;
            if (imageContent) {
                const imgContainer = document.createElement('div');
                imgContainer.className = 'mt-2';
                const img = document.createElement('img');

                img.src = imageContent.startsWith('data:image') ? imageContent : `data:image/png;base64,${imageContent}`;
                img.className = 'img-fluid rounded border';
                img.style.maxHeight = '200px';
                img.style.maxWidth = '100%';
                img.alt = 'Immagine linea guida';
                imgContainer.appendChild(img);
                divContent.appendChild(imgContainer);
            }

            li.appendChild(divContent);

            const deleteButton = document.createElement('button');
            deleteButton.type = 'button';
            deleteButton.className = 'btn btn-sm btn-outline-danger ml-3 mt-1';
            deleteButton.title = 'Elimina linea guida';
            deleteButton.onclick = () => deleteGuideline(guide.title);

            const trashIcon = document.createElement('i');
            trashIcon.className = 'fa fa-trash';
            deleteButton.appendChild(trashIcon);

            li.appendChild(deleteButton);
            listContainer.appendChild(li);
        });

    } catch (e) {
        console.error("Errore fetchAndDisplayGuidelines:", e);
        listContainer.innerHTML = '<li class="list-group-item border-0 text-danger">Errore caricamento linee guida.</li>';
    }
}

async function deleteGuideline(title) {
    try {
        await callDeleteGuideline(title);
        await fetchAndDisplayGuidelines();
    } catch (e) {
        alert("Impossibile eliminare la linea guida.");
    }
}

function handleGuidelinesUpload(inputElement) {
    const file = inputElement.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = async (event) => {
        try {
            const fileContent = event.target.result;
            const jsonData = JSON.parse(fileContent);

            if (!Array.isArray(jsonData)) {
                throw new Error("Formato non valido: Il file delle linee guida deve essere una lista JSON pura [...].");
            }

            const guidelinesPayload = jsonData.map(item => ({
                title: item.title,
                hint: item.hint, // Non mettiamo default "" per poter validare se manca
                image: item.image || item.base64Image || null
            }));

            const isValidGuideline = g => (
                typeof g === 'object' && g !== null &&

                typeof g.title === 'string' && g.title.trim().length > 0 &&

                typeof g.hint === 'string' && g.hint.trim().length > 0 &&

                (g.image === null || typeof g.image === 'string')
            );

            if (!guidelinesPayload.every(isValidGuideline)) {
                throw new Error("Dati non validi: 'title' e 'hint' sono obbligatori e non possono essere vuoti o contenere solo spazi.");
            }

            await callUploadGuidelines(guidelinesPayload);

            alert("Linee guida caricate con successo!");
            await fetchAndDisplayGuidelines();

        } catch (e) {
            console.error("Errore upload linee guida:", e);
            alert(`Errore: ${e.message}`);
        } finally {
            inputElement.value = null;
        }
    };
    reader.onerror = () => alert("Impossibile leggere il file.");
    reader.readAsText(file);
}

document.addEventListener("DOMContentLoaded", () => {
    fetchAndDisplayGuidelines();
});
