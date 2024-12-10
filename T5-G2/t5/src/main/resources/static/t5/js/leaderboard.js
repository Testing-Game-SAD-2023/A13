const pageSize = 10;
const numPages = 5;
const tableBody = document.getElementById('table-body');
const pagination = document.getElementById('pagination');
let lastPage = Infinity;
let totalLength = 0; // #######################################
let cache = {}


const statisticOptions = {
    sfida: [
        { id: 'partite_giocate', label: 'Partite giocate' },
        { id: 'partite_vinte', label: 'Partite vinte' },
        { id: '', label: 'Classi testate', disabled: true }
    ],
    scalata: [
        { id: 'statistica1', label: 'Esempio1' },
        { id: 'statistica2', label: 'Esempio2' },

    ]
};


async function fetchRows(gamemode, statistic, startPage, email) {
    try {
        const endpoint = `api/leaderboard/subInterval/${gamemode}/${statistic}`;
        let queryParams = `?pageSize=${pageSize}&numPages=${numPages}`;
        if (startPage > 0)
            queryParams = queryParams.concat(`&startPage=${startPage}`);
        else if (email.length > 0)
            queryParams = queryParams.concat(`&email=${email}`);

        const response = await fetch(`${endpoint}${queryParams}`);
        const responseJson = await response.json();

        if (!response.ok) {
            message = responseJson['message'];
            if (message)
                throw new Error(message);
            else
                //throw new Error(`HTTP error! Status: ${response.status}`);
                throw new Error('Errore nel caricamento dei dati :(');
        }

        // validazione
        if (!responseJson
            || typeof responseJson !== 'object'
            || !Array.isArray(responseJson.positions)) {
            //|| !Number.isInteger(responseJson.totalLength)) {
            throw new Error('Errore nel caricamento dei dati :(');
        }

        rows = responseJson["positions"];
        totalLength = responseJson["totalLength"]

        return rows;
    }
    catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
}

/*
Logica di fetching

Si chiede la pagina X che non è in cache:
if X==1 prendo intervallo (X, X+numPages) // prima pagina -> prendo le y successive
else // pagina intermedia -> prendo un intorno di y pagine
    if X+1 è in cache prendo l'intervallo prima (X-numPages, X) (senza scendere sotto pagina 1, quindi max(1, x-numPages))
    elif X-1 è in cache prendo l'intervallo dopo (X, X+numPages) (senza eccedere oltre l'ultima pagina (la conosco?))
    else // se è una pagina "isolata"
        prendo l'intorno (X-floor(numPages/2), X+floor(numPages/2))         //es. se numPages==5 prendo intervallo (X-2, X+2)
*/

// fetch pages and update cache
async function getRowsByPage(gamemode, statistic, page) {

    let startPage;
    let fetchedRows;

    // loading spinner
    tableBody.innerHTML = `
      <tr>
        <td colspan="4" class="text-center">
            <div class="spinner-grow text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </td>
      </tr>
    `;

    // define what page interval to fetch
    if (page == 1) {
        startPage = 1;
    } else {    // pagina intermedia
        if (cache?.[gamemode]?.[statistic]?.[page + 1]) { // se la pagina x+1 è in cache, prendo le pagine precedenti
            startPage = Math.max(1, page - numPages + 1);
        } else if (cache?.[gamemode]?.[statistic]?.[page - 1]) { // se la pagina x-1 è in cache, prendo le pagine successive 
            startPage = page;
        } else {
            offset = Math.floor(numPages / 2);
            startPage = Math.max(1, page - offset);
        }
    }

    // fetch rows
    try {
        fetchedRows = await fetchRows(gamemode, statistic, startPage);
    } catch (error) {
        throw (error);
    }

    // set last page
    lastPage = totalLength === 0 ? 1 : Math.floor((totalLength - 1) / pageSize) + 1;

    // store fetched rows in cache
    updateCache(gamemode, statistic, startPage, fetchedRows);
}

