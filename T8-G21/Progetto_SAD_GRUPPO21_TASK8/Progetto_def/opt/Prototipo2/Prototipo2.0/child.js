const http = require('http');

const server = http.createServer((req, res) => {
	if (req.url.startsWith('/api/')) {
  res.writeHead(200, { 'Content-Type': 'text/plain' });
  res.end('Richiesta ricevuta !');
  process.send('Ho ricevuto la richiesta dal file bash');
  process.exit();
  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Pagina non trovata');
  }
});

const port = 3001;
server.listen(port, () => {
  console.log(`Processo figlio in ascolto sulla porta ${port}`);
  console.log(`Elaborazione...`);
});



