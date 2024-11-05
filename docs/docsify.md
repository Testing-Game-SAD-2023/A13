La documentazione è gestita tramite il tool **Docsify**. Il tool prevede il deployment di un sito tramite **GitHub Pages**, piattaforma messa disposizione da GitHub per registrare pagine da correlare a progetti o team di sviluppo.

Mentre il processo di deployment è completamente gestito da GitHub, Docsify fornisce una piattaforma già predisposta alla creazione di una documentazione tramite la creazione di pagine custom da inserire nella stessa repository del progetto da documentare. Questo permette di avere tutte le comodità relative a un sistema di integrazione, tra cui **versionamento** e **controllo delle modifiche**.

Ogni pagina del sito deve essere preferibilmente scritta in [Markdown](https://www.markdownguide.org/), per uniformarsi quanto più possibile al tema generale della documentazione. In Markdown è anche possibile integrare HTML.

Per il set-up di Docsify è possibile fare riferimento alla loro [pagina di documentazione](https://docsify.js.org/#/quickstart), tuttavia qui si fornirà una guida riassuntiva sia dell'installazione che delle pratiche di documentazione che sono state adottate.

## Set-up

Per utilizzare Docsify è consigliabile innanzitutto installare il gestore di pacchetti [npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm). In seguito sarà possibile installare **docsify-cli** tramite il seguente comando:

```bash
npm i docsify-cli -g
```

> **_NOTA:_**  non è **strettamente necessario** installare docsify-cli, ma è sicuramente un'ottima scelta, in quanto permette di avere una preview del sito **localmente**, senza dover effettuare delle commit su GitHub e attendere il deploy delle GitHub Pages.

Una volta installato, sarà possibile utilizzare Docsify nella cartella della repository clonata per ottenere una preview in tempo reale del sito:

```bash
docsify serve docs
```

Questo comando avvierà un server sulla porta 3000 (accessibile quindi all'indirizzo [localhost:3000](localhost:3000)) su cui sarà possibile vedere una preview delle modifiche in tempo reale del sito Docsify. Sarà quindi semplicemente necessario modificare le pagine tramite un editor di testo e salvarle per rendere le modifiche effettive.

Una volta terminate le modifiche, sarà possibile effettuare una push su GitHub e attendere il deployment per aggiungere il tutto al sito ufficiale della repository.

## Versioning

[Da decidere varie pratiche di versioning interne per tenere traccia delle modifiche ai vari paragrafi/capitoli/pagine]