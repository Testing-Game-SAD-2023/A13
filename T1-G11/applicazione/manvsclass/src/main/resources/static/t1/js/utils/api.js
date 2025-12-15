/*      Utility functions        */

function executeFetch(url, init) {
    return fetch(url, {
        ...init,
        credentials: 'include'
    });
}

async function handleApiErrors(response) {
    try {
        const errorBody = await response.json();
        console.error('Errore dalla risposta:', errorBody);

        if (errorBody?.errors?.length > 0) {
            errorBody.errors.forEach(err => {
                const container = document.getElementById(`${err.field}_label_container`);
                if (container) {
                    addErrorDiv(container, err.message);
                } else {
                    alert(err.message);
                }
            });
        } else {
            alert(errors.notHandled);
        }
    } catch (e) {
        console.error('Errore durante la lettura del corpo JSON:', e);
        alert(errors.notHandled);
    }
}

/**
 * Effettua una chiamata fetch e ritorna i dati se la risposta è ok.
 * In caso di errore chiama handleApiErrors.
 *
 * @param {Object} param0 parametri di richiesta: url, method, headers, body
 * @param {Function} parseResponse funzione async per parsare response (es: r => r.json())
 * @returns dati parsati o null in caso di errore
 */
async function returnDataOnSuccessTemplate({ url, method, headers, body }, parseResponse) {
    try {
        const init = { method };

        // Inserisce headers solo se definiti
        if (headers) init.headers = headers;

        // Inserisce body solo se definito e diverso da null
        if (body !== undefined && body !== null) init.body = body;

        const response = await executeFetch(url, init);

        if (response.ok) {
            if (parseResponse) {
                return await parseResponse(response);
            } else {
                return null;
            }
        }

        await handleApiErrors(response);
        return null;
    } catch (err) {
        console.error(`Errore nella chiamata ${method} ${url}:`, err);
        alert(errors.notHandled);
        return null;
    }
}

/**
 * Effettua una chiamata fetch e se ok reindirizza o ricarica la pagina.
 * In caso di errore chiama handleApiErrors.
 *
 * @param {Object} param0 parametri di richiesta: url, method, headers, body
 * @param {Object} param1 opzioni: redirectTo (stringa URL opzionale), reload (booleano, default false)
 */
async function redirectOnSuccessTemplate({ url, method, headers, body }, { redirectTo, reload = false }) {
    try {
        const init = { method };

        if (headers) init.headers = headers;
        if (body !== undefined && body !== null) init.body = body;

        const response = await executeFetch(url, init);

        if (response.ok) {
            if (reload) {
                location.reload();
            } else if (redirectTo) {
                window.location.href = redirectTo;
            }
            return;
        }

        await handleApiErrors(response);
    } catch (err) {
        console.error(`Errore nella chiamata ${method} ${url}:`, err);
        alert(errors.notHandled);
    }
}


/*      API calls        */


async function callLogoutAdmin() {
    await redirectOnSuccessTemplate(
        {
            url: APIS.USER_SERVICE.LOGOUT_ADMIN,
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: null
        },
        {
            redirectTo: VIEWS.LOGIN_USER,
            reload: false
        }
    );
}

async function callChangeLanguage(lang) {

}

async function callGetAllAdmins() {
    return await returnDataOnSuccessTemplate({
        url: APIS.USER_SERVICE.ALL_ADMINS,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.json());
}

async function callGetAllPlayers() {
    return await returnDataOnSuccessTemplate({
        url: APIS.USER_SERVICE.ALL_PLAYERS,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.json());
}

async function callGetAllGames() {
    return await returnDataOnSuccessTemplate({
        url: APIS.GAMEREPO_SERVICE.ALL_GAMES,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.json());
}

async function callDownloadClassUT(className) {
    return await returnDataOnSuccessTemplate({
        url: `${APIS.DOWNLOAD_CLASSUT}/${className}`,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.blob());
}

async function callDeleteClassUT(className) {
    await redirectOnSuccessTemplate({
        url: `${APIS.DELETE_OPPONENT}/${className}`,
        method: "DELETE",
        headers: { 'Content-Type': 'application/json' },
    },
    {
        reload: true
    });
}

async function callUploadOpponent(body) {
    return await returnDataOnSuccessTemplate({
        url: APIS.UPLOAD_OPPONENT,
        method: "POST",
        body: body
    }, async response => await response.json());
}

/**
 * Chiama l'endopoint per eliminare una scalata e ricarica la pagina in caso di successo.
 *
 * @param {string} name Il nome della scalata da eliminare.
 */
async function callDeleteScalata(name) {
    await redirectOnSuccessTemplate({
            url: `${APIS.SCALATA_SERVICE.DELETE_SCALATA}/${name}`,
            method: "DELETE",
            headers: { 'Content-Type': 'application/json' },
        },
        {
            reload: true
        });
}

/**
 * Recupera la lista degli opponents disponibili.
 *
 * @returns {Promise<Object[]|null>} Array di oggetti opponents o null in caso di errore.
 */
async function callGetOpponents() {
    return await returnDataOnSuccessTemplate({
        url: `${APIS.OPPONENTS_SERVICE.GETALL_OPPONENTS}`,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.json());
}

/**
 * Invia i dati per la creazione di una nuova scalata.
 *
 * @param {Object} data L'oggetto ScalataDTO da inviare.
 * @returns {Promise<Object|null>} I dati parsati della risposta o null in caso di errore.
 */
async function callCreateScalata(data) {
    await redirectOnSuccessTemplate(
        {
            url: APIS.SCALATA_SERVICE.CREATE_SCALATA,
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        },
        {
            // Il codice originale navigava a "/scalata/main"
            redirectTo: "/scalata/main",
            reload: false
        }
    );
}


