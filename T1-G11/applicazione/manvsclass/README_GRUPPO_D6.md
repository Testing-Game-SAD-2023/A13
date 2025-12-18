# README_GRUPPO_D6

## 0) Descrizione del Task svolto e Id del Task

**ID Task:** TaskR2

**Descrizione:** Questa versione del progetto implementa quanto segue:

- **Funzionalità Suggerimenti** (**FS**): gestione suggerimenti da parte di un amministratore;
- **Migrazione Database** (**MD**): migrazione del database NoSQL associato al microservizio *Admin Service* verso uno SQL.

La nuova versione del progetto consente ad un amministratore di inserire dei suggerimenti, che possono essere di carattere generico oppure associati ad una class UT, mediante un file di tipo JSON. Inoltre, l'amministratore può visualizzare l'elenco dei suggerimenti inseriti, suddivisi tra le due categorie "generici" e "di classe". Può visualizzare il dettaglio di un suggerimento e modificarlo. Può eliminare i suggerimenti inseriti.

Il microservizio *Admin Service* gestisce la persistenza di classi UT, robot, amministratori, player ed altre entità utili per il gioco. Nella nuova versione del progetto, la persistenza viene effettuata non più su un database NoSQL, **MongoDB**, ma piuttosto su un database SQL, **PostgreSQL**.

---

## 1) Implementazione del task

### Descrizione ad alto livello FS

La Funzionalità Suggerimenti è stata implementata nel progetto modificando il microservizio T1. In particolare, per consentire ad un utente amministratore di gestire i suggerimenti sono state sviluppate ed esposte le seguenti API:

- **CREATE**:
  * **Metodo e Endpoint**: `POST /hints/upload`
  * **Descrizione**: Permette l'inserimento di nuovi suggerimenti tramite caricamento di un file JSON e, opzionalmente, delle immagini associate.
  * **Input (Multipart)**:
    * `file`: File `.json` contenente l'array dei suggerimenti da caricare.
    * `images`: Lista opzionale di file immagine referenziati all'interno del JSON.
  * **Struttura JSON attesa**:
    ```json
    [
      {
        "type": "GENERIC",
        "name": "Titolo del suggerimento generico",
        "content": "Testo del contenuto...",
        "imageUri": "nome_immagine.png"
      },
      {
        "type": "CLASS",
        "classUTName": "NomeClasse.java",
        "name": "Titolo del suggerimento specifico",
        "content": "Testo del contenuto per la classe...",
        "imageUri": ""
      }
    ]
    ```

- **READ**:
  * **Metodo e Endpoint**: `GET /hints`
  * **Descrizione**: Restituisce la lista dei suggerimenti presenti nel sistema. L'API supporta filtri dinamici tramite *Query Parameters* (es. `?type=GENERIC` o `?classUTName=NomeClasse.java`) per raffinare la ricerca.

- **UPDATE**:
  * **Modifica Contenuto**: `PUT /hints/update/{id}`
    * Consente di modificare i dettagli di un suggerimento esistente. Accetta dati in formato `multipart/form-data` per aggiornare titolo (`name`), testo (`content`) ed eventualmente sostituire l'immagine (`file`).
  * **Ordinamento**: `POST /hints/{id}/move`
    * Permette di cambiare l'ordine di visualizzazione di un suggerimento rispetto agli altri della stessa categoria. Richiede il parametro `direction` (valori ammessi: "UP", "DOWN").

- **DELETE**: Sono state implementate tre modalità di cancellazione per garantire flessibilità nella gestione:
  1. **Per Classe**: `DELETE /hints/{classUTName}`
     * Elimina massivamente tutti i suggerimenti associati a una specifica classe UT.
  2. **Singolo Suggerimento**: `DELETE /hints/className/{classUTName}/order/{order}`
     * Elimina un singolo suggerimento identificato univocamente dalla combinazione di nome classe e numero d'ordine (per i suggerimenti generici, usare `null` come nome classe).
  3. **Per Tipo**: `DELETE /hints/type/{type}`
     * Elimina massivamente tutti i suggerimenti appartenenti a una determinata categoria (es. tutti i "GENERIC" o tutti i "CLASS").

- **UTILITY**:
  * **Metodo e Endpoint**: `GET /hints/template`
  * **Descrizione**: Permette di scaricare un file JSON precompilato (template) che mostra la struttura corretta da utilizzare per l'upload dei suggerimenti.


### Descrizione ad alto livello MD

La Migrazione Database è stata implementata nel progetto procedendo per passi.

- **Scelta Database**: La scelta del database relazionale è ricaduta su **PostgreSQL** per i seguenti
motivi:
  * Si tratta di un database relazionale orientato agli oggetti (ORDBMS),
pertanto risulta più semplice l’interfacciamento con il back-end, orientato
agli oggetti.
  * L’insieme dei tipi di dati è esteso, includendo un supporto JSON più avanzato.
  * Presenta un sistema di vincoli tra le tabelle più robusto, garantendo
l’integrità dei dati e le proprietà ACID per le transazioni.
  * PostgreSQL è considerato il database più avanzato, robusto e conforme a
