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

        // Handle new uniform error structure (ErrorResponseDTO)
        if (errorBody?.message) {
            // Show a more informative error message
            const errorTitle = errorBody?.error || 'Errore';
            const errorStatus = errorBody?.status ? ` (Codice: ${errorBody.status})` : '';
            
            // Format the message nicely, preserving line breaks
            const message = errorBody.message.replace(/\\n/g, '\n');
            
            // Create a more structured error display
            const fullMessage = `${errorTitle}${errorStatus}\n\n${message}`;
            
            $('#errorModalBody').text(message);
			$('#errorModal').modal('show');
            return;
        }

        // Handle validation errors with field-specific errors (ValidationErrorDTO)
        if (errorBody?.errors?.length > 0) {
            let errorMessages = [];
            errorBody.errors.forEach(err => {
                const container = document.getElementById(`${err.field}_label_container`);
                if (container) {
                    addErrorDiv(container, err.message);
                } else {
                    errorMessages.push(`• ${err.message}`);
                }
            });
            
            if (errorMessages.length > 0) {
                const errorTitle = errorBody?.message || 'Errori di validazione';
                $('#errorModalBody').text(`${errorMessages.join('\n')}`);
				$('#errorModal').modal('show');
            }
            return;
        }

        // Fallback for other error formats
        if (errorBody?.errorMessage) {
            $('#errorModalBody').text(errorMessage);
			$('#errorModal').modal('show');
            return;
        }

        // Generic fallback
        $('#errorModalBody').text(errors.notHandled);
		$('#errorModal').modal('show');
    } catch (e) {
        $('#errorModalBody').text('Errore durante la lettura del JSON.');
		$('#errorModal').modal('show');
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
        $('#errorModalBody').text(`Errore nella chiamata ${method} ${url}:`);
		$('#errorModal').modal('show');
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
        $('#errorModalBody').text(`Errore nella chiamata ${method} ${url}:`);
		$('#errorModal').modal('show');
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




