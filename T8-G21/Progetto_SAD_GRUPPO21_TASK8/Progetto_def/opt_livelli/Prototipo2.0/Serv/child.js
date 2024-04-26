const http = require('http');

const server = http.createServer((req, res) => {

  console.log("(child.js) Creazione server HTTP in ascolto sulla porta 3081");

	if (req.url.startsWith('/api/')) {

    console.log("(child.js) Gestione richieste");

    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end('Richiesta ricevuta !');
    console.log("(child.js)  Richiesta ricevuta!");

    //Invio messaggio al processo padre per informarlo di aver ricevuto la richiesta
    process.send('Ho ricevuto la richiesta dal file bash');

    //Chiusura del processo
    process.exit();
    
  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    console.log("(child.js)  Pagina non trovata");
    res.end('Pagina non trovata');
  }
});

const port = 3081;
server.listen(port, () => {
  console.log(`(child.js)  Processo figlio in ascolto sulla porta ${port}`);
  console.log(`Elaborazione...`);
});



