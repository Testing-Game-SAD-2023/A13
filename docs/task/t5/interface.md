Introduzione a **ServiceManager** ServiceManager è una componente fondamentale del nostro progetto, progettato per semplificare e ottimizzare la gestione delle chiamate ai servizi REST in un'applicazione Java Spring. Questo strumento permette di aggregare e orchestrare richieste verso vari endpoint, facilitando l'integrazione e la comunicazione tra diversi microservizi.

## Caratteristiche Principali

* Gestione Unificata dei Servizi: ServiceManager offre un'interfaccia comune per l'accesso a diversi servizi, riducendo la complessità del codice e migliorando la manutenzione. Utilizzo di RestTemplate: Sfrutta RestTemplate per eseguire operazioni HTTP, garantendo efficienza e coerenza nelle comunicazioni di rete.
* Supporto per Metodi Generici: Permette la creazione di metodi generici per gestire vari tipi di richieste e risposte, inclusi payload complessi e headers personalizzati.
* Integrazione con Componenti di Pagina: Si integra perfettamente con i componenti dell'interfaccia utente, assicurando che i dati siano gestiti in modo efficace e che l'esperienza dell'utente sia fluida. Obiettivi

L'obiettivo principale di ServiceManager è migliorare la scalabilità e la flessibilità dell'architettura dell'applicazione, rendendo più facile l'aggiunta e la modifica dei servizi. Con il suo design modulare, gli sviluppatori possono concentrarsi sulla logica di business senza doversi preoccupare delle complessità della gestione delle chiamate ai servizi.

## Class Diagram

<img src="_assets/media/diagrams/interfaces_cd.jpg" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">

1. **BaseService:** È un'implementazione astratta che serve da base per i servizi che comunicano con API REST, offrendo un'infrastruttura condivisa per le operazioni e la gestione delle chiamate HTTP.Offre la possibilità di registrare le azioni disponibili per il servizio tramite il metodo registerAction(). Per gestire le richieste, implementa il metodo handleRequest(), che verifica l'esistenza dell'azione richiesta ed esegue la relativa operazione. Comprende metodi per effettuare chiamate HTTP di tipo GET, POST, PUT e DELETE (chiamate REST), occupandosi sia della costruzione degli URI che dell'elaborazione delle risposte. Inoltre, gestisce le eccezioni che possono sorgere durante le chiamate REST, fornendo messaggi di errore chiari e comprensibili.

2. **ServiceActionDefinition:** Rappresenta una classe immutabile che definisce azioni, incapsulando una funzione e le specifiche dei parametri da utilizzare. Include una funzione di tipo Function<Object[], Object> che determina l'azione da eseguire, sollevando eccezioni se i parametri non rispettano i tipi previsti.

3. **ServiceInterface:** Interfaccia che tutti i servizi devono implementare per essere incapsulati in un'unica classe per utilizzare il dispatcher.

4. **ServiceManager:** Funge da gestore centrale per i servizi, consentendo la registrazione e la gestione delle istanze di servizi che implementano ServiceInterface tramite il metodo registerService().Mentre, per la gestione delle richieste, implementa il metodo handleRequest() per verificare l'esistenza del servizio richiesto e instrada la richiesta all'azione appropriata, occupandosi della gestione delle eccezioni e registrando le operazioni tramite un logger.

5. **ServiceManagerLogger** Si occupa della registrazione delle attività del ServiceManager, fornendo un sistema di logging per il monitoraggio delle operazioni.

6. **T1Service:** estende BaseService e rappresenta un servizio specifico che interagisce con un API per la gestione di classi di test. Gestisce la registrazione delle azioni, come getClasses e getClassUnderTest, associando ciascuna a una funzione specifica per ottenere, rispettivamente, un elenco di classi e una classe particolare.

7. **T4Service:** estende BaseService e gestisce interazioni con un'API per la creazione e gestione di giochi e turni. Gestisce la registrazione delle azioni, come getLevels, CreateGame, EndGame, ecc., per la creazione e la gestione di giochi e turni, utilizzando chiamate REST per inviare e ricevere dati.

8. **T7Service:** estende BaseService e interagisce con un'API per compilare codice e analizzare la copertura del codice. Implementa l'azione CompileCoverage, che invia una richiesta POST al server contenente i dettagli del codice da compilare e analizzare.

9. **T23Service:** estende BaseService e gestisce l'autenticazione degli utenti e l'accesso agli utenti registrati.

## Caso d'Uso: Registrazione di una nuova azione

Per aggiungere una nuova azione, è necessario operare all'interno di una classe di servizio che estende BaseService. Se una classe per il servizio esiste già, è possibile inserirvi la nuova azione; altrimenti, sarà necessario creare una nuova classe di servizio. Ad esempio, supponiamo di voler aggiungere una nuova azione generica chiamata "newAction" al servizio T1Service. Di seguito è riportato un esempio pratico. Naturalmente, non esiste un pattern fisso, quindi è possibile implementare il metodo nel modo più appropriato.

```java
private String performNewAction(String param1, int param2) {
   return "Azione eseguita con " + param1 + " e " + param2;
}
```

Nel costruttore della classe di servizio, utilizza il metodo registerAction() per registrare la nuova azione. L'azione viene definita tramite un'istanza di ServiceActionDefinition. La prima chiamata a "params" specifica la firma della funzione, mentre la seconda indica il numero e il tipo di parametri richiesti.

```java
registerAction("newAction", new ServiceActionDefinition(
    params -> performNewAction((String) params[0], (Integer) params[1]),
    String.class, Integer.class
));
```

Una volta che l'azione è stata registrata e implementata, puoi invocarla attraverso il ServiceManager, specificando il nome del servizio e quello dell'azione.

```java
Object result = serviceManager.handleRequest("T1", "newAction", "testParam", 00);
System.out.println(result);
```

In questo esempio, il ServiceManager esegue la nuova azione "newAction", registrata all'interno del servizio T1Service, utilizzando i parametri forniti.È anche possibile creare un'azione senza parametri. In tal caso, il metodo registerAction() sarà strutturato nel seguente modo:

```java
registerAction("newAction", new ServiceActionDefinition(
        params -> newAction() //Metodo senza argomenti
));
```

## Caso d'Uso: Registrazione di un nuovo servizio

Per creare un nuovo servizio, lo sviluppatore deve definire una nuova classe che deriva **BaseService**, per poi registrarla all'interno di ServiceManger.

```java
public ServiceManager(RestTemplate restTemplate) {
    this.logger = new ServiceManagerLogger();
    // Registrazione dinamica dei servizi
    registerService("T1", T1Service.class, restTemplate);
    registerService("T23", T23Service.class, restTemplate);
    registerService("T4", T4Service.class, restTemplate);
    registerService("T7", T7Service.class, restTemplate);
    
    registerService("NewService", NewService.class, restTemplate);
}
```

> **Note:** RestTemplate è una classe fornita da Spring che facilita la comunicazione con servizi web esterni, consentendo di effettuare chiamate HTTP come GET, POST, PUT, DELETE. È un client HTTP che gestisce in modo semplificato la comunicazione tra applicazioni, rendendo facile interagire con API RESTful.
