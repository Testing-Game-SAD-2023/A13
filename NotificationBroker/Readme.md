0) In questo progetto ci è stato proposto di lavorare su un’architettura reale, organizzata a microservizi, utilizzando un approccio basato sul gioco.
   Il task da sviluppare è l’R7 che richiede l’introduzione di un sistema generalizzato di notifiche da mostrare nella home page del giocatore. Gli obiettivi fissati sono:
   • Progettare un sistema flessibile che permetta all’applicazione di inviare notifiche tra diversi microservizi
   • Mostrare le notifiche in modo chiaro e immediato nella Home del giocatore
1) Implementazione del task -
   La proposta del team riguarda l’introduzione di un broker intermedio tra i moduli
   che ne gestisca la comunicazione asincrona. Analizzando il codice dell’applicazione
   proposta, si è potuto notare la presenza di chiamate REST sincrone tra i vari moduli,
   rilevando un’alta latenza. Gli obiettivi del progetto riguardano l’implementazione
   di un sistema di notifiche generalizzato e riutilizzabile. Inoltre, la sostituzione di
   una comunicazione sincrona con una asincrona. Infine, l’assicurazione di persistenza
   e affidabilità nella gestione delle notifiche. Il broker introdotto è RabbitMQ.
   | Modulo/Classe | Descrizione della modifica |
   | --- | --- |
   | **T5–UserProfileController** | Sono stati inseriti i metodi `getNotifications`, `sendNotification`, `deleteNotification` e `clearAllNotifications` per la gestione delle notifiche lato UI. |
   | **T5–GuiController** | Modifica per la generazione di una notifica quando si accede alla modalità partita singola. |
   | **T5–interfaces–T23Service** | Eliminazione dei metodi relativi alle notifiche, successivamente estratti e riorganizzati in una nuova classe dedicata. |
   | **T5–interfaces–NotificationService** | Nuova classe introdotta per contenere la logica di gestione delle notifiche precedentemente presente in T23Service. |
   | **T5–interfaces–BaseServiceREST** | Nuova classe contenente le chiamate REST eliminate dalla classe BaseService, al fine di separare la comunicazione sincrona da quella asincrona. |
   | **T5–interfaces–BaseServiceBroker** | Classe introdotta per incapsulare i template di RabbitMQ e gestire l’invio dei messaggi AMQP verso il broker. |
   | **NotificationBroker–BrokerConfiguration** | Presente la configurazione del binding e la creazione delle code, la gestione della serializzazione dei messaggi e il Rabbit Admin per la creazione automatica all’avvio di tutte le code ed exchange. |
   | **NotificationBroker–Application** | Effettua l’inizializzazione del broker all’avvio dell’applicazione. |
   | **T23–config–RabbitMQConfig** | Nuova classe per la configurazione di RabbitMQ nel servizio T23. Definisce i bean necessari per la corretta serializzazione e deserializzazione dei messaggi. |
   | **T23–service–NotificationRabbitMQListener** | Classe contenente il listener che si mette in ascolto sulle code RabbitMQ per gestire le operazioni relative alle notifiche. Ogni metodo annotato con `@RabbitListener` rappresenta un consumer associato a una specifica coda. |
   | **T23–service–NotificationService** | Classe che incorpora la classe NotificationRepository per l’interfacciamento con il database che logicamente ingloba la classe DAO nelle funzionalità aggiungendoci le proprietà di paginazione dei dati. |
   | **UiGateway** | Sono state modificate le routes per renderle compatibili con le chiamate per la richiesta della pagina. |
2) Attualmente sia T23 sia T5 registrano i propri log localmente. Quindi l’impatto riscontrato è che in caso
    di errore nel flusso request/reply, il tracciamento di un singolo messaggio
    attraverso tutti i componenti risulta estremamente difficile.
