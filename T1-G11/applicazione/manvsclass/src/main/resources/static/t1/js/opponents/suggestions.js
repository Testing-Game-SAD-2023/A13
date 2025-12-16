async function fetchAndDisplaySuggestions(className) {

    try {

        const response = await callGetSuggestions(className);
        const suggestionsList = Array.isArray(response)
            ? response
            : (response?.suggestions || []);

        const listId = `suggestions-list_${className}`;
        const containerId = `suggestions-container_${className}`;
        const downloadBtnId = `btnDownload_${className}`;

        const listElement = document.getElementById(listId);
        const detailsContainer = document.getElementById(containerId);
        const downloadBtn = document.getElementById(downloadBtnId);

        if (!listElement || !detailsContainer) {
            console.error(`Elementi DOM mancanti per: ${className}`);
            return;
        }

        if (suggestionsList.length === 0) {
            listElement.innerHTML = '';
            detailsContainer.style.display = 'none';
            detailsContainer.open = false;
            if (downloadBtn) downloadBtn.style.display = 'none';
            return;
        }

        listElement.innerHTML = '';

        suggestionsList.forEach(suggestion => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-start bg-transparent border-bottom";

            const contentDiv = document.createElement('div');
            contentDiv.className = "w-100";

            contentDiv.innerHTML = `
                <div class="d-flex align-items-center mb-1">
                    <span class="font-weight-bold mr-2">${suggestion.order}</span>
                    ${
                        suggestion.level
                            ? `<span class="badge mr-2 ${
                                    suggestion.level === "LOW" ? "badge-success" :
                                    suggestion.level === "MEDIUM" ? "badge-warning" :
                                    "badge-danger"
                                }">${suggestion.level}</span>`
                            : ""
                    }
                    <small class="text-muted">${suggestion.date || ''}</small>
                </div>

                <div class="text-dark mb-1">${suggestion.hint}</div>

                ${
                    suggestion.image
                        ? `<div class="mt-2 position-relative" style="display: inline-block;">
                                <img src="/t1/images/${suggestion.image}?t=${new Date().getTime()}"
                                     class="img-fluid rounded border"
                                     style="max-height: 200px; max-width: 100%;"
                                     alt="Immagine suggerimento" />

                                <button type="button"
                                        class="btn btn-sm btn-danger position-absolute"
                                        style="top: 0; right: 0;"
                                        onclick="deleteSuggestionImage('${className}', ${suggestion.order})">
                                    <i class="fa fa-times"></i>
                                </button>
                           </div>`
                        : ""
                }
            `;

            const actionsDiv = document.createElement('div');
            actionsDiv.className = "d-flex flex-column ml-3";

            actionsDiv.innerHTML = `
                <label class="btn btn-sm btn-outline-primary mb-1" title="Carica Immagine" style="cursor:pointer;">
                    <i class="fa fa-camera"></i>
                    <input type="file" style="display:none;" accept="image/*"
                           data-classname="${className}"
                           data-order="${suggestion.order}"
                           onchange="uploadSuggestionImage(this, '${className}', ${suggestion.order})">
                </label>

                <button type="button"
                        class="btn btn-sm btn-outline-danger"
                        onclick="deleteSuggestion('${className}', ${suggestion.order})">
                    <i class="fa fa-trash"></i>
                </button>
            `;

            li.appendChild(contentDiv);
            li.appendChild(actionsDiv);
            listElement.appendChild(li);
        });

        // 5. Mostra e apri il container
        detailsContainer.style.display = 'block';
        detailsContainer.open = true;

        if (downloadBtn) downloadBtn.style.display = 'inline-block';

    } catch (error) {
        console.error(`Errore gestione suggerimenti (${className}):`, error);
        showStyledMessage(translations.errors.fetchSuggestions, translations.titles.error);
    }
}


async function deleteSuggestion(className, order) {
    try {
        await callDeleteSuggestion(className, order);
        await fetchAndDisplaySuggestions(className);
    } catch (error) {
        console.error("Errore cancellazione suggerimento:", error);

        showStyledMessage(translations.errors.deleteSuggestion);
    }
}

function handleSuggestionUpload(inputElement, expectedClassName) {
    const file = inputElement.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = async (event) => {
        try {
            const fileContent = event.target.result;
            const jsonData = JSON.parse(fileContent);

            if (
                typeof jsonData !== "object" ||
                jsonData === null ||
                typeof jsonData.className !== "string" ||
                !Array.isArray(jsonData.suggestions)
            ) {
                throw new Error("Formato non valido: atteso { className: string, suggestions: [] }.");
            }

            if (jsonData.className !== expectedClassName) {
                throw new Error(`La classe nel file (${jsonData.className}) non corrisponde a quella attesa (${expectedClassName}).`);
            }

            const isValidSuggestion = s => (
                typeof s === 'object' && s !== null &&
                typeof s.order === 'number' &&
                typeof s.hint === 'string' && s.hint.trim().length > 0 &&
                (s.image === undefined || s.image === null || typeof s.image === 'string') &&
                typeof s.level === 'string'
            );

            if (!jsonData.suggestions.every(isValidSuggestion)) {
                throw new Error("Dati non validi: ogni suggerimento deve contenere 'order', 'hint' e 'level'.");
            }

            const payload = {
                className: expectedClassName,
                suggestions: jsonData.suggestions
            };

            await callUploadSuggestions(payload);

            await fetchAndDisplaySuggestions(expectedClassName);

            const msg = translations.suggestions.suggestionsUploadSuccess.replace('{0}', expectedClassName);
            showStyledMessage(msg, translations.titles.success);

        } catch (e) {
            console.error("Errore upload suggerimento:", e);
            showStyledMessage(`${translations.titles.error}: ${e.message}`, translations.titles.error);
        } finally {
            inputElement.value = null;
        }
    };

    reader.onerror = () =>
        showStyledMessage(translations.errors.readFile || "Impossibile leggere il file.", "Errore");

    reader.readAsText(file);
}


async function uploadSuggestionImage(inputElement, className, order) {

    if (inputElement.files && inputElement.files[0]) {
        const file = inputElement.files[0];

        if (!file.type.match('image.*')) {
            showStyledMessage("Seleziona un'immagine valida.", translations.titles.error);
            inputElement.value = null;
            return;
        }

        const formData = new FormData();
        formData.append("image", file);

        try {
            await callUploadSuggestionImage(formData, className, order);

            await fetchAndDisplaySuggestions(className);
            showStyledMessage("Immagine caricata con successo.", translations.titles.success);

        } catch (error) {
            showStyledMessage("Errore durante il caricamento dell'immagine.", translations.titles.error);        } finally {
            inputElement.value = null;
        }
    }
}

async function downloadSuggestions(className) {

    try {

        const response = await callDownloadSuggestions(className);
        if (!response) throw new Error("Json non valido o download fallito");

        const filteredResponse = response.suggestions.map(item => ({
            order: item.order,
            hint: item.hint,
            level: item.level
        }));

        const finalOutput = {
            className: response.className,
            suggestions: filteredResponse
        };

        const jsonString = JSON.stringify(finalOutput, null, 2);
        const blob = new Blob([jsonString], {type: "application/json; charset=utf-8"});

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = className + "_Suggestions.json";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(link.href);

    } catch (err) {
        console.error("Errore nel download:", err);

        showStyledMessage(translations.errors.download);
    }
}

async function deleteSuggestionImage(className, order) {

    try {
        await callDeleteSuggestionImage(className, order);
        await fetchAndDisplaySuggestions(className);
    } catch (e) {
        showStyledMessage("Errore nella rimozione immagine.", translations.titles.error);
    }
}