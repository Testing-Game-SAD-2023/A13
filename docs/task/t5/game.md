Il principale obiettivo di **GameController** è quello di gestire il ciclo di vita di una partita in modo fluido e coerente, garantendo un'esperienza di gioco bilanciata e coinvolgente per gli utenti. Il design modulare e integrabile del controller permette di estendere facilmente la logica a nuovi giochi, migliorando la flessibilità dell'architettura di gioco.

## Class Diagram

<img src="_assets/media/diagrams/game_cd.jpg" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">

1. **GameController:** Agisce come intermediario tra il client e il backend, occupandosi della gestione delle richieste HTTP, del coordinamento del ciclo di vita delle partite e dell'interazione con i servizi esterni. Nello specifico:

    * Tiene traccia delle partite attive in una mappa (activeGames), dove ogni partita è associata a un giocatore (playerId).
    * Riceve richieste dal client (come l'avvio di una partita o l'esecuzione di un turno) attraverso metodi annotati come @PostMapping e @GetMapping.
    * Metodi di supporto: Come GetUserData o GetRobotScore, che si occupano di recuperare informazioni necessarie dalle chiamate ai servizi esterni.

2. **GameLogic:** Definisce i concetti di gioco come partite, round e turni. Offre metodi per crearli, giocarli e terminarli. Fornisce metodi che le sottoclassi devono implementare per definire la logica specifica del gioco: come giocare un turno, determinare se il gioco è finito, e calcolare il punteggio. Pertanto,i metodi principali:

    * playTurn: Definisce la logica di ogni turno.

    * isGameEnd: Definisce le condizioni di fine partita.

    * GetScore: Calcola il punteggio del giocatore in base a determinate metriche (come la copertura del codice).

    Quindi, GameLogic definisce quindi la struttura e le regole di gioco. Le sottoclassi che estendono GameLogic implementano i dettagli specifici del gioco. Non interagisce direttamente con il client o le richieste HTTP, ma si occupa della gestione interna del gioco.

3. **TurnBasedGameLogic** estende GameLogic e implementa la logica di gioco specifica per la modalità sfida.

## Caso d'Uso: Creazione di una Nuova Modalità di Gioco

Il sistema utilizza una classe astratta, GameLogic, come base per la gestione della logica di gioco. Per creare una nuova modalità di gioco, è necessario derivare una nuova classe da GameLogic e implementare i tre metodi astratti: playTurn(), isGameEnd(), e GetScore() e il metodo CreateGame().

1. Creare una Nuova Classe che Estende GameLogic Il primo passo è creare una nuova classe che estenda GameLogic. Questa classe rappresenterà la nuova modalità di gioco

2. Implementare il Metodo playTurn() è responsabile della logica che si verifica ogni volta che viene giocato un turno. Può includere la creazione di nuovi turni, la raccolta di punteggi, e la transizione tra gli stati del gioco.

3. Implementare il Metodo isGameEnd() deve restituire un valore booleano che indica se il gioco è terminato. Questo viene determinato in base alla logica della modalità di gioco.

4. Implementare il Metodo GetScore() per calcolare il punteggio dell'utente in base a una metrica specifica (es. copertura del codice, prestazioni del robot, ecc.). Questo punteggio è spesso influenzato dal numero di round giocati o dalla performance nel turno.

5. Integrare la Nuova Modalità nel Sistema Dopo aver creato la nuova modalità di gioco, è necessario integrarla nel sistema. Puoi farlo registrando la nuova modalità all'interno di GameController o altre parti del sistema che gestiscono i tipi di gioco.

## Sequence Diagram: CreateGame

Questo diagramma descrive il processo attraverso cui un utente autenticato avvia una nuova partita. Dopo aver selezionato i parametri di gioco (classe e robot), il sistema invia le informazioni al backend, che crea la partita e ne memorizza i dettagli. Successivamente, viene restituito all'utente l'ID della partita appena creata.

<img src="_assets/media/diagrams/createGame_sd.jpg" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">