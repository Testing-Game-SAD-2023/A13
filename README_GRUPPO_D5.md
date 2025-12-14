# Scalata - Modalità di Gioco

Questa versione del progetto implementa la gestione dela modalità di gioco **Scalata Lato Amministratore**, consentendo a quest'ultimo di creare, visualizzare ed eliminare le scalate. 

## Dettagli sulla Scalata

- Una Scalata è composta da un **numero variabile di livelli**.  
- Per ciascun livello, l’Amministratore può:  
  - Selezionare una **classe da testare** (diversa per ogni livello)  
  - Scegliere un **Robot avversario** da sfidare  
  - Scegliere il **tempo massimo** 

## Funzionalità principali

- **Creazione di una Scalata:** definizione di una nuova serie di livelli.  
- **Visualizzazione delle Scalate esistenti:** consultazione delle Scalate già create.  
- **Cancellazione delle Scalate:** rimozione di Scalate non più necessarie.


## Implementazione del task

Il task **“Creazione_Scalata”** è stato implementato modificando il microservizio **T1**.  
In particolare:  
- Sono state ridefinite le **rotte REST API** per consentire all’Amministratore di creare, visualizzare e cancellare le Scalate, e sono state aggiunte le rotte necessarie per fornire a T5 i dati relativi alle Scalate e ai relativi livelli.
- È stato completamente **rifatto il frontend**, per consentire all’Amministratore di configurare facilmente i livelli, selezionare le classi da testare e scegliere i Robot avversari.  
- Sono state introdotte eccezioni dedicate per la gestione della Scalata, con l’obiettivo di irrobustire il sistema e migliorare la gestione degli errori in modo centralizzato e chiaro attraverso un **Exception Handler Globale**.


## API Scalata

| Metodo | Percorso | Descrizione | Parametri / Corpo | Codici di Risposta Chiave |
|------|---------|------------|-------------------|---------------------------|
| POST | `/create` | Crea una nuova scala (ladder), con validazioni su duplicati e livelli. | Corpo: `ScalataDTO` | `200 OK` (Creata), `400 Bad Request` (Dati non validi), `409 Conflict` (Esistente) |
| GET | `/getAll` | Recupera una lista di tutte le scale disponibili nel sistema. | Nessuno | `200 OK` (Lista di `ScalataDTO`) |
| GET | `/get/{name}` | Recupera una singola scala per nome. | Path: `name` (stringa) | `200 OK` (Singola `ScalataDTO`), `404 Not Found` |
| GET | `/getLevel/{name}/{number}` | Recupera un livello specifico dato il nome della scala e il numero del livello. | Path: `name` (stringa), `number` (intero) | `200 OK` (`LevelDTO`), `404 Not Found` |
| GET | `/getLevels/{name}` | Recupera la lista di tutti i livelli appartenenti a una specifica scala. | Path: `name` (stringa) | `200 OK` (Lista di `LevelDTO`), `404 Not Found` |
| DELETE | `/delete/{name}` | Elimina una scala specifica dal sistema tramite il suo nome. | Path: `name` (stringa) | `200 OK` (Eliminata), `404 Not Found` |