async function getRowsByEmail(gamemode, statistic, email) {
    if (email.length == 0)
        return

    let fetchedRows;

    // loading spinner
    tableBody.innerHTML = `
        <tr>
          <td colspan="4" class="text-center">
              <div class="spinner-grow text-primary" role="status">
                  <span class="visually-hidden">Loading...</span>
              </div>
          </td>
        </tr>
      `;

    // fetch rows
    try {
        fetchedRows = await fetchRows(gamemode, statistic, 0, email);
    } catch (error) {
        throw (error);
    }

    // set last page
    lastPage = totalLength === 0 ? 1 : Math.floor((totalLength - 1) / pageSize) + 1;

    // store fetched rows in cache
    const startPage = Math.floor(fetchedRows[0].rank / pageSize) + 1 // ##################
    updateCache(gamemode, statistic, startPage, fetchedRows);

    // return page to be shown
    return startPage;
}

function updateCache(gamemode, statistic, startPage, fetchedRows) {
    if (!cache[gamemode]) {
        cache[gamemode] = {};
    }
    if (!cache[gamemode][statistic]) {
        cache[gamemode][statistic] = {}
    }

    const startRow = (startPage - 1) * pageSize;
    let endPage = Math.min(startPage + numPages - 1, lastPage)

    for (let page = startPage; page <= endPage; page++) {
        const pageStartIndex = (page - 1) * pageSize;
        const pageSlice = fetchedRows.slice(
            pageStartIndex - startRow,
            pageStartIndex - startRow + pageSize
        );
        if (pageSlice.length == 0)
            break;
        cache[gamemode][statistic][page] = pageSlice
    }

    console.log('Cache updated', cache);
}

// Rendering functions

function renderTable(gamemode, statistic, page) {

    tableBody.innerHTML = '';

    // rendering table rows
    cache[gamemode][statistic][page].forEach((row, i) => {
        const tr = document.createElement('tr');
        const position = (page - 1) * pageSize + i + 1
        tr.innerHTML = `
          <td class="text-center">${position}</td>
          <td>${row.email}</td>
          <td>${row.statistic}</td>
        `;
        tableBody.appendChild(tr);
    });
}

function renderPagination(currentPage) {

    pagination.innerHTML = '';

    // "Page 1" button
    const firstButton = document.createElement('li');
    firstButton.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    firstButton.innerHTML = `
        <button class="page-link" onclick="showPage(1)" aria-label="First">&laquo; First page</button>
      `;
    pagination.appendChild(firstButton);

    // "Previous" button
    const prevButton = document.createElement('li');
    prevButton.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prevButton.innerHTML = `
        <button class="page-link" onclick="showPage(${currentPage - 1})" aria-label="Previous">&laquo; Previous</button>
      `;
    pagination.appendChild(prevButton);

    // Current page button
    const currentPageButton = document.createElement('li');
    currentPageButton.className = 'page-item active';
    currentPageButton.innerHTML = `
        <button class="page-link">${currentPage}</button>
      `;
    pagination.appendChild(currentPageButton);

    // "Next" button
    const nextButton = document.createElement('li');
    nextButton.className = `page-item ${currentPage === lastPage ? 'disabled' : ''}`;
    nextButton.innerHTML = `
        <button class="page-link" onclick="showPage(${currentPage + 1})" aria-label="Next">Next &raquo;</button>
      `;
    pagination.appendChild(nextButton);

    // "Page last" button
    const lastButton = document.createElement('li');
    lastButton.className = `page-item ${currentPage === lastPage ? 'disabled' : ''}`;
    lastButton.innerHTML = `
        <button class="page-link" onclick="showPage(${lastPage})" aria-label="First">&laquo; Last page</button>
      `;
    pagination.appendChild(lastButton);
}

