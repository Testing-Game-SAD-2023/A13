/*
 * Copyright (c) 2025 Stefano Marano
 * Versione: Full Bootstrap Modals (Fix Edit Reload)
 */

// === CONFIGURAZIONE URL ===
const links = {
    navDashboardAdmin: VIEWS.DASHBOARD_ADMIN,
    navHintsMain: VIEWS.HINTS_MAIN,
    navDashboardAdmin2: VIEWS.DASHBOARD_ADMIN,
    navHintsUpload: VIEWS.HINTS_UPLOAD,
};
assignUrls(links);

// === FUNZIONE TRADUZIONE ===
function t(key, ...args) {
    if (typeof serverMessages === 'undefined') {
        console.error("Errore: serverMessages non definito.");
        return key;
    }
    let text = serverMessages[key] || key;
    args.forEach((arg, index) => {
        text = text.replace(`{${index}}`, arg);
    });
    return text;
}

// === VARIABILI GLOBALI ===
let currentActiveHint = null;
let pendingDeleteAction = null; // Variabile per memorizzare l'azione di eliminazione da eseguire

// === GESTIONE STORICO NAVIGAZIONE ===
window.addEventListener('popstate', (event) => {
    const state = event.state;
    if (!state) {
        toggleInitialView(true);
        setDynamicContent('');
        return;
    }
    if (state.view === 'generic') {
        renderGenericHintsView(false);
    } else if (state.view === 'classes') {
        renderClassHintsListView(false);
    } else if (state.view === 'class_detail') {
        renderClassHintsDetailView(state.className, false);
    } else if (state.view === 'hint_detail') {
        renderHintDetailView(state.order, state.type, state.classUTName, false);
    }
});

// === UTILITY ===
function escapeHtml(unsafe) {
    if (!unsafe) return '';
    return unsafe.toString().replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;").replace(/`/g, "&#x60;");
}

function toggleInitialView(show) {
    const initialButtons = document.getElementById('initial-buttons');
    if (initialButtons) initialButtons.style.display = show ? 'flex' : 'none';
    const navbarContent = document.getElementById('navbarSupportedContent');
    if (navbarContent) navbarContent.style.display = show ? 'block' : 'none';
}

function setDynamicContent(html) {
    document.getElementById('dynamic-content').innerHTML = html;
}

// === GESTIONE MODALI DI CONFERMA ===
function showConfirmModal(message, confirmCallback) {
    // 1. Imposta il testo del messaggio
    document.getElementById('confirmModalBody').innerText = message;

    // 2. Salva la funzione da eseguire in caso di click su "Sì"
    pendingDeleteAction = confirmCallback;

    // 3. Mostra la modale
    $('#confirmModal').modal('show');
}

// Listener per il bottone "Sì" della modale (definito una volta sola all'avvio)
document.addEventListener("DOMContentLoaded", () => {
    const confirmBtn = document.getElementById('confirmModalBtn');
    if(confirmBtn) {
        confirmBtn.addEventListener('click', () => {
            if (pendingDeleteAction) {
                pendingDeleteAction(); // Esegue la funzione salvata
            }
            $('#confirmModal').modal('hide'); // Chiude la modale
        });
    }

    // Setup bottoni iniziali
    const btnGeneric = document.getElementById('viewGenericHintsBtn');
    const btnClass = document.getElementById('viewClassHintsBtn');
    if(btnGeneric) {
        btnGeneric.innerHTML = `<i class="fa fa-list"></i> ${t('title_generic')}`;
        btnGeneric.addEventListener('click', () => renderGenericHintsView(true));
    }
    if(btnClass) {
        btnClass.innerHTML = `<i class="fa fa-list"></i> ${t('title_classes')}`;
        btnClass.addEventListener('click', () => renderClassHintsListView(true));
    }
    toggleInitialView(true);
});


