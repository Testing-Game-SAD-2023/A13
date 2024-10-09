# T5 - User Experience

*<b>Versione:</b> A13.2*

Il task T5 si occupa di fornire un'interfaccia utilizzabile da un giocatore per poter configurare e avviare partite, ma anche quello di ottenere informazioni sintetiche riguardo il suo storico partite (e quello di altri player) così come il progresso dei vari [achievement](/game/achievements).

## Specifiche dei requisiti

Il servizio permette ai player correttamente
autenticati di: 

a. accedere all’area riservata di
**selezione dei parametri di gioco** dove poter
visualizzare le classi ed i robot disponibili
precedentemente caricati dagli
amministratori *(A13.1)*;

b. **avviare una partita** accedendo
all’arena di gioco vero e proprio *(A13.1)*;

c. **visualizzare i profili utente** tramite apposite pagine che racchiudono informazioni sintetiche sullo storico di gioco degli utenti *(A13.2)*;

d. **aggiornare lo stato delle statistiche** degli utenti ogni volta che viene salvato lo stato di una partita *(A13.2)*.

[Use Cases Diagram]

## Pagina del Profilo

La **pagine del profilo** è stata pensata per fornire delle informazioni sommarie su un player iscritto all'applicazione. Si tratta di un'interfaccia suddivisa in tre sezioni:

* **Informazioni personali:** contiene lo username, nome e cognome dell'utente e attualmente un *mock* di biografia e immagine del profilo dell'utente.

* **Statistiche:** contiene tutte le [statistiche](/game/achievements) configurate dagli utenti amministratore, insieme al valore di progresso attuale per l'utente che si sta visualizzando.

* **Trofei:** contiene tutti gli achievement configurati dagli amministratori, insieme ai loro valori di progresso per l'utente che si sta visualizzando.

## Achievement Service

Il servizio **Achievement Service** è una classe che mette a disposizione una serie di metodi utili all'aggiornamento di statistiche e prelievo di progressi degli achievement.

### updateProgressByPlayer

```java
public List<AchievementProgress> updateProgressByPlayer(int playerID);
```

Questo metodo si occupa di eseguire un aggiornamento dei progressi di un giocatore per tutte le statistiche configurate dagli amministratori.

Nel caso in cui questo aggiornamento comporti il raggiungimento di qualche achievement, questo metodo ritorna la lista degli achievement ottenuti.

[Sequence Diagram]

### getProgressesByPlayer

```java
public List<AchievementProgress> getProgressesByPlayer(int playerID)
```

Questo metodo restituisce, per ogni achievement configurato, il relativo progresso di uno specifico giocatore.

[Sequence Diagram]

### getStatisticsByPlayer

```java
public List<StatisticProgress> getStatisticsByPlayer(int playerID)
```

Questo metodo restituisce, per ogni statistica configurata, il relativo progresso di uno specifico giocatore.

[Sequence Diagram]

### getStatistics

TODO

### getAchievements

TODO

