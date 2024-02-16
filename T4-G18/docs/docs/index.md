# Introduzione
Il componente realizza una API REST per gestire le informazioni che riguardano le partite. In particolare, è composta da:

* Rest server: componente principale dell'applicazione realizzato dal team;
* Prometheus: permette l'estrazione delle metriche dal rest-server con un sistema di monitoraggio periodico;
* Grafana: elabora le metriche estratte da Prometheus per la creazione di dashboard grafiche per misurare le performance dell'applicazione;
* Postgres: database relazionale necessario al funzionanmento dell'applicazione;

Ad eccezione del rest-server e di postgres, tutti gli altri componenti sono opzionali.


## Struttura progetto
Il progetto segue presenta 3 cartelle principali:

* **model**: in cui ci sono le strutture che servono per interagire con il database;
* **api**: in cui per ogni entità (Game, Round ecc) sono implementati gli endpoint HTTP con le relative operazioni sul database;
* **postman**: in cui si trovano la specifica OpenAPI (`postman/schemas/index.yaml`) e la collection Postman `postman/collections/GameRepositoryCollection.json`;

L'architettura dell'applicazione è mostrata di seguito:

![Architettura](images/arch.png)
## Dipendenze
In questa sezione, viene spiegato in dettaglio come è stato sviluppato il componente rest-server. Il rest-server è un'applicazione GO ed è dotata di un `Makefile` per gestire il processo di sviluppo e compilazione. Per ottenere la lista di tutti i `target` disponibili:

```sh
make help

Usage:

  build              builds the application in "build" directory
  run                runs the application in "build/game-repository"
  dev                executes the application with hot reload
  dev-dependecies    installs development dependencies
  docker-build       builds a docker image
  docker-run         runs a docker container. Needs "config" argument (i.e make docker-run config=$(pwd)/config.json)
  docker-push-ssh    sends the image on a server with ssh (i.e make docker-push-ssh SSH="10.10.1.1 -p1234")
  docker-push        sends the image on a registry (i.e make docker-push REGISTRY=<registry_name>)
  test               executes all unit tests in the repository. Use COVER_DIR=<PATH> to enable coverage. (i.e make test COVER_DIR=$(pwd)/coverage)
  test-race          executes all unit tests with a race detector. Takes longer
  test-integration   executes all tests. If CI is set, DB_URI can be used to set database URL, otherwis a docker container is used (i.e make test-integration CI=1 DB_URI=db-url COVER_DIR=/some/path)
  clean              remove build files
  help               prints this help message
```

### Installazione toolchain GO
Dato che Go è dotato di un runtime multipiattaforma, per installare la toolchain per il proprio sistema basta navigare sul [sito ufficiale](https://go.dev/dl/), scaricare la versione adatta al proprio sistema e seguire le istruzioni. In particolare, la versione utilizzata per sviluppare il progetto è la `1.20.4`.

#### Dipendenze di progetto
Visto che l'applicazione usa il `vendoring` delle dipendenze non è necessario installare alcuna libreria. Per eseguire il testing dell'applicazione è necessario avere a disposizione `docker`.

### Configurazione
L'applicazione deve essere configurata con un file in formato `json` il cui path deve essere passato con l'argomento `--config=<PATH>`. Il comportamento default è quello di cercare un file `config.json` all'interno della directory corrente. I valori di default della configurazione sono riportati nel file `config.example.json`.

```json
{
    "postgresUrl": "",
    "listenAddress": "localhost:3000",
    "apiPrefix": "/",
    "dataPath": "data",
    "enableSwagger": false,
    "rateLimiting": {
        "enabled": false,
        "burst": 4,
        "maxRate": 2
    },
    "authentication": {
        "enabled": false,
        "headerKey": "Authorization",
        "authEndpoint": "http://auth-service/auth",
        "method": "POST"
    }
}

```
Prima di eseguire l'applicazione è necessario creare un file di configurazione funzionante. In particolare, tutti i parametri specificati sopra sono opzionale ad eccezione di postgresUrl. Quindi, assicurandosi di avere un'istanza Postgres funzionante, bisogna creare un file di configurazione `config.json` nella cartella principale del progetto con il seguente contentuto:


```json title="config.json"
{
    "postgresUrl": "<POSTGRES_URL>",
}
```

Ad esempio:


```json title="config.json"
{
    "postgresUrl": "postgres://postgres:postgres@localhost:5432/postgres?sslmode=disable",
}
```

### Compilazione ed esecuzione
L'applicazione può essere compilata con:
```sh
make build
```

Mentre per eseguirla:

```sh
make run
```

Questo comando esegue l'applicazione cercando il file di configurazione nella cartella corrente. Per specificare un path custom:

```sh
make run CONFIG=<PATH_TO_CONFIG>
```

### Compilazione ed esecuzione con Docker
Per compilare un'immagine Docker:

```sh
make docker-build
```

Mentre per eseguirla:

```sh
make docker-run CONFIG=<PATH_TO_CONFIG>
```


### Testing
L'applicazione prevede due tipi di testing:

* Unit testing: non hanno bisogno di un database;
* Integration testing: utilizza un database reale;

Per effettuare unit testing:
```sh
make test
```

Per effettuare integration testing, bisogna specificare l'indirizzo di un database:

* CI=1 se il flag `CI` è settato, allora in `DB_URI` bisogna inserire l'indirizzo del database;
* CI non settato: viene creato un container usa e getta con Docker;

Entrambe le versioni dei comandi hanno bisogno dell'argomento `COVER_DIR` necessario al calcolo delle metriche di copertura. Quindi un comando completo per il test di integrazione sarà:

```sh
make test-integration CI=1 DB_URI=postgres://postgres:postgres@localhost:5432/postgres?sslmode=disable COVER_DIR=$(pwd)/coverage
```

Oppure se si preferisce utilizzare un database usa e getta:

```sh
make test-integration COVER_DIR=$(pwd)/coverage
```