// === RICERCA ===
function getSearchBarHtml(placeholderKey, value = "") {
    const key = placeholderKey || 'placeholder_search';
    return `
        <form id="dynamicSearchForm" class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" id="searchInput" value="${escapeHtml(value)}" placeholder="${t(key)}" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><i class="fa fa-search"></i> ${t('btn_search')}</button>
        </form>
    `;
}
function restoreSearchFocus(value) {
    const input = document.getElementById('searchInput');
    if (input) { input.focus(); input.value = ''; input.value = value; }
}
function handleSearchSubmit(event) {
    event.preventDefault();
    const state = history.state;
    if (!state) return;
    if (state.view === 'generic') renderGenericHintsView(false);
    else if (state.view === 'classes') renderClassHintsListView(false);
    else if (state.view === 'class_detail') renderClassHintsDetailView(state.className, false);
}
function attachSearchListener() {
    const form = document.getElementById('dynamicSearchForm');
    if (form) {
        form.removeEventListener('submit', handleSearchSubmit);
        form.addEventListener('submit', handleSearchSubmit);
    }
}
function getActionButtons(hintId, isFirst) {
    return `
        <div class="btn-group-vertical btn-group-sm">
            <button onclick="event.stopPropagation(); moveHintJS(${hintId}, 'UP')" class="btn btn-outline-secondary" ${isFirst ? 'disabled' : ''}><i class="fa fa-chevron-up"></i></button>
            <button onclick="event.stopPropagation(); moveHintJS(${hintId}, 'DOWN')" class="btn btn-outline-secondary"><i class="fa fa-chevron-down"></i></button>
        </div>`;
}

// === FUNZIONI DI ELIMINAZIONE ===

// Wrapper per l'eliminazione singolo suggerimento
function tryDeleteHint(classUTName, order) {
    showConfirmModal(t('msg_confirm_delete_single'), () => {
        executeDeleteHint(classUTName, order);
    });
}

// Logica effettiva di eliminazione (chiamata dalla modale)
async function executeDeleteHint(classUTName, order) {
    const isGeneric = (classUTName === 'null' || !classUTName);
    const safeClassUTName = isGeneric ? 'null' : encodeURIComponent(classUTName);
    const url = `${APIS.DELETE_HINT_BY_CLASS_AND_ORDER}/${safeClassUTName}/order/${order}`;
    const token = getCookie("jwtToken");

    try {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            if (isGeneric) {
                history.replaceState({ view: 'generic' }, "Generic Hints", "#generic");
                await renderGenericHintsView(false);
            } else {
                try {
                    const hints = await callGetHints(`type=CLASS&classUTName=${safeClassUTName}`);
                    if (hints && hints.length > 0) {
                        history.replaceState({ view: 'class_detail', className: classUTName }, "Class Detail", "#class/" + classUTName);
                        await renderClassHintsDetailView(classUTName, false);
                    } else {
                        history.replaceState({ view: 'classes' }, "Class List", "#classes");
                        await renderClassHintsListView(false);
                    }
                } catch (e) {
                    history.replaceState({ view: 'classes' }, "Class List", "#classes");
                    await renderClassHintsListView(false);
                }
            }
        } else {
            const err = await response.text();
            displayError(t('title_error') + ": " + err);
        }
    } catch (error) {
        displayError(t('err_communication'));
    }
}

// Wrapper per eliminazione di massa
function tryDeleteAllGeneric() {
    showConfirmModal(t('msg_confirm_delete_all_generic'), async () => {
        const url = `${APIS.DELETE_HINT_BY_CLASS}/null`;
        await callDeleteHint(url);
    });
}

function tryDeleteAllClassHints() {
    showConfirmModal(t('msg_confirm_delete_all_class'), async () => {
        const url = `${APIS.DELETE_HINT_BY_TYPE}/CLASS`;
        await callDeleteHint(url);
    });
}

function tryDeleteAllSpecific(classUTName) {
    showConfirmModal(t('msg_confirm_delete_specific', classUTName), async () => {
        const safeClassUTName = encodeURIComponent(classUTName);
        const url = `${APIS.DELETE_HINT_BY_CLASS}/${safeClassUTName}`;
        await callDeleteHint(url);
    });
}

async function callDeleteHint(url) {
    try {
        await redirectOnSuccessTemplate({
            url: url,
            method: "DELETE",
            headers: { 'Content-Type': 'application/json' },
        }, { reload: true });
    } catch (error) {
        let errorMessage = t('err_unknown');
        if (error.response && error.response.message) errorMessage = error.response.message;
        else if (error.message) errorMessage = error.message;
        displayError(errorMessage);
    }
}

// === API MOVEMENT ===
async function moveHintJS(id, direction) {
    const url = `/hints/${id}/move?direction=${direction}`;
    const token = getCookie("jwtToken");
    try {
        const response = await fetch(url, { method: 'POST', headers: { 'Authorization': 'Bearer ' + token } });
        if (response.ok) {
            const currentView = history.state ? history.state.view : null;
            if (currentView === 'generic') renderGenericHintsView(false);
            else if (currentView === 'class_detail') renderClassHintsDetailView(history.state.className, false);
            else location.reload();
        }
    } catch (e) { console.error("Errore rete", e); }
}

