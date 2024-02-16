# Gruppo A7-2024
Componenti:
- Simone Rinaldi - M63/1654
- Lorenza Pezzullo - M63/1671
- Giuseppe Imparato - M63/
- Giada Ottaiano - M63/1640
  
# Guida all'installazione

Per installare correttamente l'applicazione, seguire attentamente i seguenti passaggi:

1. Scaricare Docker Desktop dal seguente [link](https://www.docker.com/products/docker-desktop/).

    Nel caso non sia la prima installazione, è necessario effettuare la disinstallazione, quindi utilizzare `uninstaller.bat` mentre si ha in esecuzione Docker: in questo modo si elimina qualunque file presente su Docker. In caso di errore "136 Docker desktop - unexpected wsl error" sarà necessario eseguire il comando `wsl --shutdown` nel terminale ed eseguire il riavvio. Se l'errore persiste, allora è consigliabile installare o aggiornare WSL all'ultima versione con il comando `wsl --install` (o con `wsl --update`).

2. Una volta scaricata la cartella del progetto, avviare lo script `installer.bat` su Windows. Su MacOS è necessario eseguire il comando `chmod +x installermac.sh` nella cartella in cui è presente il file `installermac.sh` e poi eseguire `./installermac.sh` per avviarlo.

3. Alla fine dell'installazione si avrà:
    - la creazione della rete global-network comune a tutti i container;
    - la creazione dei volumi VolumeT8 e VolumeT9 per i task 1-8 e 1-9, rispettivamente;
    - la creazione dei singoli container nell'applicazione Docker desktop.
    - la configurazione del container *manvsclass-mongo db-1*; in particolare, lo script esegue in automatico le seguenti operazioni:
        - `use manvsclass`
        - `db.createCollection(ClassUT)`
        - `db.createCollection(interaction)`
        - `db.createCollection(Admin)`
        - `db.createCollection(Operation)`
        - `db.ClassUT.createIndex( difficulty: 1 )`
        - `db.Interaction.createIndex( name: text, type: 1 )`
        - `db.interaction.createIndex( name: text )`
        - `db.Admin.createIndex(username: 1)`

Per ulteriori dettagli sull'utilizzo dell'applicazione, si prega di fare riferimento alla sezione seguente.

---

# Guida all'utilizzo

Una volta installata correttamente l'applicazione, seguire le istruzioni seguenti:

## Avvio dei container

Per utilizzare l'applicazione, è necessario avviare tutti i container ad eccezione di `ui gateway`, che dovrà essere avviato per ultimo. L'applicazione è raggiungibile sulla porta `:80`.

## Esposizione dell'applicazione su un indirizzo pubblico

Per esporre l'applicazione su un indirizzo pubblico, si rimanda alla documentazione dei colleghi del gruppo A10 ragggiungibile al seguente [link](https://github.com/Testing-Game-SAD-2023/A10-2024).

---

# Guida alle modifiche al codice

Se è necessario modificare il codice dell'applicazione, seguire attentamente i seguenti passaggi:

1. Recarsi nell'applicazione Docker.
2. Aprire la sezione relativa ai containers.
3. Selezionare tutti i containers relativi ai file modificati.
4. Effettuare la delete di tali containers.
5. Aprire la sezione images.
6. Selezionare le immagini relative ai file modificati.
7. Effettuare l'eliminazione di tali immagini.
8. Recarsi sull'IDE utilizzato ed aprire il terminale integrato della cartella in cui è presente il file `pom.xml` relativo ai file modificati.
9. Eseguire il comando `mvn clean package`.
10. Fare clic sul tasto destro sul file `docker-compose.yml` e selezionare "compose up".
11. Riavviare i container.

---

# Problematiche di utilizzo

Di seguito sono elencate alcune delle problematiche riscontrate durante l'utilizzo dell'applicazione al fine di agevolarne l'utilizzo per i gruppi successivi.

## Cache del browser

Disattivando la cache del browser, è possibile garantire che ogni modifica apportata al codice sorgente o alle risorse dell'applicazione sia immediatamente visualizzata nel browser. La cache del browser può causare problemi di compatibilità e disattivarla semplifica il processo di debug, consentendo agli sviluppatori di identificare e risolvere rapidamente i bug senza dover preoccuparsi di eventuali caching persistenti che potrebbero mascherare il problema.

## Versione Docker

Durante lo sviluppo dell'applicazione, è emersa una problematica legata alla versione di Docker. È stato osservato che nelle versioni più recenti di Docker, dopo una serie di disinstallazioni e reinstallazioni dell'applicazione, potrebbero verificarsi problemi durante il download quando si avvia il file `installer.bat`. Nel caso in cui si riscontrino errori come quello mostrato nella figura seguente, potrebbe essere necessario disinstallare Docker e utilizzare una versione non successiva alla `4.25.1` evitando così errori nella costruzione dei container.

![Errore Installer.bat](Media/Foto/ErrorInstaller.png)


## Porte già in uso

Durante l'avvio dell'applicazione, potrebbe verificarsi un problema in cui alcuni container non si avviano correttamente, mostrando un messaggio di errore relativo alla porta già in uso. Un esempio comune potrebbe riguardare il container T4, poiché la porta 3000 è comunemente utilizzata dal processo `MySQL80`. In questo caso, è necessario aprire la schermata dei servizi attivi su Windows per individuare e terminare il processo che sta attualmente utilizzando la porta in questione. In alternativa, è possibile risolvere questo problema modificando la porta del container in questione per evitare conflitti.

## 502 Bad Gateway

Occasionalmente, anche dopo che tutti i container sono stati avviati con successo, la pagina web potrebbe visualizzare un messaggio di errore del tipo `502 Bad Gateway`. Una possibile ragione dietro questo tipo di errore potrebbe essere correlata alla rapidità con cui si tenta di accedere all'applicazione web subito dopo il riavvio dei container. In questi casi, la piattaforma potrebbe richiedere del tempo per completare il processo di avvio e stabilire la connessione corretta tra i vari componenti dell'applicazione. Attendere alcuni istanti prima di tentare di accedere all'applicazione può spesso risolvere il problema. Tuttavia, se l'errore persiste nonostante l'attesa, è consigliabile effettuare il riavvio di Docker.

# Video Dimostrativi

Di seguito riportiamo i video dimostrativi delle due modalità di gioco attualmente disponibili.

## Funzionamento Modalità "Sfida un Robot"

https://github.com/SimoneRinaldi02/A7-2024-ver2/assets/115701124/89f519e5-796b-4883-8826-681a0980f16e

## Funzionamento Modalità "Allenamento"

https://github.com/SimoneRinaldi02/A7-2024-ver2/assets/115701124/f99ea16d-caf9-451f-9604-99f5ff2b0960





