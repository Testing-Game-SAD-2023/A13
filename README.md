Il nostro team ha lavorato su due task fondamentali per lo sviluppo del front-end del sistema di gioco: Task 5 e Task 6. 
Il Task 5 riguarda l'interfaccia per l'avvio di una partita, consentendo ai giocatori autenticati di accedere all'area riservata, selezionare i parametri di gioco (come classi e robot caricati dagli amministratori) e avviare una partita nell'arena di gioco. 
Il Task 6, invece, si occupa dell'interfaccia per giocare una partita, offrendo ai giocatori un editor di test case per scrivere codice Java, richiedere la compilazione, visualizzare i risultati di copertura e confrontare i propri risultati con quelli generati dal robot, decretando così il vincitore.

L'obiettivo iniziale era unificare i due task in un unico front-end. Tuttavia, la scarsa modularità e la complessità del codice esistente ci hanno portato a effettuare un completo refactoring del sistema per migliorarne la manutenibilità e l'efficienza. Abbiamo quindi ristrutturato l'applicazione introducendo tre package principali:

- **Interface**: gestisce l'interazione con i servizi REST, incapsulando completamente la logica di rete e fornendo un dispatcher centralizzato, il Service Manager, per semplificare la gestione delle chiamate.
- **Component**: si occupa della costruzione delle pagine web, basate su componenti modulari che separano chiaramente la logica di business dai dati da visualizzare. I componenti sono disponibili in versioni generiche e integrate con il Service Manager per facilitare l'interoperabilità tra i vari task. In questo modo, è possibile definire una pagina come un insieme di componenti logici (che quindi rappresentano requisiti, prerequisiti o controlli di varia natura) e di componenti oggetto, che rappresentano i dati da inserire nella vista.
- **Game**: gestisce la logica di gioco, con un controller REST dedicato alla gestione della concorrenza e delle partite attive. Fornisce inoltre una classe GameLogic che consente la definizione delle diverse modalità di gioco.

Oltre alla ristrutturazione del codice, sono stati introdotti diversi miglioramenti, tra cui:
- Un completo **rifacimento grafico** per migliorare l'esperienza utente,
- L'introduzione di un sistema di **logging**, che era totalmente assente in entrambi i task, per tracciare meglio le attività e risolvere eventuali problemi,
- L'introduzione di un'**architettura** per garantire maggiore flessibilità, manutenibilità e modularità, 
- Il **refactoring** del codice JavaScript alla base del front-end,
- La risoluzione di bug presenti in vari task, non solo T5 e T6.

Infine, abbiamo creato una **wiki** dettagliata che offre una guida completa su come utilizzare e sfruttare al meglio gli strumenti e le funzionalità del sistema.