// === VISTE DI RENDERING ===

async function renderGenericHintsView(addToHistory = true) {
    if (addToHistory) history.pushState({ view: 'generic' }, "Generic Hints", "#generic");
    toggleInitialView(false);
    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput ? searchInput.value : "";
    setDynamicContent(`<p class="text-info">${t('loading_generic')}</p>`);

    try {
        let queryParams = "type=GENERIC";
        if (searchTerm) queryParams += `&search=${encodeURIComponent(searchTerm)}`;
        const hints = await callGetHints(queryParams);

        const headerButtons = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div>${getSearchBarHtml('placeholder_search', searchTerm)}</div>
                ${(hints && hints.length > 0) ? `
                <button onclick="tryDeleteAllGeneric()" class="btn btn-sm btn-danger">
                    <i class="fa fa-trash"></i> ${t('btn_delete_all_generic')}
                </button>` : ''}
            </div>`;

        if (!hints || hints.length === 0) {
            setDynamicContent(`<h2>${t('title_generic')}</h2>${headerButtons}<p class="text-warning">${t('msg_no_generic')}</p>`);
            attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm); return;
        }

        hints.sort((a, b) => a.order - b.order);
        let tableHtml = `<h2>${t('title_generic')}</h2>${headerButtons}<table id="genericTable" class="table table-hover table-striped"><thead><tr><th style="width: 5%;">${t('col_actions')}</th><th style="width: 5%;">${t('col_order')}</th><th style="width: 20%;">${t('col_title')}</th><th style="width: 55%;">${t('col_content')}</th><th style="width: 15%;">${t('col_type')}</th></tr></thead><tbody>`;

        for (let i = 0; i < hints.length; i++) {
            const h = hints[i];
            tableHtml += `<tr onclick="renderHintDetailView('${h.order}', 'GENERIC')" style="cursor:pointer;"><td>${getActionButtons(h.id, i===0)}</td><td>${h.order}</td><td><strong>${escapeHtml(h.name)}</strong></td><td>${escapeHtml(h.content).substring(0, 100)}...</td><td><span class="badge badge-info">${h.type}</span></td></tr>`;
        }
        tableHtml += `</tbody></table>`;
        setDynamicContent(tableHtml); attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm);
    } catch (e) { setDynamicContent(`<p class="text-danger">${t('msg_error_fetch', e.message)}</p>`); }
}

async function renderClassHintsListView(addToHistory = true) {
    if (addToHistory) history.pushState({ view: 'classes' }, "Class List", "#classes");
    toggleInitialView(false);
    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput ? searchInput.value : "";
    setDynamicContent(`<p class="text-info">${t('loading_classes')}</p>`);

    try {
        let queryParams = "type=CLASS";
        if (searchTerm) queryParams += `&search=${encodeURIComponent(searchTerm)}`;
        const hints = await callGetHints(queryParams);

        const headerButtons = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div>${getSearchBarHtml('placeholder_class', searchTerm)}</div>
                ${(hints && hints.length > 0) ? `
                <button onclick="tryDeleteAllClassHints()" class="btn btn-sm btn-danger">
                    <i class="fa fa-trash"></i> ${t('btn_delete_all_class')}
                </button>` : ''}
            </div>`;

        if (!hints || hints.length === 0) {
            setDynamicContent(`<h2>${t('title_classes')}</h2>${headerButtons}<p class="text-warning">${t('msg_no_classes')}</p>`);
            attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm); return;
        }

        const classGroup = hints.reduce((acc, hint) => {
            if (hint.classUTName) acc[hint.classUTName] = (acc[hint.classUTName] || 0) + 1;
            return acc;
        }, {});

        let tableHtml = `<h2>${t('title_classes')}</h2>${headerButtons}<table class="table table-hover table-striped"><thead><tr><th style="width: 70%;">${t('col_class_name')}</th><th style="width: 30%;">${t('col_hint_count')}</th></tr></thead><tbody>`;
        Object.keys(classGroup).sort().forEach(c => {
            tableHtml += `<tr onclick="renderClassHintsDetailView('${escapeHtml(c)}')" style="cursor:pointer;"><td>${escapeHtml(c)}</td><td>${classGroup[c]}</td></tr>`;
        });
        tableHtml += `</tbody></table>`;
        setDynamicContent(tableHtml); attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm);
    } catch (e) { setDynamicContent(`<p class="text-danger">${t('msg_error_fetch', e.message)}</p>`); }
}

