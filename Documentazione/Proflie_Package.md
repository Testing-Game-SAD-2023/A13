# Profile Package

**Path:** `T5-G2\t5\src\main\resources\static\t5\js\profile`

Il package `profile` contiene i moduli JavaScript responsabili della gestione lato client della pagina profilo.

![Struttura della cartella profile](https://i.imgur.com/Cq1SMZN.jpeg)
*Fig. 1: Struttura della directory profile*

## Architettura

Il modulo implementa il pattern **Modular**, utilizzando moduli ES6. La logica di controllo è centralizzata in `profileMain.js`, che gestisce le chiamate asincrone in risposta alle interazioni dell'utente, delegando l'esecuzione specifica ai moduli funzionali importati.

### Gestione delle Dipendenze
Il file principale importa le funzioni necessarie dai moduli `profileFixed.js`, `stats.js`, `matches.js` e `social.js`.

![Imports in profileMain.js](https://i.imgur.com/D7rPd9Z.png)
*Fig. 2: imports in profileMain.js*

### Event Handling
`profileMain.js` registra gli *event listeners* collegati agli elementi del DOM. Al trigger di un evento, viene invocata la funzione asincrona corrispondente definita nei moduli importati.

![Event Listeners in profileMain.js](https://i.imgur.com/O0INIwj.png)
*Fig. 3: parte degli event listeners*

## Componenti del Modulo

### profileMain.js
Agisce da entry point e gestisce la logica di navigazione interna della pagina (cambio sezione).

![Funzione cambio sezione](https://i.imgur.com/qEbn00m.png)
*Fig. 4: Logica di gestione del cambio sezione*

### Moduli Funzionali
I file seguenti contengono la logica di rendering e le operazioni specifiche per le sottosezioni del profilo:

* **`profileFixed.js`**: Gestisce la visualizzazione della colonna sinistra statica (informazioni persistenti dell'utente).
* **`stats.js`**: Contiene le funzioni per il rendering della sezione statistiche e la visualizzazione degli *achievements*.
* **`matches.js`**: Gestisce il recupero e la visualizzazione dello storico delle partite (Match History).

* **`social.js`**: Gestisce le funzionalità della sezione social.

