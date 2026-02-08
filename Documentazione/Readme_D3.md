# README - Gruppo D3 - Task R6

## 0) Descrizione del Task sviluppato
Il Task R6 consiste nella trasformazione della Pagina profilo utente, evolvendo l'architettura esistente sia a livello di logica che di interfaccia utente (UI). Il sistema permette ora la visualizzazione dinamica di statistiche (XP, Rank, Achievement), dello storico partite e la gestione delle funzionalità social (Follow/Unfollow e ricerca giocatori) senza dover ricaricare la pagina.

## 1) Implementazione del task
### Descrizione Task
Il task è stato implementato adottando un approccio modulare nel frontend (T5) e potenziando i servizi di backend (T23) per la persistenza dei dati.

### Elenco delle principali modifiche
| Microservizio Modificato | Tipo di Modifica |
| :--- | :--- |
| **T5 (Frontend)** | Sostituzione di `profile.html` |
| **T5 (Frontend)** | Integrazione di modulo JS `social.js` per la gestione chiamate per follower/following, la logica di ricerca utenti e l'aggiornamento dello stato del follow.|
| **T5 (Frontend)** | Integrazione di modulo JS `matches.js` per la gestione delle chiamate asincrone e del rendering dinamico dello storico delle partite giocate. |
| **T5 (Frontend)** | Integrazione di modulo JS `stats.js` per il recupero e la visualizzazione dei progressi di gioco, XP accumulati e achievement ottenuti. |
| **T5 (Frontend)** | Integrazione di modulo JS `profileFixed.js` per la gestione della persistenza e dell'aggiornamento dei dati anagrafici (nickname, bio, avatar). |
| **T5 (Controller)** | Aggiunti endpoint in `UserProfileController` per la gestione delle rotte asincrone verso il microservizio T23. |
| **T23 (Backend)** | Implementazione di `GameProgressService.java` per logica di recupero dello storico completo delle partite di un giocatore.  |

## 2) Errori/problematiche non risolte
**Bug nel caricamento storico partite**: Si è riscontrata un'anomalia nel caricamento dei dettagli della partita qualora l'ID dell'avversario coincida con l'ID dell'utente (stesso giocatore). In questa specifica condizione, il recupero dei dati della partita non va a buon fine.
