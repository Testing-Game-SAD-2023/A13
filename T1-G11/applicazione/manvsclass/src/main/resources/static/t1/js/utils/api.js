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

async function callGetHints(queryParams) {
    const url = `${APIS.GET_HINTS}?${queryParams}`;
    return await returnDataOnSuccessTemplate({
        url: url,
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.json());
}

async function callUploadHints(formData) {
    // Non passiamo Content-Type, il browser lo imposta correttamente con boundary per FormData
    return await returnDataOnSuccessTemplate({
        url: APIS.UPLOAD_HINTS,
        method: "POST",
        body: formData // FormData è gestito dal browser
    }, async response => await response.text());
}



