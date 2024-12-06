1-Implementare, all'interno di T5, logica per ottenere email degli utenti utilizzando T23
2-Rivedere gestione delle pagine del frontend (decidere se prima posizione da 0 o da 1)
3-Valutare se è necessario utilizzare jwt all'interno della rotta getPositions del T5
4-Implementare logica di selezione corretta delle risorse all'interno del T4 (switch-case)
5-Rivedere correttezza del trigger sulla tabella (probabilmente è necessario utilizzare on UPDATE anziché on Insert)


Estensioni possibili dei precedenti punti 

1-Valutare se è opportuno utilizzare più classi per la gestione della rotta getSubInterval, in quanto al momento si sta ritornando anche userID nel frontend


Fatto:
1-Fatto a meno della possibile estensione
3-Avendo configurato la rotta nell'API gateway dovrebbe essere lui a gestire l'autenticazione
