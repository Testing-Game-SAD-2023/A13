# Installazione (Rest Server only)
Ci sono diverse modalità per installare l'applicazione. In particolare, in questa sezione si farà riferimento solo al rest-server.

## Docker from source
Questa modalità crea un'immagine docker dal codice sorgente. Una volta effettuato il `clone` della repository eseguire il comando:

```sh
make docker-build
```

L'immagine avrà il tag `game-repository:{git-commit}`

### Push su un docker registry
L'immagine create può essere salvata in un Docker registry. Una volta effettuato il login con `docker login`, può essere effettuato il push con comando:

```sh
make docker-push REGISTRY=<registry-name>
```
Questa azione effettua il push di due immagini, una con il tag latest e una con tag riferito al commit corrente. <br>

### Push con SSH (unix-like only)
**Attenzione**: questo comando utilizza le pipe tipiche delle shell unix e del programma *pv* (che mostra l'andamento dell'upload). Pertanto non è disponibile in shell Windows.

Per caricare l'immagine su un server SSH, bisogna assicurarsi che il server abbia un agente Docker installato correttamente. Poi eseguire il comando:

```sh
make docker-push-ssh SSH="10.10.0.1 -p1234"
```
## Binary from source

Una volta effettuato il `clone` della repository e installata la GO toolchain eseguire il comando:

```sh
make build
```

L'applicazione si troverà nella cartella `build`.

## Binary from release
Una volta individuati l'architettura e il sistema operativo su cui deve essere eseguita l'applicazione è possibile scaricare un pacchetto della versione rilasciata su Github. Ogni pacchetto (tar.gz per Linux/Mac e zip per Windows) contiene i seguenti file:

* **game-repository**: eseguibile dell'applicazione;
* **README.md**: file che punta a un'istanza demo e a questa documentazione;
* **LICENSE**: file di licenza;
* **index.yaml**: specifica OpenAPI;
* **GameRepositoryCollection**: collection da importare in Postman;
* **config.example.json**: esempio di configurazione;

Le release sono disponibili al seguente [link](https://github.com/alarmfox/game-repository/releases).

## Docker release
Insieme ad ogni release, un'immagine docker viene salvata sul registry: `capas/game-repository`. Per scaricare l'immagine, eseguire:
```sh
docker pull capas/game-repository
```
