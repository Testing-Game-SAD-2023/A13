# T5/6 - User Experience

*<b>Versione:</b> A13.2*

Il task T5 si occupa di fornire un'interfaccia utilizzabile da un giocatore per poter configurare e avviare partite, ma anche quello di ottenere informazioni sintetiche riguardo il suo storico partite (e quello di altri player) così come il progresso dei vari [achievement](/game/achievements).

## Specifiche dei requisiti

Il servizio permette ai **player** correttamente autenticati di: 

a. accedere all’area riservata di **selezione dei parametri di gioco** dove poter visualizzare le classi ed i robot disponibili precedentemente caricati dagli amministratori *(A13.1)*;

b. **avviare una partita** accedendo all’arena di gioco vero e proprio *(A13.1)*;

c. **visualizzare i profili utente** tramite apposite pagine che racchiudono informazioni sintetiche sullo storico di gioco degli utenti *(A13.2)*;

Il **sistema** si occuperà inoltre di:

d. **aggiornare lo stato delle statistiche** degli utenti ogni volta che viene salvato lo stato di una partita *(A13.2)*.

<img src="_assets/media/diagrams/t5_ucd.png" style="width: 35vw; display: block; margin-left: auto; margin-right: auto;">

## Pagina del Profilo

La **pagine del profilo** è stata pensata per fornire delle informazioni sommarie su un player iscritto all'applicazione. Si tratta di un'interfaccia suddivisa in tre sezioni:

* **Informazioni personali:** contiene lo username, nome e cognome dell'utente e attualmente un *mock* di biografia e immagine del profilo dell'utente.

* **Statistiche:** contiene tutte le [statistiche](/game/achievements) configurate dagli utenti amministratore, insieme al valore di progresso attuale per l'utente che si sta visualizzando.

* **Trofei:** contiene tutti gli achievement configurati dagli amministratori, insieme ai loro valori di progresso per l'utente che si sta visualizzando.

<img src="_assets/media/profile.png">

## API

<style>
    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        border: 1px solid black;
        padding: 8px;
        text-align: left;
        max-width: 20vw;
    }

    th:first-child, td:first-child {
        max-width: 3vw;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    tbody{
        width: 100%;
        display: table;
    }

    .get {
        background: #1486b8;
        font-weight: bold;
    }

    .post {
        background: #eb4034;
        font-weight: bold;
    }
</style>

<table>
    <tr>
        <th>Tipo</th>
        <th>URL</th>
        <th>Descrizione</th>
        <th>Parametri richiesti</th>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/main</td>
        <td>Restituisce la pagina principale.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/profile</td>
        <td>Restituisce la pagina di profilo utente.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/gamemode</td>
        <td>Restituisce la pagina di selezione modalità.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/report</td>
        <td>Restituisce la pagina di report.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/editor</td>
        <td>Restituisce la pagina di editor.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/editorAllenamento</td>
        <td>Restituisce la pagina di editor allenamento.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/"/gamemode_scalata</td>
        <td>Restituisce la pagina di selezione della Scalata.</td>
        <td>Nessuno</td>
    </tr>
    <tr>
        <td class="post">POST</td>
        <td>/save-scalata</td>
        <td>Effettua il salvataggio di una partita di modalità Scalata.</td>
        <td>
            <ul>
                <li><b><i>int</i> playerID:</b> id utente</li>
                <li><b><i>String</i> scalataName:</b> il nome della scalata</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td class="post">POST</td>
        <td>/save-data</td>
        <td>Effettua il salvataggio di una partita e aggiorna le statistiche del giocatore.</td>
        <td>
            <ul>
                <li><b><i>int</i> playerID:</b> id utente</li>
                <li><b><i>String</i> robot:</b> il robot contro cui si sta giocando</li>
                <li><b><i>String</i> classe:</b> la classe su cui ci si sfida</li>
                <li><b><i>String</i> difficulty:</b> la difficoltà della partita</li>
                <li><b><i>String</i> gamemode:</b> la modalità della partita</li>
                <li><b><i>String</i> username:</b> il nome utente del giocatore</li>
                <li><b><i>(opt.) int</i> selectedScalata:</b> l'id dell'eventuale scalta corrispondente</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td class="get">GET</td>
        <td>/leaderboardScalata</td>
        <td>Restituisce la leaderboard delle partite con modalità Scalata.</td>
        <td>Nessuno</td>
    </tr>
</table>