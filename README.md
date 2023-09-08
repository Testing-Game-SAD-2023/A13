# GUIDA ALL'INSTALLAZIONE

## PASSO 1
Si deve avviare lo script "installer.bat". Saranno effettuate le seguenti operazioni:
1) creazione della rete "global-network" comune a tutti i container
2) creazione del volume "VolumeT9" comune ai Task 1 e 9
3) installazione di ogni singolo container

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