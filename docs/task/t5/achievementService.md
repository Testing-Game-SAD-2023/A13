Il servizio **Achievement Service** è una classe che mette a disposizione una serie di metodi utili all'aggiornamento di statistiche e prelievo di progressi degli achievement.

```java
@Service
public class AchievementService {
    public List<AchievementProgress> updateProgressByPlayer(int playerID);

    private List<Statistic> getStatistics();

    public List<AchievementProgress> getProgressesByPlayer(int playerID);
    public List<StatisticProgress> getStatisticsByPlayer(int playerID);
}
```

## updateProgressByPlayer

```java
public List<AchievementProgress> updateProgressByPlayer(int playerID);
```

Questo metodo si occupa di eseguire un aggiornamento dei progressi di un giocatore per tutte le statistiche configurate dagli amministratori.

Nel caso in cui questo aggiornamento comporti il raggiungimento di qualche achievement, questo metodo ritorna la lista degli achievement ottenuti.

<img src="_assets/media/diagrams/updateProgressByPlayer_sd.png" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">

## getProgressesByPlayer

```java
public List<AchievementProgress> getProgressesByPlayer(int playerID);
```

Questo metodo restituisce, per ogni achievement configurato, il relativo progresso di uno specifico giocatore.

<img src="_assets/media/diagrams/getProgressesByPlayer_sd.png" style="width: 30vw; display: block; margin-left: auto; margin-right: auto;">

## getStatisticsByPlayer

```java
public List<StatisticProgress> getStatisticsByPlayer(int playerID);
```

Questo metodo restituisce, per ogni statistica configurata, il relativo progresso di uno specifico giocatore. È una semplice __GET__ sulle API di  [T4](/task/t4/).

## getStatistics

```java
public List<Statistic> getStatistics();
```

Questo metodo restituisce ogni statistica configurata. È una semplice __GET__ sulle API di  [T1](/task/t1/).