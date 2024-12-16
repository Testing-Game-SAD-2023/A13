5-Rivedere correttezza del trigger sulla tabella (probabilmente è necessario utilizzare on UPDATE anziché on Insert)
7.EndGame per la gestione della vittoria: serve una callRestPut con application content Json, aggiorando anche i test sul method atteso da EndGame



Fatto:
1-Fatto a meno della possibile estensione
3-Avendo configurato la rotta nell'API gateway dovrebbe essere lui a gestire l'autenticazione
4-Implementare, all'interno di T5, logica per ottenere email degli utenti utilizzando T23
6.Spostare l'UI della leaderboard in un fragment da imporatare nella navbar
9.Disabilitare bottoni UI relativi a modalità e statistiche non implementate
10.Implementare la ricerca di un giocatore in leaderboard
8.Gestire le lingue nella leaderboard
11.Implementare bottone refresh leaderboard
12.Limitare grandezza della cache leaderboard