function renderTableError(error) {
    tableBody.innerHTML = '';

    const tr = document.createElement('tr');
    const td = document.createElement('td');
    const span = document.createElement('span');
    span.className = 'tableError';
    span.textContent = `${error.message}`;

    td.setAttribute('colspan', '100%');
    td.setAttribute('style', 'text-align:center')
    td.appendChild(span);

    tr.appendChild(td);
    tableBody.appendChild(tr);
}


// Render statistic options based on the selected gamemode
function updateStatisticOptions(gamemode) {
    const container = document.getElementById('statistic-options');
    container.innerHTML = '';

    // render selectors
    statisticOptions[gamemode].forEach(option => {
        const optionDiv = document.createElement('div');
        optionDiv.className = 'form-check';

        const input = document.createElement('input');
        input.className = 'form-check-input statistic';
        input.type = 'radio';
        input.name = 'statistic';
        input.id = option.id;
        input.value = option.label;
        if (option?.disabled == true)
            input.disabled = true;

        const label = document.createElement('label');
        label.className = 'form-check-label btn btn-filter';
        label.htmlFor = option.id;
        label.textContent = option.label;

        optionDiv.appendChild(input);
        optionDiv.appendChild(label);
        container.appendChild(optionDiv);
    });

    // Select the first option by default
    if (container.firstChild) {
        container.firstChild.querySelector('input').checked = true;
    }

    // add showPage trigger on selection
    const statisticSelectors = document.querySelectorAll('input[name="statistic"]');
    statisticSelectors.forEach(selector => {
        selector.addEventListener('change', (event) => {
            if (event.target.checked) {
                const email = document.getElementById('lb-search-box');
                email.value = "";
                showPage(1);
            }
        });
    });
}

// funzioni esterne
async function showPage(page) {
    if (page < 1) return;

    const gamemode = document.querySelector('input[name="gamemode"]:checked').id;
    const statistic = document.querySelector('input[name="statistic"]:checked').id;

    // fetch page if not in cache
    if (!(cache?.[gamemode]?.[statistic]?.[page])) {
        try {
            await getRowsByPage(gamemode, statistic, page);
        } catch (error) {
            renderTableError(error);
            return;
        }
    }

    // render table and buttons
    renderTable(gamemode, statistic, page);
    renderPagination(page);
}

async function searchPlayer() {
    const email = document.getElementById('lb-search-box').value;
    if (email.length == 0)
        return

    const gamemode = document.querySelector('input[name="gamemode"]:checked').id;
    const statistic = document.querySelector('input[name="statistic"]:checked').id;
    let pageToShow = 0;

    // cache look up first
    const foundCondition = (e) => e === email;

    outer: for (const page in cache[gamemode][statistic]) {
        for (const value of cache[gamemode][statistic][page]) {
            if (foundCondition(value.email)) {
                pageToShow = parseInt(page);
                break outer;
            }
        }
    }

    // fetch if not found in cache
    if (pageToShow < 1) {
        try {
            pageToShow = await getRowsByEmail(gamemode, statistic, email);
        } catch (error) {
            renderTableError(error);
            return;
        }}

    // render table and buttons
    renderTable(gamemode, statistic, pageToShow);
    renderPagination(pageToShow);
}



// Page initialization

// add listener for leaderboard creation
document.addEventListener('DOMContentLoaded', function () {
    const gamemode = document.querySelector('input[name="gamemode"]:checked').id;
    updateStatisticOptions(gamemode);

    const offcanvasElement = document.getElementById('offcanvasDarkNavbar');
    offcanvasElement.addEventListener('show.bs.offcanvas', function () {
        showPage(1);
    });
});


// add listeners on gamemode selectors
const gamemodeSelectors = document.querySelectorAll('input[name="gamemode"]');

gamemodeSelectors.forEach(selector => {
    selector.addEventListener('change', (event) => {
        if (event.target.checked) {
            const email = document.getElementById('lb-search-box');
            email.value = "";
            updateStatisticOptions(event.target.id);
            showPage(1);
        }
    });
});
