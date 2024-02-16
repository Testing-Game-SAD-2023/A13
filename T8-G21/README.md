# T8-G21
Requisiti sull’Esecuzione del RobotEvosuite
1) Prima di iniziare bisogna eseguire lo script installazione.sh che contiene i software necessari, come Evosuite e Maven
2) Procedere con l'istallazione di Node.js
3) In opt/Prototipo2.0 sono presenti: i file .js per la creazione del server e uno script per la misurazione dei test scritti dall'utente
4) In opt_livelli sono presenti: lo script per la generazione dei test da parte di evosuite (robot_generazione.sh), il java che seleziona le iterazioni da utilizzare (generazione_livelli.java) e lo script per salvare le misurazioni e i test scelti nel repository comune (misurazione_livelli.sh)
5) Per avviare il server bisogna lanciare a linea di comando "node prova_esecuzione_parametri4.js"
6) Una volta avviato il server il formato della richiesta è il seguente : http://ip:porta/api/PERCORSO_CLASSE+PERCORSO_TEST+PERCORSO_SALVATAGGIO

**esempio** : http://localhost:3000/api/mnt/f/Desktop/repository/calcolatrice/calcolatrice.java+/mnt/f/Desktop/repository/test_calcolatrice/calcolatrice_test.java+/mnt/f/Desktop/repository/risultato

7) Per un corretto avvio della generazione dei test e dei livelli bisogna lanciare lo script robot_generazione.sh nel seguente modo : >bash robot_generazione.sh NOME_CLASSE NOME_PACKAGE PERCORSO_PACKAGE NUMERO_LIVELLI

**esempio**: >bash robot_generazione.sh calcolatrice calcolatrice mnt/f/Desktop/repository/calcolatrice 3

**NOTA BENE**: sono presenti anche i link ai file .tar dei container docker, è possibile quindi importarli bypassare le prime 4 fasi di installazione

**Link container Generazione_Livelli.tar**: https://communitystudentiunina-my.sharepoint.com/:u:/g/personal/t_didona_studenti_unina_it/ESAXc4ZfmB1Mj7lR20kvpaYBJxUFy6AFp0CM-PNPgQLIzA?e=mCf2KF

**Link container Misurazione_utente.tar**: https://communitystudentiunina-my.sharepoint.com/:u:/g/personal/t_didona_studenti_unina_it/EXqy4gpOoPtJooM2hh8_gp8Bz8FG_q4LhrTABScJ-E7m6A?e=wmfsgJ
