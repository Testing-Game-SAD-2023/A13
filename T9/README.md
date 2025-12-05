# T9 Notification Service

Il servizio T9 gestisce le **notifiche applicative** generate dagli altri microservizi del sistema di gioco.

## Scopo del servizio

- Ricevere eventi dai servizi esistenti tramite **RabbitMQ** (es. partita creata, risultati disponibili, errori).
- Salvare le notifiche in un database **PostgreSQL** dedicato.
- Esporre API REST per permettere al front-end (tramite lo UI Gateway) di:
    - recuperare le notifiche di un determinato utente (es. `GET /notifications?userId=...`);
    - in seguito, eventualmente, aggiornare lo stato delle notifiche (lette/non lette).

## Architettura

Il servizio è pensato come microservizio Spring Boot con:

- **Controller** REST per la gestione delle richieste HTTP.
- **Service** con la logica di business (creazione e gestione delle notifiche).
- **Repository** per l’accesso al database PostgreSQL.
- **Listener RabbitMQ** per consumare i messaggi pubblicati dagli altri servizi.

Il database Postgres e il servizio T9 sono definiti nel `docker-compose.yml` della cartella `T9` e condividono la rete Docker `global-network` con il container RabbitMQ definito in `TB`.
