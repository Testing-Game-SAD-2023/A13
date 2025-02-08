In questa cartella ho riportato le classi che sono utilizzate come casi di prova durante lo sviluppo del gioco, fino a tutto il 2024

- Nella cartella test generati ci sono due esempi completi che possono essere utilizzati dall'interfaccia utente del back end.
Per entrambe le cartelle (Calcolatrice e VCardBean) è possibile trovare:
- il file sorgente della classe sotto test (Calcolatrice o VCardBean)
- il file zip dei test generati da Randoop
- il file zip dei test generati da Evosuite.

In maggior dettaglio, nello zip dei test generati da Randoop troviamo uno o più cartelle che identificano i livelli di difficoltà (01Level, 02Level, 03Level).
In ognuna di tali cartelle sono presenti i soli sorgenti delle classi di test generate (ad esempio RegressionTest_it0_livello1.java) e il file xml generato da JaCoCo relativamente alla copertura aggregata e di dettaglio del complesso delle classi di test elencate.
NON sono presenti dati relativi alla qualità dei test misurati da EvoSuite (si ricorda che EvoSuite ha sia una funzionalità di generazione dei casi di test che una funzionalità di misurazione della qualità di casi di test esistenti a seguito della loro esecuzione nel contesto delle librerie di EvoSuite).

Nello zip dei test generati da EvoSuite troviamo ancorauno o più cartelle che identificano i livelli di difficoltà (01Level, 02Level, 03Level).
In ognuna di tali cartelle sono presenti i soli sorgenti delle classi di test generate, nella sottocartella TestSourceCode/evosuite-tests (ad esempio Calcolatrice_ESTest.java e Calcolatrice_ESTest_scaffolding.java).
In aggiunta, è presente il file statistics.csv con i dati di qualità dei test misurati da EvoSuite stesso.
NON sono presenti dati relativi alla copertura misurata da JaCoCo.

- Nella cartella Tests sono riportati alcuni test di prova, utili per verificare il corretto funzionamento del front-end

- Nella cartella RobotStudentAndSource sono riportati, per 15 classi diverse:
    - il codice sorgente della classe sotto test
    - i test generati da Randoop (con le limitazioni già viste in precedenza);
    - i test generati da EvoSuite (con le limitazioni già viste in precedenza);
    - alcuni test realizzati da studenti che hanno utilizzato in passato queste classi. Di questi test è disponibile solo il sorgente, senza alcun supporto di copertura.

Per alcune classi sono presenti ulteriori test prodotti da studenti di Software Testing del 2024.
Per i test della classe FontInfo sono disponibili codice sorgente organizzato in progetto Eclipse, con cartelle separate src e test e coverage misurata da JaCoCo.

L'obiettivo richiesto nelle successive evoluzioni dello strumento (2025) è quello di poter avere a disposizione per ogni possibile robot:
- una struttura dei test possibilmente equivalente e che possa essere rieseguita (quindi non soltanto il codice sorgente della/e classi sotto test ma l'intero progetto di riferimento con librerie, pom.xml e suddivisione dei sorgenti in cartelle);
- gli output di copertura di dettaglio creati da JaCoCo (formato xml vanno bene);
- gli output di valutazione della qualità dei test misurati da Evosuite (statistics.csv va bene).

La scelta di questi output è allineata con gli output che si possono generare per i test scritti dall'utente tramite il front-end, in modo che lo stesso front-end non abbia difficoltà a confrontare le coperture e la qualità dei test provenienti da qualsiasi fonte e possa quindi realizzare qualsiasi tipo di valutazione utile per gamification.