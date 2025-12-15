# README - Implementazione Modalità Scalata

## 0) Descrizione del Task svolto e ID del Task

**ID Task:** R4

**Descrizione:**
Questa versione del progetto implementa la nuova modalità di gioco denominata **Scalata**, la quale prevede una progressione attraverso una serie di livelli sequenziali. 

Ogni livello richiede al giocatore di completare un obiettivo specifico (superare un test contro un determinato robot con una classe Under Test assegnata). In caso di successo, il giocatore avanza automaticamente al livello successivo; in caso di fallimento, ha la possibilità di riprovare lo stesso livello.

La modalità termina quando il giocatore completa con successo l'ultimo livello della scalata.

---

## 1) Implementazione del Task

### 1.1 Descrizione ad alto livello dell'implementazione

La modalità Scalata è stata implementata seguendo un approccio di **minima invasività** sui microservizi **T4** (gestione partite) e **T5** (game engine).

**Architettura adottata:**
- **T4**: Estensione del modello dati esistente con l'aggiunta di due attributi (`gameName` e `currentLevel`) all'entità `Game`, mantenendo inalterata l'architettura relazionale. Sono state introdotte due nuove rotte REST per supportare le operazioni specifiche della Scalata (incremento currentLevel e incremento currentRound).

- **T5**: Implementazione di una nuova classe `ScalataGame` che estende `TurnBasedGame`, seguendo il pattern già utilizzato per le altre modalità. La logica di orchestrazione è stata centralizzata nel `GameManager`, che gestisce in modo trasparente sia le modalità esistenti che la nuova Scalata.

- **T1**: Modifiche apportate dal gruppo D5.

- **Frontend**: Aggiunta di nuove pagine dedicate alla selezione e visualizzazione delle scalate, mantenendo la retrocompatibilità con l'editor di gioco esistente.

Questo approccio ha permesso di **riutilizzare l'infrastruttura esistente** (controller, sessioni Redis, calcolo punteggi, achievement) senza duplicazione di codice.

### 1.2 Elenco delle principali modifiche apportate ai microservizi

| **Microservizio** | **Tipo di Modifica**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | **Eventuali Nuove Tecnologie usate** |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------|
| **T1-G11**        | Modifiche apportate dal gruppo D5 per la gestione delle scalate lato amministratore                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Nessuna |
| **T4-G18**        | **Aggiunta di due nuove rotte REST:**<br>- `PUT /games/{id}/current-level-increment`: incrementa il livello corrente della scalata<br>- `PUT /games/{id}/rounds/last/increment-attempt`: incrementa il numero di tentativi sul round corrente<br><br>**Estensione modello dati:**<br>- Aggiunta attributi `gameName` e `currentLevel` all'entità `Game`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | Nessuna |
| **T5-G2**         | **Nuove classi Java:**<br>- `TurnBasedGame`: classe astratta base per modalità a turni<br>- `ScalataGame`: estende `TurnBasedGame`, gestisce la progressione dei livelli<br>- `ScalataParams`: DTO per i parametri specifici della Scalata<br>- `ScalataFactory`: factory per l'istanziazione di `ScalataGame`<br>- `EndScalataGameResponseDTO`: DTO di risposta con dati specifici della Scalata<br><br>**Modifiche a classi esistenti:**<br>- `GameManager`: esteso per gestire la progressione dei livelli<br>- `GameService`: aggiunto metodo `populateFirstLevelDataForScalata()`<br><br>**Frontend:**<br>- `gamemode_scalata.html`: nuova pagina per selezione e gestione scalate<br>- `gamemode_scalata.js`: logica di interazione per la modalità Scalata<br>- `gamemode_scalata.css`: stili dedicati per l'interfaccia Scalata | Nessuna |
---

## 2) Integrazioni effettuate con altri gruppi

L'implementazione della modalità Scalata ha richiesto una **stretta collaborazione con il gruppo D5** (responsabile di T1), con il quale sono stati necessari diversi confronti per definire:

### 2.1 Definizione del concetto di "Livello"

È stato necessario allinearsi sulla **semantica e struttura del livello** all'interno di una scalata:
- **Composizione**: ogni livello è caratterizzato da una specifica classe Under Test, un tipo di robot avversario (Randoop/Evosuite), un livello di difficoltà e un tempo massimo a disposizione.

### 2.2 Definizione delle rotte REST esposte da T1

Sono state concordate le seguenti interfacce REST necessarie per l'integrazione:

- **`GET /scalata/getAll`**: restituisce l'elenco di tutte le scalate disponibili nel sistema, con informazioni generali (nome, numero di livelli, descrizione).