| File | LOC (-/+) | Descrizione |
|------|-----------|-------------|
| T1-G11\applicazione\manvsclass<br>pom.xml | 0 / 23 | Aggiunte le dipendenze springdoc (per la documentazione) e mapstruct (per generare i mapper DTO). |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\controller<br>ScalataController.java | 20 / 123 | Ridefinizione dei metodi su cui sono mappate le rotte /scalata. |
|T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\controller\advices<br>GlobalExceptionHandler.java | 0 / 72 | Introdotta la classe per la gestione centralizzata delle eccezioni per separare gli interessi rispetto ai controller. |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\controller\view<br>ScalataViewController.java | 7 / 49 | Ridefinizione dei metodi su cui sono mappate le rotte frontend /scalata. |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\mapper<br>LevelMapper.java | 0 / 15 | Introdotte le classi Mapper per rispettare il pattern DTO. |
| ScalataMapper.java | 0 / 14 |  |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\model<br>ClassUT.java | 15 / 3 | Snellita la classe mediante libreria lombok. |
| Level.java | 0 / 18 | Introdotta l’entità Level. |
| Scalata.java | 74 / 12 | Riadattata l’entità Scalata e snellita mediante lombok. |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\model\dto<br>LevelDTO.java | 0 / 15 | Introdotte le classi DTO. |
| ScalataDTO.java | 0 / 19 |  |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\model\repository<br>ScalataRepository.java | 8 / 8 | Modificati i nomi dei metodi per refactoring. |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\service<br>ScalataService.java | 41 / 126 | Ridefiniti i metodi che implementano la logica di gestione delle scalate. |
| T1-G11\applicazione\manvsclass\src\main\java\com\groom\manvsclass\service\exception<br>InvalidTempoMaxFormatException.java | 0 / 7 | Introdotte le eccezioni per aumentare la robustezza. |
| LevelNotFoundException.java | 0 / 7 |  |
| LevelsSameClassException.java | 0 / 7 |  |
| NegativeTempoMaxException.java | 0 / 7 |  |
| ScalataAlreadyExistsException.java | 0 / 7 |  |
| ScalataLevelException.java | 0 / 7 |  |
| ScalataNotFoundException.java | 0 / 7 |  |
| T1-G11\applicazione\manvsclass\src\main\resources\lang<br>messages.properties | 0 / 47 | Esteso il supporto all’internazionalizzazione. |
| messages_en.properties | 1 / 49 |  |
| messages_it.properties | 1 / 49 |  |
| T1-G11\applicazione\manvsclass\src\main\resources\static\t1\css<br>scalata.css | 135 / 151 | Aggiunte animazioni ai button. |
| T1-G11\applicazione\manvsclass\src\main\resources\static\t1\js<br>dashboard.js | 1 / 1 | Aggiunto il query param `tour=true` per richiedere la presentazione del tour quando si naviga verso /scalata/main. |
| scalata.js | 600 / 0 | Eliminato per riprogettazione del frontend. |
| scalata_tour.js | 180 / 0 | Eliminato per riprogettazione del frontend. |
| T1-G11\applicazione\manvsclass\src\main\resources\static\t1\js\scalata<br>scalata_edit.js | 0 / 140 | Introdotto per gestire la generazione dinamica dei livelli in fase di creazione della scalata, recuperare gli opponents disponibili da /opponents e inviare la nuova scalata su /scalata/create. |
| scalata_main.js | 0 / 14 | Introdotto per invocare la rotta /scalata/delete per cancellare una data scalata visualizzata. |
| scalata_main_tour.js | 0 / 192 | Riadattato il tour precedente ai nuovi elementi HTML. |
| T1-G11\applicazione\manvsclass\src\main\resources\static\t1\js\utils<br>api.js | 0 / 48 | Aggiunte le chiamate wrapper per le nuove rotte. |
| endpoints.js | 0 / 10 | Aggiunte le costanti per le nuove rotte. |
| T1-G11\applicazione\manvsclass\src\main\resources\templates<br>dashboard.html | 1 / 1 | Eliminato attributo disabled al button che naviga verso /scalata/main |
| T1-G11\applicazione\manvsclass\src\main\resources\templates\scalata<br>scalata_edit.html | 0 / 97 | Aggiunta la pagina per la creazione della scalata. |
| scalata_main.html | 135 / 149 | Riprogettata la pagina di visualizzazione delle scalate. |
| ui_gateway\routes<br>T1_route.conf | 1 / 1 | Aggiunta la navigabilità sulla rotta /scalata/create. |

## Integrazioni con gli altri gruppi

Per l’implementazione della modalità **Scalata** ci siamo confrontati con il gruppo **D1**, che si occupava dello sviluppo della scalata lato **GameEngine**. L’obiettivo della collaborazione è stato definire un **modello dei dati comune** e stabilire le **rotte REST** che sarebbero state utilizzate per il corretto funzionamento del gioco.


Nella versione attuale, si assume che l’Amministratore cancelli una Scalata quando nessun utente sta giocando. La gestione della cancellazione di Scalate già in corso non è stata implementata, poiché richiederebbe l'esposizione di rotte da parte di T5 finora non ancora previste.