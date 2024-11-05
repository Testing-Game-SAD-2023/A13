**PageBuilder** è uno strumento innovativo progettato per semplificare la creazione e la gestione delle pagine web in un'applicazione Java Spring. Questa componente funge da ponte tra i vari componenti dell'interfaccia utente e i servizi backend, permettendo una costruzione dinamica e modulare delle pagine. Grazie alla sua architettura flessibile, PageBuilder consente agli sviluppatori di progettare esperienze utente interattive e reattive in modo efficiente.

## Caratteristiche principali


* Struttura Dinamica delle Pagine: PageBuilder consente di assemblare pagine web in modo dinamico, permettendo l'aggiunta di componenti in base alle necessità della speficifica situazione in cui si trova la pagina.
* Integrazione con Componenti: Supporta l'integrazione di diversi componenti, sia per la visualizzazione dei dati che per la logica di business, facilitando l'organizzazione del codice.
* Utilizzo di Thymeleaf: Sfrutta il motore di template Thymeleaf per generare HTML in modo efficiente, garantendo che le pagine siano ottimizzate per l'interazione con i dati provenienti dai servizi.
* Gestione dei Prerequisiti: Permette di definire prerequisiti per le pagine, assicurando che tutte le condizioni necessarie siano soddisfatte prima di caricare il contenuto, migliorando così l'esperienza dell'utente. Obiettivi

L'obiettivo principale di PageBuilder è fornire un framework robusto e scalabile per la costruzione di pagine web, riducendo il tempo di sviluppo e aumentando la riutilizzabilità dei componenti. Con la sua capacità di gestire dinamicamente contenuti e servizi, PageBuilder si propone di migliorare l'efficienza e la qualità delle applicazioni web.

### Class Diagram

<img src="_assets/media/diagrams/pageBuilder_cs.jpg" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">

1. **PageBuilder** è il fulcro del sistema di costruzione dinamica delle pagine .È stato progettato per combinare logic componets e object componets,consentendo un'elevata flessibilità e modularità.E'responsabile della gestione e del rendering delle pagine, orchestrando l'esecuzione di vari componenti e gestendo eventuali errori che possono sorgere durante il processo.

2. **GenericObjectComponent** funge da classe base per la gestione degli oggetti destinati al modello della pagina. Inserimento dati nel modello ,sfruttando una mappa chiamata Model per contenere oggetti associati a chiavi specifiche. Questi oggetti possono poi essere passati al template per il rendering della pagina Consente l'estensione da parte di componenti oggettuali specifici, come il ServiceObjectComponent, per implementare logiche più complesse.

3. **ServiceObjectComponent** estende il GenericObjectComponent ed è specializzato nel recupero di dati da servizi esterni.Il recupero dinamico dei dati avviene tramite il ServiceManager, che effettua richieste verso servizi esterni per ottenere le informazioni necessarie al modello.Questi dati vengono poi inseriti nel modello utilizzando una chiave specifica, permettendo una corretta visualizzazione delle informazioni nella pagina

4. **GenericLogicComponent** è una classe astratta che stabilisce la struttura per i componenti logici nel sistema. Pertanto, il componente offre due funzionalità principali: il metodo executeLogic(), che si occupa di eseguire la logica del componente restituendo true in caso di successo o false in caso di errore, e il metodo getErrorCode(), il quale fornisce un codice di errore utile per identificare eventuali problemi, come un'autenticazione fallita o la mancanza di dati necessari.

5. **AuthComponent** è un'estensione di ServiceLogicComponent e si occupa della logica di autenticazione degli utenti. Per verificare l'autenticazione, utilizza il ServiceManager inviando una richiesta al servizio per controllare se l'utente dispone delle autorizzazioni necessarie. In caso di autenticazione fallita, il componente restituisce un codice di errore specifico (come Auth_error), che può essere utilizzato per reindirizzare l'utente a una pagina di login.

## Caso di Studio: Gestione degli errori page component logic

Per la gestione degli errori, è implementato un sistema centralizzato basato su una mappa che collega i codici di errore a pagine di destinazione specifiche. Questo permette una gestione unificata degli errori, come quelli relativi all'autenticazione o alla mancanza di dati. La gestione degli errori nel sistema è implementata attraverso la funzione setStandardErrorPage(), la quale stabilisce il comportamento predefinito in caso di errori.

