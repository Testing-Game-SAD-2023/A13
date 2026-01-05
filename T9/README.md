# README - Task R7

## 0) Descrizione del Task svolto e Id del Task

**ID Task:** R7

Il task ha richiesto lo studio e la progettazione di una soluzione generica per lo scambio di notifiche fra diverse parti dell’architettura, dotando la piattaforma di un sistema centralizzato per la gestione degli avvisi agli utenti.

## 1) Implementazione del task

L'implementazione del task si basa sull'introduzione di un nuovo microservizio autonomo dedicato alla gestione delle notifiche (**TNotification**) e sull'adozione di un'architettura **Event-Driven**.

Per garantire scalabilità e resilienza, è stato introdotto un Message Broker (**RabbitMQ**) in un apposito container (**TB**) che funge da intermediario tra i servizi produttori (es. T7) e il servizio di notifiche. I produttori non invocano direttamente il servizio, ma pubblicano eventi su una coda; sarà poi il servizio di notifica a consumare questi messaggi, preoccupandosi di garantirne la persistenza su un database dedicato (**PostgreSQL**) e di inviarli immediatamente ai client connessi tramite un canale **SSE** (Server-Sent Events) per il push dei dati dal server al browser in tempo reale.

Per evitare duplicazioni di codice e facilitare l'integrazione da parte degli altri servizi, è stata sviluppata una libreria client condivisa (**T-shared-notification**) che contiene i DTO e le configurazioni standard per l'invio dei messaggi.

### Tabella delle Modifiche

| Microservizio Modificato | Tipo di Modifica | Eventuali Nuove Tecnologie usate   |
| :--- | :--- |:-----------------------------------|
| **TB** | Aggiunta container Message Broker (RabbitMQ) per l'infrastruttura di messaggistica asincrona. | **RabbitMQ**                       |
| **TNotification** | Nuovo servizio per gestione notifiche (SSE) e persistenza. Include un container DB dedicato (PostgreSQL). | **Spring Boot**, **PostgreSQL**, **SSE** |
| **T-shared-notification** | Creazione libreria condivisa per permettere agli altri servizi di inviare notifiche (DTO e config). | -  |

## 2) Integrazioni effettuate con altri gruppi

Il sistema di notifiche è stato progettato per essere **facilmente integrabile** nel resto dell'ecosistema, disaccoppiando la logica di business dall'infrastruttura.

Per i **servizi Backend**, è stata rilasciata la libreria condivisa **T-Shared-Notification**. Grazie ad essa, gli altri microservizi possono produrre notifiche:
*   Senza conoscere i dettagli di RabbitMQ (nomi code ed exchange incapsulati nella libreria).
*   Senza duplicare codice, utilizzando i DTO standard forniti.
*   In modalità asincrona, evitando colli di bottiglia nelle operazioni di gioco.

Per il **Frontend**, l'accessibilità al servizio è stata garantita configurando l'**API Gateway** per esporre l'intero path `/notifications/**`. Questo rende disponibili all'esterno:
*   Gli endpoint per lo stream real-time (**SSE**).
*   Le API REST standard per la gestione dello storico (operazioni `GET` per la lettura, `DELETE` per la rimozione...), permettendo così lo sviluppo di una UI completa per il centro notifiche.

## 3) Errori/problematiche non risolte nel progetto consegnato

Allo stato attuale, il microservizio **TNotification** e l'infrastruttura di messaggistica **non presentano errori bloccanti o malfunzionamenti noti**. Il sistema è stato testato con successo in isolamento e risponde correttamente a tutte le specifiche funzionali (connessione DB, code RabbitMQ, endpoint REST e stream SSE).

Tuttavia, l'**integrazione completa** con i flussi di business della piattaforma allo stato attuale è assente:

*   **Lato Backend:** Sebbene la libreria `T-Shared-Notification` sia pronta e funzionante, gli altri microservizi di gioco (es. T7) devono ancora importarla e configurare i trigger per l'invio degli eventi reali.
*   **Lato Frontend:** Le API sono esposte e accessibili tramite Gateway, ma la componente grafica (UI) per la visualizzazione delle notifiche non è ancora stata agganciata alle nuove rotte.

Il servizio è comunque pienamente verificabile utilizzando strumenti di test standard (Postman, cURL) o tramite test di integrazione che simulano i produttori di messaggi.
