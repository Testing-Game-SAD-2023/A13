# Achievment

Gli **Achievement**, nella **gamification**, sono obiettivi o traguardi che i giocatori raggiungono completando determinate attività o sfide all'interno di un sistema di gioco o di un'applicazione. Questi riconoscimenti, che possono essere virtuali (come medaglie, badge o punti), servono a incentivare il coinvolgimento e la motivazione degli utenti, offrendo una gratificazione immediata. Gli achievement non solo premiano il progresso, ma incoraggiano anche la competizione e la collaborazione tra i partecipanti, aumentando l'interazione e la fidelizzazione all'interno di un contesto ludico o educativo.

## Gamification

[...]

## Struttura tecnica

Il punto centrale della gestione degli Achievement è una **statistica**. Una statistica è un elemento che caratterizza il progresso dei giocatori in determinate circostanze del gioco. Ad esempio, una statistica potrebbe riferirsi al numero di partite vinte in una determinata modalità di gioco, e contro un determinato robot.

Ad ogni achievement è associata una statistica, a cui afferirà nel momento in cui si dovrà valutare se è stato ottenuto o meno da parte di un giocatore.

Una statistica è caratterizzata da tre proprietà:

* **Modalità di gioco (Game Mode)**, ad esempio: "numero di partite giocate in modalità _Scalata_".
* **Ruolo (Role)**, ovvero il particolare valore a cui si riferisce la statistica come _Punteggio_, _Partite giocate_ o _Partite vinte_.
* **Robot**, ovvero l'eventuale Robot a cui può essere associata la statistica, ad esempio: "numero di partite giocate contro _Randoop_".

Attraverso queste proprietà è possibile calcolare il valore di una statistica, data una lista di giochi da cui calcolarla. A questo scopo è previsto un oggetto associato alla statistica tramite **Pattern Strategy** che si occupa di implementare la strategia di calcolo a seconda delle proprietà sopra descritte.

Infine, l'associazione tra la statistica e il suo valore di progresso relativo a un giocatore, è realizzata attraverso una classe associativa **Statistic Progress**.

## Class Diagram

<img src="_assets/media/diagrams/achievements_cd.png" style="width: 50vw; display: block; margin-left: auto; margin-right: auto;">
