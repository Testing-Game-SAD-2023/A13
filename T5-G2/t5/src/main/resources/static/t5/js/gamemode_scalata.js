/**
 * gamemode_scalata.js
 * Gestisce la selezione e visualizzazione delle scalate disponibili
 */

// Variabile globale per tenere traccia della scalata selezionata
let selectedScalata = null;

// Quando il documento è pronto
$(document).ready(function() {
    console.log("[gamemode_scalata] Inizializzazione pagina");
    
    // Recupera le scalate dal model Thymeleaf (passato dal backend)
    const scalateData = window.available_scalate;
    
    if (!scalateData || scalateData.length === 0) {
        console.warn("[gamemode_scalata] Nessuna scalata disponibile");
        displayNoScalateMessage();
        return;
    }
    
    console.log("[gamemode_scalata] Scalate caricate:", scalateData);
    renderScalate(scalateData);
    
    // Event listener per il bottone submit
    $('#submit-button').on('click', function() {
        if (selectedScalata) {
            console.log("[gamemode_scalata] Scalata selezionata:", selectedScalata);
            startScalata(selectedScalata);
        } else {
            swal({
                title: "Attenzione!",
                text: "Seleziona una scalata prima di procedere",
                icon: "warning",
                button: "OK"
            });
        }
    });
});

/**
 * Renderizza le card delle scalate nel container
 */
function renderScalate(scalate) {
    const container = $('#scalate-container');
    container.empty();
    
    scalate.forEach(function(scalata) {
        const card = createScalataCard(scalata);
        container.append(card);
    });
}

/**
 * Crea una card HTML per una scalata
 */
function createScalataCard(scalata) {
    const cardHtml = `
        <div class="card scalata-card mb-3" data-scalata-name="${scalata.scalataName}">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h4 class="card-title">${scalata.scalataName}</h4>
                        <p class="card-text">${scalata.scalataDescription || 'Nessuna descrizione disponibile'}</p>
                        <p class="card-text">
                            <small class="text-muted">
                                Creata da: ${scalata.username || 'Sconosciuto'} | 
                                Numero di livelli: ${scalata.numberOfLevels || 0}
                            </small>
                        </p>
                    </div>
                    <div class="col-md-4 d-flex align-items-center justify-content-center">
                        <button class="btn btn-primary btn-select-scalata" data-scalata-name="${scalata.scalataName}">
                            Seleziona
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    const $card = $(cardHtml);
    
    // Event listener per il bottone di selezione
    $card.find('.btn-select-scalata').on('click', function() {
        const scalataName = $(this).data('scalata-name');
        selectScalata(scalataName, scalata);
    });
    
    return $card;
}

/**
 * Gestisce la selezione di una scalata
 */
function selectScalata(scalataName, scalataData) {
    console.log("[gamemode_scalata] Selezione scalata:", scalataName);
    
    // Rimuovi la classe selected da tutte le card
    $('.scalata-card').removeClass('selected');
    
    // Aggiungi la classe selected alla card cliccata
    $(`.scalata-card[data-scalata-name="${scalataName}"]`).addClass('selected');
    
    // Aggiorna il testo dei bottoni
    $('.btn-select-scalata').text('Seleziona');
    $(`.btn-select-scalata[data-scalata-name="${scalataName}"]`).text('Selezionata ✓');
    
    // Salva la scalata selezionata
    selectedScalata = scalataData;
}

/**
 * Avvia la scalata selezionata
 */
function startScalata(scalata) {
    console.log("[gamemode_scalata] Avvio scalata:", scalata);
    
    // Salva le informazioni della scalata nel localStorage
    localStorage.setItem('scalataName', scalata.scalataName);
    localStorage.setItem('scalataId', scalata.scalataName); // Usiamo il nome come ID
    localStorage.setItem('numberOfLevels', scalata.numberOfLevels);
    localStorage.setItem('currentLevel', 1); // Inizia dal livello 1
    
    // TODO: Recuperare il primo livello della scalata da T1
    // Per ora mostriamo un messaggio
    swal({
        title: "Scalata selezionata!",
        text: `Hai selezionato: ${scalata.scalataName}\nNumero di livelli: ${scalata.numberOfLevels}`,
        icon: "success",
        button: "Inizia!"
    }).then(() => {
        // TODO: Reindirizzare all'editor con il primo livello
        // Per ora torniamo alla main
        console.log("[gamemode_scalata] Reindirizzamento all'editor...");
        // window.location.href = "/editor?mode=Scalata&ClassUT=...";
        alert("Funzionalità in sviluppo: reindirizzamento all'editor con il primo livello della scalata");
    });
}

/**
 * Mostra un messaggio quando non ci sono scalate disponibili
 */
function displayNoScalateMessage() {
    const container = $('#scalate-container');
    container.html(`
        <div class="alert alert-warning text-center" role="alert">
            <h4 class="alert-heading">Nessuna scalata disponibile</h4>
            <p>Al momento non ci sono scalate configurate. Contatta l'amministratore per creare nuove scalate.</p>
            <hr>
            <p class="mb-0">
                <a href="/main" class="btn btn-primary">Torna alla Home</a>
            </p>
        </div>
    `);
    $('#submit-button').hide();
}
