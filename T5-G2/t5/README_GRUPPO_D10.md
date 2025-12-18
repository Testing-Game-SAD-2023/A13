# Progetto – Gruppo D10, Task R6

## 0) Descrizione del Task svolto e ID del Task

Questa versione del progetto implementa un **aggiornamento alla pagina di Profilo del Giocatore**, con l’obiettivo di rendere le informazioni più organiche e accessibili.

Nel sistema di partenza, le informazioni del profilo del giocatore erano distribuite su più componenti e alcune di esse venivano mostrate tramite pulsanti nel menù in alto a destra della home page, con dati principalmente mantenuti nel microservizio T23.  
Il task svolto prevede il **miglioramento della visualizzazione e dell’organizzazione delle informazioni** già presenti, consentendo al giocatore di consultare in modo centralizzato e strutturato:

- Immagine e biografia del giocatore
- Riepilogo delle statistiche personali (partite giocate, punti esperienza, badge guadagnati)
- Classifiche dei giocatori
- Storico delle partite (Game History)
- Lista dei follower e dei giocatori seguiti
- Follow/Unfollow di giocatori

(ID Task: R6)

---

## 1) Implementazione del Task

Il task è stato implementato aggiornando la pagina di Profilo esistente, rendendo più organica la visualizzazione dei dati e migliorando l’integrazione con i microservizi già presenti.  
Le informazioni del profilo, delle statistiche e delle interazioni sociali vengono ora aggregate e presentate in modo centralizzato, offrendo un’esperienza utente più chiara e intuitiva.

A livello architetturale, l’aggiornamento ha richiesto:

- **Riattivazione e aggiunta della logica di recupero delle informazioni** relative al profilo, alle statistiche, ai follower e allo storico delle partite.  
- **Impostazione di endpoint nel controller T5** in grado di intercettare le richieste provenienti dal front-end e inoltrarle ai servizi appropriati.  
- **Modifiche ai Service di T5** per esporre al modulo front-end le funzionalità necessarie a recuperare i dati dagli altri microservizi, con particolare attenzione a T23.  
- **Refactoring dei file lato front-end** per separare, dove possibile, la logica di visualizzazione dalla logica che gestisce le chiamate ai microservizi, migliorando manutenibilità e leggibilità del codice. Si è, inoltre, introdotta l'internazzionalizzazione del profilo. 


### Modifiche ai Microservizi

| Microservizio / Modulo            | Tipo di Modifica      | Note |
|----------------------------------|---------------------|----------------------------------|
| Achievement.html (T5)             | Eliminato           | Eliminazione pagina dedicata per insrimento Tab nella pagina profilo                                |
| GameHistory.html (T5)             | Eliminato           | Eliminazione pagina dedicata per insrimento Tab nella pagina profilo                                 |
| Leaderboard.html (T5)             | Eliminato           | Eliminazione pagina dedicata per insrimento Tab nella pagina profilo                                 |
| Navbar.html (T5)                  | Modificato          | Rimozione pulsanti Achievement, Leaderboard, Games che reindirizzavano a pagine dedicate                                |
| edit-profile.html (T5)            | Modificato          | Aggiunta di label per i campi da aggiornare, aggiunta input hidden per campo mail, aggiunta div per le notifiche per coerenza con js                                |
| profile.html (T5)                 | Modificato          | Aggiunta del Tab per Achievement, GameHistory, Leaderboard, modifica endpoint per funzionalità social, modifica recupero paramtri per coerenza con le informazini da reperire                                  |
| search.html (T5)                  | Modificato          | Aggiunta di metodi per autenticazione, modifica endopoint, modifica recupero paramtri per coerenza con le informazini da reperire, rimozione logica javascript superflua                                |
| Achievement.css (T5)              | Aggiunto            | Aggiornamento stile grafico                                |
| GameHistory.css (T5)              | Aggiunto            | Aggiornamento stile grafico                                |
| profile-edit.css (T5)             | Modificato          | Aggiornamento stile grafico                                |
| profile.css (T5)                  | Modificato          | Aggiornamento stile grafico                                |
| Achievement.js (T5)               | Aggiunto            | Logica javascript associata al nuovo componente Achievement da visualizzare nel profilo, internazionalizzazione                                |
| GameHistory.js (T5)               | Aggiunto            | Logica javascript associata al nuovo componente GameHisotry da visualizzare nel profilo, internazionalizzazione                                |
| Leaderboard.js (T5)               | Modificato          | Logica javascript associata al nuovo componente Leaderboard da visualizzare nel profilo                                |
| profile-edit.js (T5)              | Modificato          | Modifica dell'endopoint per funzioanlità update, modifica recupero paramtri per coerenza con le informazini da reperire, internazionalizzazione                                |
| language.js (T5)              | Modificato          | internazionalizzazione                                |
| T23Service.java (T5)              | Modificato          | Aggiunta/Aggiornamento dei metodi che richiamano funzionalità esposti dai vari microservizi per il recupero delle informazioni relativamente a Social, GameHistory, LeaderBoard, Achievement, Modifica Profilo                 |
| UserProfileComponent.java (T5)    | Modificato          | Modifica della action riferita per recupero dei dati del profilo utente a partire dall'id del profilo.                               |
| UserProfileController.java (T5)   | Modificato          | Aggiunta/Aggiornamento delle rotte per la gestione del recupero delle informazioni relativamente a Social, GameHistory, LeaderBoard, Achievement, Modifica Profilo.                                |
| messages_it.properties (T5)              | Modificato          | etichette internazionalizzazione                                |
| messages_en.properties (T5)              | Modificato          | etichette internazionalizzazione                                |
| messages_es.properties (T5)              | Modificato          | etichette internazionalizzazione                                |
| UserSocialController.java (T23)   | Modificato          | Aggiunta della rotta che gestisca il recupero delle informazioni utente a partire dall'id del profilo                                |
| PlayerService.java (T23)          | Modificato          | Aggiunta metodo per il recupero delle informazioni utente a partire dall'id del profilo                             |

