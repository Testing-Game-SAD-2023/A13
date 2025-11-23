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
        url: APIS.DOWNLOAD_CLASSUT(className),
        method: "GET",
        headers: { 'Content-Type': 'application/json' }
    }, async response => await response.blob());
}

async function callDeleteClassUT(className) {
    await redirectOnSuccessTemplate({
            url: APIS.DELETE_OPPONENT(className),
            method: "DELETE",
            headers: { 'Content-Type': 'application/json' },
        },
        {
            reload: true
        });
}

async function callDeleteSuggestion(className, suggestionTitle) {

    const response = await fetch(APIS.DELETE_SUGGESTION(className, suggestionTitle), {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    });

    if (!response.ok) {
        throw new Error(`Errore HTTP ${response.status}: ${response.statusText}`);
    }

    return true;
}

async function callUploadOpponent(body) {
    return await returnDataOnSuccessTemplate({
        url: APIS.UPLOAD_OPPONENT,
        method: "POST",
        body: body
    }, async response => await response.json());
}

async function callUploadGuidelines(guideLines) {

    try {
        const response = await fetch(APIS.UPLOAD_GUIDELINES, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify( {
                guidelines: guideLines
            })
        });
        if (!response.ok) {
            throw new Error(`Errore HTTP ${response.status}: ${response.statusText}`);
        }

        return await response.text();

    } catch (error) {
        console.error("Errore in Guidelines:", error);
        alert("Errore durante l'upload delle linee guida.");
        throw error;
    }
}
async function callGetGuidelines() {
    try {

        const response = await fetch(APIS.GET_GUIDELINES,  {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`Errore HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Errore recupero linee guida:", error);
        return [];
    }
}

async function callDeleteGuideline(title) {
    try {
        const url = APIS.DELETE_GUIDELINE(title);

        const response = await fetch(url, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
            throw new Error("Errore durante l'eliminazione");
        }
        return true;
    } catch (error) {
        console.error("Errore delete guideline:", error);
        throw error;
    }
}

async function callUploadSuggestions(className, suggestionsData) {
    try {
        const response = await fetch(APIS.UPLOAD_SUGGESTIONS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                className,
                suggestions: suggestionsData
            })
        });

        if (!response.ok) {
            throw new Error(`Errore HTTP ${response.status}: ${response.statusText}`);
        }

        return await response.text();

    } catch (error) {
        console.error("Errore in callUploadSuggestions:", error);
        alert("Errore durante l'upload dei suggerimenti.");
        throw error;
    }
}


async function callGetSuggestions(className) {
    try {

        const url = APIS.GET_SUGGESTIONS(className);

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Errore HTTP ${response.status}: ${response.statusText}`);
        }

        return await response.json();

    } catch (error) {

        console.error(`Errore durante il recupero dei suggerimenti per ${className}:`, error);
        throw error;
    }
}
