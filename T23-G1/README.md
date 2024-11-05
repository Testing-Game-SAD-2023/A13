<h1> Testing Game </h1>

<h2> Gruppo 1 </h2>
<li><p> Iole Morabito M63001448 i.morabito@studenti.unina.it </p> </li>
<li><p> Marika Sasso M63001438 marik.sasso@studenti.unina.it </p> </li>
<li><p> Francesca Terracciano M63001550 francesca.terracciano@studenti.unina.it </p> </li>
<li><p> Doriana Traetto M63001416 d.traetto@studenti.unina.it </p> </li>

<h2>  Task 2-3: Registrazione ed Autenticazione dei Giocatori </h2>

L'applicazione deve consentire agli studenti di registrarsi per poter conservare la storia delle attività svolte, oppure per accedere a requisiti di gioco più complessi. All'atto della registrazione, lo studente fornirà nome, cognome, un indirizzo e-mail valido ed una password, il sistema dopo aver controllato la validità dei dati forniti, aggiungerà il giocatore all'elenco dei giocatori registrati e gli assocerà un ID univoco. Sarebbe desiderabile raccogliere anche altre informazioni sugli studenti, come il corso di studi a cui sono iscritti (Bachelor, Master Degree, o altro).
All'atto della autenticazione, lo studente fornirà l'indirizzo e-mail fornito per la registrazione e la relativa password, il sistema dopo aver controllato la validità dei dati forniti, autenticherà il giocatore e gli fornirà una schermata per l'accesso alle funzionalità di gioco o di consultazione delle sessioni di gioco passate.

<h2> Deployment dell'applicazione </h2>

<li><p>Aprire Docker Desktop</p></li>
<li><p>Aprire un terminale da amministratore e posizionarsi sul percorso dove è contenuto il progetto
	<code>docker-compose up</code> viene utilizzato per avviare i servizi definiti in un file di configurazione <code>docker-compose.yml</code>. Viene creata l’immagine del container e viene eseguito il running.</p></li>
<li><p>Per effettuare le richieste, aprire da browser le pagine tramite i path in locale (http://localhost:8080/register, http://localhost:8080/login, ecc.) </p></li>
<li><p>Per visionare il popolamento delle tabelle del database, aprire un terminale ed eseguire i seguenti comandi:  
	<code>docker exec –it g1-t2t3-app-1 bash</code> viene utilizzato per entrare all'interno di un container Docker in esecuzione e avviare una shell interattiva al suo interno. </p></li>  
	<code>mysql –u root –p STUDENTSREPO</code>: viene utilizzato per accedere all'interfaccia della riga di comando di MySQL e connettersi al database <code>STUDENTSREPO</code> utilizzando l'utente <code>root</code> e richiedendo la password <code>password</code>. </p></li>
<li><p>Utilizzare i comandi SQL per la gestione delle tabelle (<code>SELECT</code>, <code>DROP</code>, <code>SHOW TABLES </code>, ecc.) </p></li>