```java
private void setStandardErrorPage() {
    errorPageMap.put("Auth_error", "redirect:/login");
    errorPageMap.put("default", "redirect:/error");
}
```

In questa funzione, viene utilizzata la put per associare una chiave identificativa di errore a una specifica pagina di indirizzamento. Ciò permette allo sviluppatore di configurare nuovi codici di errore con relativi pagine personalizzate rispetto al tipo di scenario. Un esempio concreto di questa logica è fornito dalla classe AuthComponent, che gestisce il processo di autenticazione degli utenti.

```java
public class AuthComponent extends ServiceLogicComponent {
    // Questo è a tutti gli effetti un wrapper per avere velocemente il check dell'autenticazione.
    public AuthComponent(ServiceManager serviceManager, String jwt) {
        super(serviceManager, "T23", "GetAuthenticated", jwt);
        setErrorCode("Auth_error");
    }
}
```

In questo caso, il metodo setErrorCode viene utilizzato per impostare il codice di errore relativo all'autenticazione. Questo codice può essere successivamente recuperato tramite il metodo getErrorCode della classe GenericLogicComponent. Questo metodo notifica che, durante l'esecuzione di una logica, è stato generato un evento, permettendo di segnalare il mancato rispetto di un requisito logico, come autorizzazioni insufficienti, dati mancanti o l'assenza di autenticazione.Inoltre, il codice di errore deve essere una stringa predefinita all'interno della classe.  public abstract String getErrorCode();

Quando il sistema gestisce le richieste di pagina, viene invocato il metodo executeComponentsLogic, che esegue la logica di tutti i componenti registrati. Se uno di questi componenti fallisce nell'esecuzione della propria logica, viene stampato un messaggio di errore e il metodo restituisce il codice di errore corrispondente.

```java
private List<String> executeComponentsLogic() {
    // Lista per raccogliere eventuali errori
    List<String> errorCodes = new ArrayList<>(); 

    for (GenericLogicComponent Component : LogicComponents) {
        if (!Component.executeLogic()) {
            System.out.println("Logica fallita per il componente: " + Component.getClass().getSimpleName());
            errorCodes.add(Component.getErrorCode()); // Aggiunge il codice d'errore alla lista
        }
    }
    return errorCodes;
}
```

Dopo aver eseguito la logica dei componenti, il codice di errore viene utilizzato per determinare quale pagina di errore visualizzare.Se viene restituito un codice di errore, il sistema consulta la mappa degli errori per ottenere l'indirizzamento corretto. Se non viene trovato un indirizzamento specifico per il codice, viene utilizzata una pagina di errore predefinita.

```java
// Metodo principale flusso per una richiesta di pagina
// Esegue la logica di ogni componente, poi elabora i dati da inserire nel template
public String handlePageRequest() {
    String return_page_error = null;
    if (LogicComponents != null && !LogicComponents.isEmpty()) {
        // Esegui la logica di tutti i componenti
        List<String> ErrorCode = executeComponentsLogic();
        // Gestisco le situazioni d'errore
        return_page_error = ExecuteError(ErrorCode);
    }
    if (ObjectComponents != null && !ObjectComponents.isEmpty()) {
        // Costruisci la mappa combinata dei dati dei componenti
        Map<String, Object> combinedModel = buildModel();
        model_html.addAllAttributes(combinedModel);
    }
    // Restituisco il nome del template da usare
    if(return_page_error != null) return return_page_error;
    return this.PageName;
}
```

In questo modo, il sistema non solo gestisce in modo efficace gli errori noti associati a specifiche funzionalità, ma fornisce anche una logica chiara e coerente per la gestione degli errori in generale. Questo approccio migliora l'esperienza utente, garantendo che gli utenti vengano reindirizzati a pagine di errore appropriate e, in assenza di una pagina specifica, a una pagina di errore predefinita, facilitando così la risoluzione dei problemi.

## Sequence Diagram: Richiesta Editor

Questo diagramma mostra il flusso di richiesta dell'editor da parte dell'utente, che inizia dopo la creazione della partita. Il sistema restituisce l'editor di test, permettendo all'utente di scrivere e inviare i test, che vengono poi compilati e confrontati con i risultati generati dai robot.

<img src="_assets/media/diagrams/editorRequest_sd.jpg" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">