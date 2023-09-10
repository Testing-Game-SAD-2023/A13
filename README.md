# Task 10 - Gruppo 40
Componenti:
- Pasquale Riello - M63/1516
- Lorenzo Pannone - M63/1492

# GUIDA ALL'INSTALLAZIONE

## PASSO 1
Si deve avviare lo script "installer.bat". Saranno effettuate le seguenti operazioni:
1) creazione della rete "global-network" comune a tutti i container
2) creazione del volume "VolumeT9" comune ai Task 1 e 9
3) installazione di ogni singolo container

NOTA: il container relativo al Task 9 ("Progetto-SAD-G19-master") si sospenderà autonomamente dopo l'avvio. Esso viene utilizzato solo per "popolare" il volume "VolumeT9" condiviso con il Task 1.

## PASSO 2
Si deve configurare il container "manvsclass-mongo_db-1" così come descritto anche nella documentazione del Task 1.
Per fare ciò bisogna fare le seguenti operazioni:
1) posizionarsi all'interno del terminale del container
2) digitare il comando "mongosh"
3) digitare i seguenti comandi:

        use manvsclass
        db.createCollection("ClassUT");
        db.createCollection("interaction");
        db.createCollection("Admin");
        db.createCollection("Operation");
        db.ClassUT.createIndex({ difficulty: 1 })
        db.Interaction.createIndex({ name: "text", type: 1 })
        db.interaction.createIndex({ name: "text" })
        db.Admin.createIndex({username: 1})

## PASSO 3
L'intera applicazione è adesso pienamente configurata e raggiungibile sulla porta :80.

# VIDEO DIMOSTRAZIONE
## Studente


https://github.com/Testing-Game-SAD-2023/T10-G40/assets/33938788/c6cd06c7-5d3a-4988-a651-7e1dc5896909



## Admin

https://github.com/Testing-Game-SAD-2023/T10-G40/assets/33938788/da3e5d04-7106-40ae-a0f3-1b7d6923c0cd
