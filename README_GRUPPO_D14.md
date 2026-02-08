# R6 – Profilo Giocatore

Questa versione del progetto implementa la gestione del **Profilo Giocatore**, consentendo al player di visualizzare e modificare le informazioni associate al proprio profilo, consultare i profili degli altri utenti e di sfruttare un sistema di messagistica.

## Dettagli sul Profilo Giocatore

- Il Profilo Giocatore è composto da:
  - una **parte fissa**, contenente avatar/foto profilo e biografia
  - una **parte variabile**, che consente di selezionare e visualizzare diverse informazioni 
- La biografia:
  - è **opzionale**
  - ha valore di default **“Test addicted”**
  - può contenere al massimo **500 caratteri**
- L’immagine del profilo:
  - è **opzionale**
  - se non fornita, viene mostrata un’immagine **placeholder** predefinita

## Funzionalità principali

- **Visualizzazione del profilo personale**
- **Modifica della biografia**
- **Modifica dell’immagine del profilo**
- **Visualizzazione delle informazioni di gioco**, tra cui:
  - livello corrente
  - punti esperienza totali
  - punti esperienza mancanti al livello successivo
- **Visualizzazione delle informazioni social**, tra cui:
  - follower
  - following
- **Ricerca e visualizzazione del profilo di altri player**
- **Gestione delle relazioni Follow / Unfollow**
- **Gestione della messaggistica**, con:
  - inbox
  - outbox
  - invio ed eliminazione dei messaggi

## Implementazione del task

Il task **R6 – Profilo Giocatore** è stato implementato intervenendo principalmente sui microservizi **T5** e **T23**.

In particolare:
- Sono state introdotte e/o ridefinite **rotte REST API** per la gestione del profilo, delle relazioni social e della messaggistica.
- È stato effettuato un **refactoring del frontend**, separando in modo netto struttura HTML e logica JavaScript, al fine di migliorare manutenibilità ed estendibilità.
- Sono stati introdotti **DTO dedicati** per la comunicazione tra microservizi, evitando l’esposizione diretta delle entità JPA.
- È stata implementata una gestione robusta delle operazioni di messaggistica, con controlli di autorizzazione lato backend.

## API Profilo Giocatore (principali)

| Metodo | Percorso | Descrizione |
|------|---------|-------------|
| POST | `/update_profile` | Aggiorna biografia e immagine del profilo del player |
| GET | `/friend/{playerID}` | Visualizza il profilo di un altro giocatore |
| POST | `/toggle_follow` | Gestisce Follow / Unfollow tra due player |
| GET | `/followers` | Recupera la lista dei follower |
| GET | `/following` | Recupera la lista dei following |

## API Messaggistica (principali)

| Metodo | Percorso | Descrizione |
|------|---------|-------------|
| POST | `/messages/new` | Invia un nuovo messaggio |
| GET | `/messages/inbox` | Recupera la inbox del player |
| GET | `/messages/outbox` | Recupera la outbox del player |
| DELETE | `/messages/{id}` | Elimina un messaggio (previa autorizzazione) |


| File | Descrizione |
|------|-------------|
| T5-G2/t5/src/main/java/com/g2/controllers/UserProfileController.java | Aggiunta e ridefinizione dei metodi su cui sono mappate le rotte per la modifica del profilo e la messaggistica. |
| T23-G1/src/main/java/com/example/db_setup/controller/UserSocialController.java | Introduzione delle rotte per gestire follower, following, ricerca utenti e visualizzazione del profilo di altri giocatori. |
| T23-G1/src/main/java/com/example/db_setup/controller/MessageController.java | Definisce le rotte relative alla messaggistica. |
| T23-G1/src/main/java/com/example/db_setup/model/Message.java | Permette di mappare su database le entità Messages. |
| T23-G1/src/main/java/com/example/db_setup/model/repository/MessageRepository.java | Sono stati introdotti i metodi di accesso al database (CRUD) per la gestione della tabella Messages. |
| T23-G1/src/main/java/com/example/db_setup/model/dto/business/NewMessageDTO.java | Introdotta la classe DTO. |
| T23-G1/src/main/java/com/example/db_setup/model/dto/business/MessageDTO.java | Introdotta la classe DTO. |
| T23-G1/src/main/java/com/example/db_setup/service/MessageService.java | Definiti i metodi che implementano la logica di gestione dei messaggi. | 
| T23-G1/src/main/java/com/example/db_setup/service/UserSocialService.java | Ridefiniti i metodi che implementano la logica di gestione dei follower, following, ricerca utenti e visualizzazione del profilo di altri giocatori. | 
| T5-G2/t5/src/main/java/com/g2/model/dto/NewMessageDTO.java | Introdota la classe DTO. | 
| T5-G2/t5/src/main/java/com/g2/model/Message.java | Definizione di un modello dati per rappresentare i messaggi scambiati tra i giocatori. |
| T5-G2/t5/src/main/java/com/g2/interfaces/T23Service.java | Introduzione dei metodi per coordinare i microservizi al fine di gestire messagistica tra giocatori e modifica del profilo. |
| T5-G2/t5/src/main/resources/static/t5/js/profile.js | Ne è stato fatto il refactoring per ottenere una netta separazione con il codice HTML e sono state modificate le funzionalità per la visualizzazione del profilo. | 
| T5-G2/t5/src/main/resources/static/t5/js/profile-edit.js | Sono state aggiunte delle modifiche per ottimizzare la comunicazione con il backend in vista della funzionalità di modifica del profilo. |
| T5-G2/t5/src/main/resources/templates/profile.html | E' riprogettata la pagina di visualizzazione del profilo. |
| T5-G2/t5/src/main/resources/templates/Edit_Profile.html | E' riprogettata la pagina di modifica del profilo. |