async function renderClassHintsDetailView(classUTName, addToHistory = true) {
    if (addToHistory) history.pushState({ view: 'class_detail', className: classUTName }, "Class Detail", "#class/" + classUTName);
    toggleInitialView(false);
    const safeClassName = escapeHtml(classUTName);
    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput ? searchInput.value : "";
    setDynamicContent(`<p class="text-info">${t('loading_class_hints', safeClassName)}</p>`);

    try {
        let queryParams = `type=CLASS&classUTName=${encodeURIComponent(classUTName)}`;
        if (searchTerm) queryParams += `&search=${encodeURIComponent(searchTerm)}`;
        const hints = await callGetHints(queryParams);

        const headerButtons = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div class="d-flex align-items-center">${getSearchBarHtml('placeholder_simple', searchTerm)}</div>
                ${(hints && hints.length > 0) ? `
                <button onclick="tryDeleteAllSpecific('${safeClassName}')" class="btn btn-sm btn-danger ml-2">
                    <i class="fa fa-trash"></i> ${t('btn_delete_all_specific', safeClassName)}
                </button>` : ''}
            </div>`;

        if (!hints || hints.length === 0) {
            setDynamicContent(`<h2>${t('title_class_detail', safeClassName)}</h2>${headerButtons}<p class="text-warning">${t('msg_no_hints')}</p>`);
            attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm); return;
        }

        hints.sort((a, b) => a.order - b.order);
        let tableHtml = `<h2>${t('title_class_detail', safeClassName)}</h2>${headerButtons}<table class="table table-hover table-striped"><thead><tr><th style="width: 5%;">${t('col_actions')}</th><th style="width: 5%;">${t('col_order')}</th><th style="width: 20%;">${t('col_title')}</th><th style="width: 55%;">${t('col_content')}</th><th style="width: 15%;">${t('col_type')}</th></tr></thead><tbody>`;

        for (let i = 0; i < hints.length; i++) {
            const h = hints[i];
            tableHtml += `<tr onclick="renderHintDetailView('${h.order}', 'CLASS', '${safeClassName}')" style="cursor:pointer;"><td>${getActionButtons(h.id, i===0)}</td><td>${h.order}</td><td><strong>${escapeHtml(h.name)}</strong></td><td>${escapeHtml(h.content).substring(0, 100)}...</td><td><span class="badge badge-info">${h.type}</span></td></tr>`;
        }
        tableHtml += `</tbody></table>`;
        setDynamicContent(tableHtml); attachSearchListener(); if(searchTerm) restoreSearchFocus(searchTerm);
    } catch (e) { setDynamicContent(`<p class="text-danger">${t('msg_error_fetch', e.message)}</p>`); }
}

async function renderHintDetailView(order, type, classUTName = null, addToHistory = true) {
    if (addToHistory) history.pushState({ view: 'hint_detail', order: order, type: type, classUTName: classUTName }, "Hint Detail", "#detail");
    toggleInitialView(false);
    setDynamicContent(`<p class="text-info">${t('loading_detail')}</p>`);

    try {
        let urlParams = new URLSearchParams({ type: type, order: order }).toString();
        if (classUTName && classUTName !== 'null') urlParams += `&classUTName=${encodeURIComponent(classUTName)}`;
        const hints = await callGetHints(urlParams);
        const hintData = (Array.isArray(hints) && hints.length > 0) ? hints[0] : null;

        if (!hintData) { setDynamicContent(`<p class="text-warning">${t('msg_detail_not_found')}</p>`); return; }

        currentActiveHint = hintData;
        const currentClassUTName = hintData.classUTName || 'null';
        const safeName = escapeHtml(hintData.name);

        const detailHtml = `
            <h2>${t('title_detail', safeName)}</h2>
            <div class="card p-4 mt-3">
                <p><strong>${t('label_title')}</strong> ${safeName}</p>
                <p><strong>${t('label_type')}</strong> <span class="badge badge-info">${hintData.type}</span></p>
                <p><strong>${t('label_order')}</strong> ${hintData.order}</p>
                <p><strong>${t('label_content')}</strong><br>${escapeHtml(hintData.content)}</p>
                ${hintData.imageUri ? `<div><p><strong>${t('label_image')}</strong></p><img src="${escapeHtml(hintData.imageUri)}" class="img-fluid border rounded" style="max-width: 600px;"/></div>` : `<p><strong>${t('label_image')}</strong> <em>${t('label_no_image')}</em></p>`}
                <br>
                <p><strong>${t('label_creator')}</strong> ${escapeHtml(hintData.adminEmail || 'N/A')}</p>
                <div class="mt-3">
                    <button type="button" onclick="openEditModal()" class="btn btn-warning mr-2 text-white"><i class="fa fa-pencil"></i> ${t('btn_edit')}</button>
                    <button type="button" onclick="tryDeleteHint('${currentClassUTName}', ${hintData.order})" class="btn btn-danger"><i class="fa fa-trash"></i> ${t('btn_delete')}</button>
                </div>
            </div>`;
        setDynamicContent(detailHtml);
    } catch (e) { setDynamicContent(`<p class="text-danger">${t('msg_error_fetch', e.message)}</p>`); }
}

// === EDIT E MODALI ===
function openEditModal() {
    if (!currentActiveHint) { alert(t('err_data_missing')); return; }
    document.getElementById('editHintForm').reset();
    document.getElementById('editHintId').value = currentActiveHint.id;
    document.getElementById('editHintName').value = currentActiveHint.name;
    document.getElementById('editHintContent').value = currentActiveHint.content;
    $('#editHintModal').modal('show');
}

// --- FUNZIONE AGGIORNATA ---
async function submitEditHint() {
    const id = document.getElementById('editHintId').value;
    const name = document.getElementById('editHintName').value;
    const content = document.getElementById('editHintContent').value;
    const imageInput = document.getElementById('editHintImage');

    if (!name || !content) { alert(t('err_fields_required')); return; }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('content', content);
    if (imageInput.files.length > 0) formData.append('file', imageInput.files[0]);

    try {
        const token = getCookie("jwtToken");
        const response = await fetch(`/hints/update/${id}`, { method: 'PUT', headers: { 'Authorization': 'Bearer ' + token }, body: formData });

        if (response.ok) {
            $('#editHintModal').modal('hide');

            // Aggiorniamo i dati locali (opzionale, ma utile per fluidità)
            if(currentActiveHint) {
                currentActiveHint.name = name;
                currentActiveHint.content = content;
            }

            // Setta i messaggi del modale
            document.getElementById('successModalLabel').innerText = t('title_success');
            document.getElementById('successMessageContent').innerText = t('msg_success_update');

            // --- LA MODIFICA È QUI ---
            // Invece di window.location.reload(), ricarichiamo la vista di dettaglio
            $('#successModal .btn-primary').off('click').on('click', function() {
                $('#successModal').modal('hide'); // Chiude il modale successo

                if (currentActiveHint) {
                    // Recuperiamo i parametri necessari per ri-renderizzare la vista
                    const order = currentActiveHint.order;
                    const type = currentActiveHint.type;
                    const classUTName = currentActiveHint.classUTName || 'null';

                    // Richiamiamo la funzione che disegna il dettaglio (false = non aggiungere alla history)
                    renderHintDetailView(order, type, classUTName, false);
                } else {
                    // Fallback se per caso si sono persi i dati (ma non dovrebbe accadere)
                    renderGenericHintsView(false);
                }
            });

            $('#successModal').modal('show');
        } else {
            const errorText = await response.text();
            displayError(t('title_error') + ": " + errorText);
        }
    } catch (error) { displayError(t('err_communication')); }
}

function displayError(message, containerId = null) {
    let errorContainer = containerId ? document.getElementById(containerId) : document.getElementById('api-error-alert-container');
    if (!errorContainer) errorContainer = document.getElementById('dynamic-content') || document.body;
    const oldAlert = document.getElementById('api-error-alert'); if (oldAlert) oldAlert.remove();
    const errorHtml = `<div id="api-error-alert" class="alert alert-danger mt-3" role="alert" style="color:#842029;background-color:#f8d7da;padding:15px;margin-bottom:20px;"><strong>${t('title_error')}:</strong> ${escapeHtml(message)}<button type="button" class="close" data-dismiss="alert" onclick="document.getElementById('api-error-alert').remove()">&times;</button></div>`;
    errorContainer.insertAdjacentHTML('afterbegin', errorHtml);
    errorContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
}