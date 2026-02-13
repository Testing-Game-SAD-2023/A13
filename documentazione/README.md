# README - Task R7

## 0) Descrizione del Task svolto e Id del Task

**ID Task:** R7

Il task ha richiesto lo studio e la progettazione di una soluzione generica per lo scambio di notifiche fra diverse parti dell’architettura, dotando inoltre la piattaforma di un sistema centralizzato per la gestione degli avvisi agli utenti.

## 1) Implementazione del task

L'implementazione del task ha portato all'introduzione di un **Message Broker** per garantire all'interno dell'architettura un tipo di comunicazione asincrona (Event-Driven), ed in secondo luogo, alla creazione di un servizio di notifiche utente deployato su un container apposito.
 
Per garantire scalabilità e resilienza, è stato introdotto un Broker **RabbitMQ** in un apposito container **TB** che funge da intermediario tra i servizi produttori ed i servizi consumatori. I produttori non invocano direttamente il servizio, ma pubblicano eventi su una coda; saranno poi i servizi interessati a consumare questi messaggi.
 
Il servizio **TNotification** incapsula tutta la logica di business legata alla gestione delle notifiche utente, in particolare prevede:
- un database dedicato **PostgreSQL** al fine di mantenere la persistenza dei dati;
- un'API che permette agli altri servizi, tramite il broker, di creare notifiche utente;
- un'API REST che espone operazioni di lettura, cancellazione e aggiornamento dello stato delle notifiche utente;
- un canale **SSE** (Server-Sent Events) per il push dei dati dal server al browser in tempo reale.
 
Per evitare duplicazioni di codice e facilitare l'integrazione da parte degli altri servizi, è stata sviluppata una libreria client condivisa (**T-shared-notification**) che contiene i DTO e le configurazioni standard per l'invio dei messaggi.

### Tabella delle Modifiche

| Microservizio Modificato | Tipo di Modifica | Eventuali Nuove Tecnologie usate |
| :--- | :--- | :--- |
| **TB** | Nuovo | **RabbitMQ**. Aggiunta container Message Broker per l'infrastruttura di messaggistica asincrona tra microservizi. |
| **TNotification** | Nuovo | **Spring Boot**, **PostgreSQL**, **SSE**. Microservizio dedicato alla gestione completa delle notifiche e persistenza. |
| **T-shared-notification** | Nuovo | Libreria condivisa (Java) per la definizione di DTO e costanti per l'invio notifiche tramite RabbitMQ. |
| **API_Gateway** | Modificato | Aggiunta  nuova rotta per permettere il corretto instradamento del traffico verso il servizio TNotification. |

## 2) Errori/problematiche non risolte nel progetto consegnato

Allo stato attuale, il microservizio **TNotification** e l'infrastruttura di messaggistica **non presentano errori bloccanti o malfunzionamenti noti**. Il sistema è stato testato con successo in isolamento e risponde correttamente a tutte le specifiche funzionali (connessione DB, code RabbitMQ, endpoint REST e stream SSE).

Tuttavia, l'**integrazione completa** con i flussi di business della piattaforma allo stato attuale è assente:

*   **Lato Backend:** Sebbene la libreria `T-Shared-Notification` sia pronta e funzionante, gli altri microservizi di gioco (es. T7) devono ancora importarla e configurare i trigger per l'invio degli eventi reali.
*   **Lato Frontend:** Le API REST sono esposte e accessibili tramite Gateway, ma la componente grafica (UI) per la visualizzazione delle notifiche non è ancora stata agganciata alle nuove rotte.

Il servizio è comunque pienamente verificabile utilizzando strumenti di test standard (Postman, cURL) o tramite test di integrazione che simulano i produttori di messaggi.