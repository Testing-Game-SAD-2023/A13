
# A13-new-feature/bugfix
**Componenti del gruppo**
- Andriy Korsun (M63001275)
- Giuseppe Laterza (M63001411)
- Luca D’Angelo (M63001453)
Questo branch della repository A13 contiene la versione aggiornata della web-app, con una nuova funzionalità che consente agli utenti di autenticarsi utilizzando il loro account Google e correzioni di alcuni bug segnalati.

**Novità Principali**
  - **Integrazione del Google Login:** Ora gli utenti possono autenticarsi utilizzando il loro account Google.
  - **Refactoring delle Pagine di Autenticazione:** Le pagine associate all'autenticazione sono state ristrutturate per migliorare la navigabilità, l'usabilità e le prestazioni complessive del processo di login.
  - **Bugfix e Ottimizzazioni:** Sono stati risolti alcuni bug e ottimizzate delle funzionalità per consentire il corretto funzionamento dell'applicazione.
## Indice
- [Funzionalità](#Funzionalità)

## Funzionalità
Andando nel dettaglio le principali funzionalità e bug affrontati in questa versione sottoforma di task sono:
- **T1: Google Login**
  - Integrata l'autenticazione Google per consentire agli studenti di accedere utilizzando i propri account Google.

- **T2: Bug fix Download delle classi da parte dell'amministratore**
  -  Correzione di un bug che causa un errore durante il download delle classi da parte degli amministratori.

- **T3: Bug fix Logout da parte del player**
  - Correzione della procedura di logout dei player dal proprio account.

- **T4: Bug fix Registrazione da parte del player**
    - Correzione della procedura di registrazione dei nuovi utenti.

- **T5: Refactoring**
  - Miglioramento della struttura delle pagine HTML, CSS e JavaScript dell'applicazione per una maggiore pulizia e organizzazione e restyling delle singole pagine.

- **T6: Verifica segnalazione cancellazione classi**
  - È stato controllato un problema segnalato riguardante la cancellazione delle classi, tuttavia è emerso che non esisteva un bug effettivo.

## Lista Modifiche
Per chiarezza è riportata una lista delle modifiche effettuate sul codice ed i relativi container Docker interessati.
| Container | Descrizione |
| --- | --- |
| T23 | Aggiunte le classi per il login con Google attraverso la libreria Spring Security e Spring Auth2. `SecurityConfig.java`, `OAuthUserGoogleService.java`,  `GoogleSuccessHandler.java`, `OAuthUserGoogle.java`|
| T23 | Aggiunta un servizio per semplificare le operazioni di creazione di un nuovo utente tramite le informazioni di Google: `SecurityConfig.java`|
| T23 | Restyling delle pagine HTML e CSS associati, refactoring ed ottimizzazione dei CSS, JS e HTML|
| T23 | Verifica e correzione del processo di registrazione, in particolare la funzione javascript eseguita dalla pagina dopo il submit, corretta gestione e visualizzazione dei messaggi di errore: `register.js`|
| T23 | Aggiunta del campo `isRegistredWithGoogle` nel DB MySQL. `UserRepository.java`, `User.java`|
| T23 | Modifica della richiesta POST di `/logout` per consentire la corretta cancellazione di tutti i cookie, della sessione http e del contesto di autenticazione di spring. `Controller.java`|
| T23 | Aggiunta degli endpoint per gestire il login con Google: `/loginWithGoogle`, controllo della conssessione con il servizio Google `/checkService`, controllo dell'esistenza della sessione `/checkSession. `Controller.java`|
| ui_gateway | Aggiunti gli endpoint nelle configurazioni: `downloadFile` per manvsclass per il fix del tasto DownloadClasse, `oauth2/authorization/google` e `validateToken` per T23, `register.js`|
| T5 | Modifica alla funzione `redirectToLogin()` in modo da interfacciarsi correttamente con la POST request di logout definita nel Controller. `main.js`|
| T6 | Refactoring HTML, CSS e js|
