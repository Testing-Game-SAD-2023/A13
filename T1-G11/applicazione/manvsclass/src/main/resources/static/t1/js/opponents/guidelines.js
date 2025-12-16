async function fetchAndDisplayGuidelines() {
    try {
        const guidelines = await callGetGuidelines();

        const listContainer = document.getElementById('global-guidelines-list');
        const detailsContainer = document.getElementById('global-guidelines-container');
        const downloadBtn = document.getElementById('downloadGuidelinesBtn');

        if (!listContainer) return;

        if (!guidelines || guidelines.length === 0) {
            listContainer.innerHTML = '';
            listContainer.style.display = 'none';
            if (downloadBtn) downloadBtn.style.display = 'none';
            if (detailsContainer) detailsContainer.open = false;
            return;
        }

        listContainer.innerHTML = '';

        guidelines.forEach(guide => {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between align-items-start bg-transparent border-bottom';

            const contentDiv = document.createElement('div');
            contentDiv.className = 'w-100';

            const headerDiv = document.createElement('div');
            headerDiv.className = 'd-flex align-items-center mb-1';

            const orderSpan = document.createElement('span');
            orderSpan.className = 'font-weight-bold mr-2 text-info';
            orderSpan.textContent = guide.order;

            const dateSmall = document.createElement('small');
            dateSmall.className = 'text-muted';
            dateSmall.textContent = guide.date || '';

            headerDiv.appendChild(orderSpan);
            headerDiv.appendChild(dateSmall);

            const hintDiv = document.createElement('div');
            hintDiv.className = 'text-dark mb-1';
            hintDiv.textContent = guide.hint || '';

            contentDiv.appendChild(headerDiv);
            contentDiv.appendChild(hintDiv);

            if (guide.image) {
                const imageWrapper = document.createElement('div');
                imageWrapper.className = 'mt-2 position-relative';
                imageWrapper.style.display = 'inline-block';
                const img = document.createElement('img');
                img.src = `/t1/images/${guide.image}?t=${new Date().getTime()}`;
                img.className = 'img-fluid rounded border';
                img.style.maxHeight = '200px';
                img.style.maxWidth = '100%';
                img.alt = 'Immagine linea guida';

                // Pulsante elimina immagine (X)
                const deleteImgBtn = document.createElement('button');
                deleteImgBtn.type = 'button';
                deleteImgBtn.className = 'btn btn-sm btn-danger position-absolute';
                deleteImgBtn.style.top = '0';
                deleteImgBtn.style.right = '0';
                deleteImgBtn.addEventListener('click', () => deleteGuidelineImage(guide.order));

                const xIcon = document.createElement('i');
                xIcon.className = 'fa fa-times';
                deleteImgBtn.appendChild(xIcon);

                imageWrapper.appendChild(img);
                imageWrapper.appendChild(deleteImgBtn);
                contentDiv.appendChild(imageWrapper);
            }

            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'd-flex flex-column ml-3';

            const uploadLabel = document.createElement('label');
            uploadLabel.className = 'btn btn-sm btn-outline-primary mb-1';
            uploadLabel.style.cursor = 'pointer';
            uploadLabel.title = 'Carica Immagine';

            const cameraIcon = document.createElement('i');
            cameraIcon.className = 'fa fa-camera';
            uploadLabel.appendChild(cameraIcon);

            const fileInput = document.createElement('input');
            fileInput.type = 'file';
            fileInput.style.display = 'none';
            fileInput.accept = 'image/*';
            fileInput.dataset.order = guide.order;
            fileInput.addEventListener('change', function () {
                uploadGuidelineImage(this, guide.order);
            });

            uploadLabel.appendChild(fileInput);
            actionsDiv.appendChild(uploadLabel);

            const deleteBtn = document.createElement('button');
            deleteBtn.type = 'button';
            deleteBtn.className = 'btn btn-sm btn-outline-danger';
            deleteBtn.addEventListener('click', () => deleteGuideline(guide.order));

            const trashIcon = document.createElement('i');
            trashIcon.className = 'fa fa-trash';
            deleteBtn.appendChild(trashIcon);
            actionsDiv.appendChild(deleteBtn);

            li.appendChild(contentDiv);
            li.appendChild(actionsDiv);
            listContainer.appendChild(li);
        });

        listContainer.style.display = 'block';
        if (downloadBtn) downloadBtn.style.display = 'inline-block';
        if (detailsContainer) detailsContainer.open = true;

    } catch (e) {
        console.error("Errore fetchAndDisplayGuidelines:", e);
        showStyledMessage(translations.errors.fetchGuidelines, translations.titles.error);
    }
}

async function deleteGuideline(order) {
    try {
        await callDeleteGuideline(order);
        await fetchAndDisplayGuidelines();
    } catch (e) {

        showStyledMessage(translations.errors.deleteGuideline);
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
                order: item.order,
                hint: item.hint,
                image: item.image || null
            }));

            const isValidGuideline = g => (
                typeof g === 'object' && g !== null &&
                typeof g.order === 'number' &&
                typeof g.hint === 'string' && g.hint.trim().length > 0 &&
                (g.image === null || typeof g.image === 'string')
            );

            if (!guidelinesPayload.every(isValidGuideline)) {
                throw new Error("Dati non validi: 'order' e 'hint' sono obbligatori.");
            }

            await callUploadGuidelines(guidelinesPayload);

            showStyledMessage(translations.guidelines.uploadSuccess, translations.titles.success);

            await fetchAndDisplayGuidelines();

        } catch (e) {
            console.error("Errore upload linee guida:", e);

            showStyledMessage(`${translations.titles.error}: ${e.message}`, translations.titles.error);
        } finally {
            inputElement.value = null;
        }
    };
    reader.onerror = () => showStyledMessage(translations.errors.readFile || "Impossibile leggere il file.", "Errore");
    reader.readAsText(file);
}

async function uploadGuidelineImage(inputElement, order) {
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

            await callUploadGuidelineImage(formData, order);

            await fetchAndDisplayGuidelines();

            showStyledMessage("Immagine caricata con successo.", translations.titles.success);

        } catch (error) {
            showStyledMessage("Errore durante il caricamento dell'immagine.", translations.titles.error);
            console.error("uploadGuidelineImage error:", error);
        } finally {
            inputElement.value = null;
        }
    }
}

async function downloadGuidelines() {

    try {

        const response = await callDownloadGuidelines();
        if (!response) throw new Error("Json non valido o download fallito");

        const filteredResponse = response.map(item => ({
            order: item.order,
            hint: item.hint
        }));

        const jsonString = JSON.stringify(filteredResponse, null, 2);
        const blob = new Blob([jsonString], {type: "application/json; charset=utf-8"});

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = "Guidelines.json";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(link.href);

    } catch (err) {
        console.error("Errore nel download:", err);

        showStyledMessage(translations.errors.download);
    }
}

async function deleteGuidelineImage(order) {

    try {
        await callDeleteGuidelineImage(order);
        await fetchAndDisplayGuidelines();
    } catch (e) {
        showStyledMessage("Errore nella rimozione immagine.", translations.titles.error);
    }
}