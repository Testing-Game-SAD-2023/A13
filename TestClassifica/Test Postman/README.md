Questa cartella contiene i test della API '/leaderboard/subInterval' per il T4 e per il T5. Tutti i test sono stati effettuati con Postman attraverso lo strumento Collection runner.

1. La cartella 'TabelleDBTest' contiene le INSERT da effettuare nel T23 e nel T4 per inizializzare le tabelle Students (T23) e PlayerStats (T4) in modo da stabilire le pre-condizioni dei test
    - Per connettersi al database MySQL in T23 eseguire nel container:
    mysql --user=root --password=password
    use studentsrepo;
    - Per connettersi al database Postgres in T4 eseguire nel container:
    psql -U postgres -d postgres
    - Nota: le INSERT in T23 partono dall'ID 2 in quanto il giocatore con ID 1 viene creato all'atto della prima registrazione

2. La cartella 'Postman collections' contiene i JSON relativi alle collections da importare in Postman per effettuare i test. Questi JSON sono già comprensivi delle requests parametrizzate e di Post-request scripts che validano l'output ottenuto rispetto a quello atteso da ciascun test

3. La cartella 'Test cases' contiene i file JSON relativi ai casi di test per il T4 e per il T5 da caricare nel Collection runner di Postman

4. La cartella 'Test results' contiene i file JSON con i risultati dei test eseguiti