- **`GET /scalata/getLevel/{scalataName}/{number}`**: recupera i dati di configurazione di uno specifico livello di una scalata (classe Under Test, tipo di robot, difficoltà, tempo massimo). Questa rotta è fondamentale per il caricamento dinamico dei parametri di gioco in T5.

### 2.3 Modalità di collaborazione

Il processo di integrazione ha seguito questo flusso:
1. **Analisi congiunta** dei requisiti funzionali della modalità Scalata
2. **Definizione del contratto API** (formato JSON, parametri, codici di errore)
3. **Implementazione parallela**: il gruppo D5 ha sviluppato gli endpoint in T1, mentre il nostro gruppo ha implementato le chiamate in T5
4. **Test di integrazione** per verificare la corretta comunicazione tra i microservizi

Questa collaborazione ha permesso di mantenere una **separazione delle responsabilità** chiara: T1 gestisce i metadati delle scalate (configurazione statica), mentre T5 e T4 gestiscono lo stato dinamico della partita.

---

## 3) Errori/problematiche non risolte nel progetto consegnato

### 3.1 Complessità eccessiva del metodo `handleEndGame()` in GameManager

Il metodo `handleEndGame()` della classe `GameManager` presenta attualmente una **complessità cognitiva elevata**, come evidenziato anche dall'analisi statica del codice tramite **SonarQube**.

**Descrizione del problema:**
Il metodo gestisce molteplici responsabilità:
- Esecuzione dell'ultimo turno di gioco
- Distinzione tra modalità Scalata e altre modalità
- Gestione della progressione dei livelli (solo per Scalata)
- Calcolo e assegnazione di XP e achievement
- Costruzione di DTO di risposta differenziati (EndGameResponseDTO vs EndScalataGameResponseDTO)

**Perché non è stato risolto:**
Data la scadenza ravvicinata del progetto e la necessità di garantire prima la **correttezza funzionale** della modalità Scalata (test end-to-end, integrazione con T1 e T4, gestione delle sessioni Redis), si è scelto di rimandare il refactoring del codice. 

**Soluzione proposta (non implementata):**
- **Estrazione di metodi privati**: suddividere la logica in metodi come `calculateRewards()`, `buildScalataResponse()`, `buildStandardResponse()`
- **Pattern Strategy**: delegare la gestione della chiusura a strategie specifiche per modalità (ScalataEndGameStrategy, StandardEndGameStrategy)
- **Riduzione della nidificazione**: semplificare le condizioni annidate tramite early return

---

### 3.2 Limitazione nella gestione della modalità Scalata senza sessione attiva

Attualmente, la modalità Scalata funziona correttamente **solo se gestita tramite sessione Redis**. 

**Descrizione del problema:**
Se un giocatore:
- Chiude il browser 
- La sessione Redis scade per timeout
- Il giocatore riaccede

Il sistema **non è in grado di recuperare lo stato della partita** e la scalata non può essere ripresa correttamente.

**Soluzione proposta (non implementata):**

Per gestire il recupero dello stato della scalata in assenza di sessione Redis attiva, è sufficiente interrogare **T4** e ricostruire lo stato di gioco attraverso il seguente flusso:

1. **Query a T4**: Recuperare l'ultima partita Scalata del giocatore tramite `GET /games/player/{playerId}` (filtrando per tipo Scalata)

2. **Verifica dello stato del gioco**:
   - Se lo stato è **FINISHED** o **SURRENDERED**: non esiste una partita attiva da recuperare
   - Se lo stato è **STARTED** o **IN_PROGRESS**: esiste una partita attiva che può essere recuperata

3. **Recupero dei parametri della scalata**: Dall'entità `Game` recuperata da T4, estrarre:
   - `gameName`: il nome della scalata in corso
   - `currentLevel`: il livello corrente raggiunto dal giocatore

4. **Query a T1**: Utilizzare i dati estratti per interrogare T1 tramite `GET /scalata/getLevel/{scalataName}/{number}` e ottenere i metadati del livello corrente (classe Under Test, robot, difficoltà, tempo massimo)

5. **Ricostruzione dello stato**: Istanziare un nuovo oggetto `ScalataGame` con:
   - I dati recuperati da T4 (ID partita, stato, currentLevel)
   - I parametri del livello recuperati da T1
   - Lo stato di sessione salvato nuovamente in Redis

Questa soluzione permette di **ripristinare completamente** lo stato della scalata senza richiedere modifiche significative all'architettura esistente, sfruttando le informazioni già persistite in T4 e i metadati disponibili in T1.

---
