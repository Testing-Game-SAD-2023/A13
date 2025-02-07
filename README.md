# Gestione dei Team di Utenti e degli Esercizi

## Introduzione
Questo progetto è stato realizzato nell'ambito del corso di **Software Architecture Design** (Anno Accademico 2024/2025) presso l'Università Federico II di Napoli e si inserisce nel contesto del progetto **ERASMUS ENACTEST** (European iNnovative AllianCe for TESTing educaTion). L'obiettivo principale è lo sviluppo di una piattaforma che favorisca l'apprendimento del software testing attraverso la gamification. Questo lavoro, in particolare, si concentra sull'espansione del sistema software esistente, introducendo la gestione d team di utenti e l'assegnazione di esercizi da parte di un amministratore.



## Funzionalità principali
### Gestione dei Team
- **Creazione di un team**: L'amministratore può creare un nuovo team specificando nome e descrizione.
- **Modifica di un team**: L'amministratore può modificare i dettagli di un team esistente.
- **Eliminazione di un team**: L'amministratore può eliminare un team, con notifica via email agli utenti coinvolti.
- **Aggiunta e rimozione di studenti**: L'amministratore può aggiungere o rimuovere studenti da un team, con notifica automatica via email.

### Gestione degli Esercizi
- **Assegnazione di esercizi**: L'amministratore può assegnare esercizi a un team, specificando, tra le altre cose, obiettivi, data di scadenza e descrizione.
- **Modifica degli esercizi**: L'amministratore può modificare la descrizione o la data di scadenza di un esercizio.
- **Eliminazione degli esercizi**: L'amministratore può eliminare un esercizio.
- **Visualizzazione degli esercizi**: Gli studenti possono visualizzare gli esercizi assegnati ai team di cui fanno parte.

### Dashboard di Monitoraggio
- **Visualizzazione dei progressi**: L'amministratore può monitorare i progressi degli studenti rispetto agli esercizi assegnati, con una panoramica degli obiettivi completati e di quelli ancora da affrontare.

## Architettura del sistema
Il sistema è basato su un'architettura a **microservizi**, garantendo flessibilità e scalabilità. Utilizza il **Gateway Pattern**, con un UI Gateway per gestire le richieste web e un API Gateway per instradare le richieste ai microservizi.

### Servizi principali
- **T1**: Si occupa dell'interfacciamento degli amministratori con tutti i meccanismi legati ai team.
- **T23**: Gestisce la creazione, modifica ed eliminazione dei team, inclusi gli utenti che ne fanno parte.
- **T5**: Gestisce in modo dettagliato l'assegnazione, la modifica e il monitoraggio degli esercizi attribuiti ai team.

## Tecnologie utilizzate
- **Backend**: Spring Boot, Spring Data JPA, Lombok
- **Frontend**: Thymeleaf, HTML/CSS/JS
- **Database**: MySQL e MongoDB
- **Comunicazione**: JavaMail
- **Testing API**: Postman
- **Versioning**: GitHub
- **Diagrammi UML**: Visual Paradigm, draw.io
- **Containerizzazione**: Docker

## Requisiti Funzionali
- **Gestione dei team**: Creazione, modifica, eliminazione, aggiunta e rimozione di studenti.
- **Gestione degli esercizi**: Assegnazione, modifica, eliminazione e visualizzazione degli esercizi.
- **Notifiche**: Invio di notifiche via email per aggiunta, rimozione o eliminazione di team.

## Requisiti Non Funzionali
- **Usabilità**: Interfaccia intuitiva e facile da usare per l'amministratore.
- **Integrità dei dati**: Garantire che le informazioni sui team e gli esercizi siano gestite correttamente.
- **Estensibilità**: Il sistema deve essere facilmente modificabile per l'aggiunta di nuove funzionalità.

## Processo di Sviluppo
Il team ha adottato una metodologia **agile**, con cicli di sviluppo brevi e incontri regolari per discutere i progressi e risolvere eventuali problemi. Gli strumenti principali per la collaborazione sono stati **Microsoft Teams e GitHub**.

## Installazione e setup
1. **Clonare il repository:**
   ```sh
   git clone https://github.com/DelVecchio-Esposito-Mauriello-Perillo/A13
   ```
2. **Eseguire lo script di installazione:**
   ```sh
   cd A13
   installer.bat
   ```
3. **Avviare i microservizi con Docker:**
   ```sh
   docker-compose up -d
   ```
4. **Accedere all'applicazione:**
   ```
   http://localhost:8080
   ```
5. **Disinstallazione**
   ```
   uninstaller.bat
   ```

## API principali
| Metodo | Endpoint | Descrizione |
|--------|---------|-------------|
| GET | `/api/team` | Ottiene la lista dei team |
| POST | `/api/team` | Crea un nuovo team |
| PUT | `/api/team/{id}` | Modifica un team |
| DELETE | `/api/team/{id}` | Elimina un team |
| POST | `/api/team/{id}/students` | Aggiunge uno studente a un team |
| DELETE | `/api/team/{id}/students/{studentId}` | Rimuove uno studente da un team |
| POST | `/api/team/{id}/exercises` | Assegna un esercizio a un team |
| DELETE | `/api/team/{id}/exercises/{exerciseId}` | Rimuove un esercizio da un team |
| GET | `/api/team/{id}/students` | Ottiene la lista degli studenti di un team |
| GET | `/api/student/{id}/teams` | Ottiene la lista dei team a cui uno studente appartiene |
| GET | `/api/team/{id}/exercises` | Ottiene la lista degli esercizi assegnati a un team |
| GET | `/api/exercise/{id}` | Ottiene i dettagli di un esercizio |
| PUT | `/api/exercise/{id}` | Modifica un esercizio |
| DELETE | `/api/exercise/{id}` | Elimina un esercizio |

## Esempio di utilizzo dell'interfaccia
https://github.com/user-attachments/assets/b7363a27-c3d8-4b94-999a-c5bd30a62b4f

## Contributori
Il progetto è stato sviluppato dal Team:
- **Perillo Giulio**
- **Del Vecchio Federica**
- **Esposito Francesco**
- **Mauriello Valentina**

