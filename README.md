
# A13-new-language-support
**Componenti del gruppo**
- Andriy Korsun (M63001275)
- Giuseppe Laterza (M63001411)
- Luca D’Angelo (M63001453)


Questo branch della repository A13 basato sul branch 'new-feature/bugfix' aggiunge il supporto all'internazionalizzazione (i18n) all'applicazione Spring Boot, consentendo all'applicazione di supportare le lingue inglese e italiano. Gli utenti possono passare da una lingua all'altra e le loro preferenze vengono memorizzate tra le sessioni. Tutte le modifiche apportate sono riguardanti unicamente il container **T23** che gestisce i servizi di autenticazione.

## Funzionalità principali

- **Cambio Lingua**: Gli utenti possono passare dall'inglese all'italiano utilizzando i tasti disponibili nel footer delle pagine.
- **Persistenza della Lingua**: La lingua impostata dall'utente viene memorizzata per persistere tra le sessioni.
- **Integrazione Thymeleaf**: Viene utilizzato Thymeleaf facilitare la gestione della lingua appropriata.

## Dettagli di implementazione
Spring Boot supporta nativamente la creazione di applicazioni multilingue. Il framework adatta automaticamente i testi e i messaggi all'utente in base alla lingua e alla regione specificate.
Per fare ciò bisogna configurare le risorse di lingua, dei file properties, che contengono le traduzioni dei testi. Questi file vengono organizzati per lingua, nel nostro caso 'messages_en.properties' per l'inglese e 'messages_it.properties' per l'italiano.
Di seguito una lista dei componenti aggiunti per la corretta localizzazione delle pagine.
- **Locale Resolver**: il `LocaleResolver` è un componente di Spring che determina la lingua corrente dell'applicazione. Configurando un LocaleResolver, è possibile specificare quale lingua utilizzare in base alle preferenze dell'utente.
- **LocaleChangeInterceptor**: il `LocaleChangeInterceptor` è un componente che rileva le richieste HTTP e cambia la lingua dell'applicazione in base a un parametro specificato nell'URL. Questo consente agli utenti di cambiare la lingua dell'interfaccia utente dinamicamente, ad esempio nel nostro caso con la lingua inglese l'URL sarà `localhost/login?lang=en`.


Per la gestione dei testi tradotti nelle pagine web viene utilizzato Thymeleaf. Sono caricati i testi in base alla lingua selezionata dall'utente utilizzando i file di configurazione delle traduzione.
Nei file'messages_en.properties' e 'messages_it.properties' cono contenute delle coppie chiavi e i valori dei testi tradotti.
```
button.register=Registrati
button.google=Accedi con Google
button.login=Accedi
button.log_admin=Accedi come Amministratore
button.log_student=Accedi come Studente
login.title=Accesso Studenti
login.text=Non sei uno studente?
...
```
Nei template Thymeleaf, utilizziamo le chiavi dei messaggi per visualizzare il testo tradotto.
```html
<div class="left-container">
  <h1 th:text="#{login.title}"></h1>
    <p th:text="#{login.text}"></p>
      <a th:text="#{button.log_admin}" href="/loginAdmin" class="button"></a>
        <div class="side-image">
          <img th:src="@{t23/css/images/logo_blob.png}" alt="Login Image">
        </div>
</div>
```
Ad esempio, `th:text="#{login.title}"` comunica a Thymeleaf di cercare il valore associato alla chiave `login.title` nel file di risorse corrispondente alla lingua attualmente selezionata. Se la lingua è cambiata, Thymeleaf aggiornerà automaticamente il testo visualizzato con la traduzione appropriata.

Per permettere agli utenti di cambiare la lingua, è stato implementato uno script JavaScript. L'URL della pagina con un parametro che indica la lingua selezionata, e poi ricarica la pagina.
```javascript
function changeLanguage(lang) {
    const url = new URL(window.location.href);
    url.searchParams.set('lang', lang);
    window.location.href = url.toString();
}

document.getElementById('en-flag').addEventListener('click', function() {
    changeLanguage('en');
});

document.getElementById('it-flag').addEventListener('click', function() {
    changeLanguage('it');
});
```


# A13-newfeature/bugfix

**Novità Principali**
  - **Integrazione del Google Login:** Ora gli utenti possono autenticarsi utilizzando il loro account Google.
  - **Refactoring delle Pagine di Autenticazione:** Le pagine associate all'autenticazione sono state ristrutturate per migliorare la navigabilità, l'usabilità e le prestazioni complessive del processo di login.
  - **Bugfix e Ottimizzazioni:** Sono stati risolti alcuni bug e ottimizzate delle funzionalità per consentire il corretto funzionamento dell'applicazione.

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
| T23 | Aggiunta degli endpoint per gestire il login con Google: `/loginWithGoogle`, controllo della conssessione con il servizio Google `/checkService`, controllo dell'esistenza della sessione `/checkSession` in `Controller.java`|
| ui_gateway | Aggiunti gli endpoint nelle configurazioni: `downloadFile` per manvsclass per il fix del tasto DownloadClasse, `oauth2/authorization/google` e `validateToken` per T23, `register.js`|
| T5 | Modifica alla funzione `redirectToLogin()` in modo da interfacciarsi correttamente con la POST request di logout definita nel Controller. `main.js`|
| T6 | Refactoring HTML, CSS e js|

## Modalità di utilizzo 

Per poter utilizzare il social login con Google è necessario ottenere le credenziali sviluppatore OAuth2 (Client ID and Client Secret) su Google Cloud Console (https://console.cloud.google.com) per permettere all’applicazione di comunicare con l’API Google.

- Nella sezione 'Credenziali' bisogna creare delle nuove credenziali ID Client OAuth<br>

![Github_1](https://github.com/Testing-Game-SAD-2023/A13/assets/64073539/1416daac-9225-4050-8ce9-bc04db1a3388)
- Una volta creata l'applicazione è possibile visualizzare il ClientID ed il ClientSecret <br>

![GoogleAPI](https://github.com/Testing-Game-SAD-2023/A13/assets/64073539/ccb62797-2294-4e1e-9c8f-8378aa96fa7e)
- E' necessario inoltre inserire gli URI di reindirizzamento corretti, nel nostro caso 'http://localhost/login/oauth2/code/google' se si sta utilizzando l'app su Docker mentre 'http://localhost:8080/login/oauth2/code/google' nel caso si sta facendo un test in locale.
- Una volta ottenute le credenziali, si devono inserire nei campi corrispondenti all'interno del file 'docker-compose.yml' nella cartella del container T23. <br>

![Github2](https://github.com/Testing-Game-SAD-2023/A13/assets/64073539/3bfba838-3e2b-45a4-bd2d-7dc837a1425c)
## Login con Google video dimostrativo

https://github.com/Testing-Game-SAD-2023/A13/assets/64073539/2a7d5c4b-1664-4769-8ddd-5ec660634d75