livello tecnico, ideale per carichi di lavoro complessi e integrità dei dati.

  La scelta è stata effettuata anche ragionando in un’ottica di robustezza più a
  lungo termine. Il progetto in questione è in continua evoluzione, sia in termini
  di funzionalità aggiuntive sia in termini di modifica delle funzionalità esistenti.
  PostgreSQL anche se richiede un utilizzo più elevato di memoria e presenta una
  velocità di lettura leggermente più lenta rispetto ad altri database, consente
  di gestire carichi di lavoro più complessi e garantisce un’integrità dei dati più
  robusta.

- **Definizione Database**: la struttura del database è stata definita a partire dal database prensente su MongoDB, introducendo in questo caso i vincoli di integrità tra le varie tabelle.

- **Implementazione Database**: l'approccio utilizzato per implementare il database è stato di tipo *code-first*, ovvero la definizione dello schema relazionale è avvenuta mediante la modellazione di classi Java (Entity). Nello specifico, l'implementazione ha previsto:
  * **Mapping ORM (Object-Relational Mapping)**: Sono state create le classi Java annotate con le specifiche **JPA** (Java Persistence API) per rappresentare le tabelle del database. Le relazioni tra le entità sono state formalizzate utilizzando annotazioni come `@OneToMany`, `@ManyToOne` e `@JoinColumn`, delegando al framework **Hibernate** la gestione delle query SQL.
  * **Refactoring del Data Layer**: La persistenza dei dati è stata migrata sostituendo l'interfaccia `MongoRepository` con `JpaRepository`.
  * **Versionamento dello Schema (Flyway)**: Per garantire la consistenza della struttura del database tra i vari ambienti di sviluppo e produzione, è stato integrato **Flyway**. L'utilizzo di script di migrazione SQL versionati permette di tracciare l'evoluzione del database e di applicare automaticamente le modifiche strutturali ad ogni avvio del microservizio.


### Elenco delle principali modifiche ai microservizi
| Microservizio Modificato | Tipo di Modifica | Eventuali Nuove Tecnologie usate |
| :--- | :--- | :--- |
| T1 | Migrazione **database** da MongoDB a PostgreSQL | PostgreSQL |
| T1 | Modifica database per aggiungere la **tabella** *hints* dei suggerimenti | - |
| T1 | Tutte le entity sono state spostate sotto il **package** */model/entity* | - |
| T1 | Tutte le **entity** preesistenti sono state modificate per essere adattate al database sottostante | JPA |
| T1 | Tutti i **repository** sono stati modificati per estendere l'interfaccia JpaRepository  | JPA |
| T1 | Tutti i **service** preesistenti sono stati modificati e trasformati in interfacce | - |
| T1 | Per ciascuna interfaccia service è stata creata una **classe di implementazione**, usando la notazione *NomeClasseServiceImpl.java*, all'interno del package */service/implementation* | - |
| T1 | Tutti i **controller** preesistenti sono stati modificati per riadattarli a entity e service ridefiniti | - |
| T1 | È stata creata la nuova **entity** HintEntity | - |
| T1 | È stato creato il nuovo **repository** HintRepository | - |
| T1 | Sono stati creati i **DTO** Hint, HintResponse e HintUpdateDto | - |
| T1 | È stato creato un **enum** HintTypeEnum | - |
| T1 | È stato aggiunto il **package** *mapper* | - |
| T1 | È stato creato il **mapper** HintMapper per convertire entity in DTO e vicevera | MupStruct |
| T1 | È stata creata l'**interfaccia** HintService | - |
| T1 | È stata creata la **classe** HintServiceImpl che implementa l'interfaccia HintService | - |
| T1 | È stato creato il **controller** HintController | - |
| T1 | È stata creata la **classe di utility** HintSpecification per filtrare i dati sul database | - |
| T1 | È stata creata la **classe di configurazione** JpaConfig per la configurazione del database Postgres | - |
| T1 | È stata creata la **classe di configurazione** WebConfig | - |
| T1 | È stata creata la **classe di configurazione** WebSecurityConfig | - |
| T1 | È stata creata la **classe di configurazione** FlywayConfig per la configurazione Di Flyway | - |
| T1 | È stata creata la **view** HintViewController | - |
| T1 | Sono stati modificati i file dashboard.html, dashboard.js per aggiungere la card relativa ai suggerimenti | - |
| T1 | È stata aggiunta la directory *resources/static/t1/js/hint* per raccogliere i file javascript relativi ai suggerimenti | - |
| T1 | Sono stati creati i file hint_main.js, hint_upload_js per gestire lato frontend le logiche di visualizzazione, inserimento ed eliminazione dei suggerimenti | - |
| T1 | È stata aggiunta la directory *resources/templates/hint* per raccogliere i file html relativi ai suggerimenti | - |
| T1 | Sono stati creati i file hint_main.html, hint_upload.html | - |
| T1 | Sono stati modificati i file message.properties, message_en.properties e message_it.properties per introdurre i nuovi messaggi di internazionalizzazione | - |
| T1 | È stato modificato il file application.properties per aggiungere le properties di relative al nuovo database | - |
| T1 | È stato aggiornato il file .env con le nuove variabili d'ambiente | - |
| T1 | È stato aggiornato il file pom.xml per aggiungere le dipendenze relative a Postgres, MapStruct, Flyway | - |


---

## 2) Integrazioni effettuate con altri gruppi

Nessuna integrazione specifica richiesta.

---

## 3) Errori/problematiche non risolte nel progetto consegnato

Non sono stati rilevati errori o problematiche non risolte. 