const rowsPerPage = 10;
const pageOffset = 5;
const tableBody = document.getElementById('table-body');
const pagination = document.getElementById('pagination');
let lastPage = 0;
let totalLength = 0; // #######################################
let cache = {}


const statisticOptions = {
    sfida: [
        { id: 'partite_giocate', label: 'Partite giocate' },
        { id: 'partite_vinte', label: 'Partite vinte' },
        { id: '', label: 'Classi testate' }
    ],
    scalata: [
        { id: 'statistica1', label: 'Esempio1' },
        { id: 'statistica2', label: 'Esempio2' },

    ]
};


// GET 
async function fetchRows(gamemode, statistic, startPage, endPage) {
    try {
        let startPos = (startPage - 1) * rowsPerPage + 1
        let endPos = endPage * rowsPerPage;
        const response = await fetch(`api/leaderboard/subInterval/${gamemode}/${statistic}/${startPos}/${endPos}`);
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
if X==1 prendo intervallo (X, X+pageOffset) // prima pagina -> prendo le y successive
else // pagina intermedia -> prendo un intorno di y pagine
    if X+1 è in cache prendo l'intervallo prima (X-pageOffset, X) (senza scendere sotto pagina 1, quindi max(1, x-pageOffset))
    elif X-1 è in cache prendo l'intervallo dopo (X, X+pageOffset) (senza eccedere oltre l'ultima pagina (la conosco?))
    else // se è una pagina "isolata"
        prendo l'intorno (X-floor(pageOffset/2), X+floor(pageOffset/2))         //es. se pageOffset==5 prendo intervallo (X-2, X+2)
*/

// fetch rows and update cache
async function getRows(gamemode, statistic, page) {
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
    let startPage, endPage;

    // define what page interval to fetch
    if (page == 1) {
        startPage = 1;
        endPage = startPage + pageOffset - 1;
    } else {    // pagina intermedia
        if (cache?.[gamemode]?.[statistic]?.[page + 1]) { // se la pagina x+1 è in cache, prendo le pagine precedenti 
            startPage = Math.max(1, page - pageOffset + 1); // es. page = 5, pageOffset = 5, prendo pagine 1,2,3,4,5
            // es. page = 2, pageOffset = 5, prendo pagine 1,2
            endPage = page;
        } else if (cache?.[gamemode]?.[statistic]?.[page - 1]) { // se la pagina x-1 è in cache, prendo le pagine successive 
            startPage = page;

            endPage = page + pageOffset - 1;  // ############## gestire totalPages 
        } else {
            offset = Math.floor(pageOffset / 2);
            startPage = Math.max(1, page - offset);
            endPage = page + offset;
        }
    }

    // fetch rows
    let fetchedRows
    try {
        fetchedRows = await fetchRows(gamemode, statistic, startPage, endPage);
    } catch (error) {
        throw (error);
    }
    const startRow = (startPage - 1) * rowsPerPage;

    if (!cache[gamemode]) {
        cache[gamemode] = {};
    }
    if (!cache[gamemode][statistic]) {
        cache[gamemode][statistic] = {}
    }

    // store fetched rows in cache
    for (let page = startPage; page <= endPage; page++) {
        const pageStartIndex = (page - 1) * rowsPerPage;
        cache[gamemode][statistic][page] = fetchedRows.slice(
            pageStartIndex - startRow,
            pageStartIndex - startRow + rowsPerPage
        );
    }
    console.log('Cache updated', cache);
}

// Rendering functions

function renderTable(gamemode, statistic, page) {

    tableBody.innerHTML = '';

    // rendering table rows
    cache[gamemode][statistic][page].forEach((row, i) => {
        const tr = document.createElement('tr');
        const position = (page - 1) * rowsPerPage + i + 1
        tr.innerHTML = `
          <td>${position}</td>
          <td>${row.email}</td>
          <td>${row.statistic}</td>
        `;
        tableBody.appendChild(tr);
    });
}

function renderPagination(currentPage) {

    pagination.innerHTML = '';

    if (totalLength === 0) {
        lastPage = 1
    }
    else {
        lastPage = Math.floor((totalLength - 1) / rowsPerPage) + 1
    }
    // "Page 1" button
    const firstButton = document.createElement('li');
    firstButton.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    firstButton.innerHTML = `
        <button class="page-link" onclick="loadPage(1)" aria-label="First">&laquo; First page</button>
      `;
    pagination.appendChild(firstButton);

    // "Previous" button
    const prevButton = document.createElement('li');
    prevButton.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prevButton.innerHTML = `
        <button class="page-link" onclick="loadPage(${currentPage - 1})" aria-label="Previous">&laquo; Previous</button>
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
        <button class="page-link" onclick="loadPage(${currentPage + 1})" aria-label="Next">Next &raquo;</button>
      `;
    pagination.appendChild(nextButton);

    // "Page last" button
    console.log(lastPage)
    const lastButton = document.createElement('li');
    lastButton.className = `page-item ${currentPage === lastPage ? 'disabled' : ''}`;
    lastButton.innerHTML = `
        <button class="page-link" onclick="loadPage(${lastPage})" aria-label="First">&laquo; Last page</button>
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

        const label = document.createElement('label');
        label.className = 'form-check-label';
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

    // add loadPage trigger on selection
    const statisticSelectors = document.querySelectorAll('input[name="statistic"]');
    statisticSelectors.forEach(selector => {
        selector.addEventListener('change', (event) => {
            if (event.target.checked) {
                loadPage(1);
            }
        });
    });
}

async function loadPage(page) {
    if (page < 1) return;

    const gamemode = document.querySelector('input[name="gamemode"]:checked').id;
    const statistic = document.querySelector('input[name="statistic"]:checked').id;

    // fetch page if not in cache
    if (!(cache?.[gamemode]?.[statistic]?.[page])) {
        try {
            await getRows(gamemode, statistic, page);
        } catch (error) {
            renderTableError(error);
            return;
        }
    }

    // render table and buttons
    renderTable(gamemode, statistic, page);
    renderPagination(page);
}



// Page initialization

// add listener for leaderboard creation
document.addEventListener('DOMContentLoaded', function() {
    const gamemode = document.querySelector('input[name="gamemode"]:checked').id;
    updateStatisticOptions(gamemode);

    const offcanvasElement = document.getElementById('offcanvasDarkNavbar');
    offcanvasElement.addEventListener('show.bs.offcanvas', function() {
        loadPage(1);
    });
});


// add listeners on gamemode selectors
const gamemodeSelectors = document.querySelectorAll('input[name="gamemode"]');

gamemodeSelectors.forEach(selector => {
    selector.addEventListener('change', (event) => {
        if (event.target.checked) {

            updateStatisticOptions(event.target.id);
            loadPage(1);
        }
    });
});